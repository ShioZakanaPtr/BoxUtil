package io.github.shiozakanaptr.acext.util.function.longtype;

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
public interface LongFunction<R> extends Function<Long, R> {
    R apply(long t);

    default R apply(Long t) {
        return this.apply(t != null ? t : (long) 0);
    }
}
