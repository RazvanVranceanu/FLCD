package uni.flcd.scanner;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import uni.flcd.constants.Delimiters;
import uni.flcd.exception.LexicalException;
import uni.flcd.exceptions.ExistentElementException;
import uni.flcd.scanner.repository.ProgramInternalForm;
import uni.flcd.scanner.repository.ReservedWords;
import uni.flcd.scanner.scannerUtils.ReadInputUtils;
import uni.flcd.scanner.scannerUtils.WriteOutputUtils;
import uni.flcd.structures.SymbolTable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static uni.flcd.scanner.scannerUtils.ScannerUtils.*;

@Slf4j
public class Scanner {

    public static final String EMPTY_STRING = "";
    private static final String SYMBOL_TABLE_OUTPUT_FILE = "src/main/resources/ST.out";
    private static final String PIF_OUTPUT_FILE = "src/main/resources/PIF.out";

    private final SymbolTable symbolTable;
    private final ProgramInternalForm programInternalForm;
    private final ReservedWords reservedWords;
    private Map<Integer, String> lines;

    public Scanner(SymbolTable symbolTable,ProgramInternalForm programInternalForm, ReservedWords reservedWords) {
        this.symbolTable = symbolTable;
        this.programInternalForm = programInternalForm;
        this.reservedWords = reservedWords;
        this.lines = new HashMap<>();
    }

    public void scan(String pathToFile) throws LexicalException {
        lines = ReadInputUtils.readInput(pathToFile);
        for (Integer key : lines.keySet()) {
            scanLine(key, lines.get(key));
        }
        writeOutputs();
    }

    private void writeOutputs() {
        WriteOutputUtils.writeOutput(SYMBOL_TABLE_OUTPUT_FILE, symbolTable);
        WriteOutputUtils.writeOutput(PIF_OUTPUT_FILE, programInternalForm);
    }

    private void scanLine(Integer lineNumber, String line) throws LexicalException {
        StringTokenizer st = new StringTokenizer(line, Delimiters.DELIMITERS, true);
        ArrayList<String> tokens = getAllTokens(st);

        for (int i = 0; i < tokens.size(); i++) {
            analyzeToken(lineNumber, tokens, i);
        }
    }

    private void analyzeToken(Integer lineNumber, ArrayList<String> tokens, int i) {
        String currentToken = tokens.get(i).strip();

        if (isUnifiedToken(tokens, i, currentToken)) {
            processUnifiedToken(tokens, i, currentToken);
        } else if (isReservedWord(currentToken)) {
            processReservedWord(currentToken);
        } else if (isIdentifier(currentToken)) {
            processIdentifier(currentToken);
        } else if (isConstant(currentToken)) {
            processConstant(currentToken);
        } else if (!EMPTY_STRING.equals(currentToken)){
            throw new LexicalException("Illegal token definition \"" + currentToken + "\" at line " + lineNumber);
        }
    }

    private void processReservedWord(String currentToken) {
        programInternalForm.addReservedWord(currentToken);
        log.info("Successfully added reserved token {} to PIF", currentToken);
    }

    private void processUnifiedToken(ArrayList<String> tokens, int i, String currentToken) {
        String unifiedToken = currentToken + tokens.get(i + 1);
        programInternalForm.addReservedWord(unifiedToken);
        log.info("Successfully added unified token {} to PIF", unifiedToken);
    }

    private void processConstant(String currentToken) {
        try {
            symbolTable.put(currentToken);
            Optional<Pair<Integer, AtomicInteger>> positionInST = symbolTable.getPositionForValue(currentToken);
            positionInST.ifPresent(programInternalForm::addConstant);
            log.info("Successfully added constant {} to ST and PIF", currentToken);
        } catch (ExistentElementException exception) {
            Optional<Pair<Integer, AtomicInteger>> positionInST = symbolTable.getPositionForValue(currentToken);
            positionInST.ifPresent(programInternalForm::addConstant);
            log.warn("Constant {} was already in symbol table", currentToken);
            log.info("Successfully added constant {} to PIF", currentToken);
        }
    }

    private void processIdentifier(String currentToken) {
        try {
            symbolTable.put(currentToken);
            Optional<Pair<Integer, AtomicInteger>> positionInST = symbolTable.getPositionForValue(currentToken);
            positionInST.ifPresent(programInternalForm::addIdentifier);
            log.info("Successfully added identifier {} to ST and PIF", currentToken);
        } catch (ExistentElementException exception) {
            Optional<Pair<Integer, AtomicInteger>> positionInST = symbolTable.getPositionForValue(currentToken);
            positionInST.ifPresent(programInternalForm::addIdentifier);
            log.warn("Identifier {} was already in symbol table", currentToken);
            log.info("Successfully added identifier {} to PIF", currentToken);
        }
    }

    private boolean isReservedWord(String currentToken) {
        return reservedWords.contains(currentToken) && !Delimiters.SPACE.equals(currentToken);
    }

    private ArrayList<String> getAllTokens(StringTokenizer st) {
        ArrayList<String> tokens = new ArrayList<>();

        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return tokens;
    }
}
