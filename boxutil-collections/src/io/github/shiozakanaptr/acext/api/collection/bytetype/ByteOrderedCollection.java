package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.OrderedCollection;
import org.jetbrains.annotations.NotNull;

/**
 * Type specialization interface.
 *
 * @see OrderedCollection
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ByteOrderedCollection extends ByteCollection, OrderedCollection<Byte> {
    byte getFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Byte getFirst() {
        return this.getFirstRaw();
    }

    byte getLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Byte getLast() {
        return this.getLastRaw();
    }

    void addFirst(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(byte)
     */
    @Deprecated
    default void addFirst(Byte e) {
        this.addFirst(e != null ? e.byteValue() : (byte) 0);
    }

    void addLast(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(byte)
     */
    @Deprecated
    default void addLast(Byte e) {
        this.addLast(e != null ? e.byteValue() : (byte) 0);
    }

    byte removeFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Byte removeFirst() {
        return this.removeFirstRaw();
    }

    byte removeLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Byte removeLast() {
        return this.removeLastRaw();
    }

    @NotNull ByteBidiIterator iterator();

    @NotNull ByteBidiIterator iteratorReversed();
}
