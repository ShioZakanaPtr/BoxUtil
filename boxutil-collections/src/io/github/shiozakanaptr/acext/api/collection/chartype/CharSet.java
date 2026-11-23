package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.util.function.chartype.CharFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

/**
 * Type specialization class.
 *
 * @see SetExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharSet extends SetExt<Character>, CharCollection {
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

    boolean remapping(char element, @NotNull CharFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(char, CharFunction)
     */
    @Deprecated
    default boolean remapping(Character element, @NotNull Function<? super Character, ? extends Character> remappingFunction) {
        return this.remapping(element != null ? element.charValue() : (char) 0, (char l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull
    CharIterator iterator();

    @NotNull
    CharSpliterator spliterator();

}
