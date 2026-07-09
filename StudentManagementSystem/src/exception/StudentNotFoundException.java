package exception;

/**
 * Custom exception thrown when a requested student is not found in the system.
 */
public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
