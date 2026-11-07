package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSpliterator;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjLongMap;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;
import io.github.shiozakanaptr.acext.util.collection.longtype.AbstractLongCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.LongLongBiFunction;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjLongBiConsumer;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjLongBiFunction;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjLongBiOperator;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "deprecation", "RedundantCast"})
public class ObjLongLinearProbingHashMap<K> implements ObjLongMap<K>, HashContainer<K>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static Object NULL_SLOT = null;

    protected transient boolean withNullKey = false;
    protected transient int remaining = 0;
    protected transient int capacity;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected long defVal = 0;
    protected final HashBehavior<K> hashBehavior;
    protected transient K[] keyTable;
    protected transient long[] valueTable;

    protected transient SetExt<K> keySet = null;
    protected transient LongCollection valueCollection = null;
    protected transient EntrySetExt<ObjLongMap.Entry<K>> entrySet = null;

    protected K[] newKeyTable(int cap) {
        return (K[]) new Object[cap];
    }

    protected long[] newValueTable(int cap) {
        return new long[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLongLinearProbingHashMap(int initialCapacity, float loadFactor, final HashBehavior<K> hashBehavior) {
        InternalCollectionUtil.checkHashCapacity(initialCapacity);
        InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 0.9f);

        this.capacity = InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2));
        this.posMask = this.capacity - 1;
        this.loadFactor = loadFactor;
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
    public ObjLongLinearProbingHashMap(int initialCapacity, final HashBehavior<K> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjLongLinearProbingHashMap(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public ObjLongLinearProbingHashMap() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLongLinearProbingHashMap(final Map<? extends K, ? extends Long> source, float loadFactor, final HashBehavior<K> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.putAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLongLinearProbingHashMap(final Map<? extends K, ? extends Long> source, final HashBehavior<K> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public ObjLongLinearProbingHashMap(final Map<? extends K, ? extends Long> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor), newTargetCap = InternalCollectionUtil.getCapacityPOT(newCapacity);
        InternalCollectionUtil.checkHashCapacity(newTargetCap);
        if (newTargetCap == this.capacity) return;

        final int newCap = Math.max(minSize, Math.max(newTargetCap, 2)),
                newMask = newCap - 1, fullCap = newCap + 1, oldCap;
        final K[] newKey = this.newKeyTable(fullCap), oldKey = this.keyTable;
        final long[] newValue = this.newValueTable(fullCap), oldValue = this.valueTable;

        if (this.size > 0) {
            oldCap = this.capacity;
            for (int i = 0; i < oldCap; i++) {
                final K currKey;
                if ((currKey = oldKey[i]) != ((K) NULL_SLOT)) {
                    int pos = this.hashBehavior.hashing(currKey) & newMask;
                    while (newKey[pos] != ((K) NULL_SLOT)) {
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

    protected long removeNull(final K[] keyL) {
        final int capL = this.capacity;
        final long[] valueL = this.valueTable;
        final long result = valueL[capL];
        this.withNullKey = false;
        keyL[capL] = ((K) NULL_SLOT);
        this.size--;
        this.remaining++;
        return result;
    }

    protected long removeAndShift(final K[] keyL, int currPos) {
        final long[] valueL = this.valueTable;
        final long result = valueL[currPos];
        keyL[currPos] = ((K) NULL_SLOT);
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        K currKey;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currKey = keyL[nextPos = (nextPos + 1) & maskL]) != ((K) NULL_SLOT); dist++) {
            if (((nextPos - (this.hashBehavior.hashing(currKey) & maskL)) & maskL) >= dist) {
                keyL[holePos] = currKey;
                keyL[nextPos] = ((K) NULL_SLOT);
                valueL[holePos] = valueL[nextPos];
                holePos = nextPos;
                dist = 0;
            }
        }
        return result;
    }

    protected void removeAt(final K[] keyL, int pos) {
        if (pos != this.capacity) this.removeAndShift(keyL, pos); else this.removeNull(keyL);
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.capacity));
    }

    protected int find(final Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) {
            return this.withNullKey ? this.capacity : ~this.capacity;
        }

        final int maskL = this.posMask;
        final K[] keyL = this.keyTable;
        int pos = behavior.hashing(key);
        for (K currKey; (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                return pos;
            }
        }
        return ~pos;
    }

    protected long insert(int pos, final K key, final long value) {
        final int realPos = ~pos;
        if (realPos == this.capacity) this.withNullKey = true;
        this.keyTable[realPos] = key;
        this.valueTable[realPos] = value;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return value;
    }

    public long getRaw(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : this.defVal;

        final int maskL = this.posMask;
        final K[] keyL = this.keyTable;
        K currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.valueTable[pos];
        }
        return this.defVal;
    }

    public long getOrDefault(Object key, long defaultValue) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : defaultValue;

        final int maskL = this.posMask;
        final K[] keyL = this.keyTable;
        K currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.valueTable[pos];
        }
        return defaultValue;
    }

    public long put(K key, long value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) {
            final long[] valueL = this.valueTable;
            final long result;
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
        final K[] keyL = this.keyTable;
        int pos = behavior.hashing(key);
        for (K currKey; (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                final long[] valueL;
                final long oldValue = (valueL = this.valueTable)[pos];
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

    public long putIfAbsent(K key, long value) {
        final int pos;
        if ((pos = this.find(key)) < 0) {
            this.insert(pos, key, value);
            return this.defVal;
        }
        final long[] valueL;
        if ((valueL = this.valueTable)[pos] != 0) return valueL[pos];
        valueL[pos] = value;
        return 0;
    }

    public long removeRaw(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) {
            if (this.withNullKey) return this.removeNull(this.keyTable);
            return this.defVal;
        }

        final int maskL = this.posMask;
        final K[] keyL = this.keyTable;
        K currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.removeAndShift(keyL, pos);
        }
        return this.defVal;
    }

    public boolean remove(Object key, long value) {
        final int pos;
        if ((pos = this.find(key)) > -1 && value == this.valueTable[pos]) {
            this.removeAt(this.keyTable, pos);
            return true;
        }
        return false;
    }

    public long replace(K key, long value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) {
            if (this.withNullKey) {
                final long[] valueL;
                final long result = (valueL = this.valueTable)[this.capacity];
                valueL[this.capacity] = value;
                return result;
            }
            return this.defVal;
        }

        final int maskL = this.posMask;
        final K[] keyL = this.keyTable;
        K currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= this.posMask]) != ((K) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                final long[] valueL = this.valueTable;
                final long result = valueL[pos];
                valueL[pos] = value;
                return result;
            }
        }
        return this.defVal;
    }

    public boolean replace(K key, long oldValue, long newValue) {
        final int pos;
        final long[] valueL = this.valueTable;
        if ((pos = this.find(key)) > -1 && oldValue == valueL[pos]) {
            valueL[pos] = newValue;
            return true;
        }
        return false;
    }

    public long computeIfAbsentRaw(K key, Function<? super K, ? extends Long> mappingFunction) {
        final int pos;
        if ((pos = this.find(key)) > -1) return this.valueTable[pos];

        final Long newValue = mappingFunction.apply(key);
        if (newValue != null) {
            return this.insert(pos, key, newValue);
        }
        return this.defVal;
    }

    public long computeIfPresent(K key, ObjLongBiFunction<? super K, ? extends Long> remappingFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.defVal;

        final long[] valueL;
        final Long newValue = remappingFunction.apply(key, (valueL = this.valueTable)[pos]);
        if (newValue != null) {
            return valueL[pos] = newValue;
        }
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public long compute(K key, ObjLongBiFunction<? super K, ? extends Long> remappingFunction) {
        final int pos;
        final long[] valueL = this.valueTable;
        final Long newValue = remappingFunction.apply(key, (pos = this.find(key)) < 0 ? 0 : valueL[pos]);
        if (newValue != null) {
            final long rawValue = newValue;
            return pos < 0 ? this.insert(pos, key, rawValue) : (valueL[pos] = rawValue);
        }

        if (pos >= 0) this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public long merge(K key, long value, LongLongBiFunction<? extends Long> remappingFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.insert(pos, key, value);

        final long[] valueL;
        final Long newValue = remappingFunction.apply((valueL = this.valueTable)[pos], value);
        if (newValue != null) {
            return valueL[pos] = newValue;
        }
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public long update(K key, LongUnaryOperator updateFunction) {
        final int pos;
        final long[] valueL = this.valueTable;
        final long newValue = updateFunction.applyAsLong((pos = this.find(key)) < 0 ? this.defVal : valueL[pos]);
        return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);
    }

    public long remapping(K key, ObjLongBiFunction<? super K, ? extends K> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.defVal;

        final long[] valueL;
        final long currValue;
        final K newKey = remappingFunction.apply(key, currValue = (valueL = this.valueTable)[pos]);
        final boolean sameKey = this.hashBehavior.equals(newKey, key);

        final K[] keyL = this.keyTable;
        if (newKey == null || !sameKey) this.removeAt(keyL, pos);
        if (newKey == null) return this.defVal;
        if (sameKey) return currValue;

        final int newPos;
        if ((newPos = this.find(newKey)) < 0) return this.insert(newPos, newKey, currValue);

        final Long newValue = mergeFunction.apply(valueL[newPos], currValue);
        if (newValue != null) return valueL[newPos] = newValue;
        this.removeAt(keyL, newPos);
        return this.defVal;
    }

    public void swap(Map<K, Long> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ObjLongLinearProbingHashMap) {
            final ObjLongLinearProbingHashMap<K> src = (ObjLongLinearProbingHashMap<K>) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                this.clearMappingCollection();
                src.clearMappingCollection();

                final boolean currWithNull = this.withNullKey, srcWithNull = src.withNullKey;
                final int currCap = this.capacity, srcCap = src.capacity,
                        currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final K[] currKey = this.keyTable, srcKey = src.keyTable;
                final long[] currValue = this.valueTable, srcValue = src.valueTable;

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
            final Map<K, Long> tmp = new ObjLongLinearProbingHashMap<>(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<K, Long> tmp = new ObjLongLinearProbingHashMap<>(this);
            this.clear();
            this.putAll(source);
            source.clear();
            source.putAll(tmp);
        }
    }

    public boolean swapAt(K from, K to) {
        final int posA = this.find(from);
        if (posA < 0) return false;
        final int posB = this.find(to);
        if (posB < 0) return false;

        final long[] valueL = this.valueTable;
        final long tmpValue = valueL[posA];
        valueL[posA] = valueL[posB];
        valueL[posB] = tmpValue;
        return false;
    }

    public boolean containsKey(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) return this.withNullKey;

        final int maskL = this.posMask;
        final K[] keyL = this.keyTable;
        K currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return true;
        }
        return false;
    }

    public boolean containsValue(long value) {
        if (this.isEmpty()) return false;

        final int capL = this.capacity;
        final K[] keyL = this.keyTable;
        final long[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            if (keyL[pos] != ((K) NULL_SLOT) && value == valueL[pos]) return true;
        }

        return this.withNullKey && value == valueL[capL];
    }

    public void forEach(ObjLongBiConsumer<? super K> action) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final K[] keyL = this.keyTable;
        final long[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                action.accept(currKey, valueL[pos]);
            }
        }

        if (this.withNullKey) action.accept(keyL[capL], valueL[capL]);
    }

    public void replaceAll(ObjLongBiOperator<? super K> function) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final K[] keyL = this.keyTable;
        final long[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                valueL[pos] = function.apply(currKey, valueL[pos]);
            }
        }

        if (this.withNullKey) {
            valueL[capL] = function.apply(keyL[capL], valueL[capL]);
        }
    }

    public void putAll(Map<? extends K, ? extends Long> m) {
        if (m == this || m.isEmpty()) return;
        if (m.size() >= this.remaining()) this.reserve(this.size + m.size() + 1);
        if (m instanceof ObjLongLinearProbingHashMap<?>) {
            final ObjLongLinearProbingHashMap<?> cast = (ObjLongLinearProbingHashMap<?>) m;
            final K[] keyL = (K[]) cast.keyTable;
            final int capL = cast.capacity;
            final long[] valueL = cast.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                final K currKey;
                if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                    this.put(currKey, valueL[pos]);
                }
            }

            if (cast.withNullKey) this.put(keyL[capL], valueL[capL]);
        } else m.forEach(this::put);
    }

    private void calculateRemaining() {
        this.remaining = InternalCollectionUtil.getHashTableMaxRemaining(this.capacity, this.loadFactor, true) - this.size;
    }

    public long getDefaultReturnValueRaw() {
        return this.defVal;
    }

    public long setDefaultReturnValue(long newDefault) {
        final long oldDef = this.defVal;
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

    public HashBehavior<K> getBehavior() {
        return this.hashBehavior;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public SetExt<K> keySet() {
        SetExt<K> result;
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

    public EntrySetExt<ObjLongMap.Entry<K>> entrySetRaw() {
        EntrySetExt<ObjLongMap.Entry<K>> result;
        if ((result = this.entrySet) == null) {
            result = this.entrySet = new MapEntrySet();
        }
        return result;
    }

    public K[] keyTable() {
        return this.keyTable;
    }

    public long[] valueTable() {
        return this.valueTable;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        this.clearMappingCollection();
        Arrays.fill(this.keyTable, ((K) NULL_SLOT));
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
        final K[] keyL = this.keyTable;
        final long[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                result += Objects.hashCode(currKey) ^ Long.hashCode(valueL[pos]);
            }
        }

        if (this.withNullKey) result += Objects.hashCode(keyL[capL]) ^ Long.hashCode(valueL[capL]);
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Map<?, ?>) {
            final Map<?, ?> castMap = (Map<?, ?>) o;
            if (castMap.size() != this.size()) return false;
            try {
                if (castMap instanceof ObjLongLinearProbingHashMap<?>) {
                    final ObjLongLinearProbingHashMap<?> cast = (ObjLongLinearProbingHashMap<?>) castMap;
                    if (this.withNullKey != cast.withNullKey) return false;
                    final K[] currKeyL = this.keyTable, srcKeyL = (K[]) cast.keyTable;
                    final long[] currValueL = this.valueTable, srcValueL = cast.valueTable;

                    final int capacity = srcKeyL.length;
                    for (int srcPos = 0; srcPos < capacity; srcPos++) {
                        final K srcKey;
                        if ((srcKey = srcKeyL[srcPos]) != ((K) NULL_SLOT)) {
                            final int currPos = this.find(srcKey);
                            if (currPos < 0) return false;

                            final long currValue = currValueL[currPos], srcValue = srcValueL[srcPos];
                            if (srcValue != currValue) return false;
                        }
                    }
                    if (this.withNullKey) return Objects.equals(currKeyL[this.capacity], srcKeyL[cast.capacity]) && Objects.equals(currValueL[this.capacity], srcValueL[cast.capacity]);
                } else {
                    final long[] currValueL = this.valueTable;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final K srcKey = (K) entry.getKey();
                        final Long srcValue = (Long) entry.getValue();
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
        final K[] keyL = this.keyTable;
        final long[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currKey != this ? currKey : "(this Map)")
                        .append('=')
                        .append(valueL[pos]);
            }
        }

        if (this.withNullKey) {
            final K currKey;
            if (sb.length() > 1) sb.append(',').append(' ');
            sb.append((currKey = keyL[capL]) == this ? "(this Map)" : currKey)
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
        ObjLongLinearProbingHashMap<K> result;
        try {
            result = (ObjLongLinearProbingHashMap<K>) super.clone();
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
        final K[] keyL = this.keyTable;
        final long[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                out.writeObject(currKey);
                out.writeLong(valueL[pos]);
            }
        }
        if (this.withNullKey) {
            out.writeObject(keyL[capL]);
            out.writeLong(valueL[capL]);
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
        final K[] keyL = this.keyTable = this.newKeyTable(fullCap);
        final long[] valueL = this.valueTable = this.newValueTable(fullCap);
        this.withNullKey = false;

        final HashBehavior<K> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final K currKey = (K) in.readObject();
            final long currValue = in.readLong();
            int pos;
            if (behavior.equals(currKey, null)) {
                if (this.withNullKey) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                pos = this.capacity;
                this.withNullKey = true;
            } else {
                pos = behavior.hashing(currKey) & maskL;
                K findKey;
                while ((findKey = keyL[pos]) != ((K) NULL_SLOT)) {
                    if (behavior.equals(currKey, findKey)) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                    pos = (pos + 1) & maskL;
                }
            }
            keyL[pos] = currKey;
            valueL[pos] = currValue;
        }
        this.calculateRemaining();
    }

    protected long[] prepareArray(long[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return this.newValueTable(sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected long[] valueToArray(long[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final int capL = this.capacity;
        final K[] keyL = this.keyTable;
        final long[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            if (keyL[pos] != ((K) NULL_SLOT)) a[idx++] = valueL[pos];
        }

        if (this.withNullKey) a[idx] = valueL[capL];
        return a;
    }

    protected class Node implements ObjLongMap.Entry<K> {
        protected int pos;

        protected Node update(int pos) {
            this.pos = pos;
            return this;
        }

        protected Node() {}

        protected Node(int pos) {
            this.pos = pos;
        }

        public K getKey() {
            return ObjLongLinearProbingHashMap.this.keyTable[this.pos];
        }

        public long getValueRaw() {
            return ObjLongLinearProbingHashMap.this.valueTable[this.pos];
        }

        public long setValue(long newValue) {
            final int posL = this.pos;
            final long[] valueL = ObjLongLinearProbingHashMap.this.valueTable;
            final long oldValue = valueL[posL];
            valueL[posL] = newValue;
            return oldValue;
        }

        public String toString() {
            final int posL = this.pos;
            return ObjLongLinearProbingHashMap.this.keyTable[posL] + "=" + ObjLongLinearProbingHashMap.this.valueTable[posL];
        }

        public int hashCode() {
            final int posL = this.pos;
            return Objects.hashCode(ObjLongLinearProbingHashMap.this.keyTable[posL]) ^ Long.hashCode(ObjLongLinearProbingHashMap.this.valueTable[posL]);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            final int posL = this.pos;
            return Objects.equals(ObjLongLinearProbingHashMap.this.keyTable[posL], cast.getKey())
                    && Objects.equals(ObjLongLinearProbingHashMap.this.valueTable[posL], cast.getValue());
        }
    }

    protected abstract class MapIterator<C> {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected MapIterator() {
            if (ObjLongLinearProbingHashMap.this.size > 0) {
                this.scanPos(ObjLongLinearProbingHashMap.this.keyTable, 0);
            }
        }

        protected void scanPos(final K[] keyL, int start) {
            final int capL = ObjLongLinearProbingHashMap.this.capacity;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (keyL[i] != ((K) NULL_SLOT)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
            }
            if (ObjLongLinearProbingHashMap.this.withNullKey) {
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

            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            if (posL > -1) {
                final int capL = ObjLongLinearProbingHashMap.this.capacity;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (keyL[i] != ((K) NULL_SLOT)) {
                        this.posNext = i;
                        break;
                    }
                }
                if (i == capL && ObjLongLinearProbingHashMap.this.withNullKey) {
                    this.posNext = i;
                }
            }
            return current;
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            if (posPreL == ObjLongLinearProbingHashMap.this.capacity) {
                ObjLongLinearProbingHashMap.this.removeNull(ObjLongLinearProbingHashMap.this.keyTable);
                this.pos = -1;
            } else {
                final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
                ObjLongLinearProbingHashMap.this.removeAndShift(keyL, posPreL);
                this.scanPos(keyL, posPreL);
            }
            this.posPre = -1;
        }

        protected abstract void consumerProxy(C action, K key, int pos);

        public void forEachRemaining(C action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            final int capL = ObjLongLinearProbingHashMap.this.capacity;
            int i, findPre = this.posPre;
            for (i = current; i < capL; i++) {
                final K currKey;
                if ((currKey = keyL[i]) != ((K) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, findPre = i);
                }
            }
            if (i == capL && ObjLongLinearProbingHashMap.this.withNullKey) {
                findPre = i;
                this.consumerProxy(action, keyL[capL], capL);
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
                this.estSize = ObjLongLinearProbingHashMap.this.size;
                newLimit = this.limitCap = ObjLongLinearProbingHashMap.this.capacity;
            }
            return newLimit;
        }

        protected abstract S splitProxy(int index, int limitCap, int estSize);

        public S trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? this.splitProxy(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        protected abstract void consumerProxy(C action, K key, int pos);

        public boolean tryAdvance(C action) {
            final int currLimit = this.getLimitCap();

            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final K currKey;
                if ((currKey = keyL[idxL]) != ((K) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, idxL);
                    this.index = idxL + 1;
                    return true;
                }
            }
            final int capL = ObjLongLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && ObjLongLinearProbingHashMap.this.withNullKey) {
                this.consumerProxy(action, keyL[capL], capL);
                this.index = capL + 1;
                return true;
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(C action) {
            final int currLimit = this.getLimitCap();

            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final K currKey;
                if ((currKey = keyL[idxL]) != ((K) NULL_SLOT)) this.consumerProxy(action, currKey, idxL);
            }
            final int capL = ObjLongLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && ObjLongLinearProbingHashMap.this.withNullKey) {
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
            return (this.limitCap < 0 || this.estSize == ObjLongLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class KeyIterator extends MapIterator<Consumer<? super K>> implements Iterator<K> {
        public K next() {
            return ObjLongLinearProbingHashMap.this.keyTable[this.nextPos()];
        }

        protected void consumerProxy(Consumer<? super K> action, K key, int pos) {
            action.accept(key);
        }
    }

    protected class ValueIterator extends MapIterator<LongConsumer> implements LongIterator {
        public long nextLong() {
            return ObjLongLinearProbingHashMap.this.valueTable[this.nextPos()];
        }

        protected void consumerProxy(LongConsumer action, K key, int pos) {
            action.accept(ObjLongLinearProbingHashMap.this.valueTable[pos]);
        }
    }

    protected class MapEntryIterator extends MapIterator<Consumer<? super ObjLongMap.Entry<K>>> implements Iterator<ObjLongMap.Entry<K>> {
        public ObjLongMap.Entry<K> next() {
            return new Node(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super Entry<K>> action, K key, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewIterator extends MapIterator<Consumer<? super ObjLongMap.Entry<K>>> implements Iterator<ObjLongMap.Entry<K>> {
        protected final Node tmpEntry = new Node();

        public ObjLongMap.Entry<K> next() {
            return this.tmpEntry.update(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super Entry<K>> action, K key, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }
    }

    protected class KeySpliterator extends MapSpliterator<Consumer<? super K>, Spliterator<K>> implements Spliterator<K> {
        protected KeySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<K> splitProxy(int index, int limitCap, int estSize) {
            return new KeySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super K> action, K key, int pos) {
            action.accept(key);
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjLongLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }

    protected class ValueSpliterator extends MapSpliterator<LongConsumer, LongSpliterator> implements LongSpliterator {
        protected ValueSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected LongSpliterator splitProxy(int index, int limitCap, int estSize) {
            return new ValueSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(LongConsumer action, K key, int pos) {
            action.accept(ObjLongLinearProbingHashMap.this.valueTable[pos]);
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjLongLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.NONNULL;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super ObjLongMap.Entry<K>>, Spliterator<ObjLongMap.Entry<K>>> implements Spliterator<ObjLongMap.Entry<K>> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjLongMap.Entry<K>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjLongMap.Entry<K>> action, K key, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewSpliterator extends MapSpliterator<Consumer<? super ObjLongMap.Entry<K>>, Spliterator<ObjLongMap.Entry<K>>> implements Spliterator<ObjLongMap.Entry<K>> {
        protected final Node tmpEntry = new Node();

        protected MapEntryViewSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjLongMap.Entry<K>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntryViewSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjLongMap.Entry<K>> action, K key, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }
    }

    protected class KeySet extends AbstractObjSet<K> {
        public void resize(int newCapacity) {
            ObjLongLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjLongLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjLongLinearProbingHashMap.this.containsKey(o);
        }

        public boolean removeElement(Object o) {
            final HashBehavior<K> behavior = ObjLongLinearProbingHashMap.this.hashBehavior;
            if (behavior.equals(o, null)) {
                if (ObjLongLinearProbingHashMap.this.withNullKey) {
                    ObjLongLinearProbingHashMap.this.removeNull(ObjLongLinearProbingHashMap.this.keyTable);
                    return true;
                }
                return false;
            }

            final int maskL = ObjLongLinearProbingHashMap.this.posMask;
            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            K currKey;
            for (int pos = behavior.hashing(o); (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
                if (behavior.equals(o, currKey)) {
                    ObjLongLinearProbingHashMap.this.removeAndShift(keyL, pos);
                    return true;
                }
            }
            return false;
        }

        public void forEach(Consumer<? super K> action) {
            if (ObjLongLinearProbingHashMap.this.isEmpty()) return;

            final int capL = ObjLongLinearProbingHashMap.this.capacity;
            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            K currKey;
            for (int pos = 0; pos < capL;) {
                if ((currKey = keyL[pos++]) != ((K) NULL_SLOT)) {
                    action.accept(currKey);
                }
            }

            if (ObjLongLinearProbingHashMap.this.withNullKey) action.accept(keyL[capL]);
        }

        public int capacity() {
            return ObjLongLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjLongLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return ObjLongLinearProbingHashMap.this.size;
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public Spliterator<K> spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            ObjLongLinearProbingHashMap.this.clear();
        }
        
        public void clear(int newCapacity) {
            ObjLongLinearProbingHashMap.this.clear(newCapacity);
        }
    }

    protected class ValueCollection extends AbstractLongCollection {
        public void resize(int newCapacity) {
            ObjLongLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjLongLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(long o) {
            return ObjLongLinearProbingHashMap.this.containsValue(o);
        }

        public void forEach(LongConsumer action) {
            if (ObjLongLinearProbingHashMap.this.isEmpty()) return;

            final int capL = ObjLongLinearProbingHashMap.this.capacity;
            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            final long[] valueL = ObjLongLinearProbingHashMap.this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((K) NULL_SLOT)) {
                    action.accept(valueL[pos]);
                }
            }

            if (ObjLongLinearProbingHashMap.this.withNullKey) action.accept(valueL[capL]);
        }

        public int capacity() {
            return ObjLongLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjLongLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return ObjLongLinearProbingHashMap.this.size;
        }

        public LongIterator iterator() {
            return new ValueIterator();
        }

        public LongSpliterator spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjLongLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjLongLinearProbingHashMap.this.clear(newCapacity);
        }

        public long[] toArrayRaw() {
            return ObjLongLinearProbingHashMap.this.valueToArray(ObjLongLinearProbingHashMap.this.newValueTable(ObjLongLinearProbingHashMap.this.size));
        }

        public long[] toArray(long[] a) {
            return ObjLongLinearProbingHashMap.this.valueToArray(ObjLongLinearProbingHashMap.this.prepareArray(a));
        }
    }

    protected class MapEntrySet extends AbstractObjSet<ObjLongMap.Entry<K>> implements EntrySetExt<ObjLongMap.Entry<K>> {
        protected final Node tmpEntry = new Node();

        public void resize(int newCapacity) {
            ObjLongLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjLongLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjLongLinearProbingHashMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjLongLinearProbingHashMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        protected void forEachProxy(Consumer<? super ObjLongMap.Entry<K>> action, IntFunction<ObjLongMap.Entry<K>> supplier) {
            if (ObjLongLinearProbingHashMap.this.isEmpty()) return;

            final int capL = ObjLongLinearProbingHashMap.this.capacity;
            final K[] keyL = ObjLongLinearProbingHashMap.this.keyTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((K) NULL_SLOT)) {
                    action.accept(supplier.apply(pos));
                }
            }

            if (ObjLongLinearProbingHashMap.this.withNullKey) action.accept(supplier.apply(capL));
        }

        public void forEach(Consumer<? super ObjLongMap.Entry<K>> action) {
            this.forEachProxy(action, Node::new);
        }

        public void viewForEach(Consumer<? super ObjLongMap.Entry<K>> action) {
            this.forEachProxy(action, this.tmpEntry::update);
        }

        public int capacity() {
            return ObjLongLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjLongLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return ObjLongLinearProbingHashMap.this.size;
        }

        public Iterator<ObjLongMap.Entry<K>> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<ObjLongMap.Entry<K>> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<ObjLongMap.Entry<K>> viewIterator() {
            return new MapEntryViewIterator();
        }

        public Spliterator<ObjLongMap.Entry<K>> viewSpliterator() {
            return new MapEntryViewSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjLongLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjLongLinearProbingHashMap.this.clear(newCapacity);
        }
    }
}
