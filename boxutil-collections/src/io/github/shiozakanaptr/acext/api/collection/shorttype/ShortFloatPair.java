package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ShortFloatPair extends Pair<Short, Float>, ShortFloatMap.Entry {
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

    float rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Float right() {
        return this.rightRaw();
    }

    default float getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Float getValue() {
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

    default float right(float value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(float)
     */
    @Deprecated
    default <T extends Float> Float right(T value) {
        return this.right(value != null ? value.floatValue() : (float) 0);
    }

    default float setValue(float newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(float)
     */
    @Deprecated
    default Float setValue(Float value) {
        return this.right(value != null ? value.floatValue() : (float) 0);
    }
}
