package uni.flcd.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Delimiters {
    private static final String ARITHMETIC_DELIMITERS = "+-/*<>%";
    private static final String PARENTHESES_DELIMITERS = "()[]{}";
    private static final String LOGICAL_DELIMITERS = "!=";
    private static final String BASIC_DELIMITERS = ";,:";
    public static final String SPACE = " ";
    public static final String DELIMITERS = ARITHMETIC_DELIMITERS +
            PARENTHESES_DELIMITERS +
            LOGICAL_DELIMITERS +
            BASIC_DELIMITERS +
            SPACE;
}
