package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjList;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.longtype.LongObjLinearProbingHashMap;
import io.github.shiozakanaptr.acext.util.collection.longtype.LongObjMutablePair;
import io.github.shiozakanaptr.acext.util.function.longtype.LongObjBiConsumer;
import io.github.shiozakanaptr.acext.util.function.longtype.LongObjBiFunction;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
final class ImmutableLongObjMaps {
    private final static LongObjMap<?> EMPTY_MAP = new ImmutableLongObjMapEmpty<>();

    private static long[] newKeyTable(int cap) {
        return new long[cap];
    }

    private static <T> T[] newValueTable(int cap) {
        return (T[]) new Object[cap];
    }

    private static <V> LongObjMap.Entry<V>[] newEntryTable(int cap) {
        return (LongObjMap.Entry<V>[]) new LongObjMap.Entry<?>[cap];
    }

    private static abstract class AbstractImmutableLongObjMap<V> implements LongObjMap<V> {
        public void resize(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public void reserve(int newMaxSize) {
            throw new UnsupportedOperationException();
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        public V put(long key, V value) {
            throw new UnsupportedOperationException();
        }

        public V putIfAbsent(long key, V value) {
            throw new UnsupportedOperationException();
        }

        public V remove(long key) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(long key, Object value) {
            throw new UnsupportedOperationException();
        }

        public V replace(long key, V value) {
            throw new UnsupportedOperationException();
        }

        public boolean replace(long key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }

        public V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V computeIfPresent(long key, LongObjBiFunction<? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V compute(long key, LongObjBiFunction<? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V merge(long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V update(long key, Function<? super V, ? extends V> updateFunction) {
            throw new UnsupportedOperationException();
        }

        public V remapping(long key, LongObjBiFunction<? super V, ? extends Long> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(Map<Long, V> source) {
            throw new UnsupportedOperationException();
        }

        public boolean swapAt(long from, long to) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(LongObjBiFunction<? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        public V getDefaultReturnValue() {
            return null;
        }

        public V setDefaultReturnValue(V newDefault) {
            throw new UnsupportedOperationException();
        }

        public int remaining() {
            return 0;
        }

        public void clear(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean isMutable() {
            return false;
        }
    }

    private final static class ImmutableLongObjMapEmpty<V> extends AbstractImmutableLongObjMap<V> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public V get(long key) {
            return null;
        }

        public V getOrDefault(long key, V defaultValue) {
            return null;
        }

        public boolean containsKey(long key) {
            return false;
        }

        public boolean containsValue(Object value) {
            return false;
        }

        public void forEach(LongObjBiConsumer<? super V> action) {}

        public int capacity() {
            return 2;
        }

        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return true;
        }

        public LongSet keySet() {
            return LongSet.of();
        }

        public CollectionExt<V> values() {
            return ObjSet.of();
        }

        public EntrySetExt<Entry<V>> entrySetRaw() {
            return new ImmutableLongCollections.ImmutableMapEntrySet<>();
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof Map<?, ?>) return ((Map<?, ?>) o).isEmpty();
            return false;
        }

        public String toString() {
            return "{}";
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageEmpty<>();
        }
    }

    private final static class ImmutableLongObjMapOne<V> extends AbstractImmutableLongObjMap<V> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient long key;
        private final transient V value;

        private transient LongSet keySet = null;
        private transient CollectionExt<V> valueCollection = null;
        private transient EntrySetExt<LongObjMap.Entry<V>> entrySet = null;

        public ImmutableLongObjMapOne(long key, V value) {
            this.key = key;
            this.value = value;
        }

        public V get(long key) {
            return ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, this.key) ? this.value : null;
        }

        public V getOrDefault(long key, V defaultValue) {
            return ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, this.key) ? this.value : defaultValue;
        }

        public boolean containsKey(long key) {
            return ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, this.key);
        }

        public boolean containsValue(Object value) {
            return Objects.equals(value, this.value);
        }

        public void forEach(LongObjBiConsumer<? super V> action) {
            action.accept(this.key, this.value);
        }

        public int capacity() {
            return 2;
        }

        public int size() {
            return 1;
        }

        public boolean isEmpty() {
            return false;
        }

        public LongSet keySet() {
            LongSet result;
            if ((result = this.keySet) == null) {
                result = this.keySet = LongSet.of(this.key);
            }
            return result;
        }

        public CollectionExt<V> values() {
            CollectionExt<V> result;
            if ((result = this.valueCollection) == null) {
                result = this.valueCollection = ObjList.of(this.value);
            }
            return result;
        }

        public EntrySetExt<LongObjMap.Entry<V>> entrySetRaw() {
            EntrySetExt<LongObjMap.Entry<V>> result;
            if ((result = this.entrySet) == null) {
                result = this.entrySet = new ImmutableLongCollections.ImmutableMapEntrySet<>(new SimpleEntry<>(this.key, this.value));
            }
            return result;
        }

        public int hashCode() {
            return Long.hashCode(this.key) ^ Objects.hashCode(this.value);
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof Map<?, ?>) {
                final Map<?, ?> castMap = (Map<?, ?>) o;
                if (castMap.size() != 1) return false;
                return castMap.containsKey(this.key) && Objects.equals(this.value, castMap.get(this.key));
            }
            return false;
        }

