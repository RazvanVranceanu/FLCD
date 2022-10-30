package uni.flcd.scanner.scannerUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class WriteOutputUtils {
    private static void createFile(String fileName) throws IOException {
        File myObj = new File(fileName);
        if (myObj.createNewFile()) {
            log.info("File created: {}", myObj.getName());
        } else {
            log.warn("File {} already exists.", fileName);
        }
    }

    public static void writeOutput(String fileName, Object objectToWrite) {
        try {
            createFile(fileName);
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(objectToWrite.toString());
            myWriter.close();
            log.info("Successfully wrote to the file {}.", fileName);
        } catch (IOException e) {
            log.error("An error occurred while writing to file {}", fileName);
            e.printStackTrace();
        }
    }
}
