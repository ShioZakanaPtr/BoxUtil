package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.ToIntFunction;

/**
 * Type specialization functional interface.
 *
 * @see ToIntFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ObjToIntFunction<T> extends ToIntFunction<T> {
    int apply(T t);

    default int applyAsInt(T t) {
        return this.apply(t);
    }
}
