package grammar.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrammarUtils {

    public static Stream<String> getElementsFromLine(String line, String separator) {
        return Arrays.stream(line.split(separator));
    }
}
