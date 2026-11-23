package io.github.shiozakanaptr.acext.util.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharFloatPair;

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
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@SuppressWarnings("unused")
public class CharFloatMutablePair implements CharFloatPair, Cloneable, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    protected char le;
    protected float ri;

    public CharFloatMutablePair() {
        this.le = (char) 0;
        this.ri = (float) 0;
    }

    public CharFloatMutablePair(char left, float right) {
        this.le = left;
        this.ri = right;
    }

    public char leftRaw() {
        return this.le;
    }

    public float rightRaw() {
        return this.ri;
    }

    public char left(char value) {
        final char result = this.le;
        this.le = value;
        return result;
    }

    public float right(float value) {
        final float result = this.ri;
        this.ri = value;
        return result;
    }

    public boolean isMutable() {
        return true;
    }

    public Object clone() {
        CharFloatMutablePair result;
        try {
            result = (CharFloatMutablePair) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        return result;
    }

    public int hashCode() {
        return 31 * (31 + Character.hashCode(this.le)) + Float.hashCode(this.ri);
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
