package io.github.shiozakanaptr.acext.util.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteIterator;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteObjMap;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSpliterator;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjCollection;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjSet;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteObjBiConsumer;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteObjBiFunction;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "deprecation"})
public class ByteObjBitMap<V> implements ByteObjMap<V>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static long NULL_BIT = (long) 0;

    protected transient int size = 0;
    protected V defVal = null;
    protected transient long[] bitMark = new long[4];
    protected transient V[] valueTable;

    protected transient ByteSet keySet = null;
    protected transient CollectionExt<V> valueCollection = null;
    protected transient EntrySetExt<Entry<V>> entrySet = null;

    protected V[] newValueTable(int cap) {
        return (V[]) new Object[cap];
    }

    public ByteObjBitMap(int initialCapacity) {
        if (initialCapacity > 256 || initialCapacity < 1) throw new IllegalArgumentException("The capacity of map overflow: '" + initialCapacity + '\'');
        this.valueTable = this.newValueTable(Math.max(initialCapacity, 2));
    }

    public ByteObjBitMap() {
        this(4);
    }

    public ByteObjBitMap(final Map<? extends Byte, ? extends V> source) {
        this();
        this.putAll(source);
    }

    protected int getLimit() {
        final long[] bitL = this.bitMark;
        int result = 256;
        for (int i = 3; i > -1;) {
            final int space;
            result -= (space = Long.numberOfLeadingZeros(bitL[i--]));
            if (space != 64) break;
        }
        return result;
    }

    protected void refillValue() {
        final long[] bitL = this.bitMark;
        final V[] valueL = this.valueTable;
        final int capacity = valueL.length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) == NULL_BIT) valueL[seq] = this.defVal;
        }
    }

    protected void resizeTable(int newCap) {
        final int length;
        final V[] newValue = this.newValueTable(newCap);
        if ((length = this.valueTable.length) < newCap) Arrays.fill(newValue, length, newCap, this.defVal);
        System.arraycopy(this.valueTable, 0, newValue, 0, length);
        this.valueTable = newValue;
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        if (newCapacity > 256) throw new IllegalArgumentException("Capacity overflow: '" + newCapacity + '\'');
        final int newCap = Math.max(newCapacity, 2);
        if (newCap <= this.getLimit()) return;
        this.resizeTable(newCap);
    }

    public void reserve(int newMaxSize) {
        this.resize(newMaxSize);
    }

    protected void removeAt(final long[] bitL, int seq, int picker, long pos) {
        bitL[picker] &= ~pos;
        this.valueTable[seq] = this.defVal;
        this.size--;
    }

    protected void checkExpand(int seq) {
        if (seq >= this.valueTable.length) this.resizeTable(seq + 1);
    }

    protected V insert(final long[] bitL, int seq, int picker, long pos, final V value) {
        this.checkExpand(seq);
        bitL[picker] |= pos;
        this.size++;
        return this.valueTable[seq] = value;
    }

    protected void computeSize() {
        final long[] bitL = this.bitMark;
        this.size = Long.bitCount(bitL[0]) + Long.bitCount(bitL[1]) + Long.bitCount(bitL[2]) + Long.bitCount(bitL[3]);
    }

    public V get(byte key) {
        final int seq;
        final V[] valueL;
        return (seq = key & 0xff) < (valueL = this.valueTable).length ? valueL[seq] : this.defVal;
    }

    public V getOrDefault(byte key, V defaultValue) {
        final int seq;
        return (this.bitMark[(seq = key & 0xff) >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT ? this.valueTable[seq] : defaultValue;
    }

    public V put(byte key, V value) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) == NULL_BIT) {
            this.insert(bitL, seq, picker, pos, value);
            return this.defVal;
        }
        final V[] valueL;
        final V oldValue = (valueL = this.valueTable)[seq];
        valueL[seq] = value;
        return oldValue;
    }

    public V putIfAbsent(byte key, V value) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) == NULL_BIT) {
            this.insert(bitL, seq, picker, pos, value);
            return this.defVal;
        }
        final V[] valueL;
        if ((valueL = this.valueTable)[seq] != null) return valueL[seq];
        valueL[seq] = value;
        return null;
    }

    public V remove(byte key) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) {
            final V[] valueL;
            bitL[picker] &= ~pos;
            this.size--;
            final V oldValue = (valueL = this.valueTable)[seq];
            valueL[seq] = this.defVal;
            return oldValue;
        }
        return this.defVal;
    }

    public boolean remove(byte key, Object value) {
        final long[] bitL;
        final V[] valueL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) != NULL_BIT && Objects.equals(value, (valueL = this.valueTable)[seq])) {
            bitL[picker] &= ~pos;
            this.size--;
            valueL[seq] = this.defVal;
            return true;
        }
        return false;
    }

    public V replace(byte key, V value) {
        final int seq;
        if ((this.bitMark[(seq = key & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT) {
            final V[] valueL;
            final V oldValue = (valueL = this.valueTable)[seq];
            valueL[seq] = value;
            return oldValue;
        }
        return this.defVal;
    }

    public boolean replace(byte key, V oldValue, V newValue) {
        final V[] valueL;
        final int seq;
        if ((this.bitMark[(seq = key & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT && Objects.equals(oldValue, (valueL = this.valueTable)[seq])) {
            valueL[seq] = newValue;
            return true;
        }
        return false;
    }

    public V computeIfAbsent(byte key, ByteFunction<? extends V> mappingFunction) {
        final long[] bitL;
        final V[] valueL = this.valueTable;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        final V currValue;
        final boolean haveKey;
        if ((haveKey = ((bitL = this.bitMark)[picker] & pos) != NULL_BIT) && (currValue = valueL[seq]) != null) return currValue;

        final V newValue = mappingFunction.apply(key);
        if (newValue != null) return haveKey ? (valueL[seq] = newValue) : this.insert(bitL, seq, picker, pos, newValue);
        return this.defVal;
    }

    public V computeIfPresent(byte key, ByteObjBiFunction<? super V, ? extends V> remappingFunction) {
        final long[] bitL;
        final V[] valueL;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        if (((bitL = this.bitMark)[picker] & pos) == NULL_BIT || (valueL = this.valueTable)[seq] == null) return this.defVal;

        final V newValue = remappingFunction.apply(key, valueL[seq]);
        if (newValue != null) return valueL[seq] = newValue;
        this.removeAt(bitL, seq, picker, pos);
        return this.defVal;
    }

    public V compute(byte key, ByteObjBiFunction<? super V, ? extends V> remappingFunction) {
        final long[] bitL;
        final V[] valueL = this.valueTable;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        final boolean haveKey;
        final V newValue = remappingFunction.apply(key, (haveKey = ((bitL = this.bitMark)[picker] & pos) != NULL_BIT) ? valueL[seq] : null);
        if (newValue != null) return haveKey ? (valueL[seq] = newValue) : this.insert(bitL, seq, picker, pos, newValue);

        if (!haveKey) this.removeAt(bitL, seq, picker, pos);
        return this.defVal;
    }

    public V merge(byte key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        final long[] bitL;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        if (((bitL = this.bitMark)[picker] & pos) == NULL_BIT) return this.insert(bitL, seq, picker, pos, value);

        final V[] valueL = this.valueTable;
        final V oldValue = valueL[seq],
                newValue = oldValue != null ? remappingFunction.apply(oldValue, value) : value;
        if (newValue != null) return valueL[seq] = newValue;
        this.removeAt(bitL, seq, picker, pos);
        return this.defVal;
    }

    public V update(byte key, Function<? super V, ? extends V> updateFunction) {
        final long[] bitL;
        final V[] valueL = this.valueTable;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        final boolean haveKey;
        final V newValue = updateFunction.apply((haveKey = ((bitL = this.bitMark)[picker] & pos) != NULL_BIT) ? valueL[seq] : this.defVal);
        return haveKey ? (valueL[seq] = newValue) : this.insert(bitL, seq, picker, pos, newValue);
    }

    public V remapping(byte key, ByteObjBiFunction<? super V, ? extends Byte> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        final long[] bitL;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        if (((bitL = this.bitMark)[picker] & pos) == NULL_BIT) return this.defVal;

        final V[] valueL;
        final V currValue;
        final Byte newKey = remappingFunction.apply(key, currValue = (valueL = this.valueTable)[seq]);
        final byte rawKey = newKey != null ? newKey : (byte) 0;

        if (newKey == null || rawKey != key) this.removeAt(bitL, seq, picker, pos);
        if (newKey == null) return this.defVal;
        if (rawKey == key) return currValue;

        final int newSeq = key & 0xff, newPicker = newSeq >>> 6;
        final long newPos = 1L << (newSeq & 0b111111);
        if ((bitL[newPicker] & newPos) == NULL_BIT) return this.insert(bitL, newSeq, newPicker, newPos, currValue);

        final V newValue = mergeFunction.apply(valueL[newSeq], currValue);
        if (newValue != null) return valueL[newSeq] = newValue;
        this.removeAt(bitL, newSeq, newPicker, newPos);
        return this.defVal;
    }

    public void swap(Map<Byte, V> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ByteObjBitMap) {
            final ByteObjBitMap<V> src = (ByteObjBitMap<V>) source;
            this.clearMappingCollection();
            src.clearMappingCollection();

            final int currSize = this.size, srcSize = src.size;
            final long[] currKey = this.bitMark, srcKey = src.bitMark;
            final V[] currValue = this.valueTable, srcValue = src.valueTable;

            src.size = currSize;
            src.bitMark = currKey;
            src.valueTable = currValue;
            this.size = srcSize;
            this.bitMark = srcKey;
            this.valueTable = srcValue;
            if (this.defVal != src.defVal) {
                src.refillValue();
                this.refillValue();
            }
            return;
        }
        if (source.size() < this.size()) {
            final Map<Byte, V> tmp = new ByteObjBitMap<>(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<Byte, V> tmp = new ByteObjBitMap<>(this);
            this.clear();
            this.putAll(source);
            source.clear();
            source.putAll(tmp);
        }

    }

    public boolean swapAt(byte from, byte to) {
        final long[] bitL;
        final int seqA;
        if (((bitL = this.bitMark)[(seqA = from & 0xff) >>> 6] & (1L << (seqA & 0b111111))) == NULL_BIT) return false;
        final int seqB;
        if ((bitL[(seqB = to & 0xff) >>> 6] & (1L << (seqB & 0b111111))) == NULL_BIT) return false;

        final V[] valueL;
        final V tmpValue = (valueL = this.valueTable)[seqA];
        valueL[seqA] = valueL[seqB];
        valueL[seqB] = tmpValue;
        return true;
    }

    public boolean containsKey(byte key) {
        final int seq;
        return (this.bitMark[(seq = key & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT;
    }

    public boolean containsValue(Object value) {
        if (this.isEmpty()) return false;
        final long[] bitL = this.bitMark;
        final V[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT && Objects.equals(value, valueL[seq])) return true;
        }
        return false;
    }

    public void forEach(ByteObjBiConsumer<? super V> action) {
        if (this.isEmpty()) return;
        final long[] bitL = this.bitMark;
        final V[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept((byte) seq, valueL[seq]);
        }
    }

    public void replaceAll(ByteObjBiFunction<? super V, ? extends V> function) {
        final long[] bitL = this.bitMark;
        final V[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) valueL[seq] = function.apply((byte) seq, valueL[seq]);
        }
    }

    public void putAll(Map<? extends Byte, ? extends V> m) {
        if (m == this || m.isEmpty()) return;
        if (m instanceof ByteObjBitMap<?>) {
            final ByteObjBitMap<?> cast = (ByteObjBitMap<?>) m;
            final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;
            final V[] currValueL, srcValueL;
            final int capacity = (srcValueL = (V[]) cast.valueTable).length;
            if (capacity > (currValueL = this.valueTable).length) this.resizeTable(capacity);

            for (int seq = 0; seq < capacity; seq++) {
                final int picker;
                final long pos;
                if ((srcBitL[picker = seq >>> 6] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) {
                    if ((currBitL[picker] & pos) == NULL_BIT) {
                        currBitL[picker] |= pos;
                        this.size++;
                    }
                    currValueL[seq] = srcValueL[seq];
                }
            }
        } else m.forEach(this::put);
    }

    public V getDefaultReturnValue() {
        return this.defVal;
    }

    public V setDefaultReturnValue(V newDefault) {
        final V oldDef = this.defVal;
        this.defVal = newDefault;
        this.refillValue();
        return oldDef;
    }

    public int capacity() {
        return this.valueTable.length;
    }

    public int remaining() {
        return this.valueTable.length - this.size;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public ByteSet keySet() {
        ByteSet result;
        if ((result = this.keySet) == null) {
            result = this.keySet = new KeySet();
        }
        return result;
    }

    public CollectionExt<V> values() {
        CollectionExt<V> result;
        if ((result = this.valueCollection) == null) {
            result = this.valueCollection = new ValueCollection();
        }
        return result;
    }

    public EntrySetExt<Entry<V>> entrySetRaw() {
        EntrySetExt<Entry<V>> result;
        if ((result = this.entrySet) == null) {
            result = this.entrySet = new MapEntrySet();
        }
        return result;
    }

    public long[] keyTable() {
        return this.bitMark;
    }

    public V[] valueTable() {
        return this.valueTable;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.clearMappingCollection();
        final long[] bitL = this.bitMark;
        bitL[0] = bitL[1] = bitL[2] = bitL[3] = NULL_BIT;
        Arrays.fill(this.valueTable, this.defVal);
        this.size = 0;
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        final int newCap = Math.max(newCapacity, 2);
        if (newCap == this.valueTable.length) {
            this.clear();
        } else {
            this.size = 0;
            this.clearMappingCollection();
            final long[] bitL = this.bitMark;
            bitL[0] = bitL[1] = bitL[2] = bitL[3] = NULL_BIT;
            Arrays.fill(this.valueTable = this.newValueTable(newCap), this.defVal);
        }
    }

    public int hashCode() {
        final long[] bitL = this.bitMark;
        final V[] valueL = this.valueTable;
        final int capacity = valueL.length;
        int result = 0;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
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
                if (castMap instanceof ByteObjBitMap<?>) {
                    final ByteObjBitMap<?> cast = (ByteObjBitMap<?>) castMap;
                    final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;
                    if (!(currBitL[0] == srcBitL[0] &&
                            currBitL[1] == srcBitL[1] &&
                            currBitL[2] == srcBitL[2] &&
                            currBitL[3] == srcBitL[3])) return false;

                    final V[] currValueL = this.valueTable, srcValueL = (V[]) cast.valueTable;

                    final int capacity = srcValueL.length;
                    for (int seq = 0; seq < capacity; seq++) {
                        if ((srcBitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
                            final V currValue = currValueL[seq], srcValue = srcValueL[seq];
                            if (srcValue == null && currValue != null || srcValue != null && !srcValue.equals(currValue)) return false;
                        }
                    }
                } else {
                    final long[] currBitL = this.bitMark;
                    final V[] currValueL = this.valueTable;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final Byte srcKey = (Byte) entry.getKey();
                        final V srcValue = (V) entry.getValue();

                        final int seq = (srcKey != null ? srcKey : (byte) 0) & 0xff, picker = seq >>> 6;
                        final long pos = 1L << (seq & 0b111111);
                        if ((currBitL[picker] & pos) == NULL_BIT) return false;

                        final V currValue = currValueL[seq];
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
        if (this.isEmpty()) return "{}";

        final StringBuilder sb = new StringBuilder(this.size * 6 + 2);
        sb.append('{');
        final long[] bitL = this.bitMark;
        final V[] valueL = this.valueTable;
        final int capacity = valueL.length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
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

    protected void clearMappingCollection() {
        this.keySet = null;
        this.valueCollection = null;
        this.entrySet = null;
    }

    public Object clone() {
        ByteObjBitMap<V> result;
        try {
            result = (ByteObjBitMap<V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.valueTable.length;
        result.bitMark = new long[4];
        result.valueTable = result.newValueTable(srcLen);
        result.clearMappingCollection();
        System.arraycopy(this.bitMark, 0, result.bitMark, 0, 4);
        System.arraycopy(this.valueTable, 0, result.valueTable, 0, srcLen);
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        final long[] bitL = this.bitMark;
        out.writeLong(bitL[0]);
        out.writeLong(bitL[1]);
        out.writeLong(bitL[2]);
        out.writeLong(bitL[3]);
        if (this.isEmpty()) return;

        final V[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) out.writeObject(valueL[seq]);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.clearMappingCollection();
        final long[] bitL = this.bitMark = new long[]{in.readLong(), in.readLong(), in.readLong(), in.readLong()};
        this.computeSize();
        final int capacity;
        final V[] valueL;
        Arrays.fill(valueL = this.valueTable = this.newValueTable(capacity = this.getLimit()), this.defVal);

        if (this.isEmpty()) return;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
                valueL[seq] = (V) in.readObject();
            }
        }
    }

    protected byte[] prepareArray(final byte[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return new byte[sizeL];
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected byte[] keyToArray(final byte[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final long[] bitL = this.bitMark;
        for (int seq = 0; seq < 256; seq++) {
            final int picker = seq >>> 6;
            final long pos = 1L << (seq & 0b111111);
            if ((bitL[picker] & pos) != NULL_BIT) a[idx++] = (byte) seq;
        }
        return a;
    }

    protected class Node implements ByteObjMap.Entry<V> {
        protected int seq;

        protected Node update(int seq) {
            this.seq = seq;
            return this;
        }

        protected Node() {}

        protected Node(int seq) {
            this.seq = seq;
        }

        public byte getKeyRaw() {
            return (byte) this.seq;
        }

        public V getValue() {
            return ByteObjBitMap.this.valueTable[this.seq];
        }

        public V setValue(V newValue) {
            final int seqL = this.seq;
            final V[] valueL = ByteObjBitMap.this.valueTable;
            final V oldValue = valueL[seqL];
            valueL[seqL] = newValue;
            return oldValue;
        }

        public String toString() {
            final int seqL = this.seq;
            return (byte) seqL + "=" + ByteObjBitMap.this.valueTable[seqL];
        }

        public int hashCode() {
            final int seqL = this.seq;
            return Byte.hashCode((byte) seqL) ^ Objects.hashCode(ByteObjBitMap.this.valueTable[seqL]);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            final int seqL = this.seq;
            return Objects.equals(seqL, cast.getKey())
                    && Objects.equals(ByteObjBitMap.this.valueTable[seqL], cast.getValue());
        }
    }

    protected abstract class MapIterator<C> {
        protected int preSeq = -1;
        protected int seq = -1;
        protected int remaining;

        protected MapIterator() {
            this.remaining = ByteObjBitMap.this.size;
        }

        public boolean hasNext() {
            return this.remaining > 0;
        }

        public int nextSeq() {
            if (this.remaining < 1) throw new NoSuchElementException();
            this.remaining--;

            int seqL = this.seq + 1;
            final long[] bitL = ByteObjBitMap.this.bitMark;
            while (seqL < 256 && (bitL[seqL >>> 6] & (1L << (seqL & 0b111111))) == NULL_BIT) {
                seqL++;
            }
            return this.seq = this.preSeq = seqL;
        }

        public void remove() {
            final long[] bitL = ByteObjBitMap.this.bitMark;
            final int picker, seqL;
            final long pos;
            if ((seqL = this.preSeq) < 0 || (bitL[picker = seqL >>> 6] & (pos = 1L << (seqL & 0b111111))) == NULL_BIT) throw new IllegalStateException();
            this.preSeq = -1;
            ByteObjBitMap.this.removeAt(bitL, seqL, picker, pos);
        }

        protected abstract void consumerProxy(C action, int seq);

        public void forEachRemaining(C action) {
            final long[] bitL = ByteObjBitMap.this.bitMark;
            int findPre = this.preSeq;
            for (int seqL = this.seq + 1; seqL < 256; seqL++) {
                if ((bitL[seqL >>> 6] & ( 1L << (seqL & 0b111111))) != NULL_BIT) this.consumerProxy(action, (byte) (findPre = seqL));
            }
            this.preSeq = findPre;
            this.remaining = 0;
            this.seq = 255;
        }
    }

    protected abstract class MapSpliterator<C, S> {
        protected int seq;
        protected int limitCap;
        protected int estSize;

        protected MapSpliterator(int seq, int limitCap, int estSize) {
            this.seq = seq;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = ByteObjBitMap.this.size;
                newLimit = this.limitCap = ByteObjBitMap.this.valueTable.length;
            }
            return newLimit;
        }

        protected abstract S splitProxy(int seq, int limitCap, int estSize);

        public S trySplit() {
            final int newLimit = this.getLimitCap(), newSeq = this.seq, midSeq = (newSeq + newLimit) >>> 1;
            return newSeq < midSeq ? this.splitProxy(newSeq, this.seq = midSeq, this.estSize >>>= 1) : null;
        }

        protected abstract void consumerProxy(C action, int seq);

        public boolean tryAdvance(C action) {
            final int currLimit = this.getLimitCap();

            int seqL = this.seq;
            for (final long[] bitL = ByteObjBitMap.this.bitMark; seqL < currLimit; seqL++) {
                if ((bitL[seqL >>> 6] & (1L << (seqL & 0b111111))) != NULL_BIT) {
                    this.consumerProxy(action, this.seq = seqL + 1);
                    return true;
                }
            }
            this.seq = seqL;
            return false;
        }

        public void forEachRemaining(C action) {
            final int currLimit = this.getLimitCap();

            int seqL = this.seq;
            for (final long[] bitL = ByteObjBitMap.this.bitMark; seqL < currLimit; seqL++) {
                if ((bitL[seqL >>> 6] & (1L << (seqL & 0b111111))) != NULL_BIT) {
                    this.consumerProxy(action, seqL);
                }
            }
            this.seq = seqL;
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ByteObjBitMap.this.size ? Spliterator.SIZED : 0) | Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class KeyIterator extends MapIterator<ByteConsumer> implements ByteIterator {
        public byte nextByte() {
            return (byte) this.nextSeq();
        }

        protected void consumerProxy(ByteConsumer action, int seq) {
            action.accept((byte) seq);
        }
    }

    protected class ValueIterator extends MapIterator<Consumer<? super V>> implements Iterator<V> {
        public V next() {
            return ByteObjBitMap.this.valueTable[this.nextSeq()];
        }

        protected void consumerProxy(Consumer<? super V> action, int seq) {
            action.accept(ByteObjBitMap.this.valueTable[seq]);
        }
    }

    protected class MapEntryIterator extends MapIterator<Consumer<? super ByteObjMap.Entry<V>>> implements Iterator<ByteObjMap.Entry<V>> {
        public ByteObjMap.Entry<V> next() {
            return new Node(this.nextSeq());
        }

        protected void consumerProxy(Consumer<? super ByteObjMap.Entry<V>> action, int seq) {
            action.accept(new Node(seq));
        }
    }

    protected class MapEntryViewIterator extends MapIterator<Consumer<? super ByteObjMap.Entry<V>>> implements Iterator<ByteObjMap.Entry<V>> {
        protected final Node tmpEntry = new Node();

        public ByteObjMap.Entry<V> next() {
            return this.tmpEntry.update(this.nextSeq());
        }

        protected void consumerProxy(Consumer<? super ByteObjMap.Entry<V>> action, int seq) {
            action.accept(this.tmpEntry.update(this.nextSeq()));
        }
    }

    protected class KeySpliterator extends MapSpliterator<ByteConsumer, ByteSpliterator> implements ByteSpliterator {
        protected KeySpliterator(int seq, int limitCap, int estSize) {
            super(seq, limitCap, estSize);
        }

        protected ByteSpliterator splitProxy(int seq, int limitCap, int estSize) {
            return new KeySpliterator(seq, limitCap, estSize);
        }

        protected void consumerProxy(ByteConsumer action, int seq) {
            action.accept((byte) seq);
        }
    }

    protected class ValueSpliterator extends MapSpliterator<Consumer<? super V>, Spliterator<V>> implements Spliterator<V> {
        protected ValueSpliterator(int seq, int limitCap, int estSize) {
            super(seq, limitCap, estSize);
        }

        protected Spliterator<V> splitProxy(int seq, int limitCap, int estSize) {
            return new ValueSpliterator(seq, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super V> action, int seq) {
            action.accept(ByteObjBitMap.this.valueTable[seq]);
        }

        public int characteristics() {
            return this.limitCap < 0 || this.estSize == ByteObjBitMap.this.size ? Spliterator.SIZED : 0;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super ByteObjMap.Entry<V>>, Spliterator<ByteObjMap.Entry<V>>> implements Spliterator<ByteObjMap.Entry<V>> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ByteObjMap.Entry<V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ByteObjMap.Entry<V>> action, int seq) {
            action.accept(new Node(seq));
        }
    }

    protected class MapEntryViewSpliterator extends MapSpliterator<Consumer<? super ByteObjMap.Entry<V>>, Spliterator<ByteObjMap.Entry<V>>> implements Spliterator<ByteObjMap.Entry<V>> {
        protected final Node tmpEntry = new Node();

        protected MapEntryViewSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ByteObjMap.Entry<V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntryViewSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ByteObjMap.Entry<V>> action, int seq) {
            action.accept(this.tmpEntry.update(seq));
        }
    }

    protected class KeySet extends AbstractByteSet {
        public void resize(int newCapacity) {
            ByteObjBitMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ByteObjBitMap.this.reserve(newMaxSize);
        }

        public boolean contains(byte o) {
            return ByteObjBitMap.this.containsKey(o);
        }

        public boolean removeElement(byte o) {
            final long[] bitL = ByteObjBitMap.this.bitMark;
            final int seq, picker;
            final long pos;
            if ((bitL[picker = (seq = o & 0xff) >>> 6] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) {
                bitL[picker] &= ~pos;
                ByteObjBitMap.this.removeAt(bitL, seq, picker, pos);
                return true;
            }
            return false;
        }

        public void forEach(ByteConsumer action) {
            if (ByteObjBitMap.this.isEmpty()) return;
            final long[] bitL = ByteObjBitMap.this.bitMark;
            final int capacity = ByteObjBitMap.this.valueTable.length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept((byte) seq);
            }
        }

        public int capacity() {
            return ByteObjBitMap.this.capacity();
        }

        public int remaining() {
            return ByteObjBitMap.this.remaining();
        }

        public int size() {
            return ByteObjBitMap.this.size;
        }

        public ByteIterator iterator() {
            return new KeyIterator();
        }

        public ByteSpliterator spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            ByteObjBitMap.this.clear();
        }

        public void clear(int newCapacity) {
            ByteObjBitMap.this.clear(newCapacity);
        }

        public byte[] toArrayRaw() {
            return ByteObjBitMap.this.keyToArray(new byte[ByteObjBitMap.this.size]);
        }

        public byte[] toArray(byte[] a) {
            return ByteObjBitMap.this.keyToArray(ByteObjBitMap.this.prepareArray(a));
        }
    }

    protected class ValueCollection extends AbstractObjCollection<V> {
        public void resize(int newCapacity) {
            ByteObjBitMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ByteObjBitMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ByteObjBitMap.this.containsValue(o);
        }

        public void forEach(Consumer<? super V> action) {
            if (ByteObjBitMap.this.isEmpty()) return;
            final long[] bitL = ByteObjBitMap.this.bitMark;
            final V[] valueL = ByteObjBitMap.this.valueTable;
            final int capacity = valueL.length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept(valueL[seq]);
            }
        }

        public int capacity() {
            return ByteObjBitMap.this.capacity();
        }

        public int remaining() {
            return ByteObjBitMap.this.remaining();
        }

        public int size() {
            return ByteObjBitMap.this.size;
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            ByteObjBitMap.this.clear();
        }

        public void clear(int newCapacity) {
            ByteObjBitMap.this.clear(newCapacity);
        }
    }

    protected class MapEntrySet extends AbstractObjSet<ByteObjMap.Entry<V>> implements EntrySetExt<Entry<V>> {
        protected final Node tmpEntry = new Node();

        public void resize(int newCapacity) {
            ByteObjBitMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ByteObjBitMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ByteObjBitMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ByteObjBitMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        protected void forEachProxy(Consumer<? super ByteObjMap.Entry<V>> action, IntFunction<ByteObjMap.Entry<V>> supplier) {
            if (ByteObjBitMap.this.isEmpty()) return;
            final long[] bitL = ByteObjBitMap.this.bitMark;
            final V[] valueL = ByteObjBitMap.this.valueTable;
            final int capacity = valueL.length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept(supplier.apply(seq));
            }
        }

        public void forEach(Consumer<? super ByteObjMap.Entry<V>> action) {
            this.forEachProxy(action, Node::new);
        }

        public void viewForEach(Consumer<? super ByteObjMap.Entry<V>> action) {
            this.forEachProxy(action, this.tmpEntry::update);
        }

        public int capacity() {
            return ByteObjBitMap.this.capacity();
        }

        public int remaining() {
            return ByteObjBitMap.this.remaining();
        }

        public int size() {
            return ByteObjBitMap.this.size;
        }

        public Iterator<ByteObjMap.Entry<V>> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<ByteObjMap.Entry<V>> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<ByteObjMap.Entry<V>> viewIterator() {
            return new MapEntryViewIterator();
        }

        public Spliterator<ByteObjMap.Entry<V>> viewSpliterator() {
            return new MapEntryViewSpliterator(0, -1, 0);
        }

        public void clear() {
            ByteObjBitMap.this.clear();
        }

        public void clear(int newCapacity) {
            ByteObjBitMap.this.clear(newCapacity);
        }
    }
}
