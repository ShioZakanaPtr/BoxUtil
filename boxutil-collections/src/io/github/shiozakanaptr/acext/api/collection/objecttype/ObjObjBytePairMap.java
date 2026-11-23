package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.TeConsumer;
import io.github.shiozakanaptr.acext.util.function.TeFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @see PairMap
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjObjBytePairMap<L, R> extends PairMap<L, R, Byte> {
    byte getRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object, Object)
     */
    @Deprecated
    default Byte get(Object keyA, Object keyB) {
        return this.getRaw(keyA, keyB);
    }

    byte getOrDefault(Object keyA, Object keyB, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, Object, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object keyA, Object keyB, Byte defaultValue) {
        return this.getOrDefault(keyA, keyB, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(L keyA, R keyB, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(L, R, byte)
     */
    @Deprecated
    default Byte put(L keyA, R keyB, Byte value) {
        return this.put(keyA, keyB, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(L keyA, R keyB, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(L, R, byte)
     */
    @Deprecated
    default Byte putIfAbsent(L keyA, R keyB, Byte value) {
        return this.putIfAbsent(keyA, keyB, value != null ? value.byteValue() : (byte) 0);
    }

    byte removeRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object, Object)
     */
    @Deprecated
    default Byte remove(Object keyA, Object keyB) {
        return this.removeRaw(keyA, keyB);
    }

    boolean remove(Object keyA, Object keyB, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, Object, byte)
     */
    @Deprecated
    default boolean remove(Object keyA, Object keyB, Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.remove(keyA, keyB, value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte replace(L keyA, R keyB, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, byte)
     */
    @Deprecated
    default Byte replace(L keyA, R keyB, Byte value) {
        return this.replace(keyA, keyB, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(L keyA, R keyB, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, byte, byte)
     */
    @Deprecated
    default boolean replace(L keyA, R keyB, Byte oldValue, Byte newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsentRaw(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(L, R, BiFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Byte> mappingFunction) {
        return this.computeIfAbsentRaw(keyA, keyB, (L l_t, R l_u) -> mappingFunction.apply(l_t, l_u));
    }

    byte computeIfPresent(L keyA, R keyB, @NotNull ObjObjByteTeFunction<? super L, ? super R, ? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(L, R, ObjObjByteTeFunction)
     */
    @Deprecated
    default Byte computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Byte, ? extends Byte> remappingFunction) {
        return this.computeIfPresent(keyA, keyB, (L l_t, R l_u, byte l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    byte compute(L keyA, R keyB, @NotNull ObjObjByteTeFunction<? super L, ? super R, ? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(L, R, ObjObjByteTeFunction)
     */
    @Deprecated
    default Byte compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Byte, ? extends Byte> remappingFunction) {
        return this.compute(keyA, keyB, (L l_t, R l_u, byte l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    byte merge(L keyA, R keyB, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(L, R, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(L keyA, R keyB, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return this.merge(keyA, keyB, value.byteValue(), (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(L keyA, R keyB, @NotNull ByteUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(L, R, ByteUnaryOperator)
     */
    @Deprecated
    default Byte update(L keyA, R keyB, @NotNull Function<? super Byte, ? extends Byte> updateFunction) {
        return this.update(keyA, keyB, (byte l_t) -> {
            final Byte result;
            return (result = updateFunction.apply(l_t)) != null ? result : (byte) 0;
        });
    }

    byte remapping(L keyA, R keyB, @NotNull ObjObjByteTeFunction<? super L, ? super R, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(L, R, ObjObjByteTeFunction, ByteByteBiFunction)
     */
    @Deprecated
    default Byte remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Byte, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        return this.remapping(keyA, keyB, (L l_t, R l_u, byte l_v) -> remappingFunction.apply(l_t, l_u, l_v), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(byte)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.containsValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    void forEach(@NotNull ObjObjByteTeConsumer<? super L, ? super R> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjObjByteTeConsumer)
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super L, ? super R, ? super Byte> action) {
        this.forEach((L l_t, R l_u, byte l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjObjByteTeOperator<? super L, ? super R> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjObjByteTeOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super Byte, ? extends Byte> function) {
        this.replaceAll((L l_t, R l_u, byte l_v) -> function.apply(l_t, l_u, l_v));
    }

    byte getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Byte getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    byte setDefaultReturnValue(byte newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(byte)
     */
    @Deprecated
    @NotNull
    default Byte setDefaultReturnValue(@Nullable Byte newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.byteValue() : (byte) 0);
    }

    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    ByteCollection valueCollection();

    @NotNull
    EntrySetExt<ObjObjBytePairMap.PairEntry<L, R>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<PairMap.PairEntry<L, R, Byte>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R> extends PairMap.PairEntry<L, R, Byte> {
        byte getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Byte getValue() {
            return this.getValueRaw();
        }

        byte setValue(byte newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(byte)
         */
        @Deprecated
        default Byte setValue(Byte newValue) {
            return this.setValue(newValue != null ? newValue.byteValue() : (byte) 0);
        }
    }
}
