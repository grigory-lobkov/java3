package spring.jsonfiledb2.bus;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import spring.jsonfiledb2.pojo.IPojo;
import spring.jsonfiledb2.repo.IRepo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

/**
 * Класс позволяет работать с POJO объектами в виде консольного диалога:
 * - создать обхект,
 * - изменить содержимое объекта,
 * - вывести список на экран,
 * - вывести конкретный объект,
 * - удалить один объект
 *
 * Для запуска потребуется передать класс POJO объекта и репозиторий
 *
 * При возникновении любой ошибки, взаимодействие остановится
 */
@Controller
public class ConsoleBus implements IBus {

    private Class pojoClazz;
    private Constructor<IPojo> pojoConstructor;

    IRepo repo;

    public void setPojoClass(Class pojoClazz) {
        this.pojoClazz = pojoClazz;
        try {
            pojoConstructor = pojoClazz.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setRepo(IRepo repo) {
        this.repo = repo;
    }

    /**
     * Запуск опроса консоли
     */
    @Override
    public void start() {
        try (Scanner in = new Scanner(System.in)) {
            while (selectOperation(in)) {
            }
        }
    }

    /**
     * Выбрать операцию - главное меню
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     */
    private boolean selectOperation(Scanner in) {
        System.out.print("\nChoose operation (0-exit,1-create,2-update,3-delete,4-get,5-all): ");
        String o = in.nextLine().trim().substring(0, 1);
        System.out.println();
        try {
            switch (o) {
                case "/":
                case "0":
                    return false;
                case "1":
                    return doCreate(in);
                case "2":
                    return doUpdate(in);
                case "3":
                    return doDelete(in);
                case "4":
                    return getById(in) != null;
                case "5":
                    return getAll(in);
            }
        } catch (Exception e) {
            System.out.println("Error happen: " + e.getMessage());
        }
        return false;
    }

    /**
     * Получить список всех объектов из хранилища
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     */
    private boolean getAll(Scanner in) {
        List<IPojo> all = repo.get();
        if (all == null)
            return false;
        for (IPojo p : all)
            System.out.println(p);
        if (all.size() == 0)
            System.out.println("(list is empty)");
        return true;
    }

    /**
     * Удаление объекта
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     */
    private boolean doDelete(Scanner in) {
        IPojo instance = getById(in);
        if (instance == null)
            return false;
        repo.delete(instance.getId());
        System.out.println(instance + " deleted");
        return true;
    }

    /**
     * Обновление свойств объекта (кроме id)
     * Поддерживаются типы: String, int
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     * @throws IllegalAccessException если не получится считать или записать свойство объекта
     */
    private boolean doUpdate(Scanner in) throws IllegalAccessException {
        IPojo instance = getById(in);
        if (instance == null)
            return false;
        Field[] fields = pojoClazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equals("id")) {
                field.setAccessible(true);
                String fieldValue = field.get(instance).toString();
                Class type = field.getType();
                String typeName = type.getSimpleName();
                System.out.print("Enter field '" + typeName + " " + fieldName + "'='" + fieldValue + "' new value: ");
                String line = in.nextLine().trim();
                if (line != null && !line.isEmpty()) {
                    if (type.equals(String.class)) {
                        field.set(instance, line);
                    } else if (typeName.equals("int")) {
                        field.set(instance, Integer.valueOf(line));
                    }
                }
            }
        }
        repo.update(instance);
        System.out.println(instance + " saved");
        return true;
    }

    /**
     * Получить объект по идентификатору
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     */
    private IPojo getById(Scanner in) {
        System.out.print("Enter ID to get object: ");
        String line = in.nextLine().trim();
        IPojo got = repo.get(line);
        if (got != null)
            System.out.println(got);
        else
            System.out.println("ID='" + line + "' not found");
        return got;
    }

    /**
     * Создать новый объект и записать его в хранилище
     * Поддерживаются типы: String, int
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     * @throws Exception если не удастся создать экземпляр объекта, либо обновить его свойства
     */
    private boolean doCreate(Scanner in) throws Exception {
        IPojo instance = pojoConstructor.newInstance();
        Field[] fields = pojoClazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Class type = field.getType();
            String typeName = type.getSimpleName();
            System.out.print("Enter field '" + typeName + " " + fieldName + "' value: ");
            String line = in.nextLine().trim();
            if (line != null && !line.isEmpty()) {
                field.setAccessible(true);
                if (type.equals(String.class)) {
                    field.set(instance, line);
                } else if (typeName.equals("int")) {
                    field.set(instance, Integer.valueOf(line));
                }
            }
        }
        repo.save(instance);
        System.out.println(instance + " saved");
        return true;
    }

}