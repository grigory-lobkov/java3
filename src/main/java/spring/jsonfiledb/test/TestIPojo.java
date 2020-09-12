package spring.jsonfiledb.test;


import org.junit.*;
import spring.jsonfiledb.App;
import spring.jsonfiledb.pojo.IPojo;
import spring.jsonfiledb.pojo.Task;

import java.io.File;
import java.lang.reflect.Constructor;

public class TestIPojo {

    static private final String fileName = "test.json";
    static private Class pojoClazz;
    static private Constructor<IPojo> pojoConstructor;
    static private IPojo pojo;

    @BeforeClass
    public static void init() throws Exception {
        pojoClazz = App.pojo.getClass();
        pojoConstructor = pojoClazz.getDeclaredConstructor();
        pojo = pojoConstructor.newInstance();
    }

    @AfterClass
    public static void destroy() throws Exception {
        File f = new File(fileName);
        f.delete();
    }

    @Test
    public void IPojo_id_setget() {
        String newId = "unique_identifier";

        pojo.setId(newId);

        String factId = pojo.getId();
        Assert.assertEquals(newId, factId);
    }

    @Test
    public void IPojo_is_Task() {
       Assert.assertTrue(pojo instanceof Task);
    }

}