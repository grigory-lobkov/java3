package spring.calculator2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.calculator2.bus.IBus;
import spring.calculator2.repo.ICalculator;

/* 3 Домашняя работа
0. Переписать задания первого и второго урока с XML-конфигурации на Java Base конфигурацию

Создать два класса реализующих интерфейс ICalculator.
Первый простой SimpleCalculator, который не учитывает переполнение переменной,
второй продвинутый AdvancedCalculator, в случае переполнения, выбрасывающий
исключительную ситуацию new RuntimeException(“Overflow”).
Добавить управляющую бизнес-логику. Возможность ввода операции и двух числе с клавиатуры

public interface ICalculator {
    int sum(int a, int b);
    int diff(int a, int b);
    int div(int a, int b);
    int mult(int a, int b);
}
*/

public class App {

    static public ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

    static public ICalculator calculator = context.getBean(ICalculator.class);


    public static void main(String[] args) {

        // запуск программы
        context.getBean(IBus.class).start(calculator);

    }

}
