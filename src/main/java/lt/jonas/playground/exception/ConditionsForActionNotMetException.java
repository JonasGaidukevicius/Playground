package lt.jonas.playground.exception;

public class ConditionsForActionNotMetException extends RuntimeException {
    public ConditionsForActionNotMetException(String message) {
        super(message);
    }
}
