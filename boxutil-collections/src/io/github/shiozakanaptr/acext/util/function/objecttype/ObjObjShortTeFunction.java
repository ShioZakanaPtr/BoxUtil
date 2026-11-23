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
public interface ObjObjShortTeFunction<T, U, R> extends TeFunction<T, U, Short, R> {
    R apply(T t, U u, short v);

    default R apply(T t, U u, Short v) {
        return this.apply(t, u, v != null ? v : (short) 0);
    }
}
