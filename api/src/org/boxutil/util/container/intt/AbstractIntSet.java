package org.boxutil.util.container.intt;

import org.boxutil.base.api.container.intt.IntIterator;
import org.boxutil.base.api.container.intt.IntSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntFunction;

public abstract class AbstractIntSet extends AbstractIntCollection implements IntSet {
    protected AbstractIntSet() {}

    public boolean remapping(int element, @NotNull IntFunction<? extends Integer> remappingFunction) {
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
        if (this.isEmpty()) return c.isEmpty();
        if (c instanceof Set<?> && this.size() < c.size()) return false;
        if (c == this) return true;

        for (final Object e : c) {
            if (!this.contains(e)) return false;
        }
        return true;
    }

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

        if (o instanceof Set<?> cast) {
            if (cast.isEmpty()) return this.isEmpty();
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
