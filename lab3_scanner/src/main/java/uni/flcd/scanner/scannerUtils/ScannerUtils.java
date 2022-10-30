package uni.flcd.scanner.scannerUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uni.flcd.constants.RegexConstants;

import java.util.ArrayList;

import static uni.flcd.constants.RegexConstants.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScannerUtils {
    public static final String EQUALS = "=";
    public static boolean isIdentifier(String token) {
        return token.matches(IDENTIFIER_REGEX);
    }

    public static boolean isConstant(String token) {
        return token.matches(INT_REGEX) || token.matches(STRING_REGEX);
    }

    public static boolean isUnifiedToken(ArrayList<String> tokens, int i, String currentToken) {
        return currentToken.matches(RegexConstants.SPLIT_REGEX) &&
                null != tokens.get(i + 1) &&
                EQUALS.equals(tokens.get(i + 1));
    }

}
