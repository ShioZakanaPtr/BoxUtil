package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ListExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteConsumer;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteFunction;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteUnaryOperator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ByteList extends ListExt<Byte>, ByteCollection {
    byte getRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte get(int index) {
        return this.getRaw(index);
    }

    default byte getFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte getFirst() {
        return this.getFirstRaw();
    }

    default byte getLastRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.getRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte getLast() {
        return this.getLastRaw();
    }

    byte set(int index, byte element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte set(int index, Byte element) {
        return this.set(index, element != null ? element.byteValue() : (byte) 0);
    }

    default boolean add(byte e) {
        this.add(this.size() - 1, e);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Byte e) {
        return this.add(e != null ? e.byteValue() : (byte) 0);
    }

    void add(int index, byte element);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void add(int index, Byte element) {
        this.add(index, element != null ? element.byteValue() : (byte) 0);
    }

    default void addFirst(byte e) {
        this.add(0, e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void addFirst(Byte e) {
        this.addFirst(e != null ? e.byteValue() : (byte) 0);
    }

    default void addLast(byte e) {
        this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void addLast(Byte e) {
        this.addLast(e != null ? e.byteValue() : (byte) 0);
    }

    byte removeAtRaw(int index);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte removeAt(int index) {
        return this.removeAtRaw(index);
    }

    boolean removeElement(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.removeElement(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    /**
     * @see ListExt#remove(Object)
     */
    @Deprecated
    default boolean remove(byte o) {
        return this.removeElement(o);
    }

    /**
     * @see ListExt#remove(Object)
     */
    @Deprecated
    default boolean remove(Object o) {
        return this.removeElement(o);
    }

    default byte removeFirstRaw() {
        if (this.isEmpty()) throw new NoSuchElementException();
        return this.removeAtRaw(0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte removeFirst() {
        return this.removeFirstRaw();
    }

    default byte removeLastRaw() {
        return this.removeAtRaw(this.size() - 1);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte removeLast() {
        return this.removeLastRaw();
    }

    boolean contains(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.contains(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    int indexOf(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default int indexOf(Object o) {
        if (o != null && !(o instanceof Byte)) return -1;
        return this.indexOf(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    int lastIndexOf(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default int lastIndexOf(Object o) {
        if (o != null && !(o instanceof Byte)) return -1;
        return this.lastIndexOf(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    default byte update(int index, @NotNull ByteUnaryOperator updateFunction) {
        byte newValue = updateFunction.apply(this.getRaw(index));
        this.set(index, newValue);
        return newValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Byte update(int index, @NotNull Function<? super Byte, ? extends Byte> updateFunction) {
        return this.update(index, (byte l_t) -> {
            final Byte result = updateFunction.apply(l_t);
            return result != null ? result : (byte) 0;
        });
    }

    default void swapAt(int fromIndex, int toIndex) {
        byte left = this.getRaw(fromIndex);
        byte right = this.getRaw(toIndex);
        this.set(fromIndex, right);
        this.set(toIndex, left);
    }

    default void forEach(int fromIndex, int toIndex, ByteConsumer action) {
        ByteListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            action.accept(it.nextByte());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(int fromIndex, int toIndex, Consumer<? super Byte> action) {
        this.forEach(fromIndex, toIndex, (byte l_t) -> action.accept(l_t));
    }

    default void replaceAll(@NotNull ByteUnaryOperator operator) {
        final ByteListIterator li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.nextByte()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull UnaryOperator<Byte> operator) {
        this.replaceAll((byte l_t) -> {
            final Byte result = operator.apply(l_t);
            return result != null ? result : (byte) 0;
        });
    }

    default void replace(int fromIndex, int toIndex, ByteUnaryOperator operator) {
        ByteListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            it.set(operator.apply(it.nextByte()));
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replace(int fromIndex, int toIndex, Function<? super Byte, ? extends Byte> operator) {
        this.replace(fromIndex, toIndex, (byte l_t) -> {
            final Byte result = operator.apply(l_t);
            return result != null ? result : (byte) 0;
        });
    }

    default boolean compute(int fromIndex, int toIndex, ByteFunction<? extends Byte> operator) {
        boolean result = false;
        ByteListIterator it = this.listIterator(fromIndex);
        while (it.hasNext() && it.previousIndex() < (toIndex - 1)) {
            Byte newValue = operator.apply(it.nextByte());
            if (newValue != null) {
                it.set(newValue.byteValue());
            } else {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean compute(int fromIndex, int toIndex, Function<? super Byte, ? extends Byte> operator) {
        return this.compute(fromIndex, toIndex, (byte l_t) -> operator.apply(l_t));
    }

    default boolean addAll(@NotNull byte[] elements) {
        return this.addAll(elements, 0, elements.length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Byte> boolean addAll(@NotNull T[] elements) {
        return ByteCollection.super.addAll(elements);
    }

    default boolean addAll(@NotNull byte[] elements, int srcIndex, int length) {
        return this.addAll(0, elements, srcIndex, length);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Byte> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return ByteCollection.super.addAll(elements, srcIndex, length);
    }

    boolean addAll(int dstIndex, @NotNull byte[] elements, int srcIndex, int length);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default <T extends Byte> boolean addAll(int dstIndex, @NotNull T[] elements, int srcIndex, int length) {
        if (length == 0) return false;
        for (int i = 0; i < length; i++) {
            this.add(dstIndex + i, elements[srcIndex + i]);
        }
        return true;
    }

    @NotNull ByteListIterator listIterator();

    @NotNull ByteListIterator listIterator(int index);

    default ByteJumboListIterator listIterator64() {
        throw new UnsupportedOperationException();
    }

    default ByteJumboListIterator listIterator64(long index) {
        throw new UnsupportedOperationException();
    }

    @NotNull ByteList subList(int fromIndex, int toIndex);

    @NotNull
    ByteSpliterator spliterator();

    static ByteList of() {
        return ImmutableByteCollections.ofList();
    }

    static ByteList of(byte element) {
        return ImmutableByteCollections.ofList(element);
    }

    static ByteList of(byte... elements) {
        return ImmutableByteCollections.ofList(elements);
    }

    static ByteList copyOf(@NotNull Collection<? extends Byte> source) {
        return ImmutableByteCollections.copyOfList(source);
    }
}
