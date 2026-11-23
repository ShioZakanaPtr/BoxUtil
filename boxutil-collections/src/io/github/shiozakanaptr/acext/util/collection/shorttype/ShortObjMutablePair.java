package io.github.shiozakanaptr.acext.util.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortObjPair;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple mutable {@link Pair} container.
 *
 * <p>This class holds a left and a right element, both of which may be
 * modified after construction via the mutating methods inherited from
 * {@link Pair}. It is typically used when two values cannot conveniently be
 * passed around together, such as when a method must return two results or
 * when a collection stores compound entries.
 *
 * <p>In cases where both values are always available and used together,
 * declaring two separate fields is generally preferable to a pair; a pair is
 * intended for situations where bundling the two values is genuinely useful.
 *
 * <p>This class is {@link Cloneable} and {@link Serializable}.
 *
 * @see Pair
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public class ShortObjMutablePair<R> implements ShortObjPair<R>, Cloneable, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    protected short le;
    protected R ri;

    public ShortObjMutablePair() {
        this.le = (short) 0;
        this.ri = null;
    }

    public ShortObjMutablePair(short left, R right) {
        this.le = left;
        this.ri = right;
    }

    public short leftRaw() {
        return this.le;
    }

    public R right() {
        return this.ri;
    }

    public short left(short value) {
        final short result = this.le;
        this.le = value;
        return result;
    }

    public <T extends R> R right(T value) {
        final R result = this.ri;
        this.ri = value;
        return result;
    }

    public boolean isMutable() {
        return true;
    }

    public Object clone() {
        ShortObjMutablePair<R> result;
        try {
            result = (ShortObjMutablePair<R>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        return result;
    }

    public int hashCode() {
        return 31 * (31 + Short.hashCode(this.le)) + Objects.hashCode(this.ri);
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
