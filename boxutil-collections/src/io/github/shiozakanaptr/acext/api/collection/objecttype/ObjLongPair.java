package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;

/**
 * Type specialization interface.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjLongPair<L> extends Pair<L, Long>, ObjLongMap.Entry<L> {

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
