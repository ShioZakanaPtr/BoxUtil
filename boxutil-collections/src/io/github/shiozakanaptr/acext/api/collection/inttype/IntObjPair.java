package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface IntObjPair<R> extends Pair<Integer, R>, IntObjMap.Entry<R> {
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

}
