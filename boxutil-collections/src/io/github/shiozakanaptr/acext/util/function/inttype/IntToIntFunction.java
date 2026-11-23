package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface IntToIntFunction extends Function<Integer, Integer> {
    int apply(int t);

    default Integer apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
