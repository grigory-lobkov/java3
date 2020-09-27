package springboot.tt.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import springboot.tt.App;
import springboot.tt.entity.IEntity;

import java.lang.reflect.Constructor;

/**
 * Тестирование объекта, заданного контекстом spring.jsonfiledb.App
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
        entityClazz = context.getBean("userEntity").getClass();
        entityConstructor = entityClazz.getDeclaredConstructor();
        entity = entityConstructor.newInstance();
    }

    /**
     * Пробуем установить идентификатор и прочитать его - должен совпадать
     */
    @Test
    public void IEntity_id_setget() {
        String newId = "1234567890321L";

        entity.setId(newId);

        Comparable factId = entity.getId();
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