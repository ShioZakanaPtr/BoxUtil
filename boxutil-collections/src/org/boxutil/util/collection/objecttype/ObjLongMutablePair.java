package org.boxutil.util.collection.objecttype;

import org.boxutil.base.api.container.Pair;
import org.boxutil.base.api.container.objecttype.ObjLongPair;

import java.io.Serializable;
import java.util.Objects;

public class ObjLongMutablePair<L> implements ObjLongPair<L>, Serializable {
    private static final long serialVersionUID = 1L;

    protected L le;
    protected long ri;

    public ObjLongMutablePair() {
        this.le = null;
        this.ri = 0;
    }

    public ObjLongMutablePair(L left, long right) {
        this.le = left;
        this.ri = right;
    }

    public L left() {
        return this.le;
    }

    public long rightRaw() {
        return this.ri;
    }

    public <T extends L> L left(T value) {
        final L result = this.le;
        this.le = value;
        return result;
    }

    public long right(long value) {
        final long result = this.ri;
        this.ri = value;
        return result;
    }

    public boolean isMutable() {
        return true;
    }

    public int hashCode() {
        return 31 * (31 + Objects.hashCode(this.le)) + Long.hashCode(this.ri);
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
