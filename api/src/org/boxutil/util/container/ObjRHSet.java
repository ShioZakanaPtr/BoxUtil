package org.boxutil.util.container;

import org.boxutil.define.BoxEnum;
import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

/**
 * The map is not thread safe.<p>
 * Unbounded unordered hash set, based Robin Hood hashing.<p>
 * Element can be <code>null</code>.<p>
 * The default hasher using {@link ContainerHasher#hashing_HashMap(Object)}.<p>
 * This set implementation is suitable for scenarios with many reads and few writes,
 * or you can ensure that resizing is not triggered too frequently,
 * also and memory usage is not a primary concern.<p>
 * Otherwise, it is still recommended to use {@link HashSet} or another more appropriate implementation.
 */
@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused"})
public class ObjRHSet<E> extends AbstractSet<E> implements Set<E>, Cloneable {
    public static final int DEFAULT_INITIAL_CAPACITY = 1 << 7;
    public static final int MAXIMUM_CAPACITY = 1 << 30;

    protected byte op = 0;
    protected int capacity;
    protected int posMask;
    protected int size = 0;
    protected float threshold = 0.0f;
    protected final float loadFactor;
    protected final ContainerHasher hasher;
    protected byte[] state; // -1 => empty, (x > -1) => psl
    protected Object[] elements;

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, clamp to range <b>[0.2f, 0.9f]</b>.
     * @param hasher when an element is <code>null</code>, it hash must be <code>0</code>
     */
    public ObjRHSet(int initialCapacity, float loadFactor, final ContainerHasher hasher) {
        if (initialCapacity > MAXIMUM_CAPACITY || initialCapacity < 1) throw new IllegalArgumentException("The capacity of map overflow");
        this.loadFactor = Math.max(Math.min(loadFactor, 0.91f), 0.2f);
        this.capacity = CalculateUtil.getPOTMax(Math.max(initialCapacity, 2));
        this.posMask = this.capacity - 1;
        this.hasher = Objects.requireNonNull(hasher);
        this.state = new byte[this.capacity];
        this.elements = new Object[this.capacity];
        Arrays.fill(this.state, BoxEnum.NEG_ONE);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hasher when an element is <code>null</code>, it hash must be <code>0</code>
     */
    public ObjRHSet(int initialCapacity, final ContainerHasher hasher) {
        this(initialCapacity, 0.75f, hasher);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjRHSet(int initialCapacity) {
        this(initialCapacity, 0.75f, ContainerHasher::hashing_HashMap);
    }

    public ObjRHSet() {
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

    protected void calculateThreshold() {
        this.threshold = (float) this.size / (float) this.capacity;
    }

    protected int getReqCap(int extraExpand) {
        final int reqCap = this.capacity << extraExpand;
        if (reqCap < 1 || reqCap > MAXIMUM_CAPACITY) throw new IllegalStateException("The capacity of map overflow; split your KVs to different map or check your hasher and using better one");
        return reqCap;
    }

    protected void checkAndResize(boolean forced, int extraExpand) {
        if (this.threshold >= this.loadFactor || forced) {
            final int reqCap = getReqCap(extraExpand);

            final int newPosMask = reqCap - 1;
            final byte[] newState = new byte[reqCap];
            final Object[] newElements = new Object[reqCap];
            Arrays.fill(newState, BoxEnum.NEG_ONE);

            for (int i = 0; i < this.capacity; i++) {
                if (this.state[i] < 0) continue;

                Object oldEle = this.elements[i];

                int newPos = this.hasher.hashing(oldEle) & newPosMask;
                byte currPsl = 0, currState;

                final int startPos = newPos;
                while ((currState = newState[newPos]) > -1) {
                    if (currPsl > currState) {
                        final byte tmpState = newState[newPos];
                        final Object tmpEle = newElements[newPos];

                        newState[newPos] = currPsl;
                        currPsl = tmpState;
                        newElements[newPos] = oldEle;
                        oldEle = tmpEle;
                    }
                    newPos = ++newPos & newPosMask;
                    if (++currPsl < -1 || newPos == startPos) { // you should goto use better hasher
                        this.threshold = Math.max(this.threshold, 2.0f) * 2.0f;
                        this.checkAndResize(true, extraExpand + 1);
                        return;
                    }
                }

                newState[newPos] = currPsl;
                newElements[newPos] = oldEle;
            }

            this.capacity = reqCap;
            this.posMask = newPosMask;
            this.state = newState;
            this.elements = newElements;
            this.calculateThreshold();
            this.op++;
        }
    }

    protected void removeAndShift(int currPos) {
        this.size--;
        this.state[currPos] = -1;
        this.elements[currPos] = null;
        for (int i = (currPos + 1) & this.posMask; i != currPos; i = ++i & this.posMask) {
            if (this.state[i] < 1) return;
            final int prePos = (i - 1) & this.posMask;
            this.state[prePos] = --this.state[i];
            this.elements[prePos] = this.elements[i];
            this.state[i] = -1;
            this.elements[i] = null;
        }
        this.calculateThreshold();
        this.op++;
    }

    public boolean add(E e) {
        this.checkAndResize(false, 1);

        int pos = this.hasher.hashing(e) & this.posMask;
        byte currPsl = 0, currState;
        Object addEle = e;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(addEle, this.elements[pos])) return false;
            if (currPsl > currState) {
                final byte tmpState = this.state[pos];
                final Object tmpEle = this.elements[pos];

                this.state[pos] = currPsl;
                currPsl = tmpState;
                this.elements[pos] = addEle;
                addEle = tmpEle;
            }
            pos = ++pos & this.posMask;
            if (++currPsl < -1 || pos == startPos) {
                this.checkAndResize(true, 1);
                return this.add((E) addEle);
            }
        }

        this.size++;
        this.state[pos] = currPsl;
        this.elements[pos] = addEle;
        this.calculateThreshold();
        this.op++;
        return true;
    }

    public boolean addAll(@NotNull Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        this.threshold = (float) (c.size() + this.size) / (float) this.capacity;
        return super.addAll(c);
    }

    protected boolean getVal(Object element, final IntPredicate matched) {
        if (this.isEmpty()) return false;

        int pos = this.hasher.hashing(element) & this.posMask;
        byte currPsl = 0, currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (Objects.equals(element, this.elements[pos])) return matched.test(pos);
            if (currState < currPsl) return false;
            pos = ++pos & this.posMask;
            if (++currPsl < -1 || pos == startPos) return false;
        }
        return false;
    }

