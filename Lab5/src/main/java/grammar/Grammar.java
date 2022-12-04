package grammar;

import grammar.exceptions.InvalidCfg;
import grammar.model.Production;
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
import static grammar.utils.GrammarUtils.getElementsFromLine;

@AllArgsConstructor
@Builder
@Data
@ToString
@Slf4j
public class Grammar {

    @Default
    private Set<String> nonTerminals = new HashSet<>();
    @Default
    private Set<String> terminals = new HashSet<>();
    @Default
    private String startingSymbol = "";
    @Default
    private Map<String, List<Production>> productions = new HashMap<>();
    @ToString.Exclude
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
        productions.put("epsilon", List.of(Production.builder().productionNodes(List.of("")).build()));
        readFromInputStream().forEach(line -> {
            if (line.startsWith(NON_TERMINAL)) {
                getElementsFromLine(line.substring(2), COLUMN)
                        .collect(Collectors.toCollection(() -> nonTerminals));
                log.info("Successfully aggregated non terminals = {}", nonTerminals);
            } else if (line.startsWith(TERMINAL)) {
                getElementsFromLine(line.substring(2), COLUMN)
                        .collect(Collectors.toCollection(() -> terminals));
                log.info("Successfully aggregated terminals = {}", terminals);
            } else if (line.startsWith(START_SYMBOL)) {
                if (!startingSymbol.isEmpty()) {
                    throw new InvalidCfg("Invalid CFG: Can't have more than one starting point.");
                }
                startingSymbol = line.substring(2);
                log.info("Successfully aggregated starting symbol = {}", startingSymbol);
            } else {
                String leftHandSide = line.split("#")[0];
                String rightHandSide = line.split("#")[1];
                List<Production> productionsForKey = parseProductionsForKey(rightHandSide);
                productions.put(leftHandSide, productionsForKey);
                log.info("Successfully aggregated productions {} for key={}", productionsForKey, leftHandSide);
            }
        });
        if (!isCFG()) {
            throw new InvalidCfg("Grammar is not context free.");
        }
        log.info("Successfully aggregated grammar from file");
    }

    private List<Production> parseProductionsForKey(String rightHandSide) {
        return getElementsFromLine(rightHandSide, "\\|")
                .map(production -> Production.builder()
                        .productionNodes(getElementsFromLine(production, ",").toList())
                        .build())
                .toList();
    }

    public Optional<List<Production>> getProductionForKey(String key) {
        return Optional.ofNullable(productions.get(key));
    }


    public Optional<Production> getProductionForKeyAndIndex(String key, int index) {
        try {
            return Optional.ofNullable(productions.get(key)
                    .get(index));
        } catch (ArrayIndexOutOfBoundsException exception) {
            return Optional.empty();
        }
    }

    private boolean isCFG() {
        return productions.keySet()
                .stream()
                .noneMatch(key -> key.split(",").length > 1 || terminals.contains(key));
    }

    public boolean hasMoreProductions(String key, int index) {
        return index < productions.get(key).size() - 1;
    }

    public boolean isNonTerminal(String element) {
        return nonTerminals.contains(element);
    }

    public boolean isTerminal(String element) {
        return terminals.contains(element);
    }

    public boolean isStartingSymbol(String element) {
        return startingSymbol.equals(element);
    }
}
