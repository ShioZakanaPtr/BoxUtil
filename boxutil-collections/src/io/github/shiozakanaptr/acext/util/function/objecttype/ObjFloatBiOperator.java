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
public interface ObjFloatBiOperator<T> extends BiFunction<T, Float, Float> {
    float apply(T t, float u);

    default Float apply(T t, Float u) {
        return this.apply(t, u != null ? u : (float) 0);
    }
}
