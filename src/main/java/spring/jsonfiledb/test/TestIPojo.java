package spring.jsonfiledb.test;


import org.junit.*;
import spring.jsonfiledb.App;
import spring.jsonfiledb.pojo.IPojo;
import spring.jsonfiledb.pojo.Task;

import java.lang.reflect.Constructor;

/**
 * Тестирование POJO-объекта, заданного контекстом spring.jsonfiledb.App
 */
public class TestIPojo {

    static private IPojo pojo;
    static private Class pojoClazz;
    static private Constructor<IPojo> pojoConstructor;

    /**
     * Один раз перед всеми тестами
     * Создаем новый POJO-объект для тестов
     *
     * @throws Exception
     */
    @BeforeClass
    public static void init() throws Exception {
        pojoClazz = App.pojo.getClass();
        pojoConstructor = pojoClazz.getDeclaredConstructor();
        pojo = pojoConstructor.newInstance();
    }

    /**
     * Пробуем установить идентификатор и прочитать его - должен совпадать
     */
    @Test
    public void IPojo_id_setget() {
        String newId = "unique_identifier";

        pojo.setId(newId);

        String factId = pojo.getId();
        Assert.assertEquals(newId, factId);
    }

    /**
     * Проверяем, что POJO-примитив является реализацией класса Task
     */
    @Test
    public void IPojo_is_Task() {
       Assert.assertTrue(pojo instanceof Task);
    }

}