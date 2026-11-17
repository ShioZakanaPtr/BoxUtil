package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.SetExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.ByteFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public interface ByteSet extends SetExt<Byte>, ByteCollection {
    boolean contains(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #contains(byte)
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.contains(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    boolean add(byte e);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(byte)
     */
    @Deprecated
    default boolean add(Byte e) {
        return this.add(e != null ? e.byteValue() : (byte) 0);
    }

    boolean removeElement(byte o);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeElement(byte)
     */
    @Deprecated
    default boolean removeElement(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.removeElement(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    boolean remapping(byte element, @NotNull ByteFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(byte, ByteFunction)
     */
    @Deprecated
    default boolean remapping(Byte element, @NotNull Function<? super Byte, ? extends Byte> remappingFunction) {
        return this.remapping(element != null ? element.byteValue() : (byte) 0, (byte l_t) -> remappingFunction.apply(l_t));
    }

    @NotNull
    ByteIterator iterator();

    @NotNull
    ByteSpliterator spliterator();

}
