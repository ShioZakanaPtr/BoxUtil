package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

/**
 * Type specialization functional interface.
 *
 * @see BiFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ByteObjBiFunction<U, R> extends BiFunction<Byte, U, R> {
    R apply(byte t, U u);

    default R apply(Byte t, U u) {
        return this.apply(t != null ? t : (byte) 0, u);
    }
}
