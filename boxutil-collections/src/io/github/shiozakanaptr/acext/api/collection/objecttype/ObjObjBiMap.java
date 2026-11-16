package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import org.jetbrains.annotations.NotNull;

public interface ObjObjBiMap<K, V> extends BiMap<K, V>, ObjObjMap<K, V> {
    @NotNull
    ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, V>> entrySet() {
        return ObjObjMap.super.entrySet();
    }

    @NotNull ObjObjBiMap<V, K> inverse();
}
