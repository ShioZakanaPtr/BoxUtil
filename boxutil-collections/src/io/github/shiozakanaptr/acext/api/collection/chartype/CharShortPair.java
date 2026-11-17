package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface CharShortPair extends Pair<Character, Short>, CharShortMap.Entry {
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
