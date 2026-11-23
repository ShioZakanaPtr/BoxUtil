package io.github.shiozakanaptr.acext.exception;

/**
 * Thrown to indicate that a collections unable to resizing to
 * larger capacity in auto expand stage.
 *
 * <p>For this reason, should transfer the data or split to
 * different collections.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public class CapacityOverflowException extends RuntimeException {
    public CapacityOverflowException() {
        super();
    }

    public CapacityOverflowException(String message) {
        super(message);
    }
}
