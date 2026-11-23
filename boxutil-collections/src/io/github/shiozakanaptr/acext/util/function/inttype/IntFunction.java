package io.github.shiozakanaptr.acext.util.function.inttype;

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
public interface IntFunction<R> extends Function<Integer, R> {
    R apply(int t);

    default R apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
