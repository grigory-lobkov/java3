package spring.jsonfiledb.repo;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonReader;
import spring.jsonfiledb.pojo.IPojo;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Хранение POJO объектов в формате JSON в файле построчно
 *
 * Сравнение JSON библиотек
 * https://github.com/fabienrenaud/java-json-benchmark
 * Пример работы с DSL-JSON
 * https://github.com/ngs-doo/dsl-json/blob/master/examples/MavenJava8/src/main/java/com/dslplatform/maven/Example.java
 */
//FileTaskRepository
public class JsonFileRepo implements IRepo {

    /**
     * Имя файла-хранилища данных
     */
    private String fileName;

    /**
     * Указатель на актуальный файл
     */
    private File file;

    /**
     * Блокировщик всех записей в файл
     */
    private Lock lockWrite = new ReentrantLock();

    /**
     * Блокировщик чтения, используется на последнем этапе записи в файл
     */
    private ReadWriteLock lockRead = new ReentrantReadWriteLock();

    /**
     * Класс POJO объекта
     */
    private Class pojoClazz;
    /**
     * Конструктор POJO объекта
     */
    private Constructor<IPojo> pojoClazzConstructor;

    /**
     * JSON сериализатор
     */
    private DslJson<Object> json;

    /**
     * Инициализация хранилища
     *
     * @param params два параметра: имя файла хранилища и класс хранимого объекта
     * @throws NoSuchMethodException в случае отсутствия конструктор POJO объекта (используется конструктор без параметров)
     * @throws IllegalAccessException в случае проблемы создания экземпляра объекта
     * @throws InvocationTargetException в случае проблемы создания экземпляра объекта
     * @throws InstantiationException в случае проблемы создания экземпляра объекта
     * @throws IOException в случае неудачи создания файла
     */
    @Override
    public void init(Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        if (params.length != 2)
            throw new RuntimeException("You have to use init(String fileName, Class pojoClazz);");

        if (!(params[0] instanceof String))
            throw new RuntimeException("params[0] is not String");
        fileName = (String) params[0];

        pojoClazz = (Class) params[1];
        pojoClazzConstructor = pojoClazz.getConstructor();
        IPojo instance = pojoClazzConstructor.newInstance();
        if (!(instance instanceof IPojo))
            throw new RuntimeException("params[1] is not IPojo: " + pojoClazz.getName());

        file = new File(fileName);
        if (!file.exists())
            file.createNewFile();
        json = new DslJson<>();
    }

    /**
     * Завершение работы с хранилищем
     * Фактически - ожидание завершения записей и чтения - занимаем locks
     */
    @Override
    public void destroy() {
        lockWrite.lock();
        lockRead.writeLock().lock();
    }

