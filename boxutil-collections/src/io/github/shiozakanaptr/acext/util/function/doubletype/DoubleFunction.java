package io.github.shiozakanaptr.acext.util.function.doubletype;

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
public interface DoubleFunction<R> extends Function<Double, R> {
    R apply(double t);

    default R apply(Double t) {
        return this.apply(t != null ? t : (double) 0);
    }
}
