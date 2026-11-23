package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface DoubleCharPair extends Pair<Double, Character>, DoubleCharMap.Entry {
    double leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Double left() {
        return this.leftRaw();
    }

    default double getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Double getKey() {
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

    default double left(double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(double)
     */
    @Deprecated
    default <T extends Double> Double left(T value) {
        return this.left(value != null ? value.doubleValue() : (double) 0);
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
