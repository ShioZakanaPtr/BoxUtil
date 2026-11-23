package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface LongToDoubleFunction extends Function<Long, Double> {
    double apply(long t);

    default Double apply(Long t) {
        return this.apply(t != null ? t : (long) 0);
    }
}
