package org.boxutil.util.function.bytetype;

import java.util.function.Predicate;

public interface BytePredicate extends Predicate<Byte> {
    boolean test(byte t);

    default boolean test(Byte t) {
        return this.test(t == null ? (byte) 0 : t);
    }
}
