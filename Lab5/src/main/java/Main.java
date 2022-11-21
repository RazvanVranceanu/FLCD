import grammar.Grammar;

public class Main {

    private static final String INPUT_FILE = "src/main/resources/grammar.in";

    public static void main(String[] args) {
        Grammar grammar = Grammar.builder()
                .fileName(INPUT_FILE)
                .build();
        grammar.aggregateData();
        System.out.println(grammar);
    }
}
