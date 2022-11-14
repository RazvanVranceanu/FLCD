package uni.flcd.scanner.scannerUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadInputUtils {
    private static final AtomicInteger numberOfLines = new AtomicInteger(0);
    private static final Map<Integer, String> lineMap = new HashMap<>();

    public static Map<Integer, String> readInput(String pathToFile) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(pathToFile));
            String line = reader.readLine();
            while (line != null) {
                lineMap.put(numberOfLines.getAndIncrement(), line);
                line = reader.readLine();
            }
            reader.close();
            log.info("Successfully read from file {}", pathToFile);
        } catch (IOException e) {
            log.error("Failed to read from file {}", pathToFile);
            e.printStackTrace();
        }
        return lineMap;
    }
}
