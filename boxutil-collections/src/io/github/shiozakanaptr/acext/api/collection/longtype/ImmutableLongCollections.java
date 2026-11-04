package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import org.boxutil.base.api.container.*;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;
import io.github.shiozakanaptr.acext.util.collection.longtype.AbstractLongCollection;
import io.github.shiozakanaptr.acext.util.collection.longtype.LongLinearProbingHashSet;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjSet;
import io.github.shiozakanaptr.acext.util.function.longtype.LongUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
final class ImmutableLongCollections {
    final static float DEFAULT_LOAD_FACTOR = 0.75f;
    final static long NULL_SLOT = (long) 0;
    final static HashBehavior<Long> DEFAULT_BEHAVIOR = HashBehaviorFactory.fibonacciMix();
    private final static LongList EMPTY_LIST = new ImmutableLongListEmpty();
    private final static LongSet EMPTY_SET = new ImmutableLongSetEmpty();

    private static long[] newTable(int cap) {
        return new long[cap];
    }

    private static long[] newFillTable(long element) {
        return new long[]{element};
    }

    private static abstract class AbstractImmutableLongCollection extends AbstractLongCollection {
        public void resize(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public void reserve(int newMaxSize) {
            throw new UnsupportedOperationException();
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        public boolean add(long e) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(long o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(long[] elements) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(long[] elements, int srcIndex, int length) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeIf(LongPredicate filter) {
            throw new UnsupportedOperationException();
        }

        public int remaining() {
            return 0;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public void clear(int newCapacity) {
            throw new UnsupportedOperationException();
        }
    }

    private static abstract class AbstractImmutableLongList extends AbstractImmutableLongCollection implements LongList, RandomAccess {
        public long set(int index, long element) {
            throw new UnsupportedOperationException();
        }

        public void add(int index, long element) {
            throw new UnsupportedOperationException();
        }

        public void addFirst(long e) {
            throw new UnsupportedOperationException();
        }

        public void addLast(long e) {
            throw new UnsupportedOperationException();
        }

        public long removeAtRaw(int index) {
            throw new UnsupportedOperationException();
        }

        public boolean removeElement(long o) {
            throw new UnsupportedOperationException();
        }

        public long removeFirstRaw() {
            throw new UnsupportedOperationException();
        }

        public long removeLastRaw() {
            throw new UnsupportedOperationException();
        }

        public long update(int index, LongUnaryOperator updateFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(List<Long> source) {
            throw new UnsupportedOperationException();
        }

        public void swapAt(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        public void erase(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(LongUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        public void replace(int fromIndex, int toIndex, LongUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        public boolean compute(int fromIndex, int toIndex, LongFunction<? extends Long> operator) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(int index, Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(int dstIndex, long[] elements, int srcIndex, int length) {
            throw new UnsupportedOperationException();
        }

        public abstract LongIterator iterator();

        public abstract LongSpliterator spliterator();
    }

    private interface ImmutableLongIterator extends LongIterator {
        default void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private interface ImmutableLongListIterator extends LongListIterator, ImmutableLongIterator {
        default void remove() {
            throw new UnsupportedOperationException();
        }

        default void set(long e) {
            throw new UnsupportedOperationException();
        }

        default void add(long e) {
            throw new UnsupportedOperationException();
        }
    }

    private final static class ImmutableLongListEmpty extends AbstractImmutableLongList implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public long getRaw(int index) {
            throw new IndexOutOfBoundsException("Empty list");
        }

        public boolean contains(long o) {
            return false;
        }

        public int indexOf(long o) {
            return -1;
        }

        public int lastIndexOf(long o) {
            return -1;
        }

        public void forEach(LongConsumer action) {}

        public void forEach(int fromIndex, int toIndex, LongConsumer action) {
            if (fromIndex != 0 || toIndex != 0) throw new IndexOutOfBoundsException();
        }

        public boolean containsAll(@NotNull Collection<?> c) {
            return c.isEmpty();
        }

        public LongListIterator listIterator() {
            return new CollectionLongListIterator();
        }

        public LongListIterator listIterator(int index) {
            if (index != 0) throw new IndexOutOfBoundsException();
            return new CollectionLongListIterator();
        }

        public LongList subList(int fromIndex, int toIndex) {
            if (fromIndex != 0 || toIndex != 0) throw new IndexOutOfBoundsException();
            return this;
        }

        public int size() {
            return 0;
        }

        public LongIterator iterator() {
            return new CollectionLongIterator();
        }

        public LongSpliterator spliterator() {
            return new CollectionLongSpliterator();
        }

        public long[] toArrayRaw() {
            return newTable(0);
        }

        public long[] toArray(long[] a) {
            if (a.length > 1) a[0] = 0;
            return a;
        }

        public int hashCode() {
            return 1;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof List<?>) return ((List<?>) o).isEmpty();
            return false;
        }

        public String toString() {
            return "[]";
        }

        public Object clone() {
            return this;
        }

        private Object readResolve() {
            return EMPTY_LIST;
        }

        private static class CollectionLongIterator implements ImmutableLongIterator {
            public long nextLong() {
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                return false;
            }

            public void forEachRemaining(LongConsumer action) {}
        }

        private final static class CollectionLongListIterator extends CollectionLongIterator implements ImmutableLongListIterator {
            public int nextIndex() {
                return 0;
            }

            public boolean hasPrevious() {
                return false;
            }

            public int previousIndex() {
                return -1;
            }

            public long previousLong() {
                throw new NoSuchElementException();
            }
        }

        private final static class CollectionLongSpliterator implements LongSpliterator {
            public LongSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(LongConsumer action) {
                return false;
            }

            public void forEachRemaining(LongConsumer action) {}

            public long estimateSize() {
                return 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }
    }

    private final static class ImmutableLongListOne extends AbstractImmutableLongList implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient long element;

        public ImmutableLongListOne(Long element) {
            this.element = element != null ? element : 0;
        }

        public ImmutableLongListOne(long element) {
            this.element = element;
        }

        public long getRaw(int index) {
            if (index != 0) throw new IndexOutOfBoundsException("Empty list");
            return this.element;
        }

        public boolean contains(long o) {
            return o == this.element;
        }

        public int indexOf(long o) {
            return o != this.element ? -1 : 0;
        }

        public int lastIndexOf(long o) {
            return o != this.element ? -1 : 0;
        }

        public void forEach(LongConsumer action) {
            action.accept(this.element);
        }

        public void forEach(int fromIndex, int toIndex, LongConsumer action) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, 1);
            action.accept(this.element);
        }

        public boolean containsAll(@NotNull Collection<?> c) {
            return c.contains(this.element);
        }

        public LongListIterator listIterator() {
            return new CollectionLongListIterator(this.element, true);
        }

        public LongListIterator listIterator(int index) {
            if (index != 0 && index != 1) throw new IndexOutOfBoundsException();
            return new CollectionLongListIterator(this.element, index == 0);
        }

        public LongList subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, 1);
            return toIndex != 0 ? new ImmutableLongListOne(this.element) : (LongList) EMPTY_LIST;
        }

        public int capacity() {
            return 1;
        }

        public int size() {
            return 1;
        }

        public LongIterator iterator() {
            return new CollectionLongIterator(this.element);
        }

        public LongSpliterator spliterator() {
            return new CollectionLongSpliterator(this.element);
        }

        public long[] toArrayRaw() {
            return newFillTable(this.element);
        }

        public long[] toArray(long[] a) {
            if (a.length < 1) return this.toArrayRaw();
            a[0] = this.element;
            if (a.length > 2) a[1] = 0;
            return a;
        }

        public int hashCode() {
            return 31 + Long.hashCode(this.element);
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof List<?>) {
                final List<?> castList = ((List<?>) o);
                if (castList.size() != 1) return false;
                return Objects.equals(castList.get(0), this.element);
            }
            return false;
        }

        public String toString() {
            return '[' + Long.toString(this.element) + ']';
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageListOne(this.element);
        }

        private static class CollectionLongIterator implements ImmutableLongIterator {
            protected boolean onZero = true;
            private final long element;

            public CollectionLongIterator(long element) {
                this.element = element;
            }

            public long nextLong() {
                if (!this.onZero) throw new NoSuchElementException();
                this.onZero = false;
                return this.element;
            }

            public boolean hasNext() {
                return this.onZero;
            }

            public void forEachRemaining(LongConsumer action) {
                if (this.onZero) action.accept(this.element);
            }
        }

        private final static class CollectionLongListIterator extends CollectionLongIterator implements ImmutableLongListIterator {
            public CollectionLongListIterator(long element, boolean fromZero) {
                super(element);
                this.onZero = fromZero;
            }

            public int nextIndex() {
                return this.onZero ? 0 : 1;
            }

            public boolean hasPrevious() {
                return !this.onZero;
            }

            public int previousIndex() {
                return this.onZero ? -1 : 0;
            }

            public long previousLong() {
                if (this.onZero) throw new NoSuchElementException();
                this.onZero = true;
                return super.element;
            }
        }

        private final static class CollectionLongSpliterator implements LongSpliterator {
            private boolean toggle = true;
            private final long element;

            public CollectionLongSpliterator(long element) {
                this.element = element;
            }

            public LongSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(LongConsumer action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(LongConsumer action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                }
            }

            public long estimateSize() {
                return this.toggle ? 1 : 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }
    }

    private final static class ImmutableLongListN extends AbstractImmutableLongList implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient long[] element;

        public ImmutableLongListN(Long[] element) {
            final int limit = element.length;
            final long[] eleL = this.element = newTable(limit);
            for (int i = 0; i < limit; i++) {
                final Long currEle = element[i];
                eleL[i++] = currEle != null ? currEle : (long) 0;
            }
        }

        public ImmutableLongListN(long[] element) {
            this.element = element;
        }

        public long getRaw(int index) {
            return this.element[index];
        }

        public boolean contains(long o) {
            final long[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                if (eleL[i++] == o) return true;
            }
            return false;
        }

        public int indexOf(long o) {
            final long[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit; i++) {
                if (eleL[i] == o) return i;
            }
            return -1;
        }

        public int lastIndexOf(long o) {
            final long[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = limit - 1; i > -1; i--) {
                if (eleL[i] == o) return i;
            }
            return -1;
        }

        public void forEach(LongConsumer action) {
            final long[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                action.accept(eleL[i++]);
            }
        }

        public void forEach(int fromIndex, int toIndex, LongConsumer action) {
            for (int i = fromIndex; i < toIndex;) {
                action.accept(this.element[i++]);
            }
        }

        public boolean containsAll(Collection<?> c) {
            if (c == this) return true;
            if (c.isEmpty()) return false;
            return super.containsAll(c);
        }

        public LongListIterator listIterator() {
            return new CollectionLongListIterator(0);
        }

        public LongListIterator listIterator(int index) {
            InternalCollectionUtil.checkIndex(index, 0, this.element.length);
            return new CollectionLongListIterator(index);
        }

        public LongList subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.element.length);
            return new ImmutableLongSubList(fromIndex, toIndex);
        }

        public int capacity() {
            return this.element.length;
        }

        public int size() {
            return this.element.length;
        }

        public LongIterator iterator() {
            return new CollectionLongIterator();
        }

        public LongSpliterator spliterator() {
            return new CollectionLongSpliterator(0, -1);
        }

        public long[] toArrayRaw() {
            final long[] eleL;
            return Arrays.copyOf(eleL = this.element, eleL.length);
        }

        public long[] toArray(long[] a) {
            final long[] eleL;
            final int sizeL = (eleL = this.element).length;
            if (a.length < sizeL) return Arrays.copyOf(eleL, sizeL);
            System.arraycopy(eleL, 0, a, 0, sizeL);
            if (a.length > sizeL) a[sizeL] = 0;
            return a;
        }

        public int hashCode() {
            int result = 1;
            final long[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                result = 31 * result + Long.hashCode(eleL[i++]);
            }
            return result;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof List<?>) {
                final List<?> castList = ((List<?>) o);
                if (castList.isEmpty()) return false;
                if (castList.size() != this.size()) return false;
                try {
                    if (castList instanceof ImmutableLongListN) {
                        final ImmutableLongListN cast = (ImmutableLongListN) o;
                        return InternalCollectionUtil.arrayEquals(this.element, 0, this.element.length, cast.element, 0, cast.element.length);
                    } else if (castList instanceof ImmutableLongListN.ImmutableLongSubList) {
                        final ImmutableLongListN.ImmutableLongSubList cast = (ImmutableLongListN.ImmutableLongSubList) o;
                        return InternalCollectionUtil.arrayEquals(this.element, 0, this.element.length, cast.rootArray(), cast.offset, cast.size);
                    } else {
                        final Iterator<?> oit = castList.iterator();
                        for (long l : this.element) {
                            if (!oit.hasNext() || !Objects.equals(l, oit.next())) {
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
            final StringBuilder sb = new StringBuilder();
            sb.append('[');
            final long[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(eleL[i++]);
            }
            sb.append(']');
            return sb.toString();
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageListN(this.element);
        }

        private class CollectionLongIterator implements ImmutableLongIterator {
            protected int pos = 0;

            public boolean hasNext() {
                return this.pos < ImmutableLongListN.this.element.length;
            }

            public long nextLong() {
                if (this.pos >= ImmutableLongListN.this.element.length) throw new NoSuchElementException();
                return ImmutableLongListN.this.element[this.pos++];
            }

            public void forEachRemaining(LongConsumer action) {
                final long[] eleL = ImmutableLongListN.this.element;
                final int limit = eleL.length;
                for (int i = this.pos; i < limit;) {
                    action.accept(eleL[i++]);
                }
                this.pos = limit;
            }
        }

        private final class CollectionLongListIterator extends CollectionLongIterator implements ImmutableLongListIterator {
            public CollectionLongListIterator(int index) {
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
                return ImmutableLongListN.this.element[this.pos--];
            }
        }

        private final class CollectionLongSpliterator implements LongSpliterator {
            private int index;
            private int limit;

            public CollectionLongSpliterator(int index, int limit) {
                this.index = index;
                this.limit = limit;
            }

            private int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limit) < 0) {
                    newLimit = this.limit = ImmutableLongListN.this.element.length;
                }
                return newLimit;
            }

            public LongSpliterator trySplit() {
                final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                return newIndex < midIndex ? new CollectionLongSpliterator(newIndex, this.index = midIndex) : null;
            }

            public boolean tryAdvance(LongConsumer action) {
                final int currIndex;
                if ((currIndex = this.index) < this.getLimitCap()) {
                    action.accept(ImmutableLongListN.this.element[currIndex]);
                    this.index++;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(LongConsumer action) {
                final int currLimit = this.getLimitCap();
                for (int i = this.index; i < currLimit; i++) {
                    action.accept(ImmutableLongListN.this.element[i]);
                }
                this.index = currLimit;
            }

            public long estimateSize() {
                return this.getLimitCap() - this.index;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }

        protected class ImmutableLongSubList extends AbstractImmutableLongList implements RandomAccess {
            protected int size;
            protected final int limit;
            protected final int offset;

            protected ImmutableLongSubList(int fromIdx, int toIdx) {
                this.size = toIdx - fromIdx;
                this.limit = toIdx;
                this.offset = fromIdx;
            }

            protected ImmutableLongSubList(ImmutableLongSubList from, int fromIdx, int toIdx) {
                this.size = toIdx - fromIdx;
                this.limit = from.offset + toIdx;
                this.offset = from.offset + fromIdx;
            }

            protected long[] rootArray() {
                return ImmutableLongListN.this.element;
            }

            public long getRaw(int index) {
                return ImmutableLongListN.this.getRaw(index + this.offset);
            }

            public long set(int index, long element) {
                return ImmutableLongListN.this.set(index + this.offset, element);
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
                    if (eleL[i] == o) return i;
                }
                return -1;
            }

            public int lastIndexOf(long o) {
                final int limit = this.size + this.offset, min = this.offset - 1;
                final long[] eleL = this.rootArray();
                for (int i = limit - 1; i > min; i--) {
                    if (eleL[i] == o) return i;
                }
                return -1;
            }

            public void forEach(LongConsumer action) {
                final int limit = this.size + this.offset;
                final long[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
            }

            public void forEach(int fromIndex, int toIndex, LongConsumer action) {
                final int limit = toIndex + this.offset;
                final long[] eleL = this.rootArray();
                for (int i = fromIndex + this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
            }

            public boolean containsAll(Collection<?> c) {
                if (this.isEmpty()) return c.isEmpty();
                if (c == this) return true;
                return super.containsAll(c);
            }

            public LongListIterator listIterator() {
                return new CollectionLongSubListIterator(0);
            }

            public LongListIterator listIterator(int index) {
                return new CollectionLongSubListIterator(index);
            }

            public LongList subList(int fromIndex, int toIndex) {
                InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
                return new ImmutableLongSubList(this, fromIndex, toIndex);
            }

            public int capacity() {
                return -1;
            }

            public int size() {
                return this.size;
            }

            public boolean isEmpty() {
                return this.size < 1;
            }

            public LongIterator iterator() {
                return new CollectionLongSubIterator();
            }

            public LongSpliterator spliterator() {
                return new CollectionLongSubSpliterator(0, -1);
            }

            public long[] toArrayRaw() {
                return Arrays.copyOfRange(this.rootArray(), this.offset, this.offset + this.size);
            }

            public long[] toArray(long[] a) {
                final long[] eleL = this.rootArray();
                final int sizeL = this.size;
                if (a.length < sizeL) return Arrays.copyOfRange(eleL, this.offset, this.offset + sizeL);
                System.arraycopy(eleL, this.offset, a, 0, sizeL);
                if (a.length > sizeL) a[sizeL] = 0;
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
                    final List<?> castList = ((List<?>) o);
                    if (castList.isEmpty()) return this.isEmpty();
                    if (castList.size() != this.size()) return false;
                    try {
                        if (castList instanceof ImmutableLongListN) {
                            final ImmutableLongListN cast = (ImmutableLongListN) o;
                            return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.element, 0, cast.element.length);
                        } else if (castList instanceof ImmutableLongListN.ImmutableLongSubList) {
                            final ImmutableLongListN.ImmutableLongSubList cast = (ImmutableLongListN.ImmutableLongSubList) o;
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

            protected class CollectionLongSubIterator implements ImmutableLongIterator {
                protected int pos = 0;

                public boolean hasNext() {
                    return this.pos < ImmutableLongSubList.this.size;
                }

                public long nextLong() {
                    if (this.pos >= ImmutableLongSubList.this.size) throw new NoSuchElementException();
                    return ImmutableLongSubList.this.rootArray()[this.pos++ + ImmutableLongSubList.this.offset];
                }

                public void forEachRemaining(LongConsumer action) {
                    final long[] eleL = ImmutableLongSubList.this.rootArray();
                    final int limit = eleL.length;
                    for (int i = this.pos; i < limit;) {
                        action.accept(eleL[i++]);
                    }
                    this.pos = limit;
                }
            }

            protected class CollectionLongSubListIterator extends CollectionLongSubIterator implements ImmutableLongListIterator {
                protected CollectionLongSubListIterator(int index) {
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
                    return ImmutableLongSubList.this.rootArray()[this.pos-- + ImmutableLongSubList.this.offset];
                }
            }

            protected class CollectionLongSubSpliterator implements LongSpliterator {
                protected int index;
                protected int limit;

                protected CollectionLongSubSpliterator(int index, int limit) {
                    this.index = index;
                    this.limit = limit;
                }

                private int getLimitCap() {
                    int newLimit;
                    if ((newLimit = this.limit) < 0) {
                        newLimit = this.limit = ImmutableLongSubList.this.size;
                    }
                    return newLimit;
                }

                public LongSpliterator trySplit() {
                    final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                    return (newIndex >= midIndex) ? null : new CollectionLongSubSpliterator(newIndex, this.index = midIndex);
                }

                public boolean tryAdvance(LongConsumer action) {
                    final int currIndex;
                    if ((currIndex = this.index) < this.getLimitCap()) {
                        action.accept(ImmutableLongSubList.this.rootArray()[currIndex + ImmutableLongSubList.this.offset]);
                        this.index++;
                        return true;
                    }
                    return false;
                }

                public void forEachRemaining(LongConsumer action) {
                    final int rawLimit = this.getLimitCap(), currLimit = rawLimit + ImmutableLongSubList.this.offset;
                    final long[] eleL = ImmutableLongSubList.this.rootArray();
                    for (int i = this.index + ImmutableLongSubList.this.offset; i < currLimit; i++) {
                        action.accept(eleL[i]);
                    }
                    this.index = rawLimit;
                }

                public long estimateSize() {
                    return this.getLimitCap() - this.index;
                }

                public int characteristics() {
                    return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
                }
            }
        }
    }

    private static abstract class AbstractImmutableLongSet extends AbstractImmutableLongCollection implements LongSet {
        public boolean remapping(long element, LongFunction<? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(Set<Long> source) {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection<?> c) {
            Objects.requireNonNull(c);
            if (c == this) return true;
            if (this.isEmpty()) return c.isEmpty();
            if (c instanceof Set<?> && this.size() < c.size()) return false;

            for (final Object e : c) {
                if (!this.contains(e)) return false;
            }
            return true;
        }

        public abstract LongIterator iterator();

        public abstract LongSpliterator spliterator();
    }

    private static class ImmutableLongSetEmpty extends AbstractImmutableLongSet implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public boolean contains(long o) {
            return super.contains(o);
        }

        public void forEach(LongConsumer action) {}

        public boolean containsAll(Collection<?> c) {
            return c.isEmpty();
        }

        public int capacity() {
            return 2;
        }

        public boolean isEmpty() {
            return true;
        }

        public int size() {
            return 0;
        }

        public LongIterator iterator() {
            return new CollectionLongIterator();
        }

        public LongSpliterator spliterator() {
            return new CollectionLongSpliterator();
        }

        public long[] toArrayRaw() {
            return newTable(0);
        }

        public long[] toArray(long[] a) {
            if (a.length > 1) a[0] = 0;
            return a;
        }

        public int hashCode() {
            return 1;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof Set<?>) return ((Set<?>) o).isEmpty();
            return false;
        }

        public String toString() {
            return "[]";
        }

        public Object clone() {
            return this;
        }

        private Object readResolve() {
            return EMPTY_SET;
        }

        private static class CollectionLongIterator implements ImmutableLongIterator {
            public long nextLong() {
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                return false;
            }

            public void forEachRemaining(LongConsumer action) {}
        }

        private final static class CollectionLongSpliterator implements LongSpliterator {
            public LongSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(LongConsumer action) {
                return false;
            }

            public void forEachRemaining(LongConsumer action) {}

            public long estimateSize() {
                return 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }
    }

    private static class ImmutableLongSetOne extends AbstractImmutableLongSet implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient long element;

        public ImmutableLongSetOne(Long element) {
            this.element = element != null ? element : 0;
        }

        public ImmutableLongSetOne(long element) {
            this.element = element;
        }

        public boolean contains(long o) {
            return this.element == o;
        }

        public void forEach(LongConsumer action) {
            action.accept(this.element);
        }

        public boolean containsAll(Collection<?> c) {
            return c.contains(this.element);
        }

        public int capacity() {
            return 2;
        }

        public boolean isEmpty() {
            return false;
        }

        public int size() {
            return 1;
        }

        public LongIterator iterator() {
            return new CollectionLongIterator(this.element);
        }

        public LongSpliterator spliterator() {
            return new CollectionLongSpliterator(this.element);
        }

        public long[] toArrayRaw() {
            return newFillTable(this.element);
        }

        public long[] toArray(long[] a) {
            if (a.length < 1) return this.toArrayRaw();
            a[0] = this.element;
            if (a.length > 2) a[1] = 0;
            return a;
        }

        public int hashCode() {
            return 31 + Long.hashCode(this.element);
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof Set<?>) {
                final Set<?> castSet = ((Set<?>) o);
                if (castSet.size() != 1) return false;
                return castSet.contains(this.element);
            }
            return false;
        }

        public String toString() {
            return '[' + Long.toString(this.element) + ']';
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageSetOne(this.element);
        }

        static class CollectionLongIterator implements ImmutableLongIterator {
            private boolean onZero = true;
            private final long element;

            public CollectionLongIterator(long element) {
                this.element = element;
            }

            public long nextLong() {
                if (!this.onZero) throw new NoSuchElementException();
                this.onZero = false;
                return this.element;
            }

            public boolean hasNext() {
                return this.onZero;
            }

            public void forEachRemaining(LongConsumer action) {
                if (this.onZero) action.accept(this.element);
            }
        }

        static class CollectionLongSpliterator implements LongSpliterator {
            private boolean toggle = true;
            private final long element;

            public CollectionLongSpliterator(long element) {
                this.element = element;
            }

            public LongSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(LongConsumer action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(LongConsumer action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                }
            }

            public long estimateSize() {
                return this.toggle ? 1 : 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }
    }

    private static class ImmutableLongSetN extends AbstractImmutableLongSet implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient boolean withNullEle;
        private final transient int size;
        private final transient int capacity;
        private final transient int posMask;
        private final transient long[] element;

        public ImmutableLongSetN(boolean withNullEle, int size, int capacity, long[] element) {
            this.withNullEle = withNullEle;
            this.size = size;
            this.capacity = capacity;
            this.posMask = capacity - 1;
            this.element = element;
        }

        public boolean contains(long o) {
            if (DEFAULT_BEHAVIOR.equals(o, 0)) return this.withNullEle;

            long currEle;
            for (int pos = DEFAULT_BEHAVIOR.hashing(o); (currEle = this.element[pos &= this.posMask]) != ((long) NULL_SLOT); pos++) {
                if (DEFAULT_BEHAVIOR.equals(o, currEle)) return true;
            }
            return false;
        }

        public void forEach(LongConsumer action) {
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

        public int capacity() {
            return this.capacity;
        }

        public boolean isEmpty() {
            return false;
        }

        public int size() {
            return this.size;
        }

        public LongIterator iterator() {
            return new CollectionLongIterator();
        }

        public LongSpliterator spliterator() {
            return new CollectionLongSpliterator(0, -1, 0);
        }

        protected long[] prepareArray(final long[] a) {
            final int sizeL = this.withNullEle ? this.size + 1 : this.size;
            if (a.length < sizeL) return newTable(sizeL);
            if (a.length > sizeL) a[sizeL] = 0;
            return a;
        }

        protected long[] slotToArray(final long[] a) {
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
            return this.slotToArray(newTable(this.size));
        }

        public long[] toArray(long[] a) {
            return this.slotToArray(this.prepareArray(a));
        }

        public int hashCode() {
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
                final Set<?> castSet = ((Set<?>) o);
                if (castSet.size() != this.size) return false;
                try {
                    if (castSet instanceof ImmutableLongSetN) {
                        final ImmutableLongSetN cast = (ImmutableLongSetN) castSet;
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
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageSetN(this.toArrayRaw());
        }

        class CollectionLongIterator implements ImmutableLongIterator {
            protected int pos = -1;
            protected int posNext = -1;

            public CollectionLongIterator() {
                if (ImmutableLongSetN.this.size > 0) {
                    this.scanPos(ImmutableLongSetN.this.element, 0);
                }
            }

            protected void scanPos(final long[] eleL, int start) {
                final int capL = ImmutableLongSetN.this.capacity;
                int findPos = -1, findPosNext = -1;
                for (int i = start; i < capL; i++) {
                    if (eleL[i] != ((long) NULL_SLOT)) {
                        if (findPos < 0) findPos = i;
                        else if (findPosNext < 0) findPosNext = i;
                        else break;
                    }
                }
                if (ImmutableLongSetN.this.withNullEle) {
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

                final int posL = this.pos = this.posNext;
                this.posNext = -1;

                final long[] eleL = ImmutableLongSetN.this.element;
                if (posL > -1) {
                    final int capL = ImmutableLongSetN.this.capacity;
                    int i;
                    for (i = posL + 1; i < capL; i++) {
                        if (eleL[i] != ((long) NULL_SLOT)) {
                            this.posNext = i;
                            break;
                        }
                    }
                    if (i == capL && ImmutableLongSetN.this.withNullEle) {
                        this.posNext = i;
                    }
                }
                return eleL[current];
            }

            public void forEachRemaining(LongConsumer action) {
                final int current;
                if ((current = this.pos) < 0) return;
                final long[] eleL = ImmutableLongSetN.this.element;
                final int capL = ImmutableLongSetN.this.capacity;
                int i;
                for (i = current; i < capL; i++) {
                    final long currEle;
                    if ((currEle = eleL[i]) != ((long) NULL_SLOT)) {
                        action.accept(currEle);
                    }
                }
                if (i == capL && ImmutableLongSetN.this.withNullEle) {
                    action.accept(eleL[capL]);
                }
                this.pos = -1;
            }
        }

        class CollectionLongSpliterator implements LongSpliterator {
            protected int index;
            protected int limitCap;
            protected int estSize;

            protected CollectionLongSpliterator(int index, int limitCap, int estSize) {
                this.index = index;
                this.limitCap = limitCap;
                this.estSize = estSize;
            }

            protected final int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limitCap) < 0) {
                    this.estSize = ImmutableLongSetN.this.size;
                    newLimit = this.limitCap = ImmutableLongSetN.this.capacity;
                }
                return newLimit;
            }

            public LongSpliterator trySplit() {
                final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                return newIndex < midIndex ? new CollectionLongSpliterator(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
            }

            public boolean tryAdvance(LongConsumer action) {
                final int currLimit = this.getLimitCap();

                int idxL = this.index;
                final long[] eleL = ImmutableLongSetN.this.element;
                while (idxL < currLimit) {
                    final long currEle;
                    if ((currEle = eleL[idxL++]) != ((long) NULL_SLOT)) {
                        this.index = idxL;
                        action.accept(currEle);
                        return true;
                    }
                }
                final int capL = ImmutableLongSetN.this.capacity;
                if (idxL == currLimit && idxL == capL && ImmutableLongSetN.this.withNullEle) {
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
                final long[] eleL = ImmutableLongSetN.this.element;
                while (idxL < currLimit) {
                    final long currEle;
                    if ((currEle = eleL[idxL++]) != ((long) NULL_SLOT)) action.accept(currEle);
                }
                final int capL = ImmutableLongSetN.this.capacity;
                if (idxL == currLimit && idxL == capL && ImmutableLongSetN.this.withNullEle) {
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
                return (this.limitCap < 0 || this.estSize == ImmutableLongSetN.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }
    }

    static class ImmutableMapEntrySet<E> extends AbstractObjSet<E> implements EntrySetExt<E> {
        private final ObjSet<E> storage;

        public ImmutableMapEntrySet() {
            this.storage = ObjSet.of();
        }

        public ImmutableMapEntrySet(E entry) {
            this.storage = ObjSet.of(entry);
        }

        @SafeVarargs
        public ImmutableMapEntrySet(E... entries) {
            switch (entries.length) {
                case 0: {
                    this.storage = ObjSet.of();
                    break;
                }
                case 1: {
                    this.storage = ObjSet.of(entries[0]);
                    break;
                }
                default: {
                    this.storage = ObjSet.of(entries);
                }
            }
        }

        public void resize(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public void reserve(int newMaxSize) {
            throw new UnsupportedOperationException();
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object o) {
            return this.storage.contains(o);
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean remapping(E element, Function<? super E, ? extends E> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(@NotNull Set<E> source) {
            throw new UnsupportedOperationException();
        }

        public void forEach(Consumer<? super E> action) {
            this.storage.forEach(action);
        }

        public void viewForEach(Consumer<? super E> action) {
            this.storage.forEach(action);
        }

        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        public <T extends E> boolean addAll(T[] elements) {
            throw new UnsupportedOperationException();
        }

        public <T extends E> boolean addAll(T[] elements, int srcIndex, int length) {
            throw new UnsupportedOperationException();
        }

        public boolean removeIf(Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public int capacity() {
            return this.storage.capacity();
        }

        public int remaining() {
            return 0;
        }

        public int size() {
            return this.storage.size();
        }

        public boolean isEmpty() {
            return this.storage.isEmpty();
        }

        public Iterator<E> iterator() {
            return this.storage.iterator();
        }

        public Spliterator<E> spliterator() {
            return this.storage.spliterator();
        }

        public Iterator<E> viewIterator() {
            return this.storage.iterator();
        }

        public Spliterator<E> viewSpliterator() {
            return this.storage.spliterator();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public void clear(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public int hashCode() {
            return this.storage.hashCode();
        }

        public boolean equals(Object o) {
            return this.storage.equals(o);
        }

        public String toString() {
            return this.storage.toString();
        }
    }

    private final static class ProxyStorageListOne implements Serializable {
        private static final long serialVersionUID = 1L;

        private final long element;

        public ProxyStorageListOne(long element) {
            this.element = element;
        }

        private Object readResolve() throws ObjectStreamException {
            return new ImmutableLongListOne(this.element);
        }
    }

    private final static class ProxyStorageSetOne implements Serializable {
        private static final long serialVersionUID = 1L;

        private final long element;

        public ProxyStorageSetOne(long element) {
            this.element = element;
        }

        private Object readResolve() throws ObjectStreamException {
            return new ImmutableLongSetOne(this.element);
        }
    }

    private final static class ProxyStorageListN implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient long[] element;

        public ProxyStorageListN(long[] element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();

            final long[] eleL = this.element;
            final int size = eleL.length;
            out.writeInt(size);
            for (int i = 0; i < size;) {
                out.writeLong(eleL[i++]);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();

            final int sizeL = in.readInt();
            InternalCollectionUtil.SerialCheck.ioCheckSize(sizeL, 2, Container.MAXIMUM_CAPACITY);

            final long[] eleL = this.element = newTable(sizeL);
            for (int i = 0; i < sizeL;) {
                eleL[i++] = in.readLong();
            }
        }

        private Object readResolve() throws ObjectStreamException {
            final long[] eleL;
            if ((eleL = this.element) == null) throw new StreamCorruptedException("Illegal array 'NULL'");
            return new ImmutableLongListN(eleL);
        }
    }

    private final static class ProxyStorageSetN implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient long[] element;

        public ProxyStorageSetN(long[] element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();

            final long[] eleL = this.element;
            final int size = eleL.length;
            out.writeInt(size);
            for (int i = 0; i < size;) {
                out.writeLong(eleL[i++]);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();

            final int sizeL = in.readInt();
            InternalCollectionUtil.SerialCheck.ioCheckSize(sizeL, 2, HashContainer.MAXIMUM_CAPACITY);
            InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(sizeL, DEFAULT_LOAD_FACTOR, HashContainer.MAXIMUM_CAPACITY);

            final long[] eleL = this.element = newTable(sizeL);
            for (int i = 0; i < sizeL;) {
                eleL[i++] = in.readLong();
            }
        }

        private Object readResolve() throws ObjectStreamException {
            final long[] eleL;
            if ((eleL = this.element) == null) throw new StreamCorruptedException("Illegal array 'NULL'");
            return ofSetSerial(eleL);
        }
    }

    static LongList ofList() {
        return (LongList) EMPTY_LIST;
    }

    static LongList ofList(long element) {
        return new ImmutableLongListOne(element);
    }

    static LongList ofList(long... elements) {
        switch (elements.length) {
            case 0: return (LongList) EMPTY_LIST;
            case 1: return new ImmutableLongListOne(elements[0]);
            default: return new ImmutableLongListN(elements);
        }
    }

    static LongList copyOfList(Collection<? extends Long> source) {
        if (source instanceof AbstractImmutableLongList) {
            return (LongList) source;
        }
        switch (source.size()) {
            case 0: return (LongList) EMPTY_LIST;
            case 1: return new ImmutableLongListOne((Long) source.toArray(new Long[0])[0]);
            default: return new ImmutableLongListN((Long[]) source.toArray(new Long[0]));
        }
    }

    static LongSet ofSet() {
        return (LongSet) EMPTY_SET;
    }

    static LongSet ofSet(long element) {
        return new ImmutableLongSetOne(element);
    }

    static LongSet ofSetSerial(long... elements) throws StreamCorruptedException {
        final LongLinearProbingHashSet collect = new LongLinearProbingHashSet(elements, DEFAULT_LOAD_FACTOR, (HashBehavior<Long>) DEFAULT_BEHAVIOR);
        collect.shrink();
        final boolean withNullEleL = collect.contains((long) 0);
        final int sizeL = collect.size(), capacityL = collect.capacity();
        final long[] eleL = collect.table();
        InternalCollectionUtil.SerialCheck.ioCheckElementCount(elements.length, sizeL);
        return new ImmutableLongSetN(withNullEleL, sizeL, capacityL, eleL);
    }

    static LongSet ofSet(long... elements) {
        switch (elements.length) {
            case 0: return (LongSet) EMPTY_SET;
            case 1: return new ImmutableLongSetOne(elements[0]);
        }

        final LongLinearProbingHashSet collect = new LongLinearProbingHashSet(elements, DEFAULT_LOAD_FACTOR, (HashBehavior<Long>) DEFAULT_BEHAVIOR);
        collect.shrink();
        final boolean withNullEleL = collect.contains((long) 0);
        final int sizeL = collect.size(), capacityL = collect.capacity();
        final long[] eleL = collect.table();

        switch (sizeL) {
            case 0: return (LongSet) EMPTY_SET;
            case 1: return new ImmutableLongSetOne(elements[0]);
            default: return new ImmutableLongSetN(withNullEleL, sizeL, capacityL, eleL);
        }
    }

    static LongSet copyOfSet(Collection<? extends Long> source) {
        if (source instanceof AbstractImmutableLongSet) {
            return (LongSet) source;
        }

        switch (source.size()) {
            case 0: return (LongSet) EMPTY_SET;
            case 1: return new ImmutableLongSetOne((Long) source.toArray(new Long[0])[0]);
            default: {
                final LongLinearProbingHashSet collect = new LongLinearProbingHashSet(source, DEFAULT_LOAD_FACTOR, (HashBehavior<Long>) DEFAULT_BEHAVIOR);
                collect.shrink();
                final boolean withNullEleL = collect.contains((long) 0);
                final int sizeL = collect.size(), capacityL = collect.capacity();
                final long[] eleL = collect.table();

                switch (sizeL) {
                    case 0: return (LongSet) EMPTY_SET;
                    case 1: return new ImmutableLongSetOne((Long) source.toArray(new Long[0])[0]);
                    default: return new ImmutableLongSetN(withNullEleL, sizeL, capacityL, eleL);
                }
            }
        }
    }

    private ImmutableLongCollections() {}
}
