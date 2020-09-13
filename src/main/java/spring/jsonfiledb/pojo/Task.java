package spring.jsonfiledb.pojo;

import com.dslplatform.json.*;

@CompiledJson
public class Task implements IPojo {

    @JsonAttribute(name = "ID")
    private String id;
    @JsonAttribute(name = "Описание")
    private String description;
    @JsonAttribute(name = "Автор")
    private String author;
    @JsonAttribute(name = "Имя")
    private String name;
    @JsonAttribute(name = "Точка истории")
    private int storyPoint;



    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public int getStoryPoint() {
        return storyPoint;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStoryPoint(int storyPoint) {
        this.storyPoint = storyPoint;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", storyPoint=" + storyPoint +
                '}';
    }
}