package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ObjList<E> extends ListExt<E>, ObjCollection<E> {
    @NotNull ObjList<E> subList(int fromIndex, int toIndex);

    static <E> ObjList<E> of() {
        return ImmutableObjCollections.ofList();
    }

    static <E> ObjList<E> of(E element) {
        return ImmutableObjCollections.ofList(element);
    }

    @SafeVarargs
    static <E> ObjList<E> of(E... elements) {
        return ImmutableObjCollections.ofList(elements);
    }

    static <E> ObjList<E> copyOf(@NotNull Collection<? extends E> source) {
        return ImmutableObjCollections.copyOfList(source);
    }
}
