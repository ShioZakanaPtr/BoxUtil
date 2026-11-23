package io.github.shiozakanaptr.acext.util.collection;

import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;
import io.github.shiozakanaptr.acext.api.collection.PairMap;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.Objects;

/**
 * Factory for obtaining builders for map implementations that use the
 * library's default, general-purpose choices for common scenarios.
 *
 * <p>The {@code of*} methods return configurable builders such as
 * {@link Maps}, {@link ConcurrentMaps}, and {@link PairMaps}. A builder
 * captures configuration and creates map instances when its {@code create()}
 * method is invoked. The factory selects an implementation intended to be
 * suitable for general use; callers that need a specific implementation should
 * use the corresponding {@code of*} method or configure the returned builder.
 *
 * <p>Builders returned by the {@code of*} methods are reusable. Callers are
 * encouraged to retain and reuse a builder when creating multiple maps with
 * the same configuration, rather than obtaining a new builder for each map.
 *
 * <p>Callers must distinguish primitive types from their corresponding wrapper
 * types when supplying {@code Class} arguments. A primitive class literal
 * (for example, {@code int.class}) enables creation of the corresponding
 * primitive-specialized implementation when one is available, whereas the
 * wrapper class literal (for example, {@code Integer.class}) selects the
 * reference-type implementation. Only primitive class literals cause a
 * primitive-specialized implementation to be created.
 *
 * <p><strong>Primitive boolean is not supported.</strong>
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@SuppressWarnings({"unchecked", "unused"})
public final class MapFactory {
    private static StringBuilder pickPackage() {
        return new StringBuilder("io.github.shiozakanaptr.acext.util.collection.");
    }

    private static void pickHead(Class<?> clazz, StringBuilder sb) {
        if (clazz == byte.class) {
            sb.append("bytetype.Byte");
        } else if (clazz == short.class) {
            sb.append("shorttype.Short");
        } else if (clazz == char.class) {
            sb.append("chartype.Char");
        } else if (clazz == int.class) {
            sb.append("inttype.Int");
        } else if (clazz == long.class) {
            sb.append("longtype.Long");
        } else if (clazz == float.class) {
            sb.append("floattype.Float");
        } else if (clazz == double.class) {
            sb.append("doubletype.Double");
        } else {
            sb.append("objecttype.Obj");
        }
    }

    private static void pickNext(Class<?> clazz, StringBuilder sb) {
        if (clazz == byte.class) {
            sb.append("Byte");
        } else if (clazz == short.class) {
            sb.append("Short");
        } else if (clazz == char.class) {
            sb.append("Char");
        } else if (clazz == int.class) {
            sb.append("Int");
        } else if (clazz == long.class) {
            sb.append("Long");
        } else if (clazz == float.class) {
            sb.append("Float");
        } else if (clazz == double.class) {
            sb.append("Double");
        } else {
            sb.append("Obj");
        }
    }

    private static boolean isPrimitiveBool(Class<?> clazz) {
        return clazz == boolean.class;
    }

    private static boolean isPrimitive(Class<?> clazz) {
        return clazz == boolean.class ||
                clazz == byte.class ||
                clazz == short.class ||
                clazz == char.class ||
                clazz == int.class ||
                clazz == long.class ||
                clazz == float.class ||
                clazz == double.class;
    }

    private static RuntimeException typeNotFound(Class<?> clazz) {
        return new IllegalArgumentException("Not have any implementations that " + clazz.getName() + " as the specific primitive type");
    }

    private static RuntimeException factoryFailed(Throwable thr) {
        return new RuntimeException("Failed to find the collection constructor cause ", thr);
    }

    public final static class Maps<K, V> {
        private int initialCapacity;
        private float loadFactor = HashContainer.DEFAULT_LOAD_FACTOR;
        private HashBehavior<K> hashBehavior = HashBehaviorFactory.fibonacci();
        private final SupplierBit<K, V> supplierBit;
        private final SupplierHash<K, V> supplierHash;

        private interface SupplierBit<K, V> {
            Map<K, V> get(int initialCapacity);
        }

        private interface SupplierHash<K, V> {
            Map<K, V> get(int initialCapacity, float loadFactor, HashBehavior<K> hashBehavior);
        }

        private Maps(Class<K> keyClass, Class<V> valueClass, SupplierBit<K, V> bit, SupplierHash<K, V> hash) {
            this.initialCapacity = bit != null ? 4 : HashContainer.DEFAULT_INITIAL_CAPACITY;
            supplierBit = bit;
            supplierHash = hash;
        }

        public Maps<K, V> initialCapacity(int initialCapacity) {
            this.initialCapacity = initialCapacity;
            return this;
        }

        public Maps<K, V> loadFactor(float loadFactor) {
            this.loadFactor = loadFactor;
            return this;
        }

        public Maps<K, V> hashBehavior(@NotNull HashBehavior<K> hashBehavior) {
            this.hashBehavior = Objects.requireNonNull(hashBehavior);
            return this;
        }

        public <C extends Map<K, V>> C create() {
            if (this.supplierBit != null) {
                return (C) this.supplierBit.get(this.initialCapacity);
            } else if (this.supplierHash != null) {
                return (C) this.supplierHash.get(this.initialCapacity, this.loadFactor, this.hashBehavior);
            } else throw new IllegalStateException();
        }
    }

    /**
     * Returns a reusable builder for a general-purpose map using the factory's
     * default implementation for common scenarios.
     *
     * <p>The returned builder can be configured before creating one or more map
     * instances. Reuse the builder when the same configuration is needed
     * multiple times.
     *
     * <p>The {@code keyClass} and {@code valueClass} arguments distinguish
     * primitive types from wrapper types. Supplying a primitive class literal
     * may create a primitive-specialized implementation; supplying the wrapper
     * class literal selects the reference-type implementation. Only primitive
     * class literals trigger creation of primitive-specialized implementations.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param keyClass the key class; primitive and wrapper class literals have
     *        different semantics
     * @param valueClass the value class; primitive and wrapper class literals
     *        have different semantics
     * @return a reusable builder for a general-purpose map
     * @throws IllegalArgumentException if the specified classes are not
     *         supported by the selected implementation
     * @throws RuntimeException if the default implementation cannot be loaded
     *         or instantiated
     * @see Maps
     */
    public static <K, V> Maps<K, V> of(Class<K> keyClass, Class<V> valueClass) {
        if (isPrimitiveBool(keyClass)) throw typeNotFound(keyClass);
        if (isPrimitiveBool(valueClass)) throw typeNotFound(valueClass);

        StringBuilder sb = pickPackage();
        pickHead(keyClass, sb);
        pickNext(valueClass, sb);
        if (keyClass == byte.class) {
            sb.append("BitMap");
        } else {
            sb.append("LinearProbingHashMap");
        }

        try {
            final Class<?> clazz = Class.forName(sb.toString(), false, Class.class.getClassLoader());
            if (keyClass == byte.class) {
                final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class));
                return new Maps<>(keyClass, valueClass, (initialCapacity) -> {
                    try {
                        return (Map<K, V>) mh.invoke(initialCapacity);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }, null);
            } else {
                final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class, float.class, HashBehavior.class));
                return new Maps<>(keyClass, valueClass, null, (initialCapacity, loadFactor, hashBehavior) -> {
                    try {
                        return (Map<K, V>) mh.invoke(initialCapacity, loadFactor, hashBehavior);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Throwable e) {
            throw factoryFailed(e);
        }
    }

    /**
     * Returns a reusable builder for an insert ordered linked map using the
     * factory's default implementation for common scenarios.
     *
     * <p>The returned builder can be configured before creating one or more map
     * instances. Reuse the builder when the same configuration is needed
     * multiple times.
     *
     * <p>The {@code keyClass} and {@code valueClass} arguments distinguish
     * primitive types from wrapper types. Supplying a primitive class literal
     * may create a primitive-specialized implementation; supplying the wrapper
     * class literal selects the reference-type implementation. Only primitive
     * class literals trigger creation of primitive-specialized implementations.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param keyClass the key class; primitive and wrapper class literals have
     *        different semantics
     * @param valueClass the value class; primitive and wrapper class literals
     *        have different semantics
     * @return a reusable builder for an insert ordered linked map
     * @throws IllegalArgumentException if the specified classes are not
     *         supported by the selected implementation
     * @throws RuntimeException if the default implementation cannot be loaded
     *         or instantiated
     * @see Maps
     */
    public static <K, V> Maps<K, V> ofLinked(Class<K> keyClass, Class<V> valueClass) {
        if (isPrimitiveBool(keyClass)) throw typeNotFound(keyClass);
        if (isPrimitiveBool(valueClass)) throw typeNotFound(valueClass);

        StringBuilder sb = pickPackage();
        pickHead(keyClass, sb);
        pickNext(valueClass, sb);
        if (keyClass == byte.class) {
            sb.append("LinkedBitMap");
        } else {
            sb.append("LinearProbingLinkedHashMap");
        }

        try {
            final Class<?> clazz = Class.forName(sb.toString(), false, Class.class.getClassLoader());
            if (keyClass == byte.class) {
                final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class));
                return new Maps<>(keyClass, valueClass, (initialCapacity) -> {
                    try {
                        return (Map<K, V>) mh.invoke(initialCapacity);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }, null);
            } else {
                final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class, float.class, HashBehavior.class));
                return new Maps<>(keyClass, valueClass, null, (initialCapacity, loadFactor, hashBehavior) -> {
                    try {
                        return (Map<K, V>) mh.invoke(initialCapacity, loadFactor, hashBehavior);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Throwable e) {
            throw factoryFailed(e);
        }
    }

    /**
     * Returns a reusable builder for a bidirectional lookup map (bidi map)
     * using the factory's default implementation for common scenarios.
     *
     * <p>The returned builder can be configured before creating one or more map
     * instances. Reuse the builder when the same configuration is needed
     * multiple times.
     *
     * <p>The {@code keyClass} and {@code valueClass} arguments distinguish
     * primitive types from wrapper types. Supplying a primitive class literal
     * may create a primitive-specialized implementation; supplying the wrapper
     * class literal selects the reference-type implementation. Only primitive
     * class literals trigger creation of primitive-specialized implementations.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param keyClass the key class; primitive and wrapper class literals have
     *        different semantics
     * @param valueClass the value class; primitive and wrapper class literals
     *        have different semantics
     * @return a reusable builder for a bidirectional lookup map
     * @throws IllegalArgumentException if the specified classes are not
     *         supported by the selected implementation
     * @throws RuntimeException if the default implementation cannot be loaded
     *         or instantiated
     * @see Maps
     */
    public static <K, V> Maps<K, V> ofBidi(Class<K> keyClass, Class<V> valueClass) {
        if (isPrimitiveBool(keyClass)) throw typeNotFound(keyClass);
        if (isPrimitiveBool(valueClass)) throw typeNotFound(valueClass);

        StringBuilder sb = pickPackage();
        pickHead(keyClass, sb);
        pickNext(valueClass, sb);
        if (keyClass == byte.class || valueClass == byte.class) {
            sb.append(keyClass != valueClass ? "BitLinearProbingHashBidiMap" : "BitBidiMap");
        } else {
            final boolean largeKey = keyClass != short.class && keyClass != char.class && keyClass != int.class && keyClass != float.class,
                    largeValue = valueClass != short.class && valueClass != char.class && valueClass != int.class && valueClass != float.class;
            sb.append(largeKey || largeValue ? "LinearProbingHashBidiMap" : "LinearProbingDualHashBidiMap");
        }

        try {
            final Class<?> clazz = Class.forName(sb.toString(), false, Class.class.getClassLoader());
            if (keyClass == byte.class && valueClass == byte.class) {
                final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class));
                return new Maps<>(keyClass, valueClass, (initialCapacity) -> {
                    try {
                        return (Map<K, V>) mh.invoke(initialCapacity);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }, null);
            } else {
                final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class, float.class, HashBehavior.class));
                return new Maps<>(keyClass, valueClass, null, (initialCapacity, loadFactor, hashBehavior) -> {
                    try {
                        return (Map<K, V>) mh.invoke(initialCapacity, loadFactor, hashBehavior);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Throwable e) {
            throw factoryFailed(e);
        }
    }

    public final static class ConcurrentMaps<K, V> {
        private final static int DEFAULT_SHARDS = Runtime.getRuntime().availableProcessors() << 1;

        private boolean syncQuery = false;
        private short cacheLinePadding = 64;
        private int shards = DEFAULT_SHARDS;
        private int shardInitialCapacity = HashContainer.DEFAULT_INITIAL_CAPACITY;
        private float loadFactor = HashContainer.DEFAULT_LOAD_FACTOR;
        private HashBehavior<K> hashBehavior = HashBehaviorFactory.fibonacciMix();
        private final Supplier<K, V> supplier;

        private interface Supplier<K, V> {
            Map<K, V> get(int shards, int shardInitialCapacity, short cacheLinePadding, float loadFactor, final HashBehavior<K> hashBehavior, boolean syncQuery);
        }

        private ConcurrentMaps(Class<K> keyClass, Class<V> valueClass, Supplier<K, V> supplier) {
            this.supplier = supplier;
        }

        public ConcurrentMaps<K, V> syncQuery(boolean syncQuery) {
            this.syncQuery = syncQuery;
            return this;
        }

        public ConcurrentMaps<K, V> syncQuery(short cacheLinePadding) {
            this.cacheLinePadding = cacheLinePadding;
            return this;
        }

        public ConcurrentMaps<K, V> shards(int shards) {
            this.shards = shards;
            return this;
        }

        public ConcurrentMaps<K, V> shardInitialCapacity(int shardInitialCapacity) {
            this.shardInitialCapacity = shardInitialCapacity;
            return this;
        }

        public ConcurrentMaps<K, V> loadFactor(float loadFactor) {
            this.loadFactor = loadFactor;
            return this;
        }

        public ConcurrentMaps<K, V> hashBehavior(@NotNull HashBehavior<K> hashBehavior) {
            this.hashBehavior = Objects.requireNonNull(hashBehavior);
            return this;
        }

        public <C extends Map<K, V>> C create() {
            if (this.supplier != null) {
                return (C) this.supplier.get(this.shards, this.shardInitialCapacity, this.cacheLinePadding, this.loadFactor, this.hashBehavior, this.syncQuery);
            } else throw new IllegalStateException();
        }
    }

    /**
     * Returns a reusable builder for a concurrent general-purpose map using the
     * factory's default concurrent implementation for common scenarios.
     *
     * <p>The returned builder can be configured before creating one or more map
     * instances. Reuse the builder when the same configuration is needed
     * multiple times.
     *
     * <p>The {@code keyClass} and {@code valueClass} arguments distinguish
     * primitive types from wrapper types. Supplying a primitive class literal
     * may create a primitive-specialized implementation; supplying the wrapper
     * class literal selects the reference-type implementation. Only primitive
     * class literals trigger creation of primitive-specialized implementations.
     *
     * <p>The primitive {@code byte.class} is not supported by this factory
     * method. Use {@code Byte.class} instead; passing {@code byte.class} as
     * either the key or value class throws {@link IllegalArgumentException}.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param keyClass the key class; primitive and wrapper class literals have
     *        different semantics
     * @param valueClass the value class; primitive and wrapper class literals
     *        have different semantics
     * @return a reusable builder for a concurrent general-purpose map
     * @throws IllegalArgumentException if {@code byte.class} is supplied or if
     *         the specified classes are not supported by the selected
     *         implementation
     * @throws RuntimeException if the default implementation cannot be loaded
     *         or instantiated
     * @see ConcurrentMaps
     */
    public static <K, V> ConcurrentMaps<K, V> ofConcurrent(Class<K> keyClass, Class<V> valueClass) {
        if (isPrimitiveBool(keyClass) || keyClass == byte.class) throw typeNotFound(keyClass);
        if (isPrimitiveBool(valueClass)) throw typeNotFound(valueClass);

        StringBuilder sb = pickPackage();
        pickHead(keyClass, sb);
        pickNext(valueClass, sb);
        sb.append("LinearProbingShardConcurrentHashMap");

        try {
            final Class<?> clazz = Class.forName(sb.toString(), false, Class.class.getClassLoader());
            final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class, int.class, short.class, float.class, HashBehavior.class, boolean.class));
            return new ConcurrentMaps<>(keyClass, valueClass, (shards, shardInitialCapacity, cacheLinePadding, loadFactor, hashBehavior, syncQuery) -> {
                try {
                    return (Map<K, V>) mh.invoke(shards, shardInitialCapacity, cacheLinePadding, loadFactor, hashBehavior, syncQuery);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Throwable e) {
            throw factoryFailed(e);
        }
    }

    public final static class PairMaps<L, R, V> {
        private int initialCapacity = HashContainer.DEFAULT_INITIAL_CAPACITY;
        private float loadFactor = HashContainer.DEFAULT_LOAD_FACTOR;
        private HashBehavior<L> firstHashBehavior = HashBehaviorFactory.fibonacci();
        private HashBehavior<R> secondHashBehavior = HashBehaviorFactory.fibonacci();
        private final Supplier<L, R, V> supplier;

        private interface Supplier<L, R, V> {
            PairMap<L, R, V> get(int initialCapacity, float loadFactor, final HashBehavior<L> firstHashBehavior, final HashBehavior<R> secondHashBehavior);
        }

        private PairMaps(Class<L> firstKeyClass, Class<R> secondKeyClass, Class<V> valueClass, Supplier<L, R, V> supplier) {
            this.supplier = supplier;
        }

        public PairMaps<L, R, V> initialCapacity(int initialCapacity) {
            this.initialCapacity = initialCapacity;
            return this;
        }

        public PairMaps<L, R, V> loadFactor(float loadFactor) {
            this.loadFactor = loadFactor;
            return this;
        }

        public PairMaps<L, R, V> firstHashBehavior(@NotNull HashBehavior<L> firstHashBehavior) {
            this.firstHashBehavior = Objects.requireNonNull(firstHashBehavior);
            return this;
        }

        public PairMaps<L, R, V> secondHashBehavior(@NotNull HashBehavior<R> secondHashBehavior) {
            this.secondHashBehavior = Objects.requireNonNull(secondHashBehavior);
            return this;
        }

        public <C extends PairMap<L, R, V>> C create() {
            if (this.supplier != null) {
                return (C) this.supplier.get(this.initialCapacity, this.loadFactor, this.firstHashBehavior, this.secondHashBehavior);
            } else throw new IllegalStateException();
        }
    }

    /**
     * Returns a reusable builder for a map with two keys (pair map) using the
     * factory's default implementation for common scenarios.
     *
     * <p>The returned builder can be configured before creating one or more map
     * instances. Reuse the builder when the same configuration is needed
     * multiple times.
     *
     * <p>The key classes must be reference types. Passing a primitive class
     * literal as either {@code firstKeyClass} or {@code secondKeyClass} throws
     * {@link IllegalArgumentException}. The value class follows the
     * primitive/wrapper distinction: only primitive class literals trigger
     * creation of primitive-specialized value handling where supported.
     *
     * @param <L> the first key type
     * @param <R> the second key type
     * @param <V> the value type
     * @param firstKeyClass the first key class; must be a reference type
     * @param secondKeyClass the second key class; must be a reference type
     * @param valueClass the value class; primitive and wrapper class literals
     *        have different semantics
     * @return a reusable builder for a pair map
     * @throws IllegalArgumentException if either key class is primitive or if
     *         the specified classes are not supported by the selected
     *         implementation
     * @throws RuntimeException if the default implementation cannot be loaded
     *         or instantiated
     * @see PairMaps
     */
    public static <L, R, V> PairMaps<L, R, V> ofPair(Class<L> firstKeyClass, Class<R> secondKeyClass, Class<V> valueClass) {
        if (isPrimitive(firstKeyClass)) throw typeNotFound(firstKeyClass);
        if (isPrimitive(secondKeyClass)) throw typeNotFound(secondKeyClass);
        if (isPrimitiveBool(valueClass)) throw typeNotFound(valueClass);

        StringBuilder sb = pickPackage();
        sb.append("objecttype.ObjObj");
//        pickHead(firstKeyClass, sb);
//        pickNext(secondKeyClass, sb);
        pickNext(valueClass, sb);
        sb.append("LinearProbingShardConcurrentHashMap");

        try {
            final Class<?> clazz = Class.forName(sb.toString(), false, Class.class.getClassLoader());
            final MethodHandle mh = MethodHandles.lookup().findConstructor(clazz, MethodType.methodType(void.class, int.class, float.class, HashBehavior.class, HashBehavior.class));
            return new PairMaps<>(firstKeyClass, secondKeyClass, valueClass, (initialCapacity, loadFactor, firstHashBehavior, secondHashBehavior) -> {
                try {
                    return (PairMap<L, R, V>) mh.invoke(initialCapacity, loadFactor, firstHashBehavior, secondHashBehavior);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Throwable e) {
            throw factoryFailed(e);
        }
    }

    private MapFactory() {}
}
