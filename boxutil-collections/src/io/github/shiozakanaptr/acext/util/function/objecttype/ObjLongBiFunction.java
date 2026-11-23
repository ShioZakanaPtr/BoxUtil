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
public interface ObjLongBiFunction<T, R> extends BiFunction<T, Long, R> {
    R apply(T t, long u);

    default R apply(T t, Long u) {
        return this.apply(t, u != null ? u : (long) 0);
    }
}
