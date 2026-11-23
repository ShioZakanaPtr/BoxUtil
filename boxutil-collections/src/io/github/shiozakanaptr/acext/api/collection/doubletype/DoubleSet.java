package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.DoubleFunction;
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
public interface DoubleSet extends SetExt<Double>, DoubleCollection {
    boolean contains(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(double)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.contains(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    boolean add(double e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(double)
     */
    @Deprecated
    default boolean add(Double e) {
        return this.add(e != null ? e.doubleValue() : (double) 0);
    }

    boolean removeElement(double o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(double)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.removeElement(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    boolean remapping(double element, @NotNull DoubleFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(double, DoubleFunction)
     */
    @Deprecated
    default boolean remapping(Double element, @NotNull Function<? super Double, ? extends Double> remappingFunction) {
        return this.remapping(element != null ? element.doubleValue() : (double) 0, (double l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull
    DoubleIterator iterator();

    @NotNull
    DoubleSpliterator spliterator();

}
