package exceptions;

public class ManagerSaveException extends Exception {
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerSaveException(String massage) {
        super(massage);
    }
}
