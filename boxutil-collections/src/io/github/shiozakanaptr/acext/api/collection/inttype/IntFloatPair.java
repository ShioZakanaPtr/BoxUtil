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
public interface IntFloatPair extends Pair<Integer, Float>, IntFloatMap.Entry {
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
