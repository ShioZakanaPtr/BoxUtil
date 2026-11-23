package io.github.shiozakanaptr.acext.util.function.longtype;

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
public interface LongObjBiFunction<U, R> extends BiFunction<Long, U, R> {
    R apply(long t, U u);

    default R apply(Long t, U u) {
        return this.apply(t != null ? t : (long) 0, u);
    }
}
