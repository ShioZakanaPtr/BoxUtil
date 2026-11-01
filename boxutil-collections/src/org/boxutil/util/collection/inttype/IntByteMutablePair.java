package org.boxutil.util.collection.inttype;

import org.boxutil.base.api.container.Pair;
import org.boxutil.base.api.container.inttype.IntBytePair;

import java.io.Serializable;
import java.util.Objects;

public class IntByteMutablePair implements IntBytePair, Serializable {
    private static final long serialVersionUID = 1L;

    protected int le;
    protected byte ri;

    public IntByteMutablePair() {
        this.le = 0;
        this.ri = 0;
    }

    public IntByteMutablePair(int left, byte right) {
        this.le = left;
        this.ri = right;
    }

    public int leftRaw() {
        return this.le;
    }

    public byte rightRaw() {
        return this.ri;
    }

    public int left(int value) {
        final int result = this.le;
        this.le = value;
        return result;
    }

    public byte right(byte value) {
        final byte result = this.ri;
        this.ri = value;
        return result;
    }

    public boolean isMutable() {
        return true;
    }

    public int hashCode() {
        return 31 * (31 + Integer.hashCode(this.le)) + Byte.hashCode(this.ri);
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
