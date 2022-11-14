package uni.flcd;

import uni.flcd.finalAutomata.FinalAutomataProcessor;
import uni.flcd.finalAutomata.repository.FinalAutomata;
import uni.flcd.scanner.Scanner;
import uni.flcd.scanner.repository.ProgramInternalForm;
import uni.flcd.scanner.repository.ReservedWords;
import uni.flcd.structures.SymbolTable;

public class Main {

    private static final String TOKENS_INPUT = "src/main/resources/token.in";
    private static final String FINITE_AUTOMATA_INPUT_CONST = "src/main/resources/faIntegerConstant.txt";
    private static final String SCANNER_INPUT = "src/main/resources/p3.txt";


    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();

        ProgramInternalForm programInternalForm = new ProgramInternalForm();
        ReservedWords reservedWords = new ReservedWords(TOKENS_INPUT);

        FinalAutomataProcessor finalAutomataIntConst = new FinalAutomataProcessor(FINITE_AUTOMATA_INPUT_CONST, new FinalAutomata());

        Scanner scanner = new Scanner(symbolTable, programInternalForm, reservedWords, finalAutomataIntConst);
        scanner.scan(SCANNER_INPUT);
    }
}