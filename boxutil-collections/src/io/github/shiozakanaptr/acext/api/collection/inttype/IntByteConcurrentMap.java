package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IntByteConcurrentMap extends IntByteMap, ConcurrentMapExt<Integer, Byte> {
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return IntByteMap.super.getOrDefault(key, defaultValue);
    }

    default Byte putIfAbsent(Integer key, Byte value) {
        return IntByteMap.super.putIfAbsent(key, value);
    }

    default boolean remove(Object key, Object value) {
        return IntByteMap.super.remove(key, value);
    }

    default Byte replace(Integer key, Byte value) {
        return IntByteMap.super.replace(key, value);
    }

    default boolean replace(Integer key, Byte oldValue, Byte newValue) {
        return IntByteMap.super.replace(key, oldValue, newValue);
    }

    default Byte computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Byte> mappingFunction) {
        return IntByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    default Byte computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return IntByteMap.super.computeIfPresent(key, remappingFunction);
    }

    default Byte compute(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return IntByteMap.super.compute(key, remappingFunction);
    }

    default Byte merge(Integer key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return IntByteMap.super.merge(key, value, remappingFunction);
    }

    default void forEach(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        IntByteMap.super.forEach(action);
    }

    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> function) {
        IntByteMap.super.replaceAll(function);
    }
}
