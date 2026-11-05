package io.github.shiozakanaptr.acext.api.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public interface CollectionExt<E> extends Collection<E>, Container {
    /**
     * Removes a single instance of the specified element from this collection,
     * if it is present (optional operation).
     *
     * <p>More formally, removes an element {@code e} such that
     * {@code Objects.equals(element, e)}, if this collection contains one or
     * more such elements.  If this collection does not contain the element,
     * it is unchanged.  Returns {@code true} if this collection contained the
     * specified element (or equivalently, if this collection changed as a
     * result of the call).
     *
     * <p>This method is named {@code removeElement} rather than {@code remove}
     * on purpose.  The {@link java.util.List} interface exposes both
     * {@code remove(int index)} and {@code remove(Object o)}; when the element
     * type is {@link Integer}, an invocation such as {@code list.remove(1)}
     * silently resolves to the positional overload, removing the element at
     * <em>index</em> {@code 1} rather than the element whose <em>value</em> is
     * {@code 1}.  The error is invisible to the compiler and typically surfaces
     * only as corrupted data at run time.  A distinct name for removal by value
     * eliminates this overload-resolution hazard for all element types,
     * including primitive specializations.  The same naming strategy is used
     * by {@link java.util.Vector#removeElement(Object)}.
     *
     * <p>Callers should always use this method for removal <em>by value</em>.
     * The default {@link #remove(Object)} method delegates to this method.
     *
     * @param element element to be removed from this collection, if present
     * @return {@code true} if this collection contained the specified element
     *         (or equivalently, if this collection changed as a result of the
     *         call)
     * @throws UnsupportedOperationException if the {@code removeElement}
     *         operation is not supported by this collection
     * @see #remove(Object)
     * @see java.util.Collection#remove(Object)
     */
    boolean removeElement(Object element);

    /**
     * Removes a single instance of the specified element from this collection,
     * if it is present (optional operation).
     *
     * <p>More formally, removes an element {@code e} such that
     * {@code Objects.equals(o, e)}, if this collection contains one or more
     * such elements.  If this collection does not contain the element, it is
     * unchanged.  Returns {@code true} if this collection contained the
     * specified element (or equivalently, if this collection changed as a
     * result of the call).
     *
     * <p>This method is implemented as {@code return this.removeElement(o);}.
     *
     * <p><strong>Deprecation note:</strong> This interface deliberately names
     * removal by value {@link #removeElement(Object)} and removal by position
     * {@code removeAt(int)} in order to avoid overload-resolution ambiguity.
     * Although this interface itself declares no {@code remove(int)} overload,
     * subinterfaces such as the {@code List} extension interface do.  When the
     * element type {@code E} is {@link Integer}, an invocation such as
     * {@code list.remove(1)} can resolve to {@code remove(int index)} rather
     * than to this method, so the element at <em>index</em> {@code 1} is
     * removed instead of the element whose <em>value</em> is {@code 1}.  The
     * mistake is invisible to the compiler and typically surfaces only as
     * corrupted data at run time.  The same hazard can arise in interfaces
     * specialized over primitive types.  To keep removal by value unambiguous
     * across all specializations, callers should use
     * {@link #removeElement(Object)} directly, and should use
     * {@code removeAt(int)} for removal by position.
     *
     * <p>This method exists only for source compatibility with existing code;
     * new code should call {@link #removeElement(Object)} instead.
     *
     * @param o element to be removed from this collection, if present
     * @return {@code true} if this collection contained the specified element
     *         (or equivalently, if this collection changed as a result of the
     *         call)
     * @throws UnsupportedOperationException if the {@code removeElement}
     *         operation is not supported by this collection
     * @see #removeElement(Object)
     * @see java.util.Collection#remove(Object)
     * @see java.util.List#remove(int)
     * @deprecated This method is ambiguous with
     *             {@link java.util.List#remove(int) remove(int)} when the element
     *             type is {@link Integer}; an invocation such as
     *             {@code list.remove(1)} resolves to removal by position rather
     *             than removal by value, and the error goes undetected at compile
     *             time.  Use {@link #removeElement(Object)} for removal by value.
     */
    @Deprecated
    default boolean remove(Object o) {
        return this.removeElement(o);
    }

    /**
     * Adds all the elements in the specified array to this collection
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
