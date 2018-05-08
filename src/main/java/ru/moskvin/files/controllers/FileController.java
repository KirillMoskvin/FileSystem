package ru.moskvin.files.controllers;

import java.io.*;
import java.nio.file.*;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import ru.moskvin.files.models.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FileController  {
    //путь к конфигурационному файлу
    private static final String cfgFilePath="config.cfg";
    //корень файловой системы
    private static String root;
    private static Path rootPath;


    public FileController() throws IOException{
        rootPath = init();
    }

    //доступ к корню файловой системы
    @RequestMapping(value = "/showall", method = RequestMethod.GET)
    public FilesModel fileSystemShowAll() throws IOException{
        return getFiles(root);
    }

    //получение файлов по заданному пути
    @RequestMapping(value="/getfiles", method = RequestMethod.GET)
    public FilesModel getFiles(@RequestParam(value = "filePath", defaultValue="") String path) throws IOException{
        if (checkAccess(path))
            return new FilesModel(path);
        else
            throw new AccessDeniedException("Required File is not in file system");
    }
    //получение текста из текстового файла
    @RequestMapping(value = "/gettextfromfile", method = RequestMethod.GET)
    public TextFileModel getTextFromFile(@RequestParam(value = "fileName") String fileName) throws IOException{
        return new TextFileModel(fileName);
    }

    @RequestMapping(value = "/renamefile", method = RequestMethod.POST)
    public String renameFileOrDirectory(@RequestParam(value = "fileName") String fileName,
                                        @RequestParam(value = "newFileName") String newFileName) throws IOException{
        if (checkAccess(fileName))
            if (FileActionsService.renameFile(fileName,newFileName))
                return "Success";
            else
                return "Error, file with this name already exists";
        else
            throw new AccessDeniedException("Required File is not in file system");
    }

    @RequestMapping(value = "/copyfile", method = RequestMethod.POST)
    public String copyFileOrDirectory(@RequestParam(value = "fileName") String fileName,
                                      @RequestParam(value = "destPath") String destPath) throws IOException {
        if (checkAccess(fileName) && checkAccess(destPath)) {
            FileActionsService.copyFile(fileName,destPath);
            return "Succeess";
        }
        else
            throw new AccessDeniedException("Required file is not in file system");
    }

    @RequestMapping(value = "/movefile", method = RequestMethod.POST)
    public String moveFileOrDirectory(@RequestParam(value = "fileName") String fileName,
                                      @RequestParam(value = "destPath") String destPath) throws IOException {
        if (checkAccess(fileName) && checkAccess(destPath)) {
            return "Success";
        }
        else
            throw new AccessDeniedException("Required file is not in file system");
    }

    //получение файлов по заданному пути
    @RequestMapping(value = "/files/**", method = RequestMethod.GET)
    public FilesModel getSomeFiles(HttpServletRequest request)
            throws IOException {
        //достаём путь из запроса
        //String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = new AntPathMatcher().extractPathWithinPattern( "/files/**", request.getRequestURI() );
        path = path.replace("%20", " ");
        return getFiles(path);
    }

    //удаление файла из файловой системы
    @RequestMapping(value = "/files/**", method = RequestMethod.DELETE)
    public String deleteFile(HttpServletRequest request) throws IOException, Exception {
        //достаём путь из запроса
        //String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = new AntPathMatcher().extractPathWithinPattern( "/files/**", request.getRequestURI() );
        path = path.replace("%20", " ");
        if (checkAccess(path)) {
            if (FileActionsService.deleteFile(path))
                return "Success";
            else
                return "File does not exist";
        }
        else
            return "Error, access denied";
    }

    //обработка POST-запросов на переименование, копирование и перемещение
    @RequestMapping(value = "/files/**", method = RequestMethod.POST)
    public String postProcessing(@RequestBody FileActionRequestModel requestModel,
                                 HttpServletRequest request) throws IOException, Exception {

        //достаём путь из запроса
        String path = new AntPathMatcher().extractPathWithinPattern( "/files/**", request.getRequestURI() );
        path = path.replace("%20", " ");

        if (checkAccess(path)) {
            String action = requestModel.getAction();
            if (action == null)
                throw new IllegalArgumentException("Action must be specified");
            switch (action) {
                case "copy":
                    return "copy";
                case "move":
                    FileActionsService.moveFileOrDirectory(path, requestModel.getPath());
                    return "Success";
                case "rename":
                    if (FileActionsService.renameFile(path, root+"/"+ requestModel.getNewName()))
                        return "Success";
                    return "Error";
                default:
                    throw new IllegalArgumentException("Illegal action");
            }
        }
        else
            return "Access denied";
    }


    //проверка, ведёт ли файл в нашу файловую систему
    protected boolean checkAccess(String path) throws IOException{
        Path requiredPath = Paths.get(path).toAbsolutePath();
        if (requiredPath.equals(rootPath.toAbsolutePath()) || requiredPath.startsWith(rootPath.toAbsolutePath()))
            return true;
        return false;
    }

    protected static Path init () throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cfgFilePath));
        root = reader.readLine();
        return Paths.get(root);
    }
}

