package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CharByteBiFunction<R> extends BiFunction<Character, Byte, R> {
    R apply(char t, byte u);

    default R apply(Character t, Byte u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (byte) 0);
    }
}
