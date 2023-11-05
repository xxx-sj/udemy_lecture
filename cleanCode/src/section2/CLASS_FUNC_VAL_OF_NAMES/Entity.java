package section2.CLASS_FUNC_VAL_OF_NAMES;

import java.time.LocalDateTime;

public class Entity {
    private String title;
    private String description;
    private LocalDateTime ymdhm;

    public Entity(String title, String description, LocalDateTime ymdhm) {
        this.title = title;
        this.description = description;
        this.ymdhm = ymdhm;
    }

    public void output() {
        System.out.println(this.getTitle());
        System.out.println(this.getDescription());
        System.out.println(this.getYmdhm());
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

    public LocalDateTime getYmdhm() {
        return ymdhm;
    }

    public void setYmdhm(LocalDateTime ymdhm) {
        this.ymdhm = ymdhm;
    }
}
