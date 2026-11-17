package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface LongObjPair<R> extends Pair<Long, R>, LongObjMap.Entry<R> {
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

}
