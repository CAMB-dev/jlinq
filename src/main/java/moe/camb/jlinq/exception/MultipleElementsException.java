package moe.camb.jlinq.exception;

public class MultipleElementsException extends LinqException {
    public MultipleElementsException() {
        super("Sequence contains more than one element");
    }

    public MultipleElementsException(String message) {
        super(message);
    }
}
