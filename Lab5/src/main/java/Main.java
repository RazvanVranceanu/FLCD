import grammar.Grammar;

public class Main {

    private static final String INPUT_FILE = "src/main/resources/grammar.in";

    public static void main(String[] args) {
        Grammar grammar = Grammar.builder()
                .fileName(INPUT_FILE)
                .build();
        grammar.aggregateData();

        System.out.println("Non terminals: " + grammar.getNonTerminals());
        System.out.println("Terminals: " + grammar.getTerminals());
        System.out.println("Starting symbol: " + grammar.getStartingSymbol());
        System.out.println("Productions: ");
        grammar.getProductions()
                .forEach(((key, productions) -> System.out.println(key + " --> " + productions)));
    }
}
