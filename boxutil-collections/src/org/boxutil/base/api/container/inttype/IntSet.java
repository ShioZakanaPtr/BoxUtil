package org.boxutil.base.api.container.inttype;

import org.boxutil.base.api.container.SetExt;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.IntFunction;

public interface IntSet extends SetExt<Integer>, IntCollection {
    boolean contains(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.contains(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    boolean add(int e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Integer e) {
        return this.add(e != null ? e.intValue() : (int) 0);
    }

    boolean remove(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.remove(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    boolean remapping(int element, @NotNull IntFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remapping(Integer element, @NotNull Function<? super Integer, ? extends Integer> remappingFunction) {
        return this.remapping(element != null ? element.intValue() : (int) 0, (int l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull IntIterator iterator();

    @NotNull IntSpliterator spliterator();
}
