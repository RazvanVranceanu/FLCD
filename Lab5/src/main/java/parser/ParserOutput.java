package parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import parser.model.TransitionElement;
import parser.model.enums.Type;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ParserOutput {

    private static final String PARSER_OUTPUT_TXT = "src/main/resources/parserOutput.txt";

    public static void writeParserOutput(String stringToWrite) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(PARSER_OUTPUT_TXT));
            writer.write(stringToWrite);
            writer.close();
        } catch (IOException e) {
            log.error("An error occurred.", e);
        }
    }

    public static void buildStringOfProd(Stack<TransitionElement> workingStack) {
        StringBuilder stringOfProductions = new StringBuilder();
        workingStack.forEach(element -> {
            if (Type.NONTERMINAL.equals(element.getType())) {
                stringOfProductions.append(element.getValue()).append(element.getIndex()).append("\n");
            }
        });
        writeParserOutput(String.valueOf(stringOfProductions));
        log.info("This is the string of productions {}", stringOfProductions);
    }


}
