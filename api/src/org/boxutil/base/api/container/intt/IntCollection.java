package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.CollectionExt;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IntCollection extends CollectionExt<Integer>, IntIterable {
    boolean contains(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.contains(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    boolean add(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Integer e) {
        return this.add(e != null ? e.intValue() : (int) 0);
    }

    boolean remove(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.remove(o != null ? ((Integer) o).intValue() : (int) 0);
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
        return this.removeIf((int l_value) -> filter.test(l_value));
    }

    @NotNull IntIterator iterator();

    @NotNull IntSpliterator spliterator();

    @NotNull int[] toRawArray();

    @NotNull int[] toArray(@NotNull int[] a);

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
        return CollectionExt.super.stream();
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
        return CollectionExt.super.parallelStream();
    }
}
