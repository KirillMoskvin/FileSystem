package hello;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javafx.collections.transformation.FilteredList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public File fileSystemRoot(){
        return new File("");
    }

    @RequestMapping(value = "/showall", method = RequestMethod.GET)
    public FilesWrapper fileSystemShowAll(){

        File file = new File("src");
        File[] arrF = file.listFiles();
        List<File> files = Arrays.asList(file.listFiles());
        FilesWrapper res = new FilesWrapper(files);
        return res;
    }

}
