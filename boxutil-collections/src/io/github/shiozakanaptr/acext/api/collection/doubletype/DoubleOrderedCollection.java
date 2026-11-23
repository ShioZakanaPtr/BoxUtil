package io.github.shiozakanaptr.acext.api.collection.doubletype;

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
public interface DoubleOrderedCollection extends DoubleCollection, OrderedCollection<Double> {
    double getFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Double getFirst() {
        return this.getFirstRaw();
    }

    double getLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Double getLast() {
        return this.getLastRaw();
    }

    void addFirst(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(double)
     */
    @Deprecated
    default void addFirst(Double e) {
        this.addFirst(e != null ? e.doubleValue() : (double) 0);
    }

    void addLast(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(double)
     */
    @Deprecated
    default void addLast(Double e) {
        this.addLast(e != null ? e.doubleValue() : (double) 0);
    }

    double removeFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Double removeFirst() {
        return this.removeFirstRaw();
    }

    double removeLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Double removeLast() {
        return this.removeLastRaw();
    }

    @NotNull DoubleBidiIterator iterator();

    @NotNull DoubleBidiIterator iteratorReversed();
}
