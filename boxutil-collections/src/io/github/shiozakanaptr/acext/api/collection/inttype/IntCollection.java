package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;

import io.github.shiozakanaptr.acext.util.function.inttype.IntPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IntCollection extends CollectionExt<Integer>, IntIterable {
    boolean contains(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(int)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.contains(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    boolean add(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int)
     */
    @Deprecated
    default boolean add(Integer e) {
        return this.add(e != null ? e.intValue() : (int) 0);
    }

    boolean removeElement(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(int)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.removeElement(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    default boolean removeIf(@NotNull IntPredicate filter) {
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
     * @see #removeIf(IntPredicate)
     */
    @Deprecated
    default boolean removeIf(@NotNull Predicate<? super Integer> filter) {
        return this.removeIf((int l_t) -> filter.test(l_t));
    }

    default boolean addAll(@NotNull int[] elements) {
        boolean result = false;
        for (int element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int[])
     */
    @Deprecated
    default <T extends Integer> boolean addAll(@NotNull T[] elements) {
        return CollectionExt.super.addAll(elements);
    }

    default boolean addAll(@NotNull int[] elements, int srcIndex, int length) {
        boolean result = false;
        for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
            result |= this.add(elements[i]);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int[], int, int)
     */
    @Deprecated
    default <T extends Integer> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CollectionExt.super.addAll(elements, srcIndex, length);
    }

    @NotNull
    IntIterator iterator();

    @NotNull
    IntSpliterator spliterator();

    @NotNull
    int[] toArrayRaw();

    @NotNull
    int[] toArray(@NotNull int[] a);

    @NotNull
    default IntStream streamRaw() {
        return StreamSupport.intStream(this.spliterator(), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #streamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Integer> stream() {
        return CollectionExt.super.stream();
    }

    @NotNull
    default IntStream parallelStreamRaw() {
        return StreamSupport.intStream(this.spliterator(), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #parallelStreamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Integer> parallelStream() {
        return CollectionExt.super.parallelStream();
    }
}
