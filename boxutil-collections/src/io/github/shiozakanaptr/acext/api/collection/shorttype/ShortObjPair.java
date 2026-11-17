package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ShortObjPair<R> extends Pair<Short, R>, ShortObjMap.Entry<R> {
    short leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Short left() {
        return this.leftRaw();
    }

    default short getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Short getKey() {
        return this.leftRaw();
    }


    default short left(short value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(short)
     */
    @Deprecated
    default <T extends Short> Short left(T value) {
        return this.left(value != null ? value.shortValue() : (short) 0);
    }

}
