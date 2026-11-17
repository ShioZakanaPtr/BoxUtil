package io.github.shiozakanaptr.acext.api.collection.doubletype;

import java.util.ListIterator;

public interface DoubleListIterator extends ListIterator<Double>, DoubleIterator {
    boolean hasNext();

    int nextIndex();

    double nextDouble();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextDouble()
     */
    @Deprecated
    default Double next() {
        return this.nextDouble();
    }

    boolean hasPrevious();

    int previousIndex();

    double previousDouble();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousDouble()
     */
    @Deprecated
    default Double previous() {
        return this.previousDouble();
    }

    void remove();

    void set(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(double)
     */
    @Deprecated
    default void set(Double e) {
        this.set(e != null ? e.doubleValue() : (double) 0);
    }

    void add(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(double)
     */
    @Deprecated
    default void add(Double e) {
        this.add(e != null ? e.doubleValue() : (double) 0);
    }
}
