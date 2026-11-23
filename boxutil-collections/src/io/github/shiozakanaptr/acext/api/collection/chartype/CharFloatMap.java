package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
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
public interface CharFloatMap extends MapExt<Character, Float> {
    float get(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(char)
     */
    @Deprecated
    default Float get(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Character) key).charValue() : (char) 0);
    }

    float getOrDefault(char key, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key != null && !(key instanceof Character)) return defaultValue;
        return this.getOrDefault(key != null ? ((Character) key).charValue() : (char) 0, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(char key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, float)
     */
    @Deprecated
    default Float put(Character key, Float value) {
        return this.put(key != null ? key.charValue() : (char) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(char key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, float)
     */
    @Deprecated
    default Float putIfAbsent(Character key, Float value) {
        return this.putIfAbsent(key != null ? key.charValue() : (char) 0, value != null ? value.floatValue() : (float) 0);
    }

    float remove(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char)
     */
    @Deprecated
    default Float remove(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0);
    }

    boolean remove(char key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(char key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, float)
     */
    @Deprecated
    default Float replace(Character key, Float value) {
        return this.replace(key != null ? key.charValue() : (char) 0, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(char key, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, float, float)
     */
    @Deprecated
    default boolean replace(Character key, Float oldValue, Float newValue) {
        return this.replace(key != null ? key.charValue() : (char) 0, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsent(char key, @NotNull CharFunction<? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Float> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.charValue() : (char) 0, (Character l_t) -> mappingFunction.apply(l_t));
    }

    float computeIfPresent(char key, @NotNull CharFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(key != null ? key.charValue() : (char) 0, (char l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float compute(char key, @NotNull CharFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharFloatBiFunction)
     */
    @Deprecated
    default Float compute(Character key, @NotNull BiFunction<? super Character, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(key != null ? key.charValue() : (char) 0, (char l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float merge(char key, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Character key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(key != null ? key.charValue() : (char) 0, value.floatValue(), (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(char key, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(char, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(Character key, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(key != null ? key.charValue() : (char) 0, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(char key, @NotNull CharFloatBiFunction<? extends Character> remappingFunction, FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(char, CharFloatBiFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(Character key, @NotNull BiFunction<? super Character, ? super Float, ? extends Character> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(key != null ? key.charValue() : (char) 0, (char l_t, float l_u) -> remappingFunction.apply(l_t, l_u), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull CharFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Float> action) {
        this.forEach((char l_t, float l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull CharFloatBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Float, ? extends Float> function) {
        this.replaceAll((char l_t, float l_u) -> function.apply(l_t, l_u));
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
    default Float setDefaultReturnValue(Float newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.floatValue() : (float) 0);
    }

    @NotNull
    CharSet keySet();

    @NotNull
    FloatCollection values();

    @NotNull
    EntrySetExt<CharFloatMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Character, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Character, Float> {
        char getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Character getKey() {
            return this.getKeyRaw();
        }

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
        default Float setValue(Float value) {
            return this.setValue(value != null ? value.floatValue() : (float) 0);
        }
    }

}
