package springboot.tt.entity;

/*
* Интерфейс для любых POJO сущностей, с которыми будет работать репозиторий
* Для хранения обязательно только получать и задавать идентификатор объекта
*
* Для POJO бъектов допускается наличие сеттеров, геттеров, toString, getHash, equals, compareTo и т.п.
* - такие методы, которые не определяют бизнес логику
* */
//public interface IEntity<IdType extends Comparable> { // JSON сериализатор перестает работать с ID
public interface IEntity {

    /*
    * Получить текущий идентификатор
    * */
    String getId();

    /*
    * Установить новое значение идентификатора
    * */
    void setId(String id);

}
