package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.SetExt;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface IntSet extends SetExt<Integer> {
    boolean contains(int o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        return this.contains(o instanceof Integer number ? number.intValue() : (int) 0);
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
        return this.remove(o instanceof Integer number ? number.intValue() : (int) 0);
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
