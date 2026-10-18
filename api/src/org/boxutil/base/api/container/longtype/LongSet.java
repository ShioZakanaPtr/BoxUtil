package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.SetExt;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.LongFunction;

public interface LongSet extends SetExt<Long>, LongCollection {
    boolean contains(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.contains(o != null ? ((Long) o).longValue() : (long) 0);
    }

    boolean add(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Long e) {
        return this.add(e != null ? e.longValue() : (long) 0);
    }

    boolean remove(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.remove(o != null ? ((Long) o).longValue() : (long) 0);
    }

    boolean remapping(long element, @NotNull LongFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remapping(Long element, @NotNull Function<? super Long, ? extends Long> remappingFunction) {
        return this.remapping(element != null ? element.longValue() : (long) 0, (long l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull LongIterator iterator();

    @NotNull LongSpliterator spliterator();
}
