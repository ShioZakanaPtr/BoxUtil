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
public interface ObjLongBiOperator<T> extends BiFunction<T, Long, Long> {
    long apply(T t, long u);

    default Long apply(T t, Long u) {
        return this.apply(t, u != null ? u : (long) 0);
    }
}
