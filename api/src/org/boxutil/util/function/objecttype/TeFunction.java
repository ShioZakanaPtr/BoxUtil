package org.boxutil.util.function.objecttype;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TeFunction<T, U, V, R> {
    R apply(T t, U u, V v);

    default <A> TeFunction<T, U, V, A> andThen(Function<? super R, ? extends A> after) {
        Objects.requireNonNull(after);

        return (t, u, v) -> after.apply(this.apply(t, u, v));
    }
}
