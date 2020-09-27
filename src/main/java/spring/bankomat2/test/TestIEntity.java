package spring.bankomat2.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import spring.bankomat2.App;
import spring.bankomat2.entity.IEntity;

import java.lang.reflect.Constructor;

/**
 * Тестирование объекта, заданного контекстом springboot.tt.App
 */
public class TestIEntity {

    static ApplicationContext context = App.context;
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
        entityClazz = context.getBean("accountEntity").getClass();
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