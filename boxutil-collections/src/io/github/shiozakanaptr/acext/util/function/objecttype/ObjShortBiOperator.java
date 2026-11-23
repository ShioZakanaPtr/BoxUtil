package io.github.shiozakanaptr.acext.util.function.objecttype;

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
public interface ObjShortBiOperator<T> extends BiFunction<T, Short, Short> {
    short apply(T t, short u);

    default Short apply(T t, Short u) {
        return this.apply(t, u != null ? u : (short) 0);
    }
}
