package org.boxutil.base.api.container.bytetype;

import org.boxutil.base.api.container.Pair;

public interface ByteObjPair<R> extends Pair<Byte, R>, ByteObjMap.Entry<R> {
    byte leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte left() {
        return this.leftRaw();
    }

    default byte getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte getKey() {
        return this.leftRaw();
    }

    default byte left(byte value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Byte> Byte left(T value) {
        return this.left(value);
    }
}
