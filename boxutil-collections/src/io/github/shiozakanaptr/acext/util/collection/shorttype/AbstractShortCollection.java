package io.github.shiozakanaptr.acext.util.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortIterator;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSpliterator;

import java.util.*;

public abstract class AbstractShortCollection extends AbstractCollection<Short> implements ShortCollection {
    protected AbstractShortCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(short o) {
        ShortIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextShort()) return true;
        }
        return false;
    }

    public boolean contains(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.contains(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    public boolean add(short e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(Short e) {
        return this.add(e != null ? e.shortValue() : (short) 0);
    }

    public boolean removeElement(short o) {
        ShortIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextShort()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeElement(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.removeElement(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    public boolean remove(Object o) {
        return this.removeElement(o);
    }

    public boolean containsAll(Collection<?> c) {
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();
        return super.containsAll(c);
    }

    public int capacity() {
        return -1;
    }

    public int remaining() {
        return -1;
    }

    public abstract int size();

    public boolean isEmpty() {
        return this.size() < 1;
    }

    public abstract ShortIterator iterator();

    public abstract ShortSpliterator spliterator();

    public void clear() {
        ShortIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextShort();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
