package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface ShortUnaryOperator extends UnaryOperator<Short> {
    short apply(short t);

    default Short apply(Short t) {
        return this.apply(t != null ? t : (short) 0);
    }
}
