package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Title should not empty")
    private String title;

    @NotEmpty(message = "Text should not be empty")
    private String text;

    @NotNull(message = "Options should not be null")
    @NotEmpty(message = "Options should not be empty")
    @Size(min = 2, message = "Minimum 2 options required")
    @ElementCollection
    private List<String> options;

    @ElementCollection
    private List<Integer> answer;

    @JsonIgnore
    private String author;

    public Quiz() {
    }

    @JsonIgnore
    List<Integer> getAnswer() {
        return answer;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getId() {
        return id;
    }

    public String getAuthor(){
        return author;
    }

    @JsonProperty
    void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setText(String text) {
        this.text = text;
    }

    void setOptions(List<String> options) {
        this.options = options;
    }

    void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author){
        this.author=author;
    }
}
