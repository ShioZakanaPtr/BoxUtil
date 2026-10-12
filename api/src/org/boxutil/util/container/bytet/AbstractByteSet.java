package org.boxutil.util.container.bytet;

import org.boxutil.base.api.container.bytet.ByteIterator;
import org.boxutil.base.api.container.bytet.ByteSet;
import org.boxutil.util.function.bytet.ByteFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractByteSet extends AbstractByteCollection implements ByteSet {
    protected AbstractByteSet() {}

    public boolean remapping(byte element, @NotNull ByteFunction<? extends Byte> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean result = false;

        if (this.size() > c.size()) {
            for (Object e : c)
                result |= this.remove(e);
        } else {
            for (ByteIterator i = this.iterator(); i.hasNext(); ) {
                if (c.contains(i.nextByte())) {
                    i.remove();
                    result = true;
                }
            }
        }
        return result;
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
