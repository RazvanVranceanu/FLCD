package uni.flcd.scanner;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import uni.flcd.constants.ScannerConstants;
import uni.flcd.exception.LexicalException;
import uni.flcd.exception.OutOfPlaceOperand;
import uni.flcd.exceptions.ExistentElementException;
import uni.flcd.finalAutomata.FinalAutomataProcessor;
import uni.flcd.scanner.repository.ProgramInternalForm;
import uni.flcd.scanner.repository.ReservedWords;
import uni.flcd.scanner.scannerUtils.ReadInputUtils;
import uni.flcd.scanner.scannerUtils.WriteOutputUtils;
import uni.flcd.structures.SymbolTable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static uni.flcd.constants.ScannerConstants.*;
import static uni.flcd.scanner.scannerUtils.ScannerUtils.*;

@Slf4j
public class Scanner {

    private static final String SYMBOL_TABLE_OUTPUT_FILE = "src/main/resources/ST.out";
    private static final String PIF_OUTPUT_FILE = "src/main/resources/PIF.out";

    private final SymbolTable symbolTable;
    private final ProgramInternalForm programInternalForm;
    private final ReservedWords reservedWords;
    private Map<Integer, String> lines;

    private final FinalAutomataProcessor finiteAutomataIntConst;

    public Scanner(SymbolTable symbolTable,ProgramInternalForm programInternalForm, ReservedWords reservedWords, FinalAutomataProcessor finiteAutomataIntConst) {
        this.symbolTable = symbolTable;
        this.programInternalForm = programInternalForm;
        this.reservedWords = reservedWords;
        this.lines = new HashMap<>();
        this.finiteAutomataIntConst = finiteAutomataIntConst;
        this.finiteAutomataIntConst.processLines();
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
        StringTokenizer st = new StringTokenizer(line, ScannerConstants.DELIMITERS, true);
        ArrayList<String> tokens = getAllTokens(st);

        for (int i = 0; i < tokens.size(); i++) {
            analyzeToken(lineNumber, tokens, i);
        }
    }

    private void analyzeToken(Integer lineNumber, ArrayList<String> tokens, int i) {
        String currentToken = tokens.get(i).strip();
        Optional<String> oneBeforePrevToken = 0 <= i - 2 ? Optional.ofNullable(tokens.get(i - 2)) : Optional.empty();
        Optional<String> prevToken = 0 <= i - 1 ? Optional.ofNullable(tokens.get(i - 1)) : Optional.empty();
        Optional<String> nextToken = i + 1 < tokens.size() ? Optional.ofNullable(tokens.get(i + 1)) : Optional.empty();

        try {
            if (isUnifiedToken(currentToken, nextToken.orElse(EMPTY_STRING))) {
                processUnifiedToken(currentToken, nextToken.orElse(EMPTY_STRING));
            } else if (isReservedWord(prevToken.orElse(EMPTY_STRING), currentToken)) {
                processReservedWord(prevToken.orElse(EMPTY_STRING), currentToken);
            } else if (isIdentifier(currentToken)) {
                processIdentifier(currentToken);
            } else if (isStringConstant(currentToken)) {
                processConstant(currentToken);
            } else if (isNegativeIntegerConstant(oneBeforePrevToken.orElse(EMPTY_STRING), prevToken.orElse(EMPTY_STRING), currentToken)) {
                processConstant(prevToken.orElse(EMPTY_STRING) + currentToken);
            } else if (isIntegerConstant(currentToken)) {
                processConstant(currentToken);
            } else if (!EMPTY_STRING.equals(currentToken)) {
                throw new LexicalException("Illegal token definition \"" + currentToken + "\" at line " + lineNumber);
            }
        } catch(OutOfPlaceOperand exception) {
            throw new LexicalException(exception.getMessage() + " at line " + lineNumber);
        }
    }

    private boolean isIntegerConstantWithFA(final String currentToken) {
        return finiteAutomataIntConst.validateWord(currentToken);
    }

    private void processReservedWord(String prevToken, String currentToken) {
        if(isUnifiedToken(prevToken, currentToken) ||
            MINUS.equals(currentToken) && EQUALS.equals(prevToken)) {
            log.info("Avoided wrongfully adding reserved word {} to PIF", currentToken);
        } else {
            programInternalForm.addReservedWord(currentToken);
            log.info("Successfully added reserved token {} to PIF", currentToken);
        }
    }

    private void processUnifiedToken(String currentToken, String nextToken) {
        String unifiedToken = currentToken + nextToken;
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

    private boolean isReservedWord(String prevToken, String currentToken) {
        if(PLUS.equals(currentToken) && EQUALS.equals(prevToken)) {
            throw new OutOfPlaceOperand("Illegal token definition " + "\"" + currentToken + "\"");
        }
        return reservedWords.contains(currentToken) && !SPACE.equals(currentToken);
    }

    private ArrayList<String> getAllTokens(StringTokenizer st) {
        ArrayList<String> tokens = new ArrayList<>();

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (!SPACE.equals(token)) {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
