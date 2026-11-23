package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.CollectionExt;

import io.github.shiozakanaptr.acext.util.function.chartype.CharPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Type specialization interface.
 *
 * @see CollectionExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharCollection extends CollectionExt<Character>, CharIterable {
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

    boolean add(char e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(char)
     */
    @Deprecated
    default boolean add(Character e) {
        return this.add(e != null ? e.charValue() : (char) 0);
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

    default boolean removeIf(@NotNull CharPredicate filter) {
        boolean removed = false;
        final CharIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextChar())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeIf(CharPredicate)
     */
    @Deprecated
    default boolean removeIf(@NotNull Predicate<? super Character> filter) {
        return this.removeIf((char l_t) -> filter.test(l_t));
    }

    default boolean addAll(@NotNull char[] elements) {
        boolean result = false;
        for (char element : elements) {
            result |= this.add(element);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(char[])
     */
    @Deprecated
    default <T extends Character> boolean addAll(@NotNull T[] elements) {
        return CollectionExt.super.addAll(elements);
    }

    default boolean addAll(@NotNull char[] elements, int srcIndex, int length) {
        boolean result = false;
        for (int i = srcIndex, limit = srcIndex + length; i < limit; i++) {
            result |= this.add(elements[i]);
        }
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #addAll(char[], int, int)
     */
    @Deprecated
    default <T extends Character> boolean addAll(@NotNull T[] elements, int srcIndex, int length) {
        return CollectionExt.super.addAll(elements, srcIndex, length);
    }

    @NotNull
    CharIterator iterator();

    @NotNull
    CharSpliterator spliterator();

    @NotNull
    char[] toArrayRaw();

    @NotNull
    char[] toArray(@NotNull char[] a);

    @NotNull
    default IntStream streamRaw() {
        return StreamSupport.intStream(CharSpliterator.wrap(this.spliterator()), false);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #streamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Character> stream() {
        return CollectionExt.super.stream();
    }

    @NotNull
    default IntStream parallelStreamRaw() {
        return StreamSupport.intStream(CharSpliterator.wrap(this.spliterator()), true);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #parallelStreamRaw()
     */
    @Deprecated
    @NotNull
    default Stream<Character> parallelStream() {
        return CollectionExt.super.parallelStream();
    }
}
