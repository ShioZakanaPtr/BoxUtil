package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.bytetype.AbstractByteCollection;
import io.github.shiozakanaptr.acext.util.collection.bytetype.ByteBitSet;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjSet;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
final class ImmutableByteCollections {
    final static long NULL_BIT = (long) 0;
    private final static ByteList EMPTY_LIST = new ImmutableByteListEmpty();
    private final static ByteSet EMPTY_SET = new ImmutableByteSetEmpty();

    private static byte[] newTable(int cap) {
        return new byte[cap];
    }

    private static byte[] newFillTable(byte element) {
        return new byte[]{element};
    }

    private static int computeSize(long[] bitL) {
        return Long.bitCount(bitL[0]) + Long.bitCount(bitL[1]) + Long.bitCount(bitL[2]) + Long.bitCount(bitL[3]);
    }

    private static abstract class AbstractImmutableByteCollection extends AbstractByteCollection {
        public void resize(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public void reserve(int newMaxSize) {
            throw new UnsupportedOperationException();
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        public boolean add(byte e) {
            throw new UnsupportedOperationException();
        }

        public boolean removeElement(byte o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(byte[] elements) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(byte[] elements, int srcIndex, int length) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeIf(BytePredicate filter) {
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

        public boolean isMutable() {
            return false;
        }
    }

    private static abstract class AbstractImmutableByteList extends AbstractImmutableByteCollection implements ByteList, RandomAccess {
        public byte set(int index, byte element) {
            throw new UnsupportedOperationException();
        }

        public void add(int index, byte element) {
            throw new UnsupportedOperationException();
        }

        public void addFirst(byte e) {
            throw new UnsupportedOperationException();
        }

        public void addLast(byte e) {
            throw new UnsupportedOperationException();
        }

        public byte removeAtRaw(int index) {
            throw new UnsupportedOperationException();
        }

        public byte removeFirstRaw() {
            throw new UnsupportedOperationException();
        }

        public byte removeLastRaw() {
            throw new UnsupportedOperationException();
        }

        public byte update(int index, ByteUnaryOperator updateFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(List<Byte> source) {
            throw new UnsupportedOperationException();
        }

        public void swapAt(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        public void erase(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(ByteUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        public void replace(int fromIndex, int toIndex, ByteUnaryOperator operator) {
            throw new UnsupportedOperationException();
        }

        public boolean compute(int fromIndex, int toIndex, ByteFunction<? extends Byte> operator) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(int index, Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(int dstIndex, byte[] elements, int srcIndex, int length) {
            throw new UnsupportedOperationException();
        }

        public abstract ByteIterator iterator();

        public abstract ByteSpliterator spliterator();
    }

    private interface ImmutableByteIterator extends ByteIterator {
        default void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private interface ImmutableByteListIterator extends ByteListIterator, ImmutableByteIterator {
        default void remove() {
            throw new UnsupportedOperationException();
        }

        default void set(byte e) {
            throw new UnsupportedOperationException();
        }

        default void add(byte e) {
            throw new UnsupportedOperationException();
        }
    }

    private final static class ImmutableByteListEmpty extends AbstractImmutableByteList implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public byte getRaw(int index) {
            throw new IndexOutOfBoundsException("Empty list");
        }

        public boolean contains(byte o) {
            return false;
        }

        public int indexOf(byte o) {
            return -1;
        }

        public int lastIndexOf(byte o) {
            return -1;
        }

        public void forEach(ByteConsumer action) {}

        public void forEach(int fromIndex, int toIndex, ByteConsumer action) {
            if (fromIndex != 0 || toIndex != 0) throw new IndexOutOfBoundsException();
        }

        public boolean containsAll(@NotNull Collection<?> c) {
            return c.isEmpty();
        }

        public ByteListIterator listIterator() {
            return new CollectionByteListIterator();
        }

        public ByteListIterator listIterator(int index) {
            if (index != 0) throw new IndexOutOfBoundsException();
            return new CollectionByteListIterator();
        }

        public ByteList subList(int fromIndex, int toIndex) {
            if (fromIndex != 0 || toIndex != 0) throw new IndexOutOfBoundsException();
            return this;
        }

        public int size() {
            return 0;
        }

        public ByteIterator iterator() {
            return new CollectionByteIterator();
        }

        public ByteSpliterator spliterator() {
            return new CollectionByteSpliterator();
        }

        public byte[] toArrayRaw() {
            return newTable(0);
        }

        public byte[] toArray(byte[] a) {
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

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageListEmpty();
        }

        private static class CollectionByteIterator implements ImmutableByteIterator {
            public byte nextByte() {
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {}
        }

        private final static class CollectionByteListIterator extends CollectionByteIterator implements ImmutableByteListIterator {
            public int nextIndex() {
                return 0;
            }

            public boolean hasPrevious() {
                return false;
            }

            public int previousIndex() {
                return -1;
            }

            public byte previousByte() {
                throw new NoSuchElementException();
            }
        }

        private final static class CollectionByteSpliterator implements ByteSpliterator {
            public ByteSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(ByteConsumer action) {
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {}

            public long estimateSize() {
                return 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }
    }

    private final static class ImmutableByteListOne extends AbstractImmutableByteList implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient byte element;

        public ImmutableByteListOne(Byte element) {
            this.element = element != null ? element : 0;
        }

        public ImmutableByteListOne(byte element) {
            this.element = element;
        }

        public byte getRaw(int index) {
            if (index != 0) throw new IndexOutOfBoundsException("Empty list");
            return this.element;
        }

        public boolean contains(byte o) {
            return o == this.element;
        }

        public int indexOf(byte o) {
            return o != this.element ? -1 : 0;
        }

        public int lastIndexOf(byte o) {
            return o != this.element ? -1 : 0;
        }

        public void forEach(ByteConsumer action) {
            action.accept(this.element);
        }

        public void forEach(int fromIndex, int toIndex, ByteConsumer action) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, 1);
            action.accept(this.element);
        }

        public boolean containsAll(@NotNull Collection<?> c) {
            return c.contains(this.element);
        }

        public ByteListIterator listIterator() {
            return new CollectionByteListIterator(this.element, true);
        }

        public ByteListIterator listIterator(int index) {
            if (index != 0 && index != 1) throw new IndexOutOfBoundsException();
            return new CollectionByteListIterator(this.element, index == 0);
        }

        public ByteList subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, 1);
            return toIndex != 0 ? new ImmutableByteListOne(this.element) : (ByteList) EMPTY_LIST;
        }

        public int capacity() {
            return 1;
        }

        public int size() {
            return 1;
        }

        public ByteIterator iterator() {
            return new CollectionByteIterator(this.element);
        }

        public ByteSpliterator spliterator() {
            return new CollectionByteSpliterator(this.element);
        }

        public byte[] toArrayRaw() {
            return newFillTable(this.element);
        }

        public byte[] toArray(byte[] a) {
            if (a.length < 1) return this.toArrayRaw();
            a[0] = this.element;
            if (a.length > 2) a[1] = 0;
            return a;
        }

        public int hashCode() {
            return 31 + Byte.hashCode(this.element);
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
            return '[' + Byte.toString(this.element) + ']';
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

        private static class CollectionByteIterator implements ImmutableByteIterator {
            protected boolean onZero = true;
            private final byte element;

            public CollectionByteIterator(byte element) {
                this.element = element;
            }

            public byte nextByte() {
                if (!this.onZero) throw new NoSuchElementException();
                this.onZero = false;
                return this.element;
            }

            public boolean hasNext() {
                return this.onZero;
            }

            public void forEachRemaining(ByteConsumer action) {
                if (this.onZero) action.accept(this.element);
            }
        }

        private final static class CollectionByteListIterator extends CollectionByteIterator implements ImmutableByteListIterator {
            public CollectionByteListIterator(byte element, boolean fromZero) {
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

            public byte previousByte() {
                if (this.onZero) throw new NoSuchElementException();
                this.onZero = true;
                return super.element;
            }
        }

        private final static class CollectionByteSpliterator implements ByteSpliterator {
            private boolean toggle = true;
            private final byte element;

            public CollectionByteSpliterator(byte element) {
                this.element = element;
            }

            public ByteSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(ByteConsumer action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {
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

    private final static class ImmutableByteListN extends AbstractImmutableByteList implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient byte[] element;

        public ImmutableByteListN(Byte[] element) {
            final int limit = element.length;
            final byte[] eleL = this.element = newTable(limit);
            for (int i = 0; i < limit; i++) {
                final Byte currEle = element[i];
                eleL[i++] = currEle != null ? currEle : (byte) 0;
            }
        }

        public ImmutableByteListN(byte[] element) {
            this.element = element;
        }

        public byte getRaw(int index) {
            return this.element[index];
        }

        public boolean contains(byte o) {
            final byte[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                if (eleL[i++] == o) return true;
            }
            return false;
        }

        public int indexOf(byte o) {
            final byte[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit; i++) {
                if (eleL[i] == o) return i;
            }
            return -1;
        }

        public int lastIndexOf(byte o) {
            final byte[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = limit - 1; i > -1; i--) {
                if (eleL[i] == o) return i;
            }
            return -1;
        }

        public void forEach(ByteConsumer action) {
            final byte[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                action.accept(eleL[i++]);
            }
        }

        public void forEach(int fromIndex, int toIndex, ByteConsumer action) {
            for (int i = fromIndex; i < toIndex;) {
                action.accept(this.element[i++]);
            }
        }

        public boolean containsAll(Collection<?> c) {
            if (c == this) return true;
            if (c.isEmpty()) return false;
            return super.containsAll(c);
        }

        public ByteListIterator listIterator() {
            return new CollectionByteListIterator(0);
        }

        public ByteListIterator listIterator(int index) {
            InternalCollectionUtil.checkIndex(index, 0, this.element.length);
            return new CollectionByteListIterator(index);
        }

        public ByteList subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.element.length);
            return new ImmutableByteSubList(fromIndex, toIndex);
        }

        public int capacity() {
            return this.element.length;
        }

        public int size() {
            return this.element.length;
        }

        public ByteIterator iterator() {
            return new CollectionByteIterator();
        }

        public ByteSpliterator spliterator() {
            return new CollectionByteSpliterator(0, -1);
        }

        public byte[] toArrayRaw() {
            final byte[] eleL;
            return Arrays.copyOf(eleL = this.element, eleL.length);
        }

        public byte[] toArray(byte[] a) {
            final byte[] eleL;
            final int sizeL = (eleL = this.element).length;
            if (a.length < sizeL) return Arrays.copyOf(eleL, sizeL);
            System.arraycopy(eleL, 0, a, 0, sizeL);
            if (a.length > sizeL) a[sizeL] = 0;
            return a;
        }

        public int hashCode() {
            int result = 1;
            final byte[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                result = 31 * result + Byte.hashCode(eleL[i++]);
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
                    if (castList instanceof ImmutableByteListN) {
                        final ImmutableByteListN cast = (ImmutableByteListN) o;
                        return InternalCollectionUtil.arrayEquals(this.element, 0, this.element.length, cast.element, 0, cast.element.length);
                    } else if (castList instanceof ImmutableByteListN.ImmutableByteSubList) {
                        final ImmutableByteListN.ImmutableByteSubList cast = (ImmutableByteListN.ImmutableByteSubList) o;
                        return InternalCollectionUtil.arrayEquals(this.element, 0, this.element.length, cast.rootArray(), cast.offset, cast.size);
                    } else {
                        final Iterator<?> oit = castList.iterator();
                        for (byte l : this.element) {
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
            final byte[] eleL = this.element;
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

        private class CollectionByteIterator implements ImmutableByteIterator {
            protected int pos = 0;

            public boolean hasNext() {
                return this.pos < ImmutableByteListN.this.element.length;
            }

            public byte nextByte() {
                if (this.pos >= ImmutableByteListN.this.element.length) throw new NoSuchElementException();
                return ImmutableByteListN.this.element[this.pos++];
            }

            public void forEachRemaining(ByteConsumer action) {
                final byte[] eleL = ImmutableByteListN.this.element;
                final int limit = eleL.length;
                for (int i = this.pos; i < limit;) {
                    action.accept(eleL[i++]);
                }
                this.pos = limit;
            }
        }

        private final class CollectionByteListIterator extends CollectionByteIterator implements ImmutableByteListIterator {
            public CollectionByteListIterator(int index) {
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

            public byte previousByte() {
                if (this.pos < 1) throw new NoSuchElementException();
                return ImmutableByteListN.this.element[this.pos--];
            }
        }

        private final class CollectionByteSpliterator implements ByteSpliterator {
            private int index;
            private int limit;

            public CollectionByteSpliterator(int index, int limit) {
                this.index = index;
                this.limit = limit;
            }

            private int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limit) < 0) {
                    newLimit = this.limit = ImmutableByteListN.this.element.length;
                }
                return newLimit;
            }

            public ByteSpliterator trySplit() {
                final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                return newIndex < midIndex ? new CollectionByteSpliterator(newIndex, this.index = midIndex) : null;
            }

            public boolean tryAdvance(ByteConsumer action) {
                final int currIndex;
                if ((currIndex = this.index) < this.getLimitCap()) {
                    action.accept(ImmutableByteListN.this.element[currIndex]);
                    this.index++;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {
                final int currLimit = this.getLimitCap();
                for (int i = this.index; i < currLimit; i++) {
                    action.accept(ImmutableByteListN.this.element[i]);
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

        protected class ImmutableByteSubList extends AbstractImmutableByteList implements RandomAccess {
            protected int size;
            protected final int limit;
            protected final int offset;

            protected ImmutableByteSubList(int fromIdx, int toIdx) {
                this.size = toIdx - fromIdx;
                this.limit = toIdx;
                this.offset = fromIdx;
            }

            protected ImmutableByteSubList(ImmutableByteSubList from, int fromIdx, int toIdx) {
                this.size = toIdx - fromIdx;
                this.limit = from.offset + toIdx;
                this.offset = from.offset + fromIdx;
            }

            protected byte[] rootArray() {
                return ImmutableByteListN.this.element;
            }

            public byte getRaw(int index) {
                InternalCollectionUtil.checkBound(index, 0, this.size);
                return ImmutableByteListN.this.getRaw(index + this.offset);
            }

            public boolean contains(byte o) {
                final int limit = this.size + this.offset;
                final byte[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    if (eleL[i++] == o) return true;
                }
                return false;
            }

            public int indexOf(byte o) {
                final int limit = this.size + this.offset;
                final byte[] eleL = this.rootArray();
                for (int i = this.offset; i < limit; i++) {
                    if (eleL[i] == o) return i;
                }
                return -1;
            }

            public int lastIndexOf(byte o) {
                final int limit = this.size + this.offset, min = this.offset - 1;
                final byte[] eleL = this.rootArray();
                for (int i = limit - 1; i > min; i--) {
                    if (eleL[i] == o) return i;
                }
                return -1;
            }

            public void forEach(ByteConsumer action) {
                final int limit = this.size + this.offset;
                final byte[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
            }

            public void forEach(int fromIndex, int toIndex, ByteConsumer action) {
                InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
                final int limit = toIndex + this.offset;
                final byte[] eleL = this.rootArray();
                for (int i = fromIndex + this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
            }

            public boolean containsAll(Collection<?> c) {
                if (this.isEmpty()) return c.isEmpty();
                if (c == this) return true;
                return super.containsAll(c);
            }

            public ByteListIterator listIterator() {
                return new CollectionByteSubListIterator(0);
            }

            public ByteListIterator listIterator(int index) {
                InternalCollectionUtil.checkIndex(index, 0, this.size);
                return new CollectionByteSubListIterator(index);
            }

            public ByteList subList(int fromIndex, int toIndex) {
                InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
                return new ImmutableByteSubList(this, fromIndex, toIndex);
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

            public ByteIterator iterator() {
                return new CollectionByteSubIterator();
            }

            public ByteSpliterator spliterator() {
                return new CollectionByteSubSpliterator(0, -1);
            }

            public byte[] toArrayRaw() {
                return Arrays.copyOfRange(this.rootArray(), this.offset, this.offset + this.size);
            }

            public byte[] toArray(byte[] a) {
                final byte[] eleL = this.rootArray();
                final int sizeL = this.size;
                if (a.length < sizeL) return Arrays.copyOfRange(eleL, this.offset, this.offset + sizeL);
                System.arraycopy(eleL, this.offset, a, 0, sizeL);
                if (a.length > sizeL) a[sizeL] = 0;
                return a;
            }

            public int hashCode() {
                int result = 1;
                final int limit = this.size + this.offset;
                final byte[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    result = 31 * result + Byte.hashCode(eleL[i++]);
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
                        if (castList instanceof ImmutableByteListN) {
                            final ImmutableByteListN cast = (ImmutableByteListN) o;
                            return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.element, 0, cast.element.length);
                        } else if (castList instanceof ImmutableByteListN.ImmutableByteSubList) {
                            final ImmutableByteListN.ImmutableByteSubList cast = (ImmutableByteListN.ImmutableByteSubList) o;
                            return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.rootArray(), cast.offset, cast.size);
                        } else {
                            final Iterator<?> oit = castList.iterator();
                            final int limit = this.size;
                            final byte[] eleL = this.rootArray();
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
                final byte[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    if (sb.length() > 1) sb.append(',').append(' ');
                    sb.append(eleL[i++]);
                }
                sb.append(']');
                return sb.toString();
            }

            protected class CollectionByteSubIterator implements ImmutableByteIterator {
                protected int pos = 0;

                public boolean hasNext() {
                    return this.pos < ImmutableByteSubList.this.size;
                }

                public byte nextByte() {
                    if (this.pos >= ImmutableByteSubList.this.size) throw new NoSuchElementException();
                    return ImmutableByteSubList.this.rootArray()[ImmutableByteSubList.this.offset + this.pos++];
                }

                public void forEachRemaining(ByteConsumer action) {
                    final int rawLimit = ImmutableByteSubList.this.size, limit = rawLimit + ImmutableByteSubList.this.offset;
                    final byte[] eleL = ImmutableByteSubList.this.rootArray();
                    for (int i = this.pos + ImmutableByteSubList.this.offset; i < limit;) {
                        action.accept(eleL[i++]);
                    }
                    this.pos = rawLimit;
                }
            }

            protected class CollectionByteSubListIterator extends CollectionByteSubIterator implements ImmutableByteListIterator {
                protected CollectionByteSubListIterator(int index) {
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

                public byte previousByte() {
                    if (this.pos < 1) throw new NoSuchElementException();
                    return ImmutableByteSubList.this.rootArray()[this.pos-- + ImmutableByteSubList.this.offset];
                }
            }

            protected class CollectionByteSubSpliterator implements ByteSpliterator {
                protected int index;
                protected int limit;

                protected CollectionByteSubSpliterator(int index, int limit) {
                    this.index = index;
                    this.limit = limit;
                }

                private int getLimitCap() {
                    int newLimit;
                    if ((newLimit = this.limit) < 0) {
                        newLimit = this.limit = ImmutableByteSubList.this.size;
                    }
                    return newLimit;
                }

                public ByteSpliterator trySplit() {
                    final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                    return (newIndex >= midIndex) ? null : new CollectionByteSubSpliterator(newIndex, this.index = midIndex);
                }

                public boolean tryAdvance(ByteConsumer action) {
                    final int currIndex;
                    if ((currIndex = this.index) < this.getLimitCap()) {
                        action.accept(ImmutableByteSubList.this.rootArray()[currIndex + ImmutableByteSubList.this.offset]);
                        this.index++;
                        return true;
                    }
                    return false;
                }

                public void forEachRemaining(ByteConsumer action) {
                    final int rawLimit = this.getLimitCap(), currLimit = rawLimit + ImmutableByteSubList.this.offset;
                    final byte[] eleL = ImmutableByteSubList.this.rootArray();
                    for (int i = this.index + ImmutableByteSubList.this.offset; i < currLimit; i++) {
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

    private static abstract class AbstractImmutableByteSet extends AbstractImmutableByteCollection implements ByteSet {
        public boolean remapping(byte element, ByteFunction<? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(Set<Byte> source) {
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

        public abstract ByteIterator iterator();

        public abstract ByteSpliterator spliterator();
    }

    private static class ImmutableByteSetEmpty extends AbstractImmutableByteSet implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public boolean contains(byte o) {
            return false;
        }

        public void forEach(ByteConsumer action) {}

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

        public ByteIterator iterator() {
            return new CollectionByteIterator();
        }

        public ByteSpliterator spliterator() {
            return new CollectionByteSpliterator();
        }

        public byte[] toArrayRaw() {
            return newTable(0);
        }

        public byte[] toArray(byte[] a) {
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

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageSetEmpty();
        }

        private static class CollectionByteIterator implements ImmutableByteIterator {
            public byte nextByte() {
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {}
        }

        private final static class CollectionByteSpliterator implements ByteSpliterator {
            public ByteSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(ByteConsumer action) {
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {}

            public long estimateSize() {
                return 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        }
    }

    private static class ImmutableByteSetOne extends AbstractImmutableByteSet implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient byte element;

        public ImmutableByteSetOne(Byte element) {
            this.element = element != null ? element : 0;
        }

        public ImmutableByteSetOne(byte element) {
            this.element = element;
        }

        public boolean contains(byte o) {
            return this.element == o;
        }

        public void forEach(ByteConsumer action) {
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

        public ByteIterator iterator() {
            return new CollectionByteIterator(this.element);
        }

        public ByteSpliterator spliterator() {
            return new CollectionByteSpliterator(this.element);
        }

        public byte[] toArrayRaw() {
            return newFillTable(this.element);
        }

        public byte[] toArray(byte[] a) {
            if (a.length < 1) return this.toArrayRaw();
            a[0] = this.element;
            if (a.length > 2) a[1] = 0;
            return a;
        }

        public int hashCode() {
            return 31 + Byte.hashCode(this.element);
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
            return '[' + Byte.toString(this.element) + ']';
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

        static class CollectionByteIterator implements ImmutableByteIterator {
            private boolean onZero = true;
            private final byte element;

            public CollectionByteIterator(byte element) {
                this.element = element;
            }

            public byte nextByte() {
                if (!this.onZero) throw new NoSuchElementException();
                this.onZero = false;
                return this.element;
            }

            public boolean hasNext() {
                return this.onZero;
            }

            public void forEachRemaining(ByteConsumer action) {
                if (this.onZero) action.accept(this.element);
            }
        }

        static class CollectionByteSpliterator implements ByteSpliterator {
            private boolean toggle = true;
            private final byte element;

            public CollectionByteSpliterator(byte element) {
                this.element = element;
            }

            public ByteSpliterator trySplit() {
                return null;
            }

            public boolean tryAdvance(ByteConsumer action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {
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

    static class ImmutableByteSetN extends AbstractImmutableByteSet implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient int size;
        private final transient long[] bitMark;

        public ImmutableByteSetN(long[] bitMark) {
            this.size = computeSize(this.bitMark = bitMark);
        }

        public ImmutableByteSetN(int size, long[] bitMark) {
            this.size = size;
            this.bitMark = bitMark;
        }

        public boolean contains(byte o) {
            final int seq;
            return (this.bitMark[(seq = o & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT;
        }

        public void forEach(ByteConsumer action) {
            for (int seq = 0; seq < 256; seq++) {
                if ((this.bitMark[seq >>> 6] & ( 1L << (seq & 0b111111))) != NULL_BIT) action.accept((byte) seq);
            }
        }

        public int capacity() {
            return 256;
        }

        public boolean isEmpty() {
            return false;
        }

        public int size() {
            return this.size;
        }

        public ByteIterator iterator() {
            return new CollectionByteIterator();
        }

        public ByteSpliterator spliterator() {
            return new CollectionByteSpliterator(0, -1, 0);
        }

        protected byte[] prepareArray(final byte[] a) {
            final int sizeL = this.size;
            if (a.length < sizeL) return newTable(sizeL);
            if (a.length > sizeL) a[sizeL] = 0;
            return a;
        }

        protected byte[] slotToArray(final byte[] a) {
            int idx = 0;
            for (int seq = 0; seq < 256; seq++) {
                final int picker = seq >>> 6;
                final long pos = 1L << (seq & 0b111111);
                if ((this.bitMark[picker] & pos) != NULL_BIT) a[idx++] = (byte) seq;
            }
            return a;
        }

        public byte[] toArrayRaw() {
            return this.slotToArray(newTable(this.size));
        }

        public byte[] toArray(byte[] a) {
            return this.slotToArray(this.prepareArray(a));
        }

        public int hashCode() {
            final long[] bitL = this.bitMark;
            int result = 31 + Long.hashCode(bitL[0]);
            result = 31 * result + Long.hashCode(bitL[1]);
            result = 31 * result + Long.hashCode(bitL[2]);
            return 31 * result + Long.hashCode(bitL[3]);
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof Set<?>) {
                final Set<?> castSet = ((Set<?>) o);
                if (castSet.size() != this.size) return false;
                try {
                    if (castSet instanceof ImmutableByteSetN) {
                        final ImmutableByteSetN cast = (ImmutableByteSetN) castSet;
                        final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;
                        return currBitL[0] == srcBitL[0] &&
                                currBitL[1] == srcBitL[1] &&
                                currBitL[2] == srcBitL[2] &&
                                currBitL[3] == srcBitL[3];
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
            final long[] bitL = this.bitMark;
            for (int seq = 0; seq < 256; seq++) {
                if ((bitL[seq >>> 6] & (1L << (seq & 0b111111))) != NULL_BIT) {
                    if (sb.length() > 1) sb.append(',').append(' ');
                    sb.append((byte) seq);
                }
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
            return new ProxyStorageSetN(this.bitMark);
        }

        class CollectionByteIterator implements ImmutableByteIterator {
            protected int seq = -1;
            protected int remaining;

            public CollectionByteIterator() {
                this.remaining = ImmutableByteSetN.this.size;
            }

            public boolean hasNext() {
                return this.remaining > 0;
            }

            public byte nextByte() {
                if (this.remaining < 1) throw new NoSuchElementException();
                this.remaining--;

                int seqL = this.seq + 1;
                while (seqL < 256 && (ImmutableByteSetN.this.bitMark[seqL >>> 6] & (1L << (seqL & 0b111111))) == NULL_BIT) {
                    seqL++;
                }
                return (byte) (this.seq = seqL);
            }

            public void forEachRemaining(ByteConsumer action) {
                for (int seqL = this.seq + 1; seqL < 256; seqL++) {
                    if ((ImmutableByteSetN.this.bitMark[seqL >>> 6] & ( 1L << (seqL & 0b111111))) != NULL_BIT) action.accept((byte) seqL);
                }
                this.remaining = 0;
                this.seq = 255;
            }
        }

        class CollectionByteSpliterator implements ByteSpliterator {
            protected int seq;
            protected int limitCap;
            protected int estSize;

            protected CollectionByteSpliterator(int seq, int limitCap, int estSize) {
                this.seq = seq;
                this.limitCap = limitCap;
                this.estSize = estSize;
            }

            protected final int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limitCap) < 0) {
                    this.estSize = ImmutableByteSetN.this.size;
                    newLimit = this.limitCap = 256;
                }
                return newLimit;
            }

            public ByteSpliterator trySplit() {
                final int newLimit = this.getLimitCap(), newSeq = this.seq, midSeq = (newSeq + newLimit) >>> 1;
                return newSeq < midSeq ? new CollectionByteSpliterator(newSeq, this.seq = midSeq, this.estSize >>>= 1) : null;
            }

            public boolean tryAdvance(ByteConsumer action) {
                final int currLimit = this.getLimitCap();

                int seqL = this.seq;
                for (; seqL < currLimit; seqL++) {
                    if ((ImmutableByteSetN.this.bitMark[seqL >>> 6] & (1L << (seqL & 0b111111))) != NULL_BIT) {
                        action.accept((byte) (this.seq = seqL + 1));
                        return true;
                    }
                }
                this.seq = seqL;
                return false;
            }

            public void forEachRemaining(ByteConsumer action) {
                final int currLimit = this.getLimitCap();

                int seqL = this.seq;
                for (; seqL < currLimit; seqL++) {
                    if ((ImmutableByteSetN.this.bitMark[seqL >>> 6] & (1L << (seqL & 0b111111))) != NULL_BIT) {
                        action.accept((byte) (this.seq = seqL));
                    }
                }
                this.seq = seqL;
            }

            public final long estimateSize() {
                this.getLimitCap();
                return this.estSize;
            }

            public int characteristics() {
                return (this.limitCap < 0 || this.estSize == ImmutableByteSetN.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL;
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

    private final static class ProxyStorageListEmpty implements Serializable {
        private static final long serialVersionUID = 1L;

        private void writeObject(ObjectOutputStream out) throws IOException {}

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {}

        private Object readResolve() throws ObjectStreamException {
            return EMPTY_LIST;
        }
    }

    private final static class ProxyStorageSetEmpty implements Serializable {
        private static final long serialVersionUID = 1L;

        private void writeObject(ObjectOutputStream out) throws IOException {}

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {}

        private Object readResolve() throws ObjectStreamException {
            return EMPTY_SET;
        }
    }

    private final static class ProxyStorageListOne implements Serializable {
        private static final long serialVersionUID = 1L;

        private final byte element;

        public ProxyStorageListOne(byte element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private Object readResolve() throws ObjectStreamException {
            return new ImmutableByteListOne(this.element);
        }
    }

    private final static class ProxyStorageSetOne implements Serializable {
        private static final long serialVersionUID = 1L;

        private final byte element;

        public ProxyStorageSetOne(byte element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private Object readResolve() throws ObjectStreamException {
            return new ImmutableByteSetOne(this.element);
        }
    }

    private final static class ProxyStorageListN implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient byte[] element;

        public ProxyStorageListN(byte[] element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();

            final byte[] eleL = this.element;
            final int size = eleL.length;
            out.writeInt(size);
            for (int i = 0; i < size;) {
                out.writeByte(eleL[i++]);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();

            final int sizeL = in.readInt();
            InternalCollectionUtil.SerialCheck.ioCheckSize(sizeL, 2, Container.MAXIMUM_CAPACITY);

            final byte[] eleL = this.element = newTable(sizeL);
            for (int i = 0; i < sizeL;) {
                eleL[i++] = in.readByte();
            }
        }

        private Object readResolve() throws ObjectStreamException {
            final byte[] eleL;
            if ((eleL = this.element) == null) throw new StreamCorruptedException("Illegal array 'NULL'");
            return new ImmutableByteListN(eleL);
        }
    }

    private final static class ProxyStorageSetN implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient long[] bitMark;

        public ProxyStorageSetN(long[] bitMark) {
            this.bitMark = bitMark;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            final long[] bitL = this.bitMark;
            out.writeLong(bitL[0]);
            out.writeLong(bitL[1]);
            out.writeLong(bitL[2]);
            out.writeLong(bitL[3]);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            InternalCollectionUtil.SerialCheck.ioCheckSize(computeSize(this.bitMark = new long[]{in.readLong(), in.readLong(), in.readLong(), in.readLong()}), 2, 256);
        }

        private Object readResolve() throws ObjectStreamException {
            final long[] bitL;
            if ((bitL = this.bitMark) == null) throw new StreamCorruptedException("Illegal array 'NULL'");
            return new ImmutableByteSetN(bitL);
        }
    }

    static ByteList ofList() {
        return (ByteList) EMPTY_LIST;
    }

    static ByteList ofList(byte element) {
        return new ImmutableByteListOne(element);
    }

    static ByteList ofList(byte... elements) {
        switch (elements.length) {
            case 0: return (ByteList) EMPTY_LIST;
            case 1: return new ImmutableByteListOne(elements[0]);
            default: return new ImmutableByteListN(elements);
        }
    }

    static ByteList copyOfList(Collection<? extends Byte> source) {
        if (source instanceof AbstractImmutableByteList) {
            return (ByteList) source;
        }
        switch (source.size()) {
            case 0: return (ByteList) EMPTY_LIST;
            case 1: return new ImmutableByteListOne((Byte) source.toArray(new Byte[0])[0]);
            default: return new ImmutableByteListN((Byte[]) source.toArray(new Byte[0]));
        }
    }

    static ByteSet ofSet() {
        return (ByteSet) EMPTY_SET;
    }

    static ByteSet ofSet(byte element) {
        return new ImmutableByteSetOne(element);
    }

    static ByteSet ofSet(byte... elements) {
        switch (elements.length) {
            case 0: return (ByteSet) EMPTY_SET;
            case 1: return new ImmutableByteSetOne(elements[0]);
        }

        final ByteBitSet collect = new ByteBitSet(elements);
        final int sizeL = collect.size();
        final long[] eleL = collect.table();

        switch (sizeL) {
            case 0: return (ByteSet) EMPTY_SET;
            case 1: return new ImmutableByteSetOne(elements[0]);
            default: return new ImmutableByteSetN(sizeL, eleL);
        }
    }

    static ByteSet copyOfSet(Collection<? extends Byte> source) {
        if (source instanceof AbstractImmutableByteSet) {
            return (ByteSet) source;
        }

        switch (source.size()) {
            case 0: return (ByteSet) EMPTY_SET;
            case 1: return new ImmutableByteSetOne((Byte) source.toArray(new Byte[0])[0]);
            default: {
                final ByteBitSet collect = new ByteBitSet(source);
                final int sizeL = collect.size();
                final long[] eleL = collect.table();

                switch (sizeL) {
                    case 0: return (ByteSet) EMPTY_SET;
                    case 1: return new ImmutableByteSetOne((Byte) source.toArray(new Byte[0])[0]);
                    default: return new ImmutableByteSetN(sizeL, eleL);
                }
            }
        }
    }

    private ImmutableByteCollections() {}
}
