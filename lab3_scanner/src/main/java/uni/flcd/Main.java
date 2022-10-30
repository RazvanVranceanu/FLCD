package uni.flcd;

import uni.flcd.scanner.Scanner;
import uni.flcd.structures.SymbolTable;

public class Main {
    //TODO: add spring boot (maybe ?)
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        Scanner scanner = new Scanner(symbolTable);
        scanner.scan("src/main/resources/p2.txt");
    }
}