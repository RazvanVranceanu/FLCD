package uni.flcd.scanner;

import uni.flcd.constants.Delimiters;
import uni.flcd.constants.RegexConstants;
import uni.flcd.exception.LexicalException;
import uni.flcd.scannerUtils.ReadInputUtils;
import uni.flcd.structures.SymbolTable;

import java.util.*;

public class Scanner {
    private static final String STRING = "=";
    private SymbolTable symbolTable;
    private Map<Integer, String> lines;

    public Scanner(SymbolTable symbolTable) {
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
        String stripedLine = line.strip();
        StringTokenizer st = new StringTokenizer(stripedLine, Delimiters.DELIMITERS, true);
//        ArrayList<String> tokens = getAllTokens(st);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            //TODO: verifica prima data daca este in lista de tokeni definiti

            // verificam edge case-ul cu == >= <= !=
            if(token.matches(RegexConstants.SPLIT_REGEX)) {
                String secondToken = st.nextToken();
                if(STRING.equals(secondToken)) {
                    //TODO: unify the two tokens and put in pif
                    String unifiedToken = token + secondToken;
                    System.out.println(unifiedToken);
                }
                System.out.println(token);
                System.out.println(secondToken);
            } else {
                System.out.println(token);
            }

            // verificam daca este identifier
            if (isIdentifier(token)) {

            } else {
                throw new LexicalException("Illegal identifier definition " + token + " at line " + lineNumber);
            }
        }
    }

    private ArrayList<String> getAllTokens(StringTokenizer st) {
        ArrayList<String> tokens = new ArrayList<>();
        while(st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return tokens;
    }
}
