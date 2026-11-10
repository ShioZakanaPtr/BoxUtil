package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Predicate;

@FunctionalInterface
public interface IntPredicate extends Predicate<Integer> {
    boolean test(int t);

    default boolean test(Integer t) {
        return this.test(t != null ? t : (int) 0);
    }
}
