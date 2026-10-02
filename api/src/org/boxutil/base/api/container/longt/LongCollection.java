package org.boxutil.base.api.container.longt;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface LongCollection extends Collection<Long>, LongIterable {
    /**
     * Returns the default return value used when a query does not find a mapping
     * in this container. By default, this value is {@code 0}, but implementations
     * may allow customization via {@link #setDefaultReturnValue(long)}.
     *
     * @return the default return value
     */
    long getDefaultReturnValue();

    /**
     * Sets the default return value used when a query does not find an element in
     * this container. This value will be returned by query methods such
     * as {@code get} when the key is absent.
     *
     * @param newDefault the new default return value
     * @return the previous default return value
     */
    long setDefaultReturnValue(long newDefault);

    boolean contains(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o == null) return this.contains(0);
        if (!(o instanceof Number number)) return false;
        return this.contains(number.longValue());
    }

    @NotNull LongIterator iterator();

    @NotNull long[] toRawArray();

    @NotNull long[] toArray(@NotNull long[] a);

    boolean add(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Long e) {
        return this.add(e == null ? 0 : e);
    }

    boolean remove(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o == null) return this.remove(0);
        if (!(o instanceof Number number)) return false;
        return this.remove(number.longValue());
    }

    default boolean removeIf(@NotNull LongPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final LongIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextLong())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean removeIf(@NotNull Predicate<? super Long> filter) {
        Objects.requireNonNull(filter);
        return this.removeIf(filter instanceof LongPredicate cast ? cast : filter::test);
    }

    @NotNull
    LongSpliterator spliterator();

    @NotNull
    default LongStream rawStream() {
        return StreamSupport.longStream(this.spliterator(), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Long> stream() {
        return Collection.super.stream();
    }

    @NotNull
    default LongStream rawParallelStream() {
        return StreamSupport.longStream(this.spliterator(), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Long> parallelStream() {
        return Collection.super.parallelStream();
    }
}
