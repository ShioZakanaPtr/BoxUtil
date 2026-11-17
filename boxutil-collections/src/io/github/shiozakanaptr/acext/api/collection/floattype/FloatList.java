package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatConsumer;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatFunction;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface FloatList extends ListExt<Float>, FloatCollection {
    float getRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(int)
     */
    @Deprecated
    default Float get(int index) {
        return this.getRaw(index);
    }

    default float getFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Float getFirst() {
        return this.getFirstRaw();
    }

    default float getLastRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Float getLast() {
        return this.getLastRaw();
    }

    float set(int index, float element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(int, float)
     */
    @Deprecated
    default Float set(int index, Float element) {
        return this.set(index, element != null ? element.floatValue() : (float) 0);
    }

    default boolean add(float e) {
        this.add(this.size() - 1, e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(float)
     */
    @Deprecated
    default boolean add(Float e) {
        return this.add(e != null ? e.floatValue() : (float) 0);
    }

    void add(int index, float element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int, float)
     */
    @Deprecated
    default void add(int index, Float element) {
        this.add(index, element != null ? element.floatValue() : (float) 0);
    }

    default void addFirst(float e) {
        this.add(0, e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(float)
     */
    @Deprecated
    default void addFirst(Float e) {
        this.addFirst(e != null ? e.floatValue() : (float) 0);
    }

    default void addLast(float e) {
        this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(float)
     */
    @Deprecated
    default void addLast(Float e) {
        this.addLast(e != null ? e.floatValue() : (float) 0);
    }

    float removeAtRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeAtRaw(int)
     */
    @Deprecated
    default Float removeAt(int index) {
        return this.removeAtRaw(index);
    }

    boolean removeElement(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(float)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.removeElement(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    default float removeFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.removeAtRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Float removeFirst() {
        return this.removeFirstRaw();
    }

    default float removeLastRaw() {
        return this.removeAtRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Float removeLast() {
        return this.removeLastRaw();
    }

    boolean contains(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(float)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.contains(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    int indexOf(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #indexOf(float)
     */
    @Deprecated
    default int indexOf(Object o) {
        if (o != null && !(o instanceof Float)) return -1;
        return this.indexOf(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    int lastIndexOf(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #lastIndexOf(float)
     */
    @Deprecated
    default int lastIndexOf(Object o) {
        if (o != null && !(o instanceof Float)) return -1;
        return this.lastIndexOf(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    default float update(int index, @NotNull FloatUnaryOperator updateFunction) {
        float newValue = updateFunction.apply(this.getRaw(index));
        this.set(index, newValue);
        return newValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(int index, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(index, (float l_t) -> {
            final Float result = updateFunction.apply(l_t);
            return result != null ? result : (float) 0;
        });
    }

    default void swapAt(int fromIndex, int toIndex) {
        float left = this.getRaw(fromIndex);
        float right = this.getRaw(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    default void forEach(int fromIndex, int toIndex, FloatConsumer action) {
        FloatListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            action.accept(it.nextFloat());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(int, int, FloatConsumer)
     */
    @Deprecated
    default void forEach(int fromIndex, int toIndex, Consumer<? super Float> action) {
        this.forEach(fromIndex, toIndex, (float l_t) -> action.accept(l_t));
    }

    default void replaceAll(@NotNull FloatUnaryOperator operator) {
        final FloatListIterator li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.nextFloat()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatUnaryOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull UnaryOperator<Float> operator) {
        this.replaceAll((float l_t) -> {
            final Float result = operator.apply(l_t);
            return result != null ? result : (float) 0;
        });
    }

    default void replace(int fromIndex, int toIndex, FloatUnaryOperator operator) {
        FloatListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.apply(it.nextFloat()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, int, FloatUnaryOperator)
     */
    @Deprecated
    default void replace(int fromIndex, int toIndex, Function<? super Float, ? extends Float> operator) {
        this.replace(fromIndex, toIndex, (float l_t) -> {
            final Float result = operator.apply(l_t);
            return result != null ? result : (float) 0;
        });
    }

    default boolean compute(int fromIndex, int toIndex, FloatFunction<? extends Float> operator) {
        boolean result = false;
        FloatListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            Float newValue = operator.apply(it.nextFloat());
            if (newValue != null) {
                it.set(newValue.floatValue());
            } else {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, int, FloatFunction)
     */
    @Deprecated
    default boolean compute(int fromIndex, int toIndex, Function<? super Float, ? extends Float> operator) {
        return this.compute(fromIndex, toIndex, (float l_t) -> operator.apply(l_t));
    }

    default boolean addAll(@NotNull float[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(float[])
     */
    @Deprecated
    default <T extends Float> boolean addAll(@NotNull T[] elements) {
        return FloatCollection.super.addAll(elements);
    }

    default boolean addAll(@NotNull float[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(float[], int, int)
     */
    @Deprecated
    default <T extends Float> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return FloatCollection.super.addAll(elements, srcIndex, length);
    }

    boolean addAll(int dstIndex, @NotNull float[] elements, int srcIndex, int length);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int, float[], int, int)
     */
    @Deprecated
    default <T extends Float> boolean addAll(int dstIndex, @NotNull T[] elements, int srcIndex, int length) {
        if (length == 0) return false;
        for (int i = 0; i < length; i++) {
            this.add(dstIndex + i, elements[srcIndex + i]);
        }
        return true;
    }

    @NotNull
    FloatListIterator listIterator();

    @NotNull
    FloatListIterator listIterator(int index);

    default FloatJumboListIterator listIterator64() {
        throw new UnsupportedOperationException();
    }

    default FloatJumboListIterator listIterator64(long index) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    FloatList subList(int fromIndex, int toIndex);

    @NotNull
    FloatSpliterator spliterator();

}
