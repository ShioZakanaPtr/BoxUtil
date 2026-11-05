package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjCollection;
import io.github.shiozakanaptr.acext.util.collection.objecttype.AbstractObjSet;
import io.github.shiozakanaptr.acext.util.collection.objecttype.ObjLinearProbingHashSet;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
final class ImmutableObjCollections {
    final static float DEFAULT_LOAD_FACTOR = 0.75f;
    final static Object NULL_SLOT = null;
    final static HashBehavior<Object> DEFAULT_BEHAVIOR = HashBehaviorFactory.fibonacciMix();
    private final static ObjList<Object> EMPTY_LIST = new ImmutableObjListEmpty<>();
    private final static ObjSet<Object> EMPTY_SET = new ImmutableObjSetEmpty<>();

    private static <T> T[] newTable(int cap) {
        return (T[]) new Object[cap];
    }

    private static <T> T[] newFillTable(T element) {
        return (T[]) new Object[]{element};
    }

    private static abstract class AbstractImmutableObjCollection<E> extends AbstractObjCollection<E> {
        public void resize(int newCapacity) {
            throw new UnsupportedOperationException();
        }

        public void reserve(int newMaxSize) {
            throw new UnsupportedOperationException();
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public boolean removeElement(Object o) {
            throw new UnsupportedOperationException();
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

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeIf(Predicate<? super E> filter) {
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

    private static abstract class AbstractImmutableObjList<E> extends AbstractImmutableObjCollection<E> implements ObjList<E>, RandomAccess {
        public E set(int index, E element) {
            throw new UnsupportedOperationException();
        }

        public void add(int index, E element) {
            throw new UnsupportedOperationException();
        }

        public void addFirst(E e) {
            throw new UnsupportedOperationException();
        }

        public void addLast(E e) {
            throw new UnsupportedOperationException();
        }

        public E removeAt(int index) {
            throw new UnsupportedOperationException();
        }

        public E removeFirst() {
            throw new UnsupportedOperationException();
        }

        public E removeLast() {
            throw new UnsupportedOperationException();
        }

        public E update(int index, Function<? super E, ? extends E> updateFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(List<E> source) {
            throw new UnsupportedOperationException();
        }

        public void swapAt(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        public void erase(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        public void replaceAll(UnaryOperator<E> operator) {
            throw new UnsupportedOperationException();
        }

        public void replace(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
            throw new UnsupportedOperationException();
        }

        public boolean compute(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        public <T extends E> boolean addAll(int dstIndex, T[] elements, int srcIndex, int length) {
            throw new UnsupportedOperationException();
        }

        public abstract Iterator<E> iterator();

        public abstract Spliterator<E> spliterator();
    }

    private interface ImmutableObjIterator<E> extends Iterator<E> {
        default void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private interface ImmutableObjListIterator<E> extends ListIterator<E>, ImmutableObjIterator<E> {
        default void remove() {
            throw new UnsupportedOperationException();
        }

        default void set(E e) {
            throw new UnsupportedOperationException();
        }

        default void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    private final static class ImmutableObjListEmpty<E> extends AbstractImmutableObjList<E> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public E get(int index) {
            throw new IndexOutOfBoundsException("Empty list");
        }

        public boolean contains(Object o) {
            return false;
        }

        public int indexOf(Object o) {
            return -1;
        }

        public int lastIndexOf(Object o) {
            return -1;
        }

        public void forEach(Consumer<? super E> action) {}

        public void forEach(int fromIndex, int toIndex, Consumer<? super E> action) {
            if (fromIndex != 0 || toIndex != 0) throw new IndexOutOfBoundsException();
        }

        public boolean containsAll(@NotNull Collection<?> c) {
            return c.isEmpty();
        }

        public ListIterator<E> listIterator() {
            return new CollectionObjListIterator<>();
        }

        public ListIterator<E> listIterator(int index) {
            if (index != 0) throw new IndexOutOfBoundsException();
            return new CollectionObjListIterator<>();
        }

        public ObjList<E> subList(int fromIndex, int toIndex) {
            if (fromIndex != 0 || toIndex != 0) throw new IndexOutOfBoundsException();
            return this;
        }

        public int size() {
            return 0;
        }

        public Iterator<E> iterator() {
            return new CollectionObjIterator<>();
        }

        public Spliterator<E> spliterator() {
            return new CollectionSpliterator<>();
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
            return new ProxyStorageListEmpty<>();
        }

        private static class CollectionObjIterator<E> implements ImmutableObjIterator<E> {
            public E next() {
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                return false;
            }

            public void forEachRemaining(LongConsumer action) {}
        }

        private final static class CollectionObjListIterator<E> extends CollectionObjIterator<E> implements ImmutableObjListIterator<E> {
            public int nextIndex() {
                return 0;
            }

            public boolean hasPrevious() {
                return false;
            }

            public int previousIndex() {
                return -1;
            }

            public E previous() {
                throw new NoSuchElementException();
            }
        }

        private final static class CollectionSpliterator<E> implements Spliterator<E> {
            public Spliterator<E> trySplit() {
                return null;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                return false;
            }

            public void forEachRemaining(Consumer<? super E> action) {}

            public long estimateSize() {
                return 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE;
            }
        }
    }

    private final static class ImmutableObjListOne<E> extends AbstractImmutableObjList<E> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient E element;

        public ImmutableObjListOne(E element) {
            this.element = element;
        }

        public E get(int index) {
            if (index != 0) throw new IndexOutOfBoundsException("Empty list");
            return this.element;
        }

        public boolean contains(Object o) {
            return Objects.equals(this.element, o);
        }

        public int indexOf(Object o) {
            return Objects.equals(this.element, o) ? 0 : -1;
        }

        public int lastIndexOf(Object o) {
            return Objects.equals(this.element, o) ? 0 : -1;
        }

        public void forEach(Consumer<? super E> action) {
            action.accept(this.element);
        }

        public void forEach(int fromIndex, int toIndex, Consumer<? super E> action) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, 1);
            action.accept(this.element);
        }

        public boolean containsAll(@NotNull Collection<?> c) {
            return c.contains(this.element);
        }

        public ListIterator<E> listIterator() {
            return new CollectionObjListIterator<>(this.element, true);
        }

        public ListIterator<E> listIterator(int index) {
            if (index != 0 && index != 1) throw new IndexOutOfBoundsException();
            return new CollectionObjListIterator<>(this.element, index == 0);
        }

        public ObjList<E> subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, 1);
            return toIndex != 0 ? new ImmutableObjListOne<>(this.element) : (ObjList<E>) EMPTY_LIST;
        }

        public int capacity() {
            return 1;
        }

        public int size() {
            return 1;
        }

        public Iterator<E> iterator() {
            return new CollectionObjIterator<>(this.element);
        }

        public Spliterator<E> spliterator() {
            return new CollectionObjSpliterator<>(this.element);
        }

        public int hashCode() {
            return 31 + Objects.hashCode(this.element);
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
            return '[' + (this.element != this ? Objects.toString(this.element) : "(this Collection)") + ']';
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageListOne<>(this.element);
        }

        private static class CollectionObjIterator<E> implements ImmutableObjIterator<E> {
            protected boolean onZero = true;
            private final E element;

            public CollectionObjIterator(E element) {
                this.element = element;
            }

            public E next() {
                if (!this.onZero) throw new NoSuchElementException();
                this.onZero = false;
                return this.element;
            }

            public boolean hasNext() {
                return this.onZero;
            }

            public void forEachRemaining(Consumer<? super E> action) {
                if (this.onZero) action.accept(this.element);
            }
        }

        private final static class CollectionObjListIterator<E> extends CollectionObjIterator<E> implements ImmutableObjListIterator <E>{
            public CollectionObjListIterator(E element, boolean fromZero) {
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

            public E previous() {
                if (this.onZero) throw new NoSuchElementException();
                this.onZero = true;
                return super.element;
            }
        }

        private final static class CollectionObjSpliterator<E> implements Spliterator<E> {
            private boolean toggle = true;
            private final E element;

            public CollectionObjSpliterator(E element) {
                this.element = element;
            }

            public Spliterator<E> trySplit() {
                return null;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(Consumer<? super E> action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                }
            }

            public long estimateSize() {
                return this.toggle ? 1 : 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE;
            }
        }
    }

    private final static class ImmutableObjListN<E> extends AbstractImmutableObjList<E> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient E[] element;

        public ImmutableObjListN(E[] element) {
            this.element = element;
        }

        public E get(int index) {
            return this.element[index];
        }

        public boolean contains(Object o) {
            final E[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                if (Objects.equals(o, eleL[i++])) return true;
            }
            return false;
        }

        public int indexOf(Object o) {
            final E[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit; i++) {
                if (Objects.equals(o, eleL[i])) return i;
            }
            return -1;
        }

        public int lastIndexOf(Object o) {
            final E[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = limit - 1; i > -1; i--) {
                if (Objects.equals(o, eleL[i])) return i;
            }
            return -1;
        }

        public void forEach(Consumer<? super E> action) {
            final E[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                action.accept(eleL[i++]);
            }
        }

        public void forEach(int fromIndex, int toIndex, Consumer<? super E> action) {
            for (int i = fromIndex; i < toIndex;) {
                action.accept(this.element[i++]);
            }
        }

        public boolean containsAll(Collection<?> c) {
            if (c == this) return true;
            if (c.isEmpty()) return false;
            return super.containsAll(c);
        }

        public ListIterator<E> listIterator() {
            return new CollectionObjListIterator(0);
        }

        public ListIterator<E> listIterator(int index) {
            InternalCollectionUtil.checkIndex(index, 0, this.element.length);
            return new CollectionObjListIterator(index);
        }

        public ObjList<E> subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.element.length);
            return new ImmutableObjSubList(fromIndex, toIndex);
        }

        public int capacity() {
            return this.element.length;
        }

        public int size() {
            return this.element.length;
        }

        public Iterator<E> iterator() {
            return new CollectionObjIterator();
        }

        public Spliterator<E> spliterator() {
            return new CollectionObjSpliterator(0, -1);
        }

        public int hashCode() {
            int result = 1;
            final E[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                result = 31 * result + Objects.hashCode(eleL[i++]);
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
                    if (castList instanceof ImmutableObjListN) {
                        final ImmutableObjListN<?> cast = (ImmutableObjListN<?>) o;
                        return InternalCollectionUtil.arrayEquals(this.element, 0, this.element.length, cast.element, 0, cast.element.length);
                    } else if (castList instanceof ImmutableObjListN<?>.ImmutableObjSubList) {
                        final ImmutableObjListN<?>.ImmutableObjSubList cast = (ImmutableObjListN<?>.ImmutableObjSubList) o;
                        return InternalCollectionUtil.arrayEquals(this.element, 0, this.element.length, cast.rootArray(), cast.offset, cast.size);
                    } else {
                        final Iterator<?> oit = castList.iterator();
                        for (E l : this.element) {
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
            final E[] eleL = this.element;
            final int limit = eleL.length;
            for (int i = 0; i < limit;) {
                if (sb.length() > 1) sb.append(',').append(' ');
                final E currEle;
                sb.append((currEle = eleL[i++]) != this ? currEle : "(this Collection)");
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
            return new ProxyStorageListN<>(this.element);
        }

        private class CollectionObjIterator implements ImmutableObjIterator<E> {
            protected int pos = 0;

            public boolean hasNext() {
                return this.pos < ImmutableObjListN.this.element.length;
            }

            public E next() {
                if (this.pos >= ImmutableObjListN.this.element.length) throw new NoSuchElementException();
                return ImmutableObjListN.this.element[this.pos++];
            }

            public void forEachRemaining(Consumer<? super E> action) {
                final E[] eleL = ImmutableObjListN.this.element;
                final int limit = eleL.length;
                for (int i = this.pos; i < limit;) {
                    action.accept(eleL[i++]);
                }
                this.pos = limit;
            }
        }

        private final class CollectionObjListIterator extends CollectionObjIterator implements ImmutableObjListIterator<E> {
            public CollectionObjListIterator(int index) {
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

            public E previous() {
                if (this.pos < 1) throw new NoSuchElementException();
                return ImmutableObjListN.this.element[this.pos--];
            }
        }

        private final class CollectionObjSpliterator implements Spliterator<E> {
            private int index;
            private int limit;

            public CollectionObjSpliterator(int index, int limit) {
                this.index = index;
                this.limit = limit;
            }

            private int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limit) < 0) {
                    newLimit = this.limit = ImmutableObjListN.this.element.length;
                }
                return newLimit;
            }

            public Spliterator<E> trySplit() {
                final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                return newIndex < midIndex ? new CollectionObjSpliterator(newIndex, this.index = midIndex) : null;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                final int currIndex;
                if ((currIndex = this.index) < this.getLimitCap()) {
                    action.accept(ImmutableObjListN.this.element[currIndex]);
                    this.index++;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(Consumer<? super E> action) {
                final int currLimit = this.getLimitCap();
                for (int i = this.index; i < currLimit; i++) {
                    action.accept(ImmutableObjListN.this.element[i]);
                }
                this.index = currLimit;
            }

            public long estimateSize() {
                return this.getLimitCap() - this.index;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE;
            }
        }

        protected class ImmutableObjSubList extends AbstractImmutableObjList<E> implements RandomAccess {
            protected int size;
            protected final int limit;
            protected final int offset;

            protected ImmutableObjSubList(int fromIdx, int toIdx) {
                this.size = toIdx - fromIdx;
                this.limit = toIdx;
                this.offset = fromIdx;
            }

            protected ImmutableObjSubList(ImmutableObjSubList from, int fromIdx, int toIdx) {
                this.size = toIdx - fromIdx;
                this.limit = from.offset + toIdx;
                this.offset = from.offset + fromIdx;
            }

            protected E[] rootArray() {
                return ImmutableObjListN.this.element;
            }

            public E get(int index) {
                InternalCollectionUtil.checkBound(index, 0, this.size);
                return ImmutableObjListN.this.get(index + this.offset);
            }

            public boolean contains(Object o) {
                final int limit = this.size + this.offset;
                final E[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    if (Objects.equals(o, eleL[i++])) return true;
                }
                return false;
            }

            public int indexOf(Object o) {
                final int limit = this.size + this.offset;
                final E[] eleL = this.rootArray();
                for (int i = this.offset; i < limit; i++) {
                    if (Objects.equals(o, eleL[i])) return i;
                }
                return -1;
            }

            public int lastIndexOf(Object o) {
                final int limit = this.size + this.offset, min = this.offset - 1;
                final E[] eleL = this.rootArray();
                for (int i = limit - 1; i > min; i--) {
                    if (Objects.equals(o, eleL[i])) return i;
                }
                return -1;
            }

            public void forEach(Consumer<? super E> action) {
                final int limit = this.size + this.offset;
                final E[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
            }

            public void forEach(int fromIndex, int toIndex, Consumer<? super E> action) {
                InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
                final int limit = toIndex + this.offset;
                final E[] eleL = this.rootArray();
                for (int i = fromIndex + this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
            }

            public boolean containsAll(Collection<?> c) {
                if (this.isEmpty()) return c.isEmpty();
                if (c == this) return true;
                return super.containsAll(c);
            }

            public ListIterator<E> listIterator() {
                return new CollectionObjSubListIterator(0);
            }

            public ListIterator<E> listIterator(int index) {
                InternalCollectionUtil.checkIndex(index, 0, this.size);
                return new CollectionObjSubListIterator(index);
            }

            public ObjList<E> subList(int fromIndex, int toIndex) {
                InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
                return new ImmutableObjSubList(this, fromIndex, toIndex);
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

            public Iterator<E> iterator() {
                return new CollectionObjSubIterator();
            }

            public Spliterator<E> spliterator() {
                return new CollectionObjSubSpliterator(0, -1);
            }

            public int hashCode() {
                int result = 1;
                final int limit = this.size + this.offset;
                final E[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    result = 31 * result + Objects.hashCode(eleL[i++]);
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
                        if (castList instanceof ImmutableObjListN<?>) {
                            final ImmutableObjListN<?> cast = ((ImmutableObjListN<?>) o);
                            return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.element, 0, cast.element.length);
                        } else if (castList instanceof ImmutableObjListN<?>.ImmutableObjSubList) {
                            final ImmutableObjListN<?>.ImmutableObjSubList cast = (ImmutableObjListN<?>.ImmutableObjSubList) o;
                            return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.rootArray(), cast.offset, cast.size);
                        } else {
                            final Iterator<?> oit = castList.iterator();
                            final int limit = this.size;
                            final E[] eleL = this.rootArray();
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
                final E[] eleL = this.rootArray();
                for (int i = this.offset; i < limit;) {
                    if (sb.length() > 1) sb.append(',').append(' ');
                    final E currEle;
                    sb.append((currEle = eleL[i++]) != this ? currEle : "(this Collection)");
                }
                sb.append(']');
                return sb.toString();
            }

            protected class CollectionObjSubIterator implements ImmutableObjIterator<E> {
                protected int pos = 0;

                public boolean hasNext() {
                    return this.pos < ImmutableObjSubList.this.size;
                }

                public E next() {
                    if (this.pos >= ImmutableObjSubList.this.size) throw new NoSuchElementException();
                    return ImmutableObjSubList.this.rootArray()[ImmutableObjSubList.this.offset + this.pos++];
                }

                public void forEachRemaining(Consumer<? super E> action) {
                    final int rawLimit = ImmutableObjSubList.this.size, limit = rawLimit + ImmutableObjSubList.this.offset;
                    final E[] eleL = ImmutableObjSubList.this.rootArray();
                    for (int i = this.pos + ImmutableObjSubList.this.offset; i < limit;) {
                        action.accept(eleL[i++]);
                    }
                    this.pos = rawLimit;
                }
            }

            protected class CollectionObjSubListIterator extends CollectionObjSubIterator implements ImmutableObjListIterator<E> {
                protected CollectionObjSubListIterator(int index) {
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

                public E previous() {
                    if (this.pos < 1) throw new NoSuchElementException();
                    return ImmutableObjSubList.this.rootArray()[this.pos-- + ImmutableObjSubList.this.offset];
                }
            }

            protected class CollectionObjSubSpliterator implements Spliterator<E> {
                protected int index;
                protected int limit;

                protected CollectionObjSubSpliterator(int index, int limit) {
                    this.index = index;
                    this.limit = limit;
                }

                private int getLimitCap() {
                    int newLimit;
                    if ((newLimit = this.limit) < 0) {
                        newLimit = this.limit = ImmutableObjSubList.this.size;
                    }
                    return newLimit;
                }

                public Spliterator<E> trySplit() {
                    final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                    return (newIndex >= midIndex) ? null : new CollectionObjSubSpliterator(newIndex, this.index = midIndex);
                }

                public boolean tryAdvance(Consumer<? super E> action) {
                    final int currIndex;
                    if ((currIndex = this.index) < this.getLimitCap()) {
                        action.accept(ImmutableObjSubList.this.rootArray()[currIndex + ImmutableObjSubList.this.offset]);
                        this.index++;
                        return true;
                    }
                    return false;
                }

                public void forEachRemaining(Consumer<? super E> action) {
                    final int rawLimit = this.getLimitCap(), currLimit = rawLimit + ImmutableObjSubList.this.offset;
                    final E[] eleL = ImmutableObjSubList.this.rootArray();
                    for (int i = this.index + ImmutableObjSubList.this.offset; i < currLimit; i++) {
                        action.accept(eleL[i]);
                    }
                    this.index = rawLimit;
                }

                public long estimateSize() {
                    return this.getLimitCap() - this.index;
                }

                public int characteristics() {
                    return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE;
                }
            }
        }
    }

    private static abstract class AbstractImmutableObjSet<E> extends AbstractImmutableObjCollection<E> implements ObjSet<E> {
        public boolean remapping(E element, Function<? super E, ? extends E> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        public void swap(Set<E> source) {
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

        public abstract Iterator<E> iterator();

        public abstract Spliterator<E> spliterator();
    }

    private static class ImmutableObjSetEmpty<E> extends AbstractImmutableObjSet<E> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        public boolean contains(Object o) {
            return false;
        }

        public void forEach(Consumer<? super E> action) {}

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

        public Iterator<E> iterator() {
            return new CollectionObjIterator<>();
        }

        public Spliterator<E> spliterator() {
            return new CollectionObjSpliterator<>();
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
            return new ProxyStorageSetEmpty<>();
        }

        private static class CollectionObjIterator<E> implements ImmutableObjIterator<E> {
            public E next() {
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                return false;
            }

            public void forEachRemaining(Consumer<? super E> action) {}
        }

        private final static class CollectionObjSpliterator<E> implements Spliterator<E> {
            public Spliterator<E> trySplit() {
                return null;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                return false;
            }

            public void forEachRemaining(Consumer<? super E> action) {}

            public long estimateSize() {
                return 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE;
            }
        }
    }

    private static class ImmutableObjSetOne<E> extends AbstractImmutableObjSet<E> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient E element;

        public ImmutableObjSetOne(E element) {
            this.element = element;
        }

        public boolean contains(Object o) {
            return DEFAULT_BEHAVIOR.equals(o, this.element);
        }

        public void forEach(Consumer<? super E> action) {
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

        public Iterator<E> iterator() {
            return new CollectionObjIterator<>(this.element);
        }

        public Spliterator<E> spliterator() {
            return new CollectionObjSpliterator<>(this.element);
        }

        public int hashCode() {
            return 31 + Objects.hashCode(this.element);
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
            return '[' + (this.element != this ? Objects.toString(this.element) : "(this Collection)") + ']';
        }

        public Object clone() {
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageSetOne<>(this.element);
        }

        static class CollectionObjIterator<E> implements ImmutableObjIterator<E> {
            private boolean onZero = true;
            private final E element;

            public CollectionObjIterator(E element) {
                this.element = element;
            }

            public E next() {
                if (!this.onZero) throw new NoSuchElementException();
                this.onZero = false;
                return this.element;
            }

            public boolean hasNext() {
                return this.onZero;
            }

            public void forEachRemaining(Consumer<? super E> action) {
                if (this.onZero) action.accept(this.element);
            }
        }

        static class CollectionObjSpliterator<E> implements Spliterator<E> {
            private boolean toggle = true;
            private final E element;

            public CollectionObjSpliterator(E element) {
                this.element = element;
            }

            public Spliterator<E> trySplit() {
                return null;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(Consumer<? super E> action) {
                if (this.toggle) {
                    action.accept(this.element);
                    this.toggle = false;
                }
            }

            public long estimateSize() {
                return this.toggle ? 1 : 0;
            }

            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE;
            }
        }
    }

    private static class ImmutableObjSetN<E> extends AbstractImmutableObjSet<E> implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;

        private final transient boolean withNullEle;
        private final transient int size;
        private final transient int capacity;
        private final transient int posMask;
        private final transient E[] element;

        public ImmutableObjSetN(boolean withNullEle, int size, int capacity, E[] element) {
            this.withNullEle = withNullEle;
            this.size = size;
            this.capacity = capacity;
            this.posMask = capacity - 1;
            this.element = element;
        }

        public boolean contains(Object o) {
            if (DEFAULT_BEHAVIOR.equals(o, ((E) NULL_SLOT))) return this.withNullEle;

            E currEle;
            for (int pos = DEFAULT_BEHAVIOR.hashing(o); (currEle = this.element[pos &= this.posMask]) != ((E) NULL_SLOT); pos++) {
                if (DEFAULT_BEHAVIOR.equals(o, currEle)) return true;
            }
            return false;
        }

        public void forEach(Consumer<? super E> action) {
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

        public int capacity() {
            return this.capacity;
        }

        public boolean isEmpty() {
            return false;
        }

        public int size() {
            return this.size;
        }

        public Iterator<E> iterator() {
            return new CollectionObjIterator();
        }

        public Spliterator<E> spliterator() {
            return new CollectionObjSpliterator(0, -1, 0);
        }

        public E[] toArrayRaw() {
            final E[] result = newTable(this.size);
            int idx = 0;
            for (int pos = 0; pos < this.capacity;) {
                final E currEle;
                if ((currEle = this.element[pos++]) != ((E) NULL_SLOT)) result[idx++] = currEle;
            }
            return result;
        }

        public int hashCode() {
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
                final Set<?> castSet = ((Set<?>) o);
                if (castSet.size() != this.size) return false;
                try {
                    if (castSet instanceof ImmutableObjSetN) {
                        final ImmutableObjSetN<?> cast = (ImmutableObjSetN<?>) castSet;
                        if (this.withNullEle != cast.withNullEle) return false;
                        final Object[] srcEleL = cast.element;

                        final int capacity = srcEleL.length;
                        for (int srcPos = 0; srcPos < capacity;) {
                            final Object srcEle;
                            if ((srcEle = srcEleL[srcPos++]) != ((E) NULL_SLOT)) {
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
            return this;
        }

        private void readObject(ObjectInputStream in) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        private Object writeReplace() {
            return new ProxyStorageSetN<>(this.toArrayRaw());
        }

        class CollectionObjIterator implements ImmutableObjIterator<E> {
            protected int pos = -1;
            protected int posNext = -1;

            public CollectionObjIterator() {
                if (ImmutableObjSetN.this.size > 0) {
                    this.scanPos(ImmutableObjSetN.this.element, 0);
                }
            }

            protected void scanPos(final E[] eleL, int start) {
                final int capL = ImmutableObjSetN.this.capacity;
                int findPos = -1, findPosNext = -1;
                for (int i = start; i < capL; i++) {
                    if (eleL[i] != ((E) NULL_SLOT)) {
                        if (findPos < 0) findPos = i;
                        else if (findPosNext < 0) findPosNext = i;
                        else break;
                    }
                }
                if (ImmutableObjSetN.this.withNullEle) {
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

                final int posL = this.pos = this.posNext;
                this.posNext = -1;

                final E[] eleL = ImmutableObjSetN.this.element;
                if (posL > -1) {
                    final int capL = ImmutableObjSetN.this.capacity;
                    int i;
                    for (i = posL + 1; i < capL; i++) {
                        if (eleL[i] != ((E) NULL_SLOT)) {
                            this.posNext = i;
                            break;
                        }
                    }
                    if (i == capL && ImmutableObjSetN.this.withNullEle) {
                        this.posNext = i;
                    }
                }
                return eleL[current];
            }

            public void forEachRemaining(Consumer<? super E> action) {
                final int current;
                if ((current = this.pos) < 0) return;
                final E[] eleL = ImmutableObjSetN.this.element;
                final int capL = ImmutableObjSetN.this.capacity;
                int i;
                for (i = current; i < capL; i++) {
                    final E currEle;
                    if ((currEle = eleL[i]) != ((E) NULL_SLOT)) {
                        action.accept(currEle);
                    }
                }
                if (i == capL && ImmutableObjSetN.this.withNullEle) {
                    action.accept(eleL[capL]);
                }
                this.pos = -1;
            }
        }

        class CollectionObjSpliterator implements Spliterator<E> {
            protected int index;
            protected int limitCap;
            protected int estSize;

            protected CollectionObjSpliterator(int index, int limitCap, int estSize) {
                this.index = index;
                this.limitCap = limitCap;
                this.estSize = estSize;
            }

            protected final int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limitCap) < 0) {
                    this.estSize = ImmutableObjSetN.this.size;
                    newLimit = this.limitCap = ImmutableObjSetN.this.capacity;
                }
                return newLimit;
            }

            public Spliterator<E> trySplit() {
                final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                return newIndex < midIndex ? new CollectionObjSpliterator(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                final int currLimit = this.getLimitCap();

                int idxL = this.index;
                final E[] eleL = ImmutableObjSetN.this.element;
                while (idxL < currLimit) {
                    final E currEle;
                    if ((currEle = eleL[idxL++]) != ((E) NULL_SLOT)) {
                        this.index = idxL;
                        action.accept(currEle);
                        return true;
                    }
                }
                final int capL = ImmutableObjSetN.this.capacity;
                if (idxL == currLimit && idxL == capL && ImmutableObjSetN.this.withNullEle) {
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
                final E[] eleL = ImmutableObjSetN.this.element;
                while (idxL < currLimit) {
                    final E currEle;
                    if ((currEle = eleL[idxL++]) != ((E) NULL_SLOT)) action.accept(currEle);
                }
                final int capL = ImmutableObjSetN.this.capacity;
                if (idxL == currLimit && idxL == capL && ImmutableObjSetN.this.withNullEle) {
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
                return (this.limitCap < 0 || this.estSize == ImmutableObjSetN.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT | Spliterator.IMMUTABLE;
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

    private final static class ProxyStorageListEmpty<E> implements Serializable {
        private static final long serialVersionUID = 1L;

        private void writeObject(ObjectOutputStream out) throws IOException {}

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {}

        private Object readResolve() throws ObjectStreamException {
            return EMPTY_LIST;
        }
    }

    private final static class ProxyStorageSetEmpty<E> implements Serializable {
        private static final long serialVersionUID = 1L;

        private void writeObject(ObjectOutputStream out) throws IOException {}

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {}

        private Object readResolve() throws ObjectStreamException {
            return EMPTY_SET;
        }
    }

    private final static class ProxyStorageListOne<E> implements Serializable {
        private static final long serialVersionUID = 1L;

        private final E element;

        public ProxyStorageListOne(E element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private Object readResolve() throws ObjectStreamException {
            return new ImmutableObjListOne<>(this.element);
        }
    }

    private final static class ProxyStorageSetOne<E> implements Serializable {
        private static final long serialVersionUID = 1L;

        private final E element;

        public ProxyStorageSetOne(E element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
        }

        private Object readResolve() throws ObjectStreamException {
            return new ImmutableObjSetOne<>(this.element);
        }
    }

    private final static class ProxyStorageListN<E> implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient E[] element;

        public ProxyStorageListN(E[] element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();

            final E[] eleL = this.element;
            final int size = eleL.length;
            out.writeInt(size);
            for (int i = 0; i < size;) {
                out.writeObject(eleL[i++]);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();

            final int sizeL = in.readInt();
            InternalCollectionUtil.SerialCheck.ioCheckSize(sizeL, 2, Container.MAXIMUM_CAPACITY);

            final E[] eleL = this.element = newTable(sizeL);
            for (int i = 0; i < sizeL;) {
                eleL[i++] = (E) in.readObject();
            }
        }

        private Object readResolve() throws ObjectStreamException {
            final E[] eleL;
            if ((eleL = this.element) == null) throw new StreamCorruptedException("Illegal array 'NULL'");
            return new ImmutableObjListN<>(eleL);
        }
    }

    private final static class ProxyStorageSetN<E> implements Serializable {
        private static final long serialVersionUID = 1L;

        private transient E[] element;

        public ProxyStorageSetN(E[] element) {
            this.element = element;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();

            final E[] eleL = this.element;
            final int size = eleL.length;
            out.writeInt(size);
            for (int i = 0; i < size;) {
                out.writeObject(eleL[i++]);
            }
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();

            final int sizeL = in.readInt();
            InternalCollectionUtil.SerialCheck.ioCheckSize(sizeL, 2, HashContainer.MAXIMUM_CAPACITY);
            InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(sizeL, DEFAULT_LOAD_FACTOR, HashContainer.MAXIMUM_CAPACITY);

            final E[] eleL = this.element = newTable(sizeL);
            for (int i = 0; i < sizeL;) {
                eleL[i++] = (E) in.readObject();
            }
        }

        private Object readResolve() throws ObjectStreamException {
            final E[] eleL;
            if ((eleL = this.element) == null) throw new StreamCorruptedException("Illegal array 'NULL'");
            return ofSetSerial(eleL);
        }
    }

    static <E> ObjList<E> ofList() {
        return (ObjList<E>) EMPTY_LIST;
    }

    static <E> ObjList<E> ofList(E element) {
        return new ImmutableObjListOne<>(element);
    }

    @SafeVarargs
    static <E> ObjList<E> ofList(E... elements) {
        switch (elements.length) {
            case 0: return (ObjList<E>) EMPTY_LIST;
            case 1: return new ImmutableObjListOne<>(elements[0]);
            default: return new ImmutableObjListN<>(elements);
        }
    }

    static <E> ObjList<E> copyOfList(Collection<? extends E> source) {
        if (source instanceof AbstractImmutableObjList) {
            return (ObjList<E>) source;
        }
        switch (source.size()) {
            case 0: return (ObjList<E>) EMPTY_LIST;
            case 1: return new ImmutableObjListOne<>((E) source.toArray(new Object[0])[0]);
            default: return new ImmutableObjListN<>((E[]) source.toArray(new Object[0]));
        }
    }

    static <E> ObjSet<E> ofSet() {
        return (ObjSet<E>) EMPTY_SET;
    }

    static <E> ObjSet<E> ofSet(E element) {
        return new ImmutableObjSetOne<>(element);
    }

    @SafeVarargs
    static <E> ObjSet<E> ofSetSerial(E... elements) throws StreamCorruptedException {
        final ObjLinearProbingHashSet<E> collect = new ObjLinearProbingHashSet<>(elements, DEFAULT_LOAD_FACTOR, (HashBehavior<E>) DEFAULT_BEHAVIOR);
        collect.shrink();
        final boolean withNullEleL = collect.contains(null);
        final int sizeL = collect.size(), capacityL = collect.capacity();
        final E[] eleL = collect.table();
        InternalCollectionUtil.SerialCheck.ioCheckElementCount(elements.length, sizeL);
        return new ImmutableObjSetN<>(withNullEleL, sizeL, capacityL, eleL);
    }

    @SafeVarargs
    static <E> ObjSet<E> ofSet(E... elements) {
        switch (elements.length) {
            case 0: return (ObjSet<E>) EMPTY_SET;
            case 1: return new ImmutableObjSetOne<>(elements[0]);
        }

        final ObjLinearProbingHashSet<E> collect = new ObjLinearProbingHashSet<>(elements, DEFAULT_LOAD_FACTOR, (HashBehavior<E>) DEFAULT_BEHAVIOR);
        collect.shrink();
        final boolean withNullEleL = collect.contains(null);
        final int sizeL = collect.size(), capacityL = collect.capacity();
        final E[] eleL = collect.table();

        switch (sizeL) {
            case 0: return (ObjSet<E>) EMPTY_SET;
            case 1: return new ImmutableObjSetOne<>(elements[0]);
            default: return new ImmutableObjSetN<>(withNullEleL, sizeL, capacityL, eleL);
        }
    }

    static <E> ObjSet<E> copyOfSet(Collection<? extends E> source) {
        if (source instanceof AbstractImmutableObjSet) {
            return (ObjSet<E>) source;
        }

        switch (source.size()) {
            case 0: return (ObjSet<E>) EMPTY_SET;
            case 1: return new ImmutableObjSetOne<>((E) source.toArray(new Object[0])[0]);
            default: {
                final ObjLinearProbingHashSet<E> collect = new ObjLinearProbingHashSet<>(source, DEFAULT_LOAD_FACTOR, (HashBehavior<E>) DEFAULT_BEHAVIOR);
                collect.shrink();
                final boolean withNullEleL = collect.contains(null);
                final int sizeL = collect.size(), capacityL = collect.capacity();
                final E[] eleL = collect.table();

                switch (sizeL) {
                    case 0: return (ObjSet<E>) EMPTY_SET;
                    case 1: return new ImmutableObjSetOne<>((E) source.toArray(new Object[0])[0]);
                    default: return new ImmutableObjSetN<>(withNullEleL, sizeL, capacityL, eleL);
                }
            }
        }
    }

    private ImmutableObjCollections() {}
}
