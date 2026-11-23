package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatFunction;
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
public interface FloatSet extends SetExt<Float>, FloatCollection {
    boolean contains(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(float)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.contains(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    boolean add(float e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(float)
     */
    @Deprecated
    default boolean add(Float e) {
        return this.add(e != null ? e.floatValue() : (float) 0);
    }

    boolean removeElement(float o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(float)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.removeElement(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    boolean remapping(float element, @NotNull FloatFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(float, FloatFunction)
     */
    @Deprecated
    default boolean remapping(Float element, @NotNull Function<? super Float, ? extends Float> remappingFunction) {
        return this.remapping(element != null ? element.floatValue() : (float) 0, (float l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull
    FloatIterator iterator();

    @NotNull
    FloatSpliterator spliterator();

}
