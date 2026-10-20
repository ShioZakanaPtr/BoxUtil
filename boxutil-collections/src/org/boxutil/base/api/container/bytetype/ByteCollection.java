package org.boxutil.base.api.container.bytetype;

import org.boxutil.base.api.container.CollectionExt;
import org.boxutil.util.function.bytetype.BytePredicate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface ByteCollection extends CollectionExt<Byte>, ByteIterable {
    boolean contains(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.contains(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    boolean add(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Byte e) {
        return this.add(e != null ? e.byteValue() : (byte) 0);
    }

    boolean remove(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.remove(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    default boolean removeIf(@NotNull BytePredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final ByteIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextByte())) {
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
    default boolean removeIf(@NotNull Predicate<? super Byte> filter) {
        Objects.requireNonNull(filter);
        return this.removeIf(filter::test);
    }

    @NotNull ByteIterator iterator();

    @NotNull ByteSpliterator spliterator();

    @NotNull byte[] toArrayRaw();

    @NotNull byte[] toArray(@NotNull byte[] a);
}
