package parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.model.Configuration;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    Configuration configuration;
    Parser victim;

    @BeforeEach
    void setup() {
        victim = new Parser();

        Stack<String> inputStack = new Stack<>();
        inputStack.push("a");

        configuration = Configuration.builder()
                .workingStack(new Stack<>())
                .inputStack(inputStack)
                .build();
    }

    @Test
    void shouldAdvance() {
        victim.advance(configuration);

        Stack<String> resultInputStack = configuration.getInputStack();
        Stack<String> resultWorkingStack = configuration.getWorkingStack();

        assertTrue(resultInputStack.empty());
        assertFalse(resultWorkingStack.empty());

        assertEquals(resultWorkingStack.pop(), "a");
        assertTrue(resultWorkingStack.empty());
    }

}