package io.github.shiozakanaptr.acext.api.collection.longtype;

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
public interface LongOrderedCollection extends LongCollection, OrderedCollection<Long> {
    long getFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Long getFirst() {
        return this.getFirstRaw();
    }

    long getLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Long getLast() {
        return this.getLastRaw();
    }

    void addFirst(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(long)
     */
    @Deprecated
    default void addFirst(Long e) {
        this.addFirst(e != null ? e.longValue() : (long) 0);
    }

    void addLast(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(long)
     */
    @Deprecated
    default void addLast(Long e) {
        this.addLast(e != null ? e.longValue() : (long) 0);
    }

    long removeFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Long removeFirst() {
        return this.removeFirstRaw();
    }

    long removeLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Long removeLast() {
        return this.removeLastRaw();
    }

    @NotNull LongBidiIterator iterator();

    @NotNull LongBidiIterator iteratorReversed();
}
