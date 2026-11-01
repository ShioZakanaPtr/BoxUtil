package org.boxutil.util.collection.inttype;

import org.boxutil.base.api.container.inttype.IntIterator;
import org.boxutil.base.api.container.inttype.IntSet;
import org.boxutil.base.api.container.inttype.IntSpliterator;

import java.util.*;
import java.util.function.IntFunction;

public abstract class AbstractIntSet extends AbstractIntCollection implements IntSet {
    protected AbstractIntSet() {}

    public boolean remapping(int element, IntFunction<? extends Integer> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
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
                return this.removeIf((Integer e) -> c.contains(e));
            }
        }
    }

    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();
        if (c instanceof Set<?> && this.size() < c.size()) return false;

        for (final Object e : c) {
            if (!this.contains(e)) return false;
        }
        return true;
    }

    public abstract IntIterator iterator();

    public abstract IntSpliterator spliterator();

    public int hashCode() {
        int result = 0;
        IntIterator it = this.iterator();
        while (it.hasNext()) {
            result += Integer.hashCode(it.nextInt());
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
