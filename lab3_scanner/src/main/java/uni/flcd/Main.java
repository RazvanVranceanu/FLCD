package uni.flcd;

import uni.flcd.scanner.Scanner;
import uni.flcd.scanner.repository.ProgramInternalForm;
import uni.flcd.scanner.repository.ReservedWords;
import uni.flcd.structures.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        ProgramInternalForm programInternalForm = new ProgramInternalForm();
        ReservedWords reservedWords = new ReservedWords("src/main/resources/token.in");
        Scanner scanner = new Scanner(symbolTable, programInternalForm, reservedWords);
        scanner.scan("src/main/resources/p3.txt");
    }
}