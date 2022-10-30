package uni.flcd.scanner.repository;

import lombok.ToString;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ToString
public class ReservedWords {

    private final List<String> reservedWords;

    public ReservedWords(String pathToFile) {
        reservedWords = new ArrayList<>();
        parseTokens(pathToFile);
    }

    private void parseTokens(String pathToFile) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(pathToFile));
            String line = reader.readLine();
            while (line != null) {
                reservedWords.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String value) {
        return reservedWords.contains(value);
    }
}
