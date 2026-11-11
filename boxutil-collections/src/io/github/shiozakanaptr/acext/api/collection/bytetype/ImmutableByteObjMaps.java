package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjList;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.bytetype.ByteObjBitMap;
import io.github.shiozakanaptr.acext.util.collection.bytetype.ByteObjMutablePair;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteObjBiConsumer;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteObjBiFunction;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems"})
final class ImmutableByteObjMaps {
    private final static ByteObjMap<?> EMPTY_MAP = new ImmutableByteObjMapEmpty<>();

    private static byte[] newKeyTable(int cap) {
        return new byte[cap];
    }

    private static <T> T[] newValueTable(int cap) {
        return (T[]) new Object[cap];
    }

    private static <V> ByteObjMap.Entry<V>[] newEntryTable(int cap) {
        return (ByteObjMap.Entry<V>[]) new ByteObjMap.Entry<?>[cap];
    }

    private static int computeSize(long[] bitL) {
        return Long.bitCount(bitL[0]) + Long.bitCount(bitL[1]) + Long.bitCount(bitL[2]) + Long.bitCount(bitL[3]);
    }

    private static int getLimit(long[] bitL) {
        int result = 256;
        for (int i = 3; i > -1;) {
            final int space;
            result -= (space = Long.numberOfLeadingZeros(bitL[i--]));
            if (space != 64) break;
        }
        return result;
    }

