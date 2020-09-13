package spring.jsonfiledb.bus;

import spring.jsonfiledb.pojo.IPojo;
import spring.jsonfiledb.repo.IRepo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Scanner;

/*
5. Создать клиентский класс с методом main, в котором в виде консольного диалога пользователю предлагается:
создать задачу, изменить содержимое задачи, вывести список задач на экран, вывести конкретную задачу, удалить задачу
*/
public class ConsoleBus implements IBus {

    private Class pojoClazz;
    private Constructor<IPojo> pojoConstructor;
    IRepo repo;

    public void start(IPojo pojo, IRepo repo) {
        try {
            pojoClazz = pojo.getClass();
            pojoConstructor = pojoClazz.getConstructor();
            this.repo = repo;

            //System.out.println("type '/' to exit");

            try (Scanner in = new Scanner(System.in)) {
                while (selectOperation(in)) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean selectOperation(Scanner in) throws InterruptedException {
        System.out.print("Choose operation (0-exit,1-create,2-update,3-delete,4-get,5-all): ");
        String o = in.nextLine().trim().substring(0, 1);
        try {
            switch (o) {
                case "/":
                case "0":
                    return false;
                case "1":
                    return doCreate(in);
                case "2": return doUpdate(in);
                case "3": return doDelete(in);
                case "4": return getById(in)!=null;
                case "5": return getAll(in);
            }
        } catch (Exception e) {
            System.out.println("Error happen: " + e.getMessage());
        }
        return false;
    }

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

    private boolean doDelete(Scanner in) {
        IPojo instance = getById(in);
        if(instance==null)
            return false;
        repo.delete(instance.getId());
        System.out.println(instance+" deleted");
        return true;
    }

    private boolean doUpdate(Scanner in) throws IllegalAccessException {
        IPojo instance = getById(in);
        if(instance==null)
            return false;
        Field[] fields = pojoClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class type = field.getType();
            String typeName = type.getSimpleName();
            System.out.print("Enter field '"+ typeName + " " + field.getName()+"'='"+field.get(instance)+"' new value: ");
            String line = in.nextLine().trim();
            if(line!=null && !line.isEmpty()) {
                if (type.equals(String.class)) {
                    field.set(instance, line);
                } else if (typeName.equals("int")) {
                    field.set(instance, Integer.valueOf(line));
                }
            }
        }
        repo.save(instance);
        System.out.println(instance+" saved");
        return true;
    }

    private IPojo getById(Scanner in) {
        System.out.print("Enter ID to get object: ");
        String line = in.nextLine().trim();
        IPojo got = repo.get(line);
        if(got!=null)
            System.out.println(got);
        else
            System.out.println("ID='"+line+"' not found");
        return got;
    }

    private boolean doCreate(Scanner in) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        IPojo instance = pojoConstructor.newInstance();
        Field[] fields = pojoClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class type = field.getType();
            String typeName = type.getSimpleName();
            System.out.print("Enter field '"+ typeName + " " + field.getName()+"' value: ");
            String line = in.nextLine().trim();
            if(type.equals(String.class)){
                field.set(instance, line);
            } else if(typeName.equals("int")){
                field.set(instance, Integer.valueOf(line));
            }
        }
        repo.save(instance);
        System.out.println(instance+" saved");
        return true;
    }

}