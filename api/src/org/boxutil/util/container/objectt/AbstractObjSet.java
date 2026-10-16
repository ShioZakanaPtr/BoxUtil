package org.boxutil.util.container.objectt;

import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.longt.LongIterator;
import org.boxutil.base.api.container.longt.LongSet;
import org.boxutil.util.container.longt.AbstractLongCollection;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.LongFunction;

public abstract class AbstractObjSet<E> extends AbstractObjCollection<E> implements SetExt<E> {
    protected AbstractObjSet() {}

    public boolean remapping(E element, @NotNull Function<? super E, ? extends E> remappingFunction) {
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
                return this.removeIf((E e) -> c.contains(e));
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
        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            final E nextObj = it.next();
            if (nextObj != null) result += Objects.hashCode(nextObj);
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
