package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ShortShortPair extends Pair<Short, Short>, ShortShortMap.Entry {
    short leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Short left() {
        return this.leftRaw();
    }

    default short getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Short getKey() {
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

    default short left(short value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(short)
     */
    @Deprecated
    default <T extends Short> Short left(T value) {
        return this.left(value != null ? value.shortValue() : (short) 0);
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
