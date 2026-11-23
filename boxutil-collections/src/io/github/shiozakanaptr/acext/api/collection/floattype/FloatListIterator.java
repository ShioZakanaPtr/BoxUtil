package io.github.shiozakanaptr.acext.api.collection.floattype;

import java.util.ListIterator;

/**
 * Type specialization interface.
 *
 * @see ListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatListIterator extends ListIterator<Float>, FloatBidiIterator {
    boolean hasNext();

    int nextIndex();

    float nextFloat();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextFloat()
     */
    @Deprecated
    default Float next() {
        return this.nextFloat();
    }

    boolean hasPrevious();

    int previousIndex();

    float previousFloat();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousFloat()
     */
    @Deprecated
    default Float previous() {
        return this.previousFloat();
    }

    void remove();

    void set(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(float)
     */
    @Deprecated
    default void set(Float e) {
        this.set(e != null ? e.floatValue() : (float) 0);
    }

    void add(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(float)
     */
    @Deprecated
    default void add(Float e) {
        this.add(e != null ? e.floatValue() : (float) 0);
    }
}
