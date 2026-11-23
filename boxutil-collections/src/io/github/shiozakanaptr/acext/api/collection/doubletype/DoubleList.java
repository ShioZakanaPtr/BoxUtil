package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import java.util.function.DoubleConsumer;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleFunction;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleUnaryOperator;
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
public interface DoubleList extends ListExt<Double>, DoubleCollection {
    double getRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(int)
     */
    @Deprecated
    default Double get(int index) {
        return this.getRaw(index);
    }

    default double getFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Double getFirst() {
        return this.getFirstRaw();
    }

    default double getLastRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Double getLast() {
        return this.getLastRaw();
    }

    double set(int index, double element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(int, double)
     */
    @Deprecated
    default Double set(int index, Double element) {
        return this.set(index, element != null ? element.doubleValue() : (double) 0);
    }

    default boolean add(double e) {
        this.add(this.size() - 1, e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(double)
     */
    @Deprecated
    default boolean add(Double e) {
        return this.add(e != null ? e.doubleValue() : (double) 0);
    }

    void add(int index, double element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int, double)
     */
    @Deprecated
    default void add(int index, Double element) {
        this.add(index, element != null ? element.doubleValue() : (double) 0);
    }

    default void addFirst(double e) {
        this.add(0, e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(double)
     */
    @Deprecated
    default void addFirst(Double e) {
        this.addFirst(e != null ? e.doubleValue() : (double) 0);
    }

    default void addLast(double e) {
        this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(double)
     */
    @Deprecated
    default void addLast(Double e) {
        this.addLast(e != null ? e.doubleValue() : (double) 0);
    }

    double removeAtRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeAtRaw(int)
     */
    @Deprecated
    default Double removeAt(int index) {
        return this.removeAtRaw(index);
    }

    boolean removeElement(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(double)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.removeElement(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    default double removeFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.removeAtRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Double removeFirst() {
        return this.removeFirstRaw();
    }

    default double removeLastRaw() {
        return this.removeAtRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Double removeLast() {
        return this.removeLastRaw();
    }

    boolean contains(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(double)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.contains(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    int indexOf(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #indexOf(double)
     */
    @Deprecated
    default int indexOf(Object o) {
        if (o != null && !(o instanceof Double)) return -1;
        return this.indexOf(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    int lastIndexOf(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #lastIndexOf(double)
     */
    @Deprecated
    default int lastIndexOf(Object o) {
        if (o != null && !(o instanceof Double)) return -1;
        return this.lastIndexOf(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    default double update(int index, @NotNull DoubleUnaryOperator updateFunction) {
        double newValue = updateFunction.apply(this.getRaw(index));
        this.set(index, newValue);
        return newValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, DoubleUnaryOperator)
     */
    @Deprecated
    default Double update(int index, @NotNull Function<? super Double, ? extends Double> updateFunction) {
        return this.update(index, (double l_t) -> {
            final Double result = updateFunction.apply(l_t);
            return result != null ? result : (double) 0;
        });
    }

    default void swapAt(int fromIndex, int toIndex) {
        double left = this.getRaw(fromIndex);
        double right = this.getRaw(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    default void forEach(int fromIndex, int toIndex, DoubleConsumer action) {
        DoubleListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            action.accept(it.nextDouble());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(int, int, DoubleConsumer)
     */
    @Deprecated
    default void forEach(int fromIndex, int toIndex, Consumer<? super Double> action) {
        this.forEach(fromIndex, toIndex, (double l_t) -> action.accept(l_t));
    }

    default void replaceAll(@NotNull DoubleUnaryOperator operator) {
        final DoubleListIterator li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.nextDouble()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleUnaryOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull UnaryOperator<Double> operator) {
        this.replaceAll((double l_t) -> {
            final Double result = operator.apply(l_t);
            return result != null ? result : (double) 0;
        });
    }

    default void replace(int fromIndex, int toIndex, DoubleUnaryOperator operator) {
        DoubleListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.apply(it.nextDouble()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, int, DoubleUnaryOperator)
     */
    @Deprecated
    default void replace(int fromIndex, int toIndex, Function<? super Double, ? extends Double> operator) {
        this.replace(fromIndex, toIndex, (double l_t) -> {
            final Double result = operator.apply(l_t);
            return result != null ? result : (double) 0;
        });
    }

    default boolean compute(int fromIndex, int toIndex, DoubleFunction<? extends Double> operator) {
        boolean result = false;
        DoubleListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            Double newValue = operator.apply(it.nextDouble());
            if (newValue != null) {
                it.set(newValue.doubleValue());
            } else {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, int, DoubleFunction)
     */
    @Deprecated
    default boolean compute(int fromIndex, int toIndex, Function<? super Double, ? extends Double> operator) {
        return this.compute(fromIndex, toIndex, (double l_t) -> operator.apply(l_t));
    }

    default boolean addAll(@NotNull double[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(double[])
     */
    @Deprecated
    default <T extends Double> boolean addAll(@NotNull T[] elements) {
        return DoubleCollection.super.addAll(elements);
    }

    default boolean addAll(@NotNull double[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(double[], int, int)
     */
    @Deprecated
    default <T extends Double> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return DoubleCollection.super.addAll(elements, srcIndex, length);
    }

    boolean addAll(int dstIndex, @NotNull double[] elements, int srcIndex, int length);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int, double[], int, int)
     */
    @Deprecated
    default <T extends Double> boolean addAll(int dstIndex, @NotNull T[] elements, int srcIndex, int length) {
        if (length == 0) return false;
        for (int i = 0; i < length; i++) {
            this.add(dstIndex + i, elements[srcIndex + i]);
        }
        return true;
    }

    @NotNull
    DoubleListIterator listIterator();

    @NotNull
    DoubleListIterator listIterator(int index);

    default DoubleJumboListIterator listIterator64() {
        throw new UnsupportedOperationException();
    }

    default DoubleJumboListIterator listIterator64(long index) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    DoubleList subList(int fromIndex, int toIndex);

    @NotNull
    DoubleBidiIterator iterator();

    @NotNull
    DoubleBidiIterator iteratorReversed();

    @NotNull
    DoubleSpliterator spliterator();

}
