package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;

/**
 * Type specialization interface.
 *
 * @see DequeExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjDeque<E> extends DequeExt<E>, ObjQueue<E> {
    /**
     * @deprecated Please use the corresponding method that renamed ones to
     * avoid overload-resolution ambiguity.
     * @see #removeElement(Object)
     */
    @Deprecated
    default boolean remove(Object o) {
        return ObjQueue.super.remove(o);
    }
}
