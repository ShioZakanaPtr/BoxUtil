package org.boxutil.util.function.bytetype;

import java.util.function.UnaryOperator;

public interface ByteUnaryOperator extends UnaryOperator<Byte> {
    byte apply(byte t);

    default Byte apply(Byte t) {
        return this.apply(t == null ? (byte) 0 : t);
    }
}
