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
        boolean result = false;

        if (this.size() > c.size()) {
            for (Object e : c)
                result |= this.remove(e);
        } else {
            for (IntIterator i = this.iterator(); i.hasNext(); ) {
                if (c.contains(i.nextInt())) {
                    i.remove();
                    result = true;
                }
            }
        }
        return result;
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
