package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.Pair;

public interface LongObjPair<R> extends Pair<Long, R>, LongObjMap.Entry<R> {
    long leftRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
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
     */
    @Deprecated
    default <T extends Long> Long left(T value) {
        return this.left(value);
    }
}