    /**
     * Сериализация POJO объекта - преобразование в строку
     *
     * @param pojo объект
     * @return строка JSON
     * @throws IOException проблема с сериализацией
     */
    private String pojoToString(IPojo pojo) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        json.serialize(pojo, os);
        return os.toString("UTF-8");
    }

    /**
     * Сохранение нового POJO объекта
     * без проверки на существование идентификатора - быстрое добавление в конец хранилища
     *
     * @param pojo добавляемый объект
     */
    public void save(IPojo pojo) {
        try {
            String newLine = pojoToString(pojo);

            lockWrite.lock();
            lockRead.writeLock().lock();
            // лочим чтение и добавляем новую строчку в конец файла
            try (FileWriter fileWriter = new FileWriter(file, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

                printWriter.println(newLine);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lockRead.writeLock().unlock();
                lockWrite.unlock();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновление объекта в хранилище
     *
     * Идентификатор объекта уже должен быть в хранилище
     *
     * @param pojo обновленный объект
     * @throws NoSuchElementException если не смог найти объект в хранилище
     */
    public void update(IPojo pojo) {
        update(pojo, false, true);
    }

    /**
     * Обновление объекта в хранилище (для внутреннего использования)
     * Параметрами можно определить различное поведение
     *
     * @param pojo объект для обновления
     * @param addIfNotFound {@code true} добавляем в случае отсутствия идентификатора,
     *                      {@code false} оставляем хранилище без изменений, если не нашли объект изменения в хранилище
     * @param throwIfNotFound {@code true} если нужно сгенерировать ошибку при осутствии объекта в храниилище
     *                                    - метод не добавит объект даже если {@code addIfNotFound = true}
     * @throws NoSuchElementException если не смог найти объект в хранилище (когда {@code throwIfNotFound = true})
     */
    private void update(IPojo pojo, boolean addIfNotFound, boolean throwIfNotFound) {
        lockWrite.lock();
        try {
            IPojo instance = pojoClazzConstructor.newInstance();
            String id = pojo.getId();
            String newLine = pojoToString(pojo);
            boolean found = false;
            // предпологается создать клон основного файла, а только потом лочить чтение, чтобы переименовать файлы
            File file2 = new File(fileName + "_tmp");
            try (FileWriter fileWriter = new FileWriter(file2);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter);
                 FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                //читаем основной файл и тут же заполняем временный
                String line = bufferedReader.readLine();
                while (line != null) {
                    if (!found) {
                        lineToInstance(line, instance);
                        if (id.equals(instance.getId())) {
                            line = newLine;
                            found = true;
                        }
                    }
                    printWriter.println(line);
                    line = bufferedReader.readLine();
                }
                //если такой строчки не было, добавляем в конец
                if (!found && addIfNotFound) {
                    printWriter.println(newLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!found) {
                if (throwIfNotFound) {
                    file2.delete();
                    throw new NoSuchElementException("Cannot find id='" + id + "'");
                }
                if (!addIfNotFound) {
                    file2.delete();
                    return;
                }
            }

            // лочим чтение и подменяем на обновленный файл
            replaceFile(file, file2);

        } catch (IOException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            lockWrite.unlock();
        }
    }

    /**
     * Десериализация объекта из строки JSON в POJO-класс
     *
     * @param line строка, содержащая объект POJO в виде JSON
     * @param instance объект для сохранения свойств
     * @throws UnsupportedEncodingException в случае плохого формата строки {@code line}
     * @throws IOException в случае ошибок при установке свойств объекта {@code instance}
     */
    private void lineToInstance(String line, IPojo instance) throws IOException {
        byte[] bytes = line.getBytes("UTF-8");
        JsonReader<Object> reader = json.newReader().process(bytes, bytes.length);
        reader.next(pojoClazz, pojoClazz.cast(instance));
    }

    /**
     * Удаление объекта из хранилища
     *
     * @param id идентификатор удаляемого объекта
     * @throws NoSuchElementException если не смог найти объект в хранилище
     */
    public void delete(String id) {
        delete(id, true);
    }


    /**
     * Удаление объекта из хранилища (для внутреннего использования)
     * Параметрами можно определить различное поведение
     *
     * @param id идентификатор удаляемого объекта
     * @param throwIfNotFound {@code true} если нужно сгенерировать ошибку при осутствии объекта в храниилище
     * @throws NoSuchElementException если не смог найти объект в хранилище (когда {@code throwIfNotFound = true})
     */
    public void delete(String id, boolean throwIfNotFound) {
        lockWrite.lock();
        try {
            IPojo instance = pojoClazzConstructor.newInstance();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            boolean found = false;
            // предпологается создать клон основного файла, а только потом лочить чтение, чтобы переименовать файлы
            File file2 = new File(fileName + "_tmp");
            try (FileWriter fileWriter = new FileWriter(file2);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter);
                 FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    if (found) {
                        printWriter.println(line);
                    } else {
                        lineToInstance(line, instance);
                        if (id.equals(instance.getId())) {
                            found = true;
                        } else {
                            printWriter.println(line);
                        }
                    }
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (found) {
                // лочим чтение и подменяем на обновленный файл
                replaceFile(file, file2);
            } else {
                file2.delete();
                if (throwIfNotFound)
                    throw new NoSuchElementException("Cannot find id='" + id + "'");
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            lockWrite.unlock();
        }
    }

    /**
     * Остановить чтение и подменить один файл другим
     *
     * @param original какой файл заменяем
     * @param newOne новый файл хранилища
     */
    private void replaceFile(File original, File newOne) {
        lockRead.writeLock().lock();
        try {
            // лочим чтение и подменяем на обновленный файл
            if (original.delete()) {
                if (!newOne.renameTo(original)) {
                    newOne.delete();
                    throw new RuntimeException("File is broken. Cannot rename temp file to original one.");
                }
            } else {
                newOne.delete();
                throw new RuntimeException("Cannot replace original file.");
            }
        } finally {
            lockRead.writeLock().unlock();
        }
    }

    /**
     * Получить список всех записей из хранилища
     *
     * @param <T> тип объектов списка (тип POJO объекта)
     * @return список всех POJO-объектов из хранилища
     */
    public <T> List<T> get() {
        List<Object> result = new ArrayList<>();
        lockRead.readLock().lock();
        try {
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    IPojo instance = pojoClazzConstructor.newInstance();
                    lineToInstance(line, instance);
                    result.add(instance);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            lockRead.readLock().unlock();
        }
        return (List<T>) result;
    }

    /**
     * Получить объект из хранилища
     *
     * @param id идентификатор POJO-объекта
     * @return POJO-объект
     */
    public IPojo get(String id) {
        lockRead.readLock().lock();
        try {
            IPojo instance = pojoClazzConstructor.newInstance();
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    lineToInstance(line, instance);
                    if (id.equals(instance.getId())) return instance;
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            lockRead.readLock().unlock();
        }
        return null;
    }

}