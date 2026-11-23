package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface ObjObjShortPairMap<L, R> extends PairMap<L, R, Short> {
    short getRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object, Object)
     */
    @Deprecated
    default Short get(Object keyA, Object keyB) {
        return this.getRaw(keyA, keyB);
    }

    short getOrDefault(Object keyA, Object keyB, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, Object, short)
     */
    @Deprecated
    default Short getOrDefault(Object keyA, Object keyB, Short defaultValue) {
        return this.getOrDefault(keyA, keyB, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(L keyA, R keyB, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(L, R, short)
     */
    @Deprecated
    default Short put(L keyA, R keyB, Short value) {
        return this.put(keyA, keyB, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(L keyA, R keyB, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(L, R, short)
     */
    @Deprecated
    default Short putIfAbsent(L keyA, R keyB, Short value) {
        return this.putIfAbsent(keyA, keyB, value != null ? value.shortValue() : (short) 0);
    }

    short removeRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object, Object)
     */
    @Deprecated
    default Short remove(Object keyA, Object keyB) {
        return this.removeRaw(keyA, keyB);
    }

    boolean remove(Object keyA, Object keyB, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, Object, short)
     */
    @Deprecated
    default boolean remove(Object keyA, Object keyB, Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(keyA, keyB, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(L keyA, R keyB, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, short)
     */
    @Deprecated
    default Short replace(L keyA, R keyB, Short value) {
        return this.replace(keyA, keyB, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(L keyA, R keyB, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, short, short)
     */
    @Deprecated
    default boolean replace(L keyA, R keyB, Short oldValue, Short newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsentRaw(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(L, R, BiFunction)
     */
    @Deprecated
    default Short computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Short> mappingFunction) {
        return this.computeIfAbsentRaw(keyA, keyB, (L l_t, R l_u) -> mappingFunction.apply(l_t, l_u));
    }

    short computeIfPresent(L keyA, R keyB, @NotNull ObjObjShortTeFunction<? super L, ? super R, ? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(L, R, ObjObjShortTeFunction)
     */
    @Deprecated
    default Short computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(keyA, keyB, (L l_t, R l_u, short l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    short compute(L keyA, R keyB, @NotNull ObjObjShortTeFunction<? super L, ? super R, ? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(L, R, ObjObjShortTeFunction)
     */
    @Deprecated
    default Short compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(keyA, keyB, (L l_t, R l_u, short l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    short merge(L keyA, R keyB, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(L, R, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(L keyA, R keyB, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(keyA, keyB, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(L keyA, R keyB, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(L, R, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(L keyA, R keyB, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(keyA, keyB, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(L keyA, R keyB, @NotNull ObjObjShortTeFunction<? super L, ? super R, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(L, R, ObjObjShortTeFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Short, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(keyA, keyB, (L l_t, R l_u, short l_v) -> remappingFunction.apply(l_t, l_u, l_v), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(short)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull ObjObjShortTeConsumer<? super L, ? super R> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjObjShortTeConsumer)
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super L, ? super R, ? super Short> action) {
        this.forEach((L l_t, R l_u, short l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjObjShortTeOperator<? super L, ? super R> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjObjShortTeOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super Short, ? extends Short> function) {
        this.replaceAll((L l_t, R l_u, short l_v) -> function.apply(l_t, l_u, l_v));
    }

    short getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Short getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    short setDefaultReturnValue(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(short)
     */
    @Deprecated
    @NotNull
    default Short setDefaultReturnValue(@Nullable Short newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    ShortCollection valueCollection();

    @NotNull
    EntrySetExt<ObjObjShortPairMap.PairEntry<L, R>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<PairMap.PairEntry<L, R, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R> extends PairMap.PairEntry<L, R, Short> {
        short getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Short getValue() {
            return this.getValueRaw();
        }

        short setValue(short newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(short)
         */
        @Deprecated
        default Short setValue(Short newValue) {
            return this.setValue(newValue != null ? newValue.shortValue() : (short) 0);
        }
    }
}
