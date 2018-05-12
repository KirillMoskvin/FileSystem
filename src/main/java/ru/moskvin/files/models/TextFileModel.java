package ru.moskvin.files.models;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class TextFileModel extends FileModel {
    //Содержимое
    private String content = "";

    public TextFileModel(String filePath) throws IOException {
        super(filePath);
        List<String> lines = Files.readAllLines(path);
        StringBuilder contentBuilder = new StringBuilder();
        for (String line : lines) {
            contentBuilder.append(line);
            contentBuilder.append("\n");
        }
        content = contentBuilder.toString();
    }

    //доступ к содержимому
    public String getContent() {
        return content;
    }
}
