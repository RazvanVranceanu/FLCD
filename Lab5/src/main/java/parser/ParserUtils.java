package parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import parser.model.Configuration;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ParserUtils {

    public static boolean headEqualsCurrentSymbol(List<String> word, Configuration configuration) {
        return configuration.getInputStack().peek().equals(word.get(configuration.getIndex().get()));
    }

    public static boolean isIndexInbounds(List<String> word, Configuration configuration) {
        return configuration.getIndex().get() < word.size();
    }

    public static boolean isWordParsed(List<String> word, Configuration configuration) {
        return word.size() == configuration.getIndex().get();
    }
}
