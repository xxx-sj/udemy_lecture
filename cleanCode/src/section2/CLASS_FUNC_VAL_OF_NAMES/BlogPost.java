package section2.CLASS_FUNC_VAL_OF_NAMES;

import java.time.LocalDateTime;

public class BlogPost {
    private String title;
    private String description;
    private LocalDateTime datePublished;

    public BlogPost(String title, String description, LocalDateTime datePublished) {
        this.title = title;
        this.description = description;
        this.datePublished = datePublished;
    }

    public void print() {
        System.out.println(getTitle());
        System.out.println(getDescription());
        System.out.println(getDatePublished());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDateTime datePublished) {
        this.datePublished = datePublished;
    }
}
