package org.boxutil.base.api.container.shortt;

import org.boxutil.base.api.container.CollectionExt;
import org.boxutil.util.function.shortt.ShortPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface ShortCollection extends CollectionExt<Short>, ShortIterable {
    boolean contains(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        return this.contains(o instanceof Short number ? number.shortValue() : (short) 0);
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
        return this.remove(o instanceof Short number ? number.shortValue() : (short) 0);
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

    @NotNull ShortIterator iterator();

    @NotNull ShortSpliterator spliterator();

    @NotNull short[] toRawArray();

    @NotNull short[] toArray(@NotNull short[] a);
}
