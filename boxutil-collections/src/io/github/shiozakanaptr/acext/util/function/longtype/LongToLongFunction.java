package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface LongToLongFunction extends Function<Long, Long> {
    long apply(long t);

    default Long apply(Long t) {
        return this.apply(t != null ? t : (long) 0);
    }
}