    public boolean contains(Object o) {
        return this.getVal(o, l_pos -> true);
    }

    public boolean remove(Object o) {
        return this.getVal(o, l_pos -> {
            this.removeAndShift(l_pos);
            return true;
        });
    }

    public void clear() {
        this.size = 0;
        this.threshold = 0.0f;
        Arrays.fill(this.state, BoxEnum.NEG_ONE);
        Arrays.fill(this.elements, null);
        this.op++;
    }

    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        if (this.size < 1) return;
        for (int i = 0; i < this.capacity; i++) {
            final byte currOp = this.op;
            if (this.state[i] > -1) action.accept((E) this.elements[i]);
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
    public Iterator<E> iterator() {
        return new ElementIterator();
    }

    @NotNull
    public Spliterator<E> spliterator() {
        return new ElementSpliterator(BoxEnum.ZERO, 0, -1, 0);
    }

    protected <T> T[] prepareArray(T[] a) {
        final int size = this.size;
        if (a.length < size) return (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        if (a.length > size) a[size] = null;
        return a;
    }

    protected <T> T[] slotToArray(T[] a) {
        if (this.size < 1) return a;
        int idx = 0;
        for (int i = 0; i < this.capacity; i++) {
            if (this.state[i] > -1) {
                a[idx] = (T) this.elements[i];
                idx++;
            }
        }
        return a;
    }

    @NotNull
    public Object[] toArray() {
        return slotToArray(new Object[this.size]);
    }

    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        return slotToArray(prepareArray(a));
    }

    protected Object clone() {
        ObjRHSet<E> result;
        try {
            result = (ObjRHSet<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.state.length;
        result.state = new byte[srcLen];
        result.elements = new Object[srcLen];
        System.arraycopy(this.state, 0, result.state, 0, srcLen);
        System.arraycopy(this.elements, 0, result.elements, 0, srcLen);
        return result;
    }

    @NotNull
    public Stream<E> stream() {
        return super.stream();
    }

    protected class ElementIterator implements Iterator<E> {
        protected byte currOp;
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;
        protected int remaining;

        protected ElementIterator() {
            this.currOp = ObjRHSet.this.op;
            this.remaining = ObjRHSet.this.capacity;
            if (ObjRHSet.this.size > 0) {
                for (int i = 0; i < this.remaining; i++) {
                    if (ObjRHSet.this.state[i] > -1) {
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

        public E next() {
            if (ObjRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
            if (this.pos < 0) throw new NoSuchElementException();

            this.posPre = this.pos;
            this.pos = this.posNext;
            this.posNext = -1;

            if (this.pos > -1) {
                for (int i = this.pos + 1; i < this.remaining; i++) {
                    if (ObjRHSet.this.state[i] > -1) {
                        this.posNext = i;
                        break;
                    }
                }
            }
            return (E) ObjRHSet.this.elements[this.posPre];
        }

        public void remove() {
            if (this.posPre < 0 || ObjRHSet.this.state[this.posPre] < 0) throw new IllegalStateException();
            if (ObjRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
            final int currPos = this.posPre;
            ObjRHSet.this.size--;
            ObjRHSet.this.state[this.posPre] = -1;
            ObjRHSet.this.elements[this.posPre] = null;

            boolean refreshNextItPos = true;
            for (int i = this.posPre + 1 & ObjRHSet.this.posMask; i != currPos; i = ++i & ObjRHSet.this.posMask) {
                if (ObjRHSet.this.state[i] < 1) break;

                final int prePos = (i - 1) & ObjRHSet.this.posMask;
                ObjRHSet.this.state[prePos] = --ObjRHSet.this.state[i];
                ObjRHSet.this.elements[prePos] = ObjRHSet.this.elements[i];
                ObjRHSet.this.state[i] = -1;
                ObjRHSet.this.elements[i] = null;
                this.remaining--;
                if (refreshNextItPos && prePos == this.posPre) {
                    refreshNextItPos = false;
                    this.pos = this.posPre;
                    if (this.posNext > 0) this.posNext--;
                }
            }
            ObjRHSet.this.calculateThreshold();
            this.currOp = ++ObjRHSet.this.op;
        }
    }

    protected class ElementSpliterator implements Spliterator<E> {
        protected byte currOp;
        protected int index;
        protected int limitCap;
        protected int estSize;

        protected ElementSpliterator(byte op, int index, int limitCap, int estSize) {
            this.currOp = op;
            this.index = index;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = ObjRHSet.this.size;
                currOp = ObjRHSet.this.op;
                newLimit = this.limitCap = ObjRHSet.this.capacity;
            }
            return newLimit;
        }

        public Spliterator<E> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return (newIndex >= midIndex) ? null : new ElementSpliterator(this.currOp, newIndex, this.index = midIndex, this.estSize >>>= 1);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return false;
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (ObjRHSet.this.state[currPos] > -1) {
                    action.accept((E) ObjRHSet.this.elements[currPos]);
                    if (ObjRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return;
            while (this.index < currLimit) {
                final int currPos = this.index++;
                if (ObjRHSet.this.state[currPos] > -1) action.accept((E) ObjRHSet.this.elements[currPos]);
                if (ObjRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
            }
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjRHSet.this.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
        }
    }
}
