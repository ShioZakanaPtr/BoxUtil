package org.boxutil.util.container;

import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Unbounded unordered hash map, based Robin Hood Hash.
 */
public class UnorderedMap<K, V> implements Map<K, V>, Cloneable {
    protected static final int DEFAULT_INITIAL_CAPACITY = 1 << 7;
    protected static final int MAXIMUM_CAPACITY = 1 << 29;

    protected byte op = 0;
    protected int capacity;
    protected int posMask;
    protected int size = 0;
    protected float threshold = 0.0f;
    protected final float loadFactor;
    protected int[] state; // -1 => empty, (x > -1) => psl
    protected Object[] kvs; // i = {iKey, iValue}

    protected Set<K> tmpKeySet;
    protected Collection<V> tmpValueCol;
    protected Set<Entry<K, V>> tmpEntrySet;

    /**
     * @param loadFactor typical <b>0.7f</b> ~ <b>0.8f</b>
     */
    public UnorderedMap(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = CalculateUtil.getPOTMax(Math.max(Math.min(initialCapacity, MAXIMUM_CAPACITY), DEFAULT_INITIAL_CAPACITY));
        final int realCap = this.capacity << 1;
        this.posMask = this.capacity - 1;
        this.state = new int[this.capacity];
        this.kvs = new Object[realCap];
    }

    public UnorderedMap(int initialCapacity) {
        this(initialCapacity, 0.7f);
    }

    public UnorderedMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public int capacity() {
        return this.capacity;
    }

    public float getLoadFactor() {
        return this.loadFactor;
    }

    protected void calculateThreshold() {
        this.threshold = (float) this.size / (float) this.capacity;
    }

    protected void checkResize() {
        if (this.threshold >= this.loadFactor) {
            if ((this.capacity << 1) >= MAXIMUM_CAPACITY) return;

            final int reqCap = CalculateUtil.getPOTMax(this.threshold >= 1.0f ? Math.round(this.capacity * this.threshold) : this.size);
            if (reqCap >= MAXIMUM_CAPACITY || reqCap < 1) return;

            final int newCap = reqCap << 1, newPosMask = newCap - 1;
            final int[] newState = new int[reqCap];
            final Object[] newKvs = new Object[newCap];
            Arrays.fill(newState, -1);

            for (int i = 0; i < reqCap; i++) {
                final int oldState = this.state[i];
                if (oldState < 0) continue;

                final int realOldPos = i << 1;
                Object oldKey = this.kvs[realOldPos], oldValue = this.kvs[realOldPos + 1];

                int newPos = oldKey.hashCode() & newPosMask, realNewPos = newPos << 1,
                        currPsl = 0,
                        currState;

                final int startPos = newPos;
                boolean failed = false;
                while ((currState = newState[newPos]) > -1) {
                    if (currPsl > currState) {
                        final int tmpState = newState[newPos], realNewValPos = realNewPos + 1;
                        final Object tmpKey = newKvs[realNewPos], tmpValue = newKvs[realNewValPos];

                        newState[newPos] = currPsl;
                        currPsl = tmpState;
                        newKvs[realNewPos] = oldKey;
                        oldKey = tmpKey;
                        newKvs[realNewValPos] = oldValue;
                        oldValue = tmpValue;
                    }
                    newPos++;
                    newPos &= newPosMask;
                    realNewPos = newPos << 1;
                    currPsl++;
                    if (currPsl < -1 || newPos == startPos) {
                        failed = true;
                        break;
                    }
                }
                if (failed) continue;

                newState[newPos] = currPsl;
                newKvs[realNewPos] = oldKey;
                newKvs[realNewPos + 1] = oldValue;
            }

            this.state = newState;
            this.kvs = newKvs;
            this.calculateThreshold();
        }
    }

    protected V putVar(K key, V value, boolean ifAbsentMode) {
        if (key == null) return null;
        this.checkResize();
        if (this.threshold >= 1.0f) return null;

        int pos = key.hashCode() & this.posMask, realPos = pos << 1,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (key.equals(this.kvs[realPos])) {
                final V oldValue = (V) this.kvs[realPos + 1];
                if (ifAbsentMode) return oldValue;
                this.op++;
                this.kvs[realPos + 1] = value;
                return oldValue;
            }
            if (currPsl > currState) {
                this.op++;
                final int tmpState = this.state[pos], valPos = realPos + 1;
                final Object tmpKey = this.kvs[realPos], tmpValue = this.kvs[valPos];

                this.state[pos] = currPsl;
                currPsl = tmpState;
                this.kvs[realPos] = key;
                key = (K) tmpKey;
                this.kvs[valPos] = value;
                value = (V) tmpValue;
            }
            pos++;
            pos &= this.posMask;
            realPos = pos << 1;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return null;
        }

