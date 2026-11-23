package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface IntShortPair extends Pair<Integer, Short>, IntShortMap.Entry {
    int leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
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
     * @see #getKeyRaw()
     */
    @Deprecated
    default Integer getKey() {
        return this.leftRaw();
    }

    short rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Short right() {
        return this.rightRaw();
    }

    default short getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Short getValue() {
        return this.rightRaw();
    }

    default int left(int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(int)
     */
    @Deprecated
    default <T extends Integer> Integer left(T value) {
        return this.left(value != null ? value.intValue() : (int) 0);
    }

    default short right(short value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(short)
     */
    @Deprecated
    default <T extends Short> Short right(T value) {
        return this.right(value != null ? value.shortValue() : (short) 0);
    }

    default short setValue(short newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(short)
     */
    @Deprecated
    default Short setValue(Short value) {
        return this.right(value != null ? value.shortValue() : (short) 0);
    }
}
