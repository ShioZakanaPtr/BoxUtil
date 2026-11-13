package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSpliterator;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjLongMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
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

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "deprecation", "StatementWithEmptyBody"})
public class ObjLongSeparateChainingHashMap<K> implements ObjLongMap<K>, HashContainer<K>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;

    protected transient int remaining = 0;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected long defVal = 0;
    protected final HashBehavior<K> hashBehavior;
    protected transient Node<K>[] table;

    protected transient ObjSet<K> keySet = null;
    protected transient LongCollection valueCollection = null;
    protected transient EntrySetExt<ObjLongMap.Entry<K>> entrySet = null;

    protected Node<K>[] newTable(int cap) {
        return (Node<K>[]) new Node<?>[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, clamp to range <b>[0.2f, 2.0f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLongSeparateChainingHashMap(int initialCapacity, float loadFactor, final HashBehavior<K> hashBehavior) {
        this.loadFactor = InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 2.0f);
        final int capacity = InternalCollectionUtil.checkHashCapacity(InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2)));
        this.posMask = capacity - 1;
        this.hashBehavior = Objects.requireNonNull(hashBehavior);
        this.table = this.newTable(capacity);
        this.calculateRemaining();
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLongSeparateChainingHashMap(int initialCapacity, final HashBehavior<K> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjLongSeparateChainingHashMap(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public ObjLongSeparateChainingHashMap() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 2.0f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLongSeparateChainingHashMap(final Map<? extends K, ? extends Long> source, float loadFactor, final HashBehavior<K> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.putAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLongSeparateChainingHashMap(final Map<? extends K, ? extends Long> source, final HashBehavior<K> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public ObjLongSeparateChainingHashMap(final Map<? extends K, ? extends Long> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor),
                newTargetCap = InternalCollectionUtil.checkHashCapacity(InternalCollectionUtil.getCapacityPOT(newCapacity)),
                newCap = Math.max(minSize, Math.max(newTargetCap, 2));
        if (newCap == this.table.length) return;

        final int newMask = newCap - 1;
        final Node<K>[] newTable = this.newTable(newCap), oldTable = this.table;

        if (this.size > 0) {
            for (Node<K> currNode : oldTable) {
                while (currNode != null) {
                    final int newPos = currNode.hash & newMask;
                    final Node<K> newNode = currNode;
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

    protected long removeAt(final Node<K>[] tableL, final Node<K> preNode, final Node<K> currNode, int pos) {
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

    protected Node<K> find(final Node<K>[] tableL, final Object o) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final int hash;
        for (Node<K> currNode = tableL[(hash = behavior.hashing(o)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(o, currNode.key)) return currNode;
        }
        return null;
    }

    protected long insert(final Node<K>[] tableL, int pos, int hash, final K key, final long value) {
        final Node<K> newNode = new Node<>(hash, key, value);
        newNode.next = tableL[pos];
        tableL[pos] = newNode;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return value;
    }

    public long getRaw(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final int hash;
        for (Node<K> currNode = this.table[(hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) return currNode.value;
        }
        return this.defVal;
    }

    public long getOrDefault(Object key, long defaultValue) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final int hash;
        for (Node<K> currNode = this.table[(hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) return currNode.value;
        }
        return defaultValue;
    }

    public long put(K key, long value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                return currNode.setValue(value);
            }
        }
        this.insert(tableL, pos, hash, key, value);
        return this.defVal;
    }

    public long putIfAbsent(K key, long value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                if (currNode.value != 0) return currNode.value;
                currNode.value = value;
                return 0;
            }
        }
        this.insert(tableL, pos, hash, key, value);
        return this.defVal;
    }

    public long removeRaw(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                return this.removeAt(tableL, preNode, currNode, pos);
            }
        }
        return this.defVal;
    }

    public boolean remove(Object key, long value) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                if (value == currNode.value) {
                    this.removeAt(tableL, preNode, currNode, pos);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public long replace(K key, long value) {
        final Node<K> currNode;
        return (currNode = this.find(this.table, key)) == null ? this.defVal : currNode.setValue(value);
    }

    public boolean replace(K key, long oldValue, long newValue) {
        final Node<K> currNode;
        if ((currNode = this.find(this.table, key)) != null && oldValue == currNode.value) {
            currNode.value = newValue;
            return true;
        }
        return false;
    }

    public long computeIfAbsentRaw(K key, Function<? super K, ? extends Long> mappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) return currNode.value;
        }
        final Long newValue = mappingFunction.apply(key);
        if (newValue != null) {
            return this.insert(tableL, pos, hash, key, newValue);
        }
        return this.defVal;
    }

    public long computeIfPresent(K key, ObjLongBiFunction<? super K, ? extends Long> remappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final Long newValue = remappingFunction.apply(key, currNode.value);
                if (newValue != null) return currNode.value = newValue;
                this.removeAt(tableL, preNode, currNode, pos);
                return this.defVal;
            }
        }
        return this.defVal;
    }

    public long compute(K key, ObjLongBiFunction<? super K, ? extends Long> remappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final Long newValue = remappingFunction.apply(key, currNode.value);
                if (newValue != null) return currNode.value = newValue;
                this.removeAt(tableL, preNode, currNode, pos);
                return this.defVal;
            }
        }
        final Long newValue = remappingFunction.apply(key, 0);
        if (newValue != null) {
            return this.insert(tableL, pos, hash, key, newValue);
        }
        return this.defVal;
    }

    public long merge(K key, long value, LongLongBiFunction<? extends Long> remappingFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final Long newValue = remappingFunction.apply(currNode.value, value);
                if (newValue != null) return currNode.value = newValue;
                this.removeAt(tableL, preNode, currNode, pos);
                return this.defVal;
            }
        }
        return this.insert(tableL, pos, hash, key, value);
    }

    public long update(K key, LongUnaryOperator updateFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                return currNode.value = updateFunction.applyAsLong(currNode.value);
            }
        }
        return this.insert(tableL, pos, hash, key, updateFunction.applyAsLong(this.defVal));
    }

    public long remapping(K key, ObjLongBiFunction<? super K, ? extends K> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL;
        final int hash, pos;
        for (Node<K> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(key)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) {
                final long currValue;
                final K newKey = remappingFunction.apply(key, currValue = currNode.value);
                final boolean sameKey = behavior.equals(newKey, key);
                if (newKey == null || !sameKey) this.removeAt(tableL, preNode, currNode, pos);
                if (newKey == null) return this.defVal;
                if (sameKey) return currValue;

                final int newHash, newPos;
                for (Node<K> newCurrNode = tableL[newPos = (newHash = behavior.hashing(key)) & this.posMask], newPreNode = null; newCurrNode != null; newCurrNode = (newPreNode = newCurrNode).next) {
                    if (newHash == newCurrNode.hash && behavior.equals(newKey, newCurrNode.key)) {
                        final Long newValue = mergeFunction.apply(newCurrNode.value, currValue);
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

    public void swap(Map<K, Long> source) {
        if (this == source) return;
        if (source instanceof ObjLongSeparateChainingHashMap) {
            final ObjLongSeparateChainingHashMap<K> src = (ObjLongSeparateChainingHashMap<K>) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                this.clearMappingCollection();
                src.clearMappingCollection();

                final int currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final Node<K>[] currTable = this.table, srcTable = src.table;

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
            final Map<K, Long> tmp = new ObjLongSeparateChainingHashMap<>(source);
            source.clear();
            source.putAll(this);
            this.clear();
            this.putAll(tmp);
        } else {
            final Map<K, Long> tmp = new ObjLongSeparateChainingHashMap<>(this);
            this.clear();
            this.putAll(source);
            source.clear();
            source.putAll(tmp);
        }
    }

    public boolean swapAt(K from, K to) {
        final Node<K>[] tableL = this.table;
        final Node<K> nodeA = this.find(tableL, from);
        if (nodeA == null) return false;
        final Node<K> nodeB = this.find(tableL, to);
        if (nodeB == null) return false;

        final long tmpValue = nodeA.value;
        nodeA.value = nodeB.value;
        nodeB.value = tmpValue;
        return true;
    }

    public boolean containsKey(Object key) {
        final HashBehavior<K> behavior = this.hashBehavior;
        final Node<K>[] tableL = this.table;
        final int hash;
        for (Node<K> currNode = tableL[(hash = behavior.hashing(key)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(key, currNode.key)) return true;
        }
        return false;
    }

    public boolean containsValue(long value) {
        if (this.isEmpty()) return false;

        final Node<K>[] tableL = this.table;
        for (Node<K> currNode : tableL) {
            while (currNode != null) {
                if (value == currNode.value) return true;
                currNode = currNode.next;
            }
        }
        return false;
    }

    public void forEach(ObjLongBiConsumer<? super K> action) {
        if (this.isEmpty()) return;

        final Node<K>[] tableL = this.table;
        for (Node<K> currNode : tableL) {
            while (currNode != null) {
                action.accept(currNode.key, currNode.value);
                currNode = currNode.next;
            }
        }
    }

    public void replaceAll(ObjLongBiOperator<? super K> function) {
        if (this.isEmpty()) return;

        final Node<K>[] tableL = this.table;
        for (Node<K> currNode : tableL) {
            while (currNode != null) {
                currNode.value = function.apply(currNode.key, currNode.value);
                currNode = currNode.next;
            }
        }
    }

    public void putAll(Map<? extends K, ? extends Long> m) {
        if (m == this || m.isEmpty()) return;
        if (m.size() >= this.remaining()) this.reserve(this.size + m.size() + 1);
        if (m instanceof ObjLongSeparateChainingHashMap<?>) {
            final ObjLongSeparateChainingHashMap<?> cast = (ObjLongSeparateChainingHashMap<?>) m;
            final Node<K>[] tableL = (Node<K>[]) cast.table;
            for (Node<K> currNode : tableL) {
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

    public long getDefaultReturnValueRaw() {
        return this.defVal;
    }

    public long setDefaultReturnValue(long newDefault) {
        final long oldDef = this.defVal;
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

    public ObjSet<K> keySet() {
        ObjSet<K> result;
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

    public Node<K>[] table() {
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
        final Node<K>[] tableL = this.table;
        for (Node<K> currNode : tableL) {
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
                if (castMap instanceof ObjLongSeparateChainingHashMap<?>) {
                    final ObjLongSeparateChainingHashMap<?> cast = (ObjLongSeparateChainingHashMap<?>) castMap;
                    final Node<K>[] currTableL = this.table, srcTableL = (Node<K>[]) cast.table;
                    for (Node<K> srcNode : srcTableL) {
                        while (srcNode != null) {
                            final Node<K> currNode = this.find(currTableL, srcNode.key);
                            if (!(currNode != null && srcNode.value == currNode.value)) return false;
                            srcNode = srcNode.next;
                        }
                    }
                } else {
                    final Node<K>[] tableL = this.table;
                    for (Map.Entry<?, ?> entry : castMap.entrySet()) {
                        final K srcKey = (K) entry.getKey();
                        final Long srcValue = (Long) entry.getValue();
                        final Node<K> currNode = this.find(tableL, srcKey);
                        if (currNode == null) return false;

                        if (srcValue == null && currNode.value != 0 || srcValue != null && !srcValue.equals(currNode.value)) return false;
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
        final Node<K>[] tableL = this.table;
        for (Node<K> currNode : tableL) {
            while (currNode != null) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currNode.key != this ? currNode.key : "(this Collection)")
                        .append('=')
                        .append(currNode.value);
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
        ObjLongSeparateChainingHashMap<K> result;
        try {
            result = (ObjLongSeparateChainingHashMap<K>) super.clone();
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

        final Node<K>[] tableL = this.table;
        for (Node<K> currNode : tableL) {
            while (currNode != null) {
                out.writeObject(currNode.key);
                out.writeLong(currNode.value);
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
        final Node<K>[] tableL = this.table = this.newTable(capacity);

        final HashBehavior<K> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final K currKey = (K) in.readObject();
            final long currValue = in.readLong();

            final int hash = behavior.hashing(currKey), pos = hash & maskL;
            Node<K> checkNode = tableL[pos];
            while (checkNode != null) {
                if (behavior.equals(currKey, checkNode.key)) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currKey);
                checkNode = checkNode.next;
            }

            final Node<K> newNode = new Node<>(hash, currKey, currValue);
            newNode.next = tableL[pos];
            tableL[pos] = newNode;
        }
        this.calculateRemaining();
    }

    protected long[] prepareArray(final long[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return new long[sizeL];
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected long[] valueToArray(final long[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final Node<K>[] tableL = this.table;
        for (Node<K> currNode : tableL) {
            while (currNode != null) {
                a[idx++] = currNode.value;
                currNode = currNode.next;
            }
        }
        return a;
    }

    public static class Node<K> implements ObjLongMap.Entry<K> {
        protected final int hash;
        protected final K key;
        protected long value;
        public Node<K> next;

        protected Node(int hash, K key, long value) {
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

        public long getValueRaw() {
            return this.value;
        }

        public long setValue(long newValue) {
            final long oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public int hashCode() {
            return Objects.hashCode(this.key) ^ Long.hashCode(this.value);
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
        protected Node<K> preNode = null;
        protected Node<K> currNode = null;
        protected Node<K> nextNode = null;

        protected MapIterator() {
            if (ObjLongSeparateChainingHashMap.this.size > 0) {
                this.scanNode();
            }
        }

        protected void scanNode() {
            final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table;
            final int capacity = tableL.length;
            int posL = this.pos;
            while (posL < capacity && (this.nextNode = tableL[posL++]) == null) {}
            this.pos = posL;
        }

        public boolean hasNext() {
            return this.nextNode != null;
        }

        public Node<K> nextMapNode() {
            if (this.nextNode == null) throw new NoSuchElementException();

            if (this.currNode != null) this.preNode = this.currNode;
            final Node<K> result = this.nextNode;
            if ((this.nextNode = (this.currNode = result).next) == null) {
                this.scanNode();
            }
            return result;
        }

        public void remove() {
            if (this.currNode == null) throw new IllegalStateException();

            final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table;
            final Node<K> preL = this.preNode, currL = this.currNode;
            final int pos = currL.hash & ObjLongSeparateChainingHashMap.this.posMask;

            if (preL != null && preL.next == currL) {
                preL.next = currL.next;
            } else {
                tableL[pos] = currL.next;
                this.preNode = null;
            }
            currL.next = null;
            ObjLongSeparateChainingHashMap.this.size--;
            ObjLongSeparateChainingHashMap.this.remaining++;
            this.currNode = null;
        }
    }

    protected abstract class MapSpliterator<C, S> {
        protected int index;
        protected int limitCap;
        protected int estSize;
        protected Node<K> currNode = null;

        protected MapSpliterator(int index, int limitCap, int estSize) {
            this.index = index;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = ObjLongSeparateChainingHashMap.this.size;
                newLimit = this.limitCap = ObjLongSeparateChainingHashMap.this.table.length;
            }
            return newLimit;
        }

        protected abstract S splitProxy(int index, int limitCap, int estSize);

        public S trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex && this.currNode == null ? this.splitProxy(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        protected abstract void consumerProxy(C action, Node<K> node);

        public boolean tryAdvance(C action) {
            final int currLimit = this.getLimitCap();
            for (final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table; this.currNode != null || this.index < currLimit;) {
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
            for (final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table; this.currNode != null || this.index < currLimit;) {
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
            return (this.limitCap < 0 || this.estSize == ObjLongSeparateChainingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }

    protected class KeyIterator extends MapIterator implements Iterator<K> {
        public K next() {
            return this.nextMapNode().key;
        }
    }

    protected class ValueIterator extends MapIterator implements LongIterator {
        public long nextLong() {
            return this.nextMapNode().value;
        }
    }

    protected class MapEntryIterator extends MapIterator implements Iterator<ObjLongMap.Entry<K>> {
        public ObjLongMap.Entry<K> next() {
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

        protected void consumerProxy(Consumer<? super K> action, Node<K> node) {
            action.accept(node.key);
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjLongSeparateChainingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }

    protected class ValueSpliterator extends MapSpliterator<LongConsumer, LongSpliterator> implements LongSpliterator {
        protected ValueSpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected LongSpliterator splitProxy(int index, int limitCap, int estSize) {
            return new ValueSpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(LongConsumer action, Node<K> node) {
            action.accept(node.value);
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjLongSeparateChainingHashMap.this.size ? Spliterator.SIZED : 0) | Spliterator.NONNULL;
        }
    }

    protected class MapEntrySpliterator extends MapSpliterator<Consumer<? super ObjLongMap.Entry<K>>, Spliterator<ObjLongMap.Entry<K>>> implements Spliterator<ObjLongMap.Entry<K>> {
        protected MapEntrySpliterator(int index, int limitCap, int estSize) {
            super(index, limitCap, estSize);
        }

        protected Spliterator<ObjLongMap.Entry<K>> splitProxy(int index, int limitCap, int estSize) {
            return new MapEntrySpliterator(index, limitCap, estSize);
        }

        protected void consumerProxy(Consumer<? super ObjLongMap.Entry<K>> action, Node<K> node) {
            action.accept(node);
        }
    }

    protected class KeySet extends AbstractObjSet<K> {
        public void resize(int newCapacity) {
            ObjLongSeparateChainingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjLongSeparateChainingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            return ObjLongSeparateChainingHashMap.this.containsKey(o);
        }

        public boolean removeElement(Object o) {
            final HashBehavior<K> behavior = ObjLongSeparateChainingHashMap.this.hashBehavior;
            final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table;
            final int hash = behavior.hashing(o), pos;
            for (Node<K> currNode = tableL[pos = hash & ObjLongSeparateChainingHashMap.this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
                if (hash == currNode.hash && behavior.equals(o, currNode.key)) {
                    ObjLongSeparateChainingHashMap.this.removeAt(tableL, preNode, currNode, pos);
                    return true;
                }
            }
            return false;
        }

        public void forEach(Consumer<? super K> action) {
            if (this.isEmpty()) return;

            final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table;
            for (Node<K> currNode : tableL) {
                while (currNode != null) {
                    action.accept(currNode.key);
                    currNode = currNode.next;
                }
            }
        }

        public int capacity() {
            return ObjLongSeparateChainingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjLongSeparateChainingHashMap.this.remaining();
        }

        public int size() {
            return ObjLongSeparateChainingHashMap.this.size;
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public Spliterator<K> spliterator() {
            return new KeySpliterator(0, -1, 0);
        }

        public void clear() {
            ObjLongSeparateChainingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjLongSeparateChainingHashMap.this.clear(newCapacity);
        }
    }

    protected class ValueCollection extends AbstractLongCollection {
        public void resize(int newCapacity) {
            ObjLongSeparateChainingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjLongSeparateChainingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(long o) {
            return ObjLongSeparateChainingHashMap.this.containsValue(o);
        }

        public void forEach(LongConsumer action) {
            if (ObjLongSeparateChainingHashMap.this.isEmpty()) return;

            final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table;
            for (Node<K> currNode : tableL) {
                while (currNode != null) {
                    action.accept(currNode.value);
                    currNode = currNode.next;
                }
            }
        }

        public int capacity() {
            return ObjLongSeparateChainingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjLongSeparateChainingHashMap.this.remaining();
        }

        public int size() {
            return ObjLongSeparateChainingHashMap.this.size;
        }

        public LongIterator iterator() {
            return new ValueIterator();
        }

        public LongSpliterator spliterator() {
            return new ValueSpliterator(0, -1, 0);
        }

        public void clear() {
            ObjLongSeparateChainingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjLongSeparateChainingHashMap.this.clear(newCapacity);
        }

        public long[] toArrayRaw() {
            return ObjLongSeparateChainingHashMap.this.valueToArray(new long[ObjLongSeparateChainingHashMap.this.size]);
        }

        public long[] toArray(long[] a) {
            return ObjLongSeparateChainingHashMap.this.valueToArray(ObjLongSeparateChainingHashMap.this.prepareArray(a));
        }
    }

    protected class MapEntrySet extends AbstractObjSet<ObjLongMap.Entry<K>> implements EntrySetExt<ObjLongMap.Entry<K>> {
        public void resize(int newCapacity) {
            ObjLongSeparateChainingHashMap.this.resize(newCapacity);
        }

        public void reserve(int newMaxSize) {
            ObjLongSeparateChainingHashMap.this.reserve(newMaxSize);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjLongSeparateChainingHashMap.this.containsKey(entryCast.getKey());
        }

        public boolean removeElement(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) return false;
            final Map.Entry<?, ?> entryCast = (Map.Entry<?, ?>) o;
            return ObjLongSeparateChainingHashMap.this.remove(entryCast.getKey(), entryCast.getValue());
        }

        public void forEach(Consumer<? super ObjLongMap.Entry<K>> action) {
            if (this.isEmpty()) return;

            final Node<K>[] tableL = ObjLongSeparateChainingHashMap.this.table;
            for (Node<K> currNode : tableL) {
                while (currNode != null) {
                    action.accept(currNode);
                    currNode = currNode.next;
                }
            }
        }

        public void viewForEach(Consumer<? super ObjLongMap.Entry<K>> action) {
            this.forEach(action);
        }

        public int capacity() {
            return ObjLongSeparateChainingHashMap.this.capacity();
        }

        public int remaining() {
            return ObjLongSeparateChainingHashMap.this.remaining();
        }

        public int size() {
            return ObjLongSeparateChainingHashMap.this.size;
        }

        public Iterator<ObjLongMap.Entry<K>> iterator() {
            return new MapEntryIterator();
        }

        public Spliterator<ObjLongMap.Entry<K>> spliterator() {
            return new MapEntrySpliterator(0, -1, 0);
        }

        public Iterator<ObjLongMap.Entry<K>> viewIterator() {
            return this.iterator();
        }

        public Spliterator<ObjLongMap.Entry<K>> viewSpliterator() {
            return this.spliterator();
        }

        public void clear() {
            ObjLongSeparateChainingHashMap.this.clear();
        }

        public void clear(int newCapacity) {
            ObjLongSeparateChainingHashMap.this.clear(newCapacity);
        }
    }
}
