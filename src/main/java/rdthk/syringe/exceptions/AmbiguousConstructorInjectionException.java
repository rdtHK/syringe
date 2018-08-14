package rdthk.syringe.exceptions;

public class AmbiguousConstructorInjectionException extends SyringeException {
    public AmbiguousConstructorInjectionException(String message) {
        super(message);
    }
}
