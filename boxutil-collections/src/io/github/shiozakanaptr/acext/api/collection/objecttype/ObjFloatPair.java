package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

public interface ObjFloatPair<L> extends Pair<L, Float>, ObjFloatMap.Entry<L> {

    float rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Float right() {
        return this.rightRaw();
    }

    default float getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Float getValue() {
        return this.rightRaw();
    }


    default float right(float value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(float)
     */
    @Deprecated
    default <T extends Float> Float right(T value) {
        return this.right(value != null ? value.floatValue() : (float) 0);
    }

    default float setValue(float newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(float)
     */
    @Deprecated
    default Float setValue(Float value) {
        return this.right(value != null ? value.floatValue() : (float) 0);
    }
}
