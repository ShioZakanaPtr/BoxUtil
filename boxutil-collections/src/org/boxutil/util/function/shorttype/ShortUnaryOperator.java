package org.boxutil.util.function.shorttype;

import java.util.function.UnaryOperator;

public interface ShortUnaryOperator extends UnaryOperator<Short> {
    short apply(short t);

    default Short apply(Short t) {
        return this.apply(t == null ? (short) 0 : t);
    }
}
