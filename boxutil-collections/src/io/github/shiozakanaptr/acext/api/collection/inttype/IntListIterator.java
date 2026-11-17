package io.github.shiozakanaptr.acext.api.collection.inttype;

import java.util.ListIterator;

public interface IntListIterator extends ListIterator<Integer>, IntIterator {
    boolean hasNext();

    int nextIndex();

    int nextInt();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextInt()
     */
    @Deprecated
    default Integer next() {
        return this.nextInt();
    }

    boolean hasPrevious();

    int previousIndex();

    int previousInt();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #previousInt()
     */
    @Deprecated
    default Integer previous() {
        return this.previousInt();
    }

    void remove();

    void set(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(int)
     */
    @Deprecated
    default void set(Integer e) {
        this.set(e != null ? e.intValue() : (int) 0);
    }

    void add(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int)
     */
    @Deprecated
    default void add(Integer e) {
        this.add(e != null ? e.intValue() : (int) 0);
    }
}
