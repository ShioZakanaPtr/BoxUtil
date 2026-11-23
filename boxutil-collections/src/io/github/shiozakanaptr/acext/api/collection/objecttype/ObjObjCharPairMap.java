package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.PairMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.Pair;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
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
public interface ObjObjCharPairMap<L, R> extends PairMap<L, R, Character> {
    char getRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object, Object)
     */
    @Deprecated
    default Character get(Object keyA, Object keyB) {
        return this.getRaw(keyA, keyB);
    }

    char getOrDefault(Object keyA, Object keyB, char defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, Object, char)
     */
    @Deprecated
    default Character getOrDefault(Object keyA, Object keyB, Character defaultValue) {
        return this.getOrDefault(keyA, keyB, defaultValue != null ? defaultValue.charValue() : (char) 0);
    }

    char put(L keyA, R keyB, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(L, R, char)
     */
    @Deprecated
    default Character put(L keyA, R keyB, Character value) {
        return this.put(keyA, keyB, value != null ? value.charValue() : (char) 0);
    }

    char putIfAbsent(L keyA, R keyB, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(L, R, char)
     */
    @Deprecated
    default Character putIfAbsent(L keyA, R keyB, Character value) {
        return this.putIfAbsent(keyA, keyB, value != null ? value.charValue() : (char) 0);
    }

    char removeRaw(Object keyA, Object keyB);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object, Object)
     */
    @Deprecated
    default Character remove(Object keyA, Object keyB) {
        return this.removeRaw(keyA, keyB);
    }

    boolean remove(Object keyA, Object keyB, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, Object, char)
     */
    @Deprecated
    default boolean remove(Object keyA, Object keyB, Object value) {
        if (value != null && !(value instanceof Character)) return false;
        return this.remove(keyA, keyB, value != null ? ((Character) value).charValue() : (char) 0);
    }

    char replace(L keyA, R keyB, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, char)
     */
    @Deprecated
    default Character replace(L keyA, R keyB, Character value) {
        return this.replace(keyA, keyB, value != null ? value.charValue() : (char) 0);
    }

    boolean replace(L keyA, R keyB, char oldValue, char newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(L, R, char, char)
     */
    @Deprecated
    default boolean replace(L keyA, R keyB, Character oldValue, Character newValue) {
        return this.replace(keyA, keyB, oldValue != null ? oldValue.charValue() : (char) 0, newValue != null ? newValue.charValue() : (char) 0);
    }

    char computeIfAbsentRaw(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Character> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(L, R, BiFunction)
     */
    @Deprecated
    default Character computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends Character> mappingFunction) {
        return this.computeIfAbsentRaw(keyA, keyB, (L l_t, R l_u) -> mappingFunction.apply(l_t, l_u));
    }

    char computeIfPresent(L keyA, R keyB, @NotNull ObjObjCharTeFunction<? super L, ? super R, ? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(L, R, ObjObjCharTeFunction)
     */
    @Deprecated
    default Character computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Character, ? extends Character> remappingFunction) {
        return this.computeIfPresent(keyA, keyB, (L l_t, R l_u, char l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    char compute(L keyA, R keyB, @NotNull ObjObjCharTeFunction<? super L, ? super R, ? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(L, R, ObjObjCharTeFunction)
     */
    @Deprecated
    default Character compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Character, ? extends Character> remappingFunction) {
        return this.compute(keyA, keyB, (L l_t, R l_u, char l_v) -> remappingFunction.apply(l_t, l_u, l_v));
    }

    char merge(L keyA, R keyB, char value, @NotNull CharCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(L, R, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(L keyA, R keyB, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return this.merge(keyA, keyB, value.charValue(), (char l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char update(L keyA, R keyB, @NotNull CharUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(L, R, CharUnaryOperator)
     */
    @Deprecated
    default Character update(L keyA, R keyB, @NotNull Function<? super Character, ? extends Character> updateFunction) {
        return this.update(keyA, keyB, (char l_t) -> {
            final Character result;
            return (result = updateFunction.apply(l_t)) != null ? result : (char) 0;
        });
    }

    char remapping(L keyA, R keyB, @NotNull ObjObjCharTeFunction<? super L, ? super R, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull CharCharBiFunction<? extends Character> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(L, R, ObjObjCharTeFunction, CharCharBiFunction)
     */
    @Deprecated
    default Character remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super Character, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> mergeFunction) {
        return this.remapping(keyA, keyB, (L l_t, R l_u, char l_v) -> remappingFunction.apply(l_t, l_u, l_v), (char l_t, char l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(@NotNull ObjObjCharTeConsumer<? super L, ? super R> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjObjCharTeConsumer)
     */
    @Deprecated
    default void forEach(@NotNull TeConsumer<? super L, ? super R, ? super Character> action) {
        this.forEach((L l_t, R l_u, char l_v) -> action.accept(l_t, l_u, l_v));
    }

    void replaceAll(@NotNull ObjObjCharTeOperator<? super L, ? super R> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjObjCharTeOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super Character, ? extends Character> function) {
        this.replaceAll((L l_t, R l_u, char l_v) -> function.apply(l_t, l_u, l_v));
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
    @NotNull
    default Character setDefaultReturnValue(@Nullable Character newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.charValue() : (char) 0);
    }

    @NotNull
    ObjCollection<L> firstKeyCollection();

    @NotNull
    ObjCollection<R> secondKeyCollection();

    @NotNull
    CharCollection valueCollection();

    @NotNull
    EntrySetExt<ObjObjCharPairMap.PairEntry<L, R>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<PairMap.PairEntry<L, R, Character>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface PairEntry<L, R> extends PairMap.PairEntry<L, R, Character> {
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
        default Character setValue(Character newValue) {
            return this.setValue(newValue != null ? newValue.charValue() : (char) 0);
        }
    }
}
