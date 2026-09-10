package org.boxutil.util.container;

import org.boxutil.define.BoxEnum;
import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.Sys;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The map is not thread safe.<p>
 * Unbounded unordered hash map, based Robin Hood hashing.<p>
 * Key or value both can be <code>null</code>.<p>
 * The default hasher using {@link ContainerHasher#hashing_HashMap(Object)}.<p>
 * This Map implementation is suitable for scenarios with many reads and few writes,
 * or you can ensure that resizing is not triggered too frequently,
 * also and memory usage is not a primary concern.<p>
 * Otherwise, it is still recommended to use {@link HashMap} or another more appropriate implementation.
 */
@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused"})
public class Obj2ObjRHMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable {
    public static final int DEFAULT_INITIAL_CAPACITY = 1 << 7;
    public static final int MAXIMUM_CAPACITY = 1 << 29;

    protected byte op = 0;
    protected int capacity;
    protected int posMask;
    protected int size = 0;
    protected float threshold = 0.0f;
    protected final float loadFactor;
    protected final ContainerHasher hasher;
    protected byte[] state; // -1 => empty, (x > -1) => psl
    protected Object[] kvs; // i = {iKey, iValue}

    protected Set<K> keyViewer;
    protected Collection<V> valueViewer;
    protected Set<Entry<K, V>> entryViewer;

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, clamp to range <b>[0.2f, 0.9f]</b>.
     * @param hasher when a key is <code>null</code>, it hash must be <code>0</code>
     */
    public Obj2ObjRHMap(int initialCapacity, float loadFactor, final ContainerHasher hasher) {
        if (initialCapacity > MAXIMUM_CAPACITY || initialCapacity < 1) throw new IllegalArgumentException("The capacity of map overflow");
        this.loadFactor = Math.max(Math.min(loadFactor, 0.91f), 0.2f);
        this.capacity = CalculateUtil.getPOTMax(Math.max(initialCapacity, 2));
        final int realCap = this.capacity << 1;
        this.posMask = this.capacity - 1;
        this.hasher = Objects.requireNonNull(hasher);
        this.state = new byte[this.capacity];
        this.kvs = new Object[realCap];
        Arrays.fill(this.state, BoxEnum.NEG_ONE);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hasher when a key is <code>null</code>, it hash must be <code>0</code>
     */
    public Obj2ObjRHMap(int initialCapacity, final ContainerHasher hasher) {
        this(initialCapacity, 0.75f, hasher);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public Obj2ObjRHMap(int initialCapacity) {
        this(initialCapacity, ContainerHasher::hashing_HashMap);
    }

    public Obj2ObjRHMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public int capacity() {
        return this.capacity;
    }

    public float getCurrentThreshold() {
        return this.threshold;
    }

    public float getLoadFactor() {
        return this.loadFactor;
    }

    public ContainerHasher getHasher() {
        return this.hasher;
    }

    protected interface ObjExc<V> {
        V run(int statePos, int keyPos, int valuePos);
    }

    protected interface BoolExc {
        boolean run(int statePos, int keyPos, int valuePos);
    }

    protected void calculateThreshold() {
        this.threshold = (float) this.size / (float) this.capacity;
    }

    protected int getReqCap(int extraExpand) {
        final long reqCap = (long) this.capacity << extraExpand;
        if (reqCap < 1 || reqCap > MAXIMUM_CAPACITY) throw new IllegalStateException("The capacity of map overflow; split your KVs to different map or check your hasher and using better one");
        return Math.toIntExact(reqCap);
    }

    protected void checkAndResize(boolean forced, int extraExpand) {
        if (this.threshold >= this.loadFactor || forced) {
            final int reqCap = getReqCap(extraExpand);

            final int newPosMask = reqCap - 1;
            final byte[] newState = new byte[reqCap];
            final Object[] newKvs = new Object[reqCap << 1];
            Arrays.fill(newState, BoxEnum.NEG_ONE);

            for (int i = 0; i < this.capacity; i++) {
                if (this.state[i] < 0) continue;

                final int realOldPos = i << 1;
                Object oldKey = this.kvs[realOldPos], oldValue = this.kvs[realOldPos + 1];

                int newPos = this.hasher.hashing(oldKey) & newPosMask, realNewPos = newPos << 1;
                byte currPsl = 0, currState;

                final int startPos = newPos;
                while ((currState = newState[newPos]) > -1) {
                    if (currPsl > currState) {
                        final byte tmpState = newState[newPos];
                        final int realNewValPos = realNewPos + 1;
                        final Object tmpKey = newKvs[realNewPos], tmpValue = newKvs[realNewValPos];

                        newState[newPos] = currPsl;
                        currPsl = tmpState;
                        newKvs[realNewPos] = oldKey;
                        oldKey = tmpKey;
                        newKvs[realNewValPos] = oldValue;
                        oldValue = tmpValue;
                    }
                    newPos = ++newPos & newPosMask;
                    realNewPos = newPos << 1;
                    if (++currPsl < -1 || newPos == startPos) { // you should goto use better hasher
                        this.checkAndResize(true, extraExpand + 1);
                        return;
                    }
                }

                newState[newPos] = currPsl;
                newKvs[realNewPos] = oldKey;
                newKvs[realNewPos + 1] = oldValue;
            }

            this.capacity = reqCap;
            this.posMask = newPosMask;
            this.state = newState;
            this.kvs = newKvs;
            this.calculateThreshold();
            this.op++;
        }
    }

    protected void removeAndShift(int currPos, int currRealPos, int currRealValPos) {
        this.size--;
        this.state[currPos] = BoxEnum.NEG_ONE;
        this.kvs[currRealPos] = null;
        this.kvs[currRealValPos] = null;
        for (int i = (currPos + 1) & this.posMask; i != currPos; i = ++i & this.posMask) {
            if (this.state[i] < 1) return;
            final int realPos = i << 1,
                    realValPos = realPos + 1,
                    prePos = (i - 1) & this.posMask,
                    preRealPos = prePos << 1;
            this.state[prePos] = --this.state[i];
            this.kvs[preRealPos] = this.kvs[realPos];
            this.kvs[preRealPos + 1] = this.kvs[realValPos];
            this.state[i] = BoxEnum.NEG_ONE;
            this.kvs[realPos] = null;
            this.kvs[realValPos] = null;
        }
        this.calculateThreshold();
        this.op++;
    }

    protected V inline_putVar(K key, V value, final boolean isAbsent) {
        this.checkAndResize(false, 1);

        int pos = this.hasher.hashing(key) & this.posMask, realPos = pos << 1;
        byte currPsl = 0, currState;
        Object addKey = key, addValue = value;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(addKey, this.kvs[realPos])) {
                final int realValPos = realPos + 1;
                final V oldValue = (V) this.kvs[realValPos];
                if (isAbsent && oldValue != null) return oldValue;
                this.kvs[realValPos] = value;
                return oldValue;
            }
            if (currPsl > currState) {
                final byte tmpState = this.state[pos];
                final int valPos = realPos + 1;
                final Object tmpKey = this.kvs[realPos], tmpValue = this.kvs[valPos];

                this.state[pos] = currPsl;
                currPsl = tmpState;
                this.kvs[realPos] = addKey;
                addKey = tmpKey;
                this.kvs[valPos] = addValue;
                addValue = tmpValue;
            }
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl < -1 || pos == startPos) {
                this.checkAndResize(true, 1);
                this.inline_putVar((K) addKey, (V) addValue, false);
                return null;
            }
        }

        this.size++;
        this.state[pos] = currPsl;
        this.kvs[realPos] = addKey;
        this.kvs[realPos + 1] = addValue;
        this.calculateThreshold();
        this.op++;
        return null;
    }

    @Nullable
    public V put(K key, V value) {
        return this.inline_putVar(key, value, false);
    }

    @Nullable
    public V putIfAbsent(K key, V value) {
        return this.inline_putVar(key, value, true);
    }

    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        if (m.isEmpty()) return;
        this.threshold = (float) (m.size() + this.size) / (float) this.capacity;
        m.forEach(this::put);
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        if (this.size < 1) return;
        for (int i = 0; i < this.capacity; i++) {
            if (this.state[i] > -1) {
                final byte currOp = this.op;
                final int currPos = i << 1, currValPos = currPos + 1;
                this.kvs[currValPos] = function.apply((K) this.kvs[currPos], (V) this.kvs[currValPos]);
                if (this.op != currOp) throw new ConcurrentModificationException();
            }
        }
    }

    protected V getVal(Object key, final ObjExc<V> matched) {
        if (this.isEmpty()) return null;

        int pos = this.hasher.hashing(key) & this.posMask, realPos = pos << 1;
        byte currPsl = 0, currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(key, this.kvs[realPos])) return matched.run(pos, realPos, realPos + 1);
            if (currState < currPsl) return null;
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl < -1 || pos == startPos) return null;
        }
        return null;
    }

    protected boolean getValBool(Object key, final BoolExc matched) {
        if (this.isEmpty()) return false;

        int pos = this.hasher.hashing(key) & this.posMask, realPos = pos << 1;
        byte currPsl = 0, currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(key, this.kvs[realPos])) return matched.run(pos, realPos, realPos + 1);
            if (currState < currPsl) return false;
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl < -1 || pos == startPos) return false;
        }
        return false;
    }

    protected V inline_getVal(Object key, V defaultValue) {
        if (this.isEmpty()) return defaultValue;

        int pos = this.hasher.hashing(key) & this.posMask, realPos = pos << 1;
        byte currPsl = 0, currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(key, this.kvs[realPos])) return (V) this.kvs[realPos + 1];
            if (currState < currPsl) return defaultValue;
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl < -1 || pos == startPos) return defaultValue;
        }
        return defaultValue;
    }

    public V get(Object key) {
        return this.inline_getVal(key, null);
    }

    public V getOrDefault(Object key, V defaultValue) {
        return this.inline_getVal(key, defaultValue);
    }

    @Nullable
    public V replace(K key, V value) {
        return this.getVal(key, (l_si, l_ki, l_vi) -> {
            final Object l_oldValue = this.kvs[l_vi];
            this.kvs[l_vi] = value;
            return (V) l_oldValue;
        });
    }

    public boolean replace(K key, V oldValue, V newValue) {
        return this.getValBool(key, (l_si, l_ki, l_vi) -> {
            final Object l_oldValue = this.kvs[l_vi];
            if (Objects.equals(l_oldValue, oldValue)) {
                this.kvs[l_vi] = newValue;
                return true;
            } else return false;
        });
    }

    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        this.checkAndResize(false, 1);

        int pos = this.hasher.hashing(key) & this.posMask, realPos = pos << 1;
        byte currPsl = 0, currState;
        Object addKey = key, addValue = null;
        V returnVal = null;
        Function<? super K, ? extends V> valSupplier = mappingFunction;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(addKey, this.kvs[realPos])) {
                final int realValPos = realPos + 1;
                final Object oldValue = this.kvs[realValPos];
                if (oldValue != null) return (V) oldValue;

                final V newValue = mappingFunction.apply(key);
                if (newValue != null) this.kvs[realValPos] = newValue;
                return newValue;
            }
            if (currPsl > currState) {
                if (valSupplier != null) {
                    addValue = returnVal = mappingFunction.apply(key);
                    valSupplier = null;
                    if (returnVal == null) return null;
                }

                final byte tmpState = this.state[pos];
                final int valPos = realPos + 1;
                final Object tmpKey = this.kvs[realPos], tmpValue = this.kvs[valPos];

                this.state[pos] = currPsl;
                currPsl = tmpState;
                this.kvs[realPos] = addKey;
                addKey = tmpKey;
                this.kvs[valPos] = addValue;
                addValue = tmpValue;
            }
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl < -1 || pos == startPos) {
                this.checkAndResize(true, 1);
                this.inline_putVar((K) addKey, (V) addValue, false);
                return returnVal;
            }
        }

        if (valSupplier != null) {
            addValue = returnVal = mappingFunction.apply(key);
            if (returnVal == null) return null;
        }

        this.size++;
        this.state[pos] = currPsl;
        this.kvs[realPos] = addKey;
        this.kvs[realPos + 1] = addValue;
        this.calculateThreshold();
        this.op++;
        return returnVal;
    }

    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.getVal(key, (l_si, l_ki, l_vi) -> {
            final Object l_oldValue = this.kvs[l_vi];
            if (l_oldValue != null) {
                final V l_newValue = remappingFunction.apply(key, (V) l_oldValue);
                if (l_newValue != null) {
                    this.kvs[l_vi] = l_newValue;
                    return l_newValue;
                }
            }
            this.removeAndShift(l_si, l_ki, l_vi);
            return null;
        });
    }

    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        this.checkAndResize(false, 1);

        int pos = this.hasher.hashing(key) & this.posMask, realPos = pos << 1;
        byte currPsl = 0, currState;
        Object addKey = key, addValue = null;
        V returnVal = null;
        BiFunction<? super K, ? super V, ? extends V> valSupplier = remappingFunction;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(addKey, this.kvs[realPos])) {
                final int realValPos = realPos + 1;
                final Object oldValue = this.kvs[realValPos];

                returnVal = remappingFunction.apply(key, (V) oldValue);
                if (returnVal == null) this.removeAndShift(pos, realPos, realValPos);
                else this.kvs[realValPos] = returnVal;
                return returnVal;
            }
            if (currPsl > currState) {
                if (valSupplier != null) {
                    addValue = returnVal = remappingFunction.apply(key, null);
                    valSupplier = null;
                    if (returnVal == null) return null;
                }

                final byte tmpState = this.state[pos];
                final int valPos = realPos + 1;
                final Object tmpKey = this.kvs[realPos], tmpValue = this.kvs[valPos];

                this.state[pos] = currPsl;
                currPsl = tmpState;
                this.kvs[realPos] = addKey;
                addKey = tmpKey;
                this.kvs[valPos] = addValue;
                addValue = tmpValue;
            }
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl < -1 || pos == startPos) {
                this.checkAndResize(true, 1);
                this.inline_putVar((K) addKey, (V) addValue, false);
                return returnVal;
            }
        }

        if (valSupplier != null) {
            addValue = returnVal = remappingFunction.apply(key, null);
            if (returnVal == null) return null;
        }

        this.size++;
        this.state[pos] = currPsl;
        this.kvs[realPos] = addKey;
        this.kvs[realPos + 1] = addValue;
        this.calculateThreshold();
        this.op++;
        return returnVal;
    }

    public V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        this.checkAndResize(false, 1);

        int pos = this.hasher.hashing(key) & this.posMask, realPos = pos << 1;
        byte currPsl = 0, currState;
        Object addKey = key, addValue = value;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(addKey, this.kvs[realPos])) {
                final int realValPos = realPos + 1;
                final Object oldValue = this.kvs[realValPos];
                if (oldValue != null) addValue = remappingFunction.apply((V) oldValue, (V) addValue);
                if (addValue == null) this.removeAndShift(pos, realPos, realValPos);
                else this.kvs[realValPos] = addValue;
                return (V) addValue;
            }
            if (currPsl > currState) {
                final byte tmpState = this.state[pos];
                final int valPos = realPos + 1;
                final Object tmpKey = this.kvs[realPos], tmpValue = this.kvs[valPos];

                this.state[pos] = currPsl;
                currPsl = tmpState;
                this.kvs[realPos] = addKey;
                addKey = tmpKey;
                this.kvs[valPos] = addValue;
                addValue = tmpValue;
            }
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl < -1 || pos == startPos) {
                this.checkAndResize(true, 1);
                this.inline_putVar((K) addKey, (V) addValue, false);
                return value;
            }
        }

        this.size++;
        this.state[pos] = currPsl;
        this.kvs[realPos] = addKey;
        this.kvs[realPos + 1] = addValue;
        this.calculateThreshold();
        this.op++;
        return value;
    }

    public boolean containsKey(Object key) {
        return this.getValBool(key, (l_si, l_ki, l_vi) -> true);
    }

    public boolean containsValue(Object value) {
        if (this.size < 1) return false;
        if (value == null) return true;
        for (int i = 0; i < this.capacity; i++) {
            if (this.state[i] > -1 && value.equals(this.kvs[(i << 1) + 1])) return true;
        }
        return false;
    }

    public V remove(Object key) {
        return this.getVal(key, (l_si, l_ki, l_vi) -> {
            final Object currValue = this.kvs[l_vi];
            this.removeAndShift(l_si, l_ki, l_vi);
            return (V) currValue;
        });
    }

    public boolean remove(Object key, Object value) {
        return this.getValBool(key, (l_si, l_ki, l_vi) -> {
            final Object currValue = this.kvs[l_vi];
            if (Objects.equals(value, currValue)) {
                this.removeAndShift(l_si, l_ki, l_vi);
                return true;
            } else return false;
        });
    }

    public void clear() {
        this.size = 0;
        this.threshold = 0.0f;
        Arrays.fill(this.state, BoxEnum.NEG_ONE);
        Arrays.fill(this.kvs, null);
        this.op++;
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        if (this.size < 1) return;
        for (int i = 0; i < this.capacity; i++) {
            final byte currOp = this.op;
            final int currPos = i << 1;
            if (this.state[i] > -1) action.accept((K) this.kvs[currPos], (V) this.kvs[currPos + 1]);
            if (this.op != currOp) throw new ConcurrentModificationException();
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    @NotNull
    public Set<K> keySet() {
        Set<K> result = this.keyViewer;
        if (result == null) {
            result = new KeySet();
            this.keyViewer = result;
        }
        return result;
    }

    @NotNull
    public Collection<V> values() {
        Collection<V> result = this.valueViewer;
        if (result == null) {
            result = new Values();
            this.valueViewer = result;
        }
        return result;
    }

    @NotNull
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> result = this.entryViewer;
        if (result == null) {
            result = new EntrySet();
            this.entryViewer = result;
        }
        return result;
    }

    protected Object clone() {
        Obj2ObjRHMap<K, V> result;
        try {
            result = (Obj2ObjRHMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcStateLen = this.state.length, srcKVsLen = this.kvs.length;
        result.state = new byte[srcStateLen];
        result.kvs = new Object[srcKVsLen];
        System.arraycopy(this.state, 0, result.state, 0, srcStateLen);
        System.arraycopy(this.kvs, 0, result.kvs, 0, srcKVsLen);
        this.keyViewer = null;
        this.valueViewer = null;
        this.entryViewer = null;
        return result;
    }

    protected class Slot implements Entry<K, V> {
        protected int hash;
        protected int realPos;
        protected K key;
        protected V value;

        protected Slot(int realPos, K key, V value) {
            this.realPos = realPos;
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return this.key;
        }

        public final V getValue() {
            return this.value;
        }

        public final String toString() {
            return this.key + "=" + this.value;
        }

        public final int hashCode() {
            if (this.hash != 0) return this.hash;
            return this.hash = Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
        }

        public final V setValue(V newValue) {
            final V oldValue = this.value;
            Obj2ObjRHMap.this.kvs[this.realPos] = newValue;
            this.value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this) return true;
            return o instanceof Map.Entry<?, ?> e && Objects.equals(this.key, e.getKey()) && Objects.equals(this.value, e.getValue());
        }

        protected void reset(int realPos, K key, V value) {
            this.hash = 0;
            this.realPos = realPos;
            this.key = key;
            this.value = value;
        }
    }

    protected abstract class RHIterator<T> implements Iterator<T> {
        protected byte currOp;
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;
        protected int remaining;

        protected RHIterator() {
            this.currOp = Obj2ObjRHMap.this.op;
            this.remaining = Obj2ObjRHMap.this.capacity;
            if (Obj2ObjRHMap.this.size > 0) {
                for (int i = 0; i < this.remaining; i++) {
                    if (Obj2ObjRHMap.this.state[i] > -1) {
                        if (this.pos < 0) this.pos = i;
                        else if (this.posNext < 0) this.posNext = i;
                        else break;
                    }
                }
            }
        }

        public boolean hasNext() {
            return this.pos > -1 && this.pos < this.remaining;
        }

        protected int nextNode() {
            if (Obj2ObjRHMap.this.op != this.currOp) throw new ConcurrentModificationException();
            if (this.pos < 0) throw new NoSuchElementException();

            this.posPre = this.pos;
            this.pos = this.posNext;
            this.posNext = -1;

            if (this.pos > -1) {
                for (int i = this.pos + 1; i < Obj2ObjRHMap.this.capacity; i++) {
                    if (Obj2ObjRHMap.this.state[i] > -1) {
                        this.posNext = i;
                        break;
                    }
                }
            }
            return this.posPre << 1;
        }

        public void remove() {
            final int currRealPos = this.posPre << 1;
            if (this.posPre < 0 || Obj2ObjRHMap.this.state[currRealPos] < 0) throw new IllegalStateException();
            if (Obj2ObjRHMap.this.op != this.currOp) throw new ConcurrentModificationException();
            final int currPos = this.posPre;
            Obj2ObjRHMap.this.size--;
            Obj2ObjRHMap.this.state[this.posPre] = BoxEnum.NEG_ONE;
            Obj2ObjRHMap.this.kvs[currRealPos] = null;
            Obj2ObjRHMap.this.kvs[currRealPos + 1] = null;

            boolean refreshNextItPos = true;
            for (int i = this.posPre + 1 & Obj2ObjRHMap.this.posMask; i != currPos; i = ++i & Obj2ObjRHMap.this.posMask) {
                final int realPos = i << 1;
                if (Obj2ObjRHMap.this.state[i] < 1) break;

                final int realValPos = realPos + 1, prePos = (i - 1) & Obj2ObjRHMap.this.posMask, preRealPos = prePos << 1;
                Obj2ObjRHMap.this.state[prePos] = --Obj2ObjRHMap.this.state[i];
                Obj2ObjRHMap.this.kvs[preRealPos] = Obj2ObjRHMap.this.kvs[realPos];
                Obj2ObjRHMap.this.kvs[preRealPos + 1] = Obj2ObjRHMap.this.kvs[realValPos];
                Obj2ObjRHMap.this.state[i] = BoxEnum.NEG_ONE;
                Obj2ObjRHMap.this.kvs[realPos] = null;
                Obj2ObjRHMap.this.kvs[realValPos] = null;
                this.remaining--;
                if (refreshNextItPos && preRealPos == currRealPos) {
                    refreshNextItPos = false;
                    this.pos = this.posPre;
                    if (this.posNext > 0) this.posNext--;
                }
            }
            Obj2ObjRHMap.this.calculateThreshold();
            this.currOp = ++Obj2ObjRHMap.this.op;
        }
    }

    protected class KeyIterator extends RHIterator<K> {
        public K next() {
            return (K) Obj2ObjRHMap.this.kvs[this.nextNode()];
        }
    }

    protected class ValueIterator extends RHIterator<V> {
        public V next() {
            return (V) Obj2ObjRHMap.this.kvs[this.nextNode() + 1];
        }
    }

    protected class EntryIterator extends RHIterator<Map.Entry<K,V>> {
        public Map.Entry<K,V> next() {
            final int realPos = this.nextNode();
            return new Slot(realPos, (K) Obj2ObjRHMap.this.kvs[realPos], (V) Obj2ObjRHMap.this.kvs[realPos + 1]);
        }
    }

    protected <T> T[] prepareArray(T[] a) {
        final int size = this.size;
        if (a.length < size) return (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        if (a.length > size) a[size] = null;
        return a;
    }

    protected <T> T[] slotToArray(T[] a, byte offset) {
        if (this.size < 1) return a;
        int idx = 0;
        for (int i = 0; i < this.capacity; i++) {
            if (this.state[i] > -1) {
                a[idx] = (T) this.kvs[(i << 1) + offset];
                idx++;
            }
        }
        return a;
    }

    protected abstract class RHSpliterator<T> implements Spliterator<T> {
        protected byte currOp;
        protected int index;
        protected int limitCap;
        protected int estSize;

        protected RHSpliterator(byte op, int index, int limitCap, int estSize) {
            this.currOp = op;
            this.index = index;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = Obj2ObjRHMap.this.size;
                currOp = Obj2ObjRHMap.this.op;
                newLimit = this.limitCap = Obj2ObjRHMap.this.capacity;
            }
            return newLimit;
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == Obj2ObjRHMap.this.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
        }
    }
    
    protected class KeySpliterator extends RHSpliterator<K> {
        protected KeySpliterator(byte op, int index, int limitCap, int estSize) {
            super(op, index, limitCap, estSize);
        }

        public Spliterator<K> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return (newIndex >= midIndex) ? null : new KeySpliterator(this.currOp, newIndex, this.index = midIndex, this.estSize >>>= 1);
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return false;
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (Obj2ObjRHMap.this.state[currPos] > -1) {
                    action.accept((K) Obj2ObjRHMap.this.kvs[currPos << 1]);
                    if (Obj2ObjRHMap.this.op != this.currOp) throw new ConcurrentModificationException();
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super K> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return;
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (Obj2ObjRHMap.this.state[currPos] > -1) action.accept((K) Obj2ObjRHMap.this.kvs[currPos << 1]);
                if (Obj2ObjRHMap.this.op != this.currOp) throw new ConcurrentModificationException();
            }
        }
    }

    protected class ValueSpliterator extends RHSpliterator<V> {
        protected ValueSpliterator(byte op, int index, int limitCap, int estSize) {
            super(op, index, limitCap, estSize);
        }

        public Spliterator<V> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return (newIndex >= midIndex) ? null : new ValueSpliterator(this.currOp, newIndex, this.index = midIndex, this.estSize >>>= 1);
        }

        public boolean tryAdvance(Consumer<? super V> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return false;
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (Obj2ObjRHMap.this.state[currPos] > -1) {
                    action.accept((V) Obj2ObjRHMap.this.kvs[(currPos << 1) + 1]);
                    if (Obj2ObjRHMap.this.op != currOp) throw new ConcurrentModificationException();
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super V> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return;
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (Obj2ObjRHMap.this.state[currPos] > -1) action.accept((V) Obj2ObjRHMap.this.kvs[(currPos << 1) + 1]);
                if (Obj2ObjRHMap.this.op != this.currOp) throw new ConcurrentModificationException();
            }
        }

        public int characteristics() {
            return this.limitCap < 0 || this.estSize == Obj2ObjRHMap.this.size ? Spliterator.SIZED : 0;
        }
    }

    protected class EntrySpliterator extends RHSpliterator<Map.Entry<K,V>> {
        protected EntrySpliterator(byte op, int index, int limitCap, int estSize) {
            super(op, index, limitCap, estSize);
        }

        public Spliterator<Map.Entry<K,V>> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return (newIndex >= midIndex) ? null : new EntrySpliterator(this.currOp, newIndex, this.index = midIndex, this.estSize >>>= 1);
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K,V>> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return false;
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (Obj2ObjRHMap.this.state[currPos] > -1) {
                    final int realPos = currPos << 1;
                    action.accept(new Slot(realPos, (K) Obj2ObjRHMap.this.kvs[realPos], (V) Obj2ObjRHMap.this.kvs[realPos + 1]));
                    if (Obj2ObjRHMap.this.op != currOp) throw new ConcurrentModificationException();
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K,V>> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return;
            final Slot tmpStruct = new Slot(0, null, null);
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (Obj2ObjRHMap.this.state[currPos] > -1) {
                    final int realPos = currPos << 1;
                    tmpStruct.reset(realPos, (K) Obj2ObjRHMap.this.kvs[realPos], (V) Obj2ObjRHMap.this.kvs[realPos + 1]);
                    action.accept(tmpStruct);
                }
                if (Obj2ObjRHMap.this.op != this.currOp) throw new ConcurrentModificationException();
            }
        }
    }

    protected class KeySet extends AbstractSet<K> {
        public int size() {
            return Obj2ObjRHMap.this.size();
        }

        public void clear() {
            Obj2ObjRHMap.this.clear();
        }

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        public boolean contains(Object o) {
            return Obj2ObjRHMap.this.containsKey(o);
        }

        public boolean remove(Object key) {
            return Obj2ObjRHMap.this.getValBool(key, (l_si, l_ki, l_vi) -> {
                Obj2ObjRHMap.this.removeAndShift(l_si, l_ki, l_vi);
                return true;
            });
        }
        public Spliterator<K> spliterator() {
            return new KeySpliterator(BoxEnum.ZERO, 0, -1, 0);
        }

        public Object[] toArray() {
            return slotToArray(new Object[Obj2ObjRHMap.this.size], (byte) 0);
        }

        public <T> T[] toArray(T[] a) {
            return slotToArray(prepareArray(a), (byte) 0);
        }

        public void forEach(Consumer<? super K> action) {
            Obj2ObjRHMap.this.forEach((l_k, l_v) -> action.accept(l_k));
        }
    }

    protected class Values extends AbstractCollection<V> {
        public int size() {
            return Obj2ObjRHMap.this.size();
        }

        public void clear() {
            Obj2ObjRHMap.this.clear();
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        public boolean contains(Object o) {
            return Obj2ObjRHMap.this.containsValue(o);
        }

        public Spliterator<V> spliterator() {
            return new ValueSpliterator(BoxEnum.ZERO, 0, -1, 0);
        }

        public Object[] toArray() {
            return slotToArray(new Object[Obj2ObjRHMap.this.size], (byte) 1);
        }

        public <T> T[] toArray(T[] a) {
            return slotToArray(prepareArray(a), (byte) 1);
        }

        public void forEach(Consumer<? super V> action) {
            Obj2ObjRHMap.this.forEach((l_k, l_v) -> action.accept(l_v));
        }
    }

    protected class EntrySet extends AbstractSet<Entry<K, V>> {
        public int size() {
            return Obj2ObjRHMap.this.size();
        }

        public void clear() {
            Obj2ObjRHMap.this.clear();
        }

        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object o) {
            if (o instanceof Entry<?, ?> e) return Objects.equals(e.getValue(), Obj2ObjRHMap.this.get(e.getKey()));;
            return false;
        }

        public boolean remove(Object o) {
            if (o instanceof Entry<?, ?> e) return Obj2ObjRHMap.this.remove(e.getKey(), e.getValue());;
            return false;
        }
        public Spliterator<Entry<K, V>> spliterator() {
            return new EntrySpliterator(BoxEnum.ZERO, 0, -1, 0);
        }

        public void forEach(Consumer<? super Entry<K,V>> action) {
            Objects.requireNonNull(action);
            if (Obj2ObjRHMap.this.size < 1) return;
            final Slot tmpStruct = new Slot(0, null, null);
            for (int i = 0; i < Obj2ObjRHMap.this.capacity; i++) {
                final byte currOp = Obj2ObjRHMap.this.op;
                final int currPos = i << 1;
                if (Obj2ObjRHMap.this.state[i] > -1) {
                    tmpStruct.reset(currPos, (K) Obj2ObjRHMap.this.kvs[currPos], (V) Obj2ObjRHMap.this.kvs[currPos + 1]);
                    action.accept(tmpStruct);
                }
                if (Obj2ObjRHMap.this.op != currOp) throw new ConcurrentModificationException();
            }
        }
    }
}
