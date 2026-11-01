package org.boxutil.util.collection.objecttype;

import org.boxutil.base.api.container.Pair;
import org.boxutil.base.api.container.objecttype.ObjObjPair;

import java.io.Serializable;
import java.util.Objects;

public class ObjObjImmutablePair<L, R> implements ObjObjPair<L, R>, Serializable {
    private static final long serialVersionUID = 1L;

    protected final L le;
    protected final R ri;

    public ObjObjImmutablePair() {
        this.le = null;
        this.ri = null;
    }

    public ObjObjImmutablePair(L left, R right) {
        this.le = left;
        this.ri = right;
    }

    public L left() {
        return this.le;
    }

    public R right() {
        return this.ri;
    }

    public boolean isMutable() {
        return false;
    }

    public int hashCode() {
        return 31 * (31 + Objects.hashCode(this.le)) + Objects.hashCode(this.ri);
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
