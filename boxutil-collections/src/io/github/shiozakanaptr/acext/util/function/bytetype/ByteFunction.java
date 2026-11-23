package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.Function;

/**
 * Type specialization functional interface.
 *
 * @see Function
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ByteFunction<R> extends Function<Byte, R> {
    R apply(byte t);

    default R apply(Byte t) {
        return this.apply(t != null ? t : (byte) 0);
    }
}
