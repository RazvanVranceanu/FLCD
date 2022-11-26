package grammar.exceptions;

public class InvalidCfg extends RuntimeException{
    public InvalidCfg(String message) {
        super(message);
    }
}
