package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeFunction;

/**
 * Type specialization functional interface.
 *
 * @see TeFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ObjObjFloatTeFunction<T, U, R> extends TeFunction<T, U, Float, R> {
    R apply(T t, U u, float v);

    default R apply(T t, U u, Float v) {
        return this.apply(t, u, v != null ? v : (float) 0);
    }
}
