package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface DoubleObjPair<R> extends Pair<Double, R>, DoubleObjMap.Entry<R> {
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

}
