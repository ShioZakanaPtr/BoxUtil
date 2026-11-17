package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

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
