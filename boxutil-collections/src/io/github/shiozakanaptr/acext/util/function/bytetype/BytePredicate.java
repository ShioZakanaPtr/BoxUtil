package io.github.shiozakanaptr.acext.util.function.bytetype;

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
public interface BytePredicate extends Predicate<Byte> {
    boolean test(byte t);

    default boolean test(Byte t) {
        return this.test(t != null ? t : (byte) 0);
    }
}
