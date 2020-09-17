package spring.bankomat.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.bankomat.entity.IEntity;

import java.lang.reflect.Constructor;

/**
 * Тестирование объекта, заданного контекстом spring.jsonfiledb.App
 */
public class TestIEntity {

    static ApplicationContext context = new ClassPathXmlApplicationContext("bankomat-context.xml");
    static private IEntity entity;
    static private Class entityClazz;
    static private Constructor<IEntity> entityConstructor;

    /**
     * Один раз перед всеми тестами
     * Создаем новый POJO-объект для тестов
     *
     * @throws Exception
     */
    @BeforeClass
    public static void init() throws Exception {
        entityClazz = context.getBean("account").getClass();
        entityConstructor = entityClazz.getDeclaredConstructor();
        entity = entityConstructor.newInstance();
    }

    /**
     * Пробуем установить идентификатор и прочитать его - должен совпадать
     */
    @Test
    public void IEntity_id_setget() {
        long newId = 1234567890321L;

        entity.setId(newId);

        long factId = entity.getId();
        Assert.assertEquals(newId, factId);
    }

    /**
     * Проверяем, что POJO-примитив является реализацией класса Task
     */
    @Test
    public void IEntity_is_Task() {
       Assert.assertTrue(entity instanceof IEntity);
    }

}