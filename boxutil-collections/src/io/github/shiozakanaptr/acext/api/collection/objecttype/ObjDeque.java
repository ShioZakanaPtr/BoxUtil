package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.DequeExt;

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
