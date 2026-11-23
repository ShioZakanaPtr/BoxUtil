package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ByteObjPair<R> extends Pair<Byte, R>, ByteObjMap.Entry<R> {
    byte leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
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
     * @see #getKeyRaw()
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
     * @see #left(byte)
     */
    @Deprecated
    default <T extends Byte> Byte left(T value) {
        return this.left(value != null ? value.byteValue() : (byte) 0);
    }
}
