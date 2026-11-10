package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Consumer;

@FunctionalInterface
public interface IntConsumer extends Consumer<Integer> {
    void accept(int t);

    default void accept(Integer t) {
        this.accept(t != null ? t : (int) 0);
    }
}
