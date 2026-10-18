package org.boxutil.base.api.container;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * For any element is {@code null}, it should be treated as {@code 0}
 * or {@code Number(0)} if the element was specific primitive
 * type that the List implementation defined.
 */
public interface ListExt<E> extends List<E>, SequencedCollectionExt<E>, Container {
    /**
     * Updates the element at the specified index using the given update
     * function. The current value is obtained from {@code get(index)}; no
     * default value is used. The update function is applied to that current
     * value, and the resulting value is stored at the specified index by
     * {@code set(index, newValue)}.
     *
     * <p>If the update function returns {@code null} and this list permits
     * {@code null} elements, the element at the specified index is set to
     * {@code null}.
     *
     * <p>If the specified index is out of range, {@code get(index)} throws
     * {@link IndexOutOfBoundsException} before the update function is applied.
     *
     * <p>If the update function itself throws an (unchecked) exception, the
     * exception is rethrown, and this list is left unchanged.
     *
     * <p>The update function should not modify this list during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this list, then returning the resulting value:
     *
     * <pre> {@code
     * E oldValue = get(index);
     * E newValue = updateFunction.apply(oldValue);
     * set(index, newValue);
     * return newValue;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param index index of the element to update
     * @param updateFunction the function to compute a new value from the
     *        current value at {@code index}
     * @return the new value stored at the specified index, which may be
     *         {@code null} if this list permits {@code null} elements
     * @throws NullPointerException if {@code updateFunction} is {@code null},
     *         or if the new value is {@code null} and this list does not permit
     *         {@code null} elements
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IndexOutOfBoundsException if the specified index is out of range
     *         ({@code index < 0 || index >= size()})
     * @throws UnsupportedOperationException if the {@code set} operation is not
     *         supported by this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the new value prevents it from
     *         being stored in this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the new value
     *         prevents it from being stored in this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default E update(int index, @NotNull UnaryOperator<E> updateFunction) {
        Objects.requireNonNull(updateFunction);

        E oldValue = this.get(index);
        E newValue = updateFunction.apply(oldValue);
        this.set(index, newValue);
        return newValue;
    }

    /**
     * Swaps the elements between this list and the specified list. After this
     * operation, this list contains the elements previously held by the
     * specified list, and the specified list contains the elements previously
     * held by this list. If the specified list is the same as this list, the
     * operation has no effect.
     *
     * <p>For lists of equal size, the element at each index in this list is
     * exchanged with the element at the same index in the specified list, so
     * the relative order of the elements from each list is preserved in the
     * other list. Implementations may support lists of different sizes by
     * exchanging their entire contents and adjusting their sizes, or may
     * require equal sizes and throw {@link IllegalArgumentException}
     * otherwise. The supported size relationship is implementation-specific
     * and should be documented by the implementation.
     *
     * <p>The specified list must be compatible with this list such that all
     * elements can be exchanged without altering their values.
     *
     * <p>Unless {@code source} is this list, this operation is a structural
     * modification. Iterators, spliterators, and other traversal objects
     * previously obtained from either list are considered invalid after this
     * operation; their subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}. For lists, previously
     * obtained {@code subList} views and list iterators may be invalidated or
     * may observe implementation-specific contents after this operation.
     *
     * <p><b>Implementation Note:</b> Implementations may perform the swap by
     * exchanging internal data structures directly instead of transferring
     * elements one by one. In that case, references to internal data obtained
     * through implementation-specific means (if any) may observe the contents
     * now held by the other list, and previously obtained views or iterators
     * may be invalidated. This interface does not specify the identity,
     * validity, or fail-fast behavior of such views, iterators, or internal
     * references after a swap.
     *
     * @implSpec
     * The default implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param source the list whose elements are to be exchanged with the
     *        elements of this list
     * @throws NullPointerException if the specified list is {@code null}
     * @throws UnsupportedOperationException if the swap operation is not
     *         supported by this list or the specified list; an implementation
     *         that performs the swap using basic mutation operations may also
     *         throw if those operations are unsupported
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of an element in the specified
     *         list prevents it from being stored in this list, or vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of an element in the
     *         specified list prevents it from being stored in this list, or
     *         vice versa, or if the sizes of the lists are incompatible with
     *         the implementation's swap operation
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void swap(@NotNull List<E> source) {
        throw new UnsupportedOperationException();
    }
}
