package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface FloatToIntFunction extends Function<Float, Integer> {
    int apply(float t);

    default Integer apply(Float t) {
        return this.apply(t != null ? t : (float) 0);
    }
}
