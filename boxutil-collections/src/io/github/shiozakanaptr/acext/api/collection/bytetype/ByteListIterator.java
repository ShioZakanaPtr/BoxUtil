package io.github.shiozakanaptr.acext.api.collection.bytetype;

import java.util.ListIterator;

/**
 * Type specialization interface.
 *
 * @see ListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ByteListIterator extends ListIterator<Byte>, ByteBidiIterator {
    boolean hasNext();

    int nextIndex();

    byte nextByte();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextByte()
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
     * @see #previousByte()
     */
    @Deprecated
    default Byte previous() {
        return this.previousByte();
    }

    void remove();

    void set(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(byte)
     */
    @Deprecated
    default void set(Byte e) {
        this.set(e != null ? e.byteValue() : (byte) 0);
    }

    void add(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(byte)
     */
    @Deprecated
    default void add(Byte e) {
        this.add(e != null ? e.byteValue() : (byte) 0);
    }
}
