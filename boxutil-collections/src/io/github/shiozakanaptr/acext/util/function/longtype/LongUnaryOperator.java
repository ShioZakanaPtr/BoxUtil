package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface LongUnaryOperator extends UnaryOperator<Long> {
    long apply(long t);

    default Long apply(Long t) {
        return this.apply(t != null ? t : (long) 0);
    }
}
