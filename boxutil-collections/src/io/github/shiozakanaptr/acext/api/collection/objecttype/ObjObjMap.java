package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ObjObjMap<K, V> extends MapExt<K, V> {
    @NotNull
    ObjSet<K> keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<ObjObjMap.Entry<K, V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull EntrySetExt<Map.Entry<K, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K, V> extends Map.Entry<K, V> {
    }
}
