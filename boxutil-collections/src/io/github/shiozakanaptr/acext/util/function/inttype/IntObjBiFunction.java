package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntObjBiFunction<U, R> extends BiFunction<Integer, U, R> {
    R apply(int t, U u);

    default R apply(Integer t, U u) {
        return this.apply(t != null ? t : (int) 0, u);
    }
}
