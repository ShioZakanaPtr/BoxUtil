package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
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
public interface ObjObjFloatPairMap<L, R> extends PairMap<L, R, Float> {
    float getRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object, Object)
     */
    @Deprecated
    default Float get(Object keyA, Object keyB) {
        return this.getRaw(keyA, keyB);
    }

    float getOrDefault(Object keyA, Object keyB, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, Object, float)
     */
    @Deprecated
    default Float getOrDefault(Object keyA, Object keyB, Float defaultValue) {
        return this.getOrDefault(keyA, keyB, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(L keyA, R keyB, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(L, R, float)
     */
    @Deprecated
    default Float put(L keyA, R keyB, Float value) {
        return this.put(keyA, keyB, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(L keyA, R keyB, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(L, R, float)
     */
    @Deprecated
    default Float putIfAbsent(L keyA, R keyB, Float value) {
        return this.putIfAbsent(keyA, keyB, value != null ? value.floatValue() : (float) 0);
    }

    float removeRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object, Object)
     */
    @Deprecated
    default Float remove(Object keyA, Object keyB) {
        return this.removeRaw(keyA, keyB);
    }

    boolean remove(Object keyA, Object keyB, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, Object, float)
     */
    @Deprecated
    default boolean remove(Object keyA, Object keyB, Object value) {
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(keyA, keyB, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(L keyA, R keyB, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, float)
     */
    @Deprecated
    default Float replace(L keyA, R keyB, Float value) {
        return this.replace(keyA, keyB, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(L keyA, R keyB, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, float, float)
     */
    @Deprecated
    default boolean replace(L keyA, R keyB, Float oldValue, Float newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsentRaw(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(L, R, BiFunction)
     */
    @Deprecated
    default Float computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Float> mappingFunction) {
        return this.computeIfAbsentRaw(keyA, keyB, (L l_t, R l_u) -> mappingFunction.apply(l_t, l_u));
    }

    float computeIfPresent(L keyA, R keyB, @NotNull ObjObjFloatTeFunction<? super L, ? super R, ? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(L, R, ObjObjFloatTeFunction)
     */
    @Deprecated
    default Float computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(keyA, keyB, (L l_t, R l_u, float l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    float compute(L keyA, R keyB, @NotNull ObjObjFloatTeFunction<? super L, ? super R, ? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(L, R, ObjObjFloatTeFunction)
     */
    @Deprecated
    default Float compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(keyA, keyB, (L l_t, R l_u, float l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    float merge(L keyA, R keyB, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(L, R, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(L keyA, R keyB, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(keyA, keyB, value.floatValue(), (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(L keyA, R keyB, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(L, R, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(L keyA, R keyB, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(keyA, keyB, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(L keyA, R keyB, @NotNull ObjObjFloatTeFunction<? super L, ? super R, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(L, R, ObjObjFloatTeFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Float, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(keyA, keyB, (L l_t, R l_u, float l_v) -> remappingFunction.apply(l_t, l_u, l_v), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(float)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Float)) return false;
        return this.containsValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    void forEach(@NotNull ObjObjFloatTeConsumer<? super L, ? super R> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjObjFloatTeConsumer)
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super L, ? super R, ? super Float> action) {
        this.forEach((L l_t, R l_u, float l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjObjFloatTeOperator<? super L, ? super R> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjObjFloatTeOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super Float, ? extends Float> function) {
        this.replaceAll((L l_t, R l_u, float l_v) -> function.apply(l_t, l_u, l_v));
    }

    float getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Float getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    float setDefaultReturnValue(float newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(float)
     */
    @Deprecated
    @NotNull
    default Float setDefaultReturnValue(@Nullable Float newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.floatValue() : (float) 0);
    }

    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    FloatCollection valueCollection();

    @NotNull
    EntrySetExt<ObjObjFloatPairMap.PairEntry<L, R>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<PairMap.PairEntry<L, R, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R> extends PairMap.PairEntry<L, R, Float> {
        float getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Float getValue() {
            return this.getValueRaw();
        }

        float setValue(float newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(float)
         */
        @Deprecated
        default Float setValue(Float newValue) {
            return this.setValue(newValue != null ? newValue.floatValue() : (float) 0);
        }
    }
}
