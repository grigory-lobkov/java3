package spring.bankomat2.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import spring.bankomat2.App;
import spring.bankomat2.entity.IEntity;
import spring.bankomat2.store.IStore;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Тестирование репозитория контекстом, заданном в классе spring.bankomat.App
 */
public class TestIStore {

    /**
     * Имя файла пробного хранилища - будет удален по завершенни теста
     */
    static private final String fileName = "test.json";
    static ApplicationContext context = App.context;
    static private File file;
    static private Class entityClazz;
    static private Constructor<IEntity> entityConstructor;
    /**
     * Репозиторий
     */
    static private IStore store;
    /**
     * Предсозданный объект 0
     */
    static private IEntity entity;
    /**
     * Предсозданный объект 1
     */
    static private IEntity entity1;
    /**
     * Предсозданный объект 2
     */
    static private IEntity entity2;

    /**
     * Инициализация
     */
    static {
        try {
            entityClazz = context.getBean(IEntity.class).getClass();
            entityConstructor = entityClazz.getDeclaredConstructor();

            entity = entityConstructor.newInstance();
            entity1 = entityConstructor.newInstance();
            entity2 = entityConstructor.newInstance();
            entity.setId(0);
            entity1.setId(1);
            entity2.setId(2);

            store = context.getBean(IStore.class);
            file = new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * В начале каждого теста
     * Удаляем файл хранилища и инициализируем репозиторий на новом пустом файле
     *
     * @throws Exception при проблемах ввода-вывода
     */
    @Before
    public void init() throws Exception {
        file.delete();
        store.init(fileName, entity.getClass());
    }

    /**
     * В конце всех тестов
     * Удаляем файл хранилища
     *
     * @throws Exception при проблемах ввода-вывода
     */
    @AfterClass
    public static void destroy() throws Exception {
        file.delete();
    }

    /**
     * Проверим, что в базе нет объекта, затем добавим его и проверим, что он записался
     */
    @Test
    public void IDb_saveandget() {
        IEntity r1 = store.get(entity.getId());
        Assert.assertNull(r1);
        store.save(entity);
        r1 = store.get(entity.getId());
        Assert.assertEquals(r1.getId(), entity.getId());
    }

    /**
     * Найдем любое строковое поле и попробуем в базе обновить его значение
     * потом проверим, что оно изменилось в хранилище
     */
    @Test
    public void IDb_updateString() throws IllegalAccessException {
        String oldVal = "";
        String gotVal = "";
        String newVal = "";
        Field field = null;

        // любое строковое поле
        Field[] fields = entityClazz.getDeclaredFields();
        for (Field f : fields) {
            if (!f.getName().equals("id") && f.getType().getSimpleName().equals("String")) {
                field = f;
                f.setAccessible(true);
                Object val = f.get(entity1);
                if(val==null) {
                    val = "()";
                    field.set(entity1, val);
                }
                oldVal = val.toString();
            }
        }
        if (field != null) {
            newVal = oldVal + "t";
            System.out.println("Field '"+field.getName()+"' = '"+oldVal+"' -> '"+newVal+"'");

            store.save(entity1);
            field.set(entity1, newVal);
            store.update(entity1);
            IEntity p = store.get(entity1.getId());

            gotVal = (String) field.get(p);
        }
        Assert.assertEquals(newVal, gotVal);
    }

    /**
     * Сохраним несколько объектов, потом считаем и проверим,
     * что считали те же объекты, которые сохраняли
     * А количество записей в базе должно увеличиться на 2
     */
    @Test
    public void IDb_saveSeveral() {
        List<IEntity> list1 = store.get();
        store.save(entity1);
        store.save(entity2);
        IEntity r1 = store.get(entity1.getId());
        IEntity r2 = store.get(entity2.getId());
        List<IEntity> list2 = store.get();
        Assert.assertEquals(r1.getId(), entity1.getId());
        Assert.assertEquals(r2.getId(), entity2.getId());
        Assert.assertEquals(list2.size() - list1.size(), 2);
    }

    /**
     * Запишем три объекта, удалим второй
     * Перечитаем все объекты, проверим что они те же, а второй пуст
     * А количество записей в базе должно увеличиться на 2
     */
    @Test
    public void IDb_delete() {
        List<IEntity> list1 = store.get();
        store.save(entity);
        store.save(entity1);
        store.save(entity2);
        store.delete(entity1.getId());
        IEntity r0 = store.get(entity.getId());
        IEntity r1 = store.get(entity1.getId());
        IEntity r2 = store.get(entity2.getId());
        Assert.assertNull(r1);
        Assert.assertEquals(r0.getId(), entity.getId());
        Assert.assertEquals(r2.getId(), entity2.getId());
        List<IEntity> list2 = store.get();
        Assert.assertEquals(list2.size() - list1.size(), 2);
    }


}