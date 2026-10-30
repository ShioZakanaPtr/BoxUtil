package org.boxutil.base.api.container;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * For any element is {@code null}, it should be treated as {@code 0} or
 * {@code Number(0)} if the element was specific primitive type that the
 * List implementation defined.
 */
public interface ListExt<E> extends List<E>, Container {
    // For compatibility
    default E getFirst() {
        return this.get(0);
    }

    // For compatibility
    default E getLast() {
        return this.get(this.size() - 1);
    }

    // For compatibility
    default void addFirst(E e) {
        this.add(0, e);
    }

    // For compatibility
    default void addLast(E e) {
        this.add(e);
    }

    /**
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the list.
     *
     * <p>This method is named {@code removeAt} rather than {@code remove} on
     * purpose, so that removal <em>by position</em> can never be confused with
     * removal <em>by value</em>:
     *
     * <ul>
     *   <li>If the element type {@code E} is {@link Integer}, a call such as
     *       {@code list.remove(1)} silently resolves to
     *       {@code E remove(int index)} instead of
     *       {@link #remove(Object) boolean remove(Object)}, because the
     *       {@code int} overload is more specific than the {@code Object}
     *       overload.  Code that was intended to remove the <em>value</em>
     *       {@code 1} therefore removes the <em>element at index</em> {@code 1}
     *       instead.  The mistake is invisible to the compiler and typically
     *       surfaces only as corrupted data at run time.</li>
     *
     *   <li>If this interface is specialized over a primitive type (for example
     *       an {@code IntList}), or over a boxed type in a way that exposes both
     *       {@code remove(int)} and {@code remove(Object)} overloads, the same
     *       accidental overload resolution reappears and cannot be fixed by the
     *       caller without an explicit cast to the boxed or primitive type.
     *       Using a distinct, unambiguous name for positional removal avoids the
     *       problem uniformly across all specializations.</li>
     * </ul>
     *
     * <p>Callers should therefore always use this method for positional removal,
     * and should use {@link #remove(Object)} for removal by value.  The
     * deprecated {@link #remove(int)} method exists only for source compatibility
     * and should not be called.
     *
     * @param index index of the element to be removed
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the {@code removeAt} operation is
     *         not supported by this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     * @see #remove(Object)
     */
    E removeAt(int index);

