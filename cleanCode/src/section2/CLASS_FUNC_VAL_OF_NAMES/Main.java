package section2.CLASS_FUNC_VAL_OF_NAMES;

import java.time.LocalDateTime;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println("===========================BAD=============================");
        String title1 = "title";
        String description1 = "description";
        LocalDateTime newDate1 = LocalDateTime.now();

        Entity entity = new Entity(title1, description1, newDate1);
        entity.output();
        System.out.println("==========================================================");


        System.out.println("===========================GOOD=============================");
        String title2 = "title";
        String description2 = "description";
        LocalDateTime now = LocalDateTime.now();

        BlogPost blogPost = new BlogPost(title2, description2, now);
        blogPost.print();
        System.out.println("==========================================================");
    }
}
