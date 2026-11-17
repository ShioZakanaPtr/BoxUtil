package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface DoubleBytePair extends Pair<Double, Byte>, DoubleByteMap.Entry {
    double leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Double left() {
        return this.leftRaw();
    }

    default double getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Double getKey() {
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

    default double left(double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(double)
     */
    @Deprecated
    default <T extends Double> Double left(T value) {
        return this.left(value != null ? value.doubleValue() : (double) 0);
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
