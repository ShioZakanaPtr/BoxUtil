package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;

import io.github.shiozakanaptr.acext.util.function.bytetype.BytePredicate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ByteCollection extends CollectionExt<Byte>, ByteIterable {
    boolean contains(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(byte)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.contains(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    boolean add(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(byte)
     */
    @Deprecated
    default boolean add(Byte e) {
        return this.add(e != null ? e.byteValue() : (byte) 0);
    }

    boolean removeElement(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(byte)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.removeElement(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    default boolean removeIf(@NotNull BytePredicate filter) {
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
     * @see #removeIf(BytePredicate)
     */
    @Deprecated
    default boolean removeIf(@NotNull Predicate<? super Byte> filter) {
        return this.removeIf((byte l_t) -> filter.test(l_t));
    }

    default boolean addAll(@NotNull byte[] elements) {
        boolean result = false;
        for (byte element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(byte[])
     */
    @Deprecated
    default <T extends Byte> boolean addAll(@NotNull T[] elements) {
        return CollectionExt.super.addAll(elements);
    }

    default boolean addAll(@NotNull byte[] elements, int srcIndex, int length) {
        boolean result = false;
        for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
            result |= this.add(elements[i]);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(byte[], int, int)
     */
    @Deprecated
    default <T extends Byte> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CollectionExt.super.addAll(elements, srcIndex, length);
    }

    @NotNull
    ByteIterator iterator();

    @NotNull
    ByteSpliterator spliterator();

    @NotNull
    byte[] toArrayRaw();

    @NotNull
    byte[] toArray(@NotNull byte[] a);

    @NotNull
    default IntStream streamRaw() {
        return StreamSupport.intStream(ByteSpliterator.wrap(this.spliterator()), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #streamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Byte> stream() {
        return CollectionExt.super.stream();
    }

    @NotNull
    default IntStream parallelStreamRaw() {
        return StreamSupport.intStream(ByteSpliterator.wrap(this.spliterator()), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #parallelStreamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Byte> parallelStream() {
        return CollectionExt.super.parallelStream();
    }
}
