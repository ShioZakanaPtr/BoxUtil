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
public interface ObjObjIntTeOperator<T, U> extends TeFunction<T, U, Integer, Integer> {
    int apply(T t, U u, int v);

    default Integer apply(T t, U u, Integer v) {
        return this.apply(t, u, v != null ? v : (int) 0);
    }
}
