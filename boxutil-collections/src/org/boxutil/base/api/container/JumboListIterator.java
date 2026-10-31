package org.boxutil.base.api.container;

import java.util.ListIterator;

/**
 * A {@link ListIterator} for Jumbo lists whose index range and size may
 * exceed {@link Integer#MAX_VALUE}.
 *
 * <p>This interface extends {@link ListIterator} with long-valued index
 * queries. The inherited {@link ListIterator#nextIndex()} and
 * {@link ListIterator#previousIndex()} methods return {@code int} and may not
 * be able to represent positions in a Jumbo list. Implementations should
 * provide {@link #nextIndex64()} and {@link #previousIndex64()} as the
 * long-valued counterparts for such lists.
 *
 * @param <E> the type of elements returned by this iterator
 *
 * @apiNote This interface is intended for use with Jumbo lists, that is,
 *          lists whose index range and size may exceed
 *          {@link Integer#MAX_VALUE}. Callers should prefer the
 *          {@code *64} index methods when iterating over such lists.
 *
 * @see ListIterator
 * @see ListIterator#nextIndex()
 * @see ListIterator#previousIndex()
 * @see JumboContainer
 */
public interface JumboListIterator<E> extends ListIterator<E> {
    /**
     * Returns the index of the element that would be returned by a subsequent
     * call to {@link #next()}, as a {@code long}.
     *
     * <p>If the iterator is at the end of the list, this method returns the
     * size of the list as a {@code long}.
     *
     * @return the index of the element that would be returned by a subsequent
     *         call to {@code next()}, or the list size if the iterator is at
     *         the end of the list
     * @see #next()
     * @see #previousIndex64()
     */
    long nextIndex64();

    /**
     * Returns the index of the element that would be returned by a subsequent
     * call to {@link #previous()}, as a {@code long}.
     *
     * <p>If the iterator is at the beginning of the list, this method returns
     * {@code -1L}.
     *
     * @return the index of the element that would be returned by a subsequent
     *         call to {@code previous()}, or {@code -1L} if the iterator is at
     *         the beginning of the list
     * @see #previous()
     * @see #nextIndex64()
     */
    long previousIndex64();
}
