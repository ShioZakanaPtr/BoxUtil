package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.Function;

/**
 * Type specialization functional interface.
 *
 * @see Function
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface CharFunction<R> extends Function<Character, R> {
    R apply(char t);

    default R apply(Character t) {
        return this.apply(t != null ? t : (char) 0);
    }
}
