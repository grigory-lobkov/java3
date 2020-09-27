package spring.bankomat2.bus;

import spring.bankomat2.entity.IEntity;
import spring.bankomat2.store.IStore;

/**
 * Интерфейс-шина для связи сервиса с внешним миром
 */
public interface IBus {

    /**
     * Установить связь
     */
    void start();

}
