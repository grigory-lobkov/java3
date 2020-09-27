package springboot.tt.entity;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

import java.util.List;

@CompiledJson(name = "Пользователь")
public class User implements IEntity {

    @JsonAttribute(name = "Уникальный идентификатор")
    private String id;

    @JsonAttribute(name = "Имя")
    private String name;

    @JsonAttribute(name = "Роль")
    private Role role;

    @JsonAttribute(name = "Список правил проектов")
    private List<ProjectRules> rules;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ProjectRules> getRules() {
        return rules;
    }

    public void setRules(List<ProjectRules> rules) {
        this.rules = rules;
    }

}