    private static abstract class AbstractImmutableByteObjMap<V> implements ByteObjMap<V> {
        public void resize(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public void reserve(int newMaxSize) {
            throw new UnsupportedOperationException();
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        public V put(byte key, V value) {
            throw new UnsupportedOperationException();
        }

        public V putIfAbsent(byte key, V value) {
            throw new UnsupportedOperationException();
        }

        public V remove(byte key) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(byte key, Object value) {
            throw new UnsupportedOperationException();
        }

        public V replace(byte key, V value) {
            throw new UnsupportedOperationException();
        }

        public boolean replace(byte key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }

        public V computeIfAbsent(byte key, ByteFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V computeIfPresent(byte key, ByteObjBiFunction<? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V compute(byte key, ByteObjBiFunction<? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V merge(byte key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V update(byte key, Function<? super V, ? extends V> updateFunction) {
            throw new UnsupportedOperationException();
        }

        public V remapping(byte key, ByteObjBiFunction<? super V, ? extends Byte> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(Map<Byte, V> source) {
            throw new UnsupportedOperationException();
        }

        public boolean swapAt(byte from, byte to) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map<? extends Byte, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(ByteObjBiFunction<? super V, ? extends V> function) {
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

    private final static class ImmutableByteObjMapEmpty<V> extends AbstractImmutableByteObjMap<V> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public V get(byte key) {
            return null;
        }

        public V getOrDefault(byte key, V defaultValue) {
            return null;
        }

        public boolean containsKey(byte key) {
            return false;
        }

        public boolean containsValue(Object value) {
            return false;
        }

        public void forEach(ByteObjBiConsumer<? super V> action) {}

        public int capacity() {
            return 2;
        }

        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return true;
        }

        public ByteSet keySet() {
            return ByteSet.of();
        }

        public CollectionExt<V> values() {
            return ObjSet.of();
        }

        public EntrySetExt<ByteObjMap.Entry<V>> entrySetRaw() {
            return new ImmutableByteCollections.ImmutableMapEntrySet<>();
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

    private final static class ImmutableByteObjMapOne<V> extends AbstractImmutableByteObjMap<V> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient byte key;
        private final transient V value;

        private transient ByteSet keySet = null;
        private transient CollectionExt<V> valueCollection = null;
        private transient EntrySetExt<ByteObjMap.Entry<V>> entrySet = null;

        public ImmutableByteObjMapOne(byte key, V value) {
            this.key = key;
            this.value = value;
        }

        public V get(byte key) {
            return key == this.key ? this.value : null;
        }

        public V getOrDefault(byte key, V defaultValue) {
            return key == this.key ? this.value : defaultValue;
        }

        public boolean containsKey(byte key) {
            return key == this.key;
        }

        public boolean containsValue(Object value) {
            return Objects.equals(value, this.value);
        }

        public void forEach(ByteObjBiConsumer<? super V> action) {
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

        public ByteSet keySet() {
            ByteSet result;
            if ((result = this.keySet) == null) {
                result = this.keySet = ByteSet.of(this.key);
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

        public EntrySetExt<ByteObjMap.Entry<V>> entrySetRaw() {
            EntrySetExt<ByteObjMap.Entry<V>> result;
            if ((result = this.entrySet) == null) {
                result = this.entrySet = new ImmutableByteCollections.ImmutableMapEntrySet<>(new SimpleEntry<>(this.key, this.value));
            }
            return result;
        }

        public int hashCode() {
            return Byte.hashCode(this.key) ^ Objects.hashCode(this.value);
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

    private final static class ImmutableByteObjMapN<V> extends AbstractImmutableByteObjMap<V> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient int size;
        private final transient long[] bitMark;
        private final transient V[] valueTable;

        private transient ByteSet keySet = null;
        private transient CollectionExt<V> valueCollection = null;
        private transient EntrySetExt<ByteObjMap.Entry<V>> entrySet = null;


        public ImmutableByteObjMapN(long[] bitMark, V[] values) {
            this.size = computeSize(this.bitMark = bitMark);
            this.valueTable = values;
        }

        public ImmutableByteObjMapN(int size, long[] bitMark, V[] values) {
            this.size = size;
            this.bitMark = bitMark;
            this.valueTable = values;
        }

        public V get(byte key) {
            final int seq;
            final V[] valueL;
            return (seq = key & 0xff) < (valueL = this.valueTable).length ? valueL[seq] : null;
        }

        public V getOrDefault(byte key, V defaultValue) {
            final int seq;
            return (this.bitMark[(seq = key & 0xff) >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT ? this.valueTable[seq] : defaultValue;
        }

        public boolean containsKey(byte key) {
            final int seq;
            return (this.bitMark[(seq = key & 0xff) >>> 6] & 1L << (seq & 0b111111)) != ImmutableByteCollections.NULL_BIT;
        }

        public boolean containsValue(Object value) {
            final V[] valueL;
            final int capacity = (valueL = this.valueTable).length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((this.bitMark[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT && Objects.equals(value, valueL[seq])) return true;
            }
            return false;
        }

        public void forEach(ByteObjBiConsumer<? super V> action) {
            final V[] valueL;
            final int capacity = (valueL = this.valueTable).length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((this.bitMark[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT) action.accept((byte) seq, valueL[seq]);
            }
        }

        public int capacity() {
            return this.valueTable.length;
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return false;
        }

        public ByteSet keySet() {
            ByteSet result;
            if ((result = this.keySet) == null) {
                result = this.keySet = new ImmutableByteCollections.ImmutableByteSetN(this.bitMark);
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

        public EntrySetExt<ByteObjMap.Entry<V>> entrySetRaw() {
            EntrySetExt<ByteObjMap.Entry<V>> result;
            if ((result = this.entrySet) == null) {
                result = this.entrySet = new ImmutableByteCollections.ImmutableMapEntrySet<>(this.toEntryArray());
            }
            return result;
        }

        public int hashCode() {
            final V[] valueL = this.valueTable;
            final int capacity = valueL.length;
            int result = 0;
            for (int seq = 0; seq < capacity; seq++) {
                if ((this.bitMark[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT) {
                    result += Byte.hashCode((byte) seq) ^ Objects.hashCode(valueL[seq]);
                }
            }
            return result;
        }

        public boolean equals(Object o) {
            if (o == this) return true;

            if (o instanceof Map<?, ?>) {
                final Map<?, ?> castMap = (Map<?, ?>) o;
                if (castMap.size() != this.size()) return false;
                try {
                    if (castMap instanceof ImmutableByteObjMapN<?>) {
                        final ImmutableByteObjMapN<?> cast = (ImmutableByteObjMapN<?>) castMap;
                        final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;
                        if (!(currBitL[0] == srcBitL[0] &&
                                currBitL[1] == srcBitL[1] &&
                                currBitL[2] == srcBitL[2] &&
                                currBitL[3] == srcBitL[3])) return false;

                        final V[] srcValueL = (V[]) cast.valueTable;

                        final int capacity = srcValueL.length;
                        for (int seq = 0; seq < capacity; seq++) {
                            if ((srcBitL[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT) {
                                final V currValue = this.valueTable[seq], srcValue = srcValueL[seq];
                                if (srcValue == null && currValue != null || srcValue != null && !srcValue.equals(currValue)) return false;
                            }
                        }
                    } else {
                        for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                            final Byte srcKey = (Byte) entry.getKey();
                            final V srcValue = (V) entry.getValue();

                            final int seq = (srcKey != null ? srcKey : (byte) 0) & 0xff, picker = seq >>> 6;
                            final long pos = 1L << (seq & 0b111111);
                            if ((this.bitMark[picker] & pos) == ImmutableByteCollections.NULL_BIT) return false;

                            final V currValue = this.valueTable[seq];
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
            final StringBuilder sb = new StringBuilder(this.size * 6 + 2);
            sb.append('{');
            final V[] valueL = this.valueTable;
            final int capacity = valueL.length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((this.bitMark[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT) {
                    final V currValue;
                    if (sb.length() > 1) sb.append(',').append(' ');
                    sb.append((byte) seq)
                            .append('=')
                            .append((currValue = valueL[seq]) != this ? currValue : "(this Map)");
                }
            }
            sb.append('}');
            return sb.toString();
        }

        private ByteObjMap.Entry<V>[] toEntryArray() {
            final V[] valueL;
            final ByteObjMap.Entry<V>[] result = newEntryTable(this.size);
            final int capacity = (valueL = this.valueTable).length;
            for (int seq = 0, idx = 0; seq < capacity; seq++) {
                if ((this.bitMark[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT) {
                    result[idx++] = new SimpleEntry<>((byte) seq, valueL[seq]);
                }
            }
            return result;
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorage<>(this.bitMark, this.valueTable);
        }
    }

    private final static class SimpleEntry<V> implements ByteObjMap.Entry<V> {
        private final byte key;
        private final V value;

        private SimpleEntry(byte key, V value) {
            this.key = key;
            this.value = value;
        }

        public byte getKeyRaw() {
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
            return Byte.hashCode(this.key) ^ Objects.hashCode(this.value);
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

        private final byte keyCollect;
        private final V valueCollect;

        public ProxyStorageOne(byte keyCollect, V valueCollect) {
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
            return new ImmutableByteObjMapOne<>(this.keyCollect, this.valueCollect);
        }
    }

    private final static class ProxyStorage<V> implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient long[] bitMark;
        private transient V[] valueTable;

        public ProxyStorage(long[] bitMark, V[] valueTable) {
            this.bitMark = bitMark;
            this.valueTable = valueTable;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            final long[] bitL = this.bitMark;
            out.writeLong(bitL[0]);
            out.writeLong(bitL[1]);
            out.writeLong(bitL[2]);
            out.writeLong(bitL[3]);

            final V[] valueL;
            final int capacity = (valueL = this.valueTable).length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT) out.writeObject(valueL[seq]);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            final long[] bitL;
            InternalCollectionUtil.SerialCheck.ioCheckSize(computeSize(bitL = this.bitMark = new long[]{in.readLong(), in.readLong(), in.readLong(), in.readLong()}), 2, 256);

            final int capacity;
            final V[] valueL = this.valueTable = newValueTable(capacity = getLimit(bitL));
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != ImmutableByteCollections.NULL_BIT) {
                    valueL[seq] = (V) in.readObject();
                }
            }
        }

        private Object readResolve() throws ObjectStreamException {
            final long[] bitL;
            final V[] valueL;
            if (!((bitL = this.bitMark) != null && (valueL = this.valueTable) != null)) throw new StreamCorruptedException("Illegal array 'NULL'");
            return new ImmutableByteObjMapN<>(bitL, valueL);
        }
    }

    static <V> ByteObjMap<V> of() {
        return (ByteObjMap<V>) EMPTY_MAP;
    }

    static <V> ByteObjMap<V> of(byte k1, V v1) {
        return new ImmutableByteObjMapOne<>(k1, v1);
    }

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2) {
        final byte[] k = newKeyTable(2);
        final V[] v = newValueTable(2);
        k[0] = k1;
        k[1] = k2;
        v[0] = v1;
        v[1] = v2;
        return ofArrays(k, v);
    }

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3) {
        final byte[] k = newKeyTable(3);
        final V[] v = newValueTable(3);
        k[0] = k1;
        k[1] = k2;
        k[2] = k3;
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        return ofArrays(k, v);
    }

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3, byte k4, V v4) {
        final byte[] k = newKeyTable(4);
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

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3, byte k4, V v4, byte k5, V v5) {
        final byte[] k = newKeyTable(5);
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

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3, byte k4, V v4, byte k5, V v5, byte k6, V v6) {
        final byte[] k = newKeyTable(6);
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

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3, byte k4, V v4, byte k5, V v5, byte k6, V v6, byte k7, V v7) {
        final byte[] k = newKeyTable(7);
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

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3, byte k4, V v4, byte k5, V v5, byte k6, V v6, byte k7, V v7, byte k8, V v8) {
        final byte[] k = newKeyTable(8);
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

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3, byte k4, V v4, byte k5, V v5, byte k6, V v6, byte k7, V v7, byte k8, V v8, byte k9, V v9) {
        final byte[] k = newKeyTable(9);
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

    static <V> ByteObjMap<V> of(byte k1, V v1, byte k2, V v2, byte k3, V v3, byte k4, V v4, byte k5, V v5, byte k6, V v6, byte k7, V v7, byte k8, V v8, byte k9, V v9, byte k10, V v10) {
        final byte[] k = newKeyTable(10);
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

    static <V> ByteObjMap.Entry<V> entry(byte k, V v) {
        return new SimpleEntry<>(k, v);
    }

    @SafeVarargs
    static <V> ByteObjMap<V> ofEntries(Map.Entry<? extends Byte, ? extends V>... entries) {
        if (entries == null) throw new IllegalArgumentException();
        final int limit;
        switch (limit = entries.length) {
            case 0: return (ByteObjMap<V>) EMPTY_MAP;
            case 1: {
                final Byte rawKey;
                return new ImmutableByteObjMapOne<>((rawKey = entries[0].getKey()) != null ? rawKey.byteValue() : (byte) 0, entries[0].getValue());
            }
        }

        final ByteObjBitMap<V> collect = new ByteObjBitMap<>(limit);
        for (Map.Entry<? extends Byte, ? extends V> entry : entries) {
            collect.put(entry.getKey(), entry.getValue());
        }
        final int sizeL = collect.size();
        final long[] keyL = collect.keyTable();
        final V[] valueL = collect.valueTable();

        switch (sizeL) {
            case 0: return (ByteObjMap<V>) EMPTY_MAP;
            case 1: {
                final Byte rawKey;
                return new ImmutableByteObjMapOne<>((rawKey = entries[0].getKey()) != null ? rawKey.byteValue() : (byte) 0, entries[0].getValue());
            }
            default: return new ImmutableByteObjMapN<>(sizeL, keyL, valueL);
        }
    }

    static <V> ByteObjMap<V> ofArrays(byte[] keys, V[] values) {
        final int limit;
        if (!(keys != null && values != null && (limit = keys.length) == values.length)) throw new IllegalArgumentException();
        switch (limit) {
            case 0: return (ByteObjMap<V>) EMPTY_MAP;
            case 1: return new ImmutableByteObjMapOne<>(keys[0], values[0]);
        }

        final ByteObjBitMap<V> collect = new ByteObjBitMap<>(limit);
        for (int i = 0; i < limit; i++) {
            collect.put(keys[i], values[i]);
        }
        final int sizeL = collect.size();
        final long[] keyL = collect.keyTable();
        final V[] valueL = collect.valueTable();

        switch (sizeL) {
            case 0: return (ByteObjMap<V>) EMPTY_MAP;
            case 1: return new ImmutableByteObjMapOne<>(keys[0], values[0]);
            default: return new ImmutableByteObjMapN<>(sizeL, keyL, valueL);
        }
    }

    static <V> ByteObjMap<V> copyOf(Map<? extends Byte, ? extends V> source) {
        if (source instanceof AbstractImmutableByteObjMap) {
            return (ByteObjMap<V>) source;
        }

        switch (source.size()) {
            case 0: return (ByteObjMap<V>) EMPTY_MAP;
            case 1: {
                final ByteObjPair<V> tmp = new ByteObjMutablePair<>();
                source.forEach((l_k, l_v) -> {
                    tmp.left(l_k);
                    tmp.right(l_v);
                });
                return new ImmutableByteObjMapOne<>(tmp.leftRaw(), tmp.right());
            }
            default: {
                final ByteObjBitMap<V> collect = new ByteObjBitMap<>(source);
                final int sizeL = collect.size();
                final long[] keyL = collect.keyTable();
                final V[] valueL = collect.valueTable();

                switch (sizeL) {
                    case 0: return (ByteObjMap<V>) EMPTY_MAP;
                    case 1: {
                        final ByteObjPair<V> tmp = new ByteObjMutablePair<>();
                        source.forEach((l_k, l_v) -> {
                            tmp.left(l_k);
                            tmp.right(l_v);
                        });
                        return new ImmutableByteObjMapOne<>(tmp.leftRaw(), tmp.right());
                    }
                    default: return new ImmutableByteObjMapN<>(sizeL, keyL, valueL);
                }
            }
        }
    }

    private ImmutableByteObjMaps() {}
}
