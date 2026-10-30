package org.boxutil.base.api.container.objecttype;

import org.boxutil.base.api.container.ListExt;
import org.jetbrains.annotations.NotNull;

public interface ObjList<E> extends ListExt<E> {
    @NotNull ObjList<E> subList(int fromIndex, int toIndex);
}
