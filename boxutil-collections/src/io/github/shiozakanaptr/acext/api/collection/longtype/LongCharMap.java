package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharCollection;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
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
public interface LongCharMap extends MapExt<Long, Character> {
    char get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(long)
     */
    @Deprecated
    default Character get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    char getOrDefault(long key, char defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.charValue() : (char) 0);
    }

    char put(long key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, char)
     */
    @Deprecated
    default Character put(Long key, Character value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.charValue() : (char) 0);
    }

    char putIfAbsent(long key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, char)
     */
    @Deprecated
    default Character putIfAbsent(Long key, Character value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.charValue() : (char) 0);
    }

    char remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long)
     */
    @Deprecated
    default Character remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Character) value).charValue() : (char) 0);
    }

    char replace(long key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, char)
     */
    @Deprecated
    default Character replace(Long key, Character value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.charValue() : (char) 0);
    }

    boolean replace(long key, char oldValue, char newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, char, char)
     */
    @Deprecated
    default boolean replace(Long key, Character oldValue, Character newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.charValue() : (char) 0, newValue != null ? newValue.charValue() : (char) 0);
    }

    char computeIfAbsent(long key, @NotNull LongFunction<? extends Character> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Character> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (Long l_t) -> mappingFunction.apply(l_t));
    }

    char computeIfPresent(long key, @NotNull LongCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Character, ? extends Character> remappingFunction) {
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char compute(long key, @NotNull LongCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongCharBiFunction)
     */
    @Deprecated
    default Character compute(Long key, @NotNull BiFunction<? super Long, ? super Character, ? extends Character> remappingFunction) {
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char merge(long key, char value, @NotNull CharCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Long key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return this.merge(key != null ? key.longValue() : (long) 0, value.charValue(), (char l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char update(long key, @NotNull CharUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(long, CharUnaryOperator)
     */
    @Deprecated
    default Character update(Long key, @NotNull Function<? super Character, ? extends Character> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, (char l_t) -> {
            final Character result;
            return (result = updateFunction.apply(l_t)) != null ? result : (char) 0;
        });
    }

    char remapping(long key, @NotNull LongCharBiFunction<? extends Long> remappingFunction, CharCharBiFunction<? extends Character> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(long, LongCharBiFunction, CharCharBiFunction)
     */
    @Deprecated
    default Character remapping(Long key, @NotNull BiFunction<? super Long, ? super Character, ? extends Long> remappingFunction, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> mergeFunction) {
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, char l_u) -> remappingFunction.apply(l_t, l_u), (char l_t, char l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(long from, long to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(long, long)
     */
    @Deprecated
    default boolean swapAt(Long from, Long to) {
        return this.swapAt(from != null ? from.longValue() : (long) 0, to != null ? to.longValue() : (long) 0);
    }

    boolean containsKey(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(long)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Long)) return false;
        return this.containsKey(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean containsValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(char)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Character)) return false;
        return this.containsValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    void forEach(@NotNull LongCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Character> action) {
        this.forEach((long l_t, char l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongCharBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Character, ? extends Character> function) {
        this.replaceAll((long l_t, char l_u) -> function.apply(l_t, l_u));
    }

    char getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Character getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    char setDefaultReturnValue(char newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(char)
     */
    @Deprecated
    default Character setDefaultReturnValue(Character newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.charValue() : (char) 0);
    }

    @NotNull
    LongSet keySet();

    @NotNull
    CharCollection values();

    @NotNull
    EntrySetExt<LongCharMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Long, Character>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Long, Character> {
        long getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Long getKey() {
            return this.getKeyRaw();
        }

        char getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Character getValue() {
            return this.getValueRaw();
        }

        char setValue(char newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(char)
         */
        @Deprecated
        default Character setValue(Character value) {
            return this.setValue(value != null ? value.charValue() : (char) 0);
        }
    }

}
