package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface CharToDoubleFunction extends Function<Character, Double> {
    double apply(char t);

    default Double apply(Character t) {
        return this.apply(t != null ? t : (char) 0);
    }
}
