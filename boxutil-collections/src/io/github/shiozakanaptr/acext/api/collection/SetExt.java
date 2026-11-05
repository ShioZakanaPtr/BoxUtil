package io.github.shiozakanaptr.acext.api.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Typically, for any element is {@code null}, it should be treated as {@code 0}
 * or {@code Number(0)} if the element was specific primitive
 * type that the Set implementation defined.
 */
public interface SetExt<E> extends Set<E>, CollectionExt<E>, Container {
    /**
     * Removes the specified element from this set if it is present
     * (optional operation).
     *
     * <p>More formally, removes an element {@code e} such that
     * {@code Objects.equals(o, e)}, if this set contains such an element.
     * If this set does not contain the element, it is unchanged.  Returns
     * {@code true} if this set contained the specified element (or
     * equivalently, if this set changed as a result of the call).  Since a
     * set contains at most one such element, at most one element is removed.
     *
     * <p>This method is implemented as
     * {@code return CollectionExt.super.remove(o);}, so its behavior is
     * identical to the default implementation inherited from the parent
     * collection extension interface.  It is redeclared here only to attach
     * set-specific documentation and to keep the deprecation notice visible
     * in this interface.
     *
     * <p><strong>Deprecation note:</strong> Unlike the list extension
     * interface, this set interface declares no positional-removal overload,
     * so the {@code remove(int)} versus {@code remove(Object)} overload hazard
     * does not arise directly here.  This method is nevertheless deprecated to
     * keep the removal API uniform across the collection extension hierarchy:
     * removal by value should always be expressed with the unambiguous name
     * {@link #removeElement(Object)}.  Using a single, unambiguous name avoids
     * confusion when a set-like interface is combined with, adapted to, or
     * specialized alongside list-like interfaces that do expose positional
     * removal, and it keeps migration between the collection extension
     * interfaces straightforward.
     *
     * <p>This method exists only for source compatibility with existing code;
     * new code should call {@link #removeElement(Object)} instead.
     *
     * @param o element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     *         (or equivalently, if this set changed as a result of the call)
     * @throws UnsupportedOperationException if the {@code removeElement}
     *         operation is not supported by this set
     * @see #removeElement(Object)
     * @see CollectionExt#remove(Object)
     * @see java.util.Set#remove(Object)
     * @deprecated This method is deprecated for consistency with
     *             {@link CollectionExt#remove(Object)}.  Removal by value should
     *             use the unambiguous {@link #removeElement(Object)} so that the
     *             same call cannot be confused with positional removal in
     *             list-like interfaces or in specializations that expose both
     *             kinds of removal.  The method remains only for source
     *             compatibility.
     */
    @Deprecated
    default boolean remove(Object o) {
        return CollectionExt.super.remove(o);
    }

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
     * if (!contains(element)) return false;
     *
     * E newElement = remappingFunction.apply(element);
     * if (newElement != null && Objects.equal(newEle, element)) return false;
     *
     * remove(element);
     * if (newElement != null) add(newElement);
     * return true;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization or
     * atomicity properties of this method. Any implementation providing atomicity
     * guarantees must override this method and document its concurrency properties.
     *
     * @param element the element to be remapped
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
    default boolean remapping(E element, @NotNull Function<? super E, ? extends E> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        if (!this.contains(element)) return false;

        E newEle = remappingFunction.apply(element);
        if (newEle != null && Objects.equals(newEle, element)) return false;
        this.remove(element);
        if (newEle != null) this.add(newEle);
        return true;
    }

    /**
     * Swaps the elements between this set and the specified set. After this
     * operation, this set contains the elements previously held by the
     * specified set, and the specified set contains the elements previously
     * held by this set. If the specified set is the same as this set, the
     * operation has no effect.
     *
     * <p>The specified set must be compatible with this set such that all
     * elements can be exchanged without altering their values.
     *
     * <p>Unless {@code source} is this set, this operation is a structural
     * modification. Iterators, spliterators, and other traversal objects
     * previously obtained from either set are considered invalid after this
     * operation; their subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}. Previously obtained
     * views may remain associated with the set object, but their validity,
     * contents, and binding relationship after the swap are
     * implementation-specific.
     *
     * <p><b>Implementation Note:</b> Implementations may perform the swap by
     * exchanging internal data structures directly instead of transferring
     * elements one by one. In that case, references to internal data obtained
     * through implementation-specific means (if any) may observe the contents
     * now held by the other set, and previously obtained views or iterators
     * may be invalidated. This interface does not specify the identity,
     * validity, or fail-fast behavior of such views, iterators, or internal
     * references after a swap.
     *
     * @implSpec
     * The default implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param source the set whose elements are to be exchanged with the
     *        elements of this set
     * @throws NullPointerException if the specified set is {@code null}
     * @throws UnsupportedOperationException if the swap operation is not
     *         supported by this set or the specified set; an implementation
     *         that performs the swap using basic mutation operations may also
     *         throw if those operations are unsupported
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of an element in the specified
     *         set prevents it from being stored in this set, or vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of an element in the
     *         specified set prevents it from being stored in this set, or
     *         vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void swap(@NotNull Set<E> source) {
        throw new UnsupportedOperationException();
    }
}
