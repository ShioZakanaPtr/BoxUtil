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
     */
    @Deprecated
    default boolean contains(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.contains(o != null ? ((Byte) o).byteValue() : (byte) 0);
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
        if (o != null && !(o instanceof Byte)) return false;
        return this.remove(o != null ? ((Byte) o).byteValue() : (byte) 0);
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

    static ByteSet of() {
        return ImmutableByteCollections.ofSet();
    }

    static ByteSet of(byte element) {
        return ImmutableByteCollections.ofSet(element);
    }

    static ByteSet of(byte... elements) {
        return ImmutableByteCollections.ofSet(elements);
    }

    static ByteSet copyOf(@NotNull Collection<? extends Byte> source) {
        return ImmutableByteCollections.copyOfSet(source);
    }
}
