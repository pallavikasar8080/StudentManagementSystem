package exception;

/**
 * Custom exception thrown when user input fails demographic or formats constraints.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
