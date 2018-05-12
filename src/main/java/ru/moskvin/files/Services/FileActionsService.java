package ru.moskvin.files.Services;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileActionsService {
    //удаление
    public static boolean deleteFile(String fileName) throws FileNotFoundException, Exception {
        File file = new File(fileName);
        if (Files.exists(Paths.get(fileName))) {
            if (!Files.isDirectory(Paths.get(fileName)))
                Files.delete(Paths.get(fileName));
            else
                return FileSystemUtils.deleteRecursively(file);
            return true;
        } else
            return false;
    }

    //переименование
    public static boolean renameFile(String fileName, String newFileName) throws FileAlreadyExistsException,
            IOException {
        Path oldFile = Paths.get(fileName);
        Path newFile = Paths.get(newFileName).toAbsolutePath();
        if (newFile.toFile().exists()) {
            throw new FileAlreadyExistsException("File Already Exists");
        }

        Files.move(oldFile, newFile.resolveSibling(newFile.getFileName()));
        return true;
    }

    //копирование
    public static void copyFile(String fileName, String destPath) throws IOException {
        File sourceFile = new File(fileName);
        File destFile = new File(destPath + "/" + sourceFile.getName());
        while (destFile.exists()) {
            destFile = new File(destFile.getPath() + "1");
        }
        if (!sourceFile.isDirectory())
            Files.copy(sourceFile.toPath(), destFile.toPath());
        else {
            destFile.mkdir();
            for (File file : sourceFile.listFiles()) {
                Files.copy(file.toPath(), new File(destFile.getPath() + "/" + file.getName()).toPath());
            }
        }
    }

    //перемещение
    public static void moveFileOrDirectory(String fileName, String destPath) throws IOException {
        File sourceFile = new File(fileName);
        File destFile = new File(destPath + "/" + sourceFile.getName());
        sourceFile.renameTo(destFile);
    }
}