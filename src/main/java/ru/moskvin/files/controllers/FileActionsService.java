package ru.moskvin.files.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileActionsService {
    //удаление
    public static boolean deleteFile(String fileName) throws FileNotFoundException, Exception{
        if (Files.exists(Paths.get(fileName))) {
            Files.delete(Paths.get(fileName));
            return true;
        }
        else
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
        File destFile = new File(destPath);
        if (!sourceFile.isDirectory())
            Files.copy(sourceFile.toPath(), destFile.toPath());
        else {
            destFile.mkdir();
            for (File file : sourceFile.listFiles())
                Files.copy(file.toPath(), destFile.toPath());
        }
    }
    //перемещение
    public static void moveFileOrDirectory (String fileName, String destPath) throws IOException {
        File sourceFile = new File(fileName);
        File destFile = new File(destPath + "/"+sourceFile.getName());

        sourceFile.renameTo(destFile);
      /*  if (!sourceFile.isDirectory())
            Files.move(sourceFile.toPath(), destFile.toPath());
        else {
            destFile.mkdir();
            for (File file : sourceFile.listFiles())
                Files.move(Paths.get(fileName), Paths.get(destPath)); */
      //  }
    }
}
