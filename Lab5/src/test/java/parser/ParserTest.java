package parser;

import grammar.Grammar;
import grammar.model.Production;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.model.Configuration;
import parser.model.TransitionElement;
import parser.model.enums.State;
import parser.model.enums.Type;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private static final String VALID_WORD_1 = "aac";
    private static final String INVALID_WORD_1 = "adf";

    Grammar grammar;
    Parser victim;

    private static final String GRAMMAR_INPUT_FILE = "src/main/resources/grammar.in";

    private Configuration setupConfiguration(List<TransitionElement> workingStackInput, List<String> inputStackList, int index, State state) {
        Stack<String> inputStack = new Stack<>();
        Stack<TransitionElement> workingStack = new Stack<>();

        for (int i = workingStackInput.size() - 1; i >= 0; i--) {
            workingStack.push(workingStackInput.get(i));
        }

        for (int i = inputStackList.size() - 1; i >= 0; i--) {
            inputStack.push(inputStackList.get(i));
        }

        return Configuration.builder()
                .workingStack(workingStack)
                .inputStack(inputStack)
                .index(new AtomicInteger(index))
                .state(state)
                .build();
    }

    @BeforeEach
    void setup() {
        grammar = Grammar.builder().fileName(GRAMMAR_INPUT_FILE).build();
        grammar.aggregateData();
        victim = Parser.builder().grammar(grammar).build();
    }

    @Test
    void shouldTestSomething() {
        Optional<Production> res1 = grammar.getProductionForKeyAndIndex("S", 0);
        assertTrue(res1.isPresent());
        Optional<Production> res2 = grammar.getProductionForKeyAndIndex("S", 1321);
        assertTrue(res2.isEmpty());
    }

    @Test
    void shouldApplyAdvance() {
        Configuration configuration = setupConfiguration(List.of(), List.of("a"), 0, State.NORMAL);

        victim.advance(configuration);

        var resultInputStack = configuration.getInputStack();
        var resultWorkingStack = configuration.getWorkingStack();

        assertTrue(resultInputStack.empty());
        assertFalse(resultWorkingStack.empty());

        assertEquals("a", resultWorkingStack.pop().getValue());
        assertEquals(1, configuration.getIndex().get());
        assertTrue(resultWorkingStack.empty());
    }

    @Test
    void shouldApplyExpand() {
        Configuration configuration = setupConfiguration(List.of(), List.of("S"), 0, State.NORMAL);

        victim.expand(configuration);

        var resultInputStack = configuration.getInputStack();
        var resultWorkingStack = configuration.getWorkingStack();

        assertFalse(resultInputStack.empty());
        assertFalse(resultWorkingStack.empty());
        assertEquals(4, resultInputStack.size());
        assertEquals(1, resultWorkingStack.size());

        assertEquals("S", resultWorkingStack.get(0).getValue());
        assertEquals(0, resultWorkingStack.get(0).getIndex());
        assertEquals(Type.NONTERMINAL, resultWorkingStack.get(0).getType());

        assertEquals("a", resultInputStack.pop());
        assertEquals("S", resultInputStack.pop());
        assertEquals("b", resultInputStack.pop());
        assertEquals("S", resultInputStack.pop());
    }

    @Test
    void shouldTryAgainCase1() {
        Configuration configuration = setupConfiguration(List.of(TransitionElement.builder()
                        .value("S")
                        .index(0)
                        .type(Type.NONTERMINAL)
                        .build()),
                List.of("a", "S", "b", "S"),
                0, State.BACK);

        victim.anotherTry(configuration);

        var resultInputStack = configuration.getInputStack();
        var resultWorkingStack = configuration.getWorkingStack();

        assertEquals(State.NORMAL, configuration.getState());
        assertFalse(resultWorkingStack.empty());
        assertFalse(resultInputStack.empty());

        assertEquals("S", resultWorkingStack.peek().getValue());
        assertEquals(Type.NONTERMINAL, resultWorkingStack.peek().getType());
        assertEquals(1, resultWorkingStack.peek().getIndex());

        assertEquals("a", resultInputStack.pop());
        assertEquals("S", resultInputStack.pop());
        assertTrue(resultInputStack.empty());
    }

    @Test
    void shouldTryAgainCase2() {
        Configuration configuration = setupConfiguration(List.of(TransitionElement.builder()
                        .value("S")
                        .index(2)
                        .type(Type.NONTERMINAL)
                        .build()),
                List.of("c"),
                1, State.BACK);

        victim.anotherTry(configuration);

        var resultInputStack = configuration.getInputStack();
        var resultWorkingStack = configuration.getWorkingStack();

        assertEquals(State.BACK, configuration.getState());
        assertTrue(resultWorkingStack.empty());
        assertFalse(resultInputStack.empty());

        assertEquals("S", resultInputStack.pop());
        assertTrue(resultInputStack.empty());
    }

    @Test
    void shouldTryAgainCase3() {
        Configuration configuration = setupConfiguration(List.of(TransitionElement.builder()
                        .value("S")
                        .index(2)
                        .type(Type.NONTERMINAL)
                        .build()),
                List.of(""),
                0, State.BACK);

        victim.anotherTry(configuration);

        var resultInputStack = configuration.getInputStack();
        var resultWorkingStack = configuration.getWorkingStack();

        assertTrue(resultWorkingStack.empty());
        assertEquals(State.ERROR, configuration.getState());
        assertTrue(resultInputStack.empty());

    }

    @Test
    void shouldBack() {
        Configuration configuration = setupConfiguration(List.of(TransitionElement.builder()
                        .value("a")
                        .type(Type.TERMINAL)
                        .build()),
                List.of(), 1, State.BACK);
        victim.back(configuration);

        var resultInputStack = configuration.getInputStack();
        var resultWorkingStack = configuration.getWorkingStack();

        assertFalse(resultInputStack.empty());
        assertTrue(resultWorkingStack.empty());

        assertEquals(1, resultInputStack.size());
        assertEquals("a", resultInputStack.pop());
        assertEquals(0, configuration.getIndex().get());
        assertEquals(State.BACK, configuration.getState());
    }

    @Test
    void shouldApplyMomentaryInsuccess() {
        Configuration configuration = setupConfiguration(List.of(), List.of(), 0, State.NORMAL);
        victim.momentaryInsuccess(configuration);

        assertEquals(State.BACK, configuration.getState());
    }

    @Test
    void shouldApplySuccess() {
        Configuration configuration = setupConfiguration(List.of(), List.of(), 0, State.NORMAL);
        victim.success(configuration);

        assertEquals(State.FINAL, configuration.getState());
    }

    @Test
    void shouldAcceptWord() {
        victim.parse(VALID_WORD_1);
    }

    @Test
    void shouldNotAcceptWord() {
        victim.parse(INVALID_WORD_1);
    }
}