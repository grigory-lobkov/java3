package springboot.tt.entity;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

import java.util.List;

@CompiledJson(name = "Правила проекта")
public class ProjectRules implements IEntity {

    @JsonAttribute(name = "уникальный идентификатор")
    private String id;

    @JsonAttribute(name = "Наименование")
    private String name;

    @JsonAttribute(name = "Роль")
    private Role role;

    @JsonAttribute(name = "Правила проектов")
    private List<ProjectRules> rules;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
