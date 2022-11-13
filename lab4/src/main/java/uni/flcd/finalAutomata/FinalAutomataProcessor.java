package uni.flcd.finalAutomata;

import lombok.extern.slf4j.Slf4j;
import uni.flcd.finalAutomata.model.State;
import uni.flcd.finalAutomata.repository.FinalAutomata;
import uni.flcd.scanner.scannerUtils.ReadInputUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static uni.flcd.finalAutomata.model.StateType.*;

@Slf4j
public class FinalAutomataProcessor {

    private final Map<Integer, String> lines;
    private final FinalAutomata finalAutomata;

    public FinalAutomataProcessor(String pathToFile, FinalAutomata finalAutomata) {
        this.finalAutomata = finalAutomata;
        this.lines = ReadInputUtils.readInput(pathToFile);
    }

    public void processLines() {
        lines.forEach((lineNumber, line) -> {
            switch (lineNumber) {
                case 0 -> processAlphabet(line);
                case 1 -> processNodes(line);
                case 2 -> processInitialState(line);
                case 3 -> processFinalStates(line);
                default -> processTransition(line);
            }
        });
        System.out.println(finalAutomata);
    }

    private void processAlphabet(String line) {
        Collections.list(new StringTokenizer(line, ",")).stream()
                .map(token -> (String) token)
                .toList()
                .forEach(finalAutomata::addToAlphabet);
    }

    private void processInitialState(String line) {
        finalAutomata.getStateForLabel(line)
                .setStateType(INITIAL);
    }

    private void processFinalStates(String line) {
        Collections.list(new StringTokenizer(line, ",")).stream()
                .map(token -> (String) token)
                .toList()
                .forEach(token -> finalAutomata.getStateForLabel(token)
                        .setStateType(FINAL));
    }

    private void processNodes(String line) {
        Collections.list(new StringTokenizer(line, ",")).stream()
                .map(token -> (String) token)
                .toList()
                .forEach(token -> finalAutomata.addState(
                        State.builder()
                                .label(token)
                                .stateType(INTERMEDIARY)
                                .build()));
    }

    private void processTransition(String line) {
        List<String> transitionValues = Collections.list(new StringTokenizer(line, ",")).stream()
                .map(token -> (String) token)
                .toList();
        State srcState = finalAutomata.getStateForLabel(transitionValues.get(0));
        State destState = finalAutomata.getStateForLabel(transitionValues.get(2));
        String value = transitionValues.get(1);

        finalAutomata.addTransition(srcState, destState, value);
    }

    public boolean validateWord(String word) {
        return finalAutomata.validateWord(word);
    }
}
