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
public interface ObjObjByteTeOperator<T, U> extends TeFunction<T, U, Byte, Byte> {
    byte apply(T t, U u, byte v);

    default Byte apply(T t, U u, Byte v) {
        return this.apply(t, u, v != null ? v : (byte) 0);
    }
}
