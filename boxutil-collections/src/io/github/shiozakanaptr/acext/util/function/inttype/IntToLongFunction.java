package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Function;

@FunctionalInterface
public interface IntToLongFunction extends Function<Integer, Long> {
    long apply(int t);

    default Long apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
