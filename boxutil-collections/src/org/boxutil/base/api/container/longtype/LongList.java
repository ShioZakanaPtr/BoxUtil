package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.ListExt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

public interface LongList extends ListExt<Long>, LongCollection {
    long getRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long get(int index) {
        return this.getRaw(index);
    }

    default long getFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getFirst() {
        return this.getFirstRaw();
    }

    default long getLastRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getLast() {
        return this.getLastRaw();
    }

    long set(int index, long element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long set(int index, Long element) {
        return this.set(index, element != null ? element.longValue() : (long) 0);
    }

    default boolean add(long e) {
        this.add(this.size() - 1, e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Long e) {
        return this.add(e != null ? e.longValue() : (long) 0);
    }

    void add(int index, long element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void add(int index, Long element) {
        this.add(index, element != null ? element.longValue() : (long) 0);
    }

    default void addFirst(long e) {
        this.add(0, e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void addFirst(Long e) {
        this.addFirst(e != null ? e.longValue() : (long) 0);
    }

    default void addLast(long e) {
        this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void addLast(Long e) {
        this.addLast(e != null ? e.longValue() : (long) 0);
    }

    long removeAtRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long removeAt(int index) {
        return this.removeAtRaw(index);
    }

    boolean removeElement(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.removeElement(o != null ? ((Long) o).longValue() : (long) 0);
    }

    /**
     * @see ListExt#remove(Object)
     */
    @Deprecated
    default boolean remove(Object o) {
        return this.removeElement(o);
    }

    default long removeFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.removeAtRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long removeFirst() {
        return this.removeFirstRaw();
    }

    default long removeLastRaw() {
        return this.removeAtRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long removeLast() {
        return this.removeLastRaw();
    }

    boolean contains(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.contains(o != null ? ((Long) o).longValue() : (long) 0);
    }

    int indexOf(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default int indexOf(Object o) {
        if (o != null && !(o instanceof Long)) return -1;
        return this.indexOf(o != null ? ((Long) o).longValue() : (long) 0);
    }

    int lastIndexOf(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default int lastIndexOf(Object o) {
        if (o != null && !(o instanceof Long)) return -1;
        return this.lastIndexOf(o != null ? ((Long) o).longValue() : (long) 0);
    }

    default long update(int index, @NotNull LongUnaryOperator updateFunction) {
        long newValue = updateFunction.applyAsLong(this.getRaw(index));
        this.set(index, newValue);
        return newValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long update(int index, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(index, (long l_t) -> {
            final Long result = updateFunction.apply(l_t);
            return result != null ? result : (long) 0;
        });
    }

    default void swapAt(int fromIndex, int toIndex) {
        long left = this.getRaw(fromIndex);
        long right = this.getRaw(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    default void replaceAll(@NotNull LongUnaryOperator operator) {
        final LongListIterator li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.applyAsLong(li.nextLong()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull UnaryOperator<Long> operator) {
        this.replaceAll((long l_t) -> {
            final Long result = operator.apply(l_t);
            return result != null ? result : (long) 0;
        });
    }

    default void replaceRange(int fromIndex, int toIndex, LongUnaryOperator operator) {
        LongListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.applyAsLong(it.nextLong()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceRange(int fromIndex, int toIndex, Function<? super Long, ? extends Long> operator) {
        this.replaceRange(fromIndex, toIndex, (long l_t) -> {
            final Long result = operator.apply(l_t);
            return result != null ? result : (long) 0;
        });
    }

    default void computeRange(int fromIndex, int toIndex, LongFunction<? extends Long> operator) {
        LongListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            Long newValue = operator.apply(it.nextLong());
            if (newValue != null) {
                it.set(newValue.longValue());
            } else {
                it.remove();
            }
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void computeRange(int fromIndex, int toIndex, Function<? super Long, ? extends Long> operator) {
        this.computeRange(fromIndex, toIndex, (long l_t) -> operator.apply(l_t));
    }

    default void addAll(int dstIndex, long[] source, int srcIndex, int length) {
        for (int i = 0; i < length; i++) {
            this.add(dstIndex + i, source[srcIndex + i]);
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void addAll(int dstIndex, Long[] source, int srcIndex, int length) {
        ListExt.super.addAll(dstIndex, source, srcIndex, length);
    }

    @NotNull LongListIterator listIterator();

    @NotNull LongListIterator listIterator(int index);

    @NotNull
    LongSpliterator spliterator();

    @NotNull LongList subList(int fromIndex, int toIndex);
}
