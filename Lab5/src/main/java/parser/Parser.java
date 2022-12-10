package parser;

import com.google.common.collect.Lists;
import grammar.Grammar;
import grammar.model.Production;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import parser.model.Configuration;
import parser.model.TransitionElement;
import parser.model.enums.State;
import parser.model.enums.Type;

import java.util.List;
import java.util.Stack;

@AllArgsConstructor
@Builder
@Slf4j
public class Parser {

    Grammar grammar;

    public void expand(Configuration configuration) {
        log.info("Trying to apply expand function on configuration {}", configuration);
        Stack<TransitionElement> workingStack = configuration.getWorkingStack();
        Stack<String> inputStack = configuration.getInputStack();

        var nonTerminalHead = inputStack.pop();

        grammar.getProductionForKeyAndIndex(nonTerminalHead, 0)
                .ifPresent(production -> Lists.reverse(production.getProductionNodes())
                        .forEach(inputStack::push));

        workingStack.push(TransitionElement.builder()
                .value(nonTerminalHead)
                .index(0)
                .type(Type.NONTERMINAL)
                .build());

        log.info("Successfully applied expand function with new configuration {}", configuration);

    }

    public void advance(Configuration configuration) {
        log.info("Trying to apply advance function on configuration {}", configuration);
        Stack<TransitionElement> workingStack = configuration.getWorkingStack();
        Stack<String> inputStack = configuration.getInputStack();

        configuration.getIndex().getAndIncrement();

        workingStack.push(TransitionElement.builder()
                .value(inputStack.pop())
                .type(Type.TERMINAL)
                .build());

        log.info("Successfully applied advance function with new configuration {}", configuration);

    }

    public void momentaryInsuccess(Configuration configuration) {
        log.info("Trying to apply momentary insuccess function to configuration {}", configuration);
        configuration.setState(State.BACK);
        log.info("Successfully applied momentary insuccess function with new configuration {}", configuration);

    }

    public void back(Configuration configuration) {
        log.info("Trying to apply back function to configuration {}", configuration);

        String headWorkingStack = configuration.getWorkingStack().pop().getValue();

        configuration.getIndex().getAndDecrement();
        configuration.getInputStack().push(headWorkingStack);
        log.info("Successfully applied back function with new configuration {}", configuration);
    }

    public void anotherTry(Configuration configuration) {
        log.info("Trying to apply another try function for configuration {}", configuration);
        Stack<TransitionElement> workingStack = configuration.getWorkingStack();
        Stack<String> inputStack = configuration.getInputStack();

        TransitionElement headWorkingStack = workingStack.pop();
        var productionSize = grammar.getProductionForKeyAndIndex(headWorkingStack.getValue(), headWorkingStack.getIndex())
                .orElse(Production.builder().build())
                .getProductionNodes()
                .size();
        for (int i = 0; i < productionSize; i++) {
            inputStack.pop();
        }

        if (grammar.hasMoreProductions(headWorkingStack.getValue(), headWorkingStack.getIndex())) {
            workingStack.push(TransitionElement.builder()
                    .value(headWorkingStack.getValue())
                    .index(headWorkingStack.getIndex() + 1)
                    .type(Type.NONTERMINAL)
                    .build());

            grammar.getProductionForKeyAndIndex(headWorkingStack.getValue(), headWorkingStack.getIndex() + 1)
                    .ifPresent(production -> Lists.reverse(production.getProductionNodes())
                            .forEach(inputStack::push));
            configuration.setState(State.NORMAL);
            log.info("Successfully applied another try case 1 with new configuration {}", configuration);
        } else if (!grammar.hasMoreProductions(headWorkingStack.getValue(), headWorkingStack.getIndex())) {
            if (configuration.getIndex().get() == 0 &&
                    grammar.isStartingSymbol(headWorkingStack.getValue())) {
                configuration.setState(State.ERROR);
                log.info("Successfully applied another try case 3 with new configuration {}", configuration);
                return;
            }
            inputStack.push(headWorkingStack.getValue());
            configuration.setState(State.BACK);
            log.info("Successfully applied another try case 2 with new configuration {}", configuration);
        } else {
            log.error("Failed to apply another try function for configuration {}", configuration);
        }
    }

    public void success(Configuration configuration) {
        log.info("Trying to apply success function for configuration {}", configuration);
        configuration.setState(State.FINAL);
        log.info("Successfully applied success function for configuration {}", configuration);
    }

    public boolean parse(List<String> word) {
        Configuration configuration = Configuration.builder().build();
        configuration.getInputStack().push(grammar.getStartingSymbol());

        while (!State.FINAL.equals(configuration.getState()) && !State.ERROR.equals(configuration.getState())) {
            if (State.NORMAL.equals(configuration.getState())) {
                if (word.size() == configuration.getIndex().get() && configuration.getInputStack().isEmpty()) {
                    success(configuration);
                } else {
                    if (grammar.isNonTerminal(configuration.getInputStack().peek())) {
                        expand(configuration);
                    } else if (grammar.isTerminal(configuration.getInputStack().peek()) &&
                            configuration.getIndex().get() < word.size() &&
                            configuration.getInputStack().peek().equals(word.get(configuration.getIndex().get()))) {
                        advance(configuration);
                    } else {
                        momentaryInsuccess(configuration);
                    }
                }
            } else if (State.BACK.equals(configuration.getState())) {
                if (Type.TERMINAL.equals(configuration.getWorkingStack().peek().getType())) {
                    back(configuration);
                } else {
                    anotherTry(configuration);
                }
            }
        }

        if (State.ERROR.equals(configuration.getState())) {
            log.error("Word {} is not accepted", word);
            return false;
        } else {
            log.info("Word {} is accepted", word);
            buildStringOfProd(configuration.getWorkingStack());
            return true;
        }
    }

    private void buildStringOfProd(Stack<TransitionElement> workingStack) {
        StringBuilder stringOfProductions = new StringBuilder();
        while(!workingStack.empty()) {
            TransitionElement headOfStack = workingStack.pop();
            if(Type.NONTERMINAL.equals(headOfStack.getType())) {
                stringOfProductions.append(headOfStack.getValue()).append(headOfStack.getIndex());
            }
        }

        log.info("This is the string of productions {}", stringOfProductions);
    }
}
