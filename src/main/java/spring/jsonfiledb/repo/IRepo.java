package spring.jsonfiledb.repo;

import spring.jsonfiledb.pojo.IPojo;

import java.util.List;

/**
 * Доступ к храненилищу POJO объектов
 */
//TaskRepository
public interface IRepo {

    /**
     * Инициализация хранилища - передача значимых параметров
     *
     * @param params параметры для имплементации
     * @throws Exception в случае ошибок ввода-вывода или рефлексии
     */
    void init(Object... params) throws Exception;

    /**
     * Уничтожение хранилища - сброс кеша, правильное завершение открытых файлов/соединений
     */
    void destroy();

    /**
     * Добавить новый POJO-объект в хранилище
     *
     * @param task POJO-объект
     */
    void save(IPojo task);

    /**
     * Обновить POJO-объект в хранилище
     *
     * @param task POJO-объект
     */
    void update(IPojo task);

    /**
     * Удалить POJO-объект из хранилища
     *
     * @param id Идентификатор POJO-объекта
     */
    void delete(String id);

    /**
     * Получить список всех POJO-объектов хранилища
     *
     * @param <T> тип POJO-объекта, должен имплементировать IPojo
     * @return список всех POJO-объектов хранилища
     */
    <T> List<T> get();

    /**
     * Получить объект из хранилища
     *
     * @param id идентификатор POJO-объекта
     * @return POJO-объект
     */
    IPojo get(String id);

}
