package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjList;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
public class ObjArrayList<E> extends AbstractObjList<E> implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static Object NULL_SLOT = null;

    protected int size = 0;
    protected transient E[] element;

    protected E[] newTable(int cap) {
        return (E[]) new Object[cap];
    }

    /**
     * @param initialCapacity will directly use the value.
     */
    public ObjArrayList(int initialCapacity) {
        InternalCollectionUtil.checkCapacity(initialCapacity);
        this.element = this.newTable(initialCapacity);
    }

    public ObjArrayList() {
        this(Container.DEFAULT_INITIAL_CAPACITY);
    }

    public ObjArrayList(final Collection<? extends E> source) {
        this(source.size() + 1);
        this.addAll(source);
    }

    public <T extends E> ObjArrayList(final T[] source) {
        this(source.length + 1);
        this.addAll(source);
    }

    public void resize(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        InternalCollectionUtil.checkCapacity(newCapacity);
        final E[] oldEle;
        final int newCap, sizeL;
        if ((newCap = Math.max(sizeL = this.size, newCapacity)) == (oldEle = this.element).length) return;

        final E[] newEle;
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

    protected void removeAndShift(final E[] eleL, int fromIndex, int toIndex, int length) {
        if (length == 0) return;
        System.arraycopy(eleL, toIndex, eleL, fromIndex, this.size - toIndex);
        final int oldSize = this.size;
        Arrays.fill(eleL, oldSize - length, oldSize, ((E) NULL_SLOT));
        this.size -= length;
    }

    public E get(int index) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        return this.element[index];
    }

    public E set(int index, E element) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        final E[] eleL;
        final E oldValue = (eleL = this.element)[index];
        eleL[index] = element;
        return oldValue;
    }

    public boolean add(E e) {
        this.checkExpand();
        this.element[this.size++] = e;
        return true;
    }

    public void add(int index, E element) {
        final int sizeL;
        InternalCollectionUtil.checkIndex(index, 0, sizeL = this.size);
        this.checkExpand();
        final E[] eleL;
        System.arraycopy(eleL = this.element, index, eleL, index + 1, sizeL - index);
        eleL[index] = element;
        this.size++;
    }

    public E removeAt(int index) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        final E[] eleL;
        final E oldValue = (eleL = this.element)[index];
        this.removeAndShift(eleL, index, index + 1, 1);
        return oldValue;
    }

    public boolean removeElement(Object o) {
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit; i++) {
            if (Objects.equals(o, eleL[i])) {
                this.removeAndShift(eleL, i, i + 1, 1);
                return true;
            }
        }
        return false;
    }

    public boolean contains(Object o) {
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            if (Objects.equals(o, eleL[i++])) return true;
        }
        return false;
    }

    public int indexOf(Object o) {
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit; i++) {
            if (Objects.equals(o, eleL[i])) return i;
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = limit - 1; i > -1; i--) {
            if (Objects.equals(o, eleL[i])) return i;
        }
        return -1;
    }

    public E update(int index, Function<? super E, ? extends E> updateFunction) {
        InternalCollectionUtil.checkBound(index, 0, this.size);
        final E[] eleL = this.element;
        return eleL[index] = updateFunction.apply(eleL[index]);
    }

    public void swap(List<E> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ObjArrayList) {
            final ObjArrayList<E> src = (ObjArrayList<E>) source;
            final int currSize = this.size, srcSize = src.size;
            final E[] currEle = this.element, srcEle = src.element;

            src.size = currSize;
            src.element = currEle;
            this.size = srcSize;
            this.element = srcEle;
        } else {
            if (source.size() < this.size()) {
                final List<E> tmp = new ObjArrayList<>(source);
                source.clear();
                source.addAll(this);
                this.clear();
                this.addAll(tmp);
            } else {
                final List<E> tmp = new ObjArrayList<>(this);
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
        final E[] eleL = this.element;
        final E tmpValue = eleL[fromIndex];
        eleL[fromIndex] = eleL[toIndex];
        eleL[toIndex] = tmpValue;
    }

    public void erase(int fromIndex, int toIndex) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        this.removeAndShift(this.element, fromIndex, toIndex, toIndex - fromIndex);
    }

    public void forEach(Consumer<? super E> action) {
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            action.accept(eleL[i++]);
        }
    }

    public void forEach(int fromIndex, int toIndex, Consumer<? super E> action) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        final E[] eleL = this.element;
        for (int i = fromIndex; i < toIndex;) {
            action.accept(eleL[i++]);
        }
    }

    public void replaceAll(UnaryOperator<E> operator) {
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit; i++) {
            eleL[i] = operator.apply(eleL[i]);
        }
    }

    public void replace(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        final E[] eleL = this.element;
        for (int i = fromIndex; i < toIndex; i++) {
            eleL[i] = operator.apply(eleL[i]);
        }
    }

    protected int in_computeRange(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
        if (fromIndex == toIndex) return 0;

        int idx = fromIndex;
        final E[] eleL = this.element;
        for (int i = fromIndex; i < toIndex;) {
            final E newValue = operator.apply(eleL[i++]);
            if (newValue != null) eleL[idx++] = newValue;
        }
        final int result;
        this.removeAndShift(this.element, idx, toIndex, result = toIndex - idx);
        return result;
    }

    public boolean compute(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        return this.in_computeRange(fromIndex, toIndex, operator) != 0;
    }

    public boolean addAll(Collection<? extends E> c) {
        return this.addAll(this.size, c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        final int sizeL;
        InternalCollectionUtil.checkIndex(index, 0, sizeL = this.size);
        final int length = c.size();
        if (length < 1) return false;
        if (length > this.remaining()) this.resize(sizeL + length + 1);
        if (c instanceof ObjArrayList<?>) {
            final ObjArrayList<?> cast = (ObjArrayList<?>) c;
            return this.addAll(index, (E[]) cast.element, 0, length);
        } else if (c instanceof ObjArrayList<?>.ObjSubArrayList) {
            final ObjArrayList<?>.ObjSubArrayList cast = (ObjArrayList<?>.ObjSubArrayList) c;
            return this.addAll(index, (E[]) cast.rootArray(), cast.offset, length);
        }

        final E[] eleL = this.element;
        System.arraycopy(eleL, index, eleL, index + length, sizeL - index);

        int idx = index;
        for (final E value : c) {
            eleL[idx++] = value;
        }
        this.size += length;
        return true;
    }

    public <T extends E> boolean addAll(T[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    public <T extends E> boolean addAll(T[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    public <T extends E> boolean addAll(int dstIndex, T[] elements, int srcIndex, int length) {
        final int sizeL;
        InternalCollectionUtil.checkIndex(dstIndex, 0, sizeL = this.size);
        if (length == 0) return false;
        if (length > this.remaining()) this.resize(sizeL + length + 1);
        if (elements == this.element) {
            elements = Arrays.copyOfRange(elements, srcIndex, srcIndex + length);
            srcIndex = 0;
        }
        final E[] eleL = this.element;
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
            Arrays.fill(this.element, 0, this.size, ((E) NULL_SLOT));
            this.size = 0;
            return true;
        }

        int idx = 0;
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            final E currEle;
            if (!c.contains(currEle = eleL[i++])) eleL[idx++] = currEle;
        }
        Arrays.fill(eleL, idx, limit, ((E) NULL_SLOT));
        final boolean result = idx != limit;
        this.size = idx;
        return result;
    }

    public boolean retainAll(Collection<?> c) {
        if (c == this || this.isEmpty()) return false;

        int idx = 0;
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            final E currEle;
            if (c.contains(currEle = eleL[i++])) eleL[idx++] = currEle;
        }
        Arrays.fill(eleL, idx, limit, ((E) NULL_SLOT));
        final boolean result = idx != limit;
        this.size = idx;
        return result;
    }

    public boolean removeIf(Predicate<? super E> filter) {
        if (this.isEmpty()) return false;

        int idx = 0;
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            final E currEle;
            if (!filter.test(currEle = eleL[i++])) eleL[idx++] = currEle;
        }
        Arrays.fill(eleL, idx, limit, ((E) NULL_SLOT));
        final boolean result = idx != limit;
        this.size = idx;
        return result;
    }

    public ListIterator<E> listIterator() {
        return new CollectionListIterator(0);
    }

    public ListIterator<E> listIterator(int index) {
        InternalCollectionUtil.checkIndex(index, 0, this.size);
        return new CollectionListIterator(index);
    }

    public ObjList<E> subList(int fromIndex, int toIndex) {
        InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
        return new ObjSubArrayList(fromIndex, toIndex);
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

    public Iterator<E> iterator() {
        return new CollectionIterator();
    }

    public Spliterator<E> spliterator() {
        return new CollectionSpliterator(0, -1);
    }

    public E[] table() {
        return this.element;
    }

    public void clear() {
        if (this.isEmpty()) return;
        Arrays.fill(this.element, 0, this.size, ((E) NULL_SLOT));
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

    public int hashCode() {
        int result = 1;
        final int limit = this.size;
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            result = 31 * result + Objects.hashCode(eleL[i++]);
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof List<?>) {
            final List<?> castList = (List<?>) o;
            if (castList.size() != this.size()) return false;
            try {
                if (castList instanceof ObjArrayList<?>) {
                    final ObjArrayList<?> cast = (ObjArrayList<?>) castList;
                    return InternalCollectionUtil.arrayEquals(this.element, 0, this.size, cast.element, 0, cast.size);
                } else if (castList instanceof ObjArrayList<?>.ObjSubArrayList) {
                    final ObjArrayList<?>.ObjSubArrayList cast = (ObjArrayList<?>.ObjSubArrayList) castList;
                    return InternalCollectionUtil.arrayEquals(this.element, 0, this.size, cast.rootArray(), cast.offset, cast.size);
                } else {
                    final Iterator<?> oit = castList.iterator();
                    final int limit = this.size;
                    final E[] eleL = this.element;
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
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            if (sb.length() > 1) sb.append(',').append(' ');
            final E currEle;
            sb.append((currEle =eleL[i++]) != this ? currEle : "(this Collection)");
        }
        sb.append(']');
        return sb.toString();
    }

    public Object clone() {
        ObjArrayList<E> result;
        try {
            result = (ObjArrayList<E>) super.clone();
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
        final E[] eleL = this.element;
        for (int i = 0; i < limit;) {
            out.writeObject(eleL[i++]);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        final int limit = this.size;
        InternalCollectionUtil.SerialCheck.ioCheckSize(limit, 0, Container.MAXIMUM_CAPACITY);
        final E[] eleL = this.element = this.newTable(limit);
        for (int i = 0; i < limit;) {
            eleL[i++] = (E) in.readObject();
        }
    }

    protected class CollectionIterator implements Iterator<E> {
        protected int posPre = -1;
        protected int pos = 0;

        public boolean hasNext() {
            return this.pos < ObjArrayList.this.size;
        }

        public E next() {
            if (this.pos >= ObjArrayList.this.size) throw new NoSuchElementException();
            final int currPos = this.pos++;
            return ObjArrayList.this.element[this.posPre = currPos];
        }

        public void remove() {
            final int posPreL;
            if ((posPreL = this.posPre) < 0) throw new IllegalStateException();
            ObjArrayList.this.removeAt(posPreL);
            this.pos = posPreL;
            this.posPre = -1;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final int limit = ObjArrayList.this.size;
            final E[] eleL = ObjArrayList.this.element;
            for (int i = this.pos; i < limit;) {
                action.accept(eleL[i++]);
            }
            this.posPre = -1;
            this.pos = limit;
        }
    }

    protected class CollectionListIterator extends CollectionIterator implements ListIterator<E> {
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

        public E previous() {
            if (this.pos < 1) throw new NoSuchElementException();
            final int currPos = --this.pos;
            return ObjArrayList.this.element[this.posPre = currPos];
        }

        public void set(E e) {
            ObjArrayList.this.set(this.posPre, e);
        }

        public void add(E e) {
            ObjArrayList.this.add(this.pos++, e);
            this.posPre = -1;
        }
    }

    protected class CollectionSpliterator implements Spliterator<E> {
        protected int index;
        protected int limit;

        protected CollectionSpliterator(int index, int limit) {
            this.index = index;
            this.limit = limit;
        }

        private int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limit) < 0) {
                newLimit = this.limit = ObjArrayList.this.size;
            }
            return newLimit;
        }

        public Spliterator<E> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex ? new CollectionSpliterator(newIndex, this.index = midIndex) : null;
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            final int currIndex;
            if ((currIndex = this.index) < this.getLimitCap()) {
                action.accept(ObjArrayList.this.element[currIndex]);
                this.index++;
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final int currLimit = this.getLimitCap();
            final E[] eleL = ObjArrayList.this.element;
            for (int i = this.index; i < currLimit; i++) {
                action.accept(eleL[i]);
            }
            this.index = currLimit;
        }

        public long estimateSize() {
            return this.getLimitCap() - this.index;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

    protected class ObjSubArrayList extends AbstractObjList<E> implements RandomAccess {
        protected int size;
        protected final int limit;
        protected final int offset;
        protected final ObjSubArrayList from;

        protected ObjSubArrayList(int fromIdx, int toIdx) {
            this.size = toIdx - fromIdx;
            this.limit = toIdx;
            this.offset = fromIdx;
            this.from = null;
        }

        protected ObjSubArrayList(ObjSubArrayList from, int fromIdx, int toIdx) {
            this.size = toIdx - fromIdx;
            this.limit = from.offset + toIdx;
            this.offset = from.offset + fromIdx;
            this.from = from;
        }

        protected E[] rootArray() {
            return ObjArrayList.this.element;
        }

        public void shrink() {
            throw new UnsupportedOperationException();
        }

        private void updateSize(int changed) {
            if (changed == 0) return;
            ObjSubArrayList pre = this;
            do {
                pre.size += changed;
            } while ((pre = pre.from) != null);
        }

        public E get(int index) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            return ObjArrayList.this.get(index + this.offset);
        }

        public E set(int index, E element) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            return ObjArrayList.this.set(index + this.offset, element);
        }

        public boolean add(E e) {
            ObjArrayList.this.add(this.size + this.offset, e);
            this.updateSize(1);
            return true;
        }

        public void add(int index, E element) {
            InternalCollectionUtil.checkIndex(index, 0, this.size);
            ObjArrayList.this.add(index + this.offset, element);
            this.updateSize(1);
        }

        public E removeAt(int index) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            final E result = ObjArrayList.this.removeAt(index + this.offset);
            this.updateSize(-1);
            return result;
        }

        public boolean removeElement(Object o) {
            final int limit = this.size + this.offset;
            final E[] eleL = this.rootArray();
            for (int i = this.offset; i < limit; i++) {
                if (Objects.equals(o, eleL[i])) {
                    ObjArrayList.this.removeAndShift(eleL, i, i + 1, 1);
                    this.updateSize(-1);
                    return true;
                }
            }
            return false;
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
                if (Objects.equals(o, eleL[i])) return i - this.offset;
            }
            return -1;
        }

        public int lastIndexOf(Object o) {
            final int limit = this.size + this.offset, min = this.offset - 1;
            final E[] eleL = this.rootArray();
            for (int i = limit - 1; i > min; i--) {
                if (Objects.equals(o, eleL[i])) return i - this.offset;
            }
            return -1;
        }

        public E update(int index, Function<? super E, ? extends E> updateFunction) {
            InternalCollectionUtil.checkBound(index, 0, this.size);
            return ObjArrayList.this.update(index + this.offset, updateFunction);
        }

        public void swapAt(int fromIndex, int toIndex) {
            final int sizeL;
            InternalCollectionUtil.checkBound(fromIndex, 0, sizeL = this.size);
            InternalCollectionUtil.checkBound(toIndex, 0, sizeL);
            ObjArrayList.this.swapAt(fromIndex + this.offset, toIndex + this.offset);
        }

        public void erase(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            ObjArrayList.this.erase(fromIndex + this.offset, toIndex + this.offset);
            this.updateSize(fromIndex - toIndex);
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

        public void replaceAll(UnaryOperator<E> operator) {
            final int limit = this.size + this.offset;
            final E[] eleL = this.rootArray();
            for (int i = this.offset; i < limit; i++) {
                eleL[i] = operator.apply(eleL[i]);
            }
        }

        public void replace(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            final int limit = toIndex + this.offset;
            final E[] eleL = this.rootArray();
            for (int i = fromIndex + this.offset; i < limit; i++) {
                eleL[i] = operator.apply(eleL[i]);
            }
        }

        public boolean compute(int fromIndex, int toIndex, Function<? super E, ? extends E> operator) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            final int changed = ObjArrayList.this.in_computeRange(fromIndex + this.offset, toIndex + this.offset, operator);
            this.updateSize(-changed);
            return changed != 0;
        }

        public boolean addAll(Collection<? extends E> c) {
            return this.addAll(0, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            InternalCollectionUtil.checkIndex(index, 0, this.size);
            final int size;
            if ((size = c.size()) < 1) return false;
            if (ObjArrayList.this.addAll(index + this.offset, c)) {
                this.updateSize(size);
                return true;
            }
            return false;
        }

        public <T extends E> boolean addAll(T[] elements) {
            return this.addAll(elements, 0, elements.length);
        }

        public <T extends E> boolean addAll(T[] elements, int srcIndex, int length) {
            return this.addAll(0, elements, srcIndex, length);
        }

        public <T extends E> boolean addAll(int dstIndex, T[] elements, int srcIndex, int length) {
            InternalCollectionUtil.checkIndex(dstIndex, 0, this.size);
            if (ObjArrayList.this.addAll(dstIndex + this.offset, elements, srcIndex, length)) {
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
            if (c == this || c == ObjArrayList.this) {
                final int oldSize = this.size;
                ObjArrayList.this.removeAndShift(this.rootArray(), this.offset, this.size + this.offset, this.size);
                this.updateSize(-this.size);
                return oldSize != this.size;
            }

            int idx = this.offset;
            final int limit = this.size + this.offset;
            final E[] eleL = this.rootArray();
            for (int i = this.offset; i < limit; i++) {
                final E currEle;
                if (c.contains(currEle = eleL[i])) eleL[idx++] = currEle;
            }
            Arrays.fill(eleL, idx, limit, ((E) NULL_SLOT));
            final boolean result = idx != limit;
            this.updateSize(idx - limit);
            return result;
        }

        public boolean retainAll(Collection<?> c) {
            if (c == this || c == ObjArrayList.this || this.isEmpty()) return false;

            int idx = this.offset;
            final int limit = this.size + this.offset;
            final E[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                final E currEle;
                if (c.contains(currEle = eleL[i++])) eleL[idx++] = currEle;
            }
            Arrays.fill(eleL, idx, limit, ((E) NULL_SLOT));
            final boolean result = idx != limit;
            this.updateSize(idx - limit);
            return result;
        }

        public boolean removeIf(Predicate<? super E> filter) {
            if (this.isEmpty()) return false;

            int idx = this.offset;
            final int limit = this.size + this.offset;
            final E[] eleL = this.rootArray();
            for (int i = this.offset; i < limit;) {
                final E currEle;
                if (!filter.test(currEle = eleL[i++])) eleL[idx++] = currEle;
            }
            Arrays.fill(eleL, idx, limit, ((E) NULL_SLOT));
            final boolean result = idx != limit;
            this.updateSize(idx - limit);
            return result;
        }

        public ListIterator<E> listIterator() {
            return new CollectionSubListIterator(0);
        }

        public ListIterator<E> listIterator(int index) {
            InternalCollectionUtil.checkIndex(index, 0, this.size);
            return new CollectionSubListIterator(index);
        }

        public ObjList<E> subList(int fromIndex, int toIndex) {
            InternalCollectionUtil.checkRange(fromIndex, toIndex, 0, this.size);
            return new ObjSubArrayList(this, fromIndex, toIndex);
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.size < 1;
        }

        public Iterator<E> iterator() {
            return new CollectionSubIterator();
        }

        public Spliterator<E> spliterator() {
            return new CollectionSubSpliterator(0, -1);
        }

        public void clear() {
            if (this.isEmpty()) return;
            final int sizeL;
            Arrays.fill(this.rootArray(), this.offset, (sizeL = this.size) + this.offset, ((E) NULL_SLOT));
            ObjArrayList.this.size -= sizeL;
            this.updateSize(-sizeL);
        }

        public void clear(int newCapacity) {
            throw new UnsupportedOperationException();
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
                final List<?> castList = (List<?>) o;
                if (castList.isEmpty()) return this.isEmpty();
                if (castList.size() != this.size()) return false;
                try {
                    if (castList instanceof ObjArrayList<?>) {
                        final ObjArrayList<?> cast = (ObjArrayList<?>) castList;
                        return InternalCollectionUtil.arrayEquals(this.rootArray(), this.offset, this.size, cast.element, 0, cast.size);
                    } else if (castList instanceof ObjArrayList<?>.ObjSubArrayList) {
                        final ObjArrayList<?>.ObjSubArrayList cast = (ObjArrayList<?>.ObjSubArrayList) castList;
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
                sb.append((currEle =eleL[i++]) != this ? currEle : "(this Collection)");
            }
            sb.append(']');
            return sb.toString();
        }

        protected class CollectionSubIterator implements Iterator<E> {
            protected int posPre = -1;
            protected int pos = 0;

            public boolean hasNext() {
                return this.pos < ObjSubArrayList.this.size;
            }

            public E next() {
                if (this.pos >= ObjSubArrayList.this.size) throw new NoSuchElementException();
                final int currPos = this.pos++;
                return ObjSubArrayList.this.rootArray()[(this.posPre = currPos) + ObjSubArrayList.this.offset];
            }

            public void remove() {
                final int posPreL;
                if ((posPreL = this.posPre) < 0) throw new IllegalStateException();
                ObjSubArrayList.this.removeAt(posPreL);
                this.pos = posPreL;
                this.posPre = -1;
            }

            public void forEachRemaining(Consumer<? super E> action) {
                final int rawLimit = ObjSubArrayList.this.size,  limit = rawLimit + ObjSubArrayList.this.offset;
                final E[] eleL = ObjSubArrayList.this.rootArray();
                for (int i = this.pos + ObjSubArrayList.this.offset; i < limit;) {
                    action.accept(eleL[i++]);
                }
                this.posPre = -1;
                this.pos = rawLimit;
            }
        }

        protected class CollectionSubListIterator extends CollectionSubIterator implements ListIterator<E> {
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

            public E previous() {
                if (this.pos < 1) throw new NoSuchElementException();
                final int currPos = --this.pos;
                return ObjSubArrayList.this.rootArray()[(this.posPre = currPos) + ObjSubArrayList.this.offset];
            }

            public void set(E e) {
                ObjSubArrayList.this.set(this.posPre, e);
            }

            public void add(E e) {
                ObjSubArrayList.this.add(this.pos++, e);
                this.posPre = -1;
            }
        }

        protected class CollectionSubSpliterator implements Spliterator<E> {
            protected int index;
            protected int limit;

            protected CollectionSubSpliterator(int index, int limit) {
                this.index = index;
                this.limit = limit;
            }

            private int getLimitCap() {
                int newLimit;
                if ((newLimit = this.limit) < 0) {
                    newLimit = this.limit = ObjSubArrayList.this.size;
                }
                return newLimit;
            }

            public Spliterator<E> trySplit() {
                final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
                return (newIndex >= midIndex) ? null : new CollectionSubSpliterator(newIndex, this.index = midIndex);
            }

            public boolean tryAdvance(Consumer<? super E> action) {
                final int currIndex;
                if ((currIndex = this.index) < this.getLimitCap()) {
                    action.accept(ObjSubArrayList.this.rootArray()[currIndex + ObjSubArrayList.this.offset]);
                    this.index++;
                    return true;
                }
                return false;
            }

            public void forEachRemaining(Consumer<? super E> action) {
                final int rawLimit = this.getLimitCap(), currLimit = rawLimit + ObjSubArrayList.this.offset;
                final E[] eleL = ObjSubArrayList.this.rootArray();
                for (int i = this.index + ObjSubArrayList.this.offset; i < currLimit; i++) {
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
