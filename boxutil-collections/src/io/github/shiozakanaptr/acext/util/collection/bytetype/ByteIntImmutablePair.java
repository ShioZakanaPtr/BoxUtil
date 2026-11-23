package io.github.shiozakanaptr.acext.util.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteIntPair;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple immutable {@link Pair} container.
 *
 * <p>This class holds a left and a right element that are fixed at construction
 * time; attempts to modify either element through the mutating methods
 * inherited from {@link Pair} throw {@link UnsupportedOperationException}. It
 * is typically used when two values cannot conveniently be passed around
 * together, such as when a method must return two results or when a collection
 * stores compound entries.
 *
 * <p>In cases where both values are always available and used together,
 * declaring two separate fields is generally preferable to a pair; a pair is
 * intended for situations where bundling the two values is genuinely useful.
 *
 * <p>This class is {@link Cloneable} and {@link Serializable}.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@SuppressWarnings("unused")
public class ByteIntImmutablePair implements ByteIntPair, Cloneable, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    protected byte le;
    protected int ri;

    public ByteIntImmutablePair() {
        this.le = (byte) 0;
        this.ri = (int) 0;
    }

    public ByteIntImmutablePair(byte left, int right) {
        this.le = left;
        this.ri = right;
    }

    public byte leftRaw() {
        return this.le;
    }

    public int rightRaw() {
        return this.ri;
    }

    public boolean isMutable() {
        return false;
    }

    public Object clone() {
        ByteIntImmutablePair result;
        try {
            result = (ByteIntImmutablePair) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        return result;
    }

    public int hashCode() {
        return 31 * (31 + Byte.hashCode(this.le)) + Integer.hashCode(this.ri);
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
