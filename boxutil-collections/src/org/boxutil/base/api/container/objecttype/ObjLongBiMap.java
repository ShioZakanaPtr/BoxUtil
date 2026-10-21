package org.boxutil.base.api.container.objecttype;

import com.fs.starfarer.api.util.Pair;
import org.boxutil.base.api.container.BiMap;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.longtype.LongCollection;
import org.boxutil.util.function.longtype.LongLongBiFunction;
import org.boxutil.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;

public interface ObjLongBiMap<X, Y> extends BiMap<X, Y, Long> {
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

    long put(X keyA, Y keyB, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long put(X keyA, Y keyB, Long value) {
        return this.put(keyA, keyB, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(X keyA, Y keyB, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long putIfAbsent(X keyA, Y keyB, Long value) {
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

    long replace(X keyA, Y keyB, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long replace(X keyA, Y keyB, Long value) {
        return this.replace(keyA, keyB, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(X keyA, Y keyB, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(X keyA, Y keyB, Long oldValue, Long newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsentRaw(X keyA, Y keyB, @NotNull BiFunction<? super X, ? super Y, ? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfAbsent(X keyA, Y keyB, @NotNull BiFunction<? super X, ? super Y, ? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsentRaw(keyA, keyB, (X l_t, Y l_u) -> mappingFunction.apply(l_t, l_u));
    }

    long computeIfPresent(X keyA, Y keyB, @NotNull ObjLongTeFunction<? super X, ? super Y, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfPresent(X keyA, Y keyB, @NotNull TeFunction<? super X, ? super Y, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(keyA, keyB, (X l_t, Y l_u, long l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    long compute(X keyA, Y keyB, @NotNull ObjLongTeFunction<? super X, ? super Y, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long compute(X keyA, Y keyB, @NotNull TeFunction<? super X, ? super Y, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(keyA, keyB, (X l_t, Y l_u, long l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    long merge(X keyA, Y keyB, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long merge(X keyA, Y keyB, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(keyA, keyB, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(X keyA, Y keyB, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long update(X keyA, Y keyB, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(keyA, keyB, (long l_t) -> {
            final Long result = updateFunction.apply(l_t);
            return result != null ? result : (long) 0;
        });
    }

    long remapping(X keyA, Y keyB, @NotNull ObjLongTeFunction<? super X, ? super Y, ? extends Pair<? extends X, ? extends Y>> remappingFunction, @NotNull LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remapping(X keyA, Y keyB, @NotNull TeFunction<? super X, ? super Y, ? super Long, ? extends Pair<? extends X, ? extends Y>> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(keyA, keyB, (X l_t, Y l_u, long l_v) -> remappingFunction.apply(l_t, l_u, l_v), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ObjLongTeConsumer<? super X, ? super Y> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super X, ? super Y, ? super Long> action) {
        Objects.requireNonNull(action);
        this.forEach((X l_t, Y l_u, long l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjLongTeOperator<? super X, ? super Y> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super X, ? super Y, ? super Long, ? extends Long> function) {
        Objects.requireNonNull(function);
        this.replaceAll((X l_t, Y l_u, long l_v) -> function.apply(l_t, l_u, l_v));
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

    LongCollection valueCollection();

    SetExt<ObjLongBiMap.BiEntry<X, Y>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default SetExt<BiMap.BiEntry<X, Y, Long>> entrySet() {
        return (SetExt) this.entrySetRaw();
    }

    interface BiEntry<X, Y> extends BiMap.BiEntry<X, Y, Long> {
        long getValueRaw();

        default Long getValue() {
            return this.getValueRaw();
        }

        long setValue(long newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long setValue(Long newValue) {
            return this.setValue(newValue != null ? newValue.longValue() : (long) 0);
        }
    }
}
