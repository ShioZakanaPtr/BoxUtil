package org.boxutil.util.function.shortt;

import java.util.function.BinaryOperator;

public interface ShortShortBiOperator extends BinaryOperator<Short> {
    short apply(short t, short u);

    default Short apply(Short t, Short u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (short) 0 : u);
    }
}
