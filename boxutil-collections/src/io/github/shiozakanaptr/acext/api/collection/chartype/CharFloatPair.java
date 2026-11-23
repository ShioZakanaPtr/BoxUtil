package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharFloatPair extends Pair<Character, Float>, CharFloatMap.Entry {
    char leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Character left() {
        return this.leftRaw();
    }

    default char getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Character getKey() {
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

    default char left(char value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(char)
     */
    @Deprecated
    default <T extends Character> Character left(T value) {
        return this.left(value != null ? value.charValue() : (char) 0);
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
