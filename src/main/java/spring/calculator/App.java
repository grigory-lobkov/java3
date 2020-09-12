package spring.calculator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.calculator.business.IInput;
import spring.calculator.repository.ICalculator;

/* 1.1. Практическая работа

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

    static BeanFactory oldcontext;
    static ApplicationContext context;

    //static ICalculator calculator=new SimpleCalculator();
    static public ICalculator calculator;
    static public IInput input;


    static {
        // old-simple-style, deprecated
        //oldcontext = new XmlBeanFactory(new ClassPathResource("context.xml"));

        // modern-style, инициализация контекста занимает 500мс
        context = new ClassPathXmlApplicationContext("calculator-context.xml");

        // создание бинов
        calculator = context.getBean(ICalculator.class);
        input = context.getBean(IInput.class);
    }


    public static void main(String[] args) {

        // запуск программы
        input.start(calculator);

    }

}