        this.op++;
        this.state[pos] = currPsl;
        this.kvs[realPos] = key;
        this.kvs[realPos + 1] = value;
        this.size++;
        this.calculateThreshold();
        return null;
    }

    @Nullable
    public V put(K key, V value) {
        return this.putVar(key, value, false);
    }

    @Nullable
    public V putIfAbsent(K key, V value) {
        return this.putVar(key, value, true);
    }

    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
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

    protected interface ObjExc<V> {
        V run(int statePos, int keyPos, int valuePos);
    }

    protected interface BoolExc<V> {
        boolean run(int statePos, int keyPos, int valuePos);
    }

    protected V getVal(Object key, V defaultValue, final ObjExc<V> exc) {
        if (key == null || this.isEmpty()) return defaultValue;

        int pos = key.hashCode() & this.posMask, realPos = pos << 1,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (key.equals(this.kvs[realPos])) return exc.run(pos, realPos, realPos + 1);
            if (currState < currPsl) return defaultValue;
            pos++;
            pos &= this.posMask;
            realPos = pos << 1;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return defaultValue;
        }
        return defaultValue;
    }

    protected boolean getVal(Object key, final BoolExc<V> exc) {
        if (key == null || this.isEmpty()) return false;

        int pos = key.hashCode() & this.posMask, realPos = pos << 1,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (key.equals(this.kvs[realPos])) return exc.run(pos, realPos, realPos + 1);
            if (currState < currPsl) return false;
            pos++;
            pos &= this.posMask;
            realPos = pos << 1;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return false;
        }
        return false;
    }

    public V get(Object key) {
        return this.getOrDefault(key, null);
    }

    public V getOrDefault(Object key, V defaultValue) {
        return this.getVal(key, defaultValue, (l_si, l_ki, l_vi) -> (V) this.kvs[l_vi]);
    }

    @Nullable
    public V replace(K key, V value) {
        return this.getVal(key, null, (l_si, l_ki, l_vi) -> {
            final Object l_oldValue = this.kvs[l_vi];
            this.kvs[l_vi] = value;
            return (V) l_oldValue;
        });
    }

    public boolean replace(K key, V oldValue, V newValue) {
        return this.getVal(key, (l_si, l_ki, l_vi) -> {
            final Object l_oldValue = this.kvs[l_vi];
            if (Objects.equals(l_oldValue, oldValue)) {
                this.kvs[l_vi] = newValue;
                return true;
            } else return false;
        });
    }

    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        if (key == null) return null;
        Objects.requireNonNull(mappingFunction);
        return Map.super.computeIfAbsent(key, mappingFunction);
    }

    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null) return null;
        Objects.requireNonNull(remappingFunction);
        return Map.super.computeIfPresent(key, remappingFunction);
    }

    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null) return null;
        Objects.requireNonNull(remappingFunction);
        return Map.super.compute(key, remappingFunction);
    }

    public V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (key == null) return null;
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        return Map.super.merge(key, value, remappingFunction);
    }

    public boolean containsKey(Object key) {
        return this.getVal(key, (l_si, l_ki, l_vi) -> true);
    }

    public boolean containsValue(Object value) {
        if (value == null) return true;
        if (this.size < 1) return false;
        for (int i = 0; i < this.capacity; i++) {
            if (this.state[i] > -1 && value.equals(this.kvs[(i << 1) + 1])) return true;
        }
        return false;
    }

    public V remove(Object key) {
        return this.getVal(key, null, (l_si, l_ki, l_vi) -> {
            this.op++;
            this.state[l_si] = -1;
            this.size--;
            final Object currValue = this.kvs[l_vi];
            this.kvs[l_ki] = null;
            this.kvs[l_vi] = null;
            return (V) currValue;
        });
    }

    public boolean remove(Object key, Object value) {
        return this.getVal(key, (l_si, l_ki, l_vi) -> {
            final Object currValue = this.kvs[l_vi];
            if (Objects.equals(value, currValue)) {
                this.op++;
                this.state[l_si] = -1;
                this.size--;
                this.kvs[l_ki] = null;
                this.kvs[l_vi] = null;
                return true;
            } else return false;
        });
    }

    public void clear() {
        this.op++;
        Arrays.fill(this.state, (byte) -1);
        Arrays.fill(this.kvs, null);
        this.size = 0;
        this.threshold = 0.0f;
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
        return this.size() < 1;
    }

    @NotNull
    public Set<K> keySet() {
        return this.tmpKeySet;
    }

    @NotNull
    public Collection<V> values() {
        return this.tmpValueCol;
    }

    @NotNull
    public Set<Entry<K, V>> entrySet() {
        return this.tmpEntrySet;
    }

    protected Object clone() {
        UnorderedMap<K, V> result;
        try {
            result = (UnorderedMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcStateLen = this.state.length, srcKVsLen = this.kvs.length;
        result.state = new int[srcStateLen];
        result.kvs = new Object[srcKVsLen];
        System.arraycopy(this.state, 0, result.state, 0, srcStateLen);
        System.arraycopy(this.kvs, 0, result.kvs, 0, srcKVsLen);
        this.tmpKeySet = null;
        this.tmpValueCol = null;
        this.tmpEntrySet = null;
        return result;
    }
}
