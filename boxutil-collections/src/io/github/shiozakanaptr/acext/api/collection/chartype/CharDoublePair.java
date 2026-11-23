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
public interface CharDoublePair extends Pair<Character, Double>, CharDoubleMap.Entry {
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

    double rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Double right() {
        return this.rightRaw();
    }

    default double getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Double getValue() {
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

    default double right(double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(double)
     */
    @Deprecated
    default <T extends Double> Double right(T value) {
        return this.right(value != null ? value.doubleValue() : (double) 0);
    }

    default double setValue(double newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(double)
     */
    @Deprecated
    default Double setValue(Double value) {
        return this.right(value != null ? value.doubleValue() : (double) 0);
    }
}
