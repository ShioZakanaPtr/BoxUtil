package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ByteBytePair extends Pair<Byte, Byte>, ByteByteMap.Entry {
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

    byte rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Byte right() {
        return this.rightRaw();
    }

    default byte getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Byte getValue() {
        return this.rightRaw();
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

    default byte right(byte value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(byte)
     */
    @Deprecated
    default <T extends Byte> Byte right(T value) {
        return this.right(value != null ? value.byteValue() : (byte) 0);
    }

    default byte setValue(byte newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(byte)
     */
    @Deprecated
    default Byte setValue(Byte value) {
        return this.right(value != null ? value.byteValue() : (byte) 0);
    }
}
