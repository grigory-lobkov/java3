package springboot.tt.entity;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

import java.time.LocalDate;
import java.util.List;

@CompiledJson(name = "Задача")
public class Task implements IEntity {

    @JsonAttribute(name = "Уникальный идентификатор")
    private String id;

    @JsonAttribute(name = "Наименование")
    private String name;

    @JsonAttribute(name = "Описание")
    private String description;

    @JsonAttribute(name = "Тип")
    private TaskType type;

    @JsonAttribute(name = "Приоритет")
    private PriorityType priority;

    @JsonAttribute(name = "Список вложений")
    private List<Attachment> attachments;

    @JsonAttribute(name = "Создатель")
    private User author;

    @JsonAttribute(name = "Исполнитель")
    private User executor;

    @JsonAttribute(name = "Создано")
    private LocalDate created;

    @JsonAttribute(name = "Обновлено")
    private LocalDate updated;

    @JsonAttribute(name = "Точка истории")
    private int storyPoint;

    @JsonAttribute(name = "Список подзадач")
    private List<Task> subtask;

    @JsonAttribute(name = "Список связанных задач")
    private List<Task> relatedTask;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
