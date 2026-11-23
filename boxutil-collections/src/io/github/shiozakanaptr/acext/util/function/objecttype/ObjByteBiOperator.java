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
public interface ObjByteBiOperator<T> extends BiFunction<T, Byte, Byte> {
    byte apply(T t, byte u);

    default Byte apply(T t, Byte u) {
        return this.apply(t, u != null ? u : (byte) 0);
    }
}
