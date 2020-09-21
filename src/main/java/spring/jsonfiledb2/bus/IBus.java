package spring.jsonfiledb2.bus;

import spring.jsonfiledb2.pojo.IPojo;
import spring.jsonfiledb2.repo.IRepo;

/**
 * Интерфейс-шина для связи сервиса с внешним миром
 */
public interface IBus {

    /**
     * Установить связь
     */
    void start();

}
