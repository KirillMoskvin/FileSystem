package files;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.LinkedList;
import java.util.List;
import java.util.SplittableRandom;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
    //путь к конфигурационному файлу
    private static final String cfgFilePath="config.cfg";
    //корень файловой системы
    private static String root;
    static {
        try(BufferedReader reader = new BufferedReader(new FileReader(cfgFilePath)))
        {
            root = reader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //доступ к корню файловой системы
    @RequestMapping(value = "/showall", method = RequestMethod.GET)
    public FilesModel fileSystemShowAll() throws IOException{
        return new FilesModel(root);
    }


    @RequestMapping(value="/getfiles", method = RequestMethod.GET)
    public FileModel getFiles(@RequestParam(value = "filePath", defaultValue="") String path) throws IOException{
        if (checkAccess(path))
            return new FileModel(path);
        else
            throw new AccessDeniedException("Required File is not in file system");
    }

    @RequestMapping(value = "/gettextfromfile", method = RequestMethod.GET)
    public TextFileModel getTextFromFile(@RequestParam(value = "fileName") String fileName) throws IOException{
        return new TextFileModel(fileName);
    }


    //проверка, ведёт ли файл в нашу файловую систему
    protected boolean checkAccess(String path){
        if (path.startsWith(root))
            return true;
        return false;
    }
}
