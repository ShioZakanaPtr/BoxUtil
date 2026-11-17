package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface ShortIntMap extends MapExt<Short, Integer> {
    int get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(short)
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    int getOrDefault(short key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, int)
     */
    @Deprecated
    default Integer put(Short key, Integer value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, int)
     */
    @Deprecated
    default Integer putIfAbsent(Short key, Integer value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short)
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, int)
     */
    @Deprecated
    default Integer replace(Short key, Integer value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(short key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, int, int)
     */
    @Deprecated
    default boolean replace(Short key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(short key, @NotNull ShortFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Integer> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (Short l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(short key, @NotNull ShortIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(short key, @NotNull ShortIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortIntBiFunction)
     */
    @Deprecated
    default Integer compute(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(short key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Short key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(short key, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(short, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(Short key, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(key != null ? key.shortValue() : (short) 0, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(short key, @NotNull ShortIntBiFunction<? extends Short> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(short, ShortIntBiFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Short> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(short from, short to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(short, short)
     */
    @Deprecated
    default boolean swapAt(Short from, Short to) {
        return this.swapAt(from != null ? from.shortValue() : (short) 0, to != null ? to.shortValue() : (short) 0);
    }

    boolean containsKey(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(short)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Short)) return false;
        return this.containsKey(key != null ? ((Short) key).shortValue() : (short) 0);
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

    void forEach(@NotNull ShortIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        this.forEach((short l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> function) {
        this.replaceAll((short l_t, int l_u) -> function.apply(l_t, l_u));
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
    ShortSet keySet();

    @NotNull
    IntCollection values();

    @NotNull
    EntrySetExt<ShortIntMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Short, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Short, Integer> {
        short getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Short getKey() {
            return this.getKeyRaw();
        }

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
