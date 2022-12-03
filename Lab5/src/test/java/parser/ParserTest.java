package parser;

import grammar.Grammar;
import grammar.model.Production;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.model.Configuration;

import java.util.Optional;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    Configuration configuration;
    Grammar grammar;
    Parser victim;

    private static final String GRAMMAR_INPUT_FILE = "src/main/resources/grammar.in";

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
        Stack<String> inputStack = new Stack<>();
        inputStack.push("a");
        configuration = Configuration.builder()
                .workingStack(new Stack<>())
                .inputStack(inputStack)
                .build();

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
    void shouldApplyExpand() {}

    @Test
    void shouldTryAgainCase1() {}

    @Test
    void shouldTryAgainCase2() {}

    @Test
    void shouldTryAgainCase3() {}

    @Test
    void shouldBack() {}

    @Test
    void shouldApplyMomentaryInsuccess() {}
}