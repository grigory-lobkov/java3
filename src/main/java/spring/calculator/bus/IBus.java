package spring.calculator.bus;

import spring.calculator.repo.ICalculator;

public interface IBus {

    void start(ICalculator calculator);

}
