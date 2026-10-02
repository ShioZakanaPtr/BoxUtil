package org.boxutil.base.api.container.intt;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IntCollection extends Collection<Integer>, IntIterable {
    /**
     * Returns the default return value used when a query does not find a mapping
     * in this container. By default, this value is {@code 0}, but implementations
     * may allow customization via {@link #setDefaultReturnValue(int)}.
     *
     * @return the default return value
     */
    int getDefaultReturnValue();

    /**
     * Sets the default return value used when a query does not find an element in
     * this container. This value will be returned by query methods such
     * as {@code get} when the key is absent.
     *
     * @param newDefault the new default return value
     * @return the previous default return value
     */
    int setDefaultReturnValue(int newDefault);

    boolean contains(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o == null) return this.contains(0);
        if (!(o instanceof Number number)) return false;
        return this.contains(number.intValue());
    }

    @NotNull IntIterator iterator();

    @NotNull int[] toRawArray();

    @NotNull int[] toArray(@NotNull int[] a);

    boolean add(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Integer e) {
        return this.add(e == null ? 0 : e);
    }

    boolean remove(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o == null) return this.remove(0);
        if (!(o instanceof Number number)) return false;
        return this.remove(number.intValue());
    }

    default boolean removeIf(@NotNull IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final IntIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextInt())) {
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
    default boolean removeIf(@NotNull Predicate<? super Integer> filter) {
        Objects.requireNonNull(filter);
        return this.removeIf(filter instanceof IntPredicate cast ? cast : filter::test);
    }

    @NotNull
    IntSpliterator spliterator();

    @NotNull
    default IntStream rawStream() {
        return StreamSupport.intStream(this.spliterator(), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Integer> stream() {
        return Collection.super.stream();
    }

    @NotNull
    default IntStream rawParallelStream() {
        return StreamSupport.intStream(this.spliterator(), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Integer> parallelStream() {
        return Collection.super.parallelStream();
    }
}
