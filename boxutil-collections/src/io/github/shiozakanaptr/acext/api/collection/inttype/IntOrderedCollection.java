package io.github.shiozakanaptr.acext.api.collection.inttype;

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
public interface IntOrderedCollection extends IntCollection, OrderedCollection<Integer> {
    int getFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Integer getFirst() {
        return this.getFirstRaw();
    }

    int getLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Integer getLast() {
        return this.getLastRaw();
    }

    void addFirst(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(int)
     */
    @Deprecated
    default void addFirst(Integer e) {
        this.addFirst(e != null ? e.intValue() : (int) 0);
    }

    void addLast(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(int)
     */
    @Deprecated
    default void addLast(Integer e) {
        this.addLast(e != null ? e.intValue() : (int) 0);
    }

    int removeFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Integer removeFirst() {
        return this.removeFirstRaw();
    }

    int removeLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Integer removeLast() {
        return this.removeLastRaw();
    }

    @NotNull IntBidiIterator iterator();

    @NotNull IntBidiIterator iteratorReversed();
}
