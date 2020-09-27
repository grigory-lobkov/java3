package springboot.tt.entity;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

@CompiledJson(name = "Проект")
public class Project implements IEntity {

    @JsonAttribute(name = "Уникальный идентификатор")
    private String id;

    @JsonAttribute(name = "Наименование")
    private String name;

    @JsonAttribute(name = "Описание")
    private String description;

    @JsonAttribute(name = "Сокращение названия проекта")
    private String prefix; // Например, Progwards Task Tracker - PTT

    @JsonAttribute(name = "Руководитель")
    private User manager;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
