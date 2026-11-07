package io.github.shiozakanaptr.acext.util.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteIterator;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteLongMap;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSet;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSpliterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSpliterator;
import io.github.shiozakanaptr.acext.util.collection.longtype.AbstractLongCollection;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjSet;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.LongLongBiFunction;
import io.github.shiozakanaptr.acext.util.function.longtype.LongUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "deprecation"})
public class ByteLongBitMap implements ByteLongMap, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static long NULL_BIT = (long) 0;

    protected transient int size = 0;
    protected long defVal = 0;
    protected transient long[] bitMark = new long[4];
    protected transient long[] valueTable;

    protected transient ByteSet keySet = null;
    protected transient LongCollection valueCollection = null;
    protected transient EntrySetExt<Entry> entrySet = null;

    protected long[] newValueTable(int cap) {
        return new long[cap];
    }

    public ByteLongBitMap(int initialCapacity) {
        if (initialCapacity > 256 || initialCapacity < 1) throw new IllegalArgumentException("The capacity of map overflow: '" + initialCapacity + '\'');
        this.valueTable = this.newValueTable(Math.max(initialCapacity, 2));
    }

    public ByteLongBitMap() {
        this(4);
    }

    public ByteLongBitMap(final Map<? extends Byte, ? extends Long> source) {
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
        final long[] valueL = this.valueTable;
        final int capacity = valueL.length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) == NULL_BIT) valueL[seq] = this.defVal;
        }
    }

    protected void resizeTable(int newCap) {
        final int length;
        final long[] newValue = this.newValueTable(newCap);
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

    protected long insert(final long[] bitL, int seq, int picker, long pos, final long value) {
        this.checkExpand(seq);
        bitL[picker] |= pos;
        this.size++;
        return this.valueTable[seq] = value;
    }

    protected void computeSize() {
        final long[] bitL = this.bitMark;
        this.size = Long.bitCount(bitL[0]) + Long.bitCount(bitL[1]) + Long.bitCount(bitL[2]) + Long.bitCount(bitL[3]);
    }

    public long get(byte key) {
        final int seq;
        final long[] valueL;
        return (seq = key & 0xff) < (valueL = this.valueTable).length ? valueL[seq] : this.defVal;
    }

    public long getOrDefault(byte key, long defaultValue) {
        final int seq;
        return (this.bitMark[(seq = key & 0xff) >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT ? this.valueTable[seq] : defaultValue;
    }

    public long put(byte key, long value) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) == NULL_BIT) {
            this.insert(bitL, seq, picker, pos, value);
            return this.defVal;
        }
        final long[] valueL;
        final long oldValue = (valueL = this.valueTable)[seq];
        valueL[seq] = value;
        return oldValue;
    }

    public long putIfAbsent(byte key, long value) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) == NULL_BIT) {
            this.insert(bitL, seq, picker, pos, value);
            return this.defVal;
        }
        final long[] valueL;
        if ((valueL = this.valueTable)[seq] != NULL_BIT) return valueL[seq];
        valueL[seq] = value;
        return 0;
    }

    public long remove(byte key) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) {
            final long[] valueL;
            bitL[picker] &= ~pos;
            this.size--;
            final long oldValue = (valueL = this.valueTable)[seq];
            valueL[seq] = this.defVal;
            return oldValue;
        }
        return this.defVal;
    }

    public boolean remove(byte key, long value) {
        final long[] bitL;
        final long[] valueL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[(picker = (seq = key & 0xff) >>> 6)] & (pos = 1L << (seq & 0b111111))) != NULL_BIT && value == (valueL = this.valueTable)[seq]) {
            bitL[picker] &= ~pos;
            this.size--;
            valueL[seq] = this.defVal;
            return true;
        }
        return false;
    }

    public long replace(byte key, long value) {
        final int seq;
        if ((this.bitMark[(seq = key & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT) {
            final long[] valueL;
            final long oldValue = (valueL = this.valueTable)[seq];
            valueL[seq] = value;
            return oldValue;
        }
        return this.defVal;
    }

    public boolean replace(byte key, long oldValue, long newValue) {
        final long[] valueL;
        final int seq;
        if ((this.bitMark[(seq = key & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT && oldValue == (valueL = this.valueTable)[seq]) {
            valueL[seq] = newValue;
            return true;
        }
        return false;
    }

    public long computeIfAbsent(byte key, ByteFunction<? extends Long> mappingFunction) {
        final long[] bitL;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        if (((bitL = this.bitMark)[picker] & pos) != NULL_BIT) return this.valueTable[seq];

        final Long newValue = mappingFunction.apply(key);
        if (newValue != null) return this.insert(bitL, seq, picker, pos, newValue);
        return this.defVal;
    }

    public long computeIfPresent(byte key, ByteLongBiFunction<? extends Long> remappingFunction) {
        final long[] bitL;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        if (((bitL = this.bitMark)[picker] & pos) == NULL_BIT) return this.defVal;

        final long[] valueL;
        final Long newValue = remappingFunction.apply(key, (valueL = this.valueTable)[seq]);
        if (newValue != null) return valueL[seq] = newValue;
        this.removeAt(bitL, seq, picker, pos);
        return this.defVal;
    }

    public long compute(byte key, ByteLongBiFunction<? extends Long> remappingFunction) {
        final long[] bitL;
        final long[] valueL = this.valueTable;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        final boolean haveKey;
        final Long newValue = remappingFunction.apply(key, (haveKey = ((bitL = this.bitMark)[picker] & pos) != NULL_BIT) ? valueL[seq] : 0);
        if (newValue != null) return haveKey ? (valueL[seq] = newValue) : this.insert(bitL, seq, picker, pos, newValue);

        if (!haveKey) this.removeAt(bitL, seq, picker, pos);
        return this.defVal;
    }

    public long merge(byte key, long value, LongLongBiFunction<? extends Long> remappingFunction) {
        final long[] bitL;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        if (((bitL = this.bitMark)[picker] & pos) == NULL_BIT) return this.insert(bitL, seq, picker, pos, value);

        final long[] valueL;
        final Long newValue = remappingFunction.apply((valueL = this.valueTable)[seq], value);
        if (newValue != null) return valueL[seq] = newValue;
        this.removeAt(bitL, seq, picker, pos);
        return this.defVal;
    }

    public long update(byte key, LongUnaryOperator updateFunction) {
        final long[] bitL;
        final long[] valueL = this.valueTable;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        final boolean haveKey;
        final long newValue = updateFunction.apply((haveKey = ((bitL = this.bitMark)[picker] & pos) != NULL_BIT) ? valueL[seq] : this.defVal);
        return haveKey ? (valueL[seq] = newValue) : this.insert(bitL, seq, picker, pos, newValue);
    }

    public long remapping(byte key, ByteLongBiFunction<? extends Byte> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction) {
        final long[] bitL;
        final int seq = key & 0xff, picker = seq >>> 6;
        final long pos = 1L << (seq & 0b111111);
        if (((bitL = this.bitMark)[picker] & pos) == NULL_BIT) return this.defVal;

        final long[] valueL;
        final long currValue;
        final Byte newKey = remappingFunction.apply(key, currValue = (valueL = this.valueTable)[seq]);
        final byte rawKey = newKey != null ? newKey : (byte) 0;

        if (newKey == null || rawKey != key) this.removeAt(bitL, seq, picker, pos);
        if (newKey == null) return this.defVal;
        if (rawKey == key) return currValue;

        final int newSeq = key & 0xff, newPicker = newSeq >>> 6;
        final long newPos = 1L << (newSeq & 0b111111);
        if ((bitL[newPicker] & newPos) == NULL_BIT) return this.insert(bitL, newSeq, newPicker, newPos, currValue);

        final Long newValue = mergeFunction.apply(valueL[newSeq], currValue);
        if (newValue != null) return valueL[newSeq] = newValue;
        this.removeAt(bitL, newSeq, newPicker, newPos);
        return this.defVal;
    }

    public void swap(Map<Byte, Long> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ByteLongBitMap) {
            final ByteLongBitMap src = (ByteLongBitMap) source;
            this.clearMappingCollection();
            src.clearMappingCollection();

            final int currSize = this.size, srcSize = src.size;
            final long[] currKey = this.bitMark, srcKey = src.bitMark;
            final long[] currValue = this.valueTable, srcValue = src.valueTable;

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
            final Map<Byte, Long> tmp = new ByteLongBitMap(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<Byte, Long> tmp = new ByteLongBitMap(this);
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

        final long[] valueL;
        final long tmpValue = (valueL = this.valueTable)[seqA];
        valueL[seqA] = valueL[seqB];
        valueL[seqB] = tmpValue;
        return true;
    }

    public boolean containsKey(byte key) {
        final int seq;
        return (this.bitMark[(seq = key & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT;
    }

    public boolean containsValue(long value) {
        if (this.isEmpty()) return false;
        final long[] bitL = this.bitMark;
        final long[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT && value == valueL[seq]) return true;
        }
        return false;
    }

    public void forEach(ByteLongBiConsumer action) {
        if (this.isEmpty()) return;
        final long[] bitL = this.bitMark;
        final long[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept((byte) seq, valueL[seq]);
        }
    }

    public void replaceAll(ByteLongBiOperator function) {
        final long[] bitL = this.bitMark;
        final long[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) valueL[seq] = function.apply((byte) seq, valueL[seq]);
        }
    }

    public void putAll(Map<? extends Byte, ? extends Long> m) {
        if (m == this || m.isEmpty()) return;
        if (m instanceof ByteLongBitMap) {
            final ByteLongBitMap cast = (ByteLongBitMap) m;
            final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;
            final long[] currValueL, srcValueL;
            final int capacity = (srcValueL = cast.valueTable).length;
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

    public long getDefaultReturnValueRaw() {
        return this.defVal;
    }

    public long setDefaultReturnValue(long newDefault) {
        final long oldDef = this.defVal;
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

    public LongCollection values() {
        LongCollection result;
        if ((result = this.valueCollection) == null) {
            result = this.valueCollection = new ValueCollection();
        }
        return result;
    }

    public EntrySetExt<Entry> entrySetRaw() {
        EntrySetExt<Entry> result;
        if ((result = this.entrySet) == null) {
            result = this.entrySet = new MapEntrySet();
        }
        return result;
    }

    public long[] keyTable() {
        return this.bitMark;
    }

    public long[] valueTable() {
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
        final long[] valueL = this.valueTable;
        final int capacity = valueL.length;
        int result = 0;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
                result += Byte.hashCode((byte) seq) ^ Long.hashCode(valueL[seq]);
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
                if (castMap instanceof ByteLongBitMap) {
                    final ByteLongBitMap cast = (ByteLongBitMap) castMap;
                    final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;
                    if (!(currBitL[0] == srcBitL[0] &&
                            currBitL[1] == srcBitL[1] &&
                            currBitL[2] == srcBitL[2] &&
                            currBitL[3] == srcBitL[3])) return false;

                    final long[] currValueL = this.valueTable, srcValueL = cast.valueTable;

                    final int capacity = srcValueL.length;
                    for (int seq = 0; seq < capacity; seq++) {
                        if ((srcBitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
                            if (currValueL[seq] != srcValueL[seq]) return false;
                        }
                    }
                } else {
                    final long[] currBitL = this.bitMark;
                    final long[] currValueL = this.valueTable;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final Byte srcKey = (Byte) entry.getKey();
                        final Long srcValue = (Long) entry.getValue();

                        final int seq = (srcKey != null ? srcKey : (byte) 0) & 0xff, picker = seq >>> 6;
                        final long pos = 1L << (seq & 0b111111);
                        if ((currBitL[picker] & pos) == NULL_BIT) return false;

                        final long currValue = currValueL[seq];
                        if (srcValue == null && currValue != 0 || srcValue != null && srcValue != currValue) return false;
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
        final long[] valueL = this.valueTable;
        final int capacity = valueL.length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append((byte) seq)
                        .append('=')
                        .append(valueL[seq]);
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
        ByteLongBitMap result;
        try {
            result = (ByteLongBitMap) super.clone();
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

        final long[] valueL;
        final int capacity = (valueL = this.valueTable).length;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) out.writeLong(valueL[seq]);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.clearMappingCollection();
        final long[] bitL = this.bitMark = new long[]{in.readLong(), in.readLong(), in.readLong(), in.readLong()};
        this.computeSize();
        final int capacity;
        final long[] valueL;
        Arrays.fill(valueL = this.valueTable = this.newValueTable(capacity = this.getLimit()), this.defVal);

        if (this.isEmpty()) return;
        for (int seq = 0; seq < capacity; seq++) {
            if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
                valueL[seq] = in.readLong();
            }
        }
    }

    protected byte[] prepareArray(final byte[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return new byte[sizeL];
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected long[] prepareArray(final long[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return this.newValueTable(sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected byte[] keyToArray(final byte[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final long[] bitL = this.bitMark;
        final long[] valueL = this.valueTable;
        final int capacity = valueL.length;
        for (int seq = 0; seq < capacity; seq++) {
            final int picker = seq >>> 6;
            final long pos = 1L << (seq & 0b111111);
            if ((bitL[picker] & pos) != NULL_BIT) a[idx++] = (byte) seq;
        }
        return a;
    }

    protected long[] valueToArray(final long[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final long[] bitL = this.bitMark;
        final long[] valueL = this.valueTable;
        final int capacity = valueL.length;
        for (int seq = 0; seq < capacity; seq++) {
            final int picker = seq >>> 6;
            final long pos = 1L << (seq & 0b111111);
            if ((bitL[picker] & pos) != NULL_BIT) a[idx++] = valueL[seq];
        }
        return a;
    }

    protected class Node implements ByteLongMap.Entry {
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

        public long getValueRaw() {
            return ByteLongBitMap.this.valueTable[this.seq];
        }

        public long setValue(long newValue) {
            final int seqL = this.seq;
            final long[] valueL = ByteLongBitMap.this.valueTable;
            final long oldValue = valueL[seqL];
            valueL[seqL] = newValue;
            return oldValue;
        }

        public String toString() {
            final int seqL = this.seq;
            return (byte) seqL + "=" + ByteLongBitMap.this.valueTable[seqL];
        }

        public int hashCode() {
            final int seqL = this.seq;
            return Byte.hashCode((byte) seqL) ^ Long.hashCode(ByteLongBitMap.this.valueTable[seqL]);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            final int seqL = this.seq;
            return Objects.equals(seqL, cast.getKey())
                    && Objects.equals(ByteLongBitMap.this.valueTable[seqL], cast.getValue());
        }
    }

    protected abstract class MapIterator<C> {
        protected int preSeq = -1;
        protected int seq = -1;
        protected int remaining;

        protected MapIterator() {
            this.remaining = ByteLongBitMap.this.size;
        }

        public boolean hasNext() {
            return this.remaining > 0;
        }

        public int nextSeq() {
            if (this.remaining < 1) throw new NoSuchElementException();
            this.remaining--;

            int seqL = this.seq + 1;
            final long[] bitL = ByteLongBitMap.this.bitMark;
            while (seqL < 256 && (bitL[seqL >>> 6] & (1L << (seqL & 0b111111))) == NULL_BIT) {
                seqL++;
            }
            return this.seq = this.preSeq = seqL;
        }

        public void remove() {
            final long[] bitL = ByteLongBitMap.this.bitMark;
            final int picker, seqL;
            final long pos;
            if ((seqL = this.preSeq) < 0 || (bitL[picker = seqL >>> 6] & (pos = 1L << (seqL & 0b111111))) == NULL_BIT) throw new IllegalStateException();
            this.preSeq = -1;
            ByteLongBitMap.this.removeAt(bitL, seqL, picker, pos);
        }

        protected abstract void consumerProxy(C action, int seq);

        public void forEachRemaining(C action) {
            final long[] bitL = ByteLongBitMap.this.bitMark;
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
                this.estSize = ByteLongBitMap.this.size;
                newLimit = this.limitCap = ByteLongBitMap.this.valueTable.length;
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
            for (final long[] bitL = ByteLongBitMap.this.bitMark; seqL < currLimit; seqL++) {
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
            for (final long[] bitL = ByteLongBitMap.this.bitMark; seqL < currLimit; seqL++) {
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
            return (this.limitCap < 0 || this.estSize == ByteLongBitMap.this.size ? Spliterator.SIZED : 0) | Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL;
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

    protected class ValueIterator extends MapIterator<LongConsumer> implements LongIterator {
        public long nextLong() {
            return ByteLongBitMap.this.valueTable[this.nextSeq()];
        }

        protected void consumerProxy(LongConsumer action, int seq) {
            action.accept(ByteLongBitMap.this.valueTable[seq]);
        }
    }

    protected class MapEntryIterator extends MapIterator<Consumer<? super ByteLongMap.Entry>> implements Iterator<ByteLongMap.Entry> {
        public ByteLongMap.Entry next() {
            return new Node(this.nextSeq());
        }

        protected void consumerProxy(Consumer<? super Entry> action, int seq) {
            action.accept(new Node(seq));
        }
    }

    protected class MapEntryViewIterator extends MapIterator<Consumer<? super ByteLongMap.Entry>> implements Iterator<ByteLongMap.Entry> {
        protected final Node tmpEntry = new Node();

        public ByteLongMap.Entry next() {
            return this.tmpEntry.update(this.nextSeq());
        }

        protected void consumerProxy(Consumer<? super ByteLongMap.Entry> action, int seq) {
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

    protected class ValueSpliterator extends MapSpliterator<LongConsumer, LongSpliterator> implements LongSpliterator {
        protected ValueSpliterator(int seq, int limitCap, int estSize) {
            super(seq, limitCap, estSize);
        }

        protected LongSpliterator splitProxy(int seq, int limitCap, int estSize) {
            return new ValueSpliterator(seq, limitCap, estSize);
        }

        protected void consumerProxy(LongConsumer action, int seq) {
            action.accept(ByteLongBitMap.this.valueTable[seq]);
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ByteLongBitMap.this.size ? Spliterator.SIZED : 0) | Spliterator.NONNULL;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super ByteLongMap.Entry>, Spliterator<ByteLongMap.Entry>> implements Spliterator<ByteLongMap.Entry> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ByteLongMap.Entry> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super Entry> action, int seq) {
            action.accept(new Node(seq));
        }
    }

    protected class MapEntryViewSpliterator extends MapSpliterator<Consumer<? super ByteLongMap.Entry>, Spliterator<ByteLongMap.Entry>> implements Spliterator<ByteLongMap.Entry> {
        protected final Node tmpEntry = new Node();

        protected MapEntryViewSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ByteLongMap.Entry> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntryViewSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ByteLongMap.Entry> action, int seq) {
            action.accept(this.tmpEntry.update(seq));
        }
    }

    protected class KeySet extends AbstractByteSet {
        public void resize(int newCapacity) {
            ByteLongBitMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ByteLongBitMap.this.reserve(newMaxSize);
        }

        public boolean contains(byte o) {
            return ByteLongBitMap.this.containsKey(o);
        }

        public boolean removeElement(byte o) {
            final long[] bitL = ByteLongBitMap.this.bitMark;
            final int seq, picker;
            final long pos;
            if ((bitL[picker = (seq = o & 0xff) >>> 6] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) {
                bitL[picker] &= ~pos;
                ByteLongBitMap.this.removeAt(bitL, seq, picker, pos);
                return true;
            }
            return false;
        }

        public void forEach(ByteConsumer action) {
            if (ByteLongBitMap.this.isEmpty()) return;
            final long[] bitL = ByteLongBitMap.this.bitMark;
            final int capacity = ByteLongBitMap.this.valueTable.length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept((byte) seq);
            }
        }

        public int capacity() {
            return ByteLongBitMap.this.capacity();
        }

        public int remaining() {
            return ByteLongBitMap.this.remaining();
        }

        public int size() {
            return ByteLongBitMap.this.size;
        }

        public ByteIterator iterator() {
            return new KeyIterator();
        }

        public ByteSpliterator spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            ByteLongBitMap.this.clear();
        }

        public void clear(int newCapacity) {
            ByteLongBitMap.this.clear(newCapacity);
        }

        public byte[] toArrayRaw() {
            return ByteLongBitMap.this.keyToArray(new byte[ByteLongBitMap.this.size]);
        }

        public byte[] toArray(byte[] a) {
            return ByteLongBitMap.this.keyToArray(ByteLongBitMap.this.prepareArray(a));
        }
    }

    protected class ValueCollection extends AbstractLongCollection {
        public void resize(int newCapacity) {
            ByteLongBitMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ByteLongBitMap.this.reserve(newMaxSize);
        }

        public boolean contains(long o) {
            return ByteLongBitMap.this.containsValue(o);
        }

        public void forEach(LongConsumer action) {
            if (ByteLongBitMap.this.isEmpty()) return;
            final long[] bitL = ByteLongBitMap.this.bitMark;
            final long[] valueL = ByteLongBitMap.this.valueTable;
            final int capacity = valueL.length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept(valueL[seq]);
            }
        }

        public int capacity() {
            return ByteLongBitMap.this.capacity();
        }

        public int remaining() {
            return ByteLongBitMap.this.remaining();
        }

        public int size() {
            return ByteLongBitMap.this.size;
        }

        public LongIterator iterator() {
            return new ValueIterator();
        }

        public LongSpliterator spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            ByteLongBitMap.this.clear();
        }

        public void clear(int newCapacity) {
            ByteLongBitMap.this.clear(newCapacity);
        }

        @NotNull
        public long[] toArrayRaw() {
            return ByteLongBitMap.this.valueToArray(ByteLongBitMap.this.newValueTable(ByteLongBitMap.this.size));
        }

        @NotNull
        public long[] toArray(@NotNull long[] a) {
            return ByteLongBitMap.this.valueToArray(ByteLongBitMap.this.prepareArray(a));
        }
    }

    protected class MapEntrySet extends AbstractObjSet<ByteLongMap.Entry> implements EntrySetExt<Entry> {
        protected final Node tmpEntry = new Node();

        public void resize(int newCapacity) {
            ByteLongBitMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ByteLongBitMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ByteLongBitMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ByteLongBitMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        protected void forEachProxy(Consumer<? super ByteLongMap.Entry> action, IntFunction<ByteLongMap.Entry> supplier) {
            if (ByteLongBitMap.this.isEmpty()) return;
            final long[] bitL = ByteLongBitMap.this.bitMark;
            final long[] valueL = ByteLongBitMap.this.valueTable;
            final int capacity = valueL.length;
            for (int seq = 0; seq < capacity; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) action.accept(supplier.apply(seq));
            }
        }

        public void forEach(Consumer<? super ByteLongMap.Entry> action) {
            this.forEachProxy(action, Node::new);
        }

        public void viewForEach(Consumer<? super ByteLongMap.Entry> action) {
            this.forEachProxy(action, this.tmpEntry::update);
        }

        public int capacity() {
            return ByteLongBitMap.this.capacity();
        }

        public int remaining() {
            return ByteLongBitMap.this.remaining();
        }

        public int size() {
            return ByteLongBitMap.this.size;
        }

        public Iterator<ByteLongMap.Entry> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<ByteLongMap.Entry> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<ByteLongMap.Entry> viewIterator() {
            return new MapEntryViewIterator();
        }

        public Spliterator<ByteLongMap.Entry> viewSpliterator() {
            return new MapEntryViewSpliterator(0, -1, 0);
        }

        public void clear() {
            ByteLongBitMap.this.clear();
        }

        public void clear(int newCapacity) {
            ByteLongBitMap.this.clear(newCapacity);
        }
    }
}
