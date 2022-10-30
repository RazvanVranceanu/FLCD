package uni.flcd.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConstants {
    public static final String SPLIT_REGEX = "[=!<>]";

    public static final String IDENTIFIER_REGEX = "^([a-zA-z]+)([0-9]*)([a-zA-z]*)";

    public static final String INT_REGEX = "^([-]?[1-9][0-9]*)|(0)$";
    public static final String STRING_REGEX = "^(\\\"[a-zA-Z0-9]*\\\")$";
}
