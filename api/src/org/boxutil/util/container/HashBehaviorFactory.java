package org.boxutil.util.container;

import org.boxutil.base.api.container.HashBehavior;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.IntUnaryOperator;

@SuppressWarnings("unchecked")
public final class HashBehaviorFactory {
    private final static ConcurrentMap<String, HashBehavior<Object>> CUSTOM = new ConcurrentHashMap<>();
    private final static HashBehavior<Object>[] DEFAULT;

    static {
        DEFAULT = new HashBehavior[Strategy.values().length];
        DEFAULT[Strategy.DIRECTLY.ordinal()] = HashBehavior::hashing_Directly;
        DEFAULT[Strategy.FIBONACCI.ordinal()] = new DefaultFibonacci();
        DEFAULT[Strategy.HASH_MAP.ordinal()] = HashBehavior::hashing_HashMap;
        DEFAULT[Strategy.FIBONACCI_MIX.ordinal()] = new DefaultFibonacciMix();
        DEFAULT[Strategy.MURMUR_HASH3.ordinal()] = new DefaultMurmurHash3();
    }

    public enum Strategy {
        DIRECTLY,
        FIBONACCI,
        HASH_MAP,
        FIBONACCI_MIX,
        MURMUR_HASH3
    }

    public static <K> HashBehavior<K> get(@NotNull Strategy strategy) {
        return (HashBehavior<K>) DEFAULT[strategy.ordinal()];
    }

    public static <K> HashBehavior<K> of(@NotNull String name, @NotNull HashBehavior<K> behavior) {
        if (name.isBlank()) throw new IllegalArgumentException("Illegal id: a white space");
        return (HashBehavior<K>) CUSTOM.computeIfAbsent(name, l_k -> (HashBehavior<Object>) behavior);
    }

    public static <K> HashBehavior<K> directly() {
        return get(Strategy.DIRECTLY);
    }

    public static <K> HashBehavior<K> fibonacci() {
        return get(Strategy.FIBONACCI);
    }

    public static <K> HashBehavior<K> hashMap() {
        return get(Strategy.HASH_MAP);
    }

    public static <K> HashBehavior<K> fibonacciMix() {
        return get(Strategy.FIBONACCI_MIX);
    }

    public static <K> HashBehavior<K> murmurHash3() {
        return get(Strategy.MURMUR_HASH3);
    }

    private HashBehaviorFactory() {}

    private final static class DefaultFibonacci implements HashBehavior<Object> {
        public int hashing(int key) {
            return HashBehavior.hashing_Fibonacci_32(key);
        }

        public int hashing(long key) {
            return Long.hashCode(HashBehavior.hashing_Fibonacci_64(key));
        }

        public int hashing(@Nullable Object key) {
            return HashBehavior.hashing_Fibonacci(key);
        }
    }

    private final static class DefaultFibonacciMix implements HashBehavior<Object> {
        public int hashing(int key) {
            return HashBehavior.hashing_FibonacciMix_32(key);
        }

        public int hashing(long key) {
            return Long.hashCode(HashBehavior.hashing_FibonacciMix_64(key));
        }

        public int hashing(@Nullable Object key) {
            return HashBehavior.hashing_FibonacciMix(key);
        }
    }

    private final static class DefaultMurmurHash3 implements HashBehavior<Object> {
        public int hashing(int key) {
            return HashBehavior.hashing_MurmurHash3_32(key);
        }

        public int hashing(long key) {
            return Long.hashCode(HashBehavior.hashing_MurmurHash3_64(key));
        }

        public int hashing(@Nullable Object key) {
            return HashBehavior.hashing_MurmurHash3(key);
        }
    }
}
