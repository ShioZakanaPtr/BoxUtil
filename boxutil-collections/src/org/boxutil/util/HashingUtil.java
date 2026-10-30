package org.boxutil.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HashingUtil {
    /**
     * A hash strategy that uses the input parameter value directly as the
     * final hash, without any mixing.
     *
     * @param key the input value
     * @return the same value as {@code key}
     */
    public static int directly(int key) {
        return key;
    }

    /**
     * A hash strategy that uses {@link Object#hashCode()} as the final hash.
     * If the key is {@code null}, returns {@code 0}.
     *
     * @param key the object key, possibly {@code null}
     * @return the hash code of the key, or {@code 0} if the key is {@code null}
     */
    public static int directly(@Nullable final Object key) {
        return key == null ? 0 : key.hashCode();
    }

    /**
     * A hash strategy that multiply by golden ratio to the input value.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    public static int fibonacciBit32(int key) {
        return key * 0x61c88647;
    }

    /**
     * A hash strategy that multiply by golden ratio to the input value.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    public static long fibonacciBit64(long key) {
        return key * 0x61c8864680b583ebL;
    }

    /**
     * A hash strategy that multiply by golden ratio to an object key.
     * If the key is {@code null}, returns {@code 0}; otherwise,
     * applies {@link #fibonacciBit32(int)} to the key's {@link Object#hashCode()}.
     *
     * @param key the object key, possibly {@code null}
     * @return the hash code of the key, or {@code 0} if the key is {@code null}
     */
    public static int fibonacci(@Nullable final Object key) {
        return key == null ? 0 : fibonacciBit32(key.hashCode());
    }

    /**
     * A hash strategy that applies the same supplemental hash function like
     * {@link java.util.HashMap}: {@code key ^ (key >>> 16)}.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    public static int bitMix(int key) {
        return key ^ (key >>> 16);
    }

    /**
     * A hash strategy that applies the same supplemental hash function like
     * {@link java.util.HashMap} to an object key. If the key is {@code null},
     * returns {@code 0}; otherwise, applies {@link #bitMix(int)} to
     * the key's {@link Object#hashCode()}.
     *
     * @param key the object key, possibly {@code null}
     * @return the mixed hash value
     */
    public static int bitMix(@Nullable final Object key) {
        return key == null ? 0 : bitMix(key.hashCode());
    }

    /**
     * A hash strategy that multiply by golden ratio to the input value, and
     * then mix it as {@link java.util.HashMap}.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    public static int fibonacciMixBit32(int key) {
        final int result = key * 0x61c88647;
        return result ^ (result >>> 16);
    }

    /**
     * A hash strategy that multiply by golden ratio to the input value, and
     * then mix it as {@link java.util.HashMap}.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    public static long fibonacciMixBit64(long key) {
        final long result = key * 0x61c8864680b583ebL;
        return result ^ (result >>> 32);
    }

    /**
     * A hash strategy that multiply by golden ratio to an object key, and
     * then mix it as {@link java.util.HashMap}.
     * If the key is {@code null}, returns {@code 0}; otherwise,
     * applies {@link #fibonacciBit32(int)} to the key's {@link Object#hashCode()}.
     *
     * @param key the object key, possibly {@code null}
     * @return the hash code of the key, or {@code 0} if the key is {@code null}
     */
    public static int fibonacciMix(@Nullable final Object key) {
        return key == null ? 0 : fibonacciMixBit32(key.hashCode());
    }

    /**
     * A hash strategy that applies the MurmurHash3 finalization mix to the
     * input value.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    public static int murmur3HashFMixBit32(int key) {
        key = (key ^ (key >>> 16)) * 0x85ebca6b;
        key = (key ^ (key >>> 13)) * 0xc2b2ae35;
        return key ^ (key >>> 16);
    }

    /**
     * A hash strategy that applies the MurmurHash3 finalization mix to the
     * input value.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    public static long murmur3HashFMixBit64(long key) {
        key = (key ^ (key >>> 33)) * 0xff51afd7ed558ccdL;
        key = (key ^ (key >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return key ^ (key >>> 33);
    }

    /**
     * A hash strategy that applies the MurmurHash3 finalization mix to an
     * object key. If the key is {@code null}, returns {@code 0}; otherwise,
     * applies {@link #murmur3HashFMixBit32(int)} to the key's
     * {@link Object#hashCode()}.
     *
     * @param key the object key, possibly {@code null}
     * @return the mixed hash value
     */
    public static int murmurHash3FMix(@Nullable final Object key) {
        return key == null ? 0 : murmur3HashFMixBit32(key.hashCode());
    }

    private static int read32(final byte[] data, int ptr) {
        return (data[ptr] & 0xff) | (data[ptr + 1] & 0xff) << 8 | (data[ptr + 2] & 0xff) << 16 | (data[ptr + 3] & 0xff) << 24;
    }

    private static long read64(final byte[] data, int ptr) {
        return (data[ptr] & 0xffL) | (data[ptr + 1] & 0xffL) << 8 | (data[ptr + 2] & 0xffL) << 16 | (data[ptr + 3] & 0xffL) << 24 |
                (data[ptr + 4] & 0xffL) << 32 | (data[ptr + 5] & 0xffL) << 40 | (data[ptr + 6] & 0xffL) << 48 | (data[ptr + 7] & 0xffL) << 56;
    }

    /**
     * Returns the 32-bit MurmurHash3 hash of the given bytes using the
     * specified seed.
     *
     * <p>This method implements the {@code MurmurHash3_x86_32} variant.
     *
     * @apiNote The result is a 32-bit hash value. Use
     *          {@link Integer#toUnsignedLong(int)} to obtain its unsigned
     *          representation.
     *
     * @param data the input bytes to hash; must not be {@code null}
     * @param seed the seed value
     * @return the 32-bit MurmurHash3 hash of {@code data}
     * @throws NullPointerException if {@code data} is {@code null}
     */
    public static int murmurHash3Bit32(@NotNull final byte[] data, int seed) {
        final int dataLength = data.length,
                fullLength = dataLength & ~3,
                c1 = 0xcc9e2d51,
                c2 = 0x1b873593;
        int hash = seed;

        for (int i = 0; i < fullLength; i += 4) {
            hash = Integer.rotateLeft(hash ^ (Integer.rotateLeft(read32(data, i) * c1, 15) * c2), 13) * 5 + 0xe6546b64;
        }

        int k = 0;
        switch (dataLength & 3) {
            case 3: k ^= (data[fullLength + 2] & 0xff) << 16;
            case 2: k ^= (data[fullLength + 1] & 0xff) << 8;
            case 1: hash ^= Integer.rotateLeft((k ^ (data[fullLength] & 0xff)) * c1, 15) * c2;
        }

        return murmur3HashFMixBit32(hash ^ dataLength);
    }

    /**
     * Computes the 128-bit MurmurHash3 hash of the given bytes using the
     * specified seed and stores the result in {@code result}.
     *
     * <p>This method implements the {@code MurmurHash3_x64_128} variant.
     * The first 64-bit half of the hash is stored in {@code result[0]}, and
     * the second 64-bit half is stored in {@code result[1]}. Only the first
     * two elements of {@code result} are written.
     *
     * @param data the input bytes to hash; must not be {@code null}
     * @param seed the seed value
     * @param result the array that receives the 128-bit hash; must not be
     *        {@code null} and must have a length of at least {@code 2}
     * @throws NullPointerException if {@code data} or {@code result} is
     *         {@code null}
     * @throws IllegalArgumentException if {@code result.length < 2}
     */
    public static void murmurHash3Bit128(@NotNull final byte[] data, int seed, @NotNull final long[] result) {
        if (result.length < 2) throw new IllegalArgumentException("Length of the result array must be at least 2, now: '" + result.length + '\'');

        final int dataLength = data.length,
                fullLength = dataLength & ~15;
        final long c1 = 0x87c37b91114253d5L,
                c2 = 0x4cf5ad432745937fL;
        long hash1 = seed, hash2 = seed;

        for (int i = 0; i < fullLength; i += 16) {
            hash1 = (Long.rotateLeft(hash1 ^ (Long.rotateLeft(read64(data, i) * c1, 31) * c2), 27) + hash2) * 5 + 0x52dce729L;
            hash2 = (Long.rotateLeft(hash2 ^ (Long.rotateLeft(read64(data, i + 8) * c2, 33) * c1), 31) + hash1) * 5 + 0x38495ab5L;
        }

        long k1 = 0, k2 = 0;
        switch (dataLength & 15) {
            case 15: k2 ^= (data[fullLength + 14] & 0xffL) << 48;
            case 14: k2 ^= (data[fullLength + 13] & 0xffL) << 40;
            case 13: k2 ^= (data[fullLength + 12] & 0xffL) << 32;
            case 12: k2 ^= (data[fullLength + 11] & 0xffL) << 24;
            case 11: k2 ^= (data[fullLength + 10] & 0xffL) << 16;
            case 10: k2 ^= (data[fullLength + 9] & 0xffL) << 8;
            case 9: hash2 ^= Long.rotateLeft((k2 ^ (data[fullLength + 8] & 0xff)) * c2, 33) * c1;
            case 8: k1 ^= (data[fullLength + 7] & 0xffL) << 56;
            case 7: k1 ^= (data[fullLength + 6] & 0xffL) << 48;
            case 6: k1 ^= (data[fullLength + 5] & 0xffL) << 40;
            case 5: k1 ^= (data[fullLength + 4] & 0xffL) << 32;
            case 4: k1 ^= (data[fullLength + 3] & 0xffL) << 24;
            case 3: k1 ^= (data[fullLength + 2] & 0xffL) << 16;
            case 2: k1 ^= (data[fullLength + 1] & 0xffL) << 8;
            case 1: hash1 ^= Long.rotateLeft((k1 ^ (data[fullLength] & 0xff)) * c1, 31) * c2;
        }

        hash1 ^= dataLength;
        hash2 ^= dataLength;
        hash1 += hash2;
        hash2 += hash1;
        hash1 = murmur3HashFMixBit64(hash1);
        hash2 = murmur3HashFMixBit64(hash2);
        result[0] = (hash1 += hash2);
        result[1] = hash2 + hash1;
    }

    /**
     * Returns the SipHash value of the given bytes using the specified
     * 128-bit key and round counts.
     *
     * <p>The key is supplied as two 64-bit halves. The {@code compression}
     * and {@code finalization} parameters select the SipHash compression
     * and finalization round counts, respectively.
     *
     * @param data the input bytes to hash; must not be {@code null}
     * @param keyLow the low 64 bits of the 128-bit key
     * @param keyHigh the high 64 bits of the 128-bit key
     * @param compression the number of compression rounds (the {@code c}
     *        parameter of SipHash); must be non-negative
     * @param finalization the number of finalization rounds (the {@code d}
     *        parameter of SipHash); must be non-negative
     * @return the 64-bit SipHash value of {@code data}
     * @throws NullPointerException if {@code data} is {@code null}
     * @throws IllegalArgumentException if {@code compression} or
     *         {@code finalization} is negative
     */
    public static long sipHash(@NotNull final byte[] data, long keyLow, long keyHigh, int compression, int finalization) {
        if (compression < 0) throw new IllegalArgumentException("Invalid compression rounds: '" + compression + '\'');
        if (finalization < 0) throw new IllegalArgumentException("Invalid finalization rounds: '" + finalization + '\'');

        final int dataLength = data.length,
                fullLength = dataLength & ~7;
        long v0 = 0x736f6d6570736575L ^ keyLow,
                v1 = 0x646f72616e646f6dL ^ keyHigh,
                v2 = 0x6c7967656e657261L ^ keyLow,
                v3 = 0x7465646279746573L ^ keyHigh,
                tail = (long) dataLength << 56;

        for (int i = 0; i < fullLength; i += 8) {
            final long m;
            v3 ^= (m = read64(data, i));
            for (int r = 0; r < compression; r++) {
                v0 += v1;
                v1 = Long.rotateLeft(v1, 13) ^ v0;
                v2 += v3;
                v3 = Long.rotateLeft(v3, 16) ^ v2;
                v0 = Long.rotateLeft(v0, 32) + v3;
                v3 = Long.rotateLeft(v3, 21) ^ v0;
                v2 += v1;
                v1 = Long.rotateLeft(v1, 17) ^ v2;
                v2 = Long.rotateLeft(v2, 32);
            }
            v0 ^= m;
        }

        switch (dataLength & 7) {
            case 7: tail |= (data[fullLength + 6] & 0xffL) << 48;
            case 6: tail |= (data[fullLength + 5] & 0xffL) << 40;
            case 5: tail |= (data[fullLength + 4] & 0xffL) << 32;
            case 4: tail |= (data[fullLength + 3] & 0xffL) << 24;
            case 3: tail |= (data[fullLength + 2] & 0xffL) << 16;
            case 2: tail |= (data[fullLength + 1] & 0xffL) << 8;
            case 1: tail |= data[fullLength] & 0xffL;
        }

        v3 ^= tail;
        for (int r = 0; r < compression; r++) {
            v0 += v1;
            v1 = Long.rotateLeft(v1, 13) ^ v0;
            v2 += v3;
            v3 = Long.rotateLeft(v3, 16) ^ v2;
            v0 = Long.rotateLeft(v0, 32) + v3;
            v3 = Long.rotateLeft(v3, 21) ^ v0;
            v2 += v1;
            v1 = Long.rotateLeft(v1, 17) ^ v2;
            v2 = Long.rotateLeft(v2, 32);
        }
        v0 ^= tail;

        v2 ^= 0xff;
        for (int r = 0; r < finalization; r++) {
            v0 += v1;
            v1 = Long.rotateLeft(v1, 13) ^ v0;
            v2 += v3;
            v3 = Long.rotateLeft(v3, 16) ^ v2;
            v0 = Long.rotateLeft(v0, 32) + v3;
            v3 = Long.rotateLeft(v3, 21) ^ v0;
            v2 += v1;
            v1 = Long.rotateLeft(v1, 17) ^ v2;
            v2 = Long.rotateLeft(v2, 32);
        }
        return v0 ^ v1 ^ v2 ^ v3;
    }

    /**
     * Returns the 32-bit HalfSipHash value of the given bytes using the
     * specified 64-bit key and round counts.
     *
     * <p>The key is supplied as two 32-bit halves. The {@code compression}
     * and {@code finalization} parameters select the HalfSipHash compression
     * and finalization round counts, respectively.
     *
     * @apiNote The result is a 32-bit hash value. Use
     *          {@link Integer#toUnsignedLong(int)} to obtain its unsigned
     *          representation.
     *
     * @param data the input bytes to hash; must not be {@code null}
     * @param keyLow the low 32 bits of the 64-bit key
     * @param keyHigh the high 32 bits of the 64-bit key
     * @param compression the number of compression rounds (the {@code c}
     *        parameter of HalfSipHash); must be non-negative
     * @param finalization the number of finalization rounds (the {@code d}
     *        parameter of HalfSipHash); must be non-negative
     * @return the 32-bit HalfSipHash value of {@code data}
     * @throws NullPointerException if {@code data} is {@code null}
     * @throws IllegalArgumentException if {@code compression} or
     *         {@code finalization} is negative
     */
    public static int halfSipHash(@NotNull final byte[] data, int keyLow, int keyHigh, int compression, int finalization) {
        if (compression < 0) throw new IllegalArgumentException("Invalid compression rounds: '" + compression + '\'');
        if (finalization < 0) throw new IllegalArgumentException("Invalid finalization rounds: '" + finalization + '\'');

        final int dataLength = data.length,
                fullLength = dataLength & ~3;
        int v0 = keyLow,
                v1 = keyHigh,
                v2 = 0x6c796765 ^ keyLow,
                v3 = 0x74656462 ^ keyHigh,
                tail = dataLength << 24;

        for (int i = 0; i < fullLength; i += 4) {
            final int m;
            v3 ^= (m = read32(data, i));
            for (int r = 0; r < compression; r++) {
                v0 += v1;
                v1 = Integer.rotateLeft(v1, 5) ^ v0;
                v2 += v3;
                v3 = Integer.rotateLeft(v3, 8) ^ v2;
                v0 += Integer.rotateLeft(v0, 16) + v3;
                v3 = Integer.rotateLeft(v3, 7) ^ v0;
                v2 += v1;
                v1 = Integer.rotateLeft(v1, 13) ^ v2;
                v2 = Integer.rotateLeft(v2, 16);
            }
            v0 ^= m;
        }

        switch (dataLength & 3) {
            case 3: tail |= (data[fullLength + 2] & 0xff) << 16;
            case 2: tail |= (data[fullLength + 1] & 0xff) << 8;
            case 1: tail |= data[fullLength] & 0xff;
        }

        v3 ^= tail;
        for (int r = 0; r < compression; r++) {
            v0 += v1;
            v1 = Integer.rotateLeft(v1, 5) ^ v0;
            v2 += v3;
            v3 = Integer.rotateLeft(v3, 8) ^ v2;
            v0 += Integer.rotateLeft(v0, 16) + v3;
            v3 = Integer.rotateLeft(v3, 7) ^ v0;
            v2 += v1;
            v1 = Integer.rotateLeft(v1, 13) ^ v2;
            v2 = Integer.rotateLeft(v2, 16);
        }
        v0 ^= tail;

        v2 ^= 0xff;
        for (int r = 0; r < finalization; r++) {
            v0 += v1;
            v1 = Integer.rotateLeft(v1, 5) ^ v0;
            v2 += v3;
            v3 = Integer.rotateLeft(v3, 8) ^ v2;
            v0 += Integer.rotateLeft(v0, 16) + v3;
            v3 = Integer.rotateLeft(v3, 7) ^ v0;
            v2 += v1;
            v1 = Integer.rotateLeft(v1, 13) ^ v2;
            v2 = Integer.rotateLeft(v2, 16);
        }
        return v0 ^ v1 ^ v2 ^ v3;
    }

    private HashingUtil() {}
}
