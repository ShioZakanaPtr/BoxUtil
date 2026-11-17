package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import java.util.function.IntConsumer;
import io.github.shiozakanaptr.acext.util.function.inttype.IntFunction;
import io.github.shiozakanaptr.acext.util.function.inttype.IntUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface IntList extends ListExt<Integer>, IntCollection {
    int getRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(int)
     */
    @Deprecated
    default Integer get(int index) {
        return this.getRaw(index);
    }

    default int getFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Integer getFirst() {
        return this.getFirstRaw();
    }

    default int getLastRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Integer getLast() {
        return this.getLastRaw();
    }

    int set(int index, int element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(int, int)
     */
    @Deprecated
    default Integer set(int index, Integer element) {
        return this.set(index, element != null ? element.intValue() : (int) 0);
    }

    default boolean add(int e) {
        this.add(this.size() - 1, e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int)
     */
    @Deprecated
    default boolean add(Integer e) {
        return this.add(e != null ? e.intValue() : (int) 0);
    }

    void add(int index, int element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int, int)
     */
    @Deprecated
    default void add(int index, Integer element) {
        this.add(index, element != null ? element.intValue() : (int) 0);
    }

    default void addFirst(int e) {
        this.add(0, e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(int)
     */
    @Deprecated
    default void addFirst(Integer e) {
        this.addFirst(e != null ? e.intValue() : (int) 0);
    }

    default void addLast(int e) {
        this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(int)
     */
    @Deprecated
    default void addLast(Integer e) {
        this.addLast(e != null ? e.intValue() : (int) 0);
    }

    int removeAtRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeAtRaw(int)
     */
    @Deprecated
    default Integer removeAt(int index) {
        return this.removeAtRaw(index);
    }

    boolean removeElement(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(int)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.removeElement(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    default int removeFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.removeAtRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Integer removeFirst() {
        return this.removeFirstRaw();
    }

    default int removeLastRaw() {
        return this.removeAtRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Integer removeLast() {
        return this.removeLastRaw();
    }

    boolean contains(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(int)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.contains(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    int indexOf(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #indexOf(int)
     */
    @Deprecated
    default int indexOf(Object o) {
        if (o != null && !(o instanceof Integer)) return -1;
        return this.indexOf(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    int lastIndexOf(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #lastIndexOf(int)
     */
    @Deprecated
    default int lastIndexOf(Object o) {
        if (o != null && !(o instanceof Integer)) return -1;
        return this.lastIndexOf(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    default int update(int index, @NotNull IntUnaryOperator updateFunction) {
        int newValue = updateFunction.apply(this.getRaw(index));
        this.set(index, newValue);
        return newValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(int index, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(index, (int l_t) -> {
            final Integer result = updateFunction.apply(l_t);
            return result != null ? result : (int) 0;
        });
    }

    default void swapAt(int fromIndex, int toIndex) {
        int left = this.getRaw(fromIndex);
        int right = this.getRaw(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    default void forEach(int fromIndex, int toIndex, IntConsumer action) {
        IntListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            action.accept(it.nextInt());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(int, int, IntConsumer)
     */
    @Deprecated
    default void forEach(int fromIndex, int toIndex, Consumer<? super Integer> action) {
        this.forEach(fromIndex, toIndex, (int l_t) -> action.accept(l_t));
    }

    default void replaceAll(@NotNull IntUnaryOperator operator) {
        final IntListIterator li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.nextInt()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntUnaryOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull UnaryOperator<Integer> operator) {
        this.replaceAll((int l_t) -> {
            final Integer result = operator.apply(l_t);
            return result != null ? result : (int) 0;
        });
    }

    default void replace(int fromIndex, int toIndex, IntUnaryOperator operator) {
        IntListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.apply(it.nextInt()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, int, IntUnaryOperator)
     */
    @Deprecated
    default void replace(int fromIndex, int toIndex, Function<? super Integer, ? extends Integer> operator) {
        this.replace(fromIndex, toIndex, (int l_t) -> {
            final Integer result = operator.apply(l_t);
            return result != null ? result : (int) 0;
        });
    }

    default boolean compute(int fromIndex, int toIndex, IntFunction<? extends Integer> operator) {
        boolean result = false;
        IntListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            Integer newValue = operator.apply(it.nextInt());
            if (newValue != null) {
                it.set(newValue.intValue());
            } else {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, int, IntFunction)
     */
    @Deprecated
    default boolean compute(int fromIndex, int toIndex, Function<? super Integer, ? extends Integer> operator) {
        return this.compute(fromIndex, toIndex, (int l_t) -> operator.apply(l_t));
    }

    default boolean addAll(@NotNull int[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int[])
     */
    @Deprecated
    default <T extends Integer> boolean addAll(@NotNull T[] elements) {
        return IntCollection.super.addAll(elements);
    }

    default boolean addAll(@NotNull int[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int[], int, int)
     */
    @Deprecated
    default <T extends Integer> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return IntCollection.super.addAll(elements, srcIndex, length);
    }

    boolean addAll(int dstIndex, @NotNull int[] elements, int srcIndex, int length);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int, int[], int, int)
     */
    @Deprecated
    default <T extends Integer> boolean addAll(int dstIndex, @NotNull T[] elements, int srcIndex, int length) {
        if (length == 0) return false;
        for (int i = 0; i < length; i++) {
            this.add(dstIndex + i, elements[srcIndex + i]);
        }
        return true;
    }

    @NotNull
    IntListIterator listIterator();

    @NotNull
    IntListIterator listIterator(int index);

    default IntJumboListIterator listIterator64() {
        throw new UnsupportedOperationException();
    }

    default IntJumboListIterator listIterator64(long index) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    IntList subList(int fromIndex, int toIndex);

    @NotNull
    IntSpliterator spliterator();

}
