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
public interface ObjObjCharTeFunction<T, U, R> extends TeFunction<T, U, Character, R> {
    R apply(T t, U u, char v);

    default R apply(T t, U u, Character v) {
        return this.apply(t, u, v != null ? v : (char) 0);
    }
}
