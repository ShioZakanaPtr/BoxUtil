package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.Consumer;

@FunctionalInterface
public interface FloatConsumer extends Consumer<Float> {
    void accept(float t);

    default void accept(Float t) {
        this.accept(t != null ? t : (float) 0);
    }
}
