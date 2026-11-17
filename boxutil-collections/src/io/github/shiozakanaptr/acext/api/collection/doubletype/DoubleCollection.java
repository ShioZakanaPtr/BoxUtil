package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;

import io.github.shiozakanaptr.acext.util.function.doubletype.DoublePredicate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface DoubleCollection extends CollectionExt<Double>, DoubleIterable {
    boolean contains(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(double)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.contains(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    boolean add(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(double)
     */
    @Deprecated
    default boolean add(Double e) {
        return this.add(e != null ? e.doubleValue() : (double) 0);
    }

    boolean removeElement(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(double)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.removeElement(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    default boolean removeIf(@NotNull DoublePredicate filter) {
        boolean removed = false;
        final DoubleIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextDouble())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeIf(DoublePredicate)
     */
    @Deprecated
    default boolean removeIf(@NotNull Predicate<? super Double> filter) {
        return this.removeIf((double l_t) -> filter.test(l_t));
    }

    default boolean addAll(@NotNull double[] elements) {
        boolean result = false;
        for (double element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(double[])
     */
    @Deprecated
    default <T extends Double> boolean addAll(@NotNull T[] elements) {
        return CollectionExt.super.addAll(elements);
    }

    default boolean addAll(@NotNull double[] elements, int srcIndex, int length) {
        boolean result = false;
        for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
            result |= this.add(elements[i]);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(double[], int, int)
     */
    @Deprecated
    default <T extends Double> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CollectionExt.super.addAll(elements, srcIndex, length);
    }

    @NotNull
    DoubleIterator iterator();

    @NotNull
    DoubleSpliterator spliterator();

    @NotNull
    double[] toArrayRaw();

    @NotNull
    double[] toArray(@NotNull double[] a);

    @NotNull
    default DoubleStream streamRaw() {
        return StreamSupport.doubleStream(this.spliterator(), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #streamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Double> stream() {
        return CollectionExt.super.stream();
    }

    @NotNull
    default DoubleStream parallelStreamRaw() {
        return StreamSupport.doubleStream(this.spliterator(), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #parallelStreamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Double> parallelStream() {
        return CollectionExt.super.parallelStream();
    }
}
