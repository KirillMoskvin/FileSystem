package files.models;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class TextFileModel extends FileModel{
    //Содержимое
    private String content="";

    public TextFileModel(String filePath) throws IOException {
        super(filePath);
        List<String> lines = Files.readAllLines(path);
        for (String line: lines) {
            content+=line+"\n";
        }
    }
    //доступ к содержимому
    public String getContent() {
        return content;
    }
}
