package org.boxutil.base.api.container;

import com.fs.starfarer.api.util.Pair;
import org.boxutil.util.function.objecttype.TeConsumer;
import org.boxutil.util.function.objecttype.TeFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Typically, for any key or value is {@code null}, it should be treated as {@code 0}
 * if the key was specific primitive type that the Map implementation defined.
 * <p>However, for any value is {@code 0}, it not be treated as {@code null}
 * if the value was specific primitive type that the Map implementation defined.
 */
public interface BiMap<X, Y, V> extends Container {
    V get(Object keyA, Object keyB);

    default V getOrDefault(Object keyA, Object keyB, V defaultValue) {
        V result;
        return ((result = this.get(keyA, keyB)) != null || this.containsKey(keyA, keyB)) ? result : defaultValue;
    }

    @Nullable V put(X keyA, Y keyB, V value);

    default V putIfAbsent(X keyA, Y keyB, V value) {
        V result;
        if ((result = this.get(keyA, keyB)) == null) {
            result = this.put(keyA, keyB, value);
        }
        return result;
    }

    V remove(Object keyA, Object keyB);

    default boolean remove(Object keyA, Object keyB, Object value) {
        Object currValue;
        if (!Objects.equals(currValue = this.get(keyA, keyB), value) || (currValue == null && !this.containsKey(keyA, keyB))) {
            return false;
        }
        this.remove(keyA, keyB);
        return true;
    }

    default V replace(X keyA, Y keyB, V value) {
        V currValue;
        if ((currValue = this.get(keyA, keyB)) != null || this.containsKey(keyA, keyB)) {
            currValue = this.put(keyA, keyB, value);
        }
        return currValue;
    }

    default boolean replace(X keyA, Y keyB, V oldValue, V newValue) {
        Object currValue;
        if (!Objects.equals(currValue = this.get(keyA, keyB), oldValue) || (currValue == null && !this.containsKey(keyA, keyB))) {
            return false;
        }
        this.put(keyA, keyB, newValue);
        return true;
    }

    default V computeIfAbsent(X keyA, Y keyB, @NotNull BiFunction<? super X, ? super Y, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);

        V result;
        if ((result = this.get(keyA, keyB)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(keyA, keyB)) != null) {
                this.put(keyA, keyB, newValue);
                return newValue;
            }
        }
        return result;
    }

    default V computeIfPresent(X keyA, Y keyB, @NotNull TeFunction<? super X, ? super Y, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        V oldValue;
        if ((oldValue = this.get(keyA, keyB)) != null) {
            V newValue = remappingFunction.apply(keyA, keyB, oldValue);
            if (newValue != null) {
                this.put(keyA, keyB, newValue);
                return newValue;
            } else {
                this.remove(keyA, keyB);
                return null;
            }
        } else {
            return null;
        }
    }

    default V compute(X keyA, Y keyB, @NotNull TeFunction<? super X, ? super Y, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        V oldValue = get(keyA, keyB);
        V newValue;
        if ((newValue = remappingFunction.apply(keyA, keyB, oldValue)) == null) {
            if (oldValue != null || containsKey(keyA, keyB)) {
                remove(keyA, keyB);
            }
            return null;
        } else {
            put(keyA, keyB, newValue);
            return newValue;
        }
    }

    default V merge(X keyA, Y keyB, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);

        V oldValue = get(keyA, keyB);
        V newValue = (oldValue == null) ? value :
                remappingFunction.apply(oldValue, value);
        if (newValue == null) {
            remove(keyA, keyB);
        } else {
            put(keyA, keyB, newValue);
        }
        return newValue;
    }

    default V update(X keyA, Y keyB, @NotNull Function<? super V, ? extends V> updateFunction) {
        Objects.requireNonNull(updateFunction);

        V oldValue = this.get(keyA, keyB);
        // if the `get` method also can return a default value, directly use it
        V newValue = updateFunction.apply(oldValue);
        this.put(keyA, keyB, newValue);
        return newValue;
    }

    default V remapping(X keyA, Y keyB, @NotNull TeFunction<? super X, ? super Y, ? super V, ? extends Pair<? extends X, ? extends Y>> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);

        V def = this.getDefaultReturnValue();
        if (!this.containsKey(keyA, keyB)) return def;

        V currValue = this.get(keyA, keyB);
        Pair<? extends X, ? extends Y> newKeyPair = remappingFunction.apply(keyA, keyB, currValue);

        if (newKeyPair == null || !(Objects.equals(newKeyPair.one, keyA) && Objects.equals(newKeyPair.two, keyB))) this.remove(keyA, keyB);
        if (newKeyPair == null) return def;
        if (!(Objects.equals(newKeyPair.one, keyA) && Objects.equals(newKeyPair.two, keyB))) {
            if (this.containsKey(newKeyPair.one, newKeyPair.two)) {
                V oldValue = this.get(newKeyPair.one, newKeyPair.two),
                        // if the `get` method also can return a default value, should check it here and use null for result, because it means "not found"
                        newValue = mergeFunction.apply(oldValue == def ? null : oldValue, currValue);
                if (newValue != null) {
                    currValue = newValue;
                } else {
                    this.remove(newKeyPair.one, newKeyPair.two);
                    return def;
                }
            }
            this.put(newKeyPair.one, newKeyPair.two, currValue);
        }
        return currValue;
    }

    default void swap(@NotNull BiMap<X, Y, V> source) {
        throw new UnsupportedOperationException();
    }

    default boolean swapAt(X fromA, Y fromB, X toA, Y toB) {
        if (!(this.containsKey(fromA, fromB) && this.containsKey(toA, toB))) return false;
        V left = this.get(fromA, fromB);
        V right = this.get(toA, toB);
        this.put(fromA, fromB, right);
        this.put(toA, toB, left);
        return true;
    }

    boolean containsKey(Object keyA, Object keyB);

    boolean containsFirstKey(Object keyA);

    boolean containsSecondKey(Object keyB);

    boolean containsValue(Object value);

    void forEach(@NotNull TeConsumer<? super X, ? super Y, ? super V> action);

    void putAll(@NotNull BiMap<? extends X, ? extends Y, ? extends V> m);

    void replaceAll(@NotNull TeFunction<? super X, ? super Y, ? super V, ? extends V> function);

    default V getDefaultReturnValue() {
        return null;
    }

    default V setDefaultReturnValue(@Nullable V newDefault) {
        throw new UnsupportedOperationException();
    }

    int size();

    boolean isEmpty();

    CollectionExt<X> firstKeySet();

    CollectionExt<Y> secondKeySet();

    CollectionExt<V> values();

    SetExt<BiEntry<X, Y, V>> entrySet();

    void clear();

    int hashCode();

    boolean equals(Object o);

    String toString();

    interface BiEntry<X, Y, V> {
        X getFirstKey();

        Y getSecondKey();

        V getValue();

        V setValue(V newValue);

        boolean equals(Object o);

        int hashCode();
    }
}
