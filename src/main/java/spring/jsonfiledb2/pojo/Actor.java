package spring.jsonfiledb2.pojo;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;
import org.springframework.stereotype.Component;

/**
 * POJO объект "Задача"
 */
@Component
@CompiledJson(name = "Исполнитель")
public class Actor implements IPojo {

    @JsonAttribute(name = "Идентификатор")
    private String id;

    @JsonAttribute(name = "Название")
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}