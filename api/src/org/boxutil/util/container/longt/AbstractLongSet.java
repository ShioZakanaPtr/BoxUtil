package org.boxutil.util.container.longt;

import org.boxutil.base.api.container.longt.LongIterator;
import org.boxutil.base.api.container.longt.LongSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
        boolean result = false;

        if (this.size() > c.size()) {
            for (Object e : c)
                result |= this.remove(e);
        } else {
            for (LongIterator i = this.iterator(); i.hasNext(); ) {
                if (c.contains(i.nextLong())) {
                    i.remove();
                    result = true;
                }
            }
        }
        return result;
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
