package uni.flcd.scanner.scannerUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uni.flcd.constants.RegexConstants;

import static uni.flcd.constants.RegexConstants.*;
import static uni.flcd.constants.ScannerConstants.MINUS;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScannerUtils {
    public static final String EQUALS = "=";
    public static boolean isIdentifier(String token) {
        return token.matches(IDENTIFIER_REGEX);
    }

    public static boolean isConstant(String token) {
        return token.matches(INT_REGEX) || token.matches(STRING_REGEX);
    }

    public static boolean isNegativeIntegerConstant(String oneBeforePrevToken, String prevToken, String token) {
        return !oneBeforePrevToken.matches(IDENTIFIER_REGEX) && prevToken.equals(MINUS) && token.matches(INT_REGEX);
    }
    public static boolean isIntegerConstant(String token) {
        return token.matches(INT_REGEX);
    }
    public static boolean isStringConstant(String token) {
        return token.matches(STRING_REGEX);
    }

    public static boolean isUnifiedToken(String currentToken, String nextToken) {
        return currentToken.matches(RegexConstants.SPLIT_REGEX) &&
                EQUALS.equals(nextToken);
    }
}
