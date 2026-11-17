package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ByteDoublePair extends Pair<Byte, Double>, ByteDoubleMap.Entry {
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

    double rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Double right() {
        return this.rightRaw();
    }

    default double getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Double getValue() {
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

    default double right(double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(double)
     */
    @Deprecated
    default <T extends Double> Double right(T value) {
        return this.right(value != null ? value.doubleValue() : (double) 0);
    }

    default double setValue(double newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(double)
     */
    @Deprecated
    default Double setValue(Double value) {
        return this.right(value != null ? value.doubleValue() : (double) 0);
    }
}
