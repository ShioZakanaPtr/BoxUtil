package io.github.shiozakanaptr.acext.api.collection.floattype;

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
public interface FloatOrderedCollection extends FloatCollection, OrderedCollection<Float> {
    float getFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Float getFirst() {
        return this.getFirstRaw();
    }

    float getLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Float getLast() {
        return this.getLastRaw();
    }

    void addFirst(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(float)
     */
    @Deprecated
    default void addFirst(Float e) {
        this.addFirst(e != null ? e.floatValue() : (float) 0);
    }

    void addLast(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(float)
     */
    @Deprecated
    default void addLast(Float e) {
        this.addLast(e != null ? e.floatValue() : (float) 0);
    }

    float removeFirstRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Float removeFirst() {
        return this.removeFirstRaw();
    }

    float removeLastRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Float removeLast() {
        return this.removeLastRaw();
    }

    @NotNull FloatBidiIterator iterator();

    @NotNull FloatBidiIterator iteratorReversed();
}
