package spring.jsonfiledb2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.config.IntervalTask;
import spring.jsonfiledb2.bus.IBus;
import spring.jsonfiledb2.pojo.IPojo;
import spring.jsonfiledb2.pojo.Task;
import spring.jsonfiledb2.repo.IRepo;


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

    /**
     * Инициализация контекста приложения
     */
    static public ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

    /**
     * Точка входа в приложение
     *
     * Управление таблицей POJO-объектов в файле через консоль
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // запуск программы
        context.getBean(IBus.class).start();

    }

}
