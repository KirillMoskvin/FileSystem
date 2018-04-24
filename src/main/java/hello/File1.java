package hello;

import java.util.Date;
import java.io.File;

public class File1 {
    private String name;
    private Integer size;
    private Date creationDate;
    private Date modificationDate;
    private Date accessDate;

    public File1(){
        File file = new File("Greeting.java");
        file.listFiles();
    }
}
