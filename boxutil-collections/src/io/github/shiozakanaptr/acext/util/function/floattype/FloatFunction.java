package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.Function;

/**
 * Type specialization functional interface.
 *
 * @see Function
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface FloatFunction<R> extends Function<Float, R> {
    R apply(float t);

    default R apply(Float t) {
        return this.apply(t != null ? t : (float) 0);
    }
}
