package io.github.shiozakanaptr.acext.util.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.chartype.*;

public abstract class AbstractCharList extends AbstractCharCollection implements CharList {
    protected AbstractCharList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract char getRaw(int index);

    public char set(int index, char element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(char e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int index, char element) {
        throw new UnsupportedOperationException();
    }

    public char removeAtRaw(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(char o) {
        CharBidiIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextChar()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(char o) {
        CharListIterator it = this.listIterator();
        while (it.hasNext()) {
            if (o == it.nextChar()) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(char o) {
        CharListIterator it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (o == it.previousChar()) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    public abstract CharBidiIterator iterator();

    public abstract CharBidiIterator iteratorReversed();

    public abstract CharSpliterator spliterator();
}
