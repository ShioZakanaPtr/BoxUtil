package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.ToLongFunction;

/**
 * Type specialization functional interface.
 *
 * @see ToLongFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ObjToLongFunction<T> extends ToLongFunction<T> {
    long apply(T t);

    default long applyAsLong(T t) {
        return this.apply(t);
    }
}
