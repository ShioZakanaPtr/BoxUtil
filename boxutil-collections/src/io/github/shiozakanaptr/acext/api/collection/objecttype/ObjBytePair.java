package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjBytePair<L> extends Pair<L, Byte>, ObjByteMap.Entry<L> {
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
