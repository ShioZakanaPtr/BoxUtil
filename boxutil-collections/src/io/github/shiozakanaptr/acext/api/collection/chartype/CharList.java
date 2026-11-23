package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import io.github.shiozakanaptr.acext.util.function.chartype.CharConsumer;
import io.github.shiozakanaptr.acext.util.function.chartype.CharFunction;
import io.github.shiozakanaptr.acext.util.function.chartype.CharUnaryOperator;
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
public interface CharList extends ListExt<Character>, CharCollection {
    char getRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(int)
     */
    @Deprecated
    default Character get(int index) {
        return this.getRaw(index);
    }

    default char getFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getFirstRaw()
     */
    @Deprecated
    default Character getFirst() {
        return this.getFirstRaw();
    }

    default char getLastRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getLastRaw()
     */
    @Deprecated
    default Character getLast() {
        return this.getLastRaw();
    }

    char set(int index, char element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #set(int, char)
     */
    @Deprecated
    default Character set(int index, Character element) {
        return this.set(index, element != null ? element.charValue() : (char) 0);
    }

    default boolean add(char e) {
        this.add(this.size() - 1, e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(char)
     */
    @Deprecated
    default boolean add(Character e) {
        return this.add(e != null ? e.charValue() : (char) 0);
    }

    void add(int index, char element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int, char)
     */
    @Deprecated
    default void add(int index, Character element) {
        this.add(index, element != null ? element.charValue() : (char) 0);
    }

    default void addFirst(char e) {
        this.add(0, e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addFirst(char)
     */
    @Deprecated
    default void addFirst(Character e) {
        this.addFirst(e != null ? e.charValue() : (char) 0);
    }

    default void addLast(char e) {
        this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addLast(char)
     */
    @Deprecated
    default void addLast(Character e) {
        this.addLast(e != null ? e.charValue() : (char) 0);
    }

    char removeAtRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeAtRaw(int)
     */
    @Deprecated
    default Character removeAt(int index) {
        return this.removeAtRaw(index);
    }

    boolean removeElement(char o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(char)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Character)) return false;
        return this.removeElement(o != null ? ((Character) o).charValue() : (char) 0);
    }

    default char removeFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.removeAtRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeFirstRaw()
     */
    @Deprecated
    default Character removeFirst() {
        return this.removeFirstRaw();
    }

    default char removeLastRaw() {
        return this.removeAtRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeLastRaw()
     */
    @Deprecated
    default Character removeLast() {
        return this.removeLastRaw();
    }

    boolean contains(char o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(char)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Character)) return false;
        return this.contains(o != null ? ((Character) o).charValue() : (char) 0);
    }

    int indexOf(char o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #indexOf(char)
     */
    @Deprecated
    default int indexOf(Object o) {
        if (o != null && !(o instanceof Character)) return -1;
        return this.indexOf(o != null ? ((Character) o).charValue() : (char) 0);
    }

    int lastIndexOf(char o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #lastIndexOf(char)
     */
    @Deprecated
    default int lastIndexOf(Object o) {
        if (o != null && !(o instanceof Character)) return -1;
        return this.lastIndexOf(o != null ? ((Character) o).charValue() : (char) 0);
    }

    default char update(int index, @NotNull CharUnaryOperator updateFunction) {
        char newValue = updateFunction.apply(this.getRaw(index));
        this.set(index, newValue);
        return newValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, CharUnaryOperator)
     */
    @Deprecated
    default Character update(int index, @NotNull Function<? super Character, ? extends Character> updateFunction) {
        return this.update(index, (char l_t) -> {
            final Character result = updateFunction.apply(l_t);
            return result != null ? result : (char) 0;
        });
    }

    default void swapAt(int fromIndex, int toIndex) {
        char left = this.getRaw(fromIndex);
        char right = this.getRaw(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    default void forEach(int fromIndex, int toIndex, CharConsumer action) {
        CharListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            action.accept(it.nextChar());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(int, int, CharConsumer)
     */
    @Deprecated
    default void forEach(int fromIndex, int toIndex, Consumer<? super Character> action) {
        this.forEach(fromIndex, toIndex, (char l_t) -> action.accept(l_t));
    }

    default void replaceAll(@NotNull CharUnaryOperator operator) {
        final CharListIterator li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.nextChar()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharUnaryOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull UnaryOperator<Character> operator) {
        this.replaceAll((char l_t) -> {
            final Character result = operator.apply(l_t);
            return result != null ? result : (char) 0;
        });
    }

    default void replace(int fromIndex, int toIndex, CharUnaryOperator operator) {
        CharListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.apply(it.nextChar()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, int, CharUnaryOperator)
     */
    @Deprecated
    default void replace(int fromIndex, int toIndex, Function<? super Character, ? extends Character> operator) {
        this.replace(fromIndex, toIndex, (char l_t) -> {
            final Character result = operator.apply(l_t);
            return result != null ? result : (char) 0;
        });
    }

    default boolean compute(int fromIndex, int toIndex, CharFunction<? extends Character> operator) {
        boolean result = false;
        CharListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            Character newValue = operator.apply(it.nextChar());
            if (newValue != null) {
                it.set(newValue.charValue());
            } else {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, int, CharFunction)
     */
    @Deprecated
    default boolean compute(int fromIndex, int toIndex, Function<? super Character, ? extends Character> operator) {
        return this.compute(fromIndex, toIndex, (char l_t) -> operator.apply(l_t));
    }

    default boolean addAll(@NotNull char[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(char[])
     */
    @Deprecated
    default <T extends Character> boolean addAll(@NotNull T[] elements) {
        return CharCollection.super.addAll(elements);
    }

    default boolean addAll(@NotNull char[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(char[], int, int)
     */
    @Deprecated
    default <T extends Character> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CharCollection.super.addAll(elements, srcIndex, length);
    }

    boolean addAll(int dstIndex, @NotNull char[] elements, int srcIndex, int length);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(int, char[], int, int)
     */
    @Deprecated
    default <T extends Character> boolean addAll(int dstIndex, @NotNull T[] elements, int srcIndex, int length) {
        if (length == 0) return false;
        for (int i = 0; i < length; i++) {
            this.add(dstIndex + i, elements[srcIndex + i]);
        }
        return true;
    }

    @NotNull
    CharListIterator listIterator();

    @NotNull
    CharListIterator listIterator(int index);

    default CharJumboListIterator listIterator64() {
        throw new UnsupportedOperationException();
    }

    default CharJumboListIterator listIterator64(long index) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    CharList subList(int fromIndex, int toIndex);

    @NotNull
    CharBidiIterator iterator();

    @NotNull
    CharBidiIterator iteratorReversed();

    @NotNull
    CharSpliterator spliterator();

}
