package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.Function;

@FunctionalInterface
public interface DoubleToLongFunction extends Function<Double, Long> {
    long apply(double t);

    default Long apply(Double t) {
        return this.apply(t != null ? t : (double) 0);
    }
}
