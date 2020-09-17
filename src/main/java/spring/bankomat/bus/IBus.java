package spring.bankomat.bus;

import spring.bankomat.entity.IEntity;
import spring.bankomat.store.IStore;

/**
 * Интерфейс-шина для связи сервиса с внешним миром
 */
public interface IBus {

    /**
     * Установить связь
     *
     * @param entity пример объекта
     * @param store инициализованный репозиторий
     */
    void start(IEntity entity, IStore store);

}
