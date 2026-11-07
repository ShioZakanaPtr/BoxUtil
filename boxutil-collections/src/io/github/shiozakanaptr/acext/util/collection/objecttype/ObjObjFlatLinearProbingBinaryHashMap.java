package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.*;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjObjBiMap;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;
import io.github.shiozakanaptr.acext.util.function.objecttype.TeConsumer;
import io.github.shiozakanaptr.acext.util.function.objecttype.TeFunction;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
public class ObjObjFlatLinearProbingBinaryHashMap<X, Y, V> implements ObjObjBiMap<X, Y, V>, BinaryHashContainer<X, Y>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;

    protected final static Object NULL_KEY_A = new EmptySlot();
    protected final static Object NULL_KEY_B = null;

    protected static class EmptySlot {
        public int hashCode() {
            return 0;
        }

        public String toString() {
            return "Placeholder NULL";
        }
    }

    protected transient int remaining = 0;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected V defVal = null;
    protected final HashBehavior<X> hashBehaviorA;
    protected final HashBehavior<Y> hashBehaviorB;
    protected transient int[] hashTable;
    protected transient X[] keyATable;
    protected transient Y[] keyBTable;
    protected transient V[] valueTable;

    protected transient CollectionExt<X> firstKeyCollection = null;
    protected transient CollectionExt<Y> secondKeyCollection = null;
    protected transient CollectionExt<V> valueCollection = null;
    protected transient EntrySetExt<BiEntry<X, Y, V>> entrySet = null;

    protected X[] newKeyATable(int cap) {
        return (X[]) new Object[cap];
    }

    protected Y[] newKeyBTable(int cap) {
        return (Y[]) new Object[cap];
    }

    protected V[] newValueTable(int cap) {
        return (V[]) new Object[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param firstHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     * @param secondHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjFlatLinearProbingBinaryHashMap(int initialCapacity, float loadFactor, final HashBehavior<X> firstHashBehavior, final HashBehavior<Y> secondHashBehavior) {
        InternalCollectionUtil.checkHashCapacity(initialCapacity);
        InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 0.9f);

        final int capacity = InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2));
        this.posMask = capacity - 1;
        this.loadFactor = loadFactor;
        this.hashBehaviorA = Objects.requireNonNull(firstHashBehavior);
        this.hashBehaviorB = Objects.requireNonNull(secondHashBehavior);
        this.hashTable = new int[capacity];
        this.keyATable = this.newKeyATable(capacity);
        this.keyBTable = this.newKeyBTable(capacity);
        this.valueTable = this.newValueTable(capacity);
        Arrays.fill(this.keyATable, ((X) NULL_KEY_A));
        this.calculateRemaining();
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param firstHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     * @param secondHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjFlatLinearProbingBinaryHashMap(int initialCapacity, final HashBehavior<X> firstHashBehavior, final HashBehavior<Y> secondHashBehavior) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, firstHashBehavior, secondHashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjObjFlatLinearProbingBinaryHashMap(int initialCapacity) {
        this(initialCapacity, HashBehaviorFactory.fibonacci(), HashBehaviorFactory.fibonacci());
    }

    public ObjObjFlatLinearProbingBinaryHashMap() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param firstHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     * @param secondHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjFlatLinearProbingBinaryHashMap(final BiMap<? extends X, ? extends Y, ? extends V> source, float loadFactor, final HashBehavior<X> firstHashBehavior, final HashBehavior<Y> secondHashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, firstHashBehavior, secondHashBehavior);
        this.putAll(source);
    }

    /**
     * @param firstHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     * @param secondHashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjFlatLinearProbingBinaryHashMap(final BiMap<? extends X, ? extends Y, ? extends V> source, final HashBehavior<X> firstHashBehavior, final HashBehavior<Y> secondHashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, firstHashBehavior, secondHashBehavior);
    }

    public ObjObjFlatLinearProbingBinaryHashMap(final BiMap<? extends X, ? extends Y, ? extends V> source) {
        this(source, HashBehaviorFactory.fibonacci(), HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size, this.loadFactor), newTargetCap = InternalCollectionUtil.getCapacityPOT(newCapacity);
        InternalCollectionUtil.checkHashCapacity(newTargetCap);
        if (newTargetCap == this.keyATable.length) return;

        final int newCap = Math.max(minSize, Math.max(newTargetCap, 2)),
                newMask = newCap - 1, oldCap;
        final int[] newHash = new int[newCap], oldHash = this.hashTable;
        final X[] newKeyA = this.newKeyATable(newCap), oldKeyA = this.keyATable;
        final Y[] newKeyB = this.newKeyBTable(newCap), oldKeyB = this.keyBTable;
        final V[] newValue = this.newValueTable(newCap), oldValue = this.valueTable;
        Arrays.fill(newKeyA, ((X) NULL_KEY_A));

        if (this.size > 0) {
            oldCap = oldKeyA.length;
            for (int i = 0; i < oldCap; i++) {
                final X currKeyA;
                if ((currKeyA = oldKeyA[i]) != ((X) NULL_KEY_A)) {
                    int pos = oldHash[i] & newMask;
                    while (newKeyA[pos] != ((X) NULL_KEY_A)) {
                        pos = (pos + 1) & newMask;
                    }
                    newHash[pos] = oldHash[i];
                    newKeyA[pos] = currKeyA;
                    newKeyB[pos] = oldKeyB[i];
                    newValue[pos] = oldValue[i];
                }
            }
        }

        this.posMask = newMask;
        this.hashTable = newHash;
        this.keyATable = newKeyA;
        this.keyBTable = newKeyB;
        this.valueTable = newValue;
        this.calculateRemaining();
    }

    public void reserve(int newMaxSize) {
        if (newMaxSize < 1) throw new IllegalArgumentException("Not a valid size: '" + newMaxSize + '\'');
        this.resize(InternalCollectionUtil.getHashTableCapacity(newMaxSize, this.loadFactor));
    }

    protected V removeAndShift(final X[] keyAL, final Y[] keyBL, int currPos) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        final V[] valueL = this.valueTable;
        final V result = valueL[currPos];
        keyAL[currPos] = ((X) NULL_KEY_A);
        keyBL[currPos] = ((Y) NULL_KEY_B);
        valueL[currPos] = null;
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        X currKeyA;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currKeyA = keyAL[nextPos = (nextPos + 1) & maskL]) != ((X) NULL_KEY_A); dist++) {
            if (((nextPos - (behaviorA.mix(behaviorA.hashing(currKeyA), this.hashBehaviorB.hashing(keyBL[nextPos])) & maskL)) & maskL) >= dist) {
                keyAL[holePos] = currKeyA;
                keyAL[nextPos] = ((X) NULL_KEY_A);
                keyBL[holePos] = keyBL[nextPos];
                keyBL[nextPos] = ((Y) NULL_KEY_B);
                valueL[holePos] = valueL[nextPos];
                valueL[nextPos] = null;
                holePos = nextPos;
                dist = 0;
            }
        }
        return result;
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.keyATable.length));
    }

    protected int getHash(final Object keyA, final Object keyB) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        return behaviorA.mix(behaviorA.hashing(keyA), this.hashBehaviorB.hashing(keyB));
    }

    protected int find(int hash, final Object keyA, final Object keyB) {
        final int maskL = this.posMask;
        final int[] hashL = this.hashTable;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        int pos = hash;
        for (X currKeyA; (currKeyA = keyAL[pos &= maskL]) != ((X) NULL_KEY_A); pos++) {
            if (hash == hashL[pos] && this.hashBehaviorA.equals(keyA, currKeyA) && this.hashBehaviorB.equals(keyB, keyBL[pos])) {
                return pos;
            }
        }
        return ~pos;
    }

    protected V insert(int pos, int hash, final X keyA, final Y keyB, final V value) {
        final int realPos = ~pos;
        this.hashTable[realPos] = hash;
        this.keyATable[realPos] = keyA;
        this.keyBTable[realPos] = keyB;
        this.valueTable[realPos] = value;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return value;
    }

    public V get(Object keyA, Object keyB) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        final HashBehavior<Y> behaviorB = this.hashBehaviorB;
        final int maskL = this.posMask, hash;
        final int[] hashL = this.hashTable;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        X currKeyA;
        for (int pos = hash = behaviorA.mix(behaviorA.hashing(keyA), behaviorB.hashing(keyB)); (currKeyA = keyAL[pos &= maskL]) != ((X) NULL_KEY_A); pos++) {
            if (hash == hashL[pos] && behaviorA.equals(keyA, currKeyA) && behaviorB.equals(keyB, keyBL[pos])) return this.valueTable[pos];
        }
        return this.defVal;
    }

    public V getOrDefault(Object keyA, Object keyB, V defaultValue) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        final HashBehavior<Y> behaviorB = this.hashBehaviorB;
        final int maskL = this.posMask, hash;
        final int[] hashL = this.hashTable;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        X currKeyA;
        for (int pos = hash = behaviorA.mix(behaviorA.hashing(keyA), behaviorB.hashing(keyB)); (currKeyA = keyAL[pos &= maskL]) != ((X) NULL_KEY_A); pos++) {
            if (hash == hashL[pos] && behaviorA.equals(keyA, currKeyA) && behaviorB.equals(keyB, keyBL[pos])) return this.valueTable[pos];
        }
        return defaultValue;
    }

    public V put(X keyA, Y keyB, V value) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        final HashBehavior<Y> behaviorB = this.hashBehaviorB;
        final int maskL = this.posMask, hash;
        final int[] hashL = this.hashTable;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        int pos = hash = behaviorA.mix(behaviorA.hashing(keyA), behaviorB.hashing(keyB));
        for (X currKeyA; (currKeyA = keyAL[pos &= maskL]) != ((X) NULL_KEY_A); pos++) {
            if (hash == hashL[pos] && behaviorA.equals(keyA, currKeyA) && behaviorB.equals(keyB, keyBL[pos])) {
                final V[] valueL;
                final V oldValue = (valueL = this.valueTable)[pos];
                valueL[pos] = value;
                return oldValue;
            }
        }
        hashL[pos] = hash;
        keyAL[pos] = keyA;
        keyBL[pos] = keyB;
        this.valueTable[pos] = value;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return this.defVal;
    }

    public V putIfAbsent(X keyA, Y keyB, V value) {
        final int pos, hash;
        if ((pos = this.find(hash = this.getHash(keyA, keyB), keyA, keyB)) < 0) {
            this.insert(pos, hash, keyA, keyB, value);
            return this.defVal;
        }
        final V[] valueL;
        if ((valueL = this.valueTable)[pos] != null) return valueL[pos];
        valueL[pos] = value;
        return null;
    }

    public V remove(Object keyA, Object keyB) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        final HashBehavior<Y> behaviorB = this.hashBehaviorB;
        final int maskL = this.posMask, hash;
        final int[] hashL = this.hashTable;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        X currKeyA;
        for (int pos = hash = behaviorA.mix(behaviorA.hashing(keyA), behaviorB.hashing(keyB)); (currKeyA = keyAL[pos &= maskL]) != ((X) NULL_KEY_A); pos++) {
            if (hash == hashL[pos] && behaviorA.equals(keyA, currKeyA) && behaviorB.equals(keyB, keyBL[pos])) return this.removeAndShift(keyAL, keyBL, pos);
        }
        return this.defVal;
    }

    public boolean remove(Object keyA, Object keyB, Object value) {
        final int pos;
        if ((pos = this.find(this.getHash(keyA, keyB), keyA, keyB)) > -1 && Objects.equals(value, this.valueTable[pos])) {
            this.removeAndShift(this.keyATable, this.keyBTable, pos);
            return true;
        }
        return false;
    }

    public V replace(X keyA, Y keyB, V value) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        final HashBehavior<Y> behaviorB = this.hashBehaviorB;
        final int maskL = this.posMask, hash;
        final int[] hashL = this.hashTable;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        X currKeyA;
        for (int pos = hash = behaviorA.mix(behaviorA.hashing(keyA), behaviorB.hashing(keyB)); (currKeyA = keyAL[pos &= maskL]) != ((X) NULL_KEY_A); pos++) {
            if (hash == hashL[pos] && behaviorA.equals(keyA, currKeyA) && behaviorB.equals(keyB, keyBL[pos])) {
                final V[] valueL;
                final V result = (valueL = this.valueTable)[pos];
                valueL[pos] = value;
                return result;
            }
        }
        return this.defVal;
    }

    public boolean replace(X keyA, Y keyB, V oldValue, V newValue) {
        final int pos;
        final V[] valueL = this.valueTable;
        if ((pos = this.find(this.getHash(keyA, keyB), keyA, keyB)) > -1 && Objects.equals(oldValue, valueL[pos])) {
            valueL[pos] = newValue;
            return true;
        }
        return false;
    }

    public V computeIfAbsent(X keyA, Y keyB, BiFunction<? super X, ? super Y, ? extends V> mappingFunction) {
        final int pos, hash;
        final V[] valueL = this.valueTable;
        final V currValue;
        if ((pos = this.find(hash = this.getHash(keyA, keyB), keyA, keyB)) > -1 && (currValue = valueL[pos]) != null) return currValue;

        final V newValue = mappingFunction.apply(keyA, keyB);
        if (newValue != null) {
            return pos < 0 ? this.insert(pos, hash, keyA, keyB, newValue) : (valueL[pos] = newValue);
        }
        return this.defVal;
    }

    public V computeIfPresent(X keyA, Y keyB, TeFunction<? super X, ? super Y, ? super V, ? extends V> remappingFunction) {
        final int pos;
        final V[] valueL;
        if ((pos = this.find(this.getHash(keyA, keyB), keyA, keyB)) < 0 || (valueL = this.valueTable)[pos] == null) return this.defVal;

        final V newValue = remappingFunction.apply(keyA, keyB, valueL[pos]);
        if (newValue != null) return valueL[pos] = newValue;
        this.removeAndShift(this.keyATable, this.keyBTable, pos);
        return this.defVal;
    }

    public V compute(X keyA, Y keyB, TeFunction<? super X, ? super Y, ? super V, ? extends V> remappingFunction) {
        final int pos, hash;
        final V[] valueL = this.valueTable;
        final V newValue = remappingFunction.apply(keyA, keyB, (pos = this.find(hash = this.getHash(keyA, keyB), keyA, keyB)) < 0 ? null : valueL[pos]);
        if (newValue != null) return pos < 0 ? this.insert(pos, hash, keyA, keyB, newValue) : (valueL[pos] = newValue);

        if (pos >= 0) this.removeAndShift(this.keyATable, this.keyBTable, pos);
        return this.defVal;
    }

    public V merge(X keyA, Y keyB, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        final int pos, hash;
        if ((pos = this.find(hash = this.getHash(keyA, keyB), keyA, keyB)) < 0) return this.insert(pos, hash, keyA, keyB, value);

        final V[] valueL = this.valueTable;
        final V oldValue = valueL[pos],
                newValue = oldValue != null ? remappingFunction.apply(oldValue, value) : value;
        if (newValue != null) return valueL[pos] = newValue;
        this.removeAndShift(this.keyATable, this.keyBTable, pos);
        return this.defVal;
    }

    public V update(X keyA, Y keyB, Function<? super V, ? extends V> updateFunction) {
        final int pos, hash;
        final V[] valueL = this.valueTable;
        final V newValue = updateFunction.apply((pos = this.find(hash = this.getHash(keyA, keyB), keyA, keyB)) < 0 ? this.defVal : valueL[pos]);
        return pos < 0 ? this.insert(pos, hash, keyA, keyB, newValue) : (valueL[pos] = newValue);
    }

    public V remapping(X keyA, Y keyB, TeFunction<? super X, ? super Y, ? super V, ? extends Pair<? extends X, ? extends Y>> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        final int pos;
        if ((pos = this.find(this.getHash(keyA, keyB), keyA, keyB)) < 0) return this.defVal;

        final V[] valueL;
        final V currValue;
        final Pair<? extends X, ? extends Y> newKey = remappingFunction.apply(keyA, keyB, currValue = (valueL = this.valueTable)[pos]);
        final boolean sameKey = newKey != null && this.hashBehaviorA.equals(newKey.left(), keyA) && this.hashBehaviorB.equals(newKey.right(), keyB);

        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        if (newKey == null || !sameKey) this.removeAndShift(keyAL, keyBL, pos);
        if (newKey == null) return this.defVal;
        if (sameKey) return currValue;

        final int newPos, newHash;
        if ((newPos = this.find(newHash = this.getHash(newKey.left(), newKey.right()), newKey.left(), newKey.right())) < 0) return this.insert(newPos, newHash, newKey.left(), newKey.right(), currValue);

        final V newValue = mergeFunction.apply(valueL[newPos], currValue);
        if (newValue != null) return valueL[newPos] = newValue;
        this.removeAndShift(keyAL, keyBL, newPos);
        return this.defVal;
    }

    public void swap(BiMap<X, Y, V> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ObjObjFlatLinearProbingBinaryHashMap) {
            final ObjObjFlatLinearProbingBinaryHashMap<X, Y, V> src = (ObjObjFlatLinearProbingBinaryHashMap<X, Y, V>) source;
            if (Objects.equals(src.hashBehaviorA, this.hashBehaviorA) && Objects.equals(src.hashBehaviorB, this.hashBehaviorB) && this.loadFactor == src.loadFactor) {
                this.clearMappingCollection();
                src.clearMappingCollection();

                final int currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final int[] currHashA = this.hashTable, srcHashA = src.hashTable;
                final X[] currKeyA = this.keyATable, srcKeyA = src.keyATable;
                final Y[] currKeyB = this.keyBTable, srcKeyB = src.keyBTable;
                final V[] currValue = this.valueTable, srcValue = src.valueTable;

                src.size = currSize;
                src.posMask = currMask;
                src.remaining = currRemaining;
                src.hashTable = currHashA;
                src.keyATable = currKeyA;
                src.keyBTable = currKeyB;
                src.valueTable = currValue;
                this.size = srcSize;
                this.posMask = srcMask;
                this.remaining = srcRemaining;
                this.hashTable = srcHashA;
                this.keyATable = srcKeyA;
                this.keyBTable = srcKeyB;
                this.valueTable = srcValue;
                return;
            }
        }
        if (source.size() < this.size()) {
            final BiMap<X, Y, V> tmp = new ObjObjFlatLinearProbingBinaryHashMap<>(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final BiMap<X, Y, V> tmp = new ObjObjFlatLinearProbingBinaryHashMap<>(this);
            this.clear();
            this.putAll(source);
            source.clear();
            source.putAll(tmp);
        }
    }

    public boolean swapAt(X fromA, Y fromB, X toA, Y toB) {
        final int posA, hashA = this.getHash(fromA, fromB);
        if ((posA = this.find(hashA, fromA, fromB)) < 0) return false;
        final int posB, hashB = this.getHash(toA, toB);
        if ((posB = this.find(hashB, toA, toB)) < 0) return false;

        final V[] valueL = this.valueTable;
        final V tmpValue = valueL[posA];
        valueL[posA] = valueL[posB];
        valueL[posB] = tmpValue;
        return false;
    }

    public boolean containsKey(Object keyA, Object keyB) {
        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        final HashBehavior<Y> behaviorB = this.hashBehaviorB;
        final int maskL = this.posMask, hash = behaviorA.mix(behaviorA.hashing(keyA), behaviorB.hashing(keyB));
        final int[] hashL = this.hashTable;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        int pos = hash;
        for (X currKeyA; (currKeyA = keyAL[pos &= maskL]) != ((X) NULL_KEY_A); pos++) {
            if (hash == hashL[pos] && behaviorA.equals(keyA, currKeyA) && behaviorB.equals(keyB, keyBL[pos])) {
                return true;
            }
        }
        return false;
    }

    public boolean containsFirstKey(Object keyA) {
        if (this.isEmpty()) return false;

        final X[] keyAL = this.keyATable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            final X currKeyA;
            if ((currKeyA = keyAL[pos]) != ((X) NULL_KEY_A) && Objects.equals(keyA, currKeyA)) return true;
        }
        return false;
    }

    public boolean containsSecondKey(Object keyB) {
        if (this.isEmpty()) return false;

        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            if (keyAL[pos] != ((X) NULL_KEY_A) && Objects.equals(keyB, keyBL[pos])) return true;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        if (this.isEmpty()) return false;

        final X[] keyAL = this.keyATable;
        final V[] valueL = this.valueTable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            if (keyAL[pos] != ((X) NULL_KEY_A) && Objects.equals(value, valueL[pos])) return true;
        }
        return false;
    }

    public void forEach(TeConsumer<? super X, ? super Y, ? super V> action) {
        if (this.isEmpty()) return;

        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        final V[] valueL = this.valueTable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            final X currKeyA;
            if ((currKeyA = keyAL[pos]) != ((X) NULL_KEY_A)) {
                action.accept(currKeyA, keyBL[pos], valueL[pos]);
            }
        }
    }

    public void replaceAll(TeFunction<? super X, ? super Y, ? super V, ? extends V> function) {
        if (this.isEmpty()) return;

        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        final V[] valueL = this.valueTable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            final X currKeyA;
            if ((currKeyA = keyAL[pos]) != ((X) NULL_KEY_A)) {
                valueL[pos] = function.apply(currKeyA, keyBL[pos], valueL[pos]);
            }
        }
    }

    public void putAll(BiMap<? extends X, ? extends Y, ? extends V> m) {
        if (m == this || m.isEmpty()) return;
        if (m.size() >= this.remaining()) this.reserve(this.size + m.size() + 1);
        if (m instanceof ObjObjFlatLinearProbingBinaryHashMap<?, ?, ?>) {
            final ObjObjFlatLinearProbingBinaryHashMap<?, ?, ?> cast = (ObjObjFlatLinearProbingBinaryHashMap<?, ?, ?>) m;
            final X[] keyAL = (X[]) cast.keyATable;
            final Y[] keyBL = (Y[]) cast.keyBTable;
            final V[] valueL = (V[]) cast.valueTable;
            final int capL = cast.keyATable.length;
            for (int pos = 0; pos < capL; pos++) {
                final X currKeyA;
                if ((currKeyA = keyAL[pos]) != ((X) NULL_KEY_A)) {
                    this.put(currKeyA, keyBL[pos], valueL[pos]);
                }
            }
        } else m.forEach(this::put);
    }

    private void calculateRemaining() {
        this.remaining = InternalCollectionUtil.getHashTableMaxRemaining(this.keyATable.length, this.loadFactor, true) - this.size;
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
        return this.keyATable.length;
    }

    public int remaining() {
        return this.remaining;
    }

    public float getLoadFactor() {
        return this.loadFactor;
    }

    public HashBehavior<X> getFirstBehavior() {
        return this.hashBehaviorA;
    }

    public HashBehavior<Y> getSecondBehavior() {
        return this.hashBehaviorB;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public CollectionExt<X> firstKeyCollection() {
        CollectionExt<X> result;
        if ((result = this.firstKeyCollection) == null) {
            result = this.firstKeyCollection = new FirstKeyCollection();
        }
        return result;
    }

    public CollectionExt<Y> secondKeyCollection() {
        CollectionExt<Y> result;
        if ((result = this.secondKeyCollection) == null) {
            result = this.secondKeyCollection = new SecondKeyCollection();
        }
        return result;
    }

    public CollectionExt<V> valueCollection() {
        CollectionExt<V> result;
        if ((result = this.valueCollection) == null) {
            result = this.valueCollection = new ValueCollection();
        }
        return result;
    }

    public EntrySetExt<ObjObjBiMap.BiEntry<X, Y, V>> entrySet() {
        EntrySetExt<ObjObjBiMap.BiEntry<X, Y, V>> result;
        if ((result = this.entrySet) == null) {
            result = this.entrySet = new MapEntrySet();
        }
        return result;
    }

    public int[] hashTable() {
        return this.hashTable;
    }

    public X[] keyATable() {
        return this.keyATable;
    }

    public Y[] keyBTable() {
        return this.keyBTable;
    }

    public V[] valueTable() {
        return this.valueTable;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        this.clearMappingCollection();
        Arrays.fill(this.hashTable, 0);
        Arrays.fill(this.keyATable, ((X) NULL_KEY_A));
        Arrays.fill(this.keyBTable, ((Y) NULL_KEY_B));
        Arrays.fill(this.valueTable, null);
        this.calculateRemaining();
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        final int newCap = InternalCollectionUtil.getCapacityPOT(Math.max(newCapacity, 2));
        if (newCap == this.keyATable.length) {
            this.clear();
        } else {
            this.posMask = newCap - 1;
            this.size = 0;
            this.clearMappingCollection();
            this.hashTable = new int[newCap];
            this.keyATable = this.newKeyATable(newCap);
            this.keyBTable = this.newKeyBTable(newCap);
            this.valueTable = this.newValueTable(newCap);
            this.calculateRemaining();
        }
    }

    public int hashCode() {
        if (this.isEmpty()) return 0;

        int result = 0;
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        final V[] valueL = this.valueTable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            final X currKeyA;
            if ((currKeyA = keyAL[pos]) != ((X) NULL_KEY_A)) {
                result += Objects.hashCode(currKeyA) ^ Objects.hashCode(keyBL[pos]) ^ Objects.hashCode(valueL[pos]);
            }
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof BiMap<?, ?, ?>) {
            final BiMap<?, ?, ?> castMap = (BiMap<?, ?, ?>) o;
            if (castMap.size() != this.size()) return false;
            try {
                if (castMap instanceof ObjObjFlatLinearProbingBinaryHashMap<?, ?, ?>) {
                    final ObjObjFlatLinearProbingBinaryHashMap<?, ?, ?> cast = (ObjObjFlatLinearProbingBinaryHashMap<?, ?, ?>) castMap;
                    final X[] srcKeyAL = (X[]) cast.keyATable;
                    final Y[] srcKeyBL = (Y[]) cast.keyBTable;
                    final V[] currValueL = this.valueTable, srcValueL = (V[]) cast.valueTable;

                    final int capacity = srcKeyAL.length;
                    for (int srcPos = 0; srcPos < capacity; srcPos++) {
                        final X srcKeyA;
                        if ((srcKeyA = srcKeyAL[srcPos]) != ((X) NULL_KEY_A)) {
                            final Y srcKeyB = srcKeyBL[srcPos];
                            final int currPos = this.find(this.getHash(srcKeyA, srcKeyB), srcKeyA, srcKeyB);
                            if (currPos < 0) return false;

                            if (!Objects.equals(currValueL[currPos], srcValueL[srcPos])) return false;
                        }
                    }
                    return true;
                } else {
                    final V[] currValueL = this.valueTable;
                    for (BiMap.BiEntry<?, ?, ?> entry : castMap.entrySet()) {
                        final X srcKeyA = (X) entry.getFirstKey();
                        final Y srcKeyB = (Y) entry.getSecondKey();
                        final V srcValue = (V) entry.getValue();
                        final int currPos = this.find(this.getHash(srcKeyA, srcKeyB), srcKeyA, srcKeyB);
                        if (currPos < 0) return false;

                        final V currValue = currValueL[currPos];
                        if (srcValue == null && currValue != null || srcValue != null && !Objects.equals(srcValue, currValue)) return false;
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
        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        final V[] valueL = this.valueTable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            final X currKeyA;
            if ((currKeyA = keyAL[pos]) != ((X) NULL_KEY_A)) {
                final Y currKeyB;
                final V currValue;
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append('[').append(currKeyA != this ? currKeyA : "(this Map)")
                        .append(',').append(' ')
                        .append((currKeyB = keyBL[pos]) != this ? currKeyB : "(this Map)")
                        .append(']')
                        .append('=')
                        .append((currValue = valueL[pos]) != this ? currValue : "(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    protected void clearMappingCollection() {
        this.firstKeyCollection = null;
        this.secondKeyCollection = null;
        this.valueCollection = null;
        this.entrySet = null;
    }

    public Object clone() {
        ObjObjFlatLinearProbingBinaryHashMap<X, Y, V> result;
        try {
            result = (ObjObjFlatLinearProbingBinaryHashMap<X, Y, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.keyATable.length;
        result.hashTable = new int[srcLen];
        result.keyATable = result.newKeyATable(srcLen);
        result.keyBTable = result.newKeyBTable(srcLen);
        result.valueTable = result.newValueTable(srcLen);
        result.clearMappingCollection();
        System.arraycopy(this.hashTable, 0, result.hashTable, 0, srcLen);
        System.arraycopy(this.keyATable, 0, result.keyATable, 0, srcLen);
        System.arraycopy(this.keyBTable, 0, result.keyBTable, 0, srcLen);
        System.arraycopy(this.valueTable, 0, result.valueTable, 0, srcLen);
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.isEmpty()) return;

        final X[] keyAL = this.keyATable;
        final Y[] keyBL = this.keyBTable;
        final V[] valueL = this.valueTable;
        final int capL = this.keyATable.length;
        for (int pos = 0; pos < capL; pos++) {
            final X currKeyA;
            if ((currKeyA = keyAL[pos]) != ((X) NULL_KEY_A)) {
                out.writeObject(currKeyA);
                out.writeObject(keyBL[pos]);
                out.writeObject(valueL[pos]);
            }
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehaviorA);
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehaviorB);
        InternalCollectionUtil.SerialCheck.ioCheckHashLoadFactor(this.loadFactor, 0.2f, 0.9f);
        InternalCollectionUtil.SerialCheck.ioCheckSize(this.size, 0, HashContainer.MAXIMUM_CAPACITY);
        final int capacity = InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(this.size, this.loadFactor, HashContainer.MAXIMUM_CAPACITY);
        final int maskL = this.posMask = capacity - 1;
        this.clearMappingCollection();
        final int[] hashL = this.hashTable = new int[capacity];
        final X[] keyAL = this.keyATable = this.newKeyATable(capacity);
        final Y[] keyBL = this.keyBTable = this.newKeyBTable(capacity);
        final V[] valueL = this.valueTable = this.newValueTable(capacity);
        Arrays.fill(keyAL, ((X) NULL_KEY_A));

        final HashBehavior<X> behaviorA = this.hashBehaviorA;
        for (int i = this.size; i-- > 0;) {
            final X currKeyA = (X) in.readObject();
            final Y currKeyB = (Y) in.readObject();
            final V currValue = (V) in.readObject();

            final int hash = behaviorA.mix(behaviorA.hashing(currKeyA), this.hashBehaviorB.hashing(currKeyB));
            int pos = hash & maskL;
            X findEle;
            while ((findEle = keyAL[pos]) != ((X) NULL_KEY_A)) {
                if (behaviorA.equals(currKeyA, findEle)) InternalCollectionUtil.SerialCheck.ioCheckElementIsDuplicate(currKeyA);
                pos = (pos + 1) & maskL;
            }
            hashL[pos ] = hash;
            keyAL[pos] = currKeyA;
            keyBL[pos] = currKeyB;
            valueL[pos] = currValue;
        }
        this.calculateRemaining();
    }

    protected class Node implements ObjObjBiMap.BiEntry<X, Y, V> {
        protected int pos;

        protected Node update(int pos) {
            this.pos = pos;
            return this;
        }

        protected Node() {}

        protected Node(int pos) {
            this.pos = pos;
        }

        public X getFirstKey() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.keyATable[this.pos];
        }

        public Y getSecondKey() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable[this.pos];
        }

        public V getValue() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.valueTable[this.pos];
        }

        public V setValue(V newValue) {
            final int posL = this.pos;
            final V[] valueL = ObjObjFlatLinearProbingBinaryHashMap.this.valueTable;
            final V oldValue = valueL[posL];
            valueL[posL] = newValue;
            return oldValue;
        }

        public String toString() {
            final int posL = this.pos;
            return "[" + ObjObjFlatLinearProbingBinaryHashMap.this.keyATable[posL] + ", " + ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable[posL] + "]=" + ObjObjFlatLinearProbingBinaryHashMap.this.valueTable[posL];
        }

        public int hashCode() {
            final int posL = this.pos;
            return Objects.hashCode(ObjObjFlatLinearProbingBinaryHashMap.this.keyATable[posL]) ^ Objects.hashCode(ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable[posL]) ^ Objects.hashCode(ObjObjFlatLinearProbingBinaryHashMap.this.valueTable[posL]);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof BiMap.BiEntry<?, ?, ?>)) return false;
            final BiMap.BiEntry<?, ?, ?> cast = (BiMap.BiEntry<?, ?, ?>) obj;
            final int posL = this.pos;
            return Objects.equals(ObjObjFlatLinearProbingBinaryHashMap.this.keyATable[posL], cast.getFirstKey())
                    && Objects.equals(ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable[posL], cast.getFirstKey())
                    && Objects.equals(ObjObjFlatLinearProbingBinaryHashMap.this.valueTable[posL], cast.getValue());
        }
    }

    protected abstract class MapIterator<C> {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected MapIterator() {
            if (ObjObjFlatLinearProbingBinaryHashMap.this.size > 0) {
                this.scanPos(ObjObjFlatLinearProbingBinaryHashMap.this.keyATable, 0);
            }
        }

        protected void scanPos(final X[] keyAL, int start) {
            final int capL = keyAL.length;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (keyAL[i] != ((X) NULL_KEY_A)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
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

            final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable;
            if (posL > -1) {
                final int capL = keyAL.length;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (keyAL[i] != ((X) NULL_KEY_A)) {
                        this.posNext = i;
                        break;
                    }
                }
            }
            return current;
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable;
            ObjObjFlatLinearProbingBinaryHashMap.this.removeAndShift(keyAL, ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable, posPreL);
            this.scanPos(keyAL, posPreL);
            this.posPre = -1;
        }

        protected abstract void consumerProxy(C action, X keyA, int pos);

        public void forEachRemaining(C action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable;
            int i = current, findPre = this.posPre;
            for (final int capL = keyAL.length; i < capL; i++) {
                final X currKeyA;
                if ((currKeyA = keyAL[i]) != ((X) NULL_KEY_A)) {
                    this.consumerProxy(action, currKeyA, findPre = i);
                }
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
                this.estSize = ObjObjFlatLinearProbingBinaryHashMap.this.size;
                newLimit = this.limitCap = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable.length;
            }
            return newLimit;
        }

        protected abstract S splitProxy(int index, int limitCap, int estSize);

        public S trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? this.splitProxy(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        protected abstract void consumerProxy(C action, X keyA, int pos);

        public boolean tryAdvance(C action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            for (final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable; idxL < currLimit; idxL++) {
                final X currKeyA;
                if ((currKeyA = keyAL[idxL]) != ((X) NULL_KEY_A)) {
                    this.consumerProxy(action, currKeyA, idxL);
                    this.index = idxL + 1;
                    return true;
                }
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(C action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            for (final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable; idxL < currLimit; idxL++) {
                final X currKeyA;
                if ((currKeyA = keyAL[idxL]) != ((X) NULL_KEY_A)) this.consumerProxy(action, currKeyA, idxL);
            }
            this.index = idxL;
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return this.limitCap < 0 || this.estSize == ObjObjFlatLinearProbingBinaryHashMap.this.size ? Spliterator.SIZED : 0;
        }
    }

    protected class FirstKeyIterator extends MapIterator<Consumer<? super X>> implements Iterator<X> {
        public X next() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.keyATable[this.nextPos()];
        }

        protected void consumerProxy(Consumer<? super X> action, X keyA, int pos) {
            action.accept(keyA);
        }
    }

    protected class SecondKeyIterator extends MapIterator<Consumer<? super Y>> implements Iterator<Y> {
        public Y next() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable[this.nextPos()];
        }

        protected void consumerProxy(Consumer<? super Y> action, X keyA, int pos) {
            action.accept(ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable[pos]);
        }
    }

    protected class ValueIterator extends MapIterator<Consumer<? super V>> implements Iterator<V> {
        public V next() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.valueTable[this.nextPos()];
        }

        protected void consumerProxy(Consumer<? super V> action, X keyA, int pos) {
            action.accept(ObjObjFlatLinearProbingBinaryHashMap.this.valueTable[pos]);
        }
    }

    protected class MapEntryIterator extends MapIterator<Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>>> implements Iterator<ObjObjBiMap.BiEntry<X, Y, V>> {
        public ObjObjBiMap.BiEntry<X, Y, V> next() {
            return new Node(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super BiEntry<X, Y, V>> action, X keyA, int pos) {
            action.accept(new Node(pos));
        }
    }

    protected class MapEntryViewIterator extends MapIterator<Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>>> implements Iterator<ObjObjBiMap.BiEntry<X, Y, V>> {
        protected final Node tmpEntry = new Node();

        public ObjObjBiMap.BiEntry<X, Y, V> next() {
            return this.tmpEntry.update(this.nextPos());
        }

        protected void consumerProxy(Consumer<? super BiEntry<X, Y, V>> action, X keyA, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }
    }

    protected class FirstKeySpliterator extends MapSpliterator<Consumer<? super X>, Spliterator<X>> implements Spliterator<X> {
        protected FirstKeySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<X> splitProxy(int index, int limitCap, int estSize) {
            return new FirstKeySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super X> action, X keyA, int pos) {
            action.accept(keyA);
        }
    }

    protected class SecondKeySpliterator extends MapSpliterator<Consumer<? super Y>, Spliterator<Y>> implements Spliterator<Y> {
        protected SecondKeySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<Y> splitProxy(int index, int limitCap, int estSize) {
            return new SecondKeySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super Y> action, X keyA, int pos) {
            action.accept(ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable[pos]);
        }
    }

    protected class ValueSpliterator extends MapSpliterator<Consumer<? super V>, Spliterator<V>> implements Spliterator<V> {
        protected ValueSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<V> splitProxy(int index, int limitCap, int estSize) {
            return new ValueSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super V> action, X keyA, int pos) {
            action.accept(ObjObjFlatLinearProbingBinaryHashMap.this.valueTable[pos]);
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>>, Spliterator<ObjObjBiMap.BiEntry<X, Y, V>>> implements Spliterator<ObjObjBiMap.BiEntry<X, Y, V>> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjObjBiMap.BiEntry<X, Y, V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>> action, X keyA, int pos) {
            action.accept(new Node(pos));
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjObjFlatLinearProbingBinaryHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class MapEntryViewSpliterator extends MapSpliterator<Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>>, Spliterator<ObjObjBiMap.BiEntry<X, Y, V>>> implements Spliterator<ObjObjBiMap.BiEntry<X, Y, V>> {
        protected final Node tmpEntry = new Node();

        protected MapEntryViewSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjObjBiMap.BiEntry<X, Y, V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntryViewSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>> action, X keyA, int pos) {
            action.accept(this.tmpEntry.update(pos));
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjObjFlatLinearProbingBinaryHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class FirstKeyCollection extends AbstractObjCollection<X> {
        public void resize(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjFlatLinearProbingBinaryHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjObjFlatLinearProbingBinaryHashMap.this.containsFirstKey(o);
        }

        public void forEach(Consumer<? super X> action) {
            if (ObjObjFlatLinearProbingBinaryHashMap.this.isEmpty()) return;

            final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable;
            for (final X currKeyA : keyAL) {
                if (currKeyA != ((X) NULL_KEY_A)) {
                    action.accept(currKeyA);
                }
            }
        }

        public int capacity() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.remaining();
        }

        public int size() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.size;
        }

        public Iterator<X> iterator() {
            return new FirstKeyIterator();
        }

        public Spliterator<X> spliterator() {
            return new FirstKeySpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear(newCapacity);
        }
    }

    protected class SecondKeyCollection extends AbstractObjCollection<Y> {
        public void resize(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjFlatLinearProbingBinaryHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjObjFlatLinearProbingBinaryHashMap.this.containsSecondKey(o);
        }

        public void forEach(Consumer<? super Y> action) {
            if (ObjObjFlatLinearProbingBinaryHashMap.this.isEmpty()) return;

            final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable;
            final Y[] keyBL = ObjObjFlatLinearProbingBinaryHashMap.this.keyBTable;
            final int capL = keyAL.length;
            for (int pos = 0; pos < capL; pos++) {
                if (keyAL[pos] != ((X) NULL_KEY_A)) {
                    action.accept(keyBL[pos]);
                }
            }
        }

        public int capacity() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.remaining();
        }

        public int size() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.size;
        }

        public Iterator<Y> iterator() {
            return new SecondKeyIterator();
        }

        public Spliterator<Y> spliterator() {
            return new SecondKeySpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear(newCapacity);
        }
    }

    protected class ValueCollection extends AbstractObjCollection<V> {
        public void resize(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjFlatLinearProbingBinaryHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(long o) {
            return ObjObjFlatLinearProbingBinaryHashMap.this.containsValue(o);
        }

        public void forEach(Consumer<? super V> action) {
            if (ObjObjFlatLinearProbingBinaryHashMap.this.isEmpty()) return;

            final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable;
            final V[] valueL = ObjObjFlatLinearProbingBinaryHashMap.this.valueTable;
            final int capL = keyAL.length;
            for (int pos = 0; pos < capL; pos++) {
                if (keyAL[pos] != ((X) NULL_KEY_A)) {
                    action.accept(valueL[pos]);
                }
            }
        }

        public int capacity() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.remaining();
        }

        public int size() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.size;
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear(newCapacity);
        }
    }

    protected class MapEntrySet extends AbstractObjSet<ObjObjBiMap.BiEntry<X, Y, V>> implements EntrySetExt<ObjObjBiMap.BiEntry<X, Y, V>> {
        protected final Node tmpEntry = new Node();

        public void resize(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjFlatLinearProbingBinaryHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof BiMap.BiEntry<?, ?, ?>)) return false;
            final BiMap.BiEntry<?, ?, ?> entryCast = (BiMap.BiEntry<?, ?, ?>) o;
            return ObjObjFlatLinearProbingBinaryHashMap.this.containsKey(entryCast.getFirstKey(), entryCast.getSecondKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof BiMap.BiEntry<?, ?, ?>)) return false;
            final BiMap.BiEntry<?, ?, ?> entryCast = (BiMap.BiEntry<?, ?, ?>) o;
            return ObjObjFlatLinearProbingBinaryHashMap.this.remove(entryCast.getFirstKey(), entryCast.getSecondKey(), entryCast.getValue());
        }

        protected void forEachProxy(Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>> action, IntFunction<ObjObjBiMap.BiEntry<X, Y, V>> supplier) {
            final X[] keyAL = ObjObjFlatLinearProbingBinaryHashMap.this.keyATable;
            final int capL = keyAL.length;
            for (int pos = 0; pos < capL; pos++) {
                if (keyAL[pos] != ((X) NULL_KEY_A)) {
                    action.accept(supplier.apply(pos));
                }
            }
        }

        public void forEach(Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>> action) {
            this.forEachProxy(action, Node::new);
        }

        public void viewForEach(Consumer<? super ObjObjBiMap.BiEntry<X, Y, V>> action) {
            this.forEachProxy(action, this.tmpEntry::update);
        }

        public int capacity() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.remaining();
        }

        public int size() {
            return ObjObjFlatLinearProbingBinaryHashMap.this.size;
        }

        public Iterator<ObjObjBiMap.BiEntry<X, Y, V>> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<ObjObjBiMap.BiEntry<X, Y, V>> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<ObjObjBiMap.BiEntry<X, Y, V>> viewIterator() {
            return new MapEntryViewIterator();
        }

        public Spliterator<ObjObjBiMap.BiEntry<X, Y, V>> viewSpliterator() {
            return new MapEntryViewSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjFlatLinearProbingBinaryHashMap.this.clear(newCapacity);
        }
    }
}
