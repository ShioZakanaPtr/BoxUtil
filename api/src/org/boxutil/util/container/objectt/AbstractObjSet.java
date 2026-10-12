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
        boolean result = false;

        if (this.size() > c.size()) {
            for (Object e : c)
                result |= this.remove(e);
        } else {
            for (Iterator<E> i = this.iterator(); i.hasNext(); ) {
                if (c.contains(i.next())) {
                    i.remove();
                    result = true;
                }
            }
        }
        return result;
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
