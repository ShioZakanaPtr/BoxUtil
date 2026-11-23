package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.OrderedMap;
import io.github.shiozakanaptr.acext.api.collection.OrderedEntrySet;

/**
 * Type specialization interface.
 *
 * @see OrderedMap
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjObjOrderedMap<K, V> extends ObjObjMap<K, V>, OrderedMap<K, V> {
    ObjObjMap.Entry<K, V> firstEntry();

    ObjObjMap.Entry<K, V> lastEntry();

    ObjObjMap.Entry<K, V> pollFirstEntry();

    ObjObjMap.Entry<K, V> pollLastEntry();

    @NotNull
    ObjOrderedSet<K> keySet();

    @NotNull
    ObjOrderedCollection<V> values();

    @NotNull
    OrderedEntrySet<ObjObjMap.Entry<K, V>> entrySetRaw();

    @NotNull
    default OrderedEntrySet<MapExt.Entry<K, V>> entrySet() {
        return (OrderedEntrySet) this.entrySetRaw();
    }
}
