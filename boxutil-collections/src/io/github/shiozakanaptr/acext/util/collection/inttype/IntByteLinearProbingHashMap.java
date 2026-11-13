package io.github.shiozakanaptr.acext.util.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteIterator;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSpliterator;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntByteMap;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntIterator;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;
import io.github.shiozakanaptr.acext.util.collection.bytetype.AbstractByteCollection;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjSet;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteByteBiFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteUnaryOperator;
import io.github.shiozakanaptr.acext.util.function.inttype.IntByteBiConsumer;
import io.github.shiozakanaptr.acext.util.function.inttype.IntByteBiFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntByteBiOperator;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "deprecation", "RedundantCast"})
public class IntByteLinearProbingHashMap implements IntByteMap, HashContainer<Integer>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static int NULL_SLOT = (int) 0;

    protected transient boolean withNullKey = false;
    protected transient int remaining = 0;
    protected transient int capacity;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected byte defVal = 0;
    protected final HashBehavior<Integer> hashBehavior;
    protected transient int[] keyTable;
    protected transient byte[] valueTable;

    protected transient IntSet keySet = null;
    protected transient ByteCollection valueCollection = null;
    protected transient EntrySetExt<IntByteMap.Entry> entrySet = null;

    protected int[] newKeyTable(int cap) {
        return new int[cap];
    }

    protected byte[] newValueTable(int cap) {
        return new byte[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntByteLinearProbingHashMap(int initialCapacity, float loadFactor, final HashBehavior<Integer> hashBehavior) {
        this.capacity = InternalCollectionUtil.checkHashCapacity(InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2)));
        this.posMask = this.capacity - 1;
        this.loadFactor = InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 0.9f);
        this.hashBehavior = Objects.requireNonNull(hashBehavior);

        final int fullCap = this.capacity + 1;
        this.keyTable = this.newKeyTable(fullCap);
        this.valueTable = this.newValueTable(fullCap);
        this.calculateRemaining();
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntByteLinearProbingHashMap(int initialCapacity, HashBehavior<Integer> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public IntByteLinearProbingHashMap(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public IntByteLinearProbingHashMap() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntByteLinearProbingHashMap(final Map<? extends Integer, ? extends Byte> source, float loadFactor, final HashBehavior<Integer> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.putAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntByteLinearProbingHashMap(final Map<? extends Integer, ? extends Byte> source, final HashBehavior<Integer> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public IntByteLinearProbingHashMap(final Map<? extends Integer, ? extends Byte> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor),
                newTargetCap = InternalCollectionUtil.checkHashCapacity(InternalCollectionUtil.getCapacityPOT(newCapacity)),
                newCap = Math.max(minSize, Math.max(newTargetCap, 2));
        if (newCap == this.capacity) return;

        final int newMask = newCap - 1, fullCap = newCap + 1, oldCap;
        final int[] newKey = this.newKeyTable(fullCap), oldKey = this.keyTable;
        final byte[] newValue = this.newValueTable(fullCap), oldValue = this.valueTable;

        if (this.size > 0) {
            oldCap = this.capacity;
            for (int i = 0; i < oldCap; i++) {
                final int currKey;
                if ((currKey = oldKey[i]) != ((int) NULL_SLOT)) {
                    int pos = this.hashBehavior.hashing(currKey) & newMask;
                    while (newKey[pos] != ((int) NULL_SLOT)) {
                        pos = (pos + 1) & newMask;
                    }
                    newKey[pos] = currKey;
                    newValue[pos] = oldValue[i];
                }
            }
            if (this.withNullKey) {
                newKey[newCap] = oldKey[oldCap];
                newValue[newCap] = oldValue[oldCap];
            }
        }

        this.capacity = newCap;
        this.posMask = newMask;
        this.keyTable = newKey;
        this.valueTable = newValue;
        this.calculateRemaining();
    }

    public void reserve(int newMaxSize) {
        if (newMaxSize < 1) throw new IllegalArgumentException("Not a valid size: '" + newMaxSize + '\'');
        this.resize(InternalCollectionUtil.getHashTableCapacity(newMaxSize, this.loadFactor));
    }

    protected byte removeNull(final int[] keyL) {
        final int capL = this.capacity;
        final byte[] valueL = this.valueTable;
        final byte result = valueL[capL];
        this.withNullKey = false;
        keyL[capL] = ((int) NULL_SLOT);
        this.size--;
        this.remaining++;
        return result;
    }

    protected byte removeAndShift(final int[] keyL, int currPos) {
        final byte[] valueL = this.valueTable;
        final byte result = valueL[currPos];
        keyL[currPos] = ((int) NULL_SLOT);
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        int currKey;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currKey = keyL[nextPos = (nextPos + 1) & maskL]) != ((int) NULL_SLOT); dist++) {
            if (((nextPos - (this.hashBehavior.hashing(currKey) & maskL)) & maskL) >= dist) {
                keyL[holePos] = currKey;
                keyL[nextPos] = ((int) NULL_SLOT);
                valueL[holePos] = valueL[nextPos];
                holePos = nextPos;
                dist = 0;
            }
        }
        return result;
    }

    protected void removeAt(final int[] keyL, int pos) {
        if (pos != this.capacity) this.removeAndShift(keyL, pos); else this.removeNull(keyL);
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.capacity));
    }

    protected int find(final int key) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(key, ((int) NULL_SLOT))) {
            return this.withNullKey ? this.capacity : ~this.capacity;
        }

        final int maskL = this.posMask;
        final int[] keyL = this.keyTable;
        int pos = behavior.hashing(key);
        for (int currKey; (currKey = keyL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                return pos;
            }
        }
        return ~pos;
    }

    protected byte insert(int pos, final int key, final byte value) {
        final int realPos = ~pos;
        if (realPos == this.capacity) this.withNullKey = true;
        this.keyTable[realPos] = key;
        this.valueTable[realPos] = value;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return value;
    }

    public byte get(int key) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(key, ((int) NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : this.defVal;

        final int maskL = this.posMask;
        final int[] keyL = this.keyTable;
        int currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.valueTable[pos];
        }
        return this.defVal;
    }

    public byte getOrDefault(int key, byte defaultValue) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(key, ((int) NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : defaultValue;

        final int maskL = this.posMask;
        final int[] keyL = this.keyTable;
        int currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.valueTable[pos];
        }
        return defaultValue;
    }

    public byte put(int key, byte value) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(key, ((int) NULL_SLOT))) {
            final byte[] valueL = this.valueTable;
            final byte result;
            if (this.withNullKey) {
                result = valueL[this.capacity];
            } else {
                result = this.defVal;
                this.withNullKey = true;
                this.keyTable[this.capacity] = key;
                this.size++;
                this.remaining--;
                this.checkExpand();
            }
            valueL[this.capacity] = value;
            return result;
        }

        final int maskL = this.posMask;
        final int[] keyL = this.keyTable;
        int pos = behavior.hashing(key);
        for (int currKey; (currKey = keyL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                final byte[] valueL;
                final byte oldValue = (valueL = this.valueTable)[pos];
                valueL[pos] = value;
                return oldValue;
            }
        }
        keyL[pos] = key;
        this.valueTable[pos] = value;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return this.defVal;
    }

    public byte putIfAbsent(int key, byte value) {
        final int pos;
        if ((pos = this.find(key)) < 0) {
            this.insert(pos, key, value);
            return this.defVal;
        }
        final byte[] valueL;
        if ((valueL = this.valueTable)[pos] != ((int) NULL_SLOT)) return valueL[pos];
        valueL[pos] = value;
        return 0;
    }

    public byte remove(int key) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(key, ((int) NULL_SLOT))) {
            if (this.withNullKey) return this.removeNull(this.keyTable);
            return this.defVal;
        }

        final int maskL = this.posMask;
        final int[] keyL = this.keyTable;
        int currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.removeAndShift(keyL, pos);
        }
        return this.defVal;
    }

    public boolean remove(int key, byte value) {
        final int pos;
        if ((pos = this.find(key)) > -1 && value == this.valueTable[pos]) {
            this.removeAt(this.keyTable, pos);
            return true;
        }
        return false;
    }

    public byte replace(int key, byte value) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(key, ((int) NULL_SLOT))) {
            if (this.withNullKey) {
                final byte[] valueL;
                final byte result = (valueL = this.valueTable)[this.capacity];
                valueL[this.capacity] = value;
                return result;
            }
            return this.defVal;
        }

        final int maskL = this.posMask;
        final int[] keyL = this.keyTable;
        int currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= this.posMask]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                final byte[] valueL;
                final byte result = (valueL = this.valueTable)[pos];
                valueL[pos] = value;
                return result;
            }
        }
        return this.defVal;
    }

    public boolean replace(int key, byte oldValue, byte newValue) {
        final int pos;
        final byte[] valueL = this.valueTable;
        if ((pos = this.find(key)) > -1 && oldValue == valueL[pos]) {
            valueL[pos] = newValue;
            return true;
        }
        return false;
    }

    public byte computeIfAbsent(int key, IntFunction<? extends Byte> mappingFunction) {
        final int pos;
        if ((pos = this.find(key)) > -1) return this.valueTable[pos];

        final Byte newValue = mappingFunction.apply(key);
        if (newValue != null) {
            return this.insert(pos, key, newValue);
        }
        return this.defVal;
    }

    public byte computeIfPresent(int key, IntByteBiFunction<? extends Byte> remappingFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.defVal;

        final byte[] valueL;
        final Byte newValue = remappingFunction.apply(key, (valueL = this.valueTable)[pos]);
        if (newValue != null) {
            return valueL[pos] = newValue;
        }
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public byte compute(int key, IntByteBiFunction<? extends Byte> remappingFunction) {
        final int pos;
        final byte[] valueL = this.valueTable;
        final Byte newValue = remappingFunction.apply(key, (pos = this.find(key)) < 0 ? 0 : valueL[pos]);
        if (newValue != null) {
            final byte rawValue = newValue;
            return pos < 0 ? this.insert(pos, key, rawValue) : (valueL[pos] = rawValue);
        }

        if (pos >= 0) this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public byte merge(int key, byte value, ByteByteBiFunction<? extends Byte> remappingFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.insert(pos, key, value);

        final byte[] valueL;
        final Byte newValue = remappingFunction.apply((valueL = this.valueTable)[pos], value);
        if (newValue != null) {
            return valueL[pos] = newValue;
        }
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public byte update(int key, ByteUnaryOperator updateFunction) {
        final int pos;
        final byte[] valueL = this.valueTable;
        final byte newValue = updateFunction.apply((pos = this.find(key)) < 0 ? this.defVal : valueL[pos]);
        return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);
    }

    public byte remapping(int key, IntByteBiFunction<? extends Integer> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.defVal;

        final byte[] valueL;
        final byte currValue;
        final Integer newKey = remappingFunction.apply(key, currValue = (valueL = this.valueTable)[pos]);
        final int rawKey = newKey != null ? newKey : 0;
        final boolean sameKey = this.hashBehavior.equals(rawKey, key);

        final int[] keyL = this.keyTable;
        if (newKey == null || !sameKey) this.removeAt(keyL, pos);
        if (newKey == null) return this.defVal;
        if (sameKey) return currValue;

        final int newPos;
        if ((newPos = this.find(rawKey)) < 0) return this.insert(newPos, rawKey, currValue);

        final Byte newValue = mergeFunction.apply(valueL[newPos], currValue);
        if (newValue != null) return valueL[newPos] = newValue;
        this.removeAt(keyL, newPos);
        return this.defVal;
    }

    public void swap(Map<Integer, Byte> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof IntByteLinearProbingHashMap) {
            final IntByteLinearProbingHashMap src = (IntByteLinearProbingHashMap) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                this.clearMappingCollection();
                src.clearMappingCollection();

                final boolean currWithNull = this.withNullKey, srcWithNull = src.withNullKey;
                final int currCap = this.capacity, srcCap = src.capacity,
                        currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final int[] currKey = this.keyTable, srcKey = src.keyTable;
                final byte[] currValue = this.valueTable, srcValue = src.valueTable;

                src.withNullKey = currWithNull;
                src.capacity = currCap;
                src.size = currSize;
                src.posMask = currMask;
                src.remaining = currRemaining;
                src.keyTable = currKey;
                src.valueTable = currValue;
                this.withNullKey = srcWithNull;
                this.capacity = srcCap;
                this.size = srcSize;
                this.posMask = srcMask;
                this.remaining = srcRemaining;
                this.keyTable = srcKey;
                this.valueTable = srcValue;
                return;
            }
        }
        if (source.size() < this.size()) {
            final Map<Integer, Byte> tmp = new IntByteLinearProbingHashMap(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<Integer, Byte> tmp = new IntByteLinearProbingHashMap(this);
            this.clear();
            this.putAll(source);
            source.clear();
            source.putAll(tmp);
        }
    }

    public boolean swapAt(int from, int to) {
        final int posA = this.find(from);
        if (posA < 0) return false;
        final int posB = this.find(to);
        if (posB < 0) return false;

        final byte[] valueL = this.valueTable;
        final byte tmpValue = valueL[posA];
        valueL[posA] = valueL[posB];
        valueL[posB] = tmpValue;
        return false;
    }

    public boolean containsKey(int key) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(key, ((int) NULL_SLOT))) return this.withNullKey;

        final int maskL = this.posMask;
        final int[] keyL = this.keyTable;
        int currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return true;
        }
        return false;
    }

    public boolean containsValue(byte value) {
        if (this.isEmpty()) return false;

        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        final byte[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            if (keyL[pos] != ((int) NULL_SLOT) && value == valueL[pos]) return true;
        }

        return this.withNullKey && value == valueL[capL];
    }

    public void forEach(IntByteBiConsumer action) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        final byte[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final int currKey;
            if ((currKey = keyL[pos]) != ((int) NULL_SLOT)) {
                action.accept(currKey, valueL[pos]);
            }
        }

        if (this.withNullKey) action.accept(keyL[capL], valueL[capL]);
    }

    public void replaceAll(IntByteBiOperator function) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        final byte[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final int currKey;
            if ((currKey = keyL[pos]) != ((int) NULL_SLOT)) {
                valueL[pos] = function.apply(currKey, valueL[pos]);
            }
        }

        if (this.withNullKey) {
            valueL[capL] = function.apply(keyL[capL], valueL[capL]);
        }
    }

    public void putAll(Map<? extends Integer, ? extends Byte> m) {
        if (m == this || m.isEmpty()) return;
        if (m.size() >= this.remaining()) this.reserve(this.size + m.size() + 1);
        if (m instanceof IntByteLinearProbingHashMap) {
            final IntByteLinearProbingHashMap cast = (IntByteLinearProbingHashMap) m;
            final int[] keyL = cast.keyTable;
            final int capL = cast.capacity;
            final byte[] valueL = cast.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                final int currKey;
                if ((currKey = keyL[pos]) != ((int) NULL_SLOT)) {
                    this.put(currKey, valueL[pos]);
                }
            }

            if (cast.withNullKey) this.put(keyL[capL], valueL[capL]);
        } else m.forEach(this::put);
    }

    private void calculateRemaining() {
        this.remaining = InternalCollectionUtil.getHashTableMaxRemaining(this.capacity, this.loadFactor, true) - this.size;
    }

    public byte getDefaultReturnValueRaw() {
        return this.defVal;
    }

    public byte setDefaultReturnValue(byte newDefault) {
        final byte oldDef = this.defVal;
        this.defVal = newDefault;
        return oldDef;
    }

    public int capacity() {
        return this.capacity;
    }

    public int remaining() {
        return this.remaining;
    }

    public float getLoadFactor() {
        return this.loadFactor;
    }

    public HashBehavior<Integer> getBehavior() {
        return this.hashBehavior;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public IntSet keySet() {
        IntSet result;
        if ((result = this.keySet) == null) {
            result = this.keySet = new KeySet();
        }
        return result;
    }

    public ByteCollection values() {
        ByteCollection result;
        if ((result = this.valueCollection) == null) {
            result = this.valueCollection = new ValueCollection();
        }
        return result;
    }

    public EntrySetExt<IntByteMap.Entry> entrySetRaw() {
        EntrySetExt<IntByteMap.Entry> result;
        if ((result = this.entrySet) == null) {
            result = this.entrySet = new MapEntrySet();
        }
        return result;
    }

    public int[] keyTable() {
        return this.keyTable;
    }

    public byte[] valueTable() {
        return this.valueTable;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        this.clearMappingCollection();
        Arrays.fill(this.keyTable, ((int) NULL_SLOT));
        this.calculateRemaining();
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        final int newCap = InternalCollectionUtil.getCapacityPOT(Math.max(newCapacity, 2));
        if (newCap == this.capacity) {
            this.clear();
        } else {
            this.capacity = newCap;
            this.posMask = newCap - 1;
            this.size = 0;
            this.clearMappingCollection();
            final int fullCap = newCap + 1;
            this.keyTable = this.newKeyTable(fullCap);
            this.valueTable = this.newValueTable(fullCap);
            this.calculateRemaining();
        }
    }

    public int hashCode() {
        if (this.isEmpty()) return 0;

        int result = 0;
        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        final byte[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final int currKey;
            if ((currKey = keyL[pos]) != ((int) NULL_SLOT)) {
                result += Integer.hashCode(currKey) ^ Byte.hashCode(valueL[pos]);
            }
        }

        if (this.withNullKey) result += Integer.hashCode(keyL[capL]) ^ Byte.hashCode(valueL[capL]);
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Map<?, ?>) {
            final Map<?, ?> castMap = (Map<?, ?>) o;
            if (castMap.size() != this.size()) return false;
            try {
                if (castMap instanceof IntByteLinearProbingHashMap) {
                    final IntByteLinearProbingHashMap cast = (IntByteLinearProbingHashMap) castMap;
                    if (this.withNullKey != cast.withNullKey) return false;
                    final int[] currKeyL = this.keyTable, srcKeyL = cast.keyTable;
                    final byte[] currValueL = this.valueTable, srcValueL = cast.valueTable;

                    final int capacity = srcKeyL.length;
                    for (int srcPos = 0; srcPos < capacity; srcPos++) {
                        final int srcKey;
                        if ((srcKey = srcKeyL[srcPos]) != ((int) NULL_SLOT)) {
                            final int currPos = this.find(srcKey);
                            if (!(currPos > -1 && currValueL[currPos] == srcValueL[srcPos])) return false;
                        }
                    }
                    if (this.withNullKey) return currKeyL[this.capacity] == srcKeyL[cast.capacity] && currValueL[this.capacity] == srcValueL[cast.capacity];
                } else {
                    final byte[] currValueL = this.valueTable;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final Integer srcKey = (Integer) entry.getKey();
                        final Byte srcValue = (Byte) entry.getValue();
                        final int currPos = this.find(srcKey);
                        if (currPos < 0) return false;

                        final long currValue = currValueL[currPos];
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

        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        final byte[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final int currKey;
            if ((currKey = keyL[pos]) != ((int) NULL_SLOT)) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currKey)
                        .append('=')
                        .append(valueL[pos]);
            }
        }

        if (this.withNullKey) {
            if (sb.length() > 1) sb.append(',').append(' ');
            sb.append(keyL[capL])
                    .append('=')
                    .append(valueL[capL]);
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
        IntByteLinearProbingHashMap result;
        try {
            result = (IntByteLinearProbingHashMap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.keyTable.length;
        result.keyTable = result.newKeyTable(srcLen);
        result.valueTable = result.newValueTable(srcLen);
        result.clearMappingCollection();
        System.arraycopy(this.keyTable, 0, result.keyTable, 0, srcLen);
        System.arraycopy(this.valueTable, 0, result.valueTable, 0, srcLen);
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        final byte[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final int currKey;
            if ((currKey = keyL[pos]) != ((int) NULL_SLOT)) {
                out.writeInt(currKey);
                out.writeByte(valueL[pos]);
            }
        }
        if (this.withNullKey) {
            out.writeInt(keyL[capL]);
            out.writeByte(valueL[capL]);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehavior);
        InternalCollectionUtil.SerialCheck.ioCheckHashLoadFactor(this.loadFactor, 0.2f, 0.9f);
        InternalCollectionUtil.SerialCheck.ioCheckSize(this.size, 0, HashContainer.MAXIMUM_CAPACITY);
        this.capacity = InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(this.size, this.loadFactor, HashContainer.MAXIMUM_CAPACITY);
        final int maskL = this.posMask = this.capacity - 1;
        this.clearMappingCollection();
        final int fullCap = this.capacity + 1;
        final int[] keyL = this.keyTable = this.newKeyTable(fullCap);
        final byte[] valueL = this.valueTable = this.newValueTable(fullCap);
        this.withNullKey = false;

        final HashBehavior<Integer> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final int currKey = in.readInt();
            final byte currValue = in.readByte();
            int pos;
            if (behavior.equals(currKey, ((int) NULL_SLOT))) {
                if (this.withNullKey) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                pos = this.capacity;
                this.withNullKey = true;
            } else {
                pos = behavior.hashing(currKey) & maskL;
                int findKey;
                while ((findKey = keyL[pos]) != ((int) NULL_SLOT)) {
                    if (behavior.equals(currKey, findKey)) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                    pos = (pos + 1) & maskL;
                }
            }
            keyL[pos] = currKey;
            valueL[pos] = currValue;
        }
        this.calculateRemaining();
    }

    protected int[] prepareArray(final int[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return this.newKeyTable(sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected byte[] prepareArray(final byte[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return this.newValueTable(sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected int[] keyToArray(final int[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        for (int pos = 0; pos < capL;) {
            final int currKey;
            if ((currKey = keyL[pos++]) != ((int) NULL_SLOT)) a[idx++] = currKey;
        }

        if (this.withNullKey) a[idx] = keyL[capL];
        return a;
    }

    protected byte[] valueToArray(final byte[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final int capL = this.capacity;
        final int[] keyL = this.keyTable;
        final byte[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            if (keyL[pos] != ((int) NULL_SLOT)) a[idx++] = valueL[pos];
        }

        if (this.withNullKey) a[idx] = valueL[capL];
        return a;
    }

    protected class Node implements IntByteMap.Entry {
        protected int pos;

        protected Node update(int pos) {
            this.pos = pos;
            return this;
        }

        protected Node() {}

        protected Node(int pos) {
            this.pos = pos;
        }

        public int getKeyRaw() {
            return IntByteLinearProbingHashMap.this.keyTable[this.pos];
        }

        public byte getValueRaw() {
            return IntByteLinearProbingHashMap.this.valueTable[this.pos];
        }

        public byte setValue(byte newValue) {
            final int posL = this.pos;
            final byte[] valueL = IntByteLinearProbingHashMap.this.valueTable;
            final byte oldValue = valueL[posL];
            valueL[posL] = newValue;
            return oldValue;
        }

        public String toString() {
            final int posL = this.pos;
            return IntByteLinearProbingHashMap.this.keyTable[posL] + "=" + IntByteLinearProbingHashMap.this.valueTable[posL];
        }

        public int hashCode() {
            final int posL = this.pos;
            return Integer.hashCode(IntByteLinearProbingHashMap.this.keyTable[posL]) ^ Byte.hashCode(IntByteLinearProbingHashMap.this.valueTable[posL]);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            final int posL = this.pos;
            return Objects.equals(IntByteLinearProbingHashMap.this.keyTable[posL], cast.getKey())
                    && Objects.equals(IntByteLinearProbingHashMap.this.valueTable[posL], cast.getValue());
        }
    }

    protected abstract class MapIterator<C> {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected MapIterator() {
            if (IntByteLinearProbingHashMap.this.size > 0) {
                this.scanPos(IntByteLinearProbingHashMap.this.keyTable, 0);
            }
        }

        protected void scanPos(final int[] keyL, int start) {
            final int capL = IntByteLinearProbingHashMap.this.capacity;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (keyL[i] != ((int) NULL_SLOT)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
            }
            if (IntByteLinearProbingHashMap.this.withNullKey) {
                if (findPos < 0) findPos = capL;
                else if (findPosNext < 0) findPosNext = capL;
            }
            this.pos = findPos;
            this.posNext = findPosNext;
        }

        public boolean hasNext() {
            return this.pos > -1;
        }

        public int nextPos() {
            final int current;
            if ((current = this.pos) < 0) throw new NoSuchElementException();

            this.posPre = current;
            final int posL = this.pos = this.posNext;
            this.posNext = -1;

            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            if (posL > -1) {
                final int capL = IntByteLinearProbingHashMap.this.capacity;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (keyL[i] != ((int) NULL_SLOT)) {
                        this.posNext = i;
                        break;
                    }
                }
                if (i == capL && IntByteLinearProbingHashMap.this.withNullKey) {
                    this.posNext = i;
                }
            }
            return current;
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            if (posPreL == IntByteLinearProbingHashMap.this.capacity) {
                IntByteLinearProbingHashMap.this.removeNull(IntByteLinearProbingHashMap.this.keyTable);
                this.pos = -1;
            } else {
                final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
                IntByteLinearProbingHashMap.this.removeAndShift(keyL, posPreL);
                this.scanPos(keyL, posPreL);
            }
            this.posPre = -1;
        }

        protected abstract void consumerProxy(C action, int key, int pos);

        public void forEachRemaining(C action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            final int capL = IntByteLinearProbingHashMap.this.capacity;
            int i, findPre = this.posPre;
            for (i = current; i < capL; i++) {
                final int currKey;
                if ((currKey = keyL[i]) != ((int) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, findPre = i);
                }
            }
            if (i == capL && IntByteLinearProbingHashMap.this.withNullKey) {
                this.consumerProxy(action, keyL[capL], findPre = capL);
            }
            this.posPre = findPre;
            this.pos = -1;
        }
    }

    protected abstract class MapSpliterator<C, S> {
        protected int index;
        protected int limitCap;
        protected int estSize;

        protected MapSpliterator(int index, int limitCap, int estSize) {
            this.index = index;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = IntByteLinearProbingHashMap.this.size;
                newLimit = this.limitCap = IntByteLinearProbingHashMap.this.capacity;
            }
            return newLimit;
        }

        protected abstract S splitProxy(int index, int limitCap, int estSize);

        public S trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? this.splitProxy(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        protected abstract void consumerProxy(C action, int key, int pos);

        public boolean tryAdvance(C action) {
            final int currLimit = this.getLimitCap();

            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final int currKey;
                if ((currKey = keyL[idxL]) != ((int) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, idxL);
                    this.index = idxL + 1;
                    return true;
                }
            }
            final int capL = IntByteLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && IntByteLinearProbingHashMap.this.withNullKey) {
                this.consumerProxy(action, keyL[capL], capL);
                this.index = capL + 1;
                return true;
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(C action) {
            final int currLimit = this.getLimitCap();

            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final int currKey;
                if ((currKey = keyL[idxL]) != ((int) NULL_SLOT)) this.consumerProxy(action, currKey, idxL);
            }
            final int capL = IntByteLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && IntByteLinearProbingHashMap.this.withNullKey) {
                this.consumerProxy(action, keyL[capL], capL);
                idxL = capL + 1;
            }
            this.index = idxL;
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == IntByteLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class KeyIterator extends MapIterator<IntConsumer> implements IntIterator {
        public int nextInt() {
            return IntByteLinearProbingHashMap.this.keyTable[this.nextPos()];
        }

        protected void consumerProxy(IntConsumer action, int key, int pos) {
            action.accept(key);
        }
    }

    protected class ValueIterator extends MapIterator<ByteConsumer> implements ByteIterator {
        public byte nextByte() {
            return IntByteLinearProbingHashMap.this.valueTable[this.nextPos()];
        }

        protected void consumerProxy(ByteConsumer action, int key, int pos) {
            action.accept(IntByteLinearProbingHashMap.this.valueTable[pos]);
        }
    }

    protected class MapEntryIterator extends MapIterator<Consumer<? super IntByteMap.Entry>> implements Iterator<IntByteMap.Entry> {
        public IntByteMap.Entry next() {
            return new Node(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super Entry> action, int key, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewIterator extends MapIterator<Consumer<? super IntByteMap.Entry>> implements Iterator<IntByteMap.Entry> {
        protected final Node tmpEntry = new Node();

        public IntByteMap.Entry next() {
            return this.tmpEntry.update(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super Entry> action, int key, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }
    }

    protected class KeySpliterator extends MapSpliterator<IntConsumer, IntSpliterator> implements IntSpliterator {
        protected KeySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected IntSpliterator splitProxy(int index, int limitCap, int estSize) {
            return new KeySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(IntConsumer action, int key, int pos) {
            action.accept(key);
        }
    }

    protected class ValueSpliterator extends MapSpliterator<ByteConsumer, ByteSpliterator> implements ByteSpliterator {
        protected ValueSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected ByteSpliterator splitProxy(int index, int limitCap, int estSize) {
            return new ValueSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(ByteConsumer action, int key, int pos) {
            action.accept(IntByteLinearProbingHashMap.this.valueTable[pos]);
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == IntByteLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.NONNULL;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super IntByteMap.Entry>, Spliterator<IntByteMap.Entry>> implements Spliterator<IntByteMap.Entry> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<IntByteMap.Entry> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super IntByteMap.Entry> action, int key, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewSpliterator extends MapSpliterator<Consumer<? super IntByteMap.Entry>, Spliterator<IntByteMap.Entry>> implements Spliterator<IntByteMap.Entry> {
        protected final Node tmpEntry = new Node();

        protected MapEntryViewSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<IntByteMap.Entry> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntryViewSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super IntByteMap.Entry> action, int key, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }
    }

    protected class KeySet extends AbstractIntSet {
        public void resize(int newCapacity) {
            IntByteLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            IntByteLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(int o) {
            return IntByteLinearProbingHashMap.this.containsKey(o);
        }

        public boolean removeElement(int o) {
            final HashBehavior<Integer> behavior = IntByteLinearProbingHashMap.this.hashBehavior;
            if (behavior.equals(o, null)) {
                if (IntByteLinearProbingHashMap.this.withNullKey) {
                    IntByteLinearProbingHashMap.this.removeNull(IntByteLinearProbingHashMap.this.keyTable);
                    return true;
                }
                return false;
            }

            final int maskL = IntByteLinearProbingHashMap.this.posMask;
            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            int currKey;
            for (int pos = behavior.hashing(o); (currKey = keyL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
                if (behavior.equals(o, currKey)) {
                    IntByteLinearProbingHashMap.this.removeAndShift(keyL, pos);
                    return true;
                }
            }
            return false;
        }

        public void forEach(IntConsumer action) {
            if (IntByteLinearProbingHashMap.this.isEmpty()) return;

            final int capL = IntByteLinearProbingHashMap.this.capacity;
            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            int currKey;
            for (int pos = 0; pos < capL;) {
                if ((currKey = keyL[pos++]) != ((int) NULL_SLOT)) {
                    action.accept(currKey);
                }
            }

            if (IntByteLinearProbingHashMap.this.withNullKey) action.accept(keyL[capL]);
        }

        public int capacity() {
            return IntByteLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return IntByteLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return IntByteLinearProbingHashMap.this.size;
        }

        public IntIterator iterator() {
            return new KeyIterator();
        }

        public IntSpliterator spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            IntByteLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            IntByteLinearProbingHashMap.this.clear(newCapacity);
        }

        public int[] toArrayRaw() {
            return IntByteLinearProbingHashMap.this.keyToArray(IntByteLinearProbingHashMap.this.newKeyTable(IntByteLinearProbingHashMap.this.size));
        }

        public int[] toArray(int[] a) {
            return IntByteLinearProbingHashMap.this.keyToArray(IntByteLinearProbingHashMap.this.prepareArray(a));
        }
    }

    protected class ValueCollection extends AbstractByteCollection {
        public void resize(int newCapacity) {
            IntByteLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            IntByteLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(byte o) {
            return IntByteLinearProbingHashMap.this.containsValue(o);
        }

        public void forEach(ByteConsumer action) {
            if (IntByteLinearProbingHashMap.this.isEmpty()) return;

            final int capL = IntByteLinearProbingHashMap.this.capacity;
            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            final byte[] valueL = IntByteLinearProbingHashMap.this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((int) NULL_SLOT)) {
                    action.accept(valueL[pos]);
                }
            }

            if (IntByteLinearProbingHashMap.this.withNullKey) action.accept(valueL[capL]);
        }

        public int capacity() {
            return IntByteLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return IntByteLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return IntByteLinearProbingHashMap.this.size;
        }

        public ByteIterator iterator() {
            return new ValueIterator();
        }

        public ByteSpliterator spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public byte[] toArrayRaw() {
            return IntByteLinearProbingHashMap.this.valueToArray(IntByteLinearProbingHashMap.this.newValueTable(IntByteLinearProbingHashMap.this.size));
        }

        public byte[] toArray(byte[] a) {
            return IntByteLinearProbingHashMap.this.valueToArray(IntByteLinearProbingHashMap.this.prepareArray(a));
        }

        public void clear() {
            IntByteLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            IntByteLinearProbingHashMap.this.clear(newCapacity);
        }
    }

    protected class MapEntrySet extends AbstractObjSet<IntByteMap.Entry> implements EntrySetExt<IntByteMap.Entry> {
        protected final Node tmpEntry = new Node();

        public void resize(int newCapacity) {
            IntByteLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            IntByteLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return IntByteLinearProbingHashMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return IntByteLinearProbingHashMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        protected void forEachProxy(Consumer<? super IntByteMap.Entry> action, IntFunction<IntByteMap.Entry> supplier) {
            if (IntByteLinearProbingHashMap.this.isEmpty()) return;

            final int capL = IntByteLinearProbingHashMap.this.capacity;
            final int[] keyL = IntByteLinearProbingHashMap.this.keyTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((int) NULL_SLOT)) {
                    action.accept(supplier.apply(pos));
                }
            }

            if (IntByteLinearProbingHashMap.this.withNullKey) action.accept(supplier.apply(capL));
        }

        public void forEach(Consumer<? super IntByteMap.Entry> action) {
            this.forEachProxy(action, Node::new);
        }

        public void viewForEach(Consumer<? super Entry> action) {
            this.forEachProxy(action, this.tmpEntry::update);
        }

        public int capacity() {
            return IntByteLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return IntByteLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return IntByteLinearProbingHashMap.this.size;
        }

        public Iterator<IntByteMap.Entry> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<IntByteMap.Entry> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<Entry> viewIterator() {
            return new MapEntryViewIterator();
        }

        public Spliterator<Entry> viewSpliterator() {
            return new MapEntryViewSpliterator(0, -1, 0);
        }

        public void clear() {
            IntByteLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            IntByteLinearProbingHashMap.this.clear(newCapacity);
        }
    }
}
