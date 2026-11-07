package io.github.shiozakanaptr.acext.util.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteIterator;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSpliterator;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.BytePredicate;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings({"UnusedReturnValue", "unused", "NullableProblems", "deprecation"})
public class ByteBitSet extends AbstractByteSet implements Cloneable, Serializable {
    private static final long serialVersionUID = 0L;
    protected final static long NULL_BIT = (long) 0;

    protected transient int size = 0;
    protected transient long[] bitMark = new long[4];

    public ByteBitSet() {}

    public ByteBitSet(final Collection<? extends Byte> source) {
        this();
        this.addAll(source);
    }

    public ByteBitSet(final byte[] source) {
        this();
        this.addAll(source);
    }

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException("Fixed capacity");
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException("Fixed capacity");
    }

    public void shrink() {
        throw new UnsupportedOperationException("Fixed capacity");
    }

    protected void computeSize() {
        final long[] bitL = this.bitMark;
        this.size = Long.bitCount(bitL[0]) + Long.bitCount(bitL[1]) + Long.bitCount(bitL[2]) + Long.bitCount(bitL[3]);
    }

    public boolean contains(byte o) {
        final int seq;
        return (this.bitMark[(seq = o & 0xff) >>> 6] & 1L << (seq & 0b111111)) != NULL_BIT;
    }

    public boolean add(byte e) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[picker = (seq = e & 0xff) >>> 6] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) return false;
        bitL[picker] |= pos;
        this.size++;
        return true;
    }

    public boolean removeElement(byte o) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[picker = (seq = o & 0xff) >>> 6] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) {
            bitL[picker] &= ~pos;
            this.size--;
            return true;
        }
        return false;
    }

    public boolean remapping(byte element, ByteFunction<? extends Byte> remappingFunction) {
        final long[] bitL;
        final int seq, picker;
        final long pos;
        if (((bitL = this.bitMark)[picker = (seq = element & 0xff) >>> 6] & (pos = 1L << (seq & 0b111111))) != NULL_BIT) {
            final Byte newEle;
            if ((newEle = remappingFunction.apply(element)) != null && element == newEle) return false;
            bitL[picker] &= ~pos;
            if (newEle != null) {
                bitL[picker] |= pos;
            } else this.size--;
            return true;
        }
        return false;
    }

    public void swap(Set<Byte> source) {
        if (this == source || (this.isEmpty() && source.isEmpty())) return;
        if (source instanceof ByteBitSet) {
            final ByteBitSet src = (ByteBitSet) source;
            final int currSize = this.size, srcSize = src.size;
            final long[] currBitL = this.bitMark, srcBitL = src.bitMark;

            src.size = currSize;
            src.bitMark = currBitL;
            this.size = srcSize;
            this.bitMark = srcBitL;
            return;
        }
        if (source.size() < this.size()) {
            final List<Byte> tmp = new ArrayList<>(source);
            source.clear();
            source.addAll(this);
            this.clear();
            this.addAll(tmp);
        } else {
            final List<Byte> tmp = new ArrayList<>(this);
            this.clear();
            this.addAll(source);
            source.clear();
            source.addAll(tmp);
        }
    }

    public void forEach(ByteConsumer action) {
        if (this.isEmpty()) return;

        final long[] bitL = this.bitMark;
        for (int seq = 0; seq < 256; seq++) {
            if ((bitL[seq >>> 6] & ( 1L << (seq & 0b111111))) != NULL_BIT) action.accept((byte) seq);
        }
    }

    public boolean addAll(Collection<? extends Byte> c) {
        if (c == this || c.isEmpty()) return false;
        boolean result = false;
        if (c instanceof ByteBitSet) {
            final ByteBitSet cast = (ByteBitSet) c;
            final int oldSize = this.size;
            final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;

            currBitL[0] |= srcBitL[0];
            currBitL[1] |= srcBitL[1];
            currBitL[2] |= srcBitL[2];
            currBitL[3] |= srcBitL[3];

            this.computeSize();
            result = oldSize != this.size;
        } else {
            for (Byte e : c) {
                result |= this.add(e);
            }
        }
        return result;
    }

    public boolean removeIf(BytePredicate filter) {
        if (this.isEmpty()) return false;

        boolean result = false;
        final long[] bitL = this.bitMark;
        for (int seq = 0; seq < 256; seq++) {
            final int picker;
            final long pos;
            if ((bitL[picker = seq >>> 6] & (pos = 1L << (seq & 0b111111))) != NULL_BIT && filter.test((byte) seq)) {
                bitL[picker] &= ~pos;
                result = true;
            }
        }
        this.size = Long.bitCount(bitL[0]) + Long.bitCount(bitL[1]) + Long.bitCount(bitL[2]) + Long.bitCount(bitL[3]);
        return result;
    }

    public boolean removeAll(Collection<?> c) {
        if (this.isEmpty()) return false;
        if (c == this) {
            final int oldSize = this.size;
            final long[] bitL = this.bitMark;
            bitL[0] = bitL[1] = bitL[2] = bitL[3] = NULL_BIT;
            this.size = 0;
            return oldSize != this.size;
        }

        if (c instanceof ByteBitSet) {
            final ByteBitSet cast = (ByteBitSet) c;
            final int oldSize = this.size;
            final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;

            currBitL[0] &= ~srcBitL[0];
            currBitL[1] &= ~srcBitL[1];
            currBitL[2] &= ~srcBitL[2];
            currBitL[3] &= ~srcBitL[3];

            this.computeSize();
            return oldSize != this.size;
        } else if (c instanceof List<?>) {
            boolean result = false;
            for (final Object e : c) {
                result |= this.remove(e);
            }
            return result;
        } else {
            if (this.size > c.size()) {
                boolean result = false;
                for (final Object e : c) {
                    result |= this.remove(e);
                }
                return result;
            } else {
                return this.removeIf(c::contains);
            }
        }
    }

    public boolean containsAll(Collection<?> c) {
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();

        if (c instanceof ByteBitSet) {
            final ByteBitSet cast = (ByteBitSet) c;
            final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;

            return (currBitL[0] & srcBitL[0]) == srcBitL[0] &&
                    (currBitL[1] & srcBitL[1]) == srcBitL[1] &&
                    (currBitL[2] & srcBitL[2]) == srcBitL[2] &&
                    (currBitL[3] & srcBitL[3]) == srcBitL[3];
        } else {
            if (c instanceof Set<?> && this.size < c.size()) return false;

            for (final Object e : c) {
                if (!this.contains(e)) return false;
            }
            return true;
        }
    }

    public boolean retainAll(Collection<?> c) {
        if (c == this || this.isEmpty()) return false;

        if (c instanceof ByteBitSet) {
            final ByteBitSet cast = (ByteBitSet) c;
            final int oldSize = this.size;
            final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;

            currBitL[0] &= srcBitL[0];
            currBitL[1] &= srcBitL[1];
            currBitL[2] &= srcBitL[2];
            currBitL[3] &= srcBitL[3];

            this.computeSize();
            return oldSize != this.size;
        } else {
            return this.removeIf((byte l_ele) -> !c.contains(l_ele));
        }
    }

    public int capacity() {
        return 256;
    }

    public int remaining() {
        return 256 - this.size;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public ByteIterator iterator() {
        return new SetIterator();
    }

    public ByteSpliterator spliterator() {
        return new SetSpliterator(0, -1, 0);
    }

    public long[] table() {
        return this.bitMark;
    }

    public void clear() {
        if (this.isEmpty()) return;
        final long[] bitL = this.bitMark;
        bitL[0] = bitL[1] = bitL[2] = bitL[3] = NULL_BIT;
        this.size = 0;
    }

    public void clear(int newCapacity) {
        if (newCapacity < 0) throw new IllegalArgumentException();
        super.clear();
    }

    protected byte[] prepareArray(final byte[] a) {
        final int sizeL = this.size;
        if (a.length < sizeL) return new byte[sizeL];
        if (a.length > sizeL) a[sizeL] = 0;
        return a;
    }

    protected byte[] slotToArray(final byte[] a) {
        if (this.isEmpty()) return a;

        int idx = 0;
        final long[] bitL = this.bitMark;
        for (int seq = 0; seq < 256; seq++) {
            final int picker = seq >>> 6;
            final long pos = 1L << (seq & 0b111111);
            if ((bitL[picker] & pos) != NULL_BIT) a[idx++] = (byte) seq;
        }
        return a;
    }

    public byte[] toArrayRaw() {
        return this.slotToArray(new byte[this.size]);
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
            final Set<?> castSet = (Set<?>) o;
            if (castSet.size() != this.size()) return false;
            try {
                if (castSet instanceof ByteBitSet) {
                    final ByteBitSet cast = (ByteBitSet) castSet;
                    final long[] currBitL = this.bitMark, srcBitL = cast.bitMark;
                    return currBitL[0] == srcBitL[0] &&
                            currBitL[1] == srcBitL[1] &&
                            currBitL[2] == srcBitL[2] &&
                            currBitL[3] == srcBitL[3];
                } else {
                    for (final Object e : castSet) {
                        if (!this.contains(e)) return false;
                    }
                    return true;
                }
            } catch (ClassCastException | NullPointerException unused) {
                return false;
            }
        }
        return false;
    }

    public String toString() {
        if (this.isEmpty()) return "[]";

        final StringBuilder sb = new StringBuilder(this.size * 6 + 2);
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
        ByteBitSet result;
        try {
            result = (ByteBitSet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        result.bitMark = new long[4];
        System.arraycopy(this.bitMark, 0, result.bitMark, 0, 4);
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        final long[] bitL = this.bitMark;
        out.writeLong(bitL[0]);
        out.writeLong(bitL[1]);
        out.writeLong(bitL[2]);
        out.writeLong(bitL[3]);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.bitMark = new long[]{in.readLong(), in.readLong(), in.readLong(), in.readLong()};
        this.computeSize();
    }

    protected class SetIterator implements ByteIterator {
        protected int preSeq = -1;
        protected int seq = -1;
        protected int remaining;

        protected SetIterator() {
            this.remaining = ByteBitSet.this.size;
        }

        public boolean hasNext() {
            return this.remaining > 0;
        }

        public byte nextByte() {
            if (this.remaining < 1) throw new NoSuchElementException();
            this.remaining--;

            int seqL = this.seq + 1;
            final long[] bitL = ByteBitSet.this.bitMark;
            while (seqL < 256 && (bitL[seqL >>> 6] & (1L << (seqL & 0b111111))) == NULL_BIT) {
                seqL++;
            }
            return (byte) (this.seq = this.preSeq = seqL);
        }

        public void remove() {
            final long[] bitL = ByteBitSet.this.bitMark;
            final int picker, seqL;
            final long pos;
            if ((seqL = this.preSeq) < 0 || (bitL[picker = seqL >>> 6] & (pos = 1L << (seqL & 0b111111))) == NULL_BIT) throw new IllegalStateException();
            bitL[picker] &= ~pos;
            this.preSeq = -1;
            ByteBitSet.this.size--;
        }

        public void forEachRemaining(ByteConsumer action) {
            final long[] bitL = ByteBitSet.this.bitMark;
            int findPre = this.preSeq;
            for (int seqL = this.seq + 1; seqL < 256; seqL++) {
                if ((bitL[seqL >>> 6] & ( 1L << (seqL & 0b111111))) != NULL_BIT) action.accept((byte) (findPre = seqL));
            }
            this.preSeq = findPre;
            this.remaining = 0;
            this.seq = 255;
        }
    }

    protected class SetSpliterator implements ByteSpliterator {
        protected int seq;
        protected int limitCap;
        protected int estSize;

        protected SetSpliterator(int seq, int limitCap, int estSize) {
            this.seq = seq;
            this.limitCap = limitCap;
            this.estSize = estSize;
        }

        protected final int getLimitCap() {
            int newLimit;
            if ((newLimit = this.limitCap) < 0) {
                this.estSize = ByteBitSet.this.size;
                newLimit = this.limitCap = 256;
            }
            return newLimit;
        }

        public ByteSpliterator trySplit() {
            final int newLimit = this.getLimitCap(), newSeq = this.seq, midSeq = (newSeq + newLimit) >>> 1;
            return newSeq < midSeq ? new SetSpliterator(newSeq, this.seq = midSeq, this.estSize >>>= 1) : null;
        }

        public boolean tryAdvance(ByteConsumer action) {
            final int currLimit = this.getLimitCap();

            int seqL = this.seq;
            for (final long[] bitL = ByteBitSet.this.bitMark; seqL < currLimit; seqL++) {
                if ((bitL[seqL >>> 6] & (1L << (seqL & 0b111111))) != NULL_BIT) {
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
            for (final long[] bitL = ByteBitSet.this.bitMark; seqL < currLimit; seqL++) {
                if ((bitL[seqL >>> 6] & (1L << (seqL & 0b111111))) != NULL_BIT) {
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
            return (this.limitCap < 0 || this.estSize == ByteBitSet.this.size ? Spliterator.SIZED : 0) | Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.NONNULL;
        }
    }
}
