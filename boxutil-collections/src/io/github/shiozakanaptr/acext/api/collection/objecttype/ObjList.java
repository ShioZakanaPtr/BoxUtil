package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ObjList<E> extends ListExt<E>, ObjCollection<E> {
    @NotNull
    ObjList<E> subList(int fromIndex, int toIndex);

}
