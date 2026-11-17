package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.Predicate;

@FunctionalInterface
public interface CharPredicate extends Predicate<Character> {
    boolean test(char t);

    default boolean test(Character t) {
        return this.test(t != null ? t : (char) 0);
    }
}
