package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CharByteBiOperator extends BiFunction<Character, Byte, Byte> {
    byte apply(char t, byte u);

    default Byte apply(Character t, Byte u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (byte) 0);
    }
}
