package exceptions;

public class OverlapException extends Exception{
    public OverlapException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverlapException(String massage) {
        super(massage);
    }
}
