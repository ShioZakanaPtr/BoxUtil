package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;

import io.github.shiozakanaptr.acext.util.function.floattype.FloatPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Type specialization interface.
 *
 * @see CollectionExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatCollection extends CollectionExt<Float>, FloatIterable {
    boolean contains(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(float)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.contains(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    boolean add(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(float)
     */
    @Deprecated
    default boolean add(Float e) {
        return this.add(e != null ? e.floatValue() : (float) 0);
    }

    boolean removeElement(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(float)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.removeElement(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    default boolean removeIf(@NotNull FloatPredicate filter) {
        boolean removed = false;
        final FloatIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextFloat())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeIf(FloatPredicate)
     */
    @Deprecated
    default boolean removeIf(@NotNull Predicate<? super Float> filter) {
        return this.removeIf((float l_t) -> filter.test(l_t));
    }

    default boolean addAll(@NotNull float[] elements) {
        boolean result = false;
        for (float element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(float[])
     */
    @Deprecated
    default <T extends Float> boolean addAll(@NotNull T[] elements) {
        return CollectionExt.super.addAll(elements);
    }

    default boolean addAll(@NotNull float[] elements, int srcIndex, int length) {
        boolean result = false;
        for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
            result |= this.add(elements[i]);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(float[], int, int)
     */
    @Deprecated
    default <T extends Float> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CollectionExt.super.addAll(elements, srcIndex, length);
    }

    @NotNull
    FloatIterator iterator();

    @NotNull
    FloatSpliterator spliterator();

    @NotNull
    float[] toArrayRaw();

    @NotNull
    float[] toArray(@NotNull float[] a);

    @NotNull
    default DoubleStream streamRaw() {
        return StreamSupport.doubleStream(FloatSpliterator.wrap(this.spliterator()), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #streamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Float> stream() {
        return CollectionExt.super.stream();
    }

    @NotNull
    default DoubleStream parallelStreamRaw() {
        return StreamSupport.doubleStream(FloatSpliterator.wrap(this.spliterator()), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #parallelStreamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Float> parallelStream() {
        return CollectionExt.super.parallelStream();
    }
}
