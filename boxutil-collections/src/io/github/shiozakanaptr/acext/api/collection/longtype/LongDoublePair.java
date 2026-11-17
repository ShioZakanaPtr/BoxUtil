package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface LongDoublePair extends Pair<Long, Double>, LongDoubleMap.Entry {
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
