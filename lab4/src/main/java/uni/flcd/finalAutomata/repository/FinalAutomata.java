package uni.flcd.finalAutomata.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import uni.flcd.finalAutomata.exceptions.IllegalAlphabet;
import uni.flcd.finalAutomata.exceptions.IllegalState;
import uni.flcd.finalAutomata.model.State;
import uni.flcd.finalAutomata.model.Transition;

import java.util.*;

import static uni.flcd.finalAutomata.model.StateType.FINAL;
import static uni.flcd.finalAutomata.model.StateType.INITIAL;

@NoArgsConstructor
@Data
public class FinalAutomata {
    private final Set<String> alphabet = new HashSet<>();
    private final Map<State, List<Transition>> states = new HashMap<>();

    public void addState(State state) {
        states.putIfAbsent(state, new ArrayList<>());
    }

    public State getStateForLabel(String label) {
        return states.keySet().stream()
                .filter(state -> state.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalState("Illegal label for a state: " + label));
    }

    public void addTransition(final State srcState, final State destState, final String value) {
        if (!alphabet.contains(value)) {
            throw new IllegalAlphabet("Illegal alphabet value: " + value);
        }

        if (!states.containsKey(srcState)) {
            throw new IllegalState("Illegal state value: " + srcState);
        }

        if (!states.containsKey(destState)) {
            throw new IllegalState("Illegal state value: " + destState);
        }

        states.get(srcState).add(Transition.builder()
                .destState(destState)
                .value(value)
                .build());
    }

    public void addToAlphabet(String token) {
        alphabet.add(token);
    }

    private State getStartingState() {
        return states.keySet()
                .stream()
                .filter(key -> INITIAL.equals(key.getStateType()))
                .findFirst()
                .orElseThrow(() -> new IllegalState("No starting state"));
    }

    public boolean validateWord(String word) {
        State currentState = getStartingState();

        var index = 0;
        while (index < word.length()) {
            int finalIndex = index;
            Optional<Transition> nextTransition = states.get(currentState)
                    .stream()
                    .filter(transition -> {
                        String currentCharacter = String.valueOf(word.charAt(finalIndex));
                        return transition.getValue().equals(currentCharacter);
                    })
                    .findFirst();

            if (nextTransition.isEmpty()) {
                return false;
            }

            index++;
            currentState = nextTransition.get().getDestState();
        }
        return FINAL.equals(currentState.getStateType());
    }
}
