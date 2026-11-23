package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface LongBytePair extends Pair<Long, Byte>, LongByteMap.Entry {
    long leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Long left() {
        return this.leftRaw();
    }

    default long getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Long getKey() {
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

    default long left(long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(long)
     */
    @Deprecated
    default <T extends Long> Long left(T value) {
        return this.left(value != null ? value.longValue() : (long) 0);
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
