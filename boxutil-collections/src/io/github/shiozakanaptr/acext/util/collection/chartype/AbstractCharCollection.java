package io.github.shiozakanaptr.acext.util.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.chartype.CharCollection;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharIterator;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharSpliterator;

import java.util.*;

public abstract class AbstractCharCollection extends AbstractCollection<Character> implements CharCollection {
    protected AbstractCharCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(char o) {
        CharIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextChar()) return true;
        }
        return false;
    }

    public boolean contains(Object o) {
        if (o != null && !(o instanceof Character)) return false;
        return this.contains(o != null ? ((Character) o).charValue() : (char) 0);
    }

    public boolean add(char e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(Character e) {
        return this.add(e != null ? e.charValue() : (char) 0);
    }

    public boolean removeElement(char o) {
        CharIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextChar()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeElement(Object o) {
        if (o != null && !(o instanceof Character)) return false;
        return this.removeElement(o != null ? ((Character) o).charValue() : (char) 0);
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

    public abstract CharIterator iterator();

    public abstract CharSpliterator spliterator();

    public void clear() {
        CharIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextChar();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
