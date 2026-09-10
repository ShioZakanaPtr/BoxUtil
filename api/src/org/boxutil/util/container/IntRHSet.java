package org.boxutil.util.container;

import org.boxutil.define.BoxEnum;
import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

/**
 * The map is not thread safe.<p>
 * Unbounded unordered hash set, based Robin Hood hashing.<p>
 * Element can be <code>null</code>, but it will be treated as <code>0</code> or <code>Integer(0)</code>.<p>
 * The default hasher using {@link ContainerHasher#hashing_HashMap(Object)}.<p>
 * This Set implementation is suitable for scenarios with many reads and few writes,
 * or you can ensure that resizing is not triggered too frequently,
 * also and memory usage is not a primary concern.<p>
 * Otherwise, it is still recommended to use {@link HashSet} or another more appropriate implementation.<p>
 * Internally, elements are stored directly as primitive types;
 * therefore, this class should be treated as a Set of primitive types, not as a Set of their corresponding wrapper types.<p>
 * For store float value, just using {@link Float#floatToRawIntBits(float)} and {@link Float#intBitsToFloat(int)} directly.
 */
@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused"})
public class IntRHSet extends AbstractSet<Integer> implements IntSet, Cloneable {
    public static final int DEFAULT_INITIAL_CAPACITY = 1 << 7;
    public static final int MAXIMUM_CAPACITY = 1 << 29;

    protected byte op = 0;
    protected int capacity;
    protected int posMask;
    protected int size = 0;
    protected float threshold = 0.0f;
    protected final float loadFactor;
    protected final IntUnaryOperator hasher;
    protected int[] ses; // i = {iState, iElement}, state = {-1 => empty, (x > -1) => psl}

