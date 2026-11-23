package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface LongIntPair extends Pair<Long, Integer>, LongIntMap.Entry {
    long leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Long left() {
        return this.leftRaw();
    }

    default long getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Long getKey() {
        return this.leftRaw();
    }

    int rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Integer right() {
        return this.rightRaw();
    }

    default int getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Integer getValue() {
        return this.rightRaw();
    }

    default long left(long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(long)
     */
    @Deprecated
    default <T extends Long> Long left(T value) {
        return this.left(value != null ? value.longValue() : (long) 0);
    }

    default int right(int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(int)
     */
    @Deprecated
    default <T extends Integer> Integer right(T value) {
        return this.right(value != null ? value.intValue() : (int) 0);
    }

    default int setValue(int newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(int)
     */
    @Deprecated
    default Integer setValue(Integer value) {
        return this.right(value != null ? value.intValue() : (int) 0);
    }
}
