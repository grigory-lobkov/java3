package spring.jsonfiledb.test;

import org.junit.*;
import spring.jsonfiledb.App;
import spring.jsonfiledb.db.IDb;
import spring.jsonfiledb.pojo.IPojo;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

public class TestIDb {

    static private final String fileName = "test.json";
    static private File file;
    static private Class pojoClazz;
    static private Constructor<IPojo> pojoConstructor;
    static private IPojo pojo;
    static private IPojo pojo1;
    static private IPojo pojo2;
    static private IDb input;

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

            input = App.input;
            file = new File(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void init() throws Exception {
        file.delete();
        input.init(fileName, pojo.getClass());
    }

    @AfterClass
    public static void destroy() throws Exception {
        file.delete();
    }

    @Test
    public void IDb_saveandget() {
        IPojo r1 = input.get(pojo.getId());
        Assert.assertNull(r1);
        input.save(pojo);
        r1 = input.get(pojo.getId());
        Assert.assertEquals(r1.getId(),pojo.getId());
    }

    @Test
    @Ignore
    public void IDb_resave() {
        String oldId = pojo1.getId();
        String newId = oldId+"t";
        input.save(pojo1);
        pojo1.setId(newId);
        input.save(pojo1);
        IPojo r1 = input.get(pojo1.getId());
        Assert.assertEquals(r1.getId(),pojo.getId());
    }

    @Test
    public void IDb_saveSeveral() {
        input.save(pojo1);
        input.save(pojo2);
        IPojo r1 = input.get(pojo1.getId());
        IPojo r2 = input.get(pojo2.getId());
        List<IPojo> list = input.get();
        Assert.assertEquals(r1.getId(),pojo1.getId());
        Assert.assertEquals(r2.getId(),pojo2.getId());
        Assert.assertEquals(list.size(),2);
    }

    @Test
    public void IDb_delete() {
        input.save(pojo);
        input.save(pojo1);
        input.save(pojo2);
        input.delete(pojo1.getId());
        IPojo r0 = input.get(pojo.getId());
        IPojo r1 = input.get(pojo1.getId());
        IPojo r2 = input.get(pojo2.getId());
        Assert.assertNull(r1);
        Assert.assertEquals(r0.getId(),pojo.getId());
        Assert.assertEquals(r2.getId(),pojo2.getId());
        List<IPojo> list = input.get();
        Assert.assertEquals(list.size(),2);
    }


}
