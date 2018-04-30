package files;

import java.io.*;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;
import java.util.SplittableRandom;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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
    //получение файлов по заданному пути
    @RequestMapping(value="/getfiles", method = RequestMethod.GET)
    public FileModel getFiles(@RequestParam(value = "filePath", defaultValue="") String path) throws IOException{
        if (checkAccess(path))
            return new FileModel(path);
        else
            throw new AccessDeniedException("Required File is not in file system");
    }
    //получение текста из текстового файла
    @RequestMapping(value = "/gettextfromfile", method = RequestMethod.GET)
    public TextFileModel getTextFromFile(@RequestParam(value = "fileName") String fileName) throws IOException{
        return new TextFileModel(fileName);
    }
    //удаление файла из файловой системы
    @RequestMapping(value = "/deletefile", method = RequestMethod.DELETE)
    public String deleteFileOrDirectory(@RequestParam(value = "fileName") String fileName) throws IOException{
        if (checkAccess(fileName)) {
            if (Files.exists(Paths.get(fileName))) {
                Files.delete(Paths.get(fileName));
                return "success";
            }
            else
                throw new FileNotFoundException();
        }
        else
            throw new AccessDeniedException("Required File is not in file system");
    }

    @RequestMapping(value = "/renamefile", method = RequestMethod.POST)
    public String renameFileOrDirectory(@RequestParam(value = "fileName") String fileName,
                                        @RequestParam(value = "newFileName") String newFileName) throws IOException{
        if (checkAccess(fileName)) {
            File oldFile = new File(fileName);
            File newFile = new File(newFileName);
            if (newFile.exists()) {
                throw new FileAlreadyExistsException("File Already Exists");
            }

            if (oldFile.renameTo(newFile))
                return "Success";
            else
                return "Renaming error";
            }
        else
            throw new AccessDeniedException("Required File is not in file system");
    }

    @RequestMapping(value = "/copyfile", method = RequestMethod.POST)
    public String copyFileOrDirectory(@RequestParam(value = "fileName") String fileName,
                                      @RequestParam(value = "destPath") String destPath) throws IOException {
        if (checkAccess(fileName) && checkAccess(destPath)) {
            File sourceFile = new File(fileName);
            File destFile = new File(destPath);

            try {
                if (!sourceFile.isDirectory())
                    Files.copy(sourceFile.toPath(), destFile.toPath());
                else {
                    destFile.mkdir();
                    for (File file : sourceFile.listFiles())
                        Files.copy(file.toPath(), destFile.toPath());
                }
                return "Success";
            }
            catch (IOException e){
                return "Error";
            }
        }
        else
            throw new AccessDeniedException("Required file is not in file system");
    }

    @RequestMapping(value = "/movefile", method = RequestMethod.POST)
    public String moveFileOrDirectory(@RequestParam(value = "fileName") String fileName,
                                      @RequestParam(value = "destPath") String destPath) throws IOException {
        if (checkAccess(fileName) && checkAccess(destPath)) {
            File sourceFile = new File(fileName);
            File destFile = new File(destPath);

            try {
                if (!sourceFile.isDirectory())
                    Files.move(sourceFile.toPath(), destFile.toPath());
                else {
                    destFile.mkdir();
                    for (File file : sourceFile.listFiles())
                        Files.move(file.toPath(), destFile.toPath());
                }
                return "Success";
            }
            catch (IOException e){
                return "Error";
            }
        }
        else
            throw new AccessDeniedException("Required file is not in file system");
    }


    //проверка, ведёт ли файл в нашу файловую систему
    protected boolean checkAccess(String path){
        if (path.startsWith(root))
            return true;
        return false;
    }
}
