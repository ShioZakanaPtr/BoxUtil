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
public interface ObjObjLongTeOperator<T, U> extends TeFunction<T, U, Long, Long> {
    long apply(T t, U u, long v);

    default Long apply(T t, U u, Long v) {
        return this.apply(t, u, v != null ? v : (long) 0);
    }
}
