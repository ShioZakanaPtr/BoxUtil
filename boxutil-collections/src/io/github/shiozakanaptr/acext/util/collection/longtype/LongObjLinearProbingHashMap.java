package io.github.shiozakanaptr.acext.util.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongObjMap;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSet;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSpliterator;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjCollection;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjSet;
import io.github.shiozakanaptr.acext.util.function.longtype.LongObjBiConsumer;
import io.github.shiozakanaptr.acext.util.function.longtype.LongObjBiFunction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "deprecation", "RedundantCast"})
public class LongObjLinearProbingHashMap<V> implements LongObjMap<V>, HashContainer<Long>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static long NULL_SLOT = (long) 0;

    protected transient boolean withNullKey = false;
    protected transient int remaining = 0;
    protected transient int capacity;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected V defVal = null;
    protected final HashBehavior<Long> hashBehavior;
    protected transient long[] keyTable;
    protected transient V[] valueTable;

    protected transient LongSet keySet = null;
    protected transient CollectionExt<V> valueCollection = null;
    protected transient EntrySetExt<Entry<V>> entrySet = null;

    protected long[] newKeyTable(int cap) {
        return new long[cap];
    }

    protected V[] newValueTable(int cap) {
        return (V[]) new Object[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongObjLinearProbingHashMap(int initialCapacity, float loadFactor, final HashBehavior<Long> hashBehavior) {
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
    public LongObjLinearProbingHashMap(int initialCapacity, final HashBehavior<Long> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public LongObjLinearProbingHashMap(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public LongObjLinearProbingHashMap() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongObjLinearProbingHashMap(final Map<? extends Long, ? extends V> source, float loadFactor, final HashBehavior<Long> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.putAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongObjLinearProbingHashMap(final Map<? extends Long, ? extends V> source,final HashBehavior<Long> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public LongObjLinearProbingHashMap(final Map<? extends Long, ? extends V> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor), newTargetCap = InternalCollectionUtil.getCapacityPOT(newCapacity);
        InternalCollectionUtil.checkHashCapacity(newTargetCap);
        if (newTargetCap == this.capacity) return;

        final int newCap = Math.max(minSize, Math.max(newTargetCap, 2)),
                newMask = newCap - 1, fullCap = newCap + 1, oldCap;
        final long[] newKey = this.newKeyTable(fullCap), oldKey = this.keyTable;
        final V[] newValue = this.newValueTable(fullCap), oldValue = this.valueTable;

        if (this.size > 0) {
            oldCap = this.capacity;
            for (int i = 0; i < oldCap; i++) {
                final long currKey;
                if ((currKey = oldKey[i]) != ((long) NULL_SLOT)) {
                    int pos = this.hashBehavior.hashing(currKey) & newMask;
                    while (newKey[pos] != ((long) NULL_SLOT)) {
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

    protected V removeNull(final long[] keyL) {
        final int capL = this.capacity;
        final V[] valueL = this.valueTable;
        final V result = valueL[capL];
        this.withNullKey = false;
        keyL[capL] = ((long) NULL_SLOT);
        valueL[capL] = null;
        this.size--;
        this.remaining++;
        return result;
    }

    protected V removeAndShift(final long[] keyL, int currPos) {
        final V[] valueL = this.valueTable;
        final V result = valueL[currPos];
        keyL[currPos] = ((long) NULL_SLOT);
        valueL[currPos] = null;
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        long currKey;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currKey = keyL[nextPos = (nextPos + 1) & maskL]) != ((long) NULL_SLOT); dist++) {
            if (((nextPos - (this.hashBehavior.hashing(currKey) & maskL)) & maskL) >= dist) {
                keyL[holePos] = currKey;
                keyL[nextPos] = ((long) NULL_SLOT);
                valueL[holePos] = valueL[nextPos];
                valueL[nextPos] = null;
                holePos = nextPos;
                dist = 0;
            }
        }
        return result;
    }

    protected void removeAt(final long[] keyL, int pos) {
        if (pos != this.capacity) this.removeAndShift(keyL, pos); else this.removeNull(keyL);
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.capacity));
    }

    protected int find(final long key) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(key, ((long) NULL_SLOT))) {
            return this.withNullKey ? this.capacity : ~this.capacity;
        }

        final int maskL = this.posMask;
        final long[] keyL = this.keyTable;
        int pos = behavior.hashing(key);
        for (long currKey; (currKey = keyL[pos &= maskL]) != 0; pos++) {
            if (behavior.equals(key, currKey)) {
                return pos;
            }
        }
        return ~pos;
    }

    protected V insert(int pos, final long key, final V value) {
        final int realPos = ~pos;
        if (realPos == this.capacity) this.withNullKey = true;
        this.keyTable[realPos] = key;
        this.valueTable[realPos] = value;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return value;
    }

    public V get(long key) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(key, ((long) NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : this.defVal;

        final int maskL = this.posMask;
        final long[] keyL = this.keyTable;
        long currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.valueTable[pos];
        }
        return this.defVal;
    }

    public V getOrDefault(long key, V defaultValue) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(key, ((long) NULL_SLOT))) return this.withNullKey ? this.valueTable[this.capacity] : defaultValue;

        final int maskL = this.posMask;
        final long[] keyL = this.keyTable;
        long currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.valueTable[pos];
        }
        return defaultValue;
    }

    public V put(long key, V value) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(key, ((long) NULL_SLOT))) {
            final V[] valueL = this.valueTable;
            final V result;
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
        final long[] keyL = this.keyTable;
        int pos = behavior.hashing(key);
        for (long currKey; (currKey = keyL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                final V[] valueL;
                final V oldValue = (valueL = this.valueTable)[pos];
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

    public V putIfAbsent(long key, V value) {
        final int pos;
        if ((pos = this.find(key)) < 0) {
            this.insert(pos, key, value);
            return this.defVal;
        }
        final V[] valueL;
        if ((valueL = this.valueTable)[pos] != null) return valueL[pos];
        valueL[pos] = value;
        return null;
    }

    public V remove(long key) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(key, ((long) NULL_SLOT))) {
            if (this.withNullKey) return this.removeNull(this.keyTable);
            return this.defVal;
        }

        final int maskL = this.posMask;
        final long[] keyL = this.keyTable;
        long currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return this.removeAndShift(keyL, pos);
        }
        return this.defVal;
    }

    public boolean remove(long key, Object value) {
        final int pos;
        if ((pos = this.find(key)) > -1 && Objects.equals(value, this.valueTable[pos])) {
            this.removeAt(this.keyTable, pos);
            return true;
        }
        return false;
    }

    public V replace(long key, V value) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(key, ((long) NULL_SLOT))) {
            if (this.withNullKey) {
                final V[] valueL;
                final V result = (valueL = this.valueTable)[this.capacity];
                valueL[this.capacity] = value;
                return result;
            }
            return this.defVal;
        }

        final int maskL = this.posMask;
        final long[] keyL = this.keyTable;
        long currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= this.posMask]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) {
                final V[] valueL;
                final V result = (valueL = this.valueTable)[pos];
                valueL[pos] = value;
                return result;
            }
        }
        return this.defVal;
    }

    public boolean replace(long key, V oldValue, V newValue) {
        final int pos;
        final V[] valueL = this.valueTable;
        if ((pos = this.find(key)) > -1 && Objects.equals(oldValue, valueL[pos])) {
            valueL[pos] = newValue;
            return true;
        }
        return false;
    }

    public V computeIfAbsent(long key, LongFunction<? extends V> mappingFunction) {
        final int pos;
        final V[] valueL = this.valueTable;
        final V currValue;
        if ((pos = this.find(key)) > -1 && (currValue = valueL[pos]) != null) return currValue;

        final V newValue = mappingFunction.apply(key);
        if (newValue != null) return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);
        return this.defVal;
    }

    public V computeIfPresent(long key, LongObjBiFunction<? super V, ? extends V> remappingFunction) {
        final int pos;
        final V[] valueL;
        if ((pos = this.find(key)) < 0 || (valueL = this.valueTable)[pos] == null) return this.defVal;

        final V newValue = remappingFunction.apply(key, valueL[pos]);
        if (newValue != null) return valueL[pos] = newValue;
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public V compute(long key, LongObjBiFunction<? super V, ? extends V> remappingFunction) {
        final int pos;
        final V[] valueL = this.valueTable;
        final V newValue = remappingFunction.apply(key, (pos = this.find(key)) < 0 ? null : valueL[pos]);
        if (newValue != null) return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);

        if (pos >= 0) this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public V merge(long key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.insert(pos, key, value);

        final V[] valueL;
        final V oldValue,
                newValue = (oldValue = (valueL = this.valueTable)[pos]) != null ? remappingFunction.apply(oldValue, value) : value;
        if (newValue != null) return valueL[pos] = newValue;
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public V update(long key, Function<? super V, ? extends V> updateFunction) {
        final int pos;
        final V[] valueL = this.valueTable;
        final V newValue = updateFunction.apply((pos = this.find(key)) < 0 ? this.defVal : valueL[pos]);
        return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);
    }

    public V remapping(long key, LongObjBiFunction<? super V, ? extends Long> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.defVal;

        final V[] valueL;
        final V currValue;
        final Long newKey = remappingFunction.apply(key, currValue = (valueL = this.valueTable)[pos]);
        final long rawKey = newKey != null ? newKey : 0;
        final boolean sameKey = this.hashBehavior.equals(rawKey, key);

        final long[] keyL = this.keyTable;
        if (newKey == null || !sameKey) this.removeAt(keyL, pos);
        if (newKey == null) return this.defVal;
        if (sameKey) return currValue;

        final int newPos;
        if ((newPos = this.find(rawKey)) < 0) return this.insert(newPos, rawKey, currValue);

        final V newValue = mergeFunction.apply(valueL[newPos], currValue);
        if (newValue != null) return valueL[newPos] = newValue;
        this.removeAt(keyL, newPos);
        return this.defVal;
    }

    public void swap(Map<Long, V> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof LongObjLinearProbingHashMap) {
            final LongObjLinearProbingHashMap<V> src = (LongObjLinearProbingHashMap<V>) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                this.clearMappingCollection();
                src.clearMappingCollection();

                final boolean currWithNull = this.withNullKey, srcWithNull = src.withNullKey;
                final int currCap = this.capacity, srcCap = src.capacity,
                        currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final long[] currKey = this.keyTable, srcKey = src.keyTable;
                final V[] currValue = this.valueTable, srcValue = src.valueTable;

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
            final Map<Long, V> tmp = new LongObjLinearProbingHashMap<>(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<Long, V> tmp = new LongObjLinearProbingHashMap<>(this);
            this.clear();
            this.putAll(source);
            source.clear();
            source.putAll(tmp);
        }
    }

    public boolean swapAt(long from, long to) {
        final int posA = this.find(from);
        if (posA < 0) return false;
        final int posB = this.find(to);
        if (posB < 0) return false;

        final V[] valueL = this.valueTable;
        final V tmpValue = valueL[posA];
        valueL[posA] = valueL[posB];
        valueL[posB] = tmpValue;
        return false;
    }

    public boolean containsKey(long key) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(key, ((long) NULL_SLOT))) return this.withNullKey;

        final int maskL = this.posMask;
        final long[] keyL = this.keyTable;
        long currKey;
        for (int pos = behavior.hashing(key); (currKey = keyL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(key, currKey)) return true;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        if (this.isEmpty()) return false;

        final int capL = this.capacity;
        final long[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            if (keyL[pos] != ((long) NULL_SLOT) && Objects.equals(value, valueL[pos])) return true;
        }

        return this.withNullKey && Objects.equals(value, valueL[capL]);
    }

    public void forEach(LongObjBiConsumer<? super V> action) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final long[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final long currKey;
            if ((currKey = keyL[pos]) != ((long) NULL_SLOT)) {
                action.accept(currKey, valueL[pos]);
            }
        }

        if (this.withNullKey) action.accept(keyL[capL], valueL[capL]);
    }

    public void replaceAll(LongObjBiFunction<? super V, ? extends V> function) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final long[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final long currKey;
            if ((currKey = keyL[pos]) != ((long) NULL_SLOT)) {
                valueL[pos] = function.apply(currKey, valueL[pos]);
            }
        }

        if (this.withNullKey) {
            valueL[capL] = function.apply(keyL[capL], valueL[capL]);
        }
    }

    public void putAll(Map<? extends Long, ? extends V> m) {
        if (m == this || m.isEmpty()) return;
        if (m.size() >= this.remaining()) this.reserve(this.size + m.size() + 1);
        if (m instanceof LongObjLinearProbingHashMap<?>) {
            final LongObjLinearProbingHashMap<?> cast = (LongObjLinearProbingHashMap<?>) m;
            final int capL = cast.capacity;
            final long[] keyL = cast.keyTable;
            final V[] valueL = (V[]) cast.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                final long currKey;
                if ((currKey = keyL[pos]) != ((long) NULL_SLOT)) {
                    this.put(currKey, valueL[pos]);
                }
            }

            if (cast.withNullKey) this.put(keyL[capL], valueL[capL]);
        } else m.forEach(this::put);
    }

    private void calculateRemaining() {
        this.remaining = InternalCollectionUtil.getHashTableMaxRemaining(this.capacity, this.loadFactor, true) - this.size;
    }

    public V getDefaultReturnValue() {
        return this.defVal;
    }

    public V setDefaultReturnValue(V newDefault) {
        final V oldDef = this.defVal;
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

    public HashBehavior<Long> getBehavior() {
        return this.hashBehavior;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public LongSet keySet() {
        LongSet result;
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

    public EntrySetExt<LongObjMap.Entry<V>> entrySetRaw() {
        EntrySetExt<LongObjMap.Entry<V>> result;
        if ((result = this.entrySet) == null) {
            result = this.entrySet = new MapEntrySet();
        }
        return result;
    }

    public long[] keyTable() {
        return this.keyTable;
    }

    public V[] valueTable() {
        return this.valueTable;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        this.clearMappingCollection();
        Arrays.fill(this.keyTable, ((long) NULL_SLOT));
        Arrays.fill(this.valueTable, null);
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
        final long[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final long currKey;
            if ((currKey = keyL[pos]) != ((long) NULL_SLOT)) {
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
                if (castMap instanceof LongObjLinearProbingHashMap<?>) {
                    final LongObjLinearProbingHashMap<?> cast = (LongObjLinearProbingHashMap<?>) castMap;
                    if (this.withNullKey != cast.withNullKey) return false;
                    final long[] currKeyL = this.keyTable, srcKeyL = cast.keyTable;
                    final V[] currValueL = this.valueTable, srcValueL = (V[]) cast.valueTable;

                    final int capacity = srcKeyL.length;
                    for (int srcPos = 0; srcPos < capacity; srcPos++) {
                        final long srcKey;
                        if ((srcKey = srcKeyL[srcPos]) != ((long) NULL_SLOT)) {
                            final int currPos = this.find(srcKey);
                            if (currPos < 0) return false;

                            final V currValue = currValueL[currPos], srcValue = srcValueL[srcPos];
                            if (srcValue == null && currValue != null || srcValue != null && !srcValue.equals(currValue)) return false;
                        }
                    }
                    if (this.withNullKey) return currKeyL[this.capacity] == srcKeyL[cast.capacity] && Objects.equals(currValueL[this.capacity], srcValueL[cast.capacity]);
                } else {
                    final V[] currValueL = this.valueTable;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final Long srcKey = (Long) entry.getKey();
                        final V srcValue = (V) entry.getValue();
                        final int currPos = this.find(srcKey);
                        if (currPos < 0) return false;

                        final V currValue = currValueL[currPos];
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

        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        final int capL = this.capacity;
        final long[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final long currKey;
            if ((currKey = keyL[pos]) != ((long) NULL_SLOT)) {
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

    protected void clearMappingCollection() {
        this.keySet = null;
        this.valueCollection = null;
        this.entrySet = null;
    }

    public Object clone() {
        LongObjLinearProbingHashMap<V> result;
        try {
            result = (LongObjLinearProbingHashMap<V>) super.clone();
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
        final long[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final long currKey;
            if ((currKey = keyL[pos]) != ((long) NULL_SLOT)) {
                out.writeLong(currKey);
                out.writeObject(valueL[pos]);
            }
        }
        if (this.withNullKey) {
            out.writeLong(keyL[capL]);
            out.writeObject(valueL[capL]);
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
        final long[] keyL = this.keyTable = this.newKeyTable(fullCap);
        final V[] valueL = this.valueTable = this.newValueTable(fullCap);
        this.withNullKey = false;

        final HashBehavior<Long> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final long currKey = in.readLong();
            final V currValue = (V) in.readObject();
            int pos;
            if (behavior.equals(currKey, ((long) NULL_SLOT))) {
                if (this.withNullKey) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                pos = this.capacity;
                this.withNullKey = true;
            } else {
                pos = behavior.hashing(currKey) & maskL;
                long findKey;
                while ((findKey = keyL[pos]) != ((long) NULL_SLOT)) {
                    if (behavior.equals(currKey, findKey)) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                    pos = (pos + 1) & maskL;
                }
            }
            keyL[pos] = currKey;
            valueL[pos] = currValue;
        }
        this.calculateRemaining();
    }

    protected long[] prepareArray(final long[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return this.newKeyTable(sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected long[] keyToArray(final long[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final int capL = this.capacity;
        final long[] keyL = this.keyTable;
        for (int pos = 0; pos < capL;) {
            final long currKey;
            if ((currKey = keyL[pos++]) != ((long) NULL_SLOT)) a[idx++] = currKey;
        }

        if (this.withNullKey) a[idx] = keyL[capL];
        return a;
    }

    protected class Node implements LongObjMap.Entry<V> {
        protected int pos;

        protected Node update(int pos) {
            this.pos = pos;
            return this;
        }

        protected Node() {}

        protected Node(int pos) {
            this.pos = pos;
        }

        public long getKeyRaw() {
            return LongObjLinearProbingHashMap.this.keyTable[this.pos];
        }

        public V getValue() {
            return LongObjLinearProbingHashMap.this.valueTable[this.pos];
        }

        public V setValue(V newValue) {
            final int posL = this.pos;
            final V[] valueL = LongObjLinearProbingHashMap.this.valueTable;
            final V oldValue = valueL[posL];
            valueL[posL] = newValue;
            return oldValue;
        }

        public String toString() {
            final int posL = this.pos;
            return LongObjLinearProbingHashMap.this.keyTable[posL] + "=" + LongObjLinearProbingHashMap.this.valueTable[posL];
        }

        public int hashCode() {
            final int posL = this.pos;
            return Long.hashCode(LongObjLinearProbingHashMap.this.keyTable[posL]) ^ Objects.hashCode(LongObjLinearProbingHashMap.this.valueTable[posL]);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            final int posL = this.pos;
            return Objects.equals(LongObjLinearProbingHashMap.this.keyTable[posL], cast.getKey())
                    && Objects.equals(LongObjLinearProbingHashMap.this.valueTable[posL], cast.getValue());
        }
    }

    protected abstract class MapIterator<C> {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected MapIterator() {
            if (LongObjLinearProbingHashMap.this.size > 0) {
                this.scanPos(LongObjLinearProbingHashMap.this.keyTable, 0);
            }
        }

        protected void scanPos(final long[] keyL, int start) {
            final int capL = LongObjLinearProbingHashMap.this.capacity;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (keyL[i] != ((long) NULL_SLOT)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
            }
            if (LongObjLinearProbingHashMap.this.withNullKey) {
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

            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            if (posL > -1) {
                final int capL = LongObjLinearProbingHashMap.this.capacity;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (keyL[i] != ((long) NULL_SLOT)) {
                        this.posNext = i;
                        break;
                    }
                }
                if (i == capL && LongObjLinearProbingHashMap.this.withNullKey) {
                    this.posNext = i;
                }
            }
            return current;
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            if (posPreL == LongObjLinearProbingHashMap.this.capacity) {
                LongObjLinearProbingHashMap.this.removeNull(LongObjLinearProbingHashMap.this.keyTable);
                this.pos = -1;
            } else {
                final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
                LongObjLinearProbingHashMap.this.removeAndShift(keyL, posPreL);
                this.scanPos(keyL, posPreL);
            }
            this.posPre = -1;
        }

        protected abstract void consumerProxy(C action, long key, int pos);

        public void forEachRemaining(C action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            final int capL = LongObjLinearProbingHashMap.this.capacity;
            int i, findPre = this.posPre;
            for (i = current; i < capL; i++) {
                final long currKey;
                if ((currKey = keyL[i]) != ((long) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, findPre = i);
                }
            }
            if (i == capL && LongObjLinearProbingHashMap.this.withNullKey) {
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
                this.estSize = LongObjLinearProbingHashMap.this.size;
                newLimit = this.limitCap = LongObjLinearProbingHashMap.this.capacity;
            }
            return newLimit;
        }

        protected abstract S splitProxy(int index, int limitCap, int estSize);

        public S trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? this.splitProxy(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        protected abstract void consumerProxy(C action, long key, int pos);

        public boolean tryAdvance(C action) {
            final int currLimit = this.getLimitCap();

            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final long currKey;
                if ((currKey = keyL[idxL]) != ((long) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, idxL);
                    this.index = idxL + 1;
                    return true;
                }
            }
            final int capL = LongObjLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && LongObjLinearProbingHashMap.this.withNullKey) {
                this.consumerProxy(action, keyL[capL], capL);
                this.index = capL + 1;
                return true;
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(C action) {
            final int currLimit = this.getLimitCap();

            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final long currKey;
                if ((currKey = keyL[idxL]) != ((long) NULL_SLOT)) this.consumerProxy(action, currKey, idxL);
            }
            final int capL = LongObjLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && LongObjLinearProbingHashMap.this.withNullKey) {
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
            return (this.limitCap < 0 || this.estSize == LongObjLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class KeyIterator extends MapIterator<LongConsumer> implements LongIterator {
        public long nextLong() {
            return LongObjLinearProbingHashMap.this.keyTable[this.nextPos()];
        }

        protected void consumerProxy(LongConsumer action, long key, int pos) {
            action.accept(key);
        }
    }

    protected class ValueIterator extends MapIterator<Consumer<? super V>> implements Iterator<V> {
        public V next() {
            return LongObjLinearProbingHashMap.this.valueTable[this.nextPos()];
        }

        protected void consumerProxy(Consumer<? super V> action, long key, int pos) {
            action.accept(LongObjLinearProbingHashMap.this.valueTable[pos]);
        }
    }

    protected class MapEntryIterator extends MapIterator<Consumer<? super LongObjMap.Entry<V>>> implements Iterator<LongObjMap.Entry<V>> {
        public LongObjMap.Entry<V> next() {
            return new Node(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super Entry<V>> action, long key, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewIterator extends MapIterator<Consumer<? super LongObjMap.Entry<V>>> implements Iterator<LongObjMap.Entry<V>> {
        protected final Node tmpEntry = new Node();

        public LongObjMap.Entry<V> next() {
            return this.tmpEntry.update(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super Entry<V>> action, long key, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }
    }

    protected class KeySpliterator extends MapSpliterator<LongConsumer, LongSpliterator> implements LongSpliterator {
        protected KeySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected LongSpliterator splitProxy(int index, int limitCap, int estSize) {
            return new KeySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(LongConsumer action, long key, int pos) {
            action.accept(key);
        }
    }

    protected class ValueSpliterator extends MapSpliterator<Consumer<? super V>, Spliterator<V>> implements Spliterator<V> {
        protected ValueSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<V> splitProxy(int index, int limitCap, int estSize) {
            return new ValueSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super V> action, long key, int pos) {
            action.accept(LongObjLinearProbingHashMap.this.valueTable[pos]);
        }

        public int characteristics() {
            return this.limitCap < 0 || this.estSize == LongObjLinearProbingHashMap.this.size ? Spliterator.SIZED : 0;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super LongObjMap.Entry<V>>, Spliterator<LongObjMap.Entry<V>>> implements Spliterator<LongObjMap.Entry<V>> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<LongObjMap.Entry<V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super LongObjMap.Entry<V>> action, long key, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewSpliterator extends MapSpliterator<Consumer<? super LongObjMap.Entry<V>>, Spliterator<LongObjMap.Entry<V>>> implements Spliterator<LongObjMap.Entry<V>> {
        protected final Node tmpEntry = new Node();

        protected MapEntryViewSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<LongObjMap.Entry<V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntryViewSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super LongObjMap.Entry<V>> action, long key, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }
    }

    protected class KeySet extends AbstractLongSet {
        public void resize(int newCapacity) {
            LongObjLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            LongObjLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(long o) {
            return LongObjLinearProbingHashMap.this.containsKey(o);
        }

        public boolean removeElement(long o) {
            final HashBehavior<Long> behavior = LongObjLinearProbingHashMap.this.hashBehavior;
            if (behavior.equals(o, 0)) {
                if (LongObjLinearProbingHashMap.this.withNullKey) {
                    LongObjLinearProbingHashMap.this.removeNull(LongObjLinearProbingHashMap.this.keyTable);
                    return true;
                }
                return false;
            }

            final int maskL = LongObjLinearProbingHashMap.this.posMask;
            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            long currKey;
            for (int pos = behavior.hashing(o); (currKey = keyL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
                if (behavior.equals(o, currKey)) {
                    LongObjLinearProbingHashMap.this.removeAndShift(keyL, pos);
                    return true;
                }
            }
            return false;
        }

        public void forEach(LongConsumer action) {
            if (LongObjLinearProbingHashMap.this.isEmpty()) return;

            final int capL = LongObjLinearProbingHashMap.this.capacity;
            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            long currKey;
            for (int pos = 0; pos < capL;) {
                if ((currKey = keyL[pos++]) != ((long) NULL_SLOT)) {
                    action.accept(currKey);
                }
            }

            if (LongObjLinearProbingHashMap.this.withNullKey) action.accept(keyL[capL]);
        }

        public int capacity() {
            return LongObjLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return LongObjLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return LongObjLinearProbingHashMap.this.size;
        }

        public LongIterator iterator() {
            return new KeyIterator();
        }

        public LongSpliterator spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            LongObjLinearProbingHashMap.this.clear();
        }
        
        public void clear(int newCapacity) {
            LongObjLinearProbingHashMap.this.clear(newCapacity);
        }

        public long[] toArrayRaw() {
            return LongObjLinearProbingHashMap.this.keyToArray(LongObjLinearProbingHashMap.this.newKeyTable(LongObjLinearProbingHashMap.this.size));
        }

        public long[] toArray(@NotNull long[] a) {
            return LongObjLinearProbingHashMap.this.keyToArray(LongObjLinearProbingHashMap.this.prepareArray(a));
        }
    }

    protected class ValueCollection extends AbstractObjCollection<V> {
        public void resize(int newCapacity) {
            LongObjLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            LongObjLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return LongObjLinearProbingHashMap.this.containsValue(o);
        }

        public void forEach(Consumer<? super V> action) {
            if (LongObjLinearProbingHashMap.this.isEmpty()) return;

            final int capL = LongObjLinearProbingHashMap.this.capacity;
            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            final V[] valueL = LongObjLinearProbingHashMap.this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((long) NULL_SLOT)) {
                    action.accept(valueL[pos]);
                }
            }

            if (LongObjLinearProbingHashMap.this.withNullKey) action.accept(valueL[capL]);
        }

        public int capacity() {
            return LongObjLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return LongObjLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return LongObjLinearProbingHashMap.this.size;
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            LongObjLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            LongObjLinearProbingHashMap.this.clear(newCapacity);
        }
    }

    protected class MapEntrySet extends AbstractObjSet<LongObjMap.Entry<V>> implements EntrySetExt<LongObjMap.Entry<V>> {
        protected final Node tmpEntry = new Node();

        public void resize(int newCapacity) {
            LongObjLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            LongObjLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return LongObjLinearProbingHashMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return LongObjLinearProbingHashMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        protected void forEachProxy(Consumer<? super Entry<V>> action, IntFunction<LongObjMap.Entry<V>> supplier) {
            if (LongObjLinearProbingHashMap.this.isEmpty()) return;

            final int capL = LongObjLinearProbingHashMap.this.capacity;
            final long[] keyL = LongObjLinearProbingHashMap.this.keyTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((long) NULL_SLOT)) {
                    action.accept(supplier.apply(pos));
                }
            }

            if (LongObjLinearProbingHashMap.this.withNullKey) action.accept(supplier.apply(capL));
        }

        public void forEach(Consumer<? super Entry<V>> action) {
            this.forEachProxy(action, Node::new);
        }

        public void viewForEach(Consumer<? super Entry<V>> action) {
            this.forEachProxy(action, this.tmpEntry::update);
        }

        public int capacity() {
            return LongObjLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return LongObjLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return LongObjLinearProbingHashMap.this.size;
        }

        public Iterator<LongObjMap.Entry<V>> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<LongObjMap.Entry<V>> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<LongObjMap.Entry<V>> viewIterator() {
            return new MapEntryViewIterator();
        }

        public Spliterator<LongObjMap.Entry<V>> viewSpliterator() {
            return new MapEntryViewSpliterator(0, -1, 0);
        }

        public void clear() {
            LongObjLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            LongObjLinearProbingHashMap.this.clear(newCapacity);
        }
    }
}
