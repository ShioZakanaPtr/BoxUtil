package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjObjBiMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjObjBiMap<K, V> extends BiMap<K, V>, ObjObjMap<K, V> {
    @NotNull ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, V>> entrySet() {
        return ObjObjMap.super.entrySet();
    }

    @NotNull ObjObjBiMap<V, K> inverse();
}
