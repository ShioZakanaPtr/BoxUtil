package io.github.shiozakanaptr.acext.util.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSpliterator;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "deprecation", "RedundantCast"})
public class LongLinearProbingHashSet extends AbstractLongSet implements HashContainer<Long>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static long NULL_SLOT = (long) 0;

    protected transient boolean withNullEle = false;
    protected transient int remaining = 0;
    protected transient int capacity;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected final HashBehavior<Long> hashBehavior;
    protected transient long[] element;

    protected long[] newTable(int cap) {
        return new long[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongLinearProbingHashSet(int initialCapacity, float loadFactor, final HashBehavior<Long> hashBehavior) {
        InternalCollectionUtil.checkHashCapacity(initialCapacity);
        InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 0.9f);

        this.capacity = InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2));
        this.posMask = this.capacity - 1;
        this.loadFactor = loadFactor;
        this.hashBehavior = Objects.requireNonNull(hashBehavior);
        this.element = this.newTable(this.capacity + 1);
        this.calculateRemaining();
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongLinearProbingHashSet(int initialCapacity, final HashBehavior<Long> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public LongLinearProbingHashSet(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public LongLinearProbingHashSet() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongLinearProbingHashSet(final Collection<? extends Long> source, float loadFactor, final HashBehavior<Long> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongLinearProbingHashSet(final Collection<? extends Long> source, final HashBehavior<Long> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public LongLinearProbingHashSet(final Collection<? extends Long> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongLinearProbingHashSet(final long[] source, float loadFactor, final HashBehavior<Long> hashBehavior) {
        this(Math.max(source.length, 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public LongLinearProbingHashSet(final long[] source, final HashBehavior<Long> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public LongLinearProbingHashSet(final long[] source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor), newTargetCap = InternalCollectionUtil.getCapacityPOT(newCapacity);
        InternalCollectionUtil.checkHashCapacity(newTargetCap);
        if (newTargetCap == this.capacity) return;

        final int newCap = Math.max(minSize, Math.max(newTargetCap, 2)),
                newMask = newCap - 1, oldMask, oldCap;
        final long[] newEle = this.newTable(newCap + 1), oldEle = this.element;

        if (this.size > 0) {
            oldCap = this.capacity;
            for (int i = 0; i < oldCap;) {
                final long currEle;
                if ((currEle = oldEle[i++]) != ((long) NULL_SLOT)) {
                    int pos = this.hashBehavior.hashing(currEle) & newMask;
                    while (newEle[pos] != ((long) NULL_SLOT)) {
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
        this.element[this.capacity] = ((long) NULL_SLOT);
        this.size--;
        this.remaining++;
    }

    protected void removeAndShift(final long[] eleL, int currPos) {
        eleL[currPos] = ((long) NULL_SLOT);
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        long currEle;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currEle = eleL[nextPos = (nextPos + 1) & maskL]) != ((long) NULL_SLOT); dist++) {
            if (((nextPos - (this.hashBehavior.hashing(currEle) & maskL)) & maskL) >= dist) {
                eleL[holePos] = currEle;
                eleL[nextPos] = ((long) NULL_SLOT);
                holePos = nextPos;
                dist = 0;
            }
        }
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.capacity));
    }

    public boolean contains(long o) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(o, ((long) NULL_SLOT))) return this.withNullEle;

        final int maskL = this.posMask;
        final long[] eleL = this.element;
        long currEle;
        for (int pos = behavior.hashing(o); (currEle = eleL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(o, currEle)) return true;
        }
        return false;
    }

    public boolean add(long e) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(e, ((long) NULL_SLOT))) {
            final boolean result = !this.withNullEle;
            if (result) {
                this.withNullEle = true;
                this.element[this.capacity] = e;
                this.size++;
                this.remaining--;
                this.checkExpand();
            }
            return result;
        }

        final int maskL = this.posMask;
        final long[] eleL = this.element;
        int pos = behavior.hashing(e);
        for (long currEle; (currEle = eleL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(e, currEle)) return false;
        }
        eleL[pos] = e;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return true;
    }

    public boolean removeElement(long o) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(o, ((long) NULL_SLOT))) {
            final boolean result = this.withNullEle;
            if (result) this.removeNull();
            return result;
        }

        final int maskL = this.posMask;
        final long[] eleL = this.element;
        long currEle;
        for (int pos = behavior.hashing(o); (currEle = eleL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(o, currEle)) {
                this.removeAndShift(eleL, pos);
                return true;
            }
        }
        return false;
    }

    public boolean remapping(long element, LongFunction<? extends Long> remappingFunction) {
        final HashBehavior<Long> behavior = this.hashBehavior;
        if (behavior.equals(element, ((long) NULL_SLOT))) {
            if (!this.withNullEle) return false;

            final Long newEle = remappingFunction.apply(element);
            if (newEle == null) {
                this.removeNull();
                return true;
            }
            final long rawEle = newEle;
            return !behavior.equals(rawEle, element) && this.add(rawEle);
        }

        final int maskL = this.posMask;
        final long[] eleL = this.element;
        long currEle;
        for (int pos = behavior.hashing(element); (currEle = eleL[pos &= maskL]) != ((long) NULL_SLOT); pos++) {
            if (behavior.equals(element, currEle)) {
                final Long newEle = remappingFunction.apply(element);
                final long rawEle = newEle == null ? 0 : newEle;
                if (newEle != null && behavior.equals(rawEle, element)) return false;
                this.removeAndShift(eleL, pos);
                if (newEle != null) this.add(rawEle);
                return true;
            }
        }
        return false;
    }

    public void swap(Set<Long> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof LongLinearProbingHashSet) {
            final LongLinearProbingHashSet src = (LongLinearProbingHashSet) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                final boolean currWithNull = this.withNullEle, srcWithNull = src.withNullEle;
                final int currCap = this.capacity, srcCap = src.capacity,
                        currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final long[] currEle = this.element, srcEle = src.element;

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
            final List<Long> tmp = new LongArrayList(source);
            source.clear();
            source.addAll(this);
            this.clear();
            this.addAll(tmp);
        } else {
            final List<Long> tmp = new LongArrayList(this);
            this.clear();
            this.addAll(source);
            source.clear();
            source.addAll(tmp);
        }
    }

    public void forEach(LongConsumer action) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final long[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final long currEle;
            if ((currEle = eleL[pos++]) != ((long) NULL_SLOT)) {
                action.accept(currEle);
            }
        }

        if (this.withNullEle) action.accept(eleL[capL]);
    }

    public boolean addAll(Collection<? extends Long> c) {
        if (c == this || c.isEmpty()) return false;
        if (c.size() >= this.remaining()) this.reserve(this.size + c.size() + 1);
        if (c instanceof LongLinearProbingHashSet) {
            final LongLinearProbingHashSet cast = (LongLinearProbingHashSet) c;
            boolean result = false;
            final int capL = cast.capacity;
            final long[] eleL = cast.element;
            for (int pos = 0; pos < capL;) {
                final long currEle;
                if ((currEle = eleL[pos++]) != ((long) NULL_SLOT)) {
                    result |= this.add(currEle);
                }
            }

            if (cast.withNullEle) result |= this.add(eleL[capL]);
            return result;
        } else return super.addAll(c);
    }

    public boolean removeIf(LongPredicate filter) {
        if (this.isEmpty()) return false;

        final int capL = this.capacity;
        final long[] eleL = this.element;
        boolean result = false;
        for (int pos = 0; pos < capL; pos++) {
            final long currEle;
            if ((currEle = eleL[pos]) != ((long) NULL_SLOT) && filter.test(currEle)) {
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
        return this.removeIf((long l_ele) -> !c.contains(l_ele));
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

    public HashBehavior<Long> getBehavior() {
        return this.hashBehavior;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public LongIterator iterator() {
        return new SetIterator();
    }

    public LongSpliterator spliterator() {
        return new SetSpliterator(0, -1, 0);
    }

    public long[] table() {
        return this.element;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        Arrays.fill(this.element, ((long) NULL_SLOT));
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

    protected long[] prepareArray(final long[] a) {
        final int sizeL = this.withNullEle ? this.size + 1 : this.size;
        if (a.length < sizeL) return this.newTable(sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected long[] slotToArray(final long[] a) {
        if (this.isEmpty()) return a;

        final long[] eleL = this.element;
        int idx = 0;
        for (int pos = 0; pos < this.capacity;) {
            final long currEle;
            if ((currEle = eleL[pos++]) != ((long) NULL_SLOT)) a[idx++] = currEle;
        }
        if (this.withNullEle) a[idx] = eleL[this.capacity];
        return a;
    }

    public long[] toArrayRaw() {
        return this.slotToArray(this.newTable(this.size));
    }

    public long[] toArray(long[] a) {
        return this.slotToArray(this.prepareArray(a));
    }

    public int hashCode() {
        if (this.isEmpty()) return 0;

        int result = 0;
        final int capL = this.capacity;
        final long[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final long currEle;
            if ((currEle = eleL[pos++]) != ((long) NULL_SLOT)) {
                result += Long.hashCode(currEle);
            }
        }

        if (this.withNullEle) result += Long.hashCode(eleL[capL]);
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Set<?>) {
            final Set<?> castSet = (Set<?>) o;
            if (castSet.size() != this.size()) return false;
            try {
                if (castSet instanceof LongLinearProbingHashSet) {
                    final LongLinearProbingHashSet cast = (LongLinearProbingHashSet) castSet;
                    if (this.withNullEle != cast.withNullEle) return false;
                    final long[] srcEleL = cast.element;

                    final int capacity = srcEleL.length;
                    for (int srcPos = 0; srcPos < capacity;) {
                        final long srcEle;
                        if ((srcEle = srcEleL[srcPos++]) != ((long) NULL_SLOT)) {
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
        final long[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final long currEle;
            if ((currEle = eleL[pos++]) != ((long) NULL_SLOT)) {
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
        LongLinearProbingHashSet result;
        try {
            result = (LongLinearProbingHashSet) super.clone();
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
        final long[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final long currEle;
            if ((currEle = eleL[pos++]) != ((long) NULL_SLOT)) out.writeLong(currEle);
        }
        if (this.withNullEle) out.writeLong(eleL[capL]);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehavior);
        InternalCollectionUtil.SerialCheck.ioCheckHashLoadFactor(this.loadFactor, 0.2f, 0.9f);
        InternalCollectionUtil.SerialCheck.ioCheckSize(this.size, 0, HashContainer.MAXIMUM_CAPACITY);
        this.capacity = InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(this.size, this.loadFactor, HashContainer.MAXIMUM_CAPACITY);
        final int maskL = this.posMask = this.capacity - 1;
        final long[] eleL = this.element = this.newTable(this.capacity + 1);
        this.withNullEle = false;

        final HashBehavior<Long> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final long currEle = in.readLong();
            int pos;
            if (behavior.equals(currEle, ((long) NULL_SLOT))) {
                if (this.withNullEle) InternalCollectionUtil.SerialCheck.ioCheckElementIsDuplicate(currEle);
                pos = this.capacity;
                this.withNullEle = true;
            } else {
                pos = behavior.hashing(currEle) & maskL;
                long findEle;
                while ((findEle = eleL[pos]) != ((long) NULL_SLOT)) {
                    if (behavior.equals(currEle, findEle)) InternalCollectionUtil.SerialCheck.ioCheckElementIsDuplicate(currEle);
                    pos = (pos + 1) & maskL;
                }
            }
            eleL[pos] = currEle;
        }
        this.calculateRemaining();
    }

    protected class SetIterator implements LongIterator {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected SetIterator() {
            if (LongLinearProbingHashSet.this.size > 0) {
                this.scanPos(LongLinearProbingHashSet.this.element, 0);
            }
        }

        protected void scanPos(final long[] eleL, int start) {
            final int capL = LongLinearProbingHashSet.this.capacity;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (eleL[i] != ((long) NULL_SLOT)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
            }
            if (LongLinearProbingHashSet.this.withNullEle) {
                if (findPos < 0) findPos = capL;
                else if (findPosNext < 0) findPosNext = capL;
            }
            this.pos = findPos;
            this.posNext = findPosNext;
        }

        public boolean hasNext() {
            return this.pos > -1;
        }

        public long nextLong() {
            final int current;
            if ((current = this.pos) < 0) throw new NoSuchElementException();

            this.posPre = current;
            final int posL = this.pos = this.posNext;
            this.posNext = -1;

            final long[] eleL = LongLinearProbingHashSet.this.element;
            if (posL > -1) {
                final int capL = LongLinearProbingHashSet.this.capacity;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (eleL[i] != ((long) NULL_SLOT)) {
                        this.posNext = i;
                        break;
                    }
                }
                if (i == capL && LongLinearProbingHashSet.this.withNullEle) {
                    this.posNext = i;
                }
            }
            return eleL[current];
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            if (posPreL == LongLinearProbingHashSet.this.capacity) {
                LongLinearProbingHashSet.this.removeNull();
                this.pos = -1;
            } else {
                final long[] eleL = LongLinearProbingHashSet.this.element;
                LongLinearProbingHashSet.this.removeAndShift(eleL, posPreL);
                this.scanPos(eleL, posPreL);
            }
            this.posPre = -1;
        }

        public void forEachRemaining(LongConsumer action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final long[] eleL = LongLinearProbingHashSet.this.element;
            final int capL = LongLinearProbingHashSet.this.capacity;
            int i, findPre = this.posPre;
            for (i = current; i < capL; i++) {
                final long currEle;
                if ((currEle = eleL[i]) != ((long) NULL_SLOT)) {
                    findPre = i;
                    action.accept(currEle);
                }
            }
            if (i == capL && LongLinearProbingHashSet.this.withNullEle) {
                findPre = i;
                action.accept(eleL[capL]);
            }
            this.posPre = findPre;
            this.pos = -1;
        }
    }

    protected class SetSpliterator implements LongSpliterator {
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
                this.estSize = LongLinearProbingHashSet.this.size;
                newLimit = this.limitCap = LongLinearProbingHashSet.this.capacity;
            }
            return newLimit;
        }

        public LongSpliterator trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? new SetSpliterator(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        public boolean tryAdvance(LongConsumer action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            final long[] eleL = LongLinearProbingHashSet.this.element;
            while (idxL < currLimit) {
                final long currEle;
                if ((currEle = eleL[idxL++]) != ((long) NULL_SLOT)) {
                    this.index = idxL;
                    action.accept(currEle);
                    return true;
                }
            }
            final int capL = LongLinearProbingHashSet.this.capacity;
            if (idxL == currLimit && idxL == capL && LongLinearProbingHashSet.this.withNullEle) {
                action.accept(eleL[capL]);
                this.index = capL + 1;
                return true;
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(LongConsumer action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            final long[] eleL = LongLinearProbingHashSet.this.element;
            while (idxL < currLimit) {
                final long currEle;
                if ((currEle = eleL[idxL++]) != ((long) NULL_SLOT)) action.accept(currEle);
            }
            final int capL = LongLinearProbingHashSet.this.capacity;
            if (idxL == currLimit && idxL == capL && LongLinearProbingHashSet.this.withNullEle) {
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
            return (this.limitCap < 0 || this.estSize == LongLinearProbingHashSet.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }
}
