package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import org.jetbrains.annotations.NotNull;

public interface ObjObjPairMap<X, Y, V> extends PairMap<X, Y, V> {
    @NotNull
    ObjCollection<X> firstKeyCollection();

    @NotNull
    ObjCollection<Y> secondKeyCollection();

    @NotNull
    ObjCollection<V> valueCollection();

    @NotNull
    EntrySetExt<ObjObjPairMap.PairEntry<X, Y, V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default EntrySetExt<PairMap.PairEntry<X, Y, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<X, Y, V> extends PairMap.PairEntry<X, Y, V> {
    }
}
