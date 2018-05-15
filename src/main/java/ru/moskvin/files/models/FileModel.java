package ru.moskvin.files.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileModel {
    //путь к файлу
    Path path;
    //информация о файле
    private BasicFileAttributes fileInfo;

    public FileModel(String filePath) throws IOException {
        path = Paths.get(filePath);
        fileInfo = Files.readAttributes(path, BasicFileAttributes.class);
        long sz = fileInfo.size();
        FileTime ft = fileInfo.creationTime();
        FileTime ft2 = fileInfo.lastModifiedTime();
        boolean isd = fileInfo.isDirectory();
    }

    //Имя файла
    public String getName() {
        return path.getFileName().toString();
    }

    //Размер файла
    public long getFileSize() {
        return fileInfo.size();
    }

    //Дата создания
    public Date getCreationDate() {
        return new Date(fileInfo.creationTime().toMillis());
    }

    //Дата последнего изменения
    public Date getModificationDate() {
        return new Date(fileInfo.lastModifiedTime().toMillis());
    }

    //Является ли файл директорией
    public boolean isDirectory() {
        return fileInfo.isDirectory();
    }

    //Является ли файл текстовым
    public boolean isText() {
        String extension = "";
        String name = this.getName();
        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = name.substring(i + 1);
        }
        return extension.equals("txt");
    }

    //Абсолютный путь к файлу
    public String getAbsolutePath() {
        return path.toAbsolutePath().toString();
    }

}
