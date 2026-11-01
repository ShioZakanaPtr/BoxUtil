package org.boxutil.base.api.container.objecttype;

import org.boxutil.base.api.container.Pair;

public interface ObjLongPair<L> extends Pair<L, Long>, ObjLongMap.Entry<L> {
    long rightRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
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
     */
    @Deprecated
    default Long setValue(Long value) {
        return this.right(value != null ? value.longValue() : (long) 0);
    }
}
