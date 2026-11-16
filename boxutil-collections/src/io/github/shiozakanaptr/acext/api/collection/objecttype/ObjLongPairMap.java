package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.util.function.TeConsumer;
import io.github.shiozakanaptr.acext.util.function.TeFunction;
import io.github.shiozakanaptr.acext.util.function.longtype.LongLongBiFunction;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;

public interface ObjLongPairMap<L, R> extends PairMap<L, R, Long> {
    long getRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long get(Object keyA, Object keyB) {
        return this.getRaw(keyA, keyB);
    }

    long getOrDefault(Object keyA, Object keyB, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getOrDefault(Object keyA, Object keyB, Long defaultValue) {
        return this.getOrDefault(keyA, keyB, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(L keyA, R keyB, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long put(L keyA, R keyB, Long value) {
        return this.put(keyA, keyB, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(L keyA, R keyB, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long putIfAbsent(L keyA, R keyB, Long value) {
        return this.putIfAbsent(keyA, keyB, value != null ? value.longValue() : (long) 0);
    }

    long removeRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remove(Object keyA, Object keyB) {
        return this.removeRaw(keyA, keyB);
    }

    boolean remove(Object keyA, Object keyB, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object keyA, Object keyB, Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(keyA, keyB, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(L keyA, R keyB, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long replace(L keyA, R keyB, Long value) {
        return this.replace(keyA, keyB, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(L keyA, R keyB, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(L keyA, R keyB, Long oldValue, Long newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsentRaw(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsentRaw(keyA, keyB, (L l_t, R l_u) -> mappingFunction.apply(l_t, l_u));
    }

    long computeIfPresent(L keyA, R keyB, @NotNull ObjLongTeFunction<? super L, ? super R, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(keyA, keyB, (L l_t, R l_u, long l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    long compute(L keyA, R keyB, @NotNull ObjLongTeFunction<? super L, ? super R, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(keyA, keyB, (L l_t, R l_u, long l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    long merge(L keyA, R keyB, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long merge(L keyA, R keyB, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(keyA, keyB, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(L keyA, R keyB, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long update(L keyA, R keyB, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(keyA, keyB, (long l_t) -> {
            final Long result = updateFunction.apply(l_t);
            return result != null ? result : (long) 0;
        });
    }

    long remapping(L keyA, R keyB, @NotNull ObjLongTeFunction<? super L, ? super R, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Long, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(keyA, keyB, (L l_t, R l_u, long l_v) -> remappingFunction.apply(l_t, l_u, l_v), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.containsValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    void forEach(@NotNull ObjLongTeConsumer<? super L, ? super R> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super L, ? super R, ? super Long> action) {
        Objects.requireNonNull(action);
        this.forEach((L l_t, R l_u, long l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjLongTeOperator<? super L, ? super R> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super Long, ? extends Long> function) {
        Objects.requireNonNull(function);
        this.replaceAll((L l_t, R l_u, long l_v) -> function.apply(l_t, l_u, l_v));
    }

    long getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    long setDefaultReturnValue(long newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long setDefaultReturnValue(@Nullable Long newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.longValue() : (long) 0);
    }

    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    LongCollection valueCollection();

    @NotNull
    EntrySetExt<ObjLongPairMap.PairEntry<L, R>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default EntrySetExt<PairMap.PairEntry<L, R, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R> extends PairMap.PairEntry<L, R, Long> {
        long getValueRaw();

        default Long getValue() {
            return this.getValueRaw();
        }

        long setValue(long newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(long)
         */
        @Deprecated
        default Long setValue(Long newValue) {
            return this.setValue(newValue != null ? newValue.longValue() : (long) 0);
        }
    }
}
