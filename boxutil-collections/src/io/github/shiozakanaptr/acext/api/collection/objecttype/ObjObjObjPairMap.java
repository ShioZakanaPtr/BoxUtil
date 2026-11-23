package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import org.jetbrains.annotations.NotNull;

public interface ObjObjObjPairMap<L, R, V> extends PairMap<L, R, V> {
    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    ObjCollection<V> valueCollection();

    @NotNull
    EntrySetExt<ObjObjObjPairMap.PairEntry<L, R, V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default EntrySetExt<PairMap.PairEntry<L, R, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R, V> extends PairMap.PairEntry<L, R, V> {
    }
}
