package io.github.shiozakanaptr.acext.util.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.chartype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.CharFunction;

import java.util.*;

public abstract class AbstractCharSet extends AbstractCharCollection implements CharSet {
    protected AbstractCharSet() {}

    public boolean remapping(char element, CharFunction<? extends Character> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
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
                return this.removeIf((Character e) -> c.contains(e));
            }
        }
    }

    public boolean containsAll(Collection<?> c) {
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();
        if (c instanceof Set<?> && this.size() < c.size()) return false;

        for (final Object e : c) {
            if (!this.contains(e)) return false;
        }
        return true;
    }

    public abstract CharIterator iterator();

    public abstract CharSpliterator spliterator();

    public int hashCode() {
        int result = 0;
        CharIterator it = this.iterator();
        while (it.hasNext()) {
            result += Character.hashCode(it.nextChar());
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
