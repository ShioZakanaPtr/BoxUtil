package io.github.shiozakanaptr.acext.api.collection.shorttype;

import java.util.ListIterator;

public interface ShortListIterator extends ListIterator<Short>, ShortIterator {
    boolean hasNext();

    int nextIndex();

    short nextShort();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextShort()
     */
    @Deprecated
    default Short next() {
        return this.nextShort();
    }

    boolean hasPrevious();

    int previousIndex();

    short previousShort();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousShort()
     */
    @Deprecated
    default Short previous() {
        return this.previousShort();
    }

    void remove();

    void set(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(short)
     */
    @Deprecated
    default void set(Short e) {
        this.set(e != null ? e.shortValue() : (short) 0);
    }

    void add(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(short)
     */
    @Deprecated
    default void add(Short e) {
        this.add(e != null ? e.shortValue() : (short) 0);
    }
}
