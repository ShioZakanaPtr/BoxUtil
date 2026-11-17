package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface CharCharPair extends Pair<Character, Character>, CharCharMap.Entry {
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
