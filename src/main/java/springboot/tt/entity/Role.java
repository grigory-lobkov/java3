package springboot.tt.entity;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

@CompiledJson(name = "Роль")
public class Role implements IEntity {

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
