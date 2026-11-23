package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ByteToIntFunction extends Function<Byte, Integer> {
    int apply(byte t);

    default Integer apply(Byte t) {
        return this.apply(t != null ? t : (byte) 0);
    }
}
