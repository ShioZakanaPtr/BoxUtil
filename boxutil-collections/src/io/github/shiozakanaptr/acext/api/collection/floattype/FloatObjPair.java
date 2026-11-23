package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatObjPair<R> extends Pair<Float, R>, FloatObjMap.Entry<R> {
    float leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #leftRaw()
     */
    @Deprecated
    default Float left() {
        return this.leftRaw();
    }

    default float getKeyRaw() {
        return this.leftRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw()
     */
    @Deprecated
    default Float getKey() {
        return this.leftRaw();
    }


    default float left(float value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #left(float)
     */
    @Deprecated
    default <T extends Float> Float left(T value) {
        return this.left(value != null ? value.floatValue() : (float) 0);
    }

}
