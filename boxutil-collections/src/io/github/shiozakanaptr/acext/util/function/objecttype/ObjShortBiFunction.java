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
public interface ObjShortBiFunction<T, R> extends BiFunction<T, Short, R> {
    R apply(T t, short u);

    default R apply(T t, Short u) {
        return this.apply(t, u != null ? u : (short) 0);
    }
}
