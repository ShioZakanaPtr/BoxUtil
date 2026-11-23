package io.github.shiozakanaptr.acext.util.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleFunction;

import java.util.*;

public abstract class AbstractDoubleSet extends AbstractDoubleCollection implements DoubleSet {
    protected AbstractDoubleSet() {}

    public boolean remapping(double element, DoubleFunction<? extends Double> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        if (this.isEmpty()) return false;

        if (c instanceof List<?>) {
            boolean result = false;
            for (final Object e : c) {
                result |= this.remove(e);
            }
            return result;
        } else {
            if (this.size() > c.size()) {
                boolean result = false;
                for (final Object e : c) {
                    result |= this.remove(e);
                }
                return result;
            } else {
                return this.removeIf((Double e) -> c.contains(e));
            }
        }
    }

    public boolean containsAll(Collection<?> c) {
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();
        if (c instanceof Set<?> && this.size() < c.size()) return false;

        for (final Object e : c) {
            if (!this.contains(e)) return false;
        }
        return true;
    }

    public abstract DoubleIterator iterator();

    public abstract DoubleSpliterator spliterator();

    public int hashCode() {
        int result = 0;
        DoubleIterator it = this.iterator();
        while (it.hasNext()) {
            result += Double.hashCode(it.nextDouble());
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Set<?>) {
            Set<?> cast = (Set<?>) o;
            if (cast.size() != this.size()) return false;
            try {
                return this.containsAll(cast);
            } catch (ClassCastException | NullPointerException unused) {
                return false;
            }
        }
        return false;
    }
}
