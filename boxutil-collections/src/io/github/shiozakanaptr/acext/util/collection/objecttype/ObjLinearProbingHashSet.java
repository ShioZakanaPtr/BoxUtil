package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
public class ObjLinearProbingHashSet<E> extends AbstractObjSet<E> implements HashContainer<E>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static Object NULL_SLOT = null;

    protected transient boolean withNullEle = false;
    protected transient int remaining = 0;
    protected transient int capacity;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected final HashBehavior<E> hashBehavior;
    protected transient E[] element;

    protected E[] newTable(int cap) {
        return (E[]) new Object[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLinearProbingHashSet(int initialCapacity, float loadFactor, final HashBehavior<E> hashBehavior) {
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
    public ObjLinearProbingHashSet(int initialCapacity, final HashBehavior<E> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjLinearProbingHashSet(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public ObjLinearProbingHashSet() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLinearProbingHashSet(final Collection<? extends E> source, float loadFactor, final HashBehavior<E> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjLinearProbingHashSet(final Collection<? extends E> source, final HashBehavior<E> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public ObjLinearProbingHashSet(final Collection<? extends E> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public <T extends E> ObjLinearProbingHashSet(final T[] source, float loadFactor, final HashBehavior<E> hashBehavior) {
        this(Math.max(source.length, 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public <T extends E> ObjLinearProbingHashSet(final T[] source, final HashBehavior<E> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public <T extends E> ObjLinearProbingHashSet(final T[] source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor),
                newTargetCap = InternalCollectionUtil.checkHashCapacity(InternalCollectionUtil.getCapacityPOT(newCapacity)),
                newCap = Math.max(minSize, Math.max(newTargetCap, 2));
        if (newCap == this.capacity) return;

        final int newMask = newCap - 1, oldCap;
        final E[] newEle = this.newTable(newCap + 1), oldEle = this.element;

        if (this.size > 0) {
            oldCap = this.capacity;
            for (int i = 0; i < oldCap;) {
                final E currEle;
                if ((currEle = oldEle[i++]) != ((E) NULL_SLOT)) {
                    int pos = this.hashBehavior.hashing(currEle) & newMask;
                    while (newEle[pos] != ((E) NULL_SLOT)) {
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
        this.element[this.capacity] = ((E) NULL_SLOT);
        this.size--;
        this.remaining++;
    }

    protected void removeAndShift(final E[] eleL, int currPos) {
        eleL[currPos] = ((E) NULL_SLOT);
        this.size--;
        this.remaining++;

        final int maskL = this.posMask;
        E currEle;
        for (int holePos = currPos, nextPos = holePos, dist = 1; (currEle = eleL[nextPos = (nextPos + 1) & maskL]) != ((E) NULL_SLOT); dist++) {
            if (((nextPos - (this.hashBehavior.hashing(currEle) & maskL)) & maskL) >= dist) {
                eleL[holePos] = currEle;
                eleL[nextPos] = ((E) NULL_SLOT);
                holePos = nextPos;
                dist = 0;
            }
        }
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.capacity));
    }

    public boolean contains(Object o) {
        final HashBehavior<E> behavior = this.hashBehavior;
        if (behavior.equals(o, ((E) NULL_SLOT))) return this.withNullEle;

        final int maskL = this.posMask;
        final E[] eleL = this.element;
        E currEle;
        for (int pos = behavior.hashing(o); (currEle = eleL[pos &= maskL]) != ((E) NULL_SLOT); pos++) {
            if (behavior.equals(o, currEle)) return true;
        }
        return false;
    }

    public boolean add(E e) {
        final HashBehavior<E> behavior = this.hashBehavior;
        if (behavior.equals(e, ((E) NULL_SLOT))) {
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
        final E[] eleL = this.element;
        int pos = behavior.hashing(e);
        for (E currEle; (currEle = eleL[pos &= maskL]) != ((E) NULL_SLOT); pos++) {
            if (behavior.equals(e, currEle)) return false;
        }
        eleL[pos] = e;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return true;
    }

    public boolean removeElement(Object o) {
        final HashBehavior<E> behavior = this.hashBehavior;
        if (behavior.equals(o, ((E) NULL_SLOT))) {
            final boolean result = this.withNullEle;
            if (result) this.removeNull();
            return result;
        }

        final int maskL = this.posMask;
        final E[] eleL = this.element;
        E currEle;
        for (int pos = behavior.hashing(o); (currEle = eleL[pos &= maskL]) != ((E) NULL_SLOT); pos++) {
            if (behavior.equals(o, currEle)) {
                this.removeAndShift(eleL, pos);
                return true;
            }
        }
        return false;
    }

    public boolean remapping(E element, Function<? super E, ? extends E> remappingFunction) {
        final HashBehavior<E> behavior = this.hashBehavior;
        if (behavior.equals(element, ((E) NULL_SLOT))) {
            if (!this.withNullEle) return false;

            final E newEle = remappingFunction.apply(element);
            if (newEle == ((E) NULL_SLOT)) {
                this.removeNull();
                return true;
            }
            return !behavior.equals(newEle, element) && this.add(newEle);
        }

        final int maskL = this.posMask;
        final E[] eleL = this.element;
        E currEle;
        for (int pos = behavior.hashing(element); (currEle = eleL[pos &= maskL]) != ((E) NULL_SLOT); pos++) {
            if (behavior.equals(element, currEle)) {
                final E newEle = remappingFunction.apply(element);
                if (newEle != null && behavior.equals(newEle, element)) return false;
                this.removeAndShift(eleL, pos);
                if (newEle != null) this.add(newEle);
                return true;
            }
        }
        return false;
    }

    public void swap(Set<E> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ObjLinearProbingHashSet) {
            final ObjLinearProbingHashSet<E> src = (ObjLinearProbingHashSet<E>) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                final boolean currWithNull = this.withNullEle, srcWithNull = src.withNullEle;
                final int currCap = this.capacity, srcCap = src.capacity,
                        currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final E[] currEle = this.element, srcEle = src.element;

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
            final List<E> tmp = new ArrayList<>(source);
            source.clear();
            source.addAll(this);
            this.clear();
            this.addAll(tmp);
        } else {
            final List<E> tmp = new ArrayList<>(this);
            this.clear();
            this.addAll(source);
            source.clear();
            source.addAll(tmp);
        }
    }

    public void forEach(Consumer<? super E> action) {
        if (this.isEmpty()) return;

        final int capL = this.capacity;
        final E[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final E currEle;
            if ((currEle = eleL[pos++]) != ((E) NULL_SLOT)) {
                action.accept(currEle);
            }
        }

        if (this.withNullEle) action.accept(eleL[capL]);
    }

    public boolean addAll(Collection<? extends E> c) {
        if (c == this || c.isEmpty()) return false;
        if (c.size() >= this.remaining()) this.reserve(this.size + c.size() + 1);
        if (c instanceof ObjLinearProbingHashSet) {
            final ObjLinearProbingHashSet<E> cast = (ObjLinearProbingHashSet<E>) c;
            boolean result = false;
            final int capL = cast.capacity;
            final E[] eleL = cast.element;
            for (int pos = 0; pos < capL;) {
                final E currEle;
                if ((currEle = eleL[pos++]) != ((E) NULL_SLOT)) {
                    result |= this.add(currEle);
                }
            }

            if (cast.withNullEle) result |= this.add(eleL[capL]);
            return result;
        } else return super.addAll(c);
    }

    public boolean removeIf(Predicate<? super E> filter) {
        if (this.isEmpty()) return false;

        final int capL = this.capacity;
        final E[] eleL = this.element;
        boolean result = false;
        for (int pos = 0; pos < capL; pos++) {
            final E currEle;
            if ((currEle = eleL[pos]) != ((E) NULL_SLOT) && filter.test(currEle)) {
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
        return this.removeIf((E l_ele) -> !c.contains(l_ele));
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

    public HashBehavior<E> getBehavior() {
        return this.hashBehavior;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public Iterator<E> iterator() {
        return new SetIterator();
    }

    public Spliterator<E> spliterator() {
        return new SetSpliterator(0, -1, 0);
    }

    public E[] table() {
        return this.element;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        Arrays.fill(this.element, ((E) NULL_SLOT));
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

    public int hashCode() {
        if (this.isEmpty()) return 0;

        int result = 0;
        final int capL = this.capacity;
        final E[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final E currEle;
            if ((currEle = eleL[pos++]) != ((E) NULL_SLOT)) {
                result += Objects.hashCode(currEle);
            }
        }

        if (this.withNullEle) result += Objects.hashCode(eleL[capL]);
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Set<?>) {
            final Set<?> castSet = (Set<?>) o;
            if (castSet.size() != this.size()) return false;
            try {
                if (castSet instanceof ObjLinearProbingHashSet<?>) {
                    final ObjLinearProbingHashSet<?> cast = (ObjLinearProbingHashSet<?>) castSet;
                    if (this.withNullEle != cast.withNullEle) return false;
                    final E[] srcEleL = (E[]) cast.element;

                    final int capacity = srcEleL.length;
                    for (int srcPos = 0; srcPos < capacity;) {
                        final E srcEle;
                        if ((srcEle = srcEleL[srcPos++]) != ((E) NULL_SLOT)) {
                            if (!this.contains(srcEle)) return false;
                        }
                    }

                    if (this.withNullEle) return Objects.equals(this.element[this.capacity], srcEleL[cast.capacity]);
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
        final E[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final E currEle;
            if ((currEle = eleL[pos++]) != ((E) NULL_SLOT)) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currEle != this ? currEle : "(this Collection)");
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
        ObjLinearProbingHashSet<E> result;
        try {
            result = (ObjLinearProbingHashSet<E>) super.clone();
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
        final E[] eleL = this.element;
        for (int pos = 0; pos < capL;) {
            final E currEle;
            if ((currEle = eleL[pos++]) != ((E) NULL_SLOT)) out.writeObject(currEle);
        }
        if (this.withNullEle) out.writeObject(eleL[capL]);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehavior);
        InternalCollectionUtil.SerialCheck.ioCheckHashLoadFactor(this.loadFactor, 0.2f, 0.9f);
        InternalCollectionUtil.SerialCheck.ioCheckSize(this.size, 0, HashContainer.MAXIMUM_CAPACITY);
        this.capacity = InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(this.size, this.loadFactor, HashContainer.MAXIMUM_CAPACITY);
        final int maskL = this.posMask = this.capacity - 1;
        final E[] eleL = this.element = this.newTable(this.capacity + 1);
        this.withNullEle = false;

        final HashBehavior<E> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final E currEle = (E) in.readObject();
            int pos;
            if (behavior.equals(currEle, ((E) NULL_SLOT))) {
                if (this.withNullEle) InternalCollectionUtil.SerialCheck.ioCheckElementIsDuplicate(currEle);
                pos = this.capacity;
                this.withNullEle = true;
            } else {
                pos = behavior.hashing(currEle) & maskL;
                E findEle;
                while ((findEle = eleL[pos]) != ((E) NULL_SLOT)) {
                    if (behavior.equals(currEle, findEle)) InternalCollectionUtil.SerialCheck.ioCheckElementIsDuplicate(currEle);
                    pos = (pos + 1) & maskL;
                }
            }
            eleL[pos] = currEle;
        }
        this.calculateRemaining();
    }

    protected class SetIterator implements Iterator<E> {
        protected int posPre = -1;
        protected int pos = -1;
        protected int posNext = -1;

        protected SetIterator() {
            if (ObjLinearProbingHashSet.this.size > 0) {
                this.scanPos(ObjLinearProbingHashSet.this.element, 0);
            }
        }

        protected void scanPos(final E[] eleL, int start) {
            final int capL = ObjLinearProbingHashSet.this.capacity;
            int findPos = -1, findPosNext = -1;
            for (int i = start; i < capL; i++) {
                if (eleL[i] != ((E) NULL_SLOT)) {
                    if (findPos < 0) findPos = i;
                    else if (findPosNext < 0) findPosNext = i;
                    else break;
                }
            }
            if (ObjLinearProbingHashSet.this.withNullEle) {
                if (findPos < 0) findPos = capL;
                else if (findPosNext < 0) findPosNext = capL;
            }
            this.pos = findPos;
            this.posNext = findPosNext;
        }

        public boolean hasNext() {
            return this.pos > -1;
        }

        public E next() {
            final int current;
            if ((current = this.pos) < 0) throw new NoSuchElementException();

            this.posPre = current;
            final int posL = this.pos = this.posNext;
            this.posNext = -1;

            final E[] eleL = ObjLinearProbingHashSet.this.element;
            if (posL > -1) {
                final int capL = ObjLinearProbingHashSet.this.capacity;
                int i;
                for (i = posL + 1; i < capL; i++) {
                    if (eleL[i] != ((E) NULL_SLOT)) {
                        this.posNext = i;
                        break;
                    }
                }
                if (i == capL && ObjLinearProbingHashSet.this.withNullEle) {
                    this.posNext = i;
                }
            }
            return eleL[current];
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();

            if (posPreL == ObjLinearProbingHashSet.this.capacity) {
                ObjLinearProbingHashSet.this.removeNull();
                this.pos = -1;
            } else {
                final E[] eleL = ObjLinearProbingHashSet.this.element;
                ObjLinearProbingHashSet.this.removeAndShift(eleL, posPreL);
                this.scanPos(eleL, posPreL);
            }
            this.posPre = -1;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final int current;
            if ((current = this.pos) < 0) return;
            final E[] eleL = ObjLinearProbingHashSet.this.element;
            final int capL = ObjLinearProbingHashSet.this.capacity;
            int i, findPre = this.posPre;
            for (i = current; i < capL; i++) {
                final E currEle;
                if ((currEle = eleL[i]) != ((E) NULL_SLOT)) {
                    findPre = i;
                    action.accept(currEle);
                }
            }
            if (i == capL && ObjLinearProbingHashSet.this.withNullEle) {
                findPre = i;
                action.accept(eleL[capL]);
            }
            this.posPre = findPre;
            this.pos = -1;
        }
    }

    protected class SetSpliterator implements Spliterator<E> {
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
                this.estSize = ObjLinearProbingHashSet.this.size;
                newLimit = this.limitCap = ObjLinearProbingHashSet.this.capacity;
            }
            return newLimit;
        }

        public Spliterator<E> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? new SetSpliterator(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            final E[] eleL = ObjLinearProbingHashSet.this.element;
            while (idxL < currLimit) {
                final E currEle;
                if ((currEle = eleL[idxL++]) != ((E) NULL_SLOT)) {
                    this.index = idxL;
                    action.accept(currEle);
                    return true;
                }
            }
            final int capL = ObjLinearProbingHashSet.this.capacity;
            if (idxL == currLimit && idxL == capL && ObjLinearProbingHashSet.this.withNullEle) {
                action.accept(eleL[capL]);
                this.index = capL + 1;
                return true;
            }
            this.index = idxL;
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final int currLimit = this.getLimitCap();

            int idxL = this.index;
            final E[] eleL = ObjLinearProbingHashSet.this.element;
            while (idxL < currLimit) {
                final E currEle;
                if ((currEle = eleL[idxL++]) != ((E) NULL_SLOT)) action.accept(currEle);
            }
            final int capL = ObjLinearProbingHashSet.this.capacity;
            if (idxL == currLimit && idxL == capL && ObjLinearProbingHashSet.this.withNullEle) {
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
            return (this.limitCap < 0 || this.estSize == ObjLinearProbingHashSet.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }
}
