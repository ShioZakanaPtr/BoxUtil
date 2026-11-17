package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface CharUnaryOperator extends UnaryOperator<Character> {
    char apply(char t);

    default Character apply(Character t) {
        return this.apply(t != null ? t : (char) 0);
    }
}
