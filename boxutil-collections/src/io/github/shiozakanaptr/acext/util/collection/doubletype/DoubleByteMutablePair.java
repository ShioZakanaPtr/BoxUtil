package io.github.shiozakanaptr.acext.util.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleBytePair;

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
public class DoubleByteMutablePair implements DoubleBytePair, Cloneable, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    protected double le;
    protected byte ri;

    public DoubleByteMutablePair() {
        this.le = (double) 0;
        this.ri = (byte) 0;
    }

    public DoubleByteMutablePair(double left, byte right) {
        this.le = left;
        this.ri = right;
    }

    public double leftRaw() {
        return this.le;
    }

    public byte rightRaw() {
        return this.ri;
    }

    public double left(double value) {
        final double result = this.le;
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

    public Object clone() {
        DoubleByteMutablePair result;
        try {
            result = (DoubleByteMutablePair) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        return result;
    }

    public int hashCode() {
        return 31 * (31 + Double.hashCode(this.le)) + Byte.hashCode(this.ri);
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
