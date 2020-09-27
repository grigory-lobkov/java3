package springboot.tt.entity;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

@CompiledJson(name = "Вложение")
public class Attachment implements IEntity {

    @JsonAttribute(name = "Уникальный идентификатор")
    private String id;

    @JsonAttribute(name = "Наименование")
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
