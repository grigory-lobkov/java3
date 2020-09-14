package spring.jsonfiledb.test;

import org.junit.*;
import spring.jsonfiledb.App;
import spring.jsonfiledb.repo.IRepo;
import spring.jsonfiledb.pojo.IPojo;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Тестирование репозитория контекстом, заданном в классе spring.jsonfiledb.App
 */
public class TestIRepo {

    /**
     * Имя файла пробного хранилища - будет удален по завершенни теста
     */
    static private final String fileName = "test.json";
    static private File file;
    static private Class pojoClazz;
    static private Constructor<IPojo> pojoConstructor;
    /**
     * Репозиторий
     */
    static private IRepo repo;
    /**
     * Предсозданный объект 0
     */
    static private IPojo pojo;
    /**
     * Предсозданный объект 1
     */
    static private IPojo pojo1;
    /**
     * Предсозданный объект 2
     */
    static private IPojo pojo2;

    /**
     * Инициализация
     */
    static {
        try {
            pojoClazz = App.pojo.getClass();
            pojoConstructor = pojoClazz.getDeclaredConstructor();

            pojo = pojoConstructor.newInstance();
            pojo1 = pojoConstructor.newInstance();
            pojo2 = pojoConstructor.newInstance();
            pojo.setId("000");
            pojo1.setId("001");
            pojo2.setId("002");

            repo = App.repo;
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
        repo.init(fileName, pojo.getClass());
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
        IPojo r1 = repo.get(pojo.getId());
        Assert.assertNull(r1);
        repo.save(pojo);
        r1 = repo.get(pojo.getId());
        Assert.assertEquals(r1.getId(), pojo.getId());
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
        Field[] fields = pojoClazz.getDeclaredFields();
        for (Field f : fields) {
            if (!f.getName().equals("id") && f.getType().getSimpleName().equals("String")) {
                field = f;
                f.setAccessible(true);
                Object val = f.get(pojo1);
                if(val==null) {
                    val = "()";
                    field.set(pojo1, val);
                }
                oldVal = val.toString();
            }
        }
        if (field != null) {
            newVal = oldVal + "t";
            System.out.println("Field '"+field.getName()+"' = '"+oldVal+"' -> '"+newVal+"'");

            repo.save(pojo1);
            field.set(pojo1, newVal);
            repo.update(pojo1);
            IPojo p = repo.get(pojo1.getId());

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
        List<IPojo> list1 = repo.get();
        repo.save(pojo1);
        repo.save(pojo2);
        IPojo r1 = repo.get(pojo1.getId());
        IPojo r2 = repo.get(pojo2.getId());
        List<IPojo> list2 = repo.get();
        Assert.assertEquals(r1.getId(), pojo1.getId());
        Assert.assertEquals(r2.getId(), pojo2.getId());
        Assert.assertEquals(list2.size() - list1.size(), 2);
    }

    /**
     * Запишем три объекта, удалим второй
     * Перечитаем все объекты, проверим что они те же, а второй пуст
     * А количество записей в базе должно увеличиться на 2
     */
    @Test
    public void IDb_delete() {
        List<IPojo> list1 = repo.get();
        repo.save(pojo);
        repo.save(pojo1);
        repo.save(pojo2);
        repo.delete(pojo1.getId());
        IPojo r0 = repo.get(pojo.getId());
        IPojo r1 = repo.get(pojo1.getId());
        IPojo r2 = repo.get(pojo2.getId());
        Assert.assertNull(r1);
        Assert.assertEquals(r0.getId(), pojo.getId());
        Assert.assertEquals(r2.getId(), pojo2.getId());
        List<IPojo> list2 = repo.get();
        Assert.assertEquals(list2.size() - list1.size(), 2);
    }


}