package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.ToDoubleFunction;

/**
 * Type specialization functional interface.
 *
 * @see ToDoubleFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ObjToDoubleFunction<T> extends ToDoubleFunction<T> {
    double apply(T t);

    default double applyAsDouble(T t) {
        return this.apply(t);
    }
}
