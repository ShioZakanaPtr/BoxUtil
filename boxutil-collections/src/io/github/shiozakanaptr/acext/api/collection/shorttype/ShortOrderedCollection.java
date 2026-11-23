package io.github.shiozakanaptr.acext.api.collection.shorttype;

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
public interface ShortOrderedCollection extends ShortCollection, OrderedCollection<Short> {
    short getFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Short getFirst() {
        return this.getFirstRaw();
    }

    short getLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Short getLast() {
        return this.getLastRaw();
    }

    void addFirst(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(short)
     */
    @Deprecated
    default void addFirst(Short e) {
        this.addFirst(e != null ? e.shortValue() : (short) 0);
    }

    void addLast(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(short)
     */
    @Deprecated
    default void addLast(Short e) {
        this.addLast(e != null ? e.shortValue() : (short) 0);
    }

    short removeFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Short removeFirst() {
        return this.removeFirstRaw();
    }

    short removeLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Short removeLast() {
        return this.removeLastRaw();
    }

    @NotNull ShortBidiIterator iterator();

    @NotNull ShortBidiIterator iteratorReversed();
}
