package org.boxutil.base.api.container.inttype;

import org.boxutil.base.api.container.Pair;

public interface IntBytePair extends Pair<Integer, Byte>, IntByteMap.Entry {
    int leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer left() {
        return this.leftRaw();
    }

    default int getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer getKey() {
        return this.leftRaw();
    }

    byte rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
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
     */
    @Deprecated
    default Byte getValue() {
        return this.rightRaw();
    }

    default int left(int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Integer> Integer left(T value) {
        return this.left(value != null ? value.intValue() : (int) 0);
    }

    default byte right(byte value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
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
     */
    @Deprecated
    default Byte setValue(Byte value) {
        return this.right(value != null ? value.byteValue() : (byte) 0);
    }
}
