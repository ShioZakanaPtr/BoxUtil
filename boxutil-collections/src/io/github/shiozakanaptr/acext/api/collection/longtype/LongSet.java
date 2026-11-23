package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.util.function.longtype.LongFunction;
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
public interface LongSet extends SetExt<Long>, LongCollection {
    boolean contains(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(long)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.contains(o != null ? ((Long) o).longValue() : (long) 0);
    }

    boolean add(long e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(long)
     */
    @Deprecated
    default boolean add(Long e) {
        return this.add(e != null ? e.longValue() : (long) 0);
    }

    boolean removeElement(long o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(long)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.removeElement(o != null ? ((Long) o).longValue() : (long) 0);
    }

    boolean remapping(long element, @NotNull LongFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongFunction)
     */
    @Deprecated
    default boolean remapping(Long element, @NotNull Function<? super Long, ? extends Long> remappingFunction) {
        return this.remapping(element != null ? element.longValue() : (long) 0, (long l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull
    LongIterator iterator();

    @NotNull
    LongSpliterator spliterator();

}
