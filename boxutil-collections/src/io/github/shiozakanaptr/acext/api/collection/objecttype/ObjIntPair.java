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
public interface ObjIntPair<L> extends Pair<L, Integer>, ObjIntMap.Entry<L> {
    int rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #rightRaw()
     */
    @Deprecated
    default Integer right() {
        return this.rightRaw();
    }

    default int getValueRaw() {
        return this.rightRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getValueRaw()
     */
    @Deprecated
    default Integer getValue() {
        return this.rightRaw();
    }

    default int right(int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #right(int)
     */
    @Deprecated
    default <T extends Integer> Integer right(T value) {
        return this.right(value != null ? value.intValue() : (int) 0);
    }

    default int setValue(int newValue) {
        return this.right(newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setValue(int)
     */
    @Deprecated
    default Integer setValue(Integer value) {
        return this.right(value != null ? value.intValue() : (int) 0);
    }
}
