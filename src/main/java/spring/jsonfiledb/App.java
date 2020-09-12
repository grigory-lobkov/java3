package spring.jsonfiledb;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.jsonfiledb.db.IDb;
import spring.jsonfiledb.pojo.IPojo;
import spring.jsonfiledb.pojo.Task;


/* 1.2 Домашняя работа

1. Создать класс FileTaskRepository -  который выполняет CRUD операции над задачами
и реализует интерфейс TaskRepository.

public interface TaskRepository {
void save(TaskImpl task);
void update(TaskImpl task);
void delete(String id);
List<TaskImpl> get();
TaskImpl get(String id);
}

2. Данные по задачам должны храниться в файле в операционной системе. Объект файла выглядит следующим образом:

public class TaskImpl {
    private String id;
    private String description;
    private String author;
    private String name;
    private int storyPoint;
}

3. Методы интерфейса TaskRepository должны реализовывать запись, модификацию и удаление задач из файла

4. Создать бин на основе класса реализующего интерфейс TaskRepository

5. Создать клиентский класс с методом main, в котором в виде консольного диалога пользователю предлагается:
создать задачу, изменить содержимое задачи, вывести список задач на экран, вывести конкретную задачу, удалить задачу
*/

public class App {

    static BeanFactory oldcontext;
    static ApplicationContext context;

    static public IPojo pojo;
    static public IDb input;


    static {
        context = new ClassPathXmlApplicationContext("jsonfiledb-context.xml");
        pojo = context.getBean(IPojo.class);
        input = context.getBean(IDb.class);
    }


    public static void main(String[] args) throws Exception {
        input.init("example.json", pojo.getClass());

        Task task1 = new Task();
        task1.setId("1");
        task1.setAuthor("Gregory");
        task1.setDescription("test task 1");
        task1.setName("Task 1");
        task1.setStoryPoint(1);

        Task task2 = new Task();
        task2.setId("2");
        task2.setAuthor("Gregory Lobkov");
        task2.setDescription("test \n task 2");
        task2.setName("Task 2");
        task2.setStoryPoint(2);

        input.save(task1);
        input.save(task2);

        System.out.println("1: "+input.get("1"));
        System.out.println("2: "+input.get("2"));

        task1.setName("Task 111111111111");
        task2.setDescription("2222222222222222222222222");

        input.save(task1);
        input.save(task2);

        System.out.println("1: "+input.get("1"));
        System.out.println("2: "+input.get("2"));
    }

}
