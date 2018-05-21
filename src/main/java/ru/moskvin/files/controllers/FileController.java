package ru.moskvin.files.controllers;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.*;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import ru.moskvin.files.services.FileActionsService;
import ru.moskvin.files.models.*;
import ru.moskvin.files.persistence.H2Dao;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class FileController {
    //путь к конфигурационному файлу
    private static final String cfgFilePath = "config.cfg";
    //корень файловой системы
    private static String root;
    private static Path rootPath;
    @Autowired
    private H2Dao h2Dao;
    @Autowired
    private ServletContext servletContext;

    public FileController() throws IOException {
        rootPath = init();
    }

    protected static Path init() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cfgFilePath));
        root = reader.readLine();
        return Paths.get(root);
    }

    //доступ к корню файловой системы
    @RequestMapping(value = "/showall", method = RequestMethod.GET)
    public FilesModel fileSystemShowAll() throws IOException {
        return getFiles(root);
    }

    //получение файлов по заданному пути
    @RequestMapping(value = "/getfiles", method = RequestMethod.GET)
    public FilesModel getFiles(@RequestParam(value = "filePath", defaultValue = "") String path) throws IOException {
        if (checkAccess(path))
            return new FilesModel(path);
        else
            throw new AccessDeniedException("Required File is not in file system");
    }

    //получение текста из текстового файла
    @RequestMapping(value = "/gettextfromfile", method = RequestMethod.GET)
    public TextFileModel getTextFromFile(@RequestParam(value = "fileName") String fileName) throws IOException {
        return new TextFileModel(fileName);
    }

    //получение файлов по заданному пути
    @RequestMapping(value = "/files/**", method = RequestMethod.GET)
    public FilesModel getSomeFiles(HttpServletRequest request) throws IOException {
        //достаём путь из запроса
        //String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = new AntPathMatcher().extractPathWithinPattern("/files/**", request.getRequestURI());
        path = URLDecoder.decode(path, "UTF-8");
        return getFiles(path);
    }

    //удаление файла из файловой системы
    @RequestMapping(value = "/files/**", method = RequestMethod.DELETE)
    public String deleteFile(HttpServletRequest request) throws IOException, Exception {
        //достаём путь из запроса
        //String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String path = new AntPathMatcher().extractPathWithinPattern("/files/**", request.getRequestURI());
        path = URLDecoder.decode(path, "UTF-8");
        if (checkAccess(path)) {
            if (FileActionsService.deleteFile(path))
                return "Success";
            else
                return "File does not exist";
        } else
            return "Error, access denied";
    }

    //обработка POST-запросов на переименование, копирование и перемещение
    @RequestMapping(value = "/files/**", method = RequestMethod.POST)
    public String postProcessing(@RequestBody FileActionRequestModel requestModel,
                                 HttpServletRequest request) throws IOException, Exception {
        //достаём путь из запроса
        String path = new AntPathMatcher().extractPathWithinPattern("/files/**", request.getRequestURI());
        path = URLDecoder.decode(path, "UTF-8");
        //выбираем действие в зависимости от параметра action
        if (checkAccess(path)) {
            String action = requestModel.getAction();
            if (action == null)
                throw new IllegalArgumentException("Action must be specified");
            switch (action) {
                case "copy":
                    FileActionsService.copyFile(path, requestModel.getPath());
                    return "Success";
                case "move":
                    FileActionsService.moveFileOrDirectory(path, requestModel.getPath());
                    return "Success";
                case "rename":
                    if (FileActionsService.renameFile(path, root + "/" + requestModel.getNewName()))
                        return "Success";
                    return "Error";
                default:
                    throw new IllegalArgumentException("Illegal action");
            }
        } else
            return "Access denied";
    }

    @RequestMapping(value = "/download/**", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request,
                                                            HttpServletResponse response) throws IOException {

        String path = new AntPathMatcher().extractPathWithinPattern("/files/**", request.getRequestURI());
        path = URLDecoder.decode(path, "UTF-8");
        File file = new File(path);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        String type = servletContext.getMimeType(path);
        //находим тип файла
        MediaType mediaType;
        try{
            mediaType = MediaType.parseMediaType(type);
        }
        catch (Exception e){
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }

    //проверка, ведёт ли файл в нашу файловую систему
    protected boolean checkAccess(String path) throws IOException {
        Path requiredPath = Paths.get(path).toAbsolutePath();
        if (requiredPath.equals(rootPath.toAbsolutePath()) || requiredPath.startsWith(rootPath.toAbsolutePath()))
            return true;
        return false;
    }
}

