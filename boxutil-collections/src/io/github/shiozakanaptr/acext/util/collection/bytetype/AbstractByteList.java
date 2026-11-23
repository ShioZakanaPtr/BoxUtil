package io.github.shiozakanaptr.acext.util.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.bytetype.*;

public abstract class AbstractByteList extends AbstractByteCollection implements ByteList {
    protected AbstractByteList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract byte getRaw(int index);

    public byte set(int index, byte element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(byte e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int index, byte element) {
        throw new UnsupportedOperationException();
    }

    public byte removeAtRaw(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(byte o) {
        ByteBidiIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextByte()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(byte o) {
        ByteListIterator it = this.listIterator();
        while (it.hasNext()) {
            if (o == it.nextByte()) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(byte o) {
        ByteListIterator it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (o == it.previousByte()) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    public abstract ByteBidiIterator iterator();

    public abstract ByteBidiIterator iteratorReversed();

    public abstract ByteSpliterator spliterator();
}
