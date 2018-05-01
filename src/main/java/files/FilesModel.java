package files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FilesModel {
    private final List<FileModel> files;
    private Path path;

    public FilesModel(List<FileModel> files){
        this.files=files;
    }

    public FilesModel(String path) throws IOException {
        this.path= Paths.get(path);
        File file = new File(path);
        File[] arrF = file.listFiles();
        files = new LinkedList<>();
        for(File f: arrF){
            files.add(new FileModel(f.getPath()));
        }
    }
    //файлы по заданному пути
    public List<FileModel> getFiles() {
        return files;
    }
    //путь
    public String getPath(){ return path.toAbsolutePath().toString(); }
}
