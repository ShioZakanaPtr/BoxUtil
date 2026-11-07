package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.*;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjObjMap;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
public class ObjObjLinearProbingHashMap<K, V> implements ObjObjMap<K, V>, HashContainer<K>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static Object NULL_SLOT = null;

    protected transient boolean withNullKey = false;
    protected transient int remaining = 0;
    protected transient int capacity;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected V defVal = null;
    protected final HashBehavior<K> hashBehavior;
    protected transient K[] keyTable;
    protected transient V[] valueTable;

    protected transient SetExt<K> keySet = null;
    protected transient CollectionExt<V> valueCollection = null;
    protected transient EntrySetExt<Entry<K, V>> entrySet = null;

    protected K[] newKeyTable(int cap) {
        return (K[]) new Object[cap];
    }

    protected V[] newValueTable(int cap) {
        return (V[]) new Object[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjLinearProbingHashMap(int initialCapacity, float loadFactor, final HashBehavior<K> hashBehavior) {
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
    public ObjObjLinearProbingHashMap(int initialCapacity, final HashBehavior<K> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjObjLinearProbingHashMap(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public ObjObjLinearProbingHashMap() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjLinearProbingHashMap(final Map<? extends K, ? extends V> source, float loadFactor, final HashBehavior<K> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.putAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjLinearProbingHashMap(final Map<? extends K, ? extends V> source, final HashBehavior<K> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public ObjObjLinearProbingHashMap(final Map<? extends K, ? extends V> source) {
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
        final V[] newValue = this.newValueTable(fullCap), oldValue = this.valueTable;

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

    protected V removeNull(final K[] keyL) {
        final int capL = this.capacity;
        final V[] valueL = this.valueTable;
        final V result = valueL[capL];
        this.withNullKey = false;
        keyL[capL] = ((K) NULL_SLOT);
        valueL[capL] = null;
        this.size--;
        this.remaining++;
        return result;
    }

    protected V removeAndShift(final K[] keyL, int currPos) {
        final V[] valueL = this.valueTable;
        final V result = valueL[currPos];
        keyL[currPos] = ((K) NULL_SLOT);
        valueL[currPos] = null;
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        K currKey;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currKey = keyL[nextPos = (nextPos + 1) & maskL]) != ((K) NULL_SLOT); dist++) {
            if (((nextPos - (this.hashBehavior.hashing(currKey) & maskL)) & maskL) >= dist) {
                keyL[holePos] = currKey;
                keyL[nextPos] = ((K) NULL_SLOT);
                valueL[holePos] = valueL[nextPos];
                valueL[nextPos] = null;
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

    protected V insert(int pos, final K key, final V value) {
        final int realPos = ~pos;
        if (realPos == this.capacity) this.withNullKey = true;
        this.keyTable[realPos] = key;
        this.valueTable[realPos] = value;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return value;
    }

    public V get(Object key) {
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

    public V getOrDefault(Object key, V defaultValue) {
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

    public V put(K key, V value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) {
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
        final K[] keyL = this.keyTable;
        int pos = behavior.hashing(key);
        for (K currKey; (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
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

    public V putIfAbsent(K key, V value) {
        final int pos;
        if ((pos = this.find(key)) < 0) {
            this.insert(pos, key, value);
            return this.defVal;
        }
        final V[] valueL;
        if ((valueL = this.valueTable)[pos] != ((K) NULL_SLOT)) return valueL[pos];
        valueL[pos] = value;
        return null;
    }

    public V remove(Object key) {
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

    public boolean remove(Object key, Object value) {
        final int pos;
        if ((pos = this.find(key)) > -1 && Objects.equals(value, this.valueTable[pos])) {
            this.removeAt(this.keyTable, pos);
            return true;
        }
        return false;
    }

    public V replace(K key, V value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        if (behavior.equals(key, ((K) NULL_SLOT))) {
            if (this.withNullKey) {
                final V[] valueL;
                final V result = (valueL = this.valueTable)[this.capacity];
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
                final V[] valueL;
                final V result = (valueL = this.valueTable)[pos];
                valueL[pos] = value;
                return result;
            }
        }
        return this.defVal;
    }

    public boolean replace(K key, V oldValue, V newValue) {
        final int pos;
        final V[] valueL = this.valueTable;
        if ((pos = this.find(key)) > -1 && Objects.equals(oldValue, valueL[pos])) {
            valueL[pos] = newValue;
            return true;
        }
        return false;
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        final int pos;
        final V[] valueL = this.valueTable;
        final V currValue;
        if ((pos = this.find(key)) > -1 && (currValue = valueL[pos]) != null) return currValue;

        final V newValue = mappingFunction.apply(key);
        if (newValue != null) return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);
        return this.defVal;
    }

    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final int pos;
        final V[] valueL;
        if ((pos = this.find(key)) < 0 || (valueL = this.valueTable)[pos] == null) return this.defVal;

        final V newValue = remappingFunction.apply(key, valueL[pos]);
        if (newValue != null) return valueL[pos] = newValue;
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final int pos = this.find(key);
        final V[] valueL = this.valueTable;
        final V newValue = remappingFunction.apply(key, pos < 0 ? null : valueL[pos]);
        if (newValue != null) return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);

        if (pos >= 0) this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.insert(pos, key, value);

        final V[] valueL;
        final V oldValue,
                newValue = (oldValue = (valueL = this.valueTable)[pos]) != null ? remappingFunction.apply(oldValue, value) : value;
        if (newValue != null) return valueL[pos] = newValue;
        this.removeAt(this.keyTable, pos);
        return this.defVal;
    }

    public V update(K key, Function<? super V, ? extends V> updateFunction) {
        final int pos = this.find(key);
        final V[] valueL = this.valueTable;
        final V newValue = updateFunction.apply(pos < 0 ? this.defVal : valueL[pos]);
        return pos < 0 ? this.insert(pos, key, newValue) : (valueL[pos] = newValue);
    }

    public V remapping(K key, BiFunction<? super K, ? super V, ? extends K> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        final int pos;
        if ((pos = this.find(key)) < 0) return this.defVal;

        final V[] valueL;
        final V currValue;
        final K newKey = remappingFunction.apply(key, currValue = (valueL = this.valueTable)[pos]);
        final boolean sameKey = this.hashBehavior.equals(newKey, key);

        final K[] keyL = this.keyTable;
        if (newKey == null || !sameKey) this.removeAt(keyL, pos);
        if (newKey == null) return this.defVal;
        if (sameKey) return currValue;

        final int newPos;
        if ((newPos = this.find(newKey)) < 0) return this.insert(newPos, newKey, currValue);

        final V newValue = mergeFunction.apply(valueL[newPos], currValue);
        if (newValue != null) return valueL[newPos] = newValue;
        this.removeAt(keyL, newPos);
        return this.defVal;
    }

    public void swap(Map<K, V> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ObjObjLinearProbingHashMap) {
            final ObjObjLinearProbingHashMap<K, V> src = (ObjObjLinearProbingHashMap<K, V>) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                this.clearMappingCollection();
                src.clearMappingCollection();

                final boolean currWithNull = this.withNullKey, srcWithNull = src.withNullKey;
                final int currCap = this.capacity, srcCap = src.capacity,
                        currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final K[] currKey = this.keyTable, srcKey = src.keyTable;
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
            final Map<K, V> tmp = new ObjObjLinearProbingHashMap<>(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<K, V> tmp = new ObjObjLinearProbingHashMap<>(this);
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

        final V[] valueL = this.valueTable;
        final V tmpValue = valueL[posA];
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

    public boolean containsValue(Object value) {
        if (this.isEmpty()) return false;

        final int capL = this.capacity;
        final K[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            if (keyL[pos] != ((K) NULL_SLOT) && Objects.equals(value, valueL[pos])) return true;
        }

        return this.withNullKey && Objects.equals(value, valueL[capL]);
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final K[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                action.accept(currKey, valueL[pos]);
            }
        }

        if (this.withNullKey) action.accept(keyL[capL], valueL[capL]);
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final K[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                valueL[pos] = function.apply(currKey, valueL[pos]);
            }
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == this || m.isEmpty()) return;
        if (m.size() >= this.remaining()) this.reserve(this.size + m.size() + 1);
        if (m instanceof ObjObjLinearProbingHashMap<?, ?>) {
            final ObjObjLinearProbingHashMap<?, ?> cast = (ObjObjLinearProbingHashMap<?, ?>) m;
            final int capL = cast.capacity;
            final K[] keyL = (K[]) cast.keyTable;
            final V[] valueL = (V[]) cast.valueTable;
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

    public CollectionExt<V> values() {
        CollectionExt<V> result;
        if ((result = this.valueCollection) == null) {
            result = this.valueCollection = new ValueCollection();
        }
        return result;
    }

    public EntrySetExt<ObjObjMap.Entry<K, V>> entrySet() {
        EntrySetExt<ObjObjMap.Entry<K, V>> result;
        if ((result = this.entrySet) == null) {
            result = this.entrySet = new MapEntrySet();
        }
        return result;
    }

    public K[] keyTable() {
        return this.keyTable;
    }

    public V[] valueTable() {
        return this.valueTable;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        this.clearMappingCollection();
        Arrays.fill(this.keyTable, ((K) NULL_SLOT));
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
        final K[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                result += Objects.hashCode(currKey) ^ Objects.hashCode(valueL[pos]);
            }
        }

        if (this.withNullKey) result += Objects.hashCode(keyL[capL]) ^ Objects.hashCode(valueL[capL]);
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Map<?, ?>) {
            final Map<?, ?> castMap = (Map<?, ?>) o;
            if (castMap.size() != this.size()) return false;
            try {
                if (castMap instanceof ObjObjLinearProbingHashMap<?, ?>) {
                    final ObjObjLinearProbingHashMap<?, ?> cast = (ObjObjLinearProbingHashMap<?, ?>) castMap;
                    if (this.withNullKey != cast.withNullKey) return false;
                    final K[] currKeyL = this.keyTable, srcKeyL = (K[]) cast.keyTable;
                    final V[] currValueL = this.valueTable, srcValueL = (V[]) cast.valueTable;

                    final int capacity = srcKeyL.length;
                    for (int srcPos = 0; srcPos < capacity; srcPos++) {
                        final K srcKey;
                        if ((srcKey = srcKeyL[srcPos]) != ((K) NULL_SLOT)) {
                            final int currPos = this.find(srcKey);
                            if (currPos < 0) return false;

                            final V currValue = currValueL[currPos], srcValue = srcValueL[srcPos];
                            if (srcValue == null && currValue != null || srcValue != null && !srcValue.equals(currValue)) return false;
                        }
                    }
                    if (this.withNullKey) return Objects.equals(currKeyL[this.capacity], srcKeyL[cast.capacity]) && Objects.equals(currValueL[this.capacity], srcValueL[cast.capacity]);
                } else {
                    final V[] currValueL = this.valueTable;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final K srcKey = (K) entry.getKey();
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
        final K[] keyL = this.keyTable;
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                final V currValue;
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currKey != this ? currKey : "(this Map)")
                        .append('=')
                        .append((currValue = valueL[pos]) != this ? currValue : "(this Map)");
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
        ObjObjLinearProbingHashMap<K, V> result;
        try {
            result = (ObjObjLinearProbingHashMap<K, V>) super.clone();
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
        final V[] valueL = this.valueTable;
        for (int pos = 0; pos < capL; pos++) {
            final K currKey;
            if ((currKey = keyL[pos]) != ((K) NULL_SLOT)) {
                out.writeObject(currKey);
                out.writeObject(valueL[pos]);
            }
        }
        if (this.withNullKey) {
            out.writeObject(keyL[capL]);
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
        final K[] keyL = this.keyTable = this.newKeyTable(fullCap);
        final V[] valueL = this.valueTable = this.newValueTable(fullCap);
        this.withNullKey = false;

        final HashBehavior<K> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final K currKey = (K) in.readObject();
            final V currValue = (V) in.readObject();
            int pos;
            if (behavior.equals(currKey, ((K) NULL_SLOT))) {
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

    protected class Node implements ObjObjMap.Entry<K, V> {
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
            return ObjObjLinearProbingHashMap.this.keyTable[this.pos];
        }

        public V getValue() {
            return ObjObjLinearProbingHashMap.this.valueTable[this.pos];
        }

        public V setValue(V newValue) {
            final int posL = this.pos;
            final V[] valueL = ObjObjLinearProbingHashMap.this.valueTable;
            final V oldValue = valueL[posL];
            valueL[posL] = newValue;
            return oldValue;
        }

        public String toString() {
            final int posL = this.pos;
            return ObjObjLinearProbingHashMap.this.keyTable[posL] + "=" + ObjObjLinearProbingHashMap.this.valueTable[posL];
        }

        public int hashCode() {
            final int posL = this.pos;
            return Objects.hashCode(ObjObjLinearProbingHashMap.this.keyTable[posL]) ^ Objects.hashCode(ObjObjLinearProbingHashMap.this.valueTable[posL]);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            final int posL = this.pos;
            return Objects.equals(ObjObjLinearProbingHashMap.this.keyTable[posL], cast.getKey())
                    && Objects.equals(ObjObjLinearProbingHashMap.this.valueTable[posL], cast.getValue());
        }
    }

    protected abstract class MapIterator<C> {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected MapIterator() {
            if (ObjObjLinearProbingHashMap.this.size > 0) {
                this.scanPos(ObjObjLinearProbingHashMap.this.keyTable, 0);
            }
        }

        protected void scanPos(final K[] keyL, int start) {
            final int capL = ObjObjLinearProbingHashMap.this.capacity;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (keyL[i] != ((K) NULL_SLOT)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
            }
            if (ObjObjLinearProbingHashMap.this.withNullKey) {
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

            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            if (posL > -1) {
                final int capL = ObjObjLinearProbingHashMap.this.capacity;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (keyL[i] != ((K) NULL_SLOT)) {
                        this.posNext = i;
                        break;
                    }
                }
                if (i == capL && ObjObjLinearProbingHashMap.this.withNullKey) {
                    this.posNext = i;
                }
            }
            return current;
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            if (posPreL == ObjObjLinearProbingHashMap.this.capacity) {
                ObjObjLinearProbingHashMap.this.removeNull(ObjObjLinearProbingHashMap.this.keyTable);
                this.pos = -1;
            } else {
                final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
                ObjObjLinearProbingHashMap.this.removeAndShift(keyL, posPreL);
                this.scanPos(keyL, posPreL);
            }
            this.posPre = -1;
        }

        protected abstract void consumerProxy(C action, K key, int pos);

        public void forEachRemaining(C action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            final int capL = ObjObjLinearProbingHashMap.this.capacity;
            int i, findPre = this.posPre;
            for (i = current; i < capL; i++) {
                final K currKey;
                if ((currKey = keyL[i]) != ((K) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, findPre = i);
                }
            }
            if (i == capL && ObjObjLinearProbingHashMap.this.withNullKey) {
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
                this.estSize = ObjObjLinearProbingHashMap.this.size;
                newLimit = this.limitCap = ObjObjLinearProbingHashMap.this.capacity;
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

            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final K currKey;
                if ((currKey = keyL[idxL]) != ((K) NULL_SLOT)) {
                    this.consumerProxy(action, currKey, idxL);
                    this.index = idxL + 1;
                    return true;
                }
            }
            final int capL = ObjObjLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && ObjObjLinearProbingHashMap.this.withNullKey) {
                this.consumerProxy(action, keyL[capL], capL);
                this.index = capL + 1;
                return true;
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(C action) {
            final int currLimit = this.getLimitCap();

            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            int idxL;
            for (idxL = this.index; idxL < currLimit; idxL++) {
                final K currKey;
                if ((currKey = keyL[idxL]) != ((K) NULL_SLOT)) this.consumerProxy(action, currKey, idxL);
            }
            final int capL = ObjObjLinearProbingHashMap.this.capacity;
            if (idxL == currLimit && idxL == capL && ObjObjLinearProbingHashMap.this.withNullKey) {
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
            return (this.limitCap < 0 || this.estSize == ObjObjLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }

    protected class KeyIterator extends MapIterator<Consumer<? super K>> implements Iterator<K> {
        public K next() {
            return ObjObjLinearProbingHashMap.this.keyTable[this.nextPos()];
        }

        protected void consumerProxy(Consumer<? super K> action, K key, int pos) {
            action.accept(key);
        }
    }

    protected class ValueIterator extends MapIterator<Consumer<? super V>> implements Iterator<V> {
        public V next() {
            return ObjObjLinearProbingHashMap.this.valueTable[this.nextPos()];
        }

        protected void consumerProxy(Consumer<? super V> action, K key, int pos) {
            action.accept(ObjObjLinearProbingHashMap.this.valueTable[pos]);
        }
    }

    protected class MapEntryIterator extends MapIterator<Consumer<? super ObjObjMap.Entry<K, V>>> implements Iterator<ObjObjMap.Entry<K, V>> {
        public ObjObjMap.Entry<K, V> next() {
            return new Node(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super ObjObjMap.Entry<K, V>> action, K key, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewIterator extends MapIterator<Consumer<? super ObjObjMap.Entry<K, V>>> implements Iterator<ObjObjMap.Entry<K, V>> {
        protected final Node tmpEntry = new Node();

        public ObjObjMap.Entry<K, V> next() {
            return this.tmpEntry.update(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super Entry<K, V>> action, K key, int pos) {
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
    }

    protected class ValueSpliterator extends MapSpliterator<Consumer<? super V>, Spliterator<V>> implements Spliterator<V> {
        protected ValueSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<V> splitProxy(int index, int limitCap, int estSize) {
            return new ValueSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super V> action, K key, int pos) {
            action.accept(ObjObjLinearProbingHashMap.this.valueTable[pos]);
        }

        public int characteristics() {
            return this.limitCap < 0 || this.estSize == ObjObjLinearProbingHashMap.this.size ? Spliterator.SIZED : 0;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super ObjObjMap.Entry<K, V>>, Spliterator<ObjObjMap.Entry<K, V>>> implements Spliterator<ObjObjMap.Entry<K, V>> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjObjMap.Entry<K, V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjObjMap.Entry<K, V>> action, K key, int pos) {
            action.accept(new Node(pos));
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjObjLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class MapEntryViewSpliterator extends MapSpliterator<Consumer<? super ObjObjMap.Entry<K, V>>, Spliterator<ObjObjMap.Entry<K, V>>> implements Spliterator<ObjObjMap.Entry<K, V>> {
        protected final Node tmpEntry = new Node();

        protected MapEntryViewSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjObjMap.Entry<K, V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntryViewSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjObjMap.Entry<K, V>> action, K key, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjObjLinearProbingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class KeySet extends AbstractObjSet<K> {
        public void resize(int newCapacity) {
            ObjObjLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjObjLinearProbingHashMap.this.containsKey(o);
        }

        public boolean removeElement(Object o) {
            final HashBehavior<K> behavior = ObjObjLinearProbingHashMap.this.hashBehavior;
            final int maskL = ObjObjLinearProbingHashMap.this.posMask;
            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            K currKey;
            for (int pos = behavior.hashing(o); (currKey = keyL[pos &= maskL]) != ((K) NULL_SLOT); pos++) {
                if (behavior.equals(o, currKey)) {
                    ObjObjLinearProbingHashMap.this.removeAndShift(keyL, pos);
                    return true;
                }
            }
            return false;
        }

        public void forEach(Consumer<? super K> action) {
            if (ObjObjLinearProbingHashMap.this.isEmpty()) return;

            final int capL = ObjObjLinearProbingHashMap.this.capacity;
            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            K currKey;
            for (int pos = 0; pos < capL;) {
                if ((currKey = keyL[pos++]) != ((K) NULL_SLOT)) {
                    action.accept(currKey);
                }
            }
        }

        public int capacity() {
            return ObjObjLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return ObjObjLinearProbingHashMap.this.size;
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public Spliterator<K> spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjLinearProbingHashMap.this.clear();
        }
        
        public void clear(int newCapacity) {
            ObjObjLinearProbingHashMap.this.clear(newCapacity);
        }
    }

    protected class ValueCollection extends AbstractObjCollection<V> {
        public void resize(int newCapacity) {
            ObjObjLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjObjLinearProbingHashMap.this.containsValue(o);
        }

        public void forEach(Consumer<? super V> action) {
            if (ObjObjLinearProbingHashMap.this.isEmpty()) return;

            final int capL = ObjObjLinearProbingHashMap.this.capacity;
            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            final V[] valueL = ObjObjLinearProbingHashMap.this.valueTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((K) NULL_SLOT)) {
                    action.accept(valueL[pos]);
                }
            }
        }

        public int capacity() {
            return ObjObjLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return ObjObjLinearProbingHashMap.this.size;
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjLinearProbingHashMap.this.clear(newCapacity);
        }
    }

    protected class MapEntrySet extends AbstractObjSet<ObjObjMap.Entry<K, V>> implements EntrySetExt<ObjObjMap.Entry<K, V>> {
        protected final Node tmpEntry = new Node();

        public void resize(int newCapacity) {
            ObjObjLinearProbingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjLinearProbingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjObjLinearProbingHashMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjObjLinearProbingHashMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        protected void forEachProxy(Consumer<? super ObjObjMap.Entry<K,V>> action, IntFunction<ObjObjMap.Entry<K,V>> supplier) {
            if (ObjObjLinearProbingHashMap.this.isEmpty()) return;

            final int capL = ObjObjLinearProbingHashMap.this.capacity;
            final K[] keyL = ObjObjLinearProbingHashMap.this.keyTable;
            for (int pos = 0; pos < capL; pos++) {
                if (keyL[pos] != ((K) NULL_SLOT)) {
                    action.accept(supplier.apply(pos));
                }
            }
        }

        public void forEach(Consumer<? super ObjObjMap.Entry<K,V>> action) {
            this.forEachProxy(action, Node::new);
        }

        public void viewForEach(Consumer<? super ObjObjMap.Entry<K,V>> action) {
            this.forEachProxy(action, this.tmpEntry::update);
        }

        public int capacity() {
            return ObjObjLinearProbingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjLinearProbingHashMap.this.remaining();
        }

        public int size() {
            return ObjObjLinearProbingHashMap.this.size;
        }

        public Iterator<ObjObjMap.Entry<K, V>> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<ObjObjMap.Entry<K, V>> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<ObjObjMap.Entry<K, V>> viewIterator() {
            return new MapEntryViewIterator();
        }

        public Spliterator<ObjObjMap.Entry<K, V>> viewSpliterator() {
            return new MapEntryViewSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjLinearProbingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjLinearProbingHashMap.this.clear(newCapacity);
        }
    }
}
