package spring.bankomat2.entity;

/*
* Интерфейс для любых POJO объектов, с которыми будет работать репозиторий
* Для хранения обязательно только получать и задавать идентификатор объекта
*
* Для POJO бъектов допускается наличие сеттеров, геттеров, toString, getHash, equals, compareTo и т.п.
* - такие методы, которые не определяют бизнес логику
* */
public interface IEntity {

    /*
    * Получить текущий идентификатор
    * */
    long getId();

    /*
    * Установить новое значение идентификатора
    * */
    void setId(long id);

}
