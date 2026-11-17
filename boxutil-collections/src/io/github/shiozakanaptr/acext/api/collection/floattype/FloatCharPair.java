package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface FloatCharPair extends Pair<Float, Character>, FloatCharMap.Entry {
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

    char rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Character right() {
        return this.rightRaw();
    }

    default char getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Character getValue() {
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

    default char right(char value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(char)
     */
    @Deprecated
    default <T extends Character> Character right(T value) {
        return this.right(value != null ? value.charValue() : (char) 0);
    }

    default char setValue(char newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(char)
     */
    @Deprecated
    default Character setValue(Character value) {
        return this.right(value != null ? value.charValue() : (char) 0);
    }
}
