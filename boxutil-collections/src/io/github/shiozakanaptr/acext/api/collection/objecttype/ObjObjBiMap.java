package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import org.jetbrains.annotations.NotNull;

public interface ObjObjBiMap<K, V> extends BiMap<K, V> {
    @NotNull
    ObjSet<V> values();

    @NotNull
    EntrySetExt<ObjObjMap.Entry<K, V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    @NotNull ObjObjBiMap<V, K> inverse();
}
