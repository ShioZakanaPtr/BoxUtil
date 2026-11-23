package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface CharLongMap extends MapExt<Character, Long> {
    long get(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(char)
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Character) key).charValue() : (char) 0);
    }

    long getOrDefault(char key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Character)) return defaultValue;
        return this.getOrDefault(key != null ? ((Character) key).charValue() : (char) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(char key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, long)
     */
    @Deprecated
    default Long put(Character key, Long value) {
        return this.put(key != null ? key.charValue() : (char) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(char key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, long)
     */
    @Deprecated
    default Long putIfAbsent(Character key, Long value) {
        return this.putIfAbsent(key != null ? key.charValue() : (char) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char)
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0);
    }

    boolean remove(char key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(char key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, long)
     */
    @Deprecated
    default Long replace(Character key, Long value) {
        return this.replace(key != null ? key.charValue() : (char) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(char key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, long, long)
     */
    @Deprecated
    default boolean replace(Character key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.charValue() : (char) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(char key, @NotNull CharFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Long> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.charValue() : (char) 0, (Character l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(char key, @NotNull CharLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Long, ? extends Long> remappingFunction) {
        return this.computeIfPresent(key != null ? key.charValue() : (char) 0, (char l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(char key, @NotNull CharLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharLongBiFunction)
     */
    @Deprecated
    default Long compute(Character key, @NotNull BiFunction<? super Character, ? super Long, ? extends Long> remappingFunction) {
        return this.compute(key != null ? key.charValue() : (char) 0, (char l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(char key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Character key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return this.merge(key != null ? key.charValue() : (char) 0, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(char key, @NotNull LongUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(char, LongUnaryOperator)
     */
    @Deprecated
    default Long update(Character key, @NotNull Function<? super Long, ? extends Long> updateFunction) {
        return this.update(key != null ? key.charValue() : (char) 0, (long l_t) -> {
            final Long result;
            return (result = updateFunction.apply(l_t)) != null ? result : (long) 0;
        });
    }

    long remapping(char key, @NotNull CharLongBiFunction<? extends Character> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(char, CharLongBiFunction, LongLongBiFunction)
     */
    @Deprecated
    default Long remapping(Character key, @NotNull BiFunction<? super Character, ? super Long, ? extends Character> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        return this.remapping(key != null ? key.charValue() : (char) 0, (char l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(long)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.containsValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    void forEach(@NotNull CharLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Long> action) {
        this.forEach((char l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull CharLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Long, ? extends Long> function) {
        this.replaceAll((char l_t, long l_u) -> function.apply(l_t, l_u));
    }

    long getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Long getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    long setDefaultReturnValue(long newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(long)
     */
    @Deprecated
    default Long setDefaultReturnValue(Long newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.longValue() : (long) 0);
    }

    @NotNull
    CharSet keySet();

    @NotNull
    LongCollection values();

    @NotNull
    EntrySetExt<CharLongMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Character, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Character, Long> {
        char getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Character getKey() {
            return this.getKeyRaw();
        }

        long getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Long getValue() {
            return this.getValueRaw();
        }

        long setValue(long newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(long)
         */
        @Deprecated
        default Long setValue(Long value) {
            return this.setValue(value != null ? value.longValue() : (long) 0);
        }
    }

}
