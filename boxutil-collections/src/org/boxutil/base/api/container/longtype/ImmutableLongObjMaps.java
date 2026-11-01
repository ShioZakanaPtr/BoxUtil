package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.*;
import org.boxutil.util.CollectionUtil;
import org.boxutil.util.collection.HashBehaviorFactory;
import org.boxutil.util.function.longtype.LongObjBiConsumer;
import org.boxutil.util.function.longtype.LongObjBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
final class ImmutableLongObjMaps {
    final static float DEFAULT_LOAD_FACTOR = 0.75f;
    private final static long NULL_SLOT = (long) 0;
    private final static HashBehavior<Long> DEFAULT_HASHER = HashBehaviorFactory.fibonacciMix();
    private final static LongObjMap<?> EMPTY_MAP = new ImmutableLongObjMapEmpty<>();

    private static long[] newKeyTable(int cap) {
        return new long[cap];
    }

    private static long[] newFillKeyTable(long element) {
        return new long[]{element};
    }

    private static <T> T[] newValueTable(int cap) {
        return (T[]) new Object[cap];
    }

    private static <T> T[] newFillValueTable(T element) {
        return (T[]) new Object[]{element};
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

        public V computeIfAbsent(long key, @NotNull LongFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V computeIfPresent(long key, @NotNull LongObjBiFunction<? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V compute(long key, @NotNull LongObjBiFunction<? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V merge(long key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public V update(long key, @NotNull Function<? super V, ? extends V> updateFunction) {
            throw new UnsupportedOperationException();
        }

        public V remapping(long key, LongObjBiFunction<? super V, ? extends Long> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(@NotNull Map<Long, V> source) {
            throw new UnsupportedOperationException();
        }

        public boolean swapAt(long from, long to) {
            throw new UnsupportedOperationException();
        }

        public void putAll(@NotNull Map<? extends Long, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(@NotNull LongObjBiFunction<? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }

        public V getDefaultReturnValue() {
            return null;
        }

        public V setDefaultReturnValue(@Nullable V newDefault) {
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

        public void forEach(@NotNull LongObjBiConsumer<? super V> action) {}

        public int capacity() {
            return 2;
        }

        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return true;
        }

        @NotNull
        public LongSet keySet() {
            return ImmutableLongCollections.EMPTY_SET;
        }

        @NotNull
        public CollectionExt<V> values() {
            return null;
        }

        @NotNull
        public EntrySetExt<Entry<V>> rawEntrySet() {
            return null;
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
            return EMPTY_MAP;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorage<>(newKeyTable(0), newValueTable(0));
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
            CollectionUtil.SerialCheck.ioCheckSize(sizeL, 0, HashContainer.MAXIMUM_CAPACITY);
            CollectionUtil.SerialCheck.ioGetHashTableCapacity(sizeL, DEFAULT_LOAD_FACTOR, HashContainer.MAXIMUM_CAPACITY);

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
            return null;
        }
    }

    static <V> LongObjMap<V> of() {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6, long k7, V v7) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6, long k7, V v7, long k8, V v8, long k9, V v9) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap<V> of(long k1, V v1, long k2, V v2, long k3, V v3, long k4, V v4, long k5, V v5, long k6, V v6, long k7, V v7, long k8, V v8, long k9, V v9, long k10, V v10) {
        return (LongObjMap<V>) EMPTY_MAP;
    }

    static <V> LongObjMap.Entry<V> entry(long k, V v) {
        return new SimpleEntry<>(k, v);
    }

    static <V> LongObjMap<V> copyOf(LongObjMap<? extends V> source) {
        if (source instanceof AbstractImmutableLongObjMap) {
            return (LongObjMap<V>) source;
        }
        return null;
    }

    private ImmutableLongObjMaps() {}
}
