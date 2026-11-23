package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface CharDoubleMap extends MapExt<Character, Double> {
    double get(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(char)
     */
    @Deprecated
    default Double get(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Character) key).charValue() : (char) 0);
    }

    double getOrDefault(char key, double defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        if (key != null && !(key instanceof Character)) return defaultValue;
        return this.getOrDefault(key != null ? ((Character) key).charValue() : (char) 0, defaultValue != null ? defaultValue.doubleValue() : (double) 0);
    }

    double put(char key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, double)
     */
    @Deprecated
    default Double put(Character key, Double value) {
        return this.put(key != null ? key.charValue() : (char) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putIfAbsent(char key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, double)
     */
    @Deprecated
    default Double putIfAbsent(Character key, Double value) {
        return this.putIfAbsent(key != null ? key.charValue() : (char) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double remove(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char)
     */
    @Deprecated
    default Double remove(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0);
    }

    boolean remove(char key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0, value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    double replace(char key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, double)
     */
    @Deprecated
    default Double replace(Character key, Double value) {
        return this.replace(key != null ? key.charValue() : (char) 0, value != null ? value.doubleValue() : (double) 0);
    }

    boolean replace(char key, double oldValue, double newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, double, double)
     */
    @Deprecated
    default boolean replace(Character key, Double oldValue, Double newValue) {
        return this.replace(key != null ? key.charValue() : (char) 0, oldValue != null ? oldValue.doubleValue() : (double) 0, newValue != null ? newValue.doubleValue() : (double) 0);
    }

    double computeIfAbsent(char key, @NotNull CharFunction<? extends Double> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Double> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.charValue() : (char) 0, (Character l_t) -> mappingFunction.apply(l_t));
    }

    double computeIfPresent(char key, @NotNull CharDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
        return this.computeIfPresent(key != null ? key.charValue() : (char) 0, (char l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double compute(char key, @NotNull CharDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Character key, @NotNull BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
        return this.compute(key != null ? key.charValue() : (char) 0, (char l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double merge(char key, double value, @NotNull DoubleDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Character key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return this.merge(key != null ? key.charValue() : (char) 0, value.doubleValue(), (double l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double update(char key, @NotNull DoubleUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(char, DoubleUnaryOperator)
     */
    @Deprecated
    default Double update(Character key, @NotNull Function<? super Double, ? extends Double> updateFunction) {
        return this.update(key != null ? key.charValue() : (char) 0, (double l_t) -> {
            final Double result;
            return (result = updateFunction.apply(l_t)) != null ? result : (double) 0;
        });
    }

    double remapping(char key, @NotNull CharDoubleBiFunction<? extends Character> remappingFunction, DoubleDoubleBiFunction<? extends Double> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(char, CharDoubleBiFunction, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double remapping(Character key, @NotNull BiFunction<? super Character, ? super Double, ? extends Character> remappingFunction, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> mergeFunction) {
        return this.remapping(key != null ? key.charValue() : (char) 0, (char l_t, double l_u) -> remappingFunction.apply(l_t, l_u), (double l_t, double l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(char from, char to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(char, char)
     */
    @Deprecated
    default boolean swapAt(Character from, Character to) {
        return this.swapAt(from != null ? from.charValue() : (char) 0, to != null ? to.charValue() : (char) 0);
    }

    boolean containsKey(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(char)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Character)) return false;
        return this.containsKey(key != null ? ((Character) key).charValue() : (char) 0);
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

    void forEach(@NotNull CharDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Double> action) {
        this.forEach((char l_t, double l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull CharDoubleBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Double, ? extends Double> function) {
        this.replaceAll((char l_t, double l_u) -> function.apply(l_t, l_u));
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
    default Double setDefaultReturnValue(Double newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.doubleValue() : (double) 0);
    }

    @NotNull
    CharSet keySet();

    @NotNull
    DoubleCollection values();

    @NotNull
    EntrySetExt<CharDoubleMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Character, Double>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Character, Double> {
        char getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Character getKey() {
            return this.getKeyRaw();
        }

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
        default Double setValue(Double value) {
            return this.setValue(value != null ? value.doubleValue() : (double) 0);
        }
    }

}
