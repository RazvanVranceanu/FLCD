package parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import parser.model.enums.State;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static parser.model.enums.State.NORMAL;

@AllArgsConstructor
@Builder
@Data
@ToString
public class Configuration {

    @Builder.Default
    private State state = NORMAL;
    @Builder.Default
    private AtomicInteger index = new AtomicInteger();
    @Builder.Default
    private Stack<TransitionElement> workingStack = new Stack<>();
    @Builder.Default
    private Stack<String> inputStack = new Stack<>();


    public boolean isErrorState() {
        return State.ERROR.equals(state);
    }

    public boolean isFinalState() {
        return State.FINAL.equals(state);
    }

    public boolean isNormalState() {
        return State.NORMAL.equals(state);
    }

    public boolean isBackState() {
        return State.BACK.equals(state);
    }

    public boolean isInputStackEmpty() {
        return inputStack.empty();
    }
}
