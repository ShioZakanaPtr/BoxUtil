package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

/**
 * Type specialization interface.
 *
 * @see MapExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface ObjIntMap<K> extends MapExt<K, Integer> {
    int getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object)
     */
    @Deprecated
    default Integer get(Object key) {
        return this.getRaw(key);
    }

    int getOrDefault(Object key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, int)
     */
    @Deprecated
    default Integer put(K key, Integer value) {
        return this.put(key, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, int)
     */
    @Deprecated
    default Integer putIfAbsent(K key, Integer value) {
        return this.putIfAbsent(key, value != null ? value.intValue() : (int) 0);
    }

    int removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object)
     */
    @Deprecated
    default Integer remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, int)
     */
    @Deprecated
    default Integer replace(K key, Integer value) {
        return this.replace(key, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(K key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, int, int)
     */
    @Deprecated
    default boolean replace(K key, Integer oldValue, Integer newValue) {
        return this.replace(key, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Integer computeIfAbsent(K key, @NotNull Function<? super K, ? extends Integer> mappingFunction) {
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(K key, @NotNull ObjIntBiFunction<? super K, ? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(key, (K l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(K key, @NotNull ObjIntBiFunction<? super K, ? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjIntBiFunction)
     */
    @Deprecated
    default Integer compute(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(key, (K l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(K key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(K key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(key, value != null ? value.intValue() : (int) 0, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(K key, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(K, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(K key, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(key, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(K key, @NotNull ObjIntBiFunction<? super K, ? extends K> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(K, ObjIntBiFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends K> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(key, (K l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ObjIntBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Integer> action) {
        this.forEach((K l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ObjIntBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Integer, ? extends Integer> function) {
        this.replaceAll((K l_t, int l_u) -> function.apply(l_t, l_u));
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
    default Integer setDefaultReturnValue(Integer newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.intValue() : (int) 0);
    }

    @NotNull
    ObjSet<K> keySet();

    @NotNull
    IntCollection values();

    @NotNull
    EntrySetExt<ObjIntMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K> extends MapExt.Entry<K, Integer> {

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
        default Integer setValue(Integer value) {
            return this.setValue(value != null ? value.intValue() : (int) 0);
        }
    }

}
