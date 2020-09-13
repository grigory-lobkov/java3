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

/*
Сравнение JSON библиотек
https://github.com/fabienrenaud/java-json-benchmark
Пример работы с DSL-JSON
https://github.com/ngs-doo/dsl-json/blob/master/examples/MavenJava8/src/main/java/com/dslplatform/maven/Example.java
*/
//FileTaskRepository
public class JsonFileRepo implements IRepo {

    private String fileName;

    private File file;

    private Lock lockWrite = new ReentrantLock();
    private ReadWriteLock lockRead = new ReentrantReadWriteLock();
    private Class clazz;
    private Constructor<IPojo> clazzConstructor;

    private DslJson<Object> json;

    @Override
    public void init(Object... params) throws Exception {
        if(params.length!=2)
            throw new RuntimeException("You have to use init(String fileName, Class clazz);");

        if(!(params[0] instanceof String))
            throw new RuntimeException("params[0] is not String");
        fileName = (String) params[0];

        clazz = (Class)params[1];
        clazzConstructor = clazz.getConstructor();
        IPojo instance = clazzConstructor.newInstance();
        if(!(instance instanceof IPojo))
            throw new RuntimeException("params[1] is not IPojo: "+clazz.getName());

        file = new File(fileName);
        if(!file.exists())
            file.createNewFile();
        json = new DslJson<>();
    }

    @Override
    public void destroy() {
        lockWrite.lock();
        lockRead.writeLock().lock();
    }

    private String pojoToString(IPojo pojo) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        json.serialize(pojo, os);
        return os.toString("UTF-8");
    }

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

    public void update(IPojo pojo) {
        update(pojo, false, true);
    }

    public void update(IPojo pojo, boolean addIfNotFound, boolean throwIfNotFound) {
        lockWrite.lock();
        try {
            IPojo instance = clazzConstructor.newInstance();
            String id = pojo.getId();
            String newLine = pojoToString(pojo);
            boolean found = false;
            // предпологается создать клон основного файла, а только потом лочить чтение, чтобы переименовать файлы
            File file2 = new File(fileName+"_tmp");
            try (FileWriter fileWriter = new FileWriter(file2);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter);
                 FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                //читаем основной файл и тут же заполняем временный
                String line = bufferedReader.readLine();
                while (line != null) {
                    if(!found) {
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
                if(!found && addIfNotFound) {
                    printWriter.println(newLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!found) {
                if(throwIfNotFound) {
                    file2.delete();
                    throw new NoSuchElementException("Cannot find id='"+id+"'");
                }
                if(!addIfNotFound) {
                    file2.delete();
                    return;
                }
            }
                /*//пишем монопольно один объект
                try (FileWriter fileWriter = new FileWriter(file);
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    json.serialize(pojo, os);
                    bufferedWriter.write(os.toString("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            lockRead.writeLock().lock();
            // лочим чтение и подменяем на обновленный файл
            try {
                replaceFile(file, file2);
            } finally {
                lockRead.writeLock().unlock();
            }
        } catch (IOException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            lockWrite.unlock();
        }
    }

    private void lineToInstance(String line, IPojo instance) throws IOException {
        byte[] bytes = line.getBytes("UTF-8");
        JsonReader<Object> reader = json.newReader().process(bytes, bytes.length);
        reader.next(clazz, clazz.cast(instance));
    }

    public void delete(String id) {
        delete(id, true);
    }

    public void delete(String id, boolean throwIfNotFound) {
        lockWrite.lock();
        try {
            IPojo instance = clazzConstructor.newInstance();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            boolean found = false;
            // предпологается создать клон основного файла, а только потом лочить чтение, чтобы переименовать файлы
            File file2 = new File(fileName+"_tmp");
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
            if(found) {
                lockRead.writeLock().lock();
                // лочим чтение и подменяем на обновленный файл
                try {
                    replaceFile(file, file2);
                } finally {
                    lockRead.writeLock().unlock();
                }
            } else {
                file2.delete();
                if(throwIfNotFound)
                    throw new NoSuchElementException("Cannot find id='"+id+"'");
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            lockWrite.unlock();
        }
    }

    private void replaceFile(File original, File newOne){
        if(original.delete()){
            if(!newOne.renameTo(original)) {
                newOne.delete();
                throw new RuntimeException("File is broken. Cannot rename temp file to original one.");
            }
        } else {
            newOne.delete();
            throw new RuntimeException("Cannot replace original file.");
        }
    }

    public <T> List<T> get() {
        List<Object> result = new ArrayList<>();
        lockRead.readLock().lock();
        try {
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    IPojo instance = clazzConstructor.newInstance();
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

    public IPojo get(String id) {
        lockRead.readLock().lock();
        try {
            IPojo instance = clazzConstructor.newInstance();
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
