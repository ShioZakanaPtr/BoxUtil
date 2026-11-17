package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.Predicate;

@FunctionalInterface
public interface DoublePredicate extends Predicate<Double> {
    boolean test(double t);

    default boolean test(Double t) {
        return this.test(t != null ? t : (double) 0);
    }
}
