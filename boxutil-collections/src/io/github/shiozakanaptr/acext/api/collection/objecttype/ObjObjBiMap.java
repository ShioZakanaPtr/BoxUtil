package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import org.jetbrains.annotations.NotNull;

public interface ObjObjBiMap<X, Y, V> extends BiMap<X, Y, V> {
    @NotNull
    ObjCollection<X> firstKeyCollection();

    @NotNull
    ObjCollection<Y> secondKeyCollection();

    @NotNull
    ObjCollection<V> valueCollection();

    @NotNull
    EntrySetExt<ObjObjBiMap.BiEntry<X, Y, V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default EntrySetExt<BiMap.BiEntry<X, Y, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface BiEntry<X, Y, V> extends BiMap.BiEntry<X, Y, V> {
    }
}
