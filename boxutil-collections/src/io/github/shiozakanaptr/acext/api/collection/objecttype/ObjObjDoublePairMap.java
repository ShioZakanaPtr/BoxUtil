package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface ObjObjDoublePairMap<L, R> extends PairMap<L, R, Double> {
    double getRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object, Object)
     */
    @Deprecated
    default Double get(Object keyA, Object keyB) {
        return this.getRaw(keyA, keyB);
    }

    double getOrDefault(Object keyA, Object keyB, double defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, Object, double)
     */
    @Deprecated
    default Double getOrDefault(Object keyA, Object keyB, Double defaultValue) {
        return this.getOrDefault(keyA, keyB, defaultValue != null ? defaultValue.doubleValue() : (double) 0);
    }

    double put(L keyA, R keyB, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(L, R, double)
     */
    @Deprecated
    default Double put(L keyA, R keyB, Double value) {
        return this.put(keyA, keyB, value != null ? value.doubleValue() : (double) 0);
    }

    double putIfAbsent(L keyA, R keyB, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(L, R, double)
     */
    @Deprecated
    default Double putIfAbsent(L keyA, R keyB, Double value) {
        return this.putIfAbsent(keyA, keyB, value != null ? value.doubleValue() : (double) 0);
    }

    double removeRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object, Object)
     */
    @Deprecated
    default Double remove(Object keyA, Object keyB) {
        return this.removeRaw(keyA, keyB);
    }

    boolean remove(Object keyA, Object keyB, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, Object, double)
     */
    @Deprecated
    default boolean remove(Object keyA, Object keyB, Object value) {
        if (value != null && !(value instanceof Double)) return false;
        return this.remove(keyA, keyB, value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    double replace(L keyA, R keyB, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, double)
     */
    @Deprecated
    default Double replace(L keyA, R keyB, Double value) {
        return this.replace(keyA, keyB, value != null ? value.doubleValue() : (double) 0);
    }

    boolean replace(L keyA, R keyB, double oldValue, double newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, double, double)
     */
    @Deprecated
    default boolean replace(L keyA, R keyB, Double oldValue, Double newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.doubleValue() : (double) 0, newValue != null ? newValue.doubleValue() : (double) 0);
    }

    double computeIfAbsentRaw(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Double> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(L, R, BiFunction)
     */
    @Deprecated
    default Double computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Double> mappingFunction) {
        return this.computeIfAbsentRaw(keyA, keyB, (L l_t, R l_u) -> mappingFunction.apply(l_t, l_u));
    }

    double computeIfPresent(L keyA, R keyB, @NotNull ObjObjDoubleTeFunction<? super L, ? super R, ? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(L, R, ObjObjDoubleTeFunction)
     */
    @Deprecated
    default Double computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Double, ? extends Double> remappingFunction) {
        return this.computeIfPresent(keyA, keyB, (L l_t, R l_u, double l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    double compute(L keyA, R keyB, @NotNull ObjObjDoubleTeFunction<? super L, ? super R, ? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(L, R, ObjObjDoubleTeFunction)
     */
    @Deprecated
    default Double compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Double, ? extends Double> remappingFunction) {
        return this.compute(keyA, keyB, (L l_t, R l_u, double l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    double merge(L keyA, R keyB, double value, @NotNull DoubleDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(L, R, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(L keyA, R keyB, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return this.merge(keyA, keyB, value.doubleValue(), (double l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double update(L keyA, R keyB, @NotNull DoubleUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(L, R, DoubleUnaryOperator)
     */
    @Deprecated
    default Double update(L keyA, R keyB, @NotNull Function<? super Double, ? extends Double> updateFunction) {
        return this.update(keyA, keyB, (double l_t) -> {
            final Double result;
            return (result = updateFunction.apply(l_t)) != null ? result : (double) 0;
        });
    }

    double remapping(L keyA, R keyB, @NotNull ObjObjDoubleTeFunction<? super L, ? super R, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull DoubleDoubleBiFunction<? extends Double> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(L, R, ObjObjDoubleTeFunction, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Double, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> mergeFunction) {
        return this.remapping(keyA, keyB, (L l_t, R l_u, double l_v) -> remappingFunction.apply(l_t, l_u, l_v), (double l_t, double l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(double)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Double)) return false;
        return this.containsValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    void forEach(@NotNull ObjObjDoubleTeConsumer<? super L, ? super R> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjObjDoubleTeConsumer)
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super L, ? super R, ? super Double> action) {
        this.forEach((L l_t, R l_u, double l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjObjDoubleTeOperator<? super L, ? super R> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjObjDoubleTeOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super Double, ? extends Double> function) {
        this.replaceAll((L l_t, R l_u, double l_v) -> function.apply(l_t, l_u, l_v));
    }

    double getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Double getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    double setDefaultReturnValue(double newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(double)
     */
    @Deprecated
    @NotNull
    default Double setDefaultReturnValue(@Nullable Double newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.doubleValue() : (double) 0);
    }

    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    DoubleCollection valueCollection();

    @NotNull
    EntrySetExt<ObjObjDoublePairMap.PairEntry<L, R>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<PairMap.PairEntry<L, R, Double>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R> extends PairMap.PairEntry<L, R, Double> {
        double getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Double getValue() {
            return this.getValueRaw();
        }

        double setValue(double newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(double)
         */
        @Deprecated
        default Double setValue(Double newValue) {
            return this.setValue(newValue != null ? newValue.doubleValue() : (double) 0);
        }
    }
}
