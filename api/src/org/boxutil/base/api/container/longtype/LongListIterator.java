package org.boxutil.base.api.container.longtype;

import java.util.ListIterator;

public interface LongListIterator extends ListIterator<Long>, LongIterator {
    boolean hasNext();

    int nextIndex();

    long nextLong();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long next() {
        return this.nextLong();
    }

    boolean hasPrevious();

    int previousIndex();

    long previousLong();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long previous() {
        return this.previousLong();
    }

    void remove();

    void set(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void set(Long e) {
        this.set(e != null ? e.longValue() : (long) 0);
    }

    void add(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void add(Long e) {
        this.add(e != null ? e.longValue() : (long) 0);
    }
}
