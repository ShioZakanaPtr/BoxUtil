package org.boxutil.util.container.shortt;

import org.boxutil.base.api.container.shortt.ShortIterator;
import org.boxutil.base.api.container.shortt.ShortSet;
import org.boxutil.util.function.shortt.ShortFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractShortSet extends AbstractShortCollection implements ShortSet {
    protected AbstractShortSet() {}

    public boolean remapping(short element, @NotNull ShortFunction<? extends Short> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean result = false;

        if (this.size() > c.size()) {
            for (Object e : c)
                result |= this.remove(e);
        } else {
            for (ShortIterator i = this.iterator(); i.hasNext(); ) {
                if (c.contains(i.nextShort())) {
                    i.remove();
                    result = true;
                }
            }
        }
        return result;
    }

    public int hashCode() {
        int result = 0;
        ShortIterator it = this.iterator();
        while (it.hasNext()) {
            result += Short.hashCode(it.nextShort());
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
