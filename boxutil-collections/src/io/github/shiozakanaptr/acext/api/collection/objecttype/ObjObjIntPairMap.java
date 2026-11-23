package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface ObjObjIntPairMap<L, R> extends PairMap<L, R, Integer> {
    int getRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object, Object)
     */
    @Deprecated
    default Integer get(Object keyA, Object keyB) {
        return this.getRaw(keyA, keyB);
    }

    int getOrDefault(Object keyA, Object keyB, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, Object, int)
     */
    @Deprecated
    default Integer getOrDefault(Object keyA, Object keyB, Integer defaultValue) {
        return this.getOrDefault(keyA, keyB, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(L keyA, R keyB, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(L, R, int)
     */
    @Deprecated
    default Integer put(L keyA, R keyB, Integer value) {
        return this.put(keyA, keyB, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(L keyA, R keyB, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(L, R, int)
     */
    @Deprecated
    default Integer putIfAbsent(L keyA, R keyB, Integer value) {
        return this.putIfAbsent(keyA, keyB, value != null ? value.intValue() : (int) 0);
    }

    int removeRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object, Object)
     */
    @Deprecated
    default Integer remove(Object keyA, Object keyB) {
        return this.removeRaw(keyA, keyB);
    }

    boolean remove(Object keyA, Object keyB, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, Object, int)
     */
    @Deprecated
    default boolean remove(Object keyA, Object keyB, Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(keyA, keyB, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(L keyA, R keyB, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, int)
     */
    @Deprecated
    default Integer replace(L keyA, R keyB, Integer value) {
        return this.replace(keyA, keyB, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(L keyA, R keyB, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, int, int)
     */
    @Deprecated
    default boolean replace(L keyA, R keyB, Integer oldValue, Integer newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsentRaw(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(L, R, BiFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Integer> mappingFunction) {
        return this.computeIfAbsentRaw(keyA, keyB, (L l_t, R l_u) -> mappingFunction.apply(l_t, l_u));
    }

    int computeIfPresent(L keyA, R keyB, @NotNull ObjObjIntTeFunction<? super L, ? super R, ? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(L, R, ObjObjIntTeFunction)
     */
    @Deprecated
    default Integer computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(keyA, keyB, (L l_t, R l_u, int l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    int compute(L keyA, R keyB, @NotNull ObjObjIntTeFunction<? super L, ? super R, ? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(L, R, ObjObjIntTeFunction)
     */
    @Deprecated
    default Integer compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(keyA, keyB, (L l_t, R l_u, int l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    int merge(L keyA, R keyB, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(L, R, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(L keyA, R keyB, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(keyA, keyB, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(L keyA, R keyB, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(L, R, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(L keyA, R keyB, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(keyA, keyB, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(L keyA, R keyB, @NotNull ObjObjIntTeFunction<? super L, ? super R, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(L, R, ObjObjIntTeFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Integer, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(keyA, keyB, (L l_t, R l_u, int l_v) -> remappingFunction.apply(l_t, l_u, l_v), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(int)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.containsValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    void forEach(@NotNull ObjObjIntTeConsumer<? super L, ? super R> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjObjIntTeConsumer)
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super L, ? super R, ? super Integer> action) {
        this.forEach((L l_t, R l_u, int l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjObjIntTeOperator<? super L, ? super R> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjObjIntTeOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super Integer, ? extends Integer> function) {
        this.replaceAll((L l_t, R l_u, int l_v) -> function.apply(l_t, l_u, l_v));
    }

    int getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Integer getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    int setDefaultReturnValue(int newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(int)
     */
    @Deprecated
    @NotNull
    default Integer setDefaultReturnValue(@Nullable Integer newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.intValue() : (int) 0);
    }

    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    IntCollection valueCollection();

    @NotNull
    EntrySetExt<ObjObjIntPairMap.PairEntry<L, R>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<PairMap.PairEntry<L, R, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R> extends PairMap.PairEntry<L, R, Integer> {
        int getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Integer getValue() {
            return this.getValueRaw();
        }

        int setValue(int newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(int)
         */
        @Deprecated
        default Integer setValue(Integer newValue) {
            return this.setValue(newValue != null ? newValue.intValue() : (int) 0);
        }
    }
}
