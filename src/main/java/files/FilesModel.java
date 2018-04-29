package files;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FilesModel {
    private final List<FileModel> files;

    public FilesModel(List<FileModel> files){
        this.files=files;
    }

    public FilesModel(String path) throws IOException {
        File file = new File(path);
        File[] arrF = file.listFiles();
        files = new LinkedList<>();
        for(File f: arrF){
            files.add(new FileModel(f.getPath()));
        }
    }

    public List<FileModel> getFiles() {
        return files;
    }

}
