package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortIntBiOperator extends BiFunction<Short, Integer, Integer> {
    int apply(short t, int u);

    default Integer apply(Short t, Integer u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (int) 0);
    }
}
