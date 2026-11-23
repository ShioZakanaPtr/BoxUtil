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
public interface ByteCharBiFunction<R> extends BiFunction<Byte, Character, R> {
    R apply(byte t, char u);

    default R apply(Byte t, Character u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (char) 0);
    }
}
