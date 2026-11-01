package org.boxutil.util.collection.longtype;

import org.boxutil.base.api.container.Pair;
import org.boxutil.base.api.container.longtype.LongObjPair;

import java.io.Serializable;
import java.util.Objects;

public class LongObjImmutablePair<R> implements LongObjPair<R>, Serializable {
    private static final long serialVersionUID = 1L;

    protected final long le;
    protected final R ri;

    public LongObjImmutablePair() {
        this.le = 0;
        this.ri = null;
    }

    public LongObjImmutablePair(long left, R right) {
        this.le = left;
        this.ri = right;
    }

    public long leftRaw() {
        return this.le;
    }

    public R right() {
        return this.ri;
    }

    public boolean isMutable() {
        return false;
    }

    public int hashCode() {
        return 31 * (31 + Long.hashCode(this.le)) + Objects.hashCode(this.ri);
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Pair<?, ?>) {
            final Pair<?, ?> cast = (Pair<?, ?>) o;
            return Objects.equals(this.le, cast.left()) && Objects.equals(this.ri, cast.right());
        }
        return false;
    }

    public String toString() {
        return "[" + this.le + ',' + this.ri + ']';
    }
}
