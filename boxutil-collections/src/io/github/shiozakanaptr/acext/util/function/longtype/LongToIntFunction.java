package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface LongToIntFunction extends Function<Long, Integer> {
    int apply(long t);

    default Integer apply(Long t) {
        return this.apply(t != null ? t : (long) 0);
    }
}
