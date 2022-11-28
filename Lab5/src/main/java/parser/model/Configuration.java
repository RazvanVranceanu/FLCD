package parser.model;

import lombok.*;

import java.util.Stack;

import static parser.model.State.NORMAL;

@AllArgsConstructor
@Builder
@Data
public class Configuration {

    private State state;
    private Integer index;
    private Stack<String> workingStack;
    private Stack<String> inputStack;

    public Configuration() {
        state = NORMAL;
        index = 0;
        workingStack = new Stack<>();
        inputStack = new Stack<>();
    }
}
