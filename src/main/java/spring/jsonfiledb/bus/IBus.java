package spring.jsonfiledb.bus;

import spring.calculator.repo.ICalculator;
import spring.jsonfiledb.pojo.IPojo;
import spring.jsonfiledb.repo.IRepo;

public interface IBus {

    void start(IPojo pojo, IRepo repo);

}
