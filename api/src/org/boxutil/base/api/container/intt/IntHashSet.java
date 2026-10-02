package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.HashContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

/**
 * If any element is <code>null</code>, it should be treated as <code>0</code> or <code>Integer(0)</code>.<p>
 * For store float value, just using {@link Float#floatToRawIntBits(float)} and {@link Float#intBitsToFloat(int)} directly.
 */
public interface IntHashSet extends Set<Integer>, IntCollection, HashContainer<Integer> {
    /**
     * Remaps the specified element in this set. If this set does not contain the
     * specified element, returns {@code false} and leaves the set unchanged.
     * Otherwise, computes a new element from the specified element using the given
     * remapping function and updates the set accordingly.
     *
     * <p>If the remapping function returns {@code null}, the specified element is
     * removed from this set. If the remapping function returns a new element that
     * is equal to the specified element according to this set's behavior, the set
     * is left unchanged and {@code false} is returned. Otherwise, the specified
     * element is removed and the new element is added to this set.
     *
     * <p>If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the current set is left unchanged.
     *
     * <p>The remapping function should not modify this set during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following steps
     * for this set, then returning the result:
     *
     * <pre> {@code
     * if (!contains(key)) return false;
     *
     * Integer newKey = remappingFunction.apply(key);
     * if (newKey != null && getBehavior().equal(key, newKey)) return false;
     *
     * remove(key);
     * if (newKey != null) add(newKey);
     * return true;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization or
     * atomicity properties of this method. Any implementation providing atomicity
     * guarantees must override this method and document its concurrency properties.
     *
     * @param key the element to be remapped
     * @param remappingFunction the function to compute a new element from the
     *        specified element
     * @return {@code true} if the set was modified as a result of this operation,
     *         {@code false} otherwise
     * @throws NullPointerException if {@code remappingFunction} is {@code null}
     * @throws UnsupportedOperationException if the {@code add} or {@code remove}
     *         operation is not supported by this set
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the computed element prevents it
     *         from being stored in this set
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the computed element
     *         prevents it from being stored in this set
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default boolean remapping(int key, IntFunction<? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        if (!this.contains(key)) return false;

        Integer newKey = remappingFunction.apply(key);
        int rawKey = newKey == null ? 0 : newKey;
        if (newKey != null && this.getBehavior().equals(key, rawKey)) return false;
        this.remove(key);
        if (newKey != null) this.add(rawKey);
        return true;
    }

    /**
     * Swaps the elements between this set and the specified set. After this
     * operation, this set contains the elements previously held by the specified
     * set, and the specified set contains the elements previously held by this
     * set. If the specified set is the same as this set, the operation has no
     * effect.
     *
     * <p>The specified set must be compatible with this set such that all
     * elements can be exchanged without altering their values.
     *
     * @param source the set whose elements are to be exchanged with the elements
     *        of this set
     * @throws NullPointerException if the specified set is null
     * @throws UnsupportedOperationException if the {@code add}, {@code remove},
     *         or {@code clear} operation is not supported by this set or the
     *         specified set
     * @throws ClassCastException if the class of an element in the specified set
     *         prevents it from being stored in this set, or vice versa
     * @throws IllegalArgumentException if some property of an element in the
     *         specified set prevents it from being stored in this set, or vice
     *         versa
     */
    void swap(Set<Integer> source);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        return IntCollection.super.contains(o);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Integer integer) {
        return IntCollection.super.add(integer);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        return IntCollection.super.remove(o);
    }

    @NotNull IntSpliterator spliterator();
}
