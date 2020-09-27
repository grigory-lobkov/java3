package springboot.tt.bus;

/**
 * Интерфейс-шина для связи сервиса с внешним миром
 */
public interface IBus {

    /**
     * Установить связь
     */
    void start();

}
