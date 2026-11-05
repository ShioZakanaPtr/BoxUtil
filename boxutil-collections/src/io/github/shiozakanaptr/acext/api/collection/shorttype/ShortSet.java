package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ShortSet extends SetExt<Short>, ShortCollection {
    boolean contains(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.contains(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    boolean add(short e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Short e) {
        return this.add(e != null ? e.shortValue() : (short) 0);
    }

    boolean removeElement(short o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.removeElement(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    boolean remapping(short element, @NotNull ShortFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remapping(Short element, @NotNull Function<? super Short, ? extends Short> remappingFunction) {
        return this.remapping(element != null ? element.shortValue() : (short) 0, (short l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull ShortIterator iterator();

    @NotNull ShortSpliterator spliterator();
}
