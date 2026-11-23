package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortFunction;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ShortList extends ListExt<Short>, ShortCollection {
    short getRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(int)
     */
    @Deprecated
    default Short get(int index) {
        return this.getRaw(index);
    }

    default short getFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Short getFirst() {
        return this.getFirstRaw();
    }

    default short getLastRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Short getLast() {
        return this.getLastRaw();
    }

    short set(int index, short element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(int, short)
     */
    @Deprecated
    default Short set(int index, Short element) {
        return this.set(index, element != null ? element.shortValue() : (short) 0);
    }

    default boolean add(short e) {
        this.add(this.size() - 1, e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(short)
     */
    @Deprecated
    default boolean add(Short e) {
        return this.add(e != null ? e.shortValue() : (short) 0);
    }

    void add(int index, short element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int, short)
     */
    @Deprecated
    default void add(int index, Short element) {
        this.add(index, element != null ? element.shortValue() : (short) 0);
    }

    default void addFirst(short e) {
        this.add(0, e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(short)
     */
    @Deprecated
    default void addFirst(Short e) {
        this.addFirst(e != null ? e.shortValue() : (short) 0);
    }

    default void addLast(short e) {
        this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(short)
     */
    @Deprecated
    default void addLast(Short e) {
        this.addLast(e != null ? e.shortValue() : (short) 0);
    }

    short removeAtRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeAtRaw(int)
     */
    @Deprecated
    default Short removeAt(int index) {
        return this.removeAtRaw(index);
    }

    boolean removeElement(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(short)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.removeElement(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    default short removeFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.removeAtRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Short removeFirst() {
        return this.removeFirstRaw();
    }

    default short removeLastRaw() {
        return this.removeAtRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Short removeLast() {
        return this.removeLastRaw();
    }

    boolean contains(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(short)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.contains(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    int indexOf(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #indexOf(short)
     */
    @Deprecated
    default int indexOf(Object o) {
        if (o != null && !(o instanceof Short)) return -1;
        return this.indexOf(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    int lastIndexOf(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #lastIndexOf(short)
     */
    @Deprecated
    default int lastIndexOf(Object o) {
        if (o != null && !(o instanceof Short)) return -1;
        return this.lastIndexOf(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    default short update(int index, @NotNull ShortUnaryOperator updateFunction) {
        short newValue = updateFunction.apply(this.getRaw(index));
        this.set(index, newValue);
        return newValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(int index, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(index, (short l_t) -> {
            final Short result = updateFunction.apply(l_t);
            return result != null ? result : (short) 0;
        });
    }

    default void swapAt(int fromIndex, int toIndex) {
        short left = this.getRaw(fromIndex);
        short right = this.getRaw(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    default void forEach(int fromIndex, int toIndex, ShortConsumer action) {
        ShortListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            action.accept(it.nextShort());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(int, int, ShortConsumer)
     */
    @Deprecated
    default void forEach(int fromIndex, int toIndex, Consumer<? super Short> action) {
        this.forEach(fromIndex, toIndex, (short l_t) -> action.accept(l_t));
    }

    default void replaceAll(@NotNull ShortUnaryOperator operator) {
        final ShortListIterator li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.nextShort()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortUnaryOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull UnaryOperator<Short> operator) {
        this.replaceAll((short l_t) -> {
            final Short result = operator.apply(l_t);
            return result != null ? result : (short) 0;
        });
    }

    default void replace(int fromIndex, int toIndex, ShortUnaryOperator operator) {
        ShortListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.apply(it.nextShort()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, int, ShortUnaryOperator)
     */
    @Deprecated
    default void replace(int fromIndex, int toIndex, Function<? super Short, ? extends Short> operator) {
        this.replace(fromIndex, toIndex, (short l_t) -> {
            final Short result = operator.apply(l_t);
            return result != null ? result : (short) 0;
        });
    }

    default boolean compute(int fromIndex, int toIndex, ShortFunction<? extends Short> operator) {
        boolean result = false;
        ShortListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            Short newValue = operator.apply(it.nextShort());
            if (newValue != null) {
                it.set(newValue.shortValue());
            } else {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, int, ShortFunction)
     */
    @Deprecated
    default boolean compute(int fromIndex, int toIndex, Function<? super Short, ? extends Short> operator) {
        return this.compute(fromIndex, toIndex, (short l_t) -> operator.apply(l_t));
    }

    default boolean addAll(@NotNull short[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(short[])
     */
    @Deprecated
    default <T extends Short> boolean addAll(@NotNull T[] elements) {
        return ShortCollection.super.addAll(elements);
    }

    default boolean addAll(@NotNull short[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(short[], int, int)
     */
    @Deprecated
    default <T extends Short> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return ShortCollection.super.addAll(elements, srcIndex, length);
    }

    boolean addAll(int dstIndex, @NotNull short[] elements, int srcIndex, int length);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int, short[], int, int)
     */
    @Deprecated
    default <T extends Short> boolean addAll(int dstIndex, @NotNull T[] elements, int srcIndex, int length) {
        if (length == 0) return false;
        for (int i = 0; i < length; i++) {
            this.add(dstIndex + i, elements[srcIndex + i]);
        }
        return true;
    }

    @NotNull
    ShortListIterator listIterator();

    @NotNull
    ShortListIterator listIterator(int index);

    default ShortJumboListIterator listIterator64() {
        throw new UnsupportedOperationException();
    }

    default ShortJumboListIterator listIterator64(long index) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    ShortList subList(int fromIndex, int toIndex);

    @NotNull
    ShortBidiIterator iterator();

    @NotNull
    ShortBidiIterator iteratorReversed();

    @NotNull
    ShortSpliterator spliterator();

}
