package org.boxutil.base.api.container;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public interface CollectionExt<E> extends Collection<E>, Container {
    /**
     * Adds all of the elements in the specified array to this collection
     * (optional operation). The elements are added in the order they appear in
     * the array, as if by invoking {@link #add(Object)} on each element.
     *
     * <p>This method is equivalent in effect to
     * {@link #addAll(Collection)}, except that it accepts an array of elements.
     * The behavior of this operation is undefined if the specified array is
     * modified while the operation is in progress.
     *
     * <p>If the specified array is empty, this method returns {@code false}.
     *
     * <p>If this collection does not permit {@code null} elements and the
     * specified array contains a {@code null} element, this method may throw
     * {@link NullPointerException} when that element is added.
     *
     * @implSpec
     * The default implementation is equivalent to:
     * <pre>{@code
     * boolean result = false;
     * for (T element : elements) {
     *     result |= this.add(element);
     * }
     * return result;
     * }</pre>
     * The default implementation invokes {@link #add(Object)} once for each
     * element, even after the result is known to be {@code true}. It does not
     * guarantee atomicity: if an invocation of {@code add} throws an exception,
     * elements added before the exception remain in this collection.
     *
     * @param elements the array containing the elements to be added to this
     *        collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws NullPointerException if {@code elements} is {@code null}, or if the
     *         array contains a {@code null} element and this collection does not
     *         permit {@code null} elements
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the {@code add} operation is not
     *         supported by this collection
     * @throws ClassCastException if the class of an element in the specified
     *         array prevents it from being added to this collection
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of an element in the
     *         specified array prevents it from being added to this collection
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalStateException if not all the elements can be added at this
     *         time due to insertion restrictions
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default <T extends E> boolean addAll(@NotNull T[] elements) {
        Objects.requireNonNull(elements);

        boolean result = false;
        for (T element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * Adds the elements in the specified range of the specified array to this
     * collection (optional operation). The elements are added in the order they
     * appear in the array, as if by invoking {@link #add(Object)} on each element
     * from index {@code srcIndex} (inclusive) to index
     * {@code srcIndex + length} (exclusive).
     *
     * <p>This method is equivalent in effect to {@link #addAll(Object[])} with the
     * specified range of the array as its argument. The behavior of this operation
     * is undefined if the specified array is modified while the operation is in
     * progress.
     *
     * <p>If {@code length} is zero, this method returns {@code false}. If
     * {@code length} is negative, this method adds no elements and returns
     * {@code false}.
     *
     * <p>If this collection does not permit {@code null} elements and the
     * specified range contains a {@code null} element, this method may throw
     * {@link NullPointerException} when that element is added.
     *
     * @implSpec
     * The default implementation is equivalent to:
     * <pre>{@code
     * boolean result = false;
     * for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
     *     result |= this.add(elements[i]);
     * }
     * return result;
     * }</pre>
     * The default implementation does not explicitly check the range arguments.
     * If {@code srcIndex} is negative, or if {@code srcIndex + length} is greater
     * than the length of the specified array, an
     * {@link ArrayIndexOutOfBoundsException} is thrown when the array is accessed.
     * The default implementation invokes {@link #add(Object)} once for each
     * element in the range, even after the result is known to be {@code true}. It
     * does not guarantee atomicity: if an invocation of {@code add} throws an
     * exception, elements added before the exception remain in this collection.
     *
     * @param elements the array containing the elements to be added to this
     *        collection
     * @param srcIndex the index of the first element to add
     * @param length the number of elements to add; if negative, no elements are
     *        added
     * @return {@code true} if this collection changed as a result of the call
     * @throws NullPointerException if {@code elements} is {@code null}, or if the
     *         specified range contains a {@code null} element and this collection
     *         does not permit {@code null} elements
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IndexOutOfBoundsException if {@code srcIndex} is negative, or if
     *         {@code srcIndex + length} is greater than the length of the
     *         specified array
     * @throws UnsupportedOperationException if the {@code add} operation is not
     *         supported by this collection
     * @throws ClassCastException if the class of an element in the specified
     *         range prevents it from being added to this collection
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of an element in the
     *         specified range prevents it from being added to this collection
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalStateException if not all the elements can be added at this
     *         time due to insertion restrictions
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default <T extends E> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        Objects.requireNonNull(elements);

        boolean result = false;
        for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
            result |= this.add(elements[i]);
        }
        return result;
    }
}
