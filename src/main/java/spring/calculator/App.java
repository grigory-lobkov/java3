package spring.calculator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.calculator.business.IInput;
import spring.calculator.repository.ICalculator;

public class App {

    static BeanFactory oldcontext;
    static ApplicationContext context;
    //static ICalculator calculator=new SimpleCalculator();
    static ICalculator calculator;
    static IInput input;

    static {
        //oldcontext = new XmlBeanFactory(new ClassPathResource("context.xml"));
        context = new ClassPathXmlApplicationContext("context.xml");
        calculator = context.getBean(ICalculator.class);
        input = context.getBean(IInput.class);
    }

    public static void main(String[] args) {
        input.start(calculator);
    }

}
