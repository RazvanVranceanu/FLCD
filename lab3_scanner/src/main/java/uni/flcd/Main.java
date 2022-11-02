package uni.flcd;

import uni.flcd.scanner.Scanner;
import uni.flcd.scanner.repository.ProgramInternalForm;
import uni.flcd.scanner.repository.ReservedWords;
import uni.flcd.structures.SymbolTable;

public class Main {
    public static void main(String[] args) {
        // TODO: 02.11.2022 mai verifica daca ST si PIF sunt ok pt toate exemplele
        // TODO: 02.11.2022 versiunea finala la programe
        // TODO: 02.11.2022 documentatie
        SymbolTable symbolTable = new SymbolTable();
        ProgramInternalForm programInternalForm = new ProgramInternalForm();
        ReservedWords reservedWords = new ReservedWords("src/main/resources/token.in");
        Scanner scanner = new Scanner(symbolTable, programInternalForm, reservedWords);
        scanner.scan("src/main/resources/p3.txt");
    }
}