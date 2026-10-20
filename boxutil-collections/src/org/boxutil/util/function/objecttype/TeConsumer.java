package org.boxutil.util.function.objecttype;

import java.util.Objects;

@FunctionalInterface
public interface TeConsumer<T, U, V> {
    void accept(T t, U u, V v);

    default TeConsumer<T, U, V> andThen(TeConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);

        return (t, u, v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}
