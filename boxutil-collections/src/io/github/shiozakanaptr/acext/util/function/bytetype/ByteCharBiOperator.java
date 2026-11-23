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
public interface ByteCharBiOperator extends BiFunction<Byte, Character, Character> {
    char apply(byte t, char u);

    default Character apply(Byte t, Character u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (char) 0);
    }
}
