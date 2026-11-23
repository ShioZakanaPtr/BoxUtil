package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

/**
 * Type specialization functional interface.
 *
 * @see BiFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ObjCharBiOperator<T> extends BiFunction<T, Character, Character> {
    char apply(T t, char u);

    default Character apply(T t, Character u) {
        return this.apply(t, u != null ? u : (char) 0);
    }
}
