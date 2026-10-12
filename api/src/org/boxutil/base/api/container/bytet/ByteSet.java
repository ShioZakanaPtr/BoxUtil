package org.boxutil.base.api.container.bytet;

import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.bytet.ByteFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface ByteSet extends SetExt<Byte> {
    boolean contains(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean contains(Object o) {
        return this.contains(o instanceof Byte number ? number.byteValue() : (byte) 0);
    }

    boolean add(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Byte e) {
        return this.add(e != null ? e.byteValue() : (byte) 0);
    }

    boolean remove(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object o) {
        return this.remove(o instanceof Byte number ? number.byteValue() : (byte) 0);
    }

    boolean remapping(byte element, @NotNull ByteFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remapping(Byte element, @NotNull Function<? super Byte, ? extends Byte> remappingFunction) {
        return this.remapping(element != null ? element.byteValue() : (byte) 0, (byte l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull ByteIterator iterator();

    @NotNull ByteSpliterator spliterator();
}
