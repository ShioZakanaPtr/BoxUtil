package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.*;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjObjMap;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "StatementWithEmptyBody"})
public class ObjObjSeparateChainingHashMap<K, V> implements ObjObjMap<K, V>, HashContainer<K>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;

    protected transient int remaining = 0;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected V defVal = null;
    protected final HashBehavior<K> hashBehavior;
    protected transient Node<K, V>[] table;

    protected transient SetExt<K> keySet = null;
    protected transient CollectionExt<V> valueCollection = null;
    protected transient EntrySetExt<Entry<K, V>> entrySet = null;

    protected Node<K, V>[] newTable(int cap) {
        return (Node<K, V>[]) new Node<?, ?>[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, clamp to range <b>[0.2f, 2.0f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjSeparateChainingHashMap(int initialCapacity, float loadFactor, final HashBehavior<K> hashBehavior) {
        InternalCollectionUtil.checkHashCapacity(initialCapacity);
        InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 2.0f);

        this.loadFactor = loadFactor;
        final int capacity = InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2));
        this.posMask = capacity - 1;
        this.hashBehavior = Objects.requireNonNull(hashBehavior);
        this.table = this.newTable(capacity);
        this.calculateRemaining();
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjSeparateChainingHashMap(int initialCapacity, final HashBehavior<K> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjObjSeparateChainingHashMap(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public ObjObjSeparateChainingHashMap() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 2.0f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjSeparateChainingHashMap(final Map<? extends K, ? extends V> source, float loadFactor, final HashBehavior<K> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.putAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjObjSeparateChainingHashMap(final Map<? extends K, ? extends V> source, final HashBehavior<K> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public ObjObjSeparateChainingHashMap(final Map<? extends K, ? extends V> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor), newTargetCap = InternalCollectionUtil.getCapacityPOT(newCapacity);
        InternalCollectionUtil.checkHashCapacity(newTargetCap);
        if (newTargetCap == this.table.length) return;

        final int newCap = Math.max(minSize, Math.max(newTargetCap, 2)),
                newMask = newCap - 1;
        final Node<K, V>[] newTable = this.newTable(newCap), oldTable = this.table;

        if (this.size > 0) {
            for (Node<K, V> currNode : oldTable) {
                while (currNode != null) {
                    final int newPos = currNode.hash & newMask;
                    final Node<K, V> newNode = currNode;
                    currNode = currNode.next;
                    newNode.next = newTable[newPos];
                    newTable[newPos] = newNode;
                }
            }
        }

        this.posMask = newMask;
        this.table = newTable;
        this.calculateRemaining();
    }

    public void reserve(int newMaxSize) {
        if (newMaxSize < 1) throw new IllegalArgumentException("Not a valid size: '" + newMaxSize + '\'');
        this.resize(InternalCollectionUtil.getHashTableCapacity(newMaxSize, this.loadFactor));
    }

    protected V removeAt(final Node<K, V>[] tableL, final Node<K, V> preNode, final Node<K, V> currNode, int pos) {
        if (preNode != null) preNode.next = currNode.next;
        else tableL[pos] = currNode.next;
        currNode.next = null;
        this.size--;
        this.remaining++;
        return currNode.value;
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.table.length));
    }

    protected Node<K, V> find(final Node<K, V>[] tableL, final Object o) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final int hash;
        for (Node<K, V> currNode = tableL[(hash = behavior.hashing(o)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(o, currNode.key)) return currNode;
        }
        return null;
    }

    protected V insert(final Node<K, V>[] tableL, int pos, int hash, final K key, final V value) {
        final Node<K, V> newNode = new Node<>(hash, key, value);
        newNode.next = tableL[pos];
        tableL[pos] = newNode;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return value;
    }

    public V get(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final int hash;
        for (Node<K, V> currNode = this.table[(hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) return currNode.value;
        }
        return this.defVal;
    }

    public V getOrDefault(Object key, V defaultValue) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final int hash;
        for (Node<K, V> currNode = this.table[(hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) return currNode.value;
        }
        return defaultValue;
    }

    public V put(K key, V value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                return currNode.setValue(value);
            }
        }
        this.insert(tableL, pos, hash, key, value);
        return this.defVal;
    }

    public V putIfAbsent(K key, V value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                if (currNode.value != null) return currNode.value;
                currNode.value = value;
                return null;
            }
        }
        this.insert(tableL, pos, hash, key, value);
        return this.defVal;
    }

    public V remove(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                return this.removeAt(tableL, preNode, currNode, pos);
            }
        }
        return this.defVal;
    }

    public boolean remove(Object key, Object value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                if (Objects.equals(value, currNode.value)) {
                    this.removeAt(tableL, preNode, currNode, pos);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public V replace(K key, V value) {
        final Node<K, V> currNode;
        return (currNode = this.find(this.table, key)) == null ? this.defVal : currNode.setValue(value);
    }

    public boolean replace(K key, V oldValue, V newValue) {
        final Node<K, V> currNode;
        if ((currNode = this.find(this.table, key)) != null && Objects.equals(oldValue, currNode.value)) {
            currNode.value = newValue;
            return true;
        }
        return false;
    }

    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final V oldValue;
                if ((oldValue = currNode.value) != null) return oldValue;

                final V newValue = mappingFunction.apply(key);
                if (newValue != null) return currNode.value = newValue;
                this.removeAt(tableL, preNode, currNode, pos);
                return this.defVal;
            }
        }
        final V newValue = mappingFunction.apply(key);
        if (newValue != null) {
            return this.insert(tableL, pos, hash, key, newValue);
        }
        return this.defVal;
    }

    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                if (currNode.value == null) return this.defVal;

                final V newValue = remappingFunction.apply(key, currNode.value);
                if (newValue != null) return currNode.value = newValue;
                this.removeAt(tableL, preNode, currNode, pos);
                return this.defVal;
            }
        }
        return this.defVal;
    }

    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final V newValue = remappingFunction.apply(key, currNode.value);
                if (newValue != null) return currNode.value = newValue;
                this.removeAt(tableL, preNode, currNode, pos);
                return this.defVal;
            }
        }
        final V newValue = remappingFunction.apply(key, null);
        if (newValue != null) {
            return this.insert(tableL, pos, hash, key, newValue);
        }
        return this.defVal;
    }

    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final V oldValue;
                final V newValue = (oldValue = currNode.value) != null ? remappingFunction.apply(oldValue, value) : value;
                if (newValue != null) return currNode.value = newValue;
                this.removeAt(tableL, preNode, currNode, pos);
                return this.defVal;
            }
        }
        return this.insert(tableL, pos, hash, key, value);
    }

    public V update(K key, Function<? super V, ? extends V> updateFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                return currNode.value = updateFunction.apply(currNode.value);
            }
        }
        final V newValue = updateFunction.apply(this.defVal);
        return this.insert(tableL, pos, hash, key, newValue);
    }

    public V remapping(K key, BiFunction<? super K, ? super V, ? extends K> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL;
        final int hash, pos;
        for (Node<K, V> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final V currValue;
                final K newKey = remappingFunction.apply(key, currValue = currNode.value);
                final boolean sameKey = behavior.equals(newKey, key);
                if (newKey == null || !sameKey) this.removeAt(tableL, preNode, currNode, pos);
                if (newKey == null) return this.defVal;
                if (sameKey) return currValue;

                final int newHash, newPos;
                for (Node<K, V> newCurrNode = tableL[newPos = (newHash = behavior.hashing(key)) & this.posMask], newPreNode = null; newCurrNode != null; newCurrNode = (newPreNode = newCurrNode).next) {
                    if (newHash == newCurrNode.hash && behavior.equals(newKey, newCurrNode.key)) {
                        final V newValue = mergeFunction.apply(newCurrNode.value, currValue);
                        if (newValue != null) return newCurrNode.value = newValue;
                        this.removeAt(tableL, newPreNode, currNode, newPos);
                        return this.defVal;
                    }
                }
                return this.insert(tableL, newPos, newHash, newKey, currValue);
            }
        }
        return this.defVal;
    }

    public void swap(Map<K, V> source) {
        if (this == source) return;
        if (source instanceof ObjObjSeparateChainingHashMap) {
            final ObjObjSeparateChainingHashMap<K, V> src = (ObjObjSeparateChainingHashMap<K, V>) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                this.clearMappingCollection();
                src.clearMappingCollection();

                final int currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final Node<K, V>[] currTable = this.table, srcTable = src.table;

                src.size = currSize;
                src.posMask = currMask;
                src.remaining = currRemaining;
                src.table = currTable;
                this.size = srcSize;
                this.posMask = srcMask;
                this.remaining = srcRemaining;
                this.table = srcTable;
                return;
            }
        }
        if (source.size() < this.size()) {
            final Map<K, V> tmp = new ObjObjSeparateChainingHashMap<>(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<K, V> tmp = new ObjObjSeparateChainingHashMap<>(this);
            this.clear();
            this.putAll(source);
            source.clear();
            source.putAll(tmp);
        }
    }

    public boolean swapAt(K from, K to) {
        final Node<K, V>[] tableL = this.table;
        final Node<K, V> nodeA = this.find(tableL, from);
        if (nodeA == null) return false;
        final Node<K, V> nodeB = this.find(tableL, to);
        if (nodeB == null) return false;

        final V tmpValue = nodeA.value;
        nodeA.value = nodeB.value;
        nodeB.value = tmpValue;
        return true;
    }

    public boolean containsKey(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K, V>[] tableL = this.table;
        final int hash;
        for (Node<K, V> currNode = tableL[(hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) return true;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        if (this.isEmpty()) return false;

        final Node<K, V>[] tableL = this.table;
        for (Node<K, V> currNode : tableL) {
            while (currNode != null) {
                if (Objects.equals(value, currNode.value)) return true;
                currNode = currNode.next;
            }
        }
        return false;
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        if (this.isEmpty()) return;

        final Node<K, V>[] tableL = this.table;
        for (Node<K, V> currNode : tableL) {
            while (currNode != null) {
                action.accept(currNode.key, currNode.value);
                currNode = currNode.next;
            }
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        if (this.isEmpty()) return;

        final Node<K, V>[] tableL = this.table;
        for (Node<K, V> currNode : tableL) {
            while (currNode != null) {
                currNode.value = function.apply(currNode.key, currNode.value);
                currNode = currNode.next;
            }
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == this || m.isEmpty()) return;
        if (m.size() >= this.remaining()) this.reserve(this.size + m.size() + 1);
        if (m instanceof ObjObjSeparateChainingHashMap<?, ?>) {
            final ObjObjSeparateChainingHashMap<?, ?> cast = (ObjObjSeparateChainingHashMap<?, ?>) m;
            final Node<K, V>[] tableL = (Node<K, V>[]) cast.table;
            for (Node<K, V> currNode : tableL) {
                while (currNode != null) {
                    this.put(currNode.key, currNode.value);
                    currNode = currNode.next;
                }
            }
        } else m.forEach(this::put);
    }

    private void calculateRemaining() {
        this.remaining = InternalCollectionUtil.getHashTableMaxRemaining(this.table.length, this.loadFactor, true) - this.size;
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
        return this.table.length;
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

    public Node<K, V>[] table() {
        return this.table;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        Arrays.fill(this.table, null);
        this.calculateRemaining();
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        final int newCap = InternalCollectionUtil.getCapacityPOT(Math.max(newCapacity, 2));
        if (newCap == this.table.length) {
            this.clear();
        } else {
            this.posMask = newCap - 1;
            this.size = 0;
            this.table = this.newTable(newCap);
            this.calculateRemaining();
        }
    }

    public int hashCode() {
        if (this.isEmpty()) return 0;

        int result = 0;
        final Node<K, V>[] tableL = this.table;
        for (Node<K, V> currNode : tableL) {
            while (currNode != null) {
                result += currNode.hashCode();
                currNode = currNode.next;
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
                if (castMap instanceof ObjObjSeparateChainingHashMap<?, ?>) {
                    final ObjObjSeparateChainingHashMap<?, ?> cast = (ObjObjSeparateChainingHashMap<?, ?>) castMap;
                    final Node<K, V>[] currTableL = this.table, srcTableL = (Node<K, V>[]) cast.table;
                    for (Node<K, V> srcNode : srcTableL) {
                        while (srcNode != null) {
                            final Node<K, V> currNode = this.find(currTableL, srcNode.key);
                            if (!(currNode != null && Objects.equals(srcNode.value, currNode.value))) return false;
                            srcNode = srcNode.next;
                        }
                    }
                } else {
                    final Node<K, V>[] tableL = this.table;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final K srcKey = (K) entry.getKey();
                        final V srcValue = (V) entry.getValue();
                        final Node<K, V> currNode = this.find(tableL, srcKey);
                        if (currNode == null) return false;

                        if (srcValue == null && currNode.value != null || srcValue != null && !srcValue.equals(currNode.value)) return false;
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
        if (this.isEmpty()) return "[]";

        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        final Node<K, V>[] tableL = this.table;
        for (Node<K, V> currNode : tableL) {
            while (currNode != null) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currNode.key != this ? currNode.key : "(this Collection)")
                        .append('=')
                        .append(currNode.value != this ? currNode.value : "(this Collection)");
                currNode = currNode.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    protected void clearMappingCollection() {
        this.keySet = null;
        this.valueCollection = null;
        this.entrySet = null;
    }

    public Object clone() {
        ObjObjSeparateChainingHashMap<K, V> result;
        try {
            result = (ObjObjSeparateChainingHashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.table.length;
        result.size = 0;
        result.table = result.newTable(srcLen);
        result.clearMappingCollection();
        result.calculateRemaining();
        result.putAll(this);
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.isEmpty()) return;

        final Node<K, V>[] tableL = this.table;
        for (Node<K, V> currNode : tableL) {
            while (currNode != null) {
                out.writeObject(currNode.key);
                out.writeObject(currNode.value);
                currNode = currNode.next;
            }
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehavior);
        InternalCollectionUtil.SerialCheck.ioCheckHashLoadFactor(this.loadFactor, 0.2f, 0.9f);
        InternalCollectionUtil.SerialCheck.ioCheckSize(this.size, 0, HashContainer.MAXIMUM_CAPACITY);
        final int capacity = InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(this.size, this.loadFactor, HashContainer.MAXIMUM_CAPACITY),
                maskL = this.posMask = capacity - 1;
        final Node<K, V>[] tableL = this.table = this.newTable(capacity);

        final HashBehavior<K> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final K currKey = (K) in.readObject();
            final V currValue = (V) in.readObject();

            final int hash = behavior.hashing(currKey), pos = hash & maskL;
            Node<K, V> checkNode = tableL[pos];
            while (checkNode != null) {
                if (behavior.equals(currKey, checkNode.key)) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                checkNode = checkNode.next;
            }

            final Node<K, V> newNode = new Node<>(hash, currKey, currValue);
            newNode.next = tableL[pos];
            tableL[pos] = newNode;
        }
        this.calculateRemaining();
    }

    public static class Node<K, V> implements ObjObjMap.Entry<K, V> {
        protected final int hash;
        protected final K key;
        protected V value;
        public Node<K, V> next;

        protected Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public int getHash() {
            return this.hash;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V newValue) {
            final V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public int hashCode() {
            return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> cast = (Map.Entry<?, ?>) obj;
            return Objects.equals(this.key, cast.getKey())
                    && Objects.equals(this.value, cast.getValue());
        }
    }

    protected class MapIterator {
        protected int pos = 0;
        protected Node<K, V> preNode = null;
        protected Node<K, V> currNode = null;
        protected Node<K, V> nextNode = null;

        protected MapIterator() {
            if (ObjObjSeparateChainingHashMap.this.size > 0) {
                this.scanNode();
            }
        }

        protected void scanNode() {
            final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table;
            final int capacity = tableL.length;
            int posL = this.pos;
            while (posL < capacity && (this.nextNode = tableL[posL++]) == null) {}
            this.pos = posL;
        }

        public boolean hasNext() {
            return this.nextNode != null;
        }

        public Node<K, V> nextMapNode() {
            if (this.nextNode == null) throw new NoSuchElementException();

            if (this.currNode != null) this.preNode = this.currNode;
            final Node<K, V> result = this.nextNode;
            if ((this.nextNode = (this.currNode = result).next) == null) {
                this.scanNode();
            }
            return result;
        }

        public void remove() {
            if (this.currNode == null) throw new IllegalStateException();

            final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table;
            final Node<K, V> preL = this.preNode, currL = this.currNode;
            final int pos = currL.hash & ObjObjSeparateChainingHashMap.this.posMask;

            if (preL != null && preL.next == currL) {
                preL.next = currL.next;
            } else {
                tableL[pos] = currL.next;
                this.preNode = null;
            }
            currL.next = null;
            ObjObjSeparateChainingHashMap.this.size--;
            ObjObjSeparateChainingHashMap.this.remaining++;
            this.currNode = null;
        }
    }

    protected abstract class MapSpliterator<C, S> {
        protected int index;
        protected int limitCap;
        protected int estSize;
        protected Node<K, V> currNode = null;

        protected MapSpliterator(int index, int limitCap, int estSize) {
            this.index = index;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = ObjObjSeparateChainingHashMap.this.size;
                newLimit = this.limitCap = ObjObjSeparateChainingHashMap.this.table.length;
            }
            return newLimit;
        }

        protected abstract S splitProxy(int index, int limitCap, int estSize);

        public S trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex && this.currNode == null ? this.splitProxy(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        protected abstract void consumerProxy(C action, Node<K, V> node);

        public boolean tryAdvance(C action) {
            final int currLimit = this.getLimitCap();
            for (final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table; this.currNode != null || this.index < currLimit;) {
                if (this.currNode == null) this.currNode = tableL[this.index++];
                if (this.currNode != null) {
                    this.consumerProxy(action, this.currNode);
                    this.currNode = this.currNode.next;
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(C action) {
            final int currLimit = this.getLimitCap();
            for (final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table; this.currNode != null || this.index < currLimit;) {
                if (this.currNode == null) this.currNode = tableL[this.index++];
                while (this.currNode != null) {
                    this.consumerProxy(action, this.currNode);
                    this.currNode = this.currNode.next;
                }
            }
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjObjSeparateChainingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }

    protected class KeyIterator extends MapIterator implements Iterator<K> {
        public K next() {
            return this.nextMapNode().key;
        }
    }

    protected class ValueIterator extends MapIterator implements Iterator<V> {
        public V next() {
            return this.nextMapNode().value;
        }
    }

    protected class MapEntryIterator extends MapIterator implements Iterator<ObjObjMap.Entry<K, V>> {
        public ObjObjMap.Entry<K, V> next() {
            return this.nextMapNode();
        }
    }

    protected class KeySpliterator extends MapSpliterator<Consumer<? super K>, Spliterator<K>> implements Spliterator<K> {
        protected KeySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<K> splitProxy(int index, int limitCap, int estSize) {
            return new KeySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super K> action, Node<K, V> node) {
            action.accept(node.key);
        }
    }

    protected class ValueSpliterator extends MapSpliterator<Consumer<? super V>, Spliterator<V>> implements Spliterator<V> {
        protected ValueSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<V> splitProxy(int index, int limitCap, int estSize) {
            return new ValueSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super V> action, Node<K, V> node) {
            action.accept(node.value);
        }

        public int characteristics() {
            return this.limitCap < 0 || this.estSize == ObjObjSeparateChainingHashMap.this.size ? Spliterator.SIZED : 0;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super ObjObjMap.Entry<K, V>>, Spliterator<ObjObjMap.Entry<K, V>>> implements Spliterator<ObjObjMap.Entry<K, V>> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjObjMap.Entry<K, V>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjObjMap.Entry<K, V>> action, Node<K, V> node) {
            action.accept(node);
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjObjSeparateChainingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class KeySet extends AbstractObjSet<K> {
        public void resize(int newCapacity) {
            ObjObjSeparateChainingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjSeparateChainingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjObjSeparateChainingHashMap.this.containsKey(o);
        }

        public boolean removeElement(Object o) {
            final HashBehavior<K> behavior = ObjObjSeparateChainingHashMap.this.hashBehavior;
            final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table;
            final int hash = behavior.hashing(o), pos;
            for (Node<K, V> currNode = tableL[pos = hash & ObjObjSeparateChainingHashMap.this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
                if (hash == currNode.hash && behavior.equals(o, currNode.key)) {
                    ObjObjSeparateChainingHashMap.this.removeAt(tableL, preNode, currNode, pos);
                    return true;
                }
            }
            return false;
        }

        public void forEach(Consumer<? super K> action) {
            if (this.isEmpty()) return;

            final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table;
            for (Node<K, V> currNode : tableL) {
                while (currNode != null) {
                    action.accept(currNode.key);
                    currNode = currNode.next;
                }
            }
        }

        public int capacity() {
            return ObjObjSeparateChainingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjSeparateChainingHashMap.this.remaining();
        }

        public int size() {
            return ObjObjSeparateChainingHashMap.this.size;
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public Spliterator<K> spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjSeparateChainingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjSeparateChainingHashMap.this.clear(newCapacity);
        }
    }

    protected class ValueCollection extends AbstractObjCollection<V> {
        public void resize(int newCapacity) {
            ObjObjSeparateChainingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjSeparateChainingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjObjSeparateChainingHashMap.this.containsValue(o);
        }

        public void forEach(Consumer<? super V> action) {
            if (ObjObjSeparateChainingHashMap.this.isEmpty()) return;

            final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table;
            for (Node<K, V> currNode : tableL) {
                while (currNode != null) {
                    action.accept(currNode.value);
                    currNode = currNode.next;
                }
            }
        }

        public int capacity() {
            return ObjObjSeparateChainingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjSeparateChainingHashMap.this.remaining();
        }

        public int size() {
            return ObjObjSeparateChainingHashMap.this.size;
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjObjSeparateChainingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjSeparateChainingHashMap.this.clear(newCapacity);
        }
    }

    protected class MapEntrySet extends AbstractObjSet<ObjObjMap.Entry<K, V>> implements EntrySetExt<ObjObjMap.Entry<K, V>> {
        public void resize(int newCapacity) {
            ObjObjSeparateChainingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjObjSeparateChainingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjObjSeparateChainingHashMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjObjSeparateChainingHashMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        public void forEach(Consumer<? super ObjObjMap.Entry<K, V>> action) {
            if (this.isEmpty()) return;

            final Node<K, V>[] tableL = ObjObjSeparateChainingHashMap.this.table;
            for (Node<K, V> currNode : tableL) {
                while (currNode != null) {
                    action.accept(currNode);
                    currNode = currNode.next;
                }
            }
        }

        public void viewForEach(Consumer<? super ObjObjMap.Entry<K, V>> action) {
            this.forEach(action);
        }

        public int capacity() {
            return ObjObjSeparateChainingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjObjSeparateChainingHashMap.this.remaining();
        }

        public int size() {
            return ObjObjSeparateChainingHashMap.this.size;
        }

        public Iterator<ObjObjMap.Entry<K, V>> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<ObjObjMap.Entry<K, V>> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<ObjObjMap.Entry<K, V>> viewIterator() {
            return this.iterator();
        }

        public Spliterator<ObjObjMap.Entry<K, V>> viewSpliterator() {
            return this.spliterator();
        }

        public void clear() {
            ObjObjSeparateChainingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjObjSeparateChainingHashMap.this.clear(newCapacity);
        }
    }
}
