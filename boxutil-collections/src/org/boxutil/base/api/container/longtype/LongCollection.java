package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.CollectionExt;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface LongCollection extends CollectionExt<Long>, LongIterable {
    boolean contains(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.contains(o != null ? ((Long) o).longValue() : (long) 0);
    }

    boolean add(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Long e) {
        return this.add(e != null ? e.longValue() : (long) 0);
    }

    boolean remove(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.remove(o != null ? ((Long) o).longValue() : (long) 0);
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
        return this.removeIf((long l_value) -> filter.test(l_value));
    }

    default boolean addAll(@NotNull long[] elements) {
        Objects.requireNonNull(elements);

        boolean result = false;
        for (long element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Long> boolean addAll(@NotNull T[] elements) {
        return CollectionExt.super.addAll(elements);
    }

    default boolean addAll(@NotNull long[] elements, int srcIndex, int length) {
        Objects.requireNonNull(elements);

        boolean result = false;
        for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
            result |= this.add(elements[i]);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Long> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CollectionExt.super.addAll(elements, srcIndex, length);
    }

    @NotNull LongIterator iterator();

    @NotNull LongSpliterator spliterator();

    @NotNull long[] toArrayRaw();

    @NotNull long[] toArray(@NotNull long[] a);

    @NotNull
    default LongStream streamRaw() {
        return StreamSupport.longStream(this.spliterator(), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Long> stream() {
        return CollectionExt.super.stream();
    }

    @NotNull
    default LongStream parallelStreamRaw() {
        return StreamSupport.longStream(this.spliterator(), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Long> parallelStream() {
        return CollectionExt.super.parallelStream();
    }
}
