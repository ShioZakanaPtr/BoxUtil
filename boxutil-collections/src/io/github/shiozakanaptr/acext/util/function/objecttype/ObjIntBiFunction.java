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
public interface ObjIntBiFunction<T, R> extends BiFunction<T, Integer, R> {
    R apply(T t, int u);

    default R apply(T t, Integer u) {
        return this.apply(t, u != null ? u : (int) 0);
    }
}