        public String toString() {
            return "{" + this.key + '=' + (this.value != this ? this.value : "(this Map)") + '}';
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageOne<>(this.key, this.value);
        }
    }

    private final static class ImmutableLongObjMapN<V> extends AbstractImmutableLongObjMap<V> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient boolean withNullKey;
        private final transient int size;
        private final transient int capacity;
        private final transient int posMask;
        private final transient long[] keyTable;
        private final transient V[] valueTable;

        private transient LongSet keySet = null;
        private transient CollectionExt<V> valueCollection = null;
        private transient EntrySetExt<LongObjMap.Entry<V>> entrySet = null;

        public ImmutableLongObjMapN(boolean withNullKey, int size, int capacity, long[] keys, V[] values) {
            this.withNullKey = withNullKey;
            this.size = size;
            this.capacity = capacity;
            this.posMask = capacity - 1;
            this.keyTable = keys;
            this.valueTable = values;
        }

        public int find(long key) {
            if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, ((long) ImmutableLongCollections.NULL_SLOT))) {
                return this.withNullKey ? this.capacity : ~this.capacity;
            }

            int pos = ImmutableLongCollections.DEFAULT_BEHAVIOR.hashing(key);
            for (long currKey; (currKey = this.keyTable[pos &= this.posMask]) != ((long) ImmutableLongCollections.NULL_SLOT); pos++) {
                if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, currKey)) {
                    return pos;
                }
            }
            return ~pos;
        }

        public V get(long key) {
            if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, ((long) ImmutableLongCollections.NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : null;

            long currKey;
            for (int pos = ImmutableLongCollections.DEFAULT_BEHAVIOR.hashing(key); (currKey = this.keyTable[pos &= this.posMask]) != ((long) ImmutableLongCollections.NULL_SLOT); pos++) {
                if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, currKey)) return this.valueTable[pos];
            }
            return null;
        }

        public V getOrDefault(long key, V defaultValue) {
            if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, ((long) ImmutableLongCollections.NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : defaultValue;

            long currKey;
            for (int pos = ImmutableLongCollections.DEFAULT_BEHAVIOR.hashing(key); (currKey = this.keyTable[pos &= this.posMask]) != ((long) ImmutableLongCollections.NULL_SLOT); pos++) {
                if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, currKey)) return this.valueTable[pos];
            }
            return defaultValue;
        }

        public boolean containsKey(long key) {
            if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, ((long) ImmutableLongCollections.NULL_SLOT))) return this.withNullKey;

            long currKey;
            for (int pos = ImmutableLongCollections.DEFAULT_BEHAVIOR.hashing(key); (currKey = this.keyTable[pos &= this.posMask]) != ((long) ImmutableLongCollections.NULL_SLOT); pos++) {
                if (ImmutableLongCollections.DEFAULT_BEHAVIOR.equals(key, currKey)) return true;
            }
            return false;
        }

        public boolean containsValue(Object value) {
            final int capL = this.capacity;
            final V[] valueL = this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                if (this.keyTable[pos] != ((long) ImmutableLongCollections.NULL_SLOT) && Objects.equals(value, valueL[pos])) return true;
            }

            return this.withNullKey && Objects.equals(value, valueL[capL]);
        }

        public void forEach(LongObjBiConsumer<? super V> action) {
            final int capL = this.capacity;
            final long[] keyL = this.keyTable;
            final V[] valueL = this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                final long currKey;
                if ((currKey = keyL[pos]) != ((long) ImmutableLongCollections.NULL_SLOT)) {
                    action.accept(currKey, valueL[pos]);
                }
            }

            if (this.withNullKey) action.accept(keyL[capL], valueL[capL]);
        }

        public int capacity() {
            return this.capacity;
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return false;
        }

        public LongSet keySet() {
            LongSet result;
            if ((result = this.keySet) == null) {
                result = this.keySet = LongSet.of(this.keyTable);
            }
            return result;
        }

        public CollectionExt<V> values() {
            CollectionExt<V> result;
            if ((result = this.valueCollection) == null) {
                result = this.valueCollection = ObjList.of(this.valueTable);
            }
            return result;
        }

        public EntrySetExt<LongObjMap.Entry<V>> entrySetRaw() {
            EntrySetExt<LongObjMap.Entry<V>> result;
            if ((result = this.entrySet) == null) {
                result = this.entrySet = new ImmutableLongCollections.ImmutableMapEntrySet<>(this.toEntryArray());
            }
            return result;
        }

        public int hashCode() {
            int result = 0;
            final int capL = this.capacity;
            final long[] keyL = this.keyTable;
            final V[] valueL = this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                final long currKey;
                if ((currKey = keyL[pos]) != ((long) ImmutableLongCollections.NULL_SLOT)) {
                    result += Long.hashCode(currKey) ^ Objects.hashCode(valueL[pos]);
                }
            }

            if (this.withNullKey) result += Long.hashCode(keyL[capL]) ^ Objects.hashCode(valueL[capL]);
            return result;
        }

        public boolean equals(Object o) {
            if (o == this) return true;

            if (o instanceof Map<?, ?>) {
                final Map<?, ?> castMap = (Map<?, ?>) o;
                if (castMap.size() != this.size()) return false;
                try {
                    if (castMap instanceof ImmutableLongObjMapN<?>) {
                        final ImmutableLongObjMapN<?> cast = (ImmutableLongObjMapN<?>) castMap;
                        if (this.withNullKey != cast.withNullKey) return false;
                        final long[] srcKeyL = cast.keyTable;
                        final V[] currValueL = this.valueTable, srcValueL = (V[]) cast.valueTable;

                        final int capacity = srcKeyL.length;
                        for (int srcPos = 0; srcPos < capacity; srcPos++) {
                            final long srcKey;
                            if ((srcKey = srcKeyL[srcPos]) != ((long) ImmutableLongCollections.NULL_SLOT)) {
                                final int currPos = this.find(srcKey);
                                if (currPos < 0) return false;

                                final V currValue = currValueL[currPos], srcValue = srcValueL[srcPos];
                                if (srcValue == null && currValue != null || srcValue != null && !srcValue.equals(currValue)) return false;
                            }
                        }
                        if (this.withNullKey) return this.keyTable[this.capacity] == srcKeyL[cast.capacity] && Objects.equals(currValueL[this.capacity], srcValueL[cast.capacity]);
                    } else {
                        for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                            final Long srcKey = (Long) entry.getKey();
                            final V srcValue = (V) entry.getValue();
                            final int currPos = this.find(srcKey);
                            if (currPos < 0) return false;

                            final V currValue = this.valueTable[currPos];
                            if (srcValue == null && currValue != null || srcValue != null && !srcValue.equals(currValue)) return false;
                        }
                    }
                    return true;
                } catch (ClassCastException | NullPointerException unused) {
                    return false;
                }
            }
            return false;
        }

        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('{');
            final int capL = this.capacity;
            final long[] keyL = this.keyTable;
            final V[] valueL = this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                final long currKey;
                if ((currKey = keyL[pos]) != ((long) ImmutableLongCollections.NULL_SLOT)) {
                    final V currValue;
                    if (sb.length() > 1) sb.append(',').append(' ');
                    sb.append(currKey)
                            .append('=')
                            .append((currValue = valueL[pos]) != this ? currValue : "(this Map)");
                }
            }

            if (this.withNullKey) {
                final V currValue;
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(keyL[capL])
                        .append('=')
                        .append((currValue = valueL[capL]) != this ? currValue : "(this Map)");
            }
            sb.append('}');
            return sb.toString();
        }

        private long[] toKeyArray() {
            int idx = 0;
            final int capL = this.capacity;
            final long[] keyL = this.keyTable, result = newKeyTable(this.size);
            for (int pos = 0; pos < capL;) {
                final long currKey;
                if ((currKey = keyL[pos++]) != ((long) ImmutableLongCollections.NULL_SLOT)) {
                    result[idx++] = currKey;
                }
            }

            if (this.withNullKey) result[idx] = keyL[capL];
            return result;
        }

        private V[] toValueArray() {
            int idx = 0;
            final int capL = this.capacity;
            final V[] valueL = this.valueTable, result = newValueTable(this.size);
            for (int pos = 0; pos < capL; pos++) {
                if (this.keyTable[pos] != ((long) ImmutableLongCollections.NULL_SLOT)) {
                    result[idx++] = valueL[pos];
                }
            }

            if (this.withNullKey) result[idx] = valueL[capL];
            return result;
        }

        private LongObjMap.Entry<V>[] toEntryArray() {
            int idx = 0;
            final int capL = this.capacity;
            final long[] keyL = this.keyTable;
            final V[] valueL = this.valueTable;
            final LongObjMap.Entry<V>[] result = newEntryTable(this.size);
            for (int pos = 0; pos < capL; pos++) {
                final long currKey;
                if ((currKey = keyL[pos]) != ((long) ImmutableLongCollections.NULL_SLOT)) {
                    result[idx++] = new SimpleEntry<>(currKey, valueL[pos]);
                }
            }

            if (this.withNullKey) result[idx] = new SimpleEntry<>(keyL[capL], valueL[capL]);
            return result;
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorage<>(this.toKeyArray(), this.toValueArray());
        }
    }

    private final static class SimpleEntry<V> implements LongObjMap.Entry<V> {
        private final long key;
        private final V value;

        private SimpleEntry(long key, V value) {
            this.key = key;
            this.value = value;
        }

        public long getKeyRaw() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V newValue) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public int hashCode() {
            return Long.hashCode(this.key) ^ Objects.hashCode(this.value);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            return Objects.equals(this.key, cast.getKey())
                    && Objects.equals(this.value, cast.getValue());
        }
    }

    private final static class ProxyStorageEmpty<V> implements Serializable {
        private static final long serialVersionUID = 1L;

        private void writeObject(ObjectOutputStream out) throws IOException {}

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {}

        private Object readResolve() throws ObjectStreamException {
            return EMPTY_MAP;
        }
    }

    private final static class ProxyStorageOne<V> implements Serializable {
        private static final long serialVersionUID = 1L;

        private final long keyCollect;
        private final V valueCollect;

        public ProxyStorageOne(long keyCollect, V valueCollect) {
            this.keyCollect = keyCollect;
            this.valueCollect = valueCollect;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private Object readResolve() throws ObjectStreamException {
            return new ImmutableLongObjMapOne<>(this.keyCollect, this.valueCollect);
        }
    }

    private final static class ProxyStorage<V> implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient long[] keyCollect;
        private transient V[] valueCollect;

        public ProxyStorage(long[] keyCollect, V[] valueCollect) {
            this.keyCollect = keyCollect;
            this.valueCollect = valueCollect;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();

            final long[] keyL = this.keyCollect;
            final V[] valueL = this.valueCollect;
            final int size = keyL.length;
            out.writeInt(size);
            for (int i = 0; i < size;) {
                out.writeLong(keyL[i]);
                out.writeObject(valueL[i++]);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();

            final int sizeL = in.readInt();
            InternalCollectionUtil.SerialCheck.ioCheckSize(sizeL, 2, HashContainer.MAXIMUM_CAPACITY);
            InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(sizeL, ImmutableLongCollections.DEFAULT_LOAD_FACTOR, HashContainer.MAXIMUM_CAPACITY);

            final long[] keyL = this.keyCollect = newKeyTable(sizeL);
            final V[] valueL = this.valueCollect = newValueTable(sizeL);
            for (int i = 0; i < sizeL;) {
                keyL[i] = in.readLong();
                valueL[i++] = (V) in.readObject();
            }
        }

        private Object readResolve() throws ObjectStreamException {
            final long[] keyL;
            final V[] valueL;
            if (!((keyL = this.keyCollect) != null && (valueL = this.valueCollect) != null)) throw new StreamCorruptedException("Illegal array 'NULL'");
            return ofArraysSerial(keyL, valueL);
        }
    }

    static <V> LongObjMap<V> of() {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1) {
        return new ImmutableLongObjMapOne<>(k1, v1);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2) {
        final long[] k = newKeyTable(2);
        final V[] v = newValueTable(2);
        k[0] = k1;
        k[1] = k2;
        v[0] = v1;
        v[1] = v2;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3) {
        final long[] k = newKeyTable(3);
        final V[] v = newValueTable(3);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4) {
        final long[] k = newKeyTable(4);
        final V[] v = newValueTable(4);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        k[3] = k4;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        v[3] = v4;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5) {
        final long[] k = newKeyTable(5);
        final V[] v = newValueTable(5);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        k[3] = k4;
        k[4] = k5;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        v[3] = v4;
        v[4] = v5;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6) {
        final long[] k = newKeyTable(6);
        final V[] v = newValueTable(6);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        k[3] = k4;
        k[4] = k5;
        k[5] = k6;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        v[3] = v4;
        v[4] = v5;
        v[5] = v6;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6, long k7, V v7) {
        final long[] k = newKeyTable(7);
        final V[] v = newValueTable(7);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        k[3] = k4;
        k[4] = k5;
        k[5] = k6;
        k[6] = k7;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        v[3] = v4;
        v[4] = v5;
        v[5] = v6;
        v[6] = v7;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6, long k7, V v7, long k8, V v8) {
        final long[] k = newKeyTable(8);
        final V[] v = newValueTable(8);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        k[3] = k4;
        k[4] = k5;
        k[5] = k6;
        k[6] = k7;
        k[7] = k8;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        v[3] = v4;
        v[4] = v5;
        v[5] = v6;
        v[6] = v7;
        v[7] = v8;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6, long k7, V v7, long k8, V v8, long k9, V v9) {
        final long[] k = newKeyTable(9);
        final V[] v = newValueTable(9);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        k[3] = k4;
        k[4] = k5;
        k[5] = k6;
        k[6] = k7;
        k[7] = k8;
        k[8] = k9;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        v[3] = v4;
        v[4] = v5;
        v[5] = v6;
        v[6] = v7;
        v[7] = v8;
        v[8] = v9;
        return ofArrays(k, v);
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6, long k7, V v7, long k8, V v8, long k9, V v9, long k10, V v10) {
        final long[] k = newKeyTable(10);
        final V[] v = newValueTable(10);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        k[3] = k4;
        k[4] = k5;
        k[5] = k6;
        k[6] = k7;
        k[7] = k8;
        k[8] = k9;
        k[9] = k10;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        v[3] = v4;
        v[4] = v5;
        v[5] = v6;
        v[6] = v7;
        v[7] = v8;
        v[8] = v9;
        v[9] = v10;
        return ofArrays(k, v);
    }

    static <V> LongObjMap.Entry<V> entry(long k, V v) {
        return new SimpleEntry<>(k, v);
    }

    @SafeVarargs
    static <V> LongObjMap<V> ofEntries(Map.Entry<? extends Long, ? extends V>... entries) {
        if (entries == null) throw new IllegalArgumentException();
        final int limit;
        switch (limit = entries.length) {
            case 0: return (LongObjMap<V>) EMPTY_MAP;
            case 1: {
                final Long rawKey;
                return new ImmutableLongObjMapOne<>((rawKey = entries[0].getKey()) != null ? rawKey.longValue() : (long) 0, entries[0].getValue());
            }
        }

        final LongObjLinearProbingHashMap<V> collect = new LongObjLinearProbingHashMap<>(limit, ImmutableLongCollections.DEFAULT_LOAD_FACTOR, (HashBehavior<Long>) ImmutableLongCollections.DEFAULT_BEHAVIOR);
        for (Map.Entry<? extends Long, ? extends V> entry : entries) {
            collect.put(entry.getKey(), entry.getValue());
        }
        collect.shrink();
        final boolean withNullEleL = collect.containsKey((long) 0);
        final int sizeL = collect.size(), capacityL = collect.capacity();
        final long[] keyL = collect.keyTable();
        final V[] valueL = collect.valueTable();

        switch (sizeL) {
            case 0: return (LongObjMap<V>) EMPTY_MAP;
            case 1: {
                final Long rawKey;
                return new ImmutableLongObjMapOne<>((rawKey = entries[0].getKey()) != null ? rawKey.longValue() : (long) 0, entries[0].getValue());
            }
            default: return new ImmutableLongObjMapN<>(withNullEleL, sizeL, capacityL, keyL, valueL);
        }
    }

    static <V> LongObjMap<V> ofArraysSerial(long[] keys, V[] values) throws StreamCorruptedException {
        final int limit = keys.length;
        final LongObjLinearProbingHashMap<V> collect = new LongObjLinearProbingHashMap<>(limit, ImmutableLongCollections.DEFAULT_LOAD_FACTOR, (HashBehavior<Long>) ImmutableLongCollections.DEFAULT_BEHAVIOR);
        for (int i = 0; i < limit; i++) {
            collect.put(keys[i], values[i]);
        }
        collect.shrink();
        final boolean withNullEleL = collect.containsKey((long) 0);
        final int sizeL = collect.size(), capacityL = collect.capacity();
        final long[] keyL = collect.keyTable();
        final V[] valueL = collect.valueTable();
        InternalCollectionUtil.SerialCheck.ioCheckElementCount(keys.length, sizeL);
        return new ImmutableLongObjMapN<>(withNullEleL, sizeL, capacityL, keyL, valueL);
    }

    static <V> LongObjMap<V> ofArrays(long[] keys, V[] values) {
        final int limit;
        if (!(keys != null && values != null && (limit = keys.length) == values.length)) throw new IllegalArgumentException();
        switch (limit) {
            case 0: return (LongObjMap<V>) EMPTY_MAP;
            case 1: return new ImmutableLongObjMapOne<>(keys[0], values[0]);
        }

        final LongObjLinearProbingHashMap<V> collect = new LongObjLinearProbingHashMap<>(limit, ImmutableLongCollections.DEFAULT_LOAD_FACTOR, (HashBehavior<Long>) ImmutableLongCollections.DEFAULT_BEHAVIOR);
        for (int i = 0; i < limit; i++) {
            collect.put(keys[i], values[i]);
        }
        collect.shrink();
        final boolean withNullEleL = collect.containsKey((long) 0);
        final int sizeL = collect.size(), capacityL = collect.capacity();
        final long[] keyL = collect.keyTable();
        final V[] valueL = collect.valueTable();

        switch (sizeL) {
            case 0: return (LongObjMap<V>) EMPTY_MAP;
            case 1: return new ImmutableLongObjMapOne<>(keys[0], values[0]);
            default: return new ImmutableLongObjMapN<>(withNullEleL, sizeL, capacityL, keyL, valueL);
        }
    }

    static <V> LongObjMap<V> copyOf(Map<? extends Long, ? extends V> source) {
        if (source instanceof AbstractImmutableLongObjMap) {
            return (LongObjMap<V>) source;
        }

        switch (source.size()) {
            case 0: return (LongObjMap<V>) EMPTY_MAP;
            case 1: {
                final LongObjPair<V> tmp = new LongObjMutablePair<>();
                source.forEach((l_k, l_v) -> {
                    tmp.left(l_k);
                    tmp.right(l_v);
                });
                return new ImmutableLongObjMapOne<>(tmp.leftRaw(), tmp.right());
            }
            default: {
                final LongObjLinearProbingHashMap<V> collect = new LongObjLinearProbingHashMap<>(source, ImmutableLongCollections.DEFAULT_LOAD_FACTOR, (HashBehavior<Long>) ImmutableLongCollections.DEFAULT_BEHAVIOR);
                collect.shrink();
                final boolean withNullEleL = collect.containsKey((long) 0);
                final int sizeL = collect.size(), capacityL = collect.capacity();
                final long[] keyL = collect.keyTable();
                final V[] valueL = collect.valueTable();

                switch (sizeL) {
                    case 0: return (LongObjMap<V>) EMPTY_MAP;
                    case 1: {
                        final LongObjPair<V> tmp = new LongObjMutablePair<>();
                        source.forEach((l_k, l_v) -> {
                            tmp.left(l_k);
                            tmp.right(l_v);
                        });
                        return new ImmutableLongObjMapOne<>(tmp.leftRaw(), tmp.right());
                    }
                    default: return new ImmutableLongObjMapN<>(withNullEleL, sizeL, capacityL, keyL, valueL);
                }
            }
        }
    }

    private ImmutableLongObjMaps() {}
}
