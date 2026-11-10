package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ObjSet<E> extends SetExt<E>, ObjCollection<E> {
    static <E> ObjSet<E> of() {
        return ImmutableObjCollections.ofSet();
    }

    static <E> ObjSet<E> of(E element) {
        return ImmutableObjCollections.ofSet(element);
    }

    @SafeVarargs
    static <E> ObjSet<E> of(E... elements) {
        return ImmutableObjCollections.ofSet(elements);
    }

    static <E> ObjSet<E> copyOf(@NotNull Collection<? extends E> source) {
        return ImmutableObjCollections.copyOfSet(source);
    }
}
