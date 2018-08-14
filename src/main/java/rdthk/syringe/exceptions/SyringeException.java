package rdthk.syringe.exceptions;

public class SyringeException extends RuntimeException {
    public SyringeException(String message) {
        super(message);
    }

    public SyringeException(Throwable cause) {
        super(cause);
    }
}
