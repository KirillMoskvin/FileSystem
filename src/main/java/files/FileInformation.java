package files;

import org.apache.tomcat.jni.FileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileInformation {
    //информация о файле
    private BasicFileAttributes fileInfo;

    public FileInformation(String filePath) throws IOException{
        Path file = Paths.get(filePath);
        fileInfo = Files.readAttributes(file,BasicFileAttributes.class);
        long sz = fileInfo.size();
        FileTime ft = fileInfo.creationTime();
        FileTime ft2 = fileInfo.lastModifiedTime();
        boolean isd = fileInfo.isDirectory();
    }

    //Размер файла
    public long getFileSize(){
        return fileInfo.size();
    }
    //Дата создания
    public Date getCreationDate(){
        return  new Date(fileInfo.creationTime().toMillis());
    }
    //Дата последнего изменения
    public Date getModificationDate(){
        return new Date(fileInfo.lastModifiedTime().toMillis());
    }
    //Является ли файл директорией
    public boolean isDirectory(){
        return fileInfo.isDirectory();
    }
}
