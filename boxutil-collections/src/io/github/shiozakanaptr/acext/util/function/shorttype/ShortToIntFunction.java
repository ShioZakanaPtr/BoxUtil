package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ShortToIntFunction extends Function<Short, Integer> {
    int apply(short t);

    default Integer apply(Short t) {
        return this.apply(t != null ? t : (short) 0);
    }
}
