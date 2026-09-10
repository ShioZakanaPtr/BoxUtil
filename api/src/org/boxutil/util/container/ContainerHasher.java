package org.boxutil.util.container;

import java.util.HashMap;

@FunctionalInterface
public interface ContainerHasher {
    int hashing(Object key);

    /**
     * Using {@link Object#hashCode()} as final hash.
     */
    static int hashing_Directly(final Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    static int hashing_HashMap(int key) {
        return key ^ (key >>> 16);
    }

    /**
     * Same as {@link HashMap}.
     */
    static int hashing_HashMap(Object key) {
        int h;
        return (key == null) ? 0 : hashing_HashMap(key.hashCode());
    }

    static int hashing_MurmurHash3FMix(int key) {
        key ^= key >>> 16;
        key *= 0x85ebca6b;
        key ^= key >>> 13;
        key *= 0xc2b2ae35;
        key ^= key >>> 16;
        return key;
    }

    /**
     * Finalization mix with {@link Object#hashCode()}.
     */
    static int hashing_MurmurHash3FMix(final Object key) {
        if (key == null) return 0;
        return hashing_MurmurHash3FMix(key.hashCode());
    }
}
