package parser;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import parser.model.Configuration;

import java.util.Stack;

@NoArgsConstructor
@Slf4j
public class Parser {

    public void expand(Configuration configuration) {

    }

    public void advance(Configuration configuration) {
        Stack<String> workingStack = configuration.getWorkingStack();
        Stack<String> inputStack = configuration.getInputStack();
        workingStack.push(inputStack.pop());
    }

    public void momentaryInsuccess(Stack<String> workingStack, Stack<String> inputStack) {

    }

    public void back(Stack<String> workingStack, Stack<String> inputStack) {

    }
}
