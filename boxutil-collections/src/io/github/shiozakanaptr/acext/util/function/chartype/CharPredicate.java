package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.Predicate;

/**
 * Type specialization functional interface.
 *
 * @see Predicate
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface CharPredicate extends Predicate<Character> {
    boolean test(char t);

    default boolean test(Character t) {
        return this.test(t != null ? t : (char) 0);
    }
}
