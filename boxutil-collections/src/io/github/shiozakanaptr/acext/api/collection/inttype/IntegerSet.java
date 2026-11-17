package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.util.function.inttype.IntFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public interface IntegerSet extends SetExt<Integer>, IntCollection {
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

    boolean add(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int)
     */
    @Deprecated
    default boolean add(Integer e) {
        return this.add(e != null ? e.intValue() : (int) 0);
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

    boolean remapping(int element, @NotNull IntegerFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(int, IntegerFunction)
     */
    @Deprecated
    default boolean remapping(Integer element, @NotNull Function<? super Integer, ? extends Integer> remappingFunction) {
        return this.remapping(element != null ? element.intValue() : (int) 0, (int l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull
    IntIterator iterator();

    @NotNull
    IntSpliterator spliterator();

    @NotNull
    static IntSet of() {
        return ImmutableIntCollections.ofSet();
    }

    @NotNull
    static IntSet of(int element) {
        return ImmutableIntCollections.ofSet(element);
    }

    @NotNull
    static IntSet of(int... elements) {
        return ImmutableIntCollections.ofSet(elements);
    }

    @NotNull
    static IntSet copyOf(@NotNull Collection<? extends Integer> source) {
        return ImmutableIntCollections.copyOfSet(source);
    }
}
