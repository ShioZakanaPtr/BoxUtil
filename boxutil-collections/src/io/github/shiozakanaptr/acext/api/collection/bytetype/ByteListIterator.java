package io.github.shiozakanaptr.acext.api.collection.bytetype;

import java.util.ListIterator;

public interface ByteListIterator extends ListIterator<Byte>, ByteIterator {
    boolean hasNext();

    int nextIndex();

    byte nextByte();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte next() {
        return this.nextByte();
    }

    boolean hasPrevious();

    int previousIndex();

    byte previousByte();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte previous() {
        return this.previousByte();
    }

    void remove();

    void set(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void set(Byte e) {
        this.set(e != null ? e.byteValue() : (byte) 0);
    }

    void add(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void add(Byte e) {
        this.add(e != null ? e.byteValue() : (byte) 0);
    }
}
