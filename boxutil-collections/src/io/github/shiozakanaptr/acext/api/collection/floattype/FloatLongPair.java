package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatLongPair extends Pair<Float, Long>, FloatLongMap.Entry {
    float leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Float left() {
        return this.leftRaw();
    }

    default float getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Float getKey() {
        return this.leftRaw();
    }

    long rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Long right() {
        return this.rightRaw();
    }

    default long getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Long getValue() {
        return this.rightRaw();
    }

    default float left(float value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(float)
     */
    @Deprecated
    default <T extends Float> Float left(T value) {
        return this.left(value != null ? value.floatValue() : (float) 0);
    }

    default long right(long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(long)
     */
    @Deprecated
    default <T extends Long> Long right(T value) {
        return this.right(value != null ? value.longValue() : (long) 0);
    }

    default long setValue(long newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(long)
     */
    @Deprecated
    default Long setValue(Long value) {
        return this.right(value != null ? value.longValue() : (long) 0);
    }
}