    protected static void fillStorage(final int[] target) {
        for (int i = 0, len = target.length; i < len; i++) {
            target[i] = ((i & 1) > 0) ? 0 : BoxEnum.NEG_ONE;
        }
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, clamp to range <b>[0.2f, 0.9f]</b>.
     * @param hasher when an element is <code>null</code>, it hash must be <code>0</code>
     */
    public IntRHSet(int initialCapacity, float loadFactor, final IntUnaryOperator hasher) {
        if (initialCapacity > MAXIMUM_CAPACITY || initialCapacity < 1) throw new IllegalArgumentException("The capacity of map overflow");
        this.loadFactor = Math.max(Math.min(loadFactor, 0.91f), 0.2f);
        this.capacity = CalculateUtil.getPOTMax(Math.max(initialCapacity, 2));
        this.posMask = this.capacity - 1;
        this.hasher = Objects.requireNonNull(hasher);
        this.ses = new int[this.capacity << 1];
        fillStorage(this.ses);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hasher when an element is <code>null</code>, it hash must be <code>0</code>
     */
    public IntRHSet(int initialCapacity, final IntUnaryOperator hasher) {
        this(initialCapacity, 0.75f, hasher);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public IntRHSet(int initialCapacity) {
        this(initialCapacity, 0.75f, ContainerHasher::hashing_HashMap);
    }

    public IntRHSet() {
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

    public IntUnaryOperator getHasher() {
        return this.hasher;
    }

    protected interface BoolExc {
        boolean run(int pos, int statePos, int elementPos);
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
            final int[] newSes = new int[reqCap << 1];
            fillStorage(newSes);

            for (int i = 0; i < this.capacity; i++) {
                final int newRealPos = i << 1;
                if (this.ses[newRealPos] < 0) continue;

                final int newRealElePos = newRealPos + 1;
                int oldEle = this.ses[newRealElePos];

                int newPos = this.hasher.applyAsInt(oldEle) & newPosMask, currState, currPsl = 0;

                final int startPos = newPos;
                while ((currState = newSes[newRealPos]) > -1) {
                    if (currPsl > currState) {
                        final int tmpState = newSes[newRealPos], tmpEle = newSes[newRealElePos];

                        newSes[newRealPos] = currPsl;
                        currPsl = tmpState;
                        newSes[newRealElePos] = oldEle;
                        oldEle = tmpEle;
                    }
                    newPos = ++newPos & newPosMask;
                    if (++currPsl > 127 || newPos == startPos) { // you should goto use better hasher
                        this.threshold = Math.max(this.threshold, 2.0f) * 2.0f;
                        this.checkAndResize(true, extraExpand + 1);
                        return;
                    }
                }

                newSes[newRealPos] = currPsl;
                newSes[newRealElePos] = oldEle;
            }

            this.capacity = reqCap;
            this.posMask = newPosMask;
            this.ses = newSes;
            this.calculateThreshold();
            this.op++;
        }
    }

    protected void removeAndShift(int currPos, int currRealPos, int currRealElePos) {
        this.size--;
        this.ses[currRealPos] = BoxEnum.NEG_ONE;
        this.ses[currRealElePos] = 0;
        for (int i = (currPos + 1) & this.posMask; i != currPos; i = ++i & this.posMask) {
            final int realPos = i << 1;
            if (this.ses[realPos] < 1) return;
            final int realElePos = realPos + 1, preRealPos = ((i - 1) & this.posMask) << 1;
            this.ses[preRealPos] = --this.ses[realPos];
            this.ses[preRealPos + 1] = this.ses[realElePos];
            this.ses[realPos] = BoxEnum.NEG_ONE;
            this.ses[realElePos] = 0;
        }
        this.calculateThreshold();
        this.op++;
    }

    public boolean addNumber(int e) {
        this.checkAndResize(false, 1);

        int pos = this.hasher.applyAsInt(e) & this.posMask, realPos = pos << 1, currPsl = 0, currState, addEle = e;

        final int startPos = pos;
        while ((currState = this.ses[realPos]) > -1) {
            final int realElePos = realPos + 1;
            if (addEle == this.ses[realElePos]) return false;
            if (currPsl > currState) {
                final int tmpState = this.ses[realPos], tmpEle = this.ses[realElePos];

                this.ses[realPos] = currPsl;
                currPsl = tmpState;
                this.ses[realElePos] = addEle;
                addEle = tmpEle;
            }
            pos = ++pos & this.posMask;
            if (++currPsl > 127 || pos == startPos) {
                this.checkAndResize(true, 1);
                return this.addNumber(addEle);
            }
        }

        this.size++;
        this.ses[pos] = currPsl;
        this.ses[pos] = addEle;
        this.calculateThreshold();
        this.op++;
        return true;
    }

    public boolean addAll(@NotNull Collection<? extends Integer> c) {
        if (c.isEmpty()) return false;
        this.threshold = (float) (c.size() + this.size) / (float) this.capacity;
        return super.addAll(c);
    }

    protected boolean getVal(int element, final BoolExc matched) {
        if (this.isEmpty()) return false;

        int pos = this.hasher.applyAsInt(element) & this.posMask, realPos = pos << 1, currPsl = 0, currState;

        final int startPos = pos;
        while ((currState = this.ses[realPos]) > -1) {
            final int realElePos = realPos + 1;
            if (Objects.equals(element, this.ses[realElePos])) return matched.run(pos, realPos, realElePos);
            if (currState < currPsl) return false;
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            if (++currPsl > 127 || pos == startPos) return false;
        }
        return false;
    }

    public boolean containsNumber(int o) {
        return this.getVal(o, (l_i, l_si, l_ei) -> true);
    }

    public boolean removeNumber(int o) {
        return this.getVal(o, (l_i, l_si, l_ei) -> {
            this.removeAndShift(l_i, l_si, l_ei);
            return true;
        });
    }

    public void clear() {
        this.size = 0;
        this.threshold = 0.0f;
        fillStorage(this.ses);
        this.op++;
    }

    public void forEach(Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        if (this.size < 1) return;
        for (int i = 0; i < this.capacity; i++) {
            final byte currOp = this.op;
            final int realPos = i << 1;
            if (this.ses[realPos] > -1) action.accept(this.ses[realPos + 1]);
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
    public Iterator<Integer> iterator() {
        return new ElementIterator();
    }

    @NotNull
    public Spliterator<Integer> spliterator() {
        return new ElementSpliterator(BoxEnum.ZERO, 0, -1, 0);
    }

    protected <T> T[] prepareArray(T[] a) {
        final int size = this.size;
        if (a.length < size) return (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        if (a.length > size) a[size] = null;
        return a;
    }

    protected <T> T[] slotToArray(T[] a) {
        if (!a.getClass().isAssignableFrom(Integer.class)) throw new IllegalArgumentException("Not a Integer array");
        if (this.size < 1) return a;
        int idx = 0;
        for (int i = 0; i < this.capacity; i++) {
            final int realPos = i << 1;
            if (this.ses[realPos] > -1) {
                a[idx] = (T) Integer.valueOf(this.ses[realPos + 1]);
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
        IntRHSet result;
        try {
            result = (IntRHSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.ses.length;
        result.ses = new int[srcLen];
        System.arraycopy(this.ses, 0, result.ses, 0, srcLen);
        return result;
    }

    protected class ElementIterator implements Iterator<Integer> {
        protected byte currOp;
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;
        protected int remaining;

        protected ElementIterator() {
            this.currOp = IntRHSet.this.op;
            this.remaining = IntRHSet.this.capacity;
            if (IntRHSet.this.size > 0) {
                for (int i = 0; i < IntRHSet.this.capacity; i++) {
                    final int currRealPos = i << 1;
                    if (IntRHSet.this.ses[currRealPos] > -1) {
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

        public Integer next() {
            if (IntRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
            if (this.pos < 0) throw new NoSuchElementException();

            this.posPre = this.pos;
            this.pos = this.posNext;
            this.posNext = -1;

            if (this.pos > -1) {
                for (int i = this.pos + 1; i < IntRHSet.this.capacity; i++) {
                    if (IntRHSet.this.ses[i << 1] > -1) {
                        this.posNext = i;
                        break;
                    }
                }
            }
            return IntRHSet.this.ses[(this.posPre << 1) + 1];
        }

        public void remove() {
            final int currRealPos = this.posPre << 1;
            if (this.posPre < 0 || IntRHSet.this.ses[currRealPos] < 0) throw new IllegalStateException();
            if (IntRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
            final int currPos = this.posPre;
            IntRHSet.this.size--;
            IntRHSet.this.ses[currRealPos] = BoxEnum.NEG_ONE;
            IntRHSet.this.ses[currRealPos + 1] = 0;

            boolean refreshNextItPos = true;
            for (int i = this.posPre + 1 & IntRHSet.this.posMask; i != currPos; i = ++i & IntRHSet.this.posMask) {
                final int realPos = i << 1;
                if (IntRHSet.this.ses[realPos] < 1) break;

                final int realElePos = realPos + 1, preRealPos = ((i - 1) & IntRHSet.this.posMask) << 1;
                IntRHSet.this.ses[preRealPos] = --IntRHSet.this.ses[realPos];
                IntRHSet.this.ses[preRealPos + 1] = IntRHSet.this.ses[realElePos];
                IntRHSet.this.ses[realPos] = BoxEnum.NEG_ONE;
                IntRHSet.this.ses[realElePos] = 0;
                this.remaining--;
                if (refreshNextItPos && preRealPos == currRealPos) {
                    refreshNextItPos = false;
                    this.pos = this.posPre;
                    if (this.posNext > 0) this.posNext--;
                }
            }
            IntRHSet.this.calculateThreshold();
            this.currOp = ++IntRHSet.this.op;
        }
    }

    protected class ElementSpliterator implements Spliterator<Integer> {
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
                this.estSize = IntRHSet.this.size;
                currOp = IntRHSet.this.op;
                newLimit = this.limitCap = IntRHSet.this.capacity;
            }
            return newLimit;
        }

        public Spliterator<Integer> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return (newIndex >= midIndex) ? null : new ElementSpliterator(this.currOp, newIndex, this.index = midIndex, this.estSize >>>= 1);
        }

        public boolean tryAdvance(Consumer<? super Integer> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return false;
            while (this.index < currLimit) {
                final int realPos = this.index++ << 1;
                if (IntRHSet.this.ses[realPos] > -1) {
                    action.accept(IntRHSet.this.ses[realPos + 1]);
                    if (IntRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super Integer> action) {
            Objects.requireNonNull(action);
            final int currLimit = this.getLimitCap();
            if (this.index < 0) return;
            while (this.index < currLimit) {
                final int realPos = this.index++ << 1;
                if (IntRHSet.this.ses[realPos] > -1) action.accept(IntRHSet.this.ses[realPos + 1]);
                if (IntRHSet.this.op != this.currOp) throw new ConcurrentModificationException();
            }
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == IntRHSet.this.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
        }
    }
}
