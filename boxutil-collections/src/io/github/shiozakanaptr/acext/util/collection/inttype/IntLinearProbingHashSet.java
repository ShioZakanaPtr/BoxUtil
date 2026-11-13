package io.github.shiozakanaptr.acext.util.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntIterator;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "deprecation", "RedundantCast"})
public class IntLinearProbingHashSet extends AbstractIntSet implements HashContainer<Integer>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static int NULL_SLOT = (int) 0;

    protected transient boolean withNullEle = false;
    protected transient int remaining = 0;
    protected transient int capacity;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected final HashBehavior<Integer> hashBehavior;
    protected transient int[] element;

    protected int[] newTable(int cap) {
        return new int[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntLinearProbingHashSet(int initialCapacity, float loadFactor, final HashBehavior<Integer> hashBehavior) {
        this.capacity = InternalCollectionUtil.checkHashCapacity(InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2)));
        this.posMask = this.capacity - 1;
        this.loadFactor = InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 0.9f);
        this.hashBehavior = Objects.requireNonNull(hashBehavior);
        this.element = this.newTable(this.capacity + 1);
        this.calculateRemaining();
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntLinearProbingHashSet(int initialCapacity, final HashBehavior<Integer> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public IntLinearProbingHashSet(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public IntLinearProbingHashSet() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntLinearProbingHashSet(final Collection<? extends Integer> source, float loadFactor, final HashBehavior<Integer> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntLinearProbingHashSet(final Collection<? extends Integer> source, final HashBehavior<Integer> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public IntLinearProbingHashSet(final Collection<? extends Integer> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntLinearProbingHashSet(final int[] source, float loadFactor, final HashBehavior<Integer> hashBehavior) {
        this(Math.max(source.length, 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public IntLinearProbingHashSet(final int[] source, final HashBehavior<Integer> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public IntLinearProbingHashSet(final int[] source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor),
                newTargetCap = InternalCollectionUtil.checkHashCapacity(InternalCollectionUtil.getCapacityPOT(newCapacity)),
                newCap = Math.max(minSize, Math.max(newTargetCap, 2));
        if (newCap == this.capacity) return;

        final int newMask = newCap - 1, oldCap;
        final int[] newEle = this.newTable(newCap + 1), oldEle = this.element;

        if (this.size > 0) {
            oldCap = this.capacity;
            for (int i = 0; i < oldCap;) {
                final int currEle;
                if ((currEle = oldEle[i++]) != ((int) NULL_SLOT)) {
                    int pos = this.hashBehavior.hashing(currEle) & newMask;
                    while (newEle[pos] != ((int) NULL_SLOT)) {
                        pos = (pos + 1) & newMask;
                    }
                    newEle[pos] = currEle;
                }
            }
            if (this.withNullEle) newEle[newCap] = oldEle[oldCap];
        }

        this.capacity = newCap;
        this.posMask = newMask;
        this.element = newEle;
        this.calculateRemaining();
    }

    public void reserve(int newMaxSize) {
        if (newMaxSize < 1) throw new IllegalArgumentException("Not a valid size: '" + newMaxSize + '\'');
        this.resize(InternalCollectionUtil.getHashTableCapacity(newMaxSize, this.loadFactor));
    }

    protected void removeNull() {
        this.withNullEle = false;
        this.element[this.capacity] = ((int) NULL_SLOT);
        this.size--;
        this.remaining++;
    }

    protected void removeAndShift(final int[] eleL, int currPos) {
        eleL[currPos] = ((int) NULL_SLOT);
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        int currEle;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currEle = eleL[nextPos = (nextPos + 1) & maskL]) != ((int) NULL_SLOT); dist++) {
            if (((nextPos - (this.hashBehavior.hashing(currEle) & maskL)) & maskL) >= dist) {
                eleL[holePos] = currEle;
                eleL[nextPos] = ((int) NULL_SLOT);
                holePos = nextPos;
                dist = 0;
            }
        }
    }

    protected void checkExpand() {
        if (this.remaining > 1) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.capacity));
    }

    public boolean contains(int o) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(o, ((int) NULL_SLOT))) return this.withNullEle;

        final int maskL = this.posMask;
        final int[] eleL = this.element;
        int currEle;
        for (int pos = behavior.hashing(o); (currEle = eleL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(o, currEle)) return true;
        }
        return false;
    }

    public boolean add(int e) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(e, ((int) NULL_SLOT))) {
            final boolean result = !this.withNullEle;
            if (result) {
                this.checkExpand();
                this.withNullEle = true;
                this.element[this.capacity] = e;
                this.size++;
                this.remaining--;
            }
            return result;
        }

        final int maskL = this.posMask;
        final int[] eleL = this.element;
        int pos = behavior.hashing(e);
        for (int currEle; (currEle = eleL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(e, currEle)) return false;
        }
        this.checkExpand();
        eleL[pos] = e;
        this.size++;
        this.remaining--;
        return true;
    }

    public boolean removeElement(int o) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(o, ((int) NULL_SLOT))) {
            final boolean result = this.withNullEle;
            if (result) this.removeNull();
            return result;
        }

        final int maskL = this.posMask;
        final int[] eleL = this.element;
        int currEle;
        for (int pos = behavior.hashing(o); (currEle = eleL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(o, currEle)) {
                this.removeAndShift(eleL, pos);
                return true;
            }
        }
        return false;
    }

    public boolean remapping(int element, IntFunction<? extends Integer> remappingFunction) {
        final HashBehavior<Integer> behavior = this.hashBehavior;
        if (behavior.equals(element, ((int) NULL_SLOT))) {
            if (!this.withNullEle) return false;

            final Integer newEle = remappingFunction.apply(element);
            if (newEle == null) {
                this.removeNull();
                return true;
            }
            final int rawEle = newEle;
            return !behavior.equals(rawEle, element) && this.add(rawEle);
        }

        final int maskL = this.posMask;
        final int[] eleL = this.element;
        int currEle;
        for (int pos = behavior.hashing(element); (currEle = eleL[pos &= maskL]) != ((int) NULL_SLOT); pos++) {
            if (behavior.equals(element, currEle)) {
                final Integer newEle = remappingFunction.apply(element);
                final int rawEle = newEle == null ? 0 : newEle;
                if (newEle != null && behavior.equals(rawEle, element)) return false;
                this.removeAndShift(eleL, pos);
                if (newEle != null) this.add(rawEle);
                return true;
            }
        }
        return false;
    }

    public void swap(Set<Integer> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof IntLinearProbingHashSet) {
            final IntLinearProbingHashSet src = (IntLinearProbingHashSet) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                final boolean currWithNull = this.withNullEle, srcWithNull = src.withNullEle;
                final int currCap = this.capacity, srcCap = src.capacity,
                        currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final int[] currEle = this.element, srcEle = src.element;

                src.withNullEle = currWithNull;
                src.capacity = currCap;
                src.size = currSize;
                src.posMask = currMask;
                src.remaining = currRemaining;
                src.element = currEle;
                this.withNullEle = srcWithNull;
                this.capacity = srcCap;
                this.size = srcSize;
                this.posMask = srcMask;
                this.remaining = srcRemaining;
                this.element = srcEle;
                return;
            }
        }
        if (source.size() < this.size()) {
            final List<Integer> tmp = new ArrayList<>(source);
            source.clear();
            source.addAll(this);
            this.clear();
            this.addAll(tmp);
        } else {
            final List<Integer> tmp = new ArrayList<>(this);
            this.clear();
            this.addAll(source);
            source.clear();
            source.addAll(tmp);
        }
    }

    public void forEach(IntConsumer action) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final int[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final int currEle;
            if ((currEle = eleL[pos++]) != ((int) NULL_SLOT)) {
                action.accept(currEle);
            }
        }

        if (this.withNullEle) action.accept(eleL[capL]);
    }

    public boolean addAll(Collection<? extends Integer> c) {
        if (c == this || c.isEmpty()) return false;
        if (c.size() >= this.remaining()) this.reserve(this.size + c.size() + 1);
        if (c instanceof IntLinearProbingHashSet) {
            final IntLinearProbingHashSet cast = (IntLinearProbingHashSet) c;
            boolean result = false;
            final int capL = cast.capacity;
            final int[] eleL = cast.element;
            for (int pos = 0; pos < capL;) {
                final int currEle;
                if ((currEle = eleL[pos++]) != ((int) NULL_SLOT)) {
                    result |= this.add(currEle);
                }
            }

            if (cast.withNullEle) result |= this.add(eleL[capL]);
            return result;
        } else return super.addAll(c);
    }

    public boolean removeIf(IntPredicate filter) {
        if (this.isEmpty()) return false;

        final int capL = this.capacity;
        final int[] eleL = this.element;
        boolean result = false;
        for (int pos = 0; pos < capL; pos++) {
            final int currEle;
            if ((currEle = eleL[pos]) != ((int) NULL_SLOT) && filter.test(currEle)) {
                this.removeAndShift(eleL, pos);
                result = true;
            }
        }

        if (this.withNullEle && filter.test(eleL[capL])) {
            this.removeNull();
            result = true;
        }
        return result;
    }

    public boolean retainAll(Collection<?> c) {
        return this.removeIf((int l_ele) -> !c.contains(l_ele));
    }

    private void calculateRemaining() {
        this.remaining = InternalCollectionUtil.getHashTableMaxRemaining(this.capacity, this.loadFactor, true) - this.size;
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

    public HashBehavior<Integer> getBehavior() {
        return this.hashBehavior;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public IntIterator iterator() {
        return new SetIterator();
    }

    public IntSpliterator spliterator() {
        return new SetSpliterator(0, -1, 0);
    }

    public int[] table() {
        return this.element;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        Arrays.fill(this.element, ((int) NULL_SLOT));
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
            this.element = this.newTable(newCap + 1);
            this.calculateRemaining();
        }
    }

    protected int[] prepareArray(final int[] a) {
        final int sizeL = this.withNullEle ? this.size + 1 : this.size;
        if (a.length < sizeL) return this.newTable(sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected int[] slotToArray(final int[] a) {
        if (this.isEmpty()) return a;

        final int[] eleL = this.element;
        int idx = 0;
        for (int pos = 0; pos < this.capacity;) {
            final int currEle;
            if ((currEle = eleL[pos++]) != ((int) NULL_SLOT)) a[idx++] = currEle;
        }
        if (this.withNullEle) a[idx] = eleL[this.capacity];
        return a;
    }

    public int[] toArrayRaw() {
        return this.slotToArray(this.newTable(this.size));
    }

    public int[] toArray(int[] a) {
        return this.slotToArray(this.prepareArray(a));
    }

    public int hashCode() {
        if (this.isEmpty()) return 0;

        int result = 0;
        final int capL = this.capacity;
        final int[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final int currEle;
            if ((currEle = eleL[pos++]) != ((int) NULL_SLOT)) {
                result += Integer.hashCode(currEle);
            }
        }

        if (this.withNullEle) result += Integer.hashCode(eleL[capL]);
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Set<?>) {
            final Set<?> castSet = (Set<?>) o;
            if (castSet.size() != this.size()) return false;
            try {
                if (castSet instanceof IntLinearProbingHashSet) {
                    final IntLinearProbingHashSet cast = (IntLinearProbingHashSet) castSet;
                    if (this.withNullEle != cast.withNullEle) return false;
                    final int[] srcEleL = cast.element;

                    final int capacity = srcEleL.length;
                    for (int srcPos = 0; srcPos < capacity;) {
                        final int srcEle;
                        if ((srcEle = srcEleL[srcPos++]) != ((int) NULL_SLOT)) {
                            if (!this.contains(srcEle)) return false;
                        }
                    }

                    if (this.withNullEle) return this.element[this.capacity] == srcEleL[cast.capacity];
                } else {
                    for (final Object e : castSet) {
                        if (!this.contains(e)) return false;
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
        final int capL = this.capacity;
        final int[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final int currEle;
            if ((currEle = eleL[pos++]) != ((int) NULL_SLOT)) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currEle);
            }
        }

        if (this.withNullEle) {
            if (sb.length() > 1) sb.append(',').append(' ');
            sb.append(eleL[capL]);
        }
        sb.append(']');
        return sb.toString();
    }

    public Object clone() {
        IntLinearProbingHashSet result;
        try {
            result = (IntLinearProbingHashSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.element.length;
        result.element = result.newTable(srcLen);
        System.arraycopy(this.element, 0, result.element, 0, srcLen);
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final int[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final int currEle;
            if ((currEle = eleL[pos++]) != ((int) NULL_SLOT)) out.writeInt(currEle);
        }
        if (this.withNullEle) out.writeInt(eleL[capL]);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehavior);
        InternalCollectionUtil.SerialCheck.ioCheckHashLoadFactor(this.loadFactor, 0.2f, 0.9f);
        InternalCollectionUtil.SerialCheck.ioCheckSize(this.size, 0, HashContainer.MAXIMUM_CAPACITY);
        this.capacity = InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(this.size, this.loadFactor, HashContainer.MAXIMUM_CAPACITY);
        final int maskL = this.posMask = this.capacity - 1;
        final int[] eleL = this.element = this.newTable(this.capacity + 1);
        this.withNullEle = false;

        final HashBehavior<Integer> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final int currEle = in.readInt();
            int pos;
            if (behavior.equals(currEle, ((int) NULL_SLOT))) {
                if (this.withNullEle) InternalCollectionUtil.SerialCheck.ioCheckElementIsDuplicate(currEle);
                pos = this.capacity;
                this.withNullEle = true;
            } else {
                pos = behavior.hashing(currEle) & maskL;
                int findEle;
                while ((findEle = eleL[pos]) != ((int) NULL_SLOT)) {
                    if (behavior.equals(currEle, findEle)) InternalCollectionUtil.SerialCheck.ioCheckElementIsDuplicate(currEle);
                    pos = (pos + 1) & maskL;
                }
            }
            eleL[pos] = currEle;
        }
        this.calculateRemaining();
    }

    protected class SetIterator implements IntIterator {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected SetIterator() {
            if (IntLinearProbingHashSet.this.size > 0) {
                this.scanPos(IntLinearProbingHashSet.this.element, 0);
            }
        }

        protected void scanPos(int[] eleL, int start) {
            final int capL = IntLinearProbingHashSet.this.capacity;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (eleL[i] != ((int) NULL_SLOT)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
            }
            if (IntLinearProbingHashSet.this.withNullEle) {
                if (findPos < 0) findPos = capL;
                else if (findPosNext < 0) findPosNext = capL;
            }
            this.pos = findPos;
            this.posNext = findPosNext;
        }

        public boolean hasNext() {
            return this.pos > -1;
        }

        public int nextInt() {
            final int current;
            if ((current = this.pos) < 0) throw new NoSuchElementException();

            this.posPre = current;
            final int posL = this.pos = this.posNext;
            this.posNext = -1;

            final int[] eleL = IntLinearProbingHashSet.this.element;
            if (posL > -1) {
                final int capL = IntLinearProbingHashSet.this.capacity;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (eleL[i] != ((int) NULL_SLOT)) {
                        this.posNext = i;
                        break;
                    }
                }
                if (i == capL && IntLinearProbingHashSet.this.withNullEle) {
                    this.posNext = i;
                }
            }
            return eleL[current];
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            if (posPreL == IntLinearProbingHashSet.this.capacity) {
                IntLinearProbingHashSet.this.removeNull();
                this.pos = -1;
            } else {
                final int[] eleL = IntLinearProbingHashSet.this.element;
                IntLinearProbingHashSet.this.removeAndShift(eleL, posPreL);
                this.scanPos(eleL, posPreL);
            }
            this.posPre = -1;
        }

        public void forEachRemaining(IntConsumer action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final int[] eleL = IntLinearProbingHashSet.this.element;
            final int capL = IntLinearProbingHashSet.this.capacity;
            int i, findPre = this.posPre;
            for (i = current; i < capL; i++) {
                final int currEle;
                if ((currEle = eleL[i]) != ((int) NULL_SLOT)) {
                    findPre = i;
                    action.accept(currEle);
                }
            }
            if (i == capL && IntLinearProbingHashSet.this.withNullEle) {
                findPre = i;
                action.accept(eleL[capL]);
            }
            this.posPre = findPre;
            this.pos = -1;
        }
    }

    protected class SetSpliterator implements IntSpliterator {
        protected int index;
        protected int limitCap;
        protected int estSize;

        protected SetSpliterator(int index, int limitCap, int estSize) {
            this.index = index;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = IntLinearProbingHashSet.this.size;
                newLimit = this.limitCap = IntLinearProbingHashSet.this.capacity;
            }
            return newLimit;
        }

        public IntSpliterator trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? new SetSpliterator(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        public boolean tryAdvance(IntConsumer action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            final int[] eleL = IntLinearProbingHashSet.this.element;
            while (idxL < currLimit) {
                final int currEle;
                if ((currEle = eleL[idxL++]) != ((int) NULL_SLOT)) {
                    this.index = idxL;
                    action.accept(currEle);
                    return true;
                }
            }
            final int capL = IntLinearProbingHashSet.this.capacity;
            if (idxL == currLimit && idxL == capL && IntLinearProbingHashSet.this.withNullEle) {
                action.accept(eleL[capL]);
                this.index = capL + 1;
                return true;
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(IntConsumer action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            final int[] eleL = IntLinearProbingHashSet.this.element;
            while (idxL < currLimit) {
                final int currEle;
                if ((currEle = eleL[idxL++]) != ((int) NULL_SLOT)) action.accept(currEle);
            }
            final int capL = IntLinearProbingHashSet.this.capacity;
            if (idxL == currLimit && idxL == capL && IntLinearProbingHashSet.this.withNullEle) {
                action.accept(eleL[capL]);
                idxL = capL + 1;
            }
            this.index = idxL;
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == IntLinearProbingHashSet.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }
}
