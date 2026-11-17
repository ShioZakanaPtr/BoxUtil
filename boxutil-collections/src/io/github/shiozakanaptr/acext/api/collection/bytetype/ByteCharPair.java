package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ByteCharPair extends Pair<Byte, Character>, ByteCharMap.Entry {
    byte leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Byte left() {
        return this.leftRaw();
    }

    default byte getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Byte getKey() {
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

    default byte left(byte value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(byte)
     */
    @Deprecated
    default <T extends Byte> Byte left(T value) {
        return this.left(value != null ? value.byteValue() : (byte) 0);
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
