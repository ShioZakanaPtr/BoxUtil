package org.boxutil.util.collection.bytetype;

import org.boxutil.base.api.container.bytetype.ByteIterator;
import org.boxutil.base.api.container.bytetype.ByteSet;
import org.boxutil.util.function.bytetype.ByteFunction;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractByteSet extends AbstractByteCollection implements ByteSet {
    protected AbstractByteSet() {}

    public boolean remapping(byte element, ByteFunction<? extends Byte> remappingFunction) {
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
                return this.removeIf((Byte e) -> c.contains(e));
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

    public int hashCode() {
        int result = 0;
        ByteIterator it = this.iterator();
        while (it.hasNext()) {
            result += Byte.hashCode(it.nextByte());
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Set<?> cast) {
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
