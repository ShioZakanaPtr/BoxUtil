package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface CharToIntFunction extends Function<Character, Integer> {
    int apply(char t);

    default Integer apply(Character t) {
        return this.apply(t != null ? t : (char) 0);
    }
}
