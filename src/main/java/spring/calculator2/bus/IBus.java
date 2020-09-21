package spring.calculator2.bus;

import spring.calculator2.repo.ICalculator;

public interface IBus {

    void start(ICalculator calculator);

}
