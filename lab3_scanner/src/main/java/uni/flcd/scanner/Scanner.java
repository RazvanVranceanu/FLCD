package uni.flcd.scanner;

import uni.flcd.exception.LexicalException;
import uni.flcd.scannerUtils.ReadInputUtils;
import uni.flcd.structures.SymbolTable;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Scanner {
    private SymbolTable<Integer, String> symbolTable;
    private Map<Integer, String> lines;

    public Scanner(SymbolTable<Integer, String> symbolTable) {
        this.symbolTable = symbolTable;
        this.lines = new HashMap<>();
    }

    public void scan(String pathToFile) throws LexicalException{
        lines = ReadInputUtils.readInput(pathToFile);
        for (Integer key : lines.keySet()) {
            scanLine(key, lines.get(key));
        }
    }

    private void scanLine(Integer lineNumber, String line) throws LexicalException {
        StringTokenizer st = new StringTokenizer(line, ""); //TODO: boolean + separators
        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
    }
}
