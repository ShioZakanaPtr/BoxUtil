package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.collection.HashBehaviorFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked", "UnusedReturnValue", "unused", "NullableProblems", "StatementWithEmptyBody"})
public class ObjSeparateChainingHashSet<E> extends AbstractObjSet<E> implements HashContainer<E>, Cloneable, Serializable {
    private static final long serialVersionUID = 0L;

    protected transient int remaining = 0;
    protected transient int posMask;
    protected int size = 0;
    protected final float loadFactor;
    protected final HashBehavior<E> hashBehavior;
    protected transient Node<E>[] table;

    protected Node<E>[] newTable(int cap) {
        return (Node<E>[]) new Node<?>[cap];
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 2.0f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjSeparateChainingHashSet(int initialCapacity, float loadFactor, final HashBehavior<E> hashBehavior) {
        InternalCollectionUtil.checkHashCapacity(initialCapacity);
        InternalCollectionUtil.checkHashLoadFactor(loadFactor, 0.2f, 2.0f);

        final int capacity = InternalCollectionUtil.getCapacityPOT(Math.max(initialCapacity, 2));
        this.posMask = capacity - 1;
        this.loadFactor = loadFactor;
        this.hashBehavior = Objects.requireNonNull(hashBehavior);
        this.table = this.newTable(capacity);
        this.calculateRemaining();
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjSeparateChainingHashSet(int initialCapacity, final HashBehavior<E> hashBehavior) {
        this(initialCapacity, 0.75f, hashBehavior);
    }

    /**
     * @param initialCapacity will auto conversion to <code>2^n</code> that greater than or equal to the value.
     */
    public ObjSeparateChainingHashSet(int initialCapacity) {
        this(initialCapacity, HashContainer.DEFAULT_LOAD_FACTOR, HashBehaviorFactory.fibonacci());
    }

    public ObjSeparateChainingHashSet() {
        this(HashContainer.DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 2.0f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjSeparateChainingHashSet(final Collection<? extends E> source, float loadFactor, final HashBehavior<E> hashBehavior) {
        this(Math.max(source.size(), 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public ObjSeparateChainingHashSet(final Collection<? extends E> source, final HashBehavior<E> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public ObjSeparateChainingHashSet(final Collection<? extends E> source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    /**
     * @param loadFactor typical <b>[0.7f, 0.8f]</b>, range <b>[0.2f, 0.9f]</b>.
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public <T extends E> ObjSeparateChainingHashSet(final T[] source, float loadFactor, final HashBehavior<E> hashBehavior) {
        this(Math.max(source.length, 2), loadFactor, hashBehavior);
        this.addAll(source);
    }

    /**
     * @param hashBehavior should create a new ones from {@link HashBehaviorFactory}.
     */
    public <T extends E> ObjSeparateChainingHashSet(final T[] source, final HashBehavior<E> hashBehavior) {
        this(source, HashContainer.DEFAULT_LOAD_FACTOR, hashBehavior);
    }

    public <T extends E> ObjSeparateChainingHashSet(final T[] source) {
        this(source, HashBehaviorFactory.fibonacci());
    }

    public void resize(int newCapacity) {
        if (newCapacity < 1) throw new IllegalArgumentException("Not a valid size: '" + newCapacity + '\'');
        final int minSize = InternalCollectionUtil.getHashTableCapacity(this.size + 1, this.loadFactor), newTargetCap = InternalCollectionUtil.getCapacityPOT(newCapacity);
        InternalCollectionUtil.checkHashCapacity(newTargetCap);
        if (newTargetCap == this.table.length) return;

        final int newCap = Math.max(minSize, Math.max(newTargetCap, 2)),
                newMask = newCap - 1;
        final Node<E>[] newTable = this.newTable(newCap), oldTable = this.table;

        if (this.size > 0) {
            for (Node<E> currNode : oldTable) {
                while (currNode != null) {
                    final int newPos = currNode.hash & newMask;
                    final Node<E> newNode = currNode;
                    currNode = currNode.next;
                    newNode.next = newTable[newPos];
                    newTable[newPos] = newNode;
                }
            }
        }

        this.posMask = newMask;
        this.table = newTable;
        this.calculateRemaining();
    }

    public void reserve(int newMaxSize) {
        if (newMaxSize < 1) throw new IllegalArgumentException("Not a valid size: '" + newMaxSize + '\'');
        this.resize(InternalCollectionUtil.getHashTableCapacity(newMaxSize, this.loadFactor));
    }

    protected void removeAt(final Node<E>[] tableL, final Node<E> preNode, final Node<E> currNode, int pos) {
        if (preNode != null) preNode.next = currNode.next;
        else tableL[pos] = currNode.next;
        currNode.next = null;
        this.size--;
        this.remaining++;
    }

    protected void checkExpand() {
        if (this.remaining > 0) return;
        this.resize(InternalCollectionUtil.expandHashArray(this.table.length));
    }

    public boolean contains(Object o) {
        final HashBehavior<E> behavior = this.hashBehavior;
        final int hash;
        for (Node<E> currNode = this.table[(hash = behavior.hashing(o)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(o, currNode.element)) return true;
        }
        return false;
    }

    public boolean add(E e) {
        final HashBehavior<E> behavior = this.hashBehavior;
        final Node<E>[] tableL;
        final int hash, pos;
        for (Node<E> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(e)) & this.posMask]; currNode != null; currNode = currNode.next) {
            if (hash == currNode.hash && behavior.equals(e, currNode.element)) return false;
        }
        final Node<E> newNode = new Node<>(hash, e);
        newNode.next = tableL[pos];
        tableL[pos] = newNode;
        this.size++;
        this.remaining--;
        this.checkExpand();
        return true;
    }

    public boolean removeElement(Object o) {
        final HashBehavior<E> behavior = this.hashBehavior;
        final Node<E>[] tableL;
        final int hash, pos;
        for (Node<E> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(o)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(o, currNode.element)) {
                this.removeAt(tableL, preNode, currNode, pos);
                return true;
            }
        }
        return false;
    }

    public boolean remapping(E element, Function<? super E, ? extends E> remappingFunction) {
        final HashBehavior<E> behavior = this.hashBehavior;
        final Node<E>[] tableL;
        final int hash, pos;
        for (Node<E> currNode = (tableL = this.table)[pos = (hash = behavior.hashing(element)) & this.posMask], preNode = null; currNode != null; currNode = (preNode = currNode).next) {
            if (hash == currNode.hash && behavior.equals(element, currNode.element)) {
                final E newEle = remappingFunction.apply(element);
                if (newEle != null && behavior.equals(newEle, element)) return false;
                this.removeAt(tableL, preNode, currNode, pos);
                if (newEle != null) this.add(newEle);
                return true;
            }
        }
        return false;
    }

    public void swap(Set<E> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ObjSeparateChainingHashSet) {
            final ObjSeparateChainingHashSet<E> src = (ObjSeparateChainingHashSet<E>) source;
            if (Objects.equals(src.hashBehavior, this.hashBehavior) && this.loadFactor == src.loadFactor) {
                final int currSize = this.size, srcSize = src.size,
                        currMask = this.posMask, srcMask = src.posMask,
                        currRemaining = this.remaining, srcRemaining = src.remaining;
                final Node<E>[] currTable = this.table, srcTable = src.table;

                src.size = currSize;
                src.posMask = currMask;
                src.remaining = currRemaining;
                src.table = currTable;
                this.size = srcSize;
                this.posMask = srcMask;
                this.remaining = srcRemaining;
                this.table = srcTable;
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

        final Node<E>[] tableL = this.table;
        for (Node<E> currNode : tableL) {
            while (currNode != null) {
                action.accept(currNode.element);
                currNode = currNode.next;
            }
        }
    }

    public boolean addAll(Collection<? extends E> c) {
        if (c == this || c.isEmpty()) return false;
        if (c.size() >= this.remaining()) this.reserve(this.size + c.size() + 1);
        if (c instanceof ObjSeparateChainingHashSet) {
            final ObjSeparateChainingHashSet<E> cast = (ObjSeparateChainingHashSet<E>) c;
            boolean result = false;
            final Node<? extends E>[] srcTableL = cast.table;
            for (Node<? extends E> srcNode : srcTableL) {
                while (srcNode != null) {
                    result |= this.add(srcNode.element);
                    srcNode = srcNode.next;
                }
            }
            return result;
        }
        return super.addAll(c);
    }

    public boolean removeIf(Predicate<? super E> filter) {
        if (this.isEmpty()) return false;
        boolean result = false;
        final int maskL = this.posMask;
        final Node<E>[] tableL = this.table;
        for (Node<E> currNode : tableL) {
            final int currPos = currNode != null ? (currNode.hash & maskL) : 0;
            Node<E> preNode = null;
            while (currNode != null) {
                if (result |= filter.test(currNode.element)) this.removeAt(tableL, preNode, currNode, currPos);
                currNode = (preNode = currNode).next;
            }
        }
        return result;
    }

    private void calculateRemaining() {
        this.remaining = InternalCollectionUtil.getHashTableMaxRemaining(this.table.length, this.loadFactor, true) - this.size;
    }

    public int capacity() {
        return this.table.length;
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

    public Node<E>[] table() {
        return this.table;
    }

    public void clear() {
        if (this.isEmpty()) return;
        this.size = 0;
        Arrays.fill(this.table, null);
        this.calculateRemaining();
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        final int newCap = InternalCollectionUtil.getCapacityPOT(Math.max(newCapacity, 2));
        if (newCap == this.table.length) {
            this.clear();
        } else {
            this.posMask = newCap - 1;
            this.size = 0;
            this.table = this.newTable(newCap);
            this.calculateRemaining();
        }
    }

    public int hashCode() {
        if (this.isEmpty()) return 0;

        int result = 0;
        final Node<E>[] tableL = this.table;
        for (Node<E> currNode : tableL) {
            while (currNode != null) {
                result += Objects.hashCode(currNode.element);
                currNode = currNode.next;
            }
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Set<?>) {
            final Set<?> castSet = (Set<?>) o;
            if (castSet.size() != this.size()) return false;
            try {
                if (castSet instanceof ObjSeparateChainingHashSet<?>) {
                    final ObjSeparateChainingHashSet<?> cast = (ObjSeparateChainingHashSet<?>) castSet;
                    final Node<E>[] tableL = (Node<E>[]) cast.table;
                    for (Node<E> srcNode : tableL) {
                        while (srcNode != null) {
                            if (!this.contains(srcNode.element)) return false;
                            srcNode = srcNode.next;
                        }
                    }
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
        final Node<E>[] tableL = this.table;
        for (Node<E> currNode : tableL) {
            while (currNode != null) {
                if (sb.length() > 1) sb.append(',').append(' ');
                sb.append(currNode.element != this ? currNode.element : "(this Collection)");
                currNode = currNode.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public Object clone() {
        ObjSeparateChainingHashSet<E> result;
        try {
            result = (ObjSeparateChainingHashSet<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final int srcLen = this.table.length;
        result.size = 0;
        result.table = this.newTable(srcLen);
        result.calculateRemaining();
        result.addAll(this);
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.isEmpty()) return;

        final Node<E>[] tableL = this.table;
        for (Node<E> currNode : tableL) {
            while (currNode != null) {
                out.writeObject(currNode.element);
                currNode = currNode.next;
            }
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        InternalCollectionUtil.SerialCheck.ioCheckHashBehavior(this.hashBehavior);
        InternalCollectionUtil.SerialCheck.ioCheckHashLoadFactor(this.loadFactor, 0.2f, 0.9f);
        InternalCollectionUtil.SerialCheck.ioCheckSize(this.size, 0, HashContainer.MAXIMUM_CAPACITY);
        final int capacity = InternalCollectionUtil.SerialCheck.ioGetHashTableCapacity(this.size, this.loadFactor, HashContainer.MAXIMUM_CAPACITY),
                maskL = this.posMask = capacity - 1;
        final Node<E>[] tableL = this.table = this.newTable(capacity);

        final HashBehavior<E> behavior = this.hashBehavior;
        for (int i = this.size; i-- > 0;) {
            final E currEle = (E) in.readObject();

            final int hash = behavior.hashing(currEle), pos = hash & maskL;
            Node<E> checkNode = tableL[pos];
            while (checkNode != null) {
                if (behavior.equals(currEle, checkNode.element)) InternalCollectionUtil.SerialCheck.ioCheckKeyIsDuplicate(currEle);
                checkNode = checkNode.next;
            }

            final Node<E> newNode = new Node<>(hash, currEle);
            newNode.next = tableL[pos];
            tableL[pos] = newNode;
        }
        this.calculateRemaining();
    }

    public static class Node<E> {
        protected final int hash;
        protected final E element;
        public Node<E> next = null;

        protected Node(int hash, E element) {
            this.hash = hash;
            this.element = element;
        }

        public int getHash() {
            return this.hash;
        }

        public E getElement() {
            return this.element;
        }

        public String toString() {
            return Objects.toString(this.element);
        }

        public int hashCode() {
            return Objects.hashCode(this.element);
        }

        public boolean equals(Object obj) {
            if (obj == this) return true;

            if (!(obj instanceof ObjSeparateChainingHashSet.Node<?>)) return false;
            return Objects.equals(this.element, ((ObjSeparateChainingHashSet.Node<?>) obj).element);
        }
    }

    protected class SetIterator implements Iterator<E> {
        protected int pos = 0;
        protected Node<E> preNode = null;
        protected Node<E> currNode = null;
        protected Node<E> nextNode = null;

        protected SetIterator() {
            if (ObjSeparateChainingHashSet.this.size > 0) {
                this.scanNode();
            }
        }

        protected void scanNode() {
            final Node<E>[] tableL = ObjSeparateChainingHashSet.this.table;
            final int capacity = tableL.length;
            int posL = this.pos;
            while (posL < capacity && (this.nextNode = tableL[posL++]) == null) {}
            this.pos = posL;
        }

        public boolean hasNext() {
            return this.nextNode != null;
        }

        public E next() {
            if (this.nextNode == null) throw new NoSuchElementException();

            if (this.currNode != null) this.preNode = this.currNode;
            final Node<E> result = this.nextNode;
            if ((this.nextNode = (this.currNode = result).next) == null) {
                this.scanNode();
            }
            return result.element;
        }

        public void remove() {
            if (this.currNode == null) throw new IllegalStateException();

            final Node<E>[] tableL = ObjSeparateChainingHashSet.this.table;
            final Node<E> preL = this.preNode, currL = this.currNode;
            final int pos = currL.hash & ObjSeparateChainingHashSet.this.posMask;

            if (preL != null && preL.next == currL) {
                preL.next = currL.next;
            } else {
                tableL[pos] = currL.next;
                this.preNode = null;
            }
            currL.next = null;
            ObjSeparateChainingHashSet.this.size--;
            ObjSeparateChainingHashSet.this.remaining++;
            this.currNode = null;
        }
    }

    protected class SetSpliterator implements Spliterator<E> {
        protected int index;
        protected int limitCap;
        protected int estSize;
        protected Node<E> currNode = null;

        protected SetSpliterator(int index, int limitCap, int estSize) {
            this.index = index;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = ObjSeparateChainingHashSet.this.size;
                newLimit = this.limitCap = ObjSeparateChainingHashSet.this.table.length;
            }
            return newLimit;
        }

        public Spliterator<E> trySplit() {
            final int newLimit = this.getLimitCap(), newIndex = this.index, midIndex = (newIndex + newLimit) >>> 1;
            return newIndex < midIndex && this.currNode == null ? new SetSpliterator(newIndex, this.index = midIndex, this.estSize >>>= 1) : null;
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            final int currLimit = this.getLimitCap();
            for (final Node<E>[] tableL = ObjSeparateChainingHashSet.this.table; this.currNode != null || this.index < currLimit;) {
                if (this.currNode == null) this.currNode = tableL[this.index++];
                if (this.currNode != null) {
                    action.accept(currNode.element);
                    this.currNode = this.currNode.next;
                    return true;
                }
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            final int currLimit = this.getLimitCap();
            for (final Node<E>[] tableL = ObjSeparateChainingHashSet.this.table; this.currNode != null || this.index < currLimit;) {
                if (this.currNode == null) this.currNode = tableL[this.index++];
                while (this.currNode != null) {
                    action.accept(currNode.element);
                    this.currNode = this.currNode.next;
                }
            }
        }

        public final long estimateSize() {
            this.getLimitCap();
            return this.estSize;
        }

        public int characteristics() {
            return (this.limitCap < 0 || this.estSize == ObjSeparateChainingHashSet.this.size ? Spliterator.SIZED : 0) | Spliterator.DISTINCT;
        }
    }
}
