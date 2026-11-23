package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjObjBidiMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Type specialization interface.
 *
 * @see BidiMap
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjObjBidiMap<K, V> extends BidiMap<K, V>, ObjObjMap<K, V> {
    @NotNull ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, V>> entrySet() {
        return ObjObjMap.super.entrySet();
    }

    @NotNull ObjObjBidiMap<V, K> inverse();
}
