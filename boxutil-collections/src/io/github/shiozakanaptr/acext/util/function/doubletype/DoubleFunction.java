package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.Function;

@FunctionalInterface
public interface DoubleFunction<R> extends Function<Double, R> {
    R apply(double t);

    default R apply(Double t) {
        return this.apply(t != null ? t : (double) 0);
    }
}
