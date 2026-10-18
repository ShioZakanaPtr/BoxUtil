package org.boxutil.util.function.bytetype;

import java.util.function.BinaryOperator;

public interface ByteByteBiOperator extends BinaryOperator<Byte> {
    byte apply(byte t, byte u);

    default Byte apply(Byte t, Byte u) {
        return this.apply(t == null ? (byte) 0 : t, u == null ? (byte) 0 : u);
    }
}
