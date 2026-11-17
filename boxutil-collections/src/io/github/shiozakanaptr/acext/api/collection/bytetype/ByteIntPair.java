package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ByteIntPair extends Pair<Byte, Integer>, ByteIntMap.Entry {
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

    int rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Integer right() {
        return this.rightRaw();
    }

    default int getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Integer getValue() {
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

    default int right(int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(int)
     */
    @Deprecated
    default <T extends Integer> Integer right(T value) {
        return this.right(value != null ? value.intValue() : (int) 0);
    }

    default int setValue(int newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(int)
     */
    @Deprecated
    default Integer setValue(Integer value) {
        return this.right(value != null ? value.intValue() : (int) 0);
    }
}
