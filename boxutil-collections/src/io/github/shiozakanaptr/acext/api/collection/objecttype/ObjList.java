package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Type specialization interface.
 *
 * @see ListExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjList<E> extends ListExt<E>, ObjCollection<E> {
    @NotNull
    ObjList<E> subList(int fromIndex, int toIndex);

}
