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
public interface ObjShortPair<L> extends Pair<L, Short>, ObjShortMap.Entry<L> {
    short rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Short right() {
        return this.rightRaw();
    }

    default short getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Short getValue() {
        return this.rightRaw();
    }

    default short right(short value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(short)
     */
    @Deprecated
    default <T extends Short> Short right(T value) {
        return this.right(value != null ? value.shortValue() : (short) 0);
    }

    default short setValue(short newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(short)
     */
    @Deprecated
    default Short setValue(Short value) {
        return this.right(value != null ? value.shortValue() : (short) 0);
    }
}
