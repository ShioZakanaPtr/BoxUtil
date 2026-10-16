package org.boxutil.util.container.longt;

import org.boxutil.base.api.container.longt.LongIterator;
import org.boxutil.base.api.container.longt.LongSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.LongFunction;

public abstract class AbstractLongSet extends AbstractLongCollection implements LongSet {
    protected AbstractLongSet() {}

    public boolean remapping(long element, @NotNull LongFunction<? extends Long> remappingFunction) {
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
                return this.removeIf((Long e) -> c.contains(e));
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
        LongIterator it = this.iterator();
        while (it.hasNext()) {
            result += Long.hashCode(it.nextLong());
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
