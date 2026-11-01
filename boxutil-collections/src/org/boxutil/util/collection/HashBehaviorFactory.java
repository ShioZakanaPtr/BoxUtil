package org.boxutil.util.collection;

import org.boxutil.base.api.container.HashBehavior;
import org.boxutil.util.HashingUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class HashBehaviorFactory {
    private final static ConcurrentMap<String, HashBehavior<Object>> CUSTOM = new ConcurrentHashMap<>();
    private final static HashBehavior<Object>[] DEFAULT;

    static {
        DEFAULT = new HashBehavior[Strategy.values().length];
        DEFAULT[Strategy.DIRECTLY.ordinal()] = new DefaultDirectly();
        DEFAULT[Strategy.FIBONACCI.ordinal()] = new DefaultFibonacci();
        DEFAULT[Strategy.BIT_MIX.ordinal()] = new DefaultBitMix();
        DEFAULT[Strategy.FIBONACCI_MIX.ordinal()] = new DefaultFibonacciMix();
        DEFAULT[Strategy.MURMURHASH3_FMIX.ordinal()] = new DefaultMurmurHash3();
    }

    public enum Strategy {
        DIRECTLY,
        FIBONACCI,
        BIT_MIX,
        FIBONACCI_MIX,
        MURMURHASH3_FMIX
    }

    public static <K> HashBehavior<K> get(@NotNull Strategy strategy) {
        return (HashBehavior<K>) DEFAULT[strategy.ordinal()];
    }

    public static <K> HashBehavior<K> of(@NotNull String name, @NotNull Supplier<HashBehavior<K>> behavior) {
        if (name.isEmpty() || " ".contentEquals(name)) throw new IllegalArgumentException("Illegal id: a white space");
        return (HashBehavior<K>) CUSTOM.computeIfAbsent(name, l_k -> (HashBehavior<Object>) behavior.get());
    }

    public static <K> HashBehavior<K> directly() {
        return get(Strategy.DIRECTLY);
    }

    public static <K> HashBehavior<K> fibonacci() {
        return get(Strategy.FIBONACCI);
    }

    public static <K> HashBehavior<K> bitMix() {
        return get(Strategy.BIT_MIX);
    }

    public static <K> HashBehavior<K> fibonacciMix() {
        return get(Strategy.FIBONACCI_MIX);
    }

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
            return Float.floatToRawIntBits(key);
        }

        public int hashing(double key) {
            return Long.hashCode(Double.doubleToRawLongBits(key));
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
            return HashingUtil.fibonacciBit32(Float.floatToRawIntBits(key));
        }

        public int hashing(double key) {
            return Long.hashCode(HashingUtil.fibonacciBit64(Double.doubleToRawLongBits(key)));
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
            return HashingUtil.bitMix(Float.floatToRawIntBits(key));
        }

        public int hashing(double key) {
            return HashingUtil.bitMix(Long.hashCode(Double.doubleToRawLongBits(key)));
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
            return HashingUtil.fibonacciMixBit32(Float.floatToRawIntBits(key));
        }

        public int hashing(double key) {
            return Long.hashCode(HashingUtil.fibonacciMixBit64(Double.doubleToRawLongBits(key)));
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
            return HashingUtil.murmur3HashFMixBit32(Float.floatToRawIntBits(key));
        }

        public int hashing(double key) {
            return Long.hashCode(HashingUtil.murmur3HashFMixBit64(Double.doubleToRawLongBits(key)));
        }

        public int hashing(@Nullable Object key) {
            return HashingUtil.murmurHash3FMix(key);
        }
    }

    private HashBehaviorFactory() {}
}
