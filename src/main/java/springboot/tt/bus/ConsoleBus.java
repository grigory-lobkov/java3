package springboot.tt.bus;

import springboot.tt.entity.IEntity;
import springboot.tt.store.IStore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Класс позволяет работать с POJO сущностями (Entity) в виде консольного диалога:
 * - создать обхект,
 * - изменить содержимое объекта,
 * - вывести список на экран,
 * - вывести конкретный объект,
 * - удалить один объект
 *
 * Для запуска потребуется передать пример POJO ущности (Entity) и инициализованный репозиторий
 *
 * При возникновении любой ошибки, взаимодействие остановится
 */
public class ConsoleBus implements IBus {

    private Class entityClazz;
    private Constructor<IEntity> entityConstructor;
    IStore store;

    public void setEntityClass(Class entityClazz) {
        this.entityClazz = entityClazz;
        try {
            entityConstructor = entityClazz.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setStore(IStore store) {
        this.store = store;
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
            e.printStackTrace();
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
        List<IEntity> all = store.get();
        if (all == null)
            return false;
        for (IEntity p : all)
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
        IEntity instance = getById(in);
        if (instance == null)
            return false;
        store.delete(instance.getId());
        System.out.println(instance + " deleted");
        return true;
    }

    /**
     * Обновление свойств объекта (кроме id)
     * Поддерживаются типы: String, BigDecimal, int, long
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     * @throws IllegalAccessException если не получится считать или записать свойство объекта
     */
    private boolean doUpdate(Scanner in) throws IllegalAccessException {
        IEntity instance = getById(in);
        if (instance == null)
            return false;
        Field[] fields = entityClazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equals("id")) {
                field.setAccessible(true);
                Object val = field.get(instance);
                String fieldValue = val==null?null:val.toString();
                Class type = field.getType();
                String typeName = type.getSimpleName();
                System.out.print("Enter field '" + typeName + " " + fieldName + "'='" + fieldValue + "' new value: ");
                String line = in.nextLine().trim();
                if (line != null && !line.isEmpty()) {
                    if (type.equals(String.class)) {
                        field.set(instance, line);
                    } else if (type.equals(BigDecimal.class)) {
                        field.set(instance, new BigDecimal(line));
                    } else if (typeName.equals("long")) {
                        field.set(instance, Long.valueOf(line));
                    } else if (typeName.equals("int")) {
                        field.set(instance, Integer.valueOf(line));
                    }
                }
            }
        }
        store.update(instance);
        System.out.println(instance + " saved");
        return true;
    }

    /**
     * Получить объект по идентификатору
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     */
    private IEntity getById(Scanner in) {
        System.out.print("Enter ID to get object: ");
        String line = in.nextLine().trim();
        IEntity got = store.get(line);
        if (got != null)
            System.out.println(got);
        else
            System.out.println("ID='" + line + "' not found");
        return got;
    }

    /**
     * Создать новый объект и записать его в хранилище
     * Поддерживаются типы: String, BigDecimal, int, long
     *
     * @param in проинициализированный сканер консоли
     * @return {@code true}, если можно продолжать
     * @throws Exception если не удастся создать экземпляр объекта, либо обновить его свойства
     */
    private boolean doCreate(Scanner in) throws Exception {
        IEntity instance = entityConstructor.newInstance();
        Field[] fields = entityClazz.getDeclaredFields();
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
                } if (type.equals(BigDecimal.class)) {
                    field.set(instance, new BigDecimal(line));
                } else if (typeName.equals("long")) {
                    field.set(instance, Long.valueOf(line));
                } else if (typeName.equals("int")) {
                    field.set(instance, Integer.valueOf(line));
                }
            }
        }
        store.create(instance);
        System.out.println(instance + " saved");
        return true;
    }

}