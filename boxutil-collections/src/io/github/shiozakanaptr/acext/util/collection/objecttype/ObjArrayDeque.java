package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.QueueExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjDeque;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "RedundantCast"})
public class ObjArrayDeque<E> extends AbstractObjCollection<E> implements ObjDeque<E>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static Object NULL_SLOT = null;

    protected transient int head = 0;
    protected transient int tail = 0;
    protected final E nullElement;
    protected transient E[] element;

    protected E[] newTable(int cap) {
        return (E[]) new Object[cap];
    }

    public ObjArrayDeque(int initialCapacity, E nullElement) {
        InternalCollectionUtil.checkCapacity(initialCapacity);
        this.nullElement = nullElement;
        this.element = this.newTable(Math.max(initialCapacity, 1));
    }

    public ObjArrayDeque(int initialCapacity) {
        this(initialCapacity, ((E) NULL_SLOT));
    }

    public ObjArrayDeque() {
        this(QueueExt.DEFAULT_INITIAL_CAPACITY, ((E) NULL_SLOT));
    }

    public ObjArrayDeque(final Collection<? extends E> source) {
        this(source.size() + 1);
        this.addAll(source);
    }

    public <T extends E> ObjArrayDeque(final T[] source) {
        this(source.length + 1);
        this.addAll(source);
    }

    protected void in_resize(int newCapacity, int oldSize) {
        final E[] newEle = this.newTable(newCapacity);
        if (oldSize > 0) {
            final int oldHead, oldTail;
            final E[] oldEle = this.element;
            if ((oldTail = this.tail) > (oldHead = this.head)) {
                System.arraycopy(oldEle, oldHead, newEle, 0, oldSize);
            } else {
                final int diff;
                System.arraycopy(oldEle, oldHead, newEle, 0, diff = (oldEle.length - oldHead));
                System.arraycopy(oldEle, 0, newEle, diff, oldTail);
            }
            this.tail = oldSize;
            this.head = 0;
        }
        this.element = newEle;
    }

    public void resize(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int oldSize, newCap;
        InternalCollectionUtil.checkCapacity(newCap = Math.max((oldSize = this.size()) + 1, newCapacity));
        if (newCap == this.element.length) return;
        this.in_resize(newCap, oldSize);
    }

    public void reserve(int newMaxSize) {
        if (newMaxSize < 0) throw new IllegalArgumentException("Not a valid size: '" + newMaxSize + '\'');
        this.resize(newMaxSize);
    }

    public void shrink() {
        this.resize(1);
    }

    protected void checkExpand(int currHead, int currTail) {
        if (currHead != currTail) return;
        final int end;
        this.in_resize(InternalCollectionUtil.expandArray(end = this.element.length), end);
    }

    protected static int preIdx(int ptr, int length) {
        return (ptr == 0 ? length : ptr) - 1;
    }

    protected static int nextIdx(int ptr, int length) {
        final int nextPtr;
        return ((nextPtr = ptr + 1) == length ? 0 : nextPtr);
    }

    protected static int rangeSize(int to, int from, int length) {
        final int dist;
        return (dist = to - from) < 0 ? dist + length : dist;
    }

    protected void removeAndForwardShift(final E[] eleL, int pos, int end, int currHead) {
        if (pos < currHead) {
            System.arraycopy(eleL, 0, eleL, 1, pos);
            eleL[0] = eleL[end - 1];
            final int nextHead;
            System.arraycopy(eleL, currHead, eleL, nextHead = currHead + 1, end - nextHead);
        } else {
            System.arraycopy(eleL, currHead, eleL, currHead + 1, pos - currHead);
        }
        eleL[currHead] = ((E) NULL_SLOT);
        this.head = nextIdx(currHead, end);
    }

    protected void removeAndBackwardShift(final E[] eleL, int pos, int end, int currTail) {
        if (pos < currTail) {
            final int nextPos;
            System.arraycopy(eleL, nextPos = pos + 1, eleL, pos, currTail - nextPos);
        } else {
            final int nextPos;
            System.arraycopy(eleL, nextPos = pos + 1, eleL, pos, end - nextPos);
            eleL[end - 1] = eleL[0];
            if (currTail > 1) System.arraycopy(eleL, 1, eleL, 0, currTail - 1);
        }
        eleL[this.tail = preIdx(currTail, end)] = ((E) NULL_SLOT);
    }

    protected boolean removeAndShift(final E[] eleL, int pos, int end, int currHead, int currTail) {
        if (rangeSize(pos, currHead, end) < rangeSize(currTail, pos, end)) {
            this.removeAndForwardShift(eleL, pos, end, currHead);
            return false;
        } else {
            this.removeAndBackwardShift(eleL, pos, end, currTail);
            return true;
        }
    }

    protected boolean removeAndShift(final E[] eleL, int pos, int end) {
        return this.removeAndShift(eleL, pos, end, this.head, this.tail);
    }

    public E peekFirst() {
        final int headL;
        return (headL = this.head) == this.tail ? this.nullElement : this.element[headL];
    }

    public E peekLast() {
        final int tailL;
        final E[] eleL;
        return (tailL = this.tail) == this.head ? this.nullElement : (eleL = this.element)[preIdx(tailL, eleL.length)];
    }

    public E getFirst() {
        final int headL;
        if ((headL = this.head) == this.tail) throw new NoSuchElementException();
        return this.element[headL];
    }

    public E getLast() {
        final int tailL;
        if (this.head == (tailL = this.tail)) throw new NoSuchElementException();
        final E[] eleL;
        return (eleL = this.element)[preIdx(tailL, eleL.length)];
    }

    public boolean add(E e) {
        this.addLast(e);
        return true;
    }

    public void addFirst(E e) {
        if (e == this.nullElement) throw new IllegalArgumentException("Illegal element '"  + e + "' that using for null element");
        final int currHead;
        final E[] eleL;
        (eleL = this.element)[currHead = preIdx(this.head, eleL.length)] = e;
        this.checkExpand(this.head = currHead, this.tail);
    }

    public void addLast(E e) {
        if (e == this.nullElement) throw new IllegalArgumentException("Illegal element '"  + e + "' that using for null element");
        final int currTail;
        final E[] eleL;
        (eleL = this.element)[currTail = this.tail] = e;
        this.checkExpand(this.head, this.tail = nextIdx(currTail, eleL.length));
    }

    public E pollFirst() {
        final int currHead;
        if ((currHead = this.head) == this.tail) return this.nullElement;
        final E[] eleL;
        this.head = nextIdx(currHead, (eleL = this.element).length);
        final E result = eleL[currHead];
        eleL[currHead] = ((E) NULL_SLOT);
        return result;
    }

    public E pollLast() {
        final int currTail;
        if (this.head == (currTail = this.tail)) return this.nullElement;
        final E[] eleL;
        final int lastTail;
        final E result = (eleL = this.element)[lastTail = this.tail = preIdx(currTail, eleL.length)];
        eleL[lastTail] = ((E) NULL_SLOT);
        return result;
    }

    public E removeFirst() {
        final int currHead;
        if ((currHead = this.head) == this.tail) throw new NoSuchElementException();
        final E[] eleL;
        this.head = nextIdx(currHead, (eleL = this.element).length);
        final E result = eleL[currHead];
        eleL[currHead] = ((E) NULL_SLOT);
        return result;
    }

    public E removeLast() {
        final int currTail;
        if (this.head == (currTail = this.tail)) throw new NoSuchElementException();
        final E[] eleL;
        final int lastTail;
        final E result = (eleL = this.element)[lastTail = this.tail = preIdx(currTail, eleL.length)];
        eleL[lastTail] = ((E) NULL_SLOT);
        return result;
    }

    public boolean removeElement(Object o) {
        return this.removeFirstOccurrence(o);
    }

    public boolean removeFirstOccurrence(Object o) {
        if (o == this.nullElement) return false;
        final E[] eleL = this.element;
        final int rawHead, currTail = this.tail, end = eleL.length;
        for (int currHead = rawHead = this.head; currHead != currTail;) {
            if (o == eleL[currHead++]) {
                this.removeAndShift(eleL, currTail, end, rawHead, currTail);
                return true;
            }
            if (currHead == end) currHead = 0;
        }
        return false;
    }

    public boolean removeLastOccurrence(Object o) {
        if (o == this.nullElement) return false;
        final E[] eleL = this.element;
        final int currHead = this.head, rawTail, end = eleL.length;
        for (int currTail = rawTail = this.tail; currTail != currHead;) {
            if (o == eleL[currTail = preIdx(currTail, end)]) {
                this.removeAndShift(eleL, currTail, end, currHead, rawTail);
                return true;
            }
        }
        return false;
    }

    public boolean contains(Object o) {
        if (o == this.nullElement) return false;
        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        for (int currHead = this.head; currHead != currTail;) {
            if (o == eleL[currHead++]) return true;
            if (currHead == end) currHead = 0;
        }
        return false;
    }

    public boolean tryPollExecuteOffer(Consumer<? super E> action) {
        final int currHead, currTail;
        if ((currHead = this.head) == (currTail = this.tail)) return false;
        final E[] eleL;
        final int end;
        action.accept((eleL = this.element)[currTail] = eleL[currHead]);
        eleL[currHead] = ((E) NULL_SLOT);
        this.head = nextIdx(currHead, end = eleL.length);
        this.tail = nextIdx(currTail, end);
        return true;
    }

    public boolean tryPollExecuteOfferReversed(Consumer<? super E> action) {
        final int currHead, currTail;
        if ((currHead = this.head) == (currTail = this.tail)) return false;
        final E[] eleL;
        final int end, lastTail;
        action.accept((eleL = this.element)[this.head = preIdx(currHead, end = eleL.length)] = eleL[lastTail = this.tail = preIdx(currTail, end)]);
        eleL[lastTail] = ((E) NULL_SLOT);
        return true;
    }

    public void swap(Queue<E> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ObjArrayDeque) {
            final ObjArrayDeque<E> src = (ObjArrayDeque<E>) source;
            final int currHead = this.head, currTail = this.tail, srcHead = src.head, srcTail = src.tail;
            final E[] currEle = this.element, srcEle = src.element;

            src.head = currHead;
            src.tail = currTail;
            src.element = currEle;
            this.head = srcHead;
            this.tail = srcTail;
            this.element = srcEle;
        } else {
            if (source.size() < this.size()) {
                final Deque<E> tmp = new ObjArrayDeque<>(source);
                source.clear();
                source.addAll(this);
                this.clear();
                this.addAll(tmp);
            } else {
                final Deque<E> tmp = new ObjArrayDeque<>(this);
                this.clear();
                this.addAll(source);
                source.clear();
                source.addAll(tmp);
            }
        }
    }

    public void swap(Deque<E> source) {
        this.swap((Queue<E>) source);
    }

    public void forEach(Consumer<? super E> action) {
        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        for (int currHead = this.head; currHead != currTail;) {
            action.accept(eleL[currHead++]);
            if (currHead == end) currHead = 0;
        }
    }

    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) return false;
        if (c instanceof ObjArrayDeque<?>) {
            final ObjArrayDeque<?> src = (ObjArrayDeque<?>) c;
            if (src.nullElement == this.nullElement) {
                final E[] oldEle = this.element, srcELe = c == this ? Arrays.copyOf(this.element, oldEle.length) : (E[]) src.element;
                final int oldSize, srcSize, totalSize = (oldSize = this.size()) + (srcSize = src.size()), minLength = totalSize + 1, oldHead;
                InternalCollectionUtil.checkCapacity(minLength);
                if ((oldHead = this.head) != 0 || minLength > oldEle.length) {
                    final E[] newEle = this.newTable(Math.max(minLength, oldEle.length));
                    final int oldTail;
                    if ((oldTail = this.tail) > oldHead) {
                        System.arraycopy(oldEle, oldHead, newEle, 0, oldSize);
                    } else {
                        final int diff;
                        System.arraycopy(oldEle, oldHead, newEle, 0, diff = (oldEle.length - oldHead));
                        System.arraycopy(oldEle, 0, newEle, diff, oldTail);
                    }
                    this.element = newEle;
                }

                final E[] currEle = this.element;
                final int srcHead, srcTail;
                if ((srcTail = src.tail) > (srcHead = src.head)) {
                    System.arraycopy(srcELe, srcHead, currEle, oldSize, srcSize);
                } else {
                    final int diff;
                    System.arraycopy(srcELe, srcHead, currEle, oldSize, diff = (srcELe.length - srcHead));
                    System.arraycopy(srcELe, 0, currEle, diff + oldSize, srcTail);
                }
                this.head = 0;
                this.tail = totalSize;
                return true;
            }
        }

        this.resize(this.size() + c.size());
        c.forEach(this::addLast);
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();

        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        for (int currHead = this.head; currHead != currTail;) {
            if (!c.contains(eleL[currHead++])) return false;
            if (currHead == end) currHead = 0;
        }
        return true;
    }

    protected void removeRange(final E[] eleL, int ptr, int currTail, int end) {
        if (ptr != currTail) {
            if (ptr < currTail) {
                Arrays.fill(eleL, ptr, currTail, ((E) NULL_SLOT));
            } else {
                Arrays.fill(eleL, ptr, end, ((E) NULL_SLOT));
                Arrays.fill(eleL, 0, currTail, ((E) NULL_SLOT));
            }
        }
    }

    public boolean removeAll(Collection<?> c) {
        if (this.isEmpty()) return false;
        if (c == this) {
            this.head = this.tail = 0;
            return true;
        }

        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        int ptr;
        for (int currHead = ptr = this.head; currHead != currTail;) {
            final E currEle;
            if (!c.contains(currEle = eleL[currHead])) {
                eleL[ptr++] = currEle;
                if (ptr == end) ptr = 0;
            }
            if (++currHead == end) currHead = 0;
        }

        this.removeRange(eleL, ptr, currTail, end);
        this.tail = ptr;
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        if (c == this || this.isEmpty()) return false;

        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        int ptr;
        for (int currHead = ptr = this.head; currHead != currTail;) {
            final E currEle;
            if (c.contains(currEle = eleL[currHead])) {
                eleL[ptr++] = currEle;
                if (ptr == end) ptr = 0;
            }
            if (++currHead == end) currHead = 0;
        }

        this.removeRange(eleL, ptr, currTail, end);
        this.tail = ptr;
        return true;
    }

    public boolean removeIf(Predicate<? super E> filter) {
        if (this.isEmpty()) return false;

        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        int ptr;
        for (int currHead = ptr = this.head; currHead != currTail;) {
            final E currEle;
            if (!filter.test(currEle = eleL[currHead])) {
                eleL[ptr++] = currEle;
                if (ptr == end) ptr = 0;
            }
            if (++currHead == end) currHead = 0;
        }

        this.removeRange(eleL, ptr, currTail, end);
        this.tail = ptr;
        return true;
    }

    public E getNullElement() {
        return this.nullElement;
    }

    public int capacity() {
        return this.element.length;
    }

    public int remaining() {
        final int dist;
        return (dist = this.tail - this.head) < 0 ? -dist : this.element.length - dist;
    }

    public int size() {
        return rangeSize(this.tail, this.head, this.element.length);
    }

    public boolean isEmpty() {
        return this.head == this.tail;
    }

    public Iterator<E> iterator() {
        return new DequeIterator();
    }

    public Iterator<E> descendingIterator() {
        return new DequeDescendingIterator();
    }

    public Spliterator<E> spliterator() {
        return new DequeSpliterator(0, -1);
    }

    public int getHead() {
        return this.head;
    }

    public int getTail() {
        return this.tail;
    }

    public E[] table() {
        return this.element;
    }

    public void clear() {
        final int currTail;
        int currHead;
        if ((currHead = this.head) != (currTail = this.tail)) {
            final E[] eleL = this.element;
            final int end = eleL.length;
            while (currHead != currTail) {
                eleL[currHead++] = ((E) NULL_SLOT);
                if (currHead == end) currHead = 0;
            }
        }
        this.head = this.tail = 0;
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        if (newCapacity == this.capacity()) {
            this.clear();
        } else {
            this.element = this.newTable(newCapacity + 1);
            this.head = this.tail = 0;
        }
    }

    public boolean contentEquals(Collection<?> source) {
        if (source == this) return true;

        if (source instanceof Deque<?>) {
            final Deque<?> castDeque = (Deque<?>) source;
            if (castDeque.size() != this.size()) return false;
            try {
                final E[] currEleL = this.element;
                final int currTail = this.tail, currEnd = currEleL.length;
                if (castDeque instanceof ObjArrayDeque<?>) {
                    final ObjArrayDeque<?> cast = (ObjArrayDeque<?>) castDeque;
                    final Object[] srcEleL = cast.element;
                    final int srcEnd = srcEleL.length;
                    for (int currHead = this.head, srcHead = cast.head; currHead != currTail;) {
                        if (!Objects.equals(currEleL[currHead++], srcEleL[srcHead++])) return false;
                        if (currHead == currEnd) currHead = 0;
                        if (srcHead == srcEnd) srcHead = 0;
                    }
                } else {
                    final Iterator<?> srcIt = castDeque.iterator();
                    for (int currHead = this.head; currHead != currTail;) {
                        if (!(srcIt.hasNext() && Objects.equals(currEleL[currHead++], srcIt.next()))) return false;
                        if (currHead == currEnd) currHead = 0;
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
        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        for (int currHead = this.head; currHead != currTail;) {
            if (sb.length() > 1) sb.append(',').append(' ');
            final E currEle;
            sb.append((currEle = eleL[currHead++]) != this ? currEle : "(this Collection)");
            if (currHead == end) currHead = 0;
        }
        sb.append(']');
        return sb.toString();
    }

    public Object clone() {
        ObjArrayDeque<E> result;
        try {
            result = (ObjArrayDeque<E>) super.clone();
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
        out.writeInt(this.size());

        final E[] eleL = this.element;
        final int currTail = this.tail, end = eleL.length;
        for (int currHead = this.head; currHead != currTail;) {
            out.writeObject(eleL[currHead++]);
            if (currHead == end) currHead = 0;
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final int size = in.readInt();
        InternalCollectionUtil.SerialCheck.ioCheckSize(size, 0, Container.MAXIMUM_CAPACITY);
        final E[] eleL = this.element = this.newTable(size + (size >>> 1) + 1);
        this.head = 0;
        this.tail = size;

        for (int i = 0; i < size;) {
            eleL[i++] = (E) in.readObject();
        }
    }

    protected class DequeIterator implements Iterator<E> {
        protected int itHead;
        protected int itTail;
        protected int pos = -1;

        protected DequeIterator() {
            this.itHead = ObjArrayDeque.this.head;
            this.itTail = ObjArrayDeque.this.tail;
        }

        public E next() {
            final int headL;
            if ((headL = this.itHead) == this.itTail) throw new NoSuchElementException();
            final E[] eleL;
            this.itHead = nextIdx(headL, (eleL = ObjArrayDeque.this.element).length);
            return eleL[this.pos = headL];
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final E[] eleL = ObjArrayDeque.this.element;
            final int currTail = this.itTail, end = eleL.length;
            int currHead, findPre = this.pos;
            for (currHead = this.itHead; currHead != currTail;) {
                action.accept(eleL[findPre = currHead++]);
                if (currHead == end) currHead = 0;
            }
            this.pos = findPre;
            this.itHead = currHead;
        }

        public boolean hasNext() {
            return this.itHead != this.itTail;
        }

        public void remove() {
            final int posL;
            if ((posL = this.pos) < 0) throw new IllegalStateException();
            final E[] eleL;
            final int end;
            if (ObjArrayDeque.this.removeAndShift(eleL = ObjArrayDeque.this.element, posL, end = eleL.length)) {
                this.itTail = preIdx(this.itTail, end);
            }
            this.pos = -1;
        }
    }

    protected class DequeDescendingIterator implements Iterator<E> {
        protected int itHead;
        protected int itTail;
        protected int pos = -1;

        protected DequeDescendingIterator() {
            this.itHead = ObjArrayDeque.this.head;
            this.itTail = ObjArrayDeque.this.tail;
        }

        public E next() {
            final int tailL;
            if (this.itHead == (tailL = this.itTail)) throw new NoSuchElementException();
            final E[] eleL;
            return (eleL = ObjArrayDeque.this.element)[this.itTail = this.pos = preIdx(tailL, eleL.length)];
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final E[] eleL = ObjArrayDeque.this.element;
            final int currHead = this.itHead, end = eleL.length;
            int currTail, findPre = this.pos;
            for (currTail = this.itTail; currHead != currTail;) {
                action.accept(eleL[findPre = currTail = preIdx(currTail, end)]);
            }
            this.pos = findPre;
            this.itTail = currTail;
        }

        public boolean hasNext() {
            return this.itHead != this.itTail;
        }

        public void remove() {
            final int posL;
            if ((posL = this.pos) < 0) throw new IllegalStateException();
            final E[] eleL;
            final int end;
            if (!ObjArrayDeque.this.removeAndShift(eleL = ObjArrayDeque.this.element, posL, end = eleL.length)) {
                this.itHead = nextIdx(this.itHead, end);
            }
            this.pos = -1;
        }
    }

    protected class DequeSpliterator implements Spliterator<E> {
        protected int itHead;
        protected int itTail;

        protected DequeSpliterator(int head, int tail) {
            this.itHead = head;
            this.itTail = tail;
        }

        public int getItTail() {
            int tailL;
            if ((tailL = this.itTail) < 0) {
                this.itHead = ObjArrayDeque.this.head;
                tailL = this.itTail = ObjArrayDeque.this.tail;
            }
            return tailL;
        }

        public Spliterator<E> trySplit() {
            final int headL, end = ObjArrayDeque.this.element.length;
            final int midSize, midIndex;
            return (midSize = rangeSize(this.getItTail(), headL = this.itHead, end) >>> 1) > 0 ?
                    new DequeSpliterator(headL, this.itHead = (midIndex = midSize + headL) < end ? midIndex : midIndex - end) : null;
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            int currHead;
            if (this.getItTail() != (currHead = this.itHead)) {
                final E[] eleL;
                action.accept((eleL = ObjArrayDeque.this.element)[currHead++]);
                if (currHead == eleL.length) currHead = 0;
                this.itHead = currHead;
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final E[] eleL = ObjArrayDeque.this.element;
            final int currTail = this.getItTail(), end = eleL.length;
            int currHead;
            for (currHead = this.itHead; currHead != currTail;) {
                action.accept(eleL[currHead++]);
                if (currHead == end) currHead = 0;
            }
            this.itHead = currHead;
        }

        public long estimateSize() {
            return rangeSize(this.getItTail(), this.itHead, ObjArrayDeque.this.element.length);
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL;
        }
    }
}
