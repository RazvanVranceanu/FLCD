import grammar.Grammar;
import org.apache.commons.lang3.tuple.Pair;
import parser.Parser;
import uni.flcd.scanner.Scanner;
import uni.flcd.scanner.repository.ProgramInternalForm;
import uni.flcd.scanner.repository.ReservedWords;
import uni.flcd.structures.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String GRAMMAR_INPUT_FILE = "src/main/resources/grammar2.in";
    private static final String RESERVED_WORDS_INPUT = "src/main/resources/token.in";
    private static final String MY_PROGRAM_INPUT_FILE = "src/main/resources/p3.txt";

    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        ProgramInternalForm programInternalForm = new ProgramInternalForm();
        ReservedWords reservedWords = new ReservedWords(RESERVED_WORDS_INPUT);
        Scanner scanner = new Scanner(symbolTable, programInternalForm, reservedWords);
        scanner.scan(MY_PROGRAM_INPUT_FILE);

        Grammar grammar = Grammar.builder()
                .fileName(GRAMMAR_INPUT_FILE)
                .build();
        grammar.aggregateData();

        Parser parser = Parser.builder().grammar(grammar).build();

        List<String> word = new ArrayList<>();

        programInternalForm.getProgramInternalForm()
                .stream()
                .map(Pair::getLeft)
                .collect(Collectors.toCollection(() -> word));

        parser.parse(word);
    }
}
