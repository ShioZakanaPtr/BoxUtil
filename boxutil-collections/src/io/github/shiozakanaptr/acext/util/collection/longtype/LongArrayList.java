package io.github.shiozakanaptr.acext.util.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongList;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongListIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSpliterator;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.function.longtype.LongUnaryOperator;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
public class LongArrayList extends AbstractLongList implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;

    protected int size = 0;
    protected transient long[] element;

    protected long[] newTable(int cap) {
        return new long[cap];
    }

    /**
     * @param initialCapacity will directly use the value.
     */
    public LongArrayList(int initialCapacity) {
        InternalCollectionUtil.checkCapacity(initialCapacity);
        this.element = this.newTable(initialCapacity);
    }

    public LongArrayList() {
        this(Container.DEFAULT_INITIAL_CAPACITY);
    }

    public LongArrayList(final Collection<? extends Long> source) {
        this(source.size() + 1);
        this.addAll(source);
    }

    public LongArrayList(final long[] source) {
        this(source.length + 1);
        this.addAll(source);
    }

    public void resize(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        InternalCollectionUtil.checkCapacity(newCapacity);
        final long[] oldEle;
        final int newCap, sizeL;
        if ((newCap = Math.max(sizeL = this.size, newCapacity)) == (oldEle = this.element).length) return;

        final long[] newEle;
        System.arraycopy(oldEle, 0, newEle = this.newTable(newCap), 0, sizeL);
        this.element = newEle;
    }

    public void reserve(int newMaxSize) {
        if (newMaxSize < 0) throw new IllegalArgumentException("Not a valid size: '" + newMaxSize + '\'');
        this.resize(newMaxSize);
    }

    protected void checkExpand() {
        if (this.remaining() > 0) return;
        final int end;
        this.resize((end = this.element.length) == 0 ? Container.DEFAULT_INITIAL_CAPACITY : InternalCollectionUtil.expandArray(end));
    }

    protected void removeAndShift(final long[] eleL, int fromIndex, int toIndex, int length) {
        if (length == 0) return;
        System.arraycopy(eleL, toIndex, eleL, fromIndex, this.size - toIndex);
        this.size -= length;
    }

    public long getRaw(int index) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        return this.element[index];
    }

    public long set(int index, long element) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        final long[] eleL;
        final long oldValue = (eleL = this.element)[index];
        eleL[index] = element;
        return oldValue;
    }

    public boolean add(long e) {
        this.checkExpand();
        this.element[this.size++] = e;
        return true;
    }

    public void add(int index, long element) {
        final int sizeL;
        InternalCollectionUtil.checkIndex(index, 0, sizeL = this.size);
        this.checkExpand();
        final long[] eleL;
        System.arraycopy(eleL = this.element, index, eleL, index + 1, sizeL - index);
        eleL[index] = element;
        this.size++;
    }

    public long removeAtRaw(int index) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        final long[] eleL;
        final long oldValue = (eleL = this.element)[index];
        this.removeAndShift(eleL, index, index + 1, 1);
        return oldValue;
    }

    public boolean removeElement(long o) {
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit; i++) {
            if (eleL[i] == o) {
                this.removeAndShift(eleL, i, i + 1, 1);
                return true;
            }
        }
        return false;
    }

    public boolean contains(long o) {
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            if (eleL[i++] == o) return true;
        }
        return false;
    }

    public int indexOf(long o) {
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit; i++) {
            if (eleL[i] == o) return i;
        }
        return -1;
    }

    public int lastIndexOf(long o) {
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = limit - 1; i > -1; i--) {
            if (eleL[i] == o) return i;
        }
        return -1;
    }

    public long update(int index, LongUnaryOperator updateFunction) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        final long[] eleL = this.element;
        return eleL[index] = updateFunction.apply(eleL[index]);
    }

    public void swap(List<Long> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof LongArrayList) {
            final LongArrayList src = (LongArrayList) source;
            final int currSize = this.size, srcSize = src.size;
            final long[] currEle = this.element, srcEle = src.element;

            src.size = currSize;
            src.element = currEle;
            this.size = srcSize;
            this.element = srcEle;
        } else {
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
    }

    public void swapAt(int fromIndex, int toIndex) {
        final int sizeL;
        InternalCollectionUtil.checkBound(fromIndex, 0, sizeL = this.size);
        InternalCollectionUtil.checkBound(toIndex, 0, sizeL);
        final long[] eleL = this.element;
        final long tmpValue = eleL[fromIndex];
        eleL[fromIndex] = eleL[toIndex];
        eleL[toIndex] = tmpValue;
    }

    public void erase(int fromIndex, int toIndex) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        this.removeAndShift(this.element, fromIndex, toIndex, toIndex - fromIndex);
    }

    public void forEach(LongConsumer action) {
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            action.accept(eleL[i++]);
        }
    }

    public void forEach(int fromIndex, int toIndex, LongConsumer action) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        final long[] eleL = this.element;
        for (int i = fromIndex; i < toIndex;) {
            action.accept(eleL[i++]);
        }
    }

    public void replaceAll(LongUnaryOperator operator) {
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit; i++) {
            eleL[i] = operator.apply(eleL[i]);
        }
    }

    public void replace(int fromIndex, int toIndex, LongUnaryOperator operator) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        final long[] eleL = this.element;
        for (int i = fromIndex; i < toIndex; i++) {
            eleL[i] = operator.apply(eleL[i]);
        }
    }

    protected int in_computeRange(int fromIndex, int toIndex, LongFunction<? extends Long> operator) {
        if (fromIndex == toIndex) return 0;

        int idx = fromIndex;
        final long[] eleL = this.element;
        for (int i = fromIndex; i < toIndex;) {
            final Long newValue = operator.apply(eleL[i++]);
            if (newValue != null) eleL[idx++] = newValue;
        }
        final int result;
        this.removeAndShift(this.element, idx, toIndex, result = toIndex - idx);
        return result;
    }

    public boolean compute(int fromIndex, int toIndex, LongFunction<? extends Long> operator) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        return this.in_computeRange(fromIndex, toIndex, operator) != 0;
    }

    public boolean addAll(Collection<? extends Long> c) {
        return this.addAll(this.size, c);
    }

    public boolean addAll(int index, Collection<? extends Long> c) {
        final int sizeL;
        InternalCollectionUtil.checkIndex(index, 0, sizeL = this.size);
        final int length = c.size();
        if (length < 1) return false;
        if (length > this.remaining()) this.resize(sizeL + length + 1);
        if (c instanceof LongArrayList) {
            final LongArrayList cast = (LongArrayList) c;
            return this.addAll(index, cast.element, 0, length);
        } else if (c instanceof LongArrayList.LongSubArrayList) {
            final LongArrayList.LongSubArrayList cast = (LongArrayList.LongSubArrayList) c;
            return this.addAll(index, cast.rootArray(), cast.offset, length);
        }

        final long[] eleL = this.element;
        System.arraycopy(eleL, index, eleL, index + length, sizeL - index);

        int idx = index;
        for (final Long value : c) {
            eleL[idx++] = value != null ? value : 0;
        }
        this.size += length;
        return true;
    }

    public boolean addAll(long[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    public boolean addAll(long[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    public boolean addAll(int dstIndex, long[] elements, int srcIndex, int length) {
        final int sizeL;
        InternalCollectionUtil.checkIndex(dstIndex, 0, sizeL = this.size);
        if (length == 0) return false;
        if (length > this.remaining()) this.resize(sizeL + length + 1);
        if (elements == this.element) {
            elements = Arrays.copyOfRange(elements, srcIndex, srcIndex + length);
            srcIndex = 0;
        }
        final long[] eleL = this.element;
        System.arraycopy(eleL, dstIndex, eleL, dstIndex + length, sizeL - dstIndex);
        System.arraycopy(elements, srcIndex, eleL, dstIndex, length);
        this.size += length;
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();
        return super.containsAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        if (this.isEmpty()) return false;
        if (c == this) {
            this.size = 0;
            return true;
        }

        int idx = 0;
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            final long currEle;
            if (!c.contains(currEle = eleL[i++])) eleL[idx++] = currEle;
        }
        final boolean result = idx != limit;
        this.size = idx;
        return result;
    }

    public boolean retainAll(Collection<?> c) {
        if (c == this || this.isEmpty()) return false;

        int idx = 0;
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            final long currEle;
            if (c.contains(currEle = eleL[i++])) eleL[idx++] = currEle;
        }
        final boolean result = idx != limit;
        this.size = idx;
        return result;
    }

    public boolean removeIf(LongPredicate filter) {
        if (this.isEmpty()) return false;

        int idx = 0;
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            final long currEle;
            if (!filter.test(currEle = eleL[i++])) eleL[idx++] = currEle;
        }
        final boolean result = idx != limit;
        this.size = idx;
        return result;
    }

    public LongListIterator listIterator() {
        return new CollectionListIterator(0);
    }

    public LongListIterator listIterator(int index) {
        InternalCollectionUtil.checkIndex(index, 0, this.size);
        return new CollectionListIterator(index);
    }

    public LongList subList(int fromIndex, int toIndex) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        return new LongSubArrayList(fromIndex, toIndex);
    }

    public int capacity() {
        return this.element.length;
    }

    public int remaining() {
        return this.element.length - this.size;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public LongIterator iterator() {
        return new CollectionIterator();
    }

    public LongSpliterator spliterator() {
        return new CollectionSpliterator(0, -1);
    }

    public long[] table() {
        return this.element;
    }

    public void clear() {
        if (this.isEmpty()) return;
        Arrays.fill(this.element, 0, this.size, (long) 0);
        this.size = 0;
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        if (newCapacity == this.capacity()) {
            this.clear();
        } else {
            this.size = 0;
            this.element = this.newTable(newCapacity);
        }
    }

    public long[] toArrayRaw() {
        return Arrays.copyOf(this.element, this.size);
    }

    public long[] toArray(long[] a) {
        final long[] eleL = this.element;
        final int sizeL = this.size;
        if (a.length < sizeL) return Arrays.copyOf(eleL, sizeL);
        System.arraycopy(eleL, 0, a, 0, sizeL);
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    public int hashCode() {
        int result = 1;
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            result = 31 * result + Long.hashCode(eleL[i++]);
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof List<?>) {
            final List<?> castList = (List<?>) o;
            if (castList.size() != this.size()) return false;
            try {
                if (castList instanceof LongArrayList) {
                    final LongArrayList cast = (LongArrayList) castList;
                    return InternalCollectionUtil.arrayEquals(this.element, 0, this.size, cast.element, 0, cast.size);
                } else if (castList instanceof LongArrayList.LongSubArrayList) {
                    final LongArrayList.LongSubArrayList cast = (LongArrayList.LongSubArrayList) castList;
                    return InternalCollectionUtil.arrayEquals(this.element, 0, this.size, cast.rootArray(), cast.offset, cast.size);
                } else {
                    final Iterator<?> oit = castList.iterator();
                    final int limit = this.size;
                    final long[] eleL = this.element;
                    for (int i = 0; i < limit; i++) {
                        if (!oit.hasNext() || !Objects.equals(eleL[i], oit.next())) {
                            return false;
                        }
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
        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            if (sb.length() > 1) sb.append(',').append(' ');
            sb.append(eleL[i++]);
        }
        sb.append(']');
        return sb.toString();
    }

    public Object clone() {
        LongArrayList result;
        try {
            result = (LongArrayList) super.clone();
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

        final int limit = this.size;
        final long[] eleL = this.element;
        for (int i = 0; i < limit;) {
            out.writeLong(eleL[i++]);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        final int limit = this.size;
        InternalCollectionUtil.SerialCheck.ioCheckSize(limit, 0, Container.MAXIMUM_CAPACITY);
        final long[] eleL = this.element = this.newTable(limit);
        for (int i = 0; i < limit;) {
            eleL[i++] = in.readLong();
        }
    }

    protected class CollectionIterator implements LongIterator {
        protected int posPre = -1;
        protected int pos = 0;

        public boolean hasNext() {
            return this.pos < LongArrayList.this.size;
        }

        public long nextLong() {
            if (this.pos >= LongArrayList.this.size) throw new NoSuchElementException();
            final int currPos = this.pos++;
            return LongArrayList.this.element[this.posPre = currPos];
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();
            LongArrayList.this.removeAtRaw(posPreL);
            this.pos = posPreL;
            this.posPre = -1;
        }

        public void forEachRemaining(LongConsumer action) {
            final int limit = LongArrayList.this.size;
            final long[] eleL = LongArrayList.this.element;
            for (int i = this.pos; i < limit;) {
                action.accept(eleL[i++]);
            }
            this.posPre = -1;
            this.pos = limit;
        }
    }

    protected class CollectionListIterator extends CollectionIterator implements LongListIterator {
        protected CollectionListIterator(int index) {
            super();
            this.pos = index;
        }

        public int nextIndex() {
            return this.pos;
        }

        public boolean hasPrevious() {
            return this.pos > 0;
        }

        public int previousIndex() {
            return this.pos - 1;
        }

        public long previousLong() {
            if (this.pos < 1) throw new NoSuchElementException();
            final int currPos = --this.pos;
            return LongArrayList.this.element[this.posPre = currPos];
        }

        public void set(long e) {
            LongArrayList.this.set(this.posPre, e);
        }

        public void add(long e) {
            LongArrayList.this.add(this.pos++, e);
            this.posPre = -1;
        }
    }

    protected class CollectionSpliterator implements LongSpliterator {
        protected int index;
        protected int limit;

        protected CollectionSpliterator(int index, int limit) {
            this.index = index;
            this.limit = limit;
        }

        private int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limit) < 0) {
                newLimit = this.limit = LongArrayList.this.size;
            }
            return newLimit;
        }

        public LongSpliterator trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? new CollectionSpliterator(newIndex, this.index = midIndex) : null;
        }

        public boolean tryAdvance(LongConsumer action) {
            final int currIndex;
            if ((currIndex = this.index) < this.getLimitCap()) {
                action.accept(LongArrayList.this.element[currIndex]);
                this.index++;
                return true;
            }
            return false;
        }

        public void forEachRemaining(LongConsumer action) {
            final int currLimit = this.getLimitCap();
            final long[] eleL = LongArrayList.this.element;
            for (int i = this.index; i < currLimit; i++) {
                action.accept(eleL[i]);
            }
            this.index = currLimit;
        }

        public long estimateSize() {
            return this.getLimitCap() - this.index;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL;
        }
    }

    protected class LongSubArrayList extends AbstractLongList implements RandomAccess {
        protected int size;
        protected final int limit;
        protected final int offset;
        protected final LongSubArrayList from;

        protected LongSubArrayList(int fromIdx, int toIdx) {
            this.size = toIdx - fromIdx;
            this.limit = toIdx;
            this.offset = fromIdx;
            this.from = null;
        }

        protected LongSubArrayList(LongSubArrayList from, int fromIdx, int toIdx) {
            this.size = toIdx - fromIdx;
            this.limit = from.offset + toIdx;
            this.offset = from.offset + fromIdx;
            this.from = from;
        }

        protected long[] rootArray() {
            return LongArrayList.this.element;
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        private void updateSize(int changed) {
            if (changed == 0) return;
            LongSubArrayList pre = this;
            do {
                pre.size += changed;
            } while ((pre = pre.from) != null);
        }

        public long getRaw(int index) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            return LongArrayList.this.getRaw(index + this.offset);
        }

        public long set(int index, long element) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            return LongArrayList.this.set(index + this.offset, element);
        }

        public boolean add(long e) {
            LongArrayList.this.add(this.size + this.offset, e);
            this.updateSize(1);
            return true;
        }

        public void add(int index, long element) {
            InternalCollectionUtil.checkIndex(index, 0, this.size);
            LongArrayList.this.add(index + this.offset, element);
            this.updateSize(1);
        }

        public long removeAtRaw(int index) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            final long result = LongArrayList.this.removeAtRaw(index + this.offset);
            this.updateSize(-1);
            return result;
        }

        public boolean removeElement(long o) {
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit; i++) {
                if (eleL[i] == o) {
                    LongArrayList.this.removeAndShift(eleL, i, i + 1, 1);
                    this.updateSize(-1);
                    return true;
                }
            }
            return false;
        }

        public boolean contains(long o) {
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                if (eleL[i++] == o) return true;
            }
            return false;
        }

        public int indexOf(long o) {
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit; i++) {
                if (eleL[i] == o) return i - this.offset;
            }
            return -1;
        }

        public int lastIndexOf(long o) {
            final int limit = this.size + this.offset, min = this.offset - 1;
            final long[] eleL = this.rootArray();
            for (int i = limit - 1; i > min; i--) {
                if (eleL[i] == o) return i - this.offset;
            }
            return -1;
        }

        public long update(int index, LongUnaryOperator updateFunction) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            return LongArrayList.this.update(index + this.offset, updateFunction);
        }

        public void swapAt(int fromIndex, int toIndex) {
            final int sizeL;
            InternalCollectionUtil.checkBound(fromIndex, 0, sizeL = this.size);
            InternalCollectionUtil.checkBound(toIndex, 0, sizeL);
            LongArrayList.this.swapAt(fromIndex + this.offset, toIndex + this.offset);
        }

        public void erase(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            LongArrayList.this.erase(fromIndex + this.offset, toIndex + this.offset);
            this.updateSize(fromIndex - toIndex);
        }

        public void forEach(LongConsumer action) {
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                action.accept(eleL[i++]);
            }
        }

        public void forEach(int fromIndex, int toIndex, LongConsumer action) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            final int limit = toIndex + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = fromIndex + this.offset; i < limit;) {
                action.accept(eleL[i++]);
            }
        }

        public void replaceAll(LongUnaryOperator operator) {
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit; i++) {
                eleL[i] = operator.apply(eleL[i]);
            }
        }

        public void replace(int fromIndex, int toIndex, LongUnaryOperator operator) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            final int limit = toIndex + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = fromIndex + this.offset; i < limit; i++) {
                eleL[i] = operator.apply(eleL[i]);
            }
        }

        public boolean compute(int fromIndex, int toIndex, LongFunction<? extends Long> operator) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            final int changed = LongArrayList.this.in_computeRange(fromIndex + this.offset, toIndex + this.offset, operator);
            this.updateSize(-changed);
            return changed != 0;
        }

        public boolean addAll(Collection<? extends Long> c) {
            return this.addAll(0, c);
        }

        public boolean addAll(int index, Collection<? extends Long> c) {
            InternalCollectionUtil.checkIndex(index, 0, this.size);
            final int size;
            if ((size = c.size()) < 1) return false;
            if (LongArrayList.this.addAll(index + this.offset, c)) {
                this.updateSize(size);
                return true;
            }
            return false;
        }

        public boolean addAll(long[] elements) {
            return this.addAll(elements, 0, elements.length);
        }

        public boolean addAll(long[] elements, int srcIndex, int length) {
            return this.addAll(0, elements, srcIndex, length);
        }

        public boolean addAll(int dstIndex, long[] elements, int srcIndex, int length) {
            InternalCollectionUtil.checkIndex(dstIndex, 0, this.size);
            if (LongArrayList.this.addAll(dstIndex + this.offset, elements, srcIndex, length)) {
                this.updateSize(length);
                return true;
            }
            return false;
        }

        public boolean containsAll(Collection<?> c) {
            if (this.isEmpty()) return c.isEmpty();
            if (c == this) return true;
            return super.containsAll(c);
        }

        public boolean removeAll(Collection<?> c) {
            if (this.isEmpty()) return false;
            if (c == this || c == LongArrayList.this) {
                final int oldSize = this.size;
                LongArrayList.this.removeAndShift(this.rootArray(), this.offset, this.size + this.offset, this.size);
                this.updateSize(-this.size);
                return oldSize != this.size;
            }

            int idx = this.offset;
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit; i++) {
                final long currEle;
                if (c.contains(currEle = eleL[i])) eleL[idx++] = currEle;
            }
            final boolean result = idx != limit;
            this.updateSize(idx - limit);
            return result;
        }

        public boolean retainAll(Collection<?> c) {
            if (c == this || c == LongArrayList.this || this.isEmpty()) return false;

            int idx = this.offset;
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                final long currEle;
                if (c.contains(currEle = eleL[i++])) eleL[idx++] = currEle;
            }
            final boolean result = idx != limit;
            this.updateSize(idx - limit);
            return result;
        }

        public boolean removeIf(LongPredicate filter) {
            if (this.isEmpty()) return false;

            int idx = this.offset;
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                final long currEle;
                if (!filter.test(currEle = eleL[i++])) eleL[idx++] = currEle;
            }
            final boolean result = idx != limit;
            this.updateSize(idx - limit);
            return result;
        }

        public LongListIterator listIterator() {
            return new CollectionSubListIterator(0);
        }

        public LongListIterator listIterator(int index) {
            InternalCollectionUtil.checkIndex(index, 0, this.size);
            return new CollectionSubListIterator(index);
        }

        public LongList subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            return new LongSubArrayList(this, fromIndex, toIndex);
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.size < 1;
        }

        public LongIterator iterator() {
            return new CollectionSubIterator();
        }

        public LongSpliterator spliterator() {
            return new CollectionSubSpliterator(0, -1);
        }

        public void clear() {
            if (this.isEmpty()) return;
            final int sizeL;
            Arrays.fill(this.rootArray(), this.offset, (sizeL = this.size) + this.offset, (long) 0);
            LongArrayList.this.size -= sizeL;
            this.updateSize(-sizeL);
        }

        public void clear(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public long[] toArrayRaw() {
            return Arrays.copyOfRange(this.rootArray(), this.offset, this.offset + this.size);
        }

        public long[] toArray(long[] a) {
            final long[] eleL = this.rootArray();
            if (a.length < this.size) return Arrays.copyOfRange(eleL, this.offset, this.offset + this.size);
            System.arraycopy(eleL, this.offset, a, 0, this.size);
            if (a.length > this.size) a[this.size] = 0;
            return a;
        }

        public int hashCode() {
            int result = 1;
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                result = 31 * result + Long.hashCode(eleL[i++]);
            }
            return result;
        }

        public boolean equals(Object o) {
            if (o == this) return true;

            if (o instanceof List<?>) {
                final List<?> castList = (List<?>) o;
                if (castList.isEmpty()) return this.isEmpty();
                if (castList.size() != this.size()) return false;
                try {
                    if (castList instanceof LongArrayList) {
                        final LongArrayList cast = (LongArrayList) castList;
                        return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.element, 0, cast.size);
                    } else if (castList instanceof LongArrayList.LongSubArrayList) {
                        final LongArrayList.LongSubArrayList cast = (LongArrayList.LongSubArrayList) castList;
                        return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.rootArray(), cast.offset, cast.size);
                    } else {
                        final Iterator<?> oit = castList.iterator();
                        final int limit = this.size;
                        final long[] eleL = this.rootArray();
                        for (int i = 0; i < limit; i++) {
                            if (!oit.hasNext() || !Objects.equals(eleL[i], oit.next())) {
                                return false;
                            }
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
            final int limit = this.size + this.offset;
            final long[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(eleL[i++]);
            }
            sb.append(']');
            return sb.toString();
        }

        protected class CollectionSubIterator implements LongIterator {
            protected int posPre = -1;
            protected int pos = 0;

            public boolean hasNext() {
                return this.pos < LongSubArrayList.this.size;
            }

            public long nextLong() {
                if (this.pos >= LongSubArrayList.this.size) throw new NoSuchElementException();
                final int currPos = this.pos++;
                return LongSubArrayList.this.rootArray()[(this.posPre = currPos) + LongSubArrayList.this.offset];
            }

            public void remove() {
                final int posPreL;
                if ((posPreL = this.posPre) < 0) throw new IllegalStateException();
                LongSubArrayList.this.removeAtRaw(posPreL);
                this.pos = posPreL;
                this.posPre = -1;
            }

            public void forEachRemaining(LongConsumer action) {
                final int rawLimit = LongSubArrayList.this.size, limit = rawLimit + LongSubArrayList.this.offset;
                final long[] eleL = LongSubArrayList.this.rootArray();
                for (int i = this.pos + LongSubArrayList.this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
                this.posPre = -1;
                this.pos = rawLimit;
            }
        }

        protected class CollectionSubListIterator extends CollectionSubIterator implements LongListIterator {
            protected CollectionSubListIterator(int index) {
                super();
                this.pos = index;
            }

            public int nextIndex() {
                return this.pos;
            }

            public boolean hasPrevious() {
                return this.pos > 0;
            }

            public int previousIndex() {
                return this.pos - 1;
            }

            public long previousLong() {
                if (this.pos < 1) throw new NoSuchElementException();
                final int currPos = --this.pos;
                return LongSubArrayList.this.rootArray()[(this.posPre = currPos) + LongSubArrayList.this.offset];
            }

            public void set(long e) {
                LongSubArrayList.this.set(this.posPre, e);
            }

            public void add(long e) {
                LongSubArrayList.this.add(this.pos++, e);
                this.posPre = -1;
            }
        }

        protected class CollectionSubSpliterator implements LongSpliterator {
            protected int index;
            protected int limit;

            protected CollectionSubSpliterator(int index, int limit) {
                this.index = index;
                this.limit = limit;
            }

            private int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limit) < 0) {
                    newLimit = this.limit = LongSubArrayList.this.size;
                }
                return newLimit;
            }

            public LongSpliterator trySplit() {
                final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                return newIndex < midIndex ? new CollectionSubSpliterator(newIndex, this.index = midIndex) : null;
            }

            public boolean tryAdvance(LongConsumer action) {
                final int currIndex;
                if ((currIndex = this.index) < this.getLimitCap()) {
                    action.accept(LongSubArrayList.this.rootArray()[currIndex + LongSubArrayList.this.offset]);
                    this.index++;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(LongConsumer action) {
                final int rawLimit = this.getLimitCap(), currLimit = rawLimit + LongSubArrayList.this.offset;
                final long[] eleL = LongSubArrayList.this.rootArray();
                for (int i = this.index + LongSubArrayList.this.offset; i < currLimit; i++) {
                    action.accept(eleL[i]);
                }
                this.index = rawLimit;
            }

            public long estimateSize() {
                return this.getLimitCap() - this.index;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
            }
        }
    }
}
