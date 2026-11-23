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
public interface ShortObjBiFunction<U, R> extends BiFunction<Short, U, R> {
    R apply(short t, U u);

    default R apply(Short t, U u) {
        return this.apply(t != null ? t : (short) 0, u);
    }
}