    /**
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the list.
     *
     * <p>This method is implemented as {@code return this.removeAt(index);}.
     *
     * @param index index of the element to be removed
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     * @see #removeAt(int)
     * @deprecated This method is ambiguous with
     *             {@link #remove(Object) remove(Object)}.  When the element type
     *             {@code E} is {@link Integer}, an invocation such as
     *             {@code list.remove(1)} is resolved to this overload, i.e. it
     *             removes the element at <em>index</em> {@code 1} rather than the
     *             element whose <em>value</em> is {@code 1}, and the error goes
     *             undetected at compile time.  The same hazard could arise in
     *             interfaces specialized over primitive types, where the
     *             {@code int} parameter cannot express both "by index" and
     *             "by value" semantics.  Use {@link #removeAt(int)} for removal
     *             by position, and {@link #remove(Object)} for removal by value.
     */
    @Deprecated
    default E remove(int index) {
        return this.removeAt(index);
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it
     * is present (optional operation).  If this list does not contain the element,
     * it is unchanged.  More formally, removes the element {@code e} such that
     * {@code Objects.equals(element, e)} (if such an element exists).  Returns
     * {@code true} if this list contained the specified element (or equivalently,
     * if this list changed as a result of the call).
     *
     * <p>This method is named {@code removeElement} rather than {@code remove} on
     * purpose, so that removal <em>by value</em> can never be confused with
     * removal <em>by position</em>:
     *
     * <ul>
     *   <li>If the element type {@code E} is {@link Integer}, the inherited
     *       {@link #remove(int) E remove(int index)} overload is more specific
     *       than {@link #remove(Object) boolean remove(Object)}, so a call such as
     *       {@code list.remove(1)} silently removes the <em>element at index</em>
     *       {@code 1} instead of the element whose <em>value</em> is {@code 1}.
     *       Because that method also returns {@code E} rather than {@code boolean},
     *       the mistake is not caught by the compiler and usually surfaces only as
     *       corrupted data or a surprising return value at run time.  This method
     *       has no such overload, so {@code list.removeElement(1)} always means
     *       "remove the value {@code 1}" and autoboxing resolves the argument
     *       unambiguously.</li>
     *
     *   <li>If this interface is specialized over a primitive type (for example an
     *       {@code IntList}), or over a boxed type in a way that exposes both a
     *       positional and a value-based {@code remove} overload, the same
     *       accidental overload resolution reappears, and the caller cannot fix it
     *       without an explicit cast to the boxed or primitive type.  Using a
     *       distinct, unambiguous name for value-based removal avoids the problem
     *       uniformly across all specializations, since this method is the only
     *       one named {@code removeElement}.</li>
     * </ul>
     *
     * <p>Callers should therefore always use this method for removal by value, and
     * should use {@link #removeAt(int)} for removal by position.  The deprecated
     * {@link #remove(Object)} method exists only for source compatibility and
     * should not be called.
     *
     * <p>Note that this method accepts {@code Object} rather than {@code E} so
     * that it can be invoked without a cast whenever the argument's static type
     * differs from the element type (for example when removing an {@link Integer}
     * from a list of {@code Number}).
     *
     * @param element element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     * @throws ClassCastException if the type of the specified element is
     *         incompatible with this list (optional)
     * @throws NullPointerException if the specified element is null and this list
     *         does not permit null elements (optional)
     * @throws UnsupportedOperationException if the {@code removeElement} operation
     *         is not supported by this list
     * @see #removeAt(int)
     */
    boolean removeElement(Object element);

    /**
     * Removes the first occurrence of the specified element from this list, if it
     * is present (optional operation).  If this list does not contain the element,
     * it is unchanged.  More formally, removes the element {@code e} such that
     * {@code Objects.equals(o, e)} (if such an element exists).  Returns
     * {@code true} if this list contained the specified element (or equivalently,
     * if this list changed as a result of the call).
     *
     * <p>This method is implemented as {@code return this.removeElement(o);}.
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     * @throws ClassCastException if the type of the specified element is
     *         incompatible with this list (optional)
     * @throws NullPointerException if the specified element is null and this list
     *         does not permit null elements (optional)
     * @throws UnsupportedOperationException if the {@code remove} operation is not
     *         supported by this list
     * @see #removeElement(Object)
     * @deprecated This method is ambiguous with
     *             {@link #removeAt(int) removeAt(int)} / {@link #remove(int)}
     *             because it shares the name {@code remove} with the positional
     *             overload.  When the element type {@code E} is {@link Integer},
     *             an invocation such as {@code list.remove(1)} is resolved to
     *             {@code E remove(int index)} rather than to this method, i.e. it
     *             removes the element at <em>index</em> {@code 1} instead of the
     *             element whose <em>value</em> is {@code 1}; because the two
     *             overloads differ in return type as well ({@code E} versus
     *             {@code boolean}), the error is not detected at compile time.
     *             The same hazard could arise in interfaces specialized over
     *             primitive types, where a single {@code remove} name cannot
     *             express both "by index" and "by value" semantics.  Use
     *             {@link #removeElement(Object)} for removal by value and
     *             {@link #removeAt(int)} for removal by position.
     */
    @Deprecated
    default boolean remove(Object o) {
        return this.removeElement(o);
    }

    // For compatibility
    default E removeFirst() {
        return this.removeAt(0);
    }

    // For compatibility
    default E removeLast() {
        return this.removeAt(this.size() - 1);
    }

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
    default E update(int index, @NotNull Function<? super E, ? extends E> updateFunction) {
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
     *         supported by this list or the specified list, or this list was
     *         a sub list; an implementation that performs the swap using basic
     *         mutation operations may also throw if those operations are
     *         unsupported
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

    /**
     * Swaps the elements at the specified positions in this list. After this
     * operation, the element previously at {@code fromIndex} is located at
     * {@code toIndex}, and the element previously at {@code toIndex} is located
     * at {@code fromIndex}. If {@code fromIndex == toIndex}, this operation has
     * no effect.
     *
     * <p>This operation does not change the size of this list.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this list:
     *
     * <pre> {@code
     * E left = get(fromIndex);
     * E right = get(toIndex);
     * set(toIndex, left);
     * set(fromIndex, right);
     * }</pre>
     *
     * @param fromIndex the index of one element to be swapped
     * @param toIndex the index of the other element to be swapped
     * @throws IndexOutOfBoundsException if either {@code fromIndex} or
     *         {@code toIndex} is out of range
     *         ({@code fromIndex < 0 || fromIndex >= size() ||
     *         toIndex < 0 || toIndex >= size()})
     * @throws UnsupportedOperationException if the {@code set} operation is not
     *         supported by this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void swapAt(int fromIndex, int toIndex) {
        E left = this.get(fromIndex);
        E right = this.get(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    /**
     * Removes the elements in the right-open interval
     * {@code [fromIndex, toIndex)} from this list. Any subsequent elements are
     * shifted to the left, and the size of this list is decreased by
     * {@code toIndex - fromIndex}. If {@code fromIndex == toIndex}, this
     * operation has no effect.
     *
     * <p>Unless the interval is empty, this operation is a structural
     * modification. Iterators and other traversal objects previously obtained
     * from this list are considered invalid after this operation; their
     * subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}.
     *
     * @param fromIndex the inclusive lower bound of the range to erase
     * @param toIndex the exclusive upper bound of the range to erase
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0} or
     *         {@code toIndex > size()}
     * @throws IllegalArgumentException if {@code fromIndex > toIndex}
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void erase(int fromIndex, int toIndex) {
        ListIterator<E> it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.next();
            it.remove();
        }
    }

    /**
     * Replaces each element in the right-open interval
     * {@code [fromIndex, toIndex)} with the result of applying the specified
     * operator to that element. For each element {@code e} in the interval, the
     * new value is {@code operator.apply(e)}. If the operator returns
     * {@code null} and this list permits {@code null} elements, the element is
     * replaced with {@code null}; the element is not removed. If
     * {@code fromIndex == toIndex}, this operation has no effect.
     *
     * <p>This operation does not change the size of this list.
     *
     * <p>If the operator itself throws an (unchecked) exception, the exception
     * is rethrown, and this list is left unchanged for the elements not yet
     * processed. The operator should not modify this list during computation.
     *
     * @implNote
     * Implementations may apply the operator to the elements in any order
     * within the specified interval, provided that each element in the interval
     * is replaced exactly once by the result of applying the operator to its
     * original value.
     *
     * @param fromIndex the inclusive lower bound of the range to replace
     * @param toIndex the exclusive upper bound of the range to replace
     * @param operator the function to compute the replacement value for each
     *        element in the range
     * @throws NullPointerException if {@code operator} is {@code null}, or if
     *         the operator returns {@code null} for some element and this list
     *         does not permit {@code null} elements
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0} or
     *         {@code toIndex > size()}
     * @throws IllegalArgumentException if {@code fromIndex > toIndex}
     * @throws UnsupportedOperationException if the {@code set} operation is not
     *         supported by this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of a replacement value prevents
     *         it from being stored in this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of a replacement value
     *         prevents it from being stored in this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void replaceRange(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
        ListIterator<E> it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.apply(it.next()));
        }
    }

    /**
     * Computes a new value for each element in the right-open interval
     * {@code [fromIndex, toIndex)} by applying the specified operator to that
     * element. For each element {@code e} in the interval, let
     * {@code newValue = operator.apply(e)}. If {@code newValue} is non-null,
     * the element is replaced with {@code newValue}; if {@code newValue} is
     * {@code null}, the element is removed from this list. Removing an element
     * shifts any subsequent elements to the left, so the next element to be
     * processed is the element that moves into the current position. If
     * {@code fromIndex == toIndex}, this operation has no effect.
     *
     * <p>Returns {@code true} if this operation removed at least one element
     * from this list, i.e. if it caused a structural modification by removal.
     * Replacing an element with a non-null value via {@code set} is not a
     * structural modification.
     *
     * <p>Unless the interval is empty or the operator returns a non-null value
     * for every element in the interval, this operation is a structural
     * modification. Iterators and other traversal objects previously obtained
     * from this list are considered invalid after this operation; their
     * subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}.
     *
     * <p>If the operator itself throws an (unchecked) exception, the exception
     * is rethrown. Elements already processed before the exception may have
     * been replaced or removed; elements not yet processed are left unchanged.
     * The operator should not modify this list during computation.
     *
     * @implSpec
     * An implementation is expected to process the elements originally in the
     * interval in order from {@code fromIndex} to {@code toIndex - 1}, applying
     * the operator to each element and either replacing or removing it
     * according to the result, while adjusting the current position after a
     * removal so that no original element in the interval is skipped. The
     * implementation must return {@code true} if and only if it removed at
     * least one element while processing the interval.
     *
     * @param fromIndex the inclusive lower bound of the range to compute
     * @param toIndex the exclusive upper bound of the range to compute
     * @param operator the function to compute a new value for each element in
     *        the range; returning {@code null} causes the element to be removed
     * @return {@code true} if at least one element was removed from this list by
     *         this operation, i.e. the operation caused a structural
     *         modification by removal; {@code false} if no element was removed,
     *         including when the interval is empty or every element in the
     *         interval was replaced with a non-null value
     * @throws NullPointerException if {@code operator} is {@code null}
     * @throws IndexOutOfBoundsException if {@code fromIndex < 0} or
     *         {@code toIndex > size()}
     * @throws IllegalArgumentException if {@code fromIndex > toIndex}
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this list, or if the {@code set} operation is
     *         not supported and at least one element is to be replaced
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of a replacement value prevents
     *         it from being stored in this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of a replacement value
     *         prevents it from being stored in this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default boolean computeRange(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
        boolean result = false;
        ListIterator<E> it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            E newValue = operator.apply(it.next());
            if (newValue != null) {
                it.set(newValue);
            } else {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * Inserts the elements in the specified range of the specified array into
     * this list at the specified position (optional operation). Shifts the
     * element currently at that position (if any) and any subsequent elements
     * to the right (increases their indices). The new elements appear in this
     * list in the order they appear in the specified range of the array, with
     * the element at {@code elements[srcIndex]} becoming the element at index
     * {@code dstIndex} in this list.
     *
     * <p>This method is equivalent in effect to
     * {@link #addAll(int, java.util.Collection)} with the elements of the
     * specified range of the array as its argument. The behavior of this
     * operation is undefined if the specified array is modified while the
     * operation is in progress.
     *
     * <p>If {@code length} is zero, this method returns {@code false}. If
     * {@code length} is negative, this method inserts no elements and returns
     * {@code false}. In either case, {@code dstIndex} must still be within the
     * range {@code [0, size()]}.
     *
     * <p>If this list does not permit {@code null} elements and the specified
     * range contains a {@code null} element, this method may throw
     * {@link NullPointerException} when that element is inserted.
     *
     * @param dstIndex index at which to insert the first element from the
     *        specified range; must be between {@code 0} (inclusive) and
     *        {@code size()} (inclusive)
     * @param elements the array containing the elements to be inserted into this
     *        list
     * @param srcIndex the index of the first element in {@code elements} to
     *        insert
     * @param length the number of elements to insert; if zero or negative, no
     *        elements are inserted
     * @return {@code true} if this list changed as a result of the call
     * @throws NullPointerException if {@code elements} is {@code null}, or if the
     *         specified range contains a {@code null} element and this list
     *         does not permit {@code null} elements
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IndexOutOfBoundsException if {@code dstIndex} is out of range
     *         ({@code dstIndex < 0 || dstIndex > size()})
     * @throws ArrayIndexOutOfBoundsException if {@code length} is positive and
     *         {@code srcIndex} is negative, or {@code srcIndex + length} is
     *         greater than the length of {@code elements}
     * @throws UnsupportedOperationException if the {@code add} operation is not
     *         supported by this list
     * @throws ClassCastException if the class of an element in the specified
     *         range prevents it from being added to this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of an element in the
     *         specified range prevents it from being added to this list
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalStateException if not all the elements can be added at
     *         this time due to insertion restrictions
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    <T extends E> boolean addAll(int dstIndex, @NotNull T[] elements, int srcIndex, int length);

    @NotNull ListExt<E> subList(int fromIndex, int toIndex);
}
