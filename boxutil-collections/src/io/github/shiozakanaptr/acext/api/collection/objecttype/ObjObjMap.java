package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import org.jetbrains.annotations.NotNull;

/**
 * Type specialization interface.
 *
 * @see MapExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjObjMap<K, V> extends MapExt<K, V> {
    @NotNull
    ObjSet<K> keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<ObjObjMap.Entry<K, V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K, V> extends MapExt.Entry<K, V> {
    }

}
