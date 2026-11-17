package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface IntLongPair extends Pair<Integer, Long>, IntLongMap.Entry {
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
