package spring.jsonfiledb.bus;

import spring.jsonfiledb.pojo.IPojo;
import spring.jsonfiledb.repo.IRepo;

/**
 * Интерфейс-шина для связи сервиса с внешним миром
 */
public interface IBus {

    /**
     * Установить связь
     *
     * @param pojo пример POJO объекта
     * @param repo инициализованный репозиторий
     */
    void start(IPojo pojo, IRepo repo);

}
