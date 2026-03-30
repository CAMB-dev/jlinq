package moe.camb.jlinq.exception;

public class EmptySequenceException extends LinqException {
    public EmptySequenceException() {
        super("Sequence contains no elements");
    }

    public EmptySequenceException(String message) {
        super(message);
    }
}
