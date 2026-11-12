package io.github.shiozakanaptr.acext.util.collection;

import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.util.HashingUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A factory for obtaining {@link HashBehavior} strategy instances used by hash-based
 * collections to control their hashing and equality behavior.
 *
 * <p>This factory provides a set of built-in strategies exposed through the
 * {@link Strategy} enum, as well as support for registering named custom
 * strategies via {@link #of(String, Supplier)}. Built-in strategies are
 * singletons and are shared across callers. Custom strategies are cached by
 * name, so repeated requests for the same name return the same instance.
 *
 * <p>This class is non-thread-safe.
 *
 * @see HashBehavior
 * @see Strategy
 */
@SuppressWarnings("unchecked")
public final class HashBehaviorFactory {
    private final static Map<String, HashBehavior<Object>> CUSTOM = new HashMap<>();
    private final static HashBehavior<Object>[] DEFAULT;

    static {
        DEFAULT = new HashBehavior[Strategy.values().length];
        DEFAULT[Strategy.DIRECTLY.ordinal()] = new DefaultDirectly();
        DEFAULT[Strategy.FIBONACCI.ordinal()] = new DefaultFibonacci();
        DEFAULT[Strategy.BIT_MIX.ordinal()] = new DefaultBitMix();
        DEFAULT[Strategy.FIBONACCI_MIX.ordinal()] = new DefaultFibonacciMix();
        DEFAULT[Strategy.MURMURHASH3_FMIX.ordinal()] = new DefaultMurmurHash3();
    }

    /**
     * The built-in hashing strategies supported by this factory.
     *
     * @see #get(Strategy)
     */
    public enum Strategy {
        /**
         * A strategy that uses the hash code directly.
         * @see HashingUtil#directly(Object)
         */
        DIRECTLY,
        /**
         * A strategy based on Fibonacci hashing.
         * @see HashingUtil#fibonacci(Object)
         */
        FIBONACCI,
        /**
         * A strategy that uses the hash code directly.
         * @see HashingUtil#bitMix(Object)
         */
        BIT_MIX,
        /**
         * A strategy that uses the hash code directly.
         * @see HashingUtil#fibonacciMix(Object)
         */
        FIBONACCI_MIX,
        /**
         * A strategy that uses the hash code directly.
         * @see HashingUtil#murmurHash3FMix(Object)
         */
        MURMURHASH3_FMIX
    }

    /**
     * Returns the shared {@link HashBehavior} instance for the specified built-in
     * strategy.
     *
     * @param <K> the type of keys handled by the hash behavior
     * @param strategy the built-in strategy whose behavior is to be returned
     * @return the shared hash behavior for the specified strategy
     * @throws NullPointerException if {@code strategy} is {@code null}
     */
    public static <K> HashBehavior<K> get(@NotNull Strategy strategy) {
        return (HashBehavior<K>) DEFAULT[strategy.ordinal()];
    }

    private static boolean isInvalidID(String src) {
        final int limit = src.length();
        for (int i = 0; i < limit;) {
            if (!Character.isWhitespace(src.charAt(i++))) return false;
        }
        return true;
    }

    /**
     * Returns the named custom {@link HashBehavior} instance, registering it if
     * necessary.
     *
     * <p>If a custom behavior with the specified name has already been
     * registered, that existing instance is returned and the supplied
     * {@code behavior} supplier is not invoked. Otherwise, the supplier is
     * invoked once and its result is cached under the specified name.
     *
     * <p>The name must contain at least one non-whitespace character.
     *
     * @param <K> the type of keys handled by the hash behavior
     * @param name the unique non-blank name of the custom behavior
     * @param behavior a supplier of the custom hash behavior, invoked only if no
     *        behavior is already registered under {@code name}
     * @return the named custom hash behavior
     * @throws IllegalArgumentException if {@code name} is empty or consists only
     *         of whitespace characters
     * @throws NullPointerException if {@code name} or {@code behavior} is
     *         {@code null}
     */
    public static <K> HashBehavior<K> of(@NotNull String name, @NotNull Supplier<HashBehavior<K>> behavior) {
//        if (name.isBlank()) throw new IllegalArgumentException("Illegal id: a white space"); // JDK 11+
        if (isInvalidID(name)) throw new IllegalArgumentException("Illegal id: a white space");
        return (HashBehavior<K>) CUSTOM.computeIfAbsent(name, l_k -> (HashBehavior<Object>) behavior.get());
    }

    /**
     * Returns the shared {@link HashBehavior} instance for the
     * {@link Strategy#DIRECTLY} strategy.
     *
     * @param <K> the type of keys handled by the hash behavior
     * @return the shared directly-hashing behavior
     * @see Strategy#DIRECTLY
     */
    public static <K> HashBehavior<K> directly() {
        return get(Strategy.DIRECTLY);
    }

    /**
     * Returns the shared {@link HashBehavior} instance for the
     * {@link Strategy#FIBONACCI} strategy.
     *
     * @param <K> the type of keys handled by the hash behavior
     * @return the shared Fibonacci-hashing behavior
     * @see Strategy#FIBONACCI
     */
    public static <K> HashBehavior<K> fibonacci() {
        return get(Strategy.FIBONACCI);
    }

    /**
     * Returns the shared {@link HashBehavior} instance for the
     * {@link Strategy#BIT_MIX} strategy.
     *
     * @param <K> the type of keys handled by the hash behavior
     * @return the shared bit-mixing behavior
     * @see Strategy#BIT_MIX
     */
    public static <K> HashBehavior<K> bitMix() {
        return get(Strategy.BIT_MIX);
    }

    /**
     * Returns the shared {@link HashBehavior} instance for the
     * {@link Strategy#FIBONACCI_MIX} strategy.
     *
     * @param <K> the type of keys handled by the hash behavior
     * @return the shared Fibonacci-mixing behavior
     * @see Strategy#FIBONACCI_MIX
     */
    public static <K> HashBehavior<K> fibonacciMix() {
        return get(Strategy.FIBONACCI_MIX);
    }

    /**
     * Returns the shared {@link HashBehavior} instance for the
     * {@link Strategy#MURMURHASH3_FMIX} strategy.
     *
     * @param <K> the type of keys handled by the hash behavior
     * @return the shared MurmurHash3 finalizer behavior
     * @see Strategy#MURMURHASH3_FMIX
     */
    public static <K> HashBehavior<K> murmurHash3FMix() {
        return get(Strategy.MURMURHASH3_FMIX);
    }

    private final static class DefaultDirectly implements HashBehavior<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        public int hashing(int key) {
            return key;
        }

        public int hashing(long key) {
            return Long.hashCode(key);
        }

        public int hashing(float key) {
            return Float.hashCode(key);
        }

        public int hashing(double key) {
            return Double.hashCode(key);
        }

        public int hashing(@Nullable Object key) {
            return HashingUtil.directly(key);
        }
    }

    private final static class DefaultFibonacci implements HashBehavior<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        public int hashing(int key) {
            return HashingUtil.fibonacciBit32(key);
        }

        public int hashing(long key) {
            return Long.hashCode(HashingUtil.fibonacciBit64(key));
        }

        public int hashing(float key) {
            return HashingUtil.fibonacciBit32(Float.floatToIntBits(key));
        }

        public int hashing(double key) {
            return Long.hashCode(HashingUtil.fibonacciBit64(Double.doubleToLongBits(key)));
        }

        public int hashing(@Nullable Object key) {
            return HashingUtil.fibonacci(key);
        }
    }

    private final static class DefaultBitMix implements HashBehavior<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        public int hashing(int key) {
            return HashingUtil.bitMix(key);
        }

        public int hashing(long key) {
            return HashingUtil.bitMix(Long.hashCode(key));
        }

        public int hashing(float key) {
            return HashingUtil.bitMix(Float.floatToIntBits(key));
        }

        public int hashing(double key) {
            return HashingUtil.bitMix(Long.hashCode(Double.doubleToLongBits(key)));
        }

        public int hashing(@Nullable Object key) {
            return HashingUtil.bitMix(key);
        }
    }

    private final static class DefaultFibonacciMix implements HashBehavior<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        public int hashing(int key) {
            return HashingUtil.fibonacciMixBit32(key);
        }

        public int hashing(long key) {
            return Long.hashCode(HashingUtil.fibonacciMixBit64(key));
        }

        public int hashing(float key) {
            return HashingUtil.fibonacciMixBit32(Float.floatToIntBits(key));
        }

        public int hashing(double key) {
            return Long.hashCode(HashingUtil.fibonacciMixBit64(Double.doubleToLongBits(key)));
        }

        public int hashing(@Nullable Object key) {
            return HashingUtil.fibonacciMix(key);
        }
    }

    private final static class DefaultMurmurHash3 implements HashBehavior<Object>, Serializable {
        private static final long serialVersionUID = 1L;

        public int hashing(int key) {
            return HashingUtil.murmur3HashFMixBit32(key);
        }

        public int hashing(long key) {
            return Long.hashCode(HashingUtil.murmur3HashFMixBit64(key));
        }

        public int hashing(float key) {
            return HashingUtil.murmur3HashFMixBit32(Float.floatToIntBits(key));
        }

        public int hashing(double key) {
            return Long.hashCode(HashingUtil.murmur3HashFMixBit64(Double.doubleToLongBits(key)));
        }

        public int hashing(@Nullable Object key) {
            return HashingUtil.murmurHash3FMix(key);
        }
    }

    private HashBehaviorFactory() {}
}
