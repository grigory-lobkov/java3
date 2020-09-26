package spring.bankomat2.store;

import spring.bankomat2.entity.IEntity;

import java.util.List;

/**
 * Доступ к хранилищу объектов
 */
//TaskRepository
public interface IStore<E extends IEntity> {

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
     * Добавить новый объект в хранилище
     *
     * @param entity объект
     */
    void save(E entity);

    /**
     * Обновить объект в хранилище
     *
     * @param entity объект
     */
    void update(E entity);

    /**
     * Удалить объект из хранилища
     *
     * @param id Идентификатор объекта
     */
    void delete(long id);

    /**
     * Получить список всех объектов хранилища
     *
     * @return список всех объектов хранилища
     */
    List<E> get();

    /**
     * Получить объект из хранилища
     *
     * @param id идентификатор объекта
     * @return объект
     */
    E get(long id);

}
