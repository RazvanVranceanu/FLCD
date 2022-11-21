package grammar;

import ch.qos.logback.core.joran.sanity.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static grammar.utils.GrammarConstants.*;

@AllArgsConstructor
@Builder
@Data
@ToString
@Slf4j
public class Grammar {

    private static final String COLUMN = ",";
    @Default
    private Set<String> nonTerminals = new HashSet<>();
    @Default
    private Set<String> terminals = new HashSet<>();
    @Default
    private String startingSymbol = "";
    @Default
    private Set<Pair<String, String>> productions = new HashSet<>();
    @Default
    private Map<String, String> nonTerminalToProduction = new HashMap<>();

    private String fileName;

    private Stream<String> readFromInputStream() {
        try {
            log.info("Trying to read from file {}", fileName);
            return Files.lines(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void aggregateData() {
        log.info("Trying to aggregate grammar from file");
        readFromInputStream().forEach(line -> {
            if (line.startsWith(NON_TERMINAL)) {
                Arrays.stream(line.substring(2)
                                .split(COLUMN))
                        .collect(Collectors.toCollection(() -> nonTerminals));
            } else if (line.startsWith(TERMINAL)) {
                Arrays.stream(line.substring(2)
                                .split(COLUMN))
                        .collect(Collectors.toCollection(() -> terminals));
            } else if (line.startsWith(START_SYMBOL)) {
                startingSymbol = line;
            } else{
                String key = line.split("-")[0];
                String[] productions = line.split("-")[1]
                        .split("\\|");
            }
        });
    }
}
