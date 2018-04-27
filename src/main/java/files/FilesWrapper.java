package files;

import java.io.File;
import java.util.List;

public class FilesWrapper {
    private final List<File> files;

    public FilesWrapper(List<File> files){
        this.files=files;
    }
    public List<File> getFiles() {
        return files;
    }

}
