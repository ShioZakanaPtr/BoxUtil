package org.boxutil.base.api.container.shorttype;

import org.boxutil.base.api.container.CollectionExt;
import org.boxutil.util.function.shorttype.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ShortCollection extends CollectionExt<Short>, ShortIterable {
    boolean contains(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.contains(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    boolean add(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Short e) {
        return this.add(e != null ? e.shortValue() : (short) 0);
    }

    boolean remove(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.remove(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    default boolean removeIf(@NotNull ShortPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final ShortIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextShort())) {
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
    default boolean removeIf(@NotNull Predicate<? super Short> filter) {
        Objects.requireNonNull(filter);
        return this.removeIf(filter::test);
    }

    default boolean addAll(@NotNull short[] elements) {
        Objects.requireNonNull(elements);

        boolean result = false;
        for (short element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Short> boolean addAll(@NotNull T[] elements) {
        return CollectionExt.super.addAll(elements);
    }

    default boolean addAll(@NotNull short[] elements, int srcIndex, int length) {
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
    default <T extends Short> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CollectionExt.super.addAll(elements, srcIndex, length);
    }

    @NotNull ShortIterator iterator();

    @NotNull ShortSpliterator spliterator();

    @NotNull short[] toArrayRaw();

    @NotNull short[] toArray(@NotNull short[] a);

    @NotNull
    default IntStream streamRaw() {
        return StreamSupport.intStream(ShortSpliterator.wrap(this.spliterator()), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Short> stream() {
        return CollectionExt.super.stream();
    }

    @NotNull
    default IntStream parallelStreamRaw() {
        return StreamSupport.intStream(ShortSpliterator.wrap(this.spliterator()), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @NotNull
    default Stream<Short> parallelStream() {
        return CollectionExt.super.parallelStream();
    }
}
