package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.Objects;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result. This is the three-arity specialization of
 * {@link java.util.function.Consumer}.
 *
 * <p>This is a <a href="{@docRoot}/java.base/java/lang/FunctionalInterface.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object, Object)}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * @see java.util.function.Consumer
 * @see java.util.function.BiConsumer
 * @since 1.0
 */
@FunctionalInterface
public interface TeConsumer<T, U, V> {
    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(T t, U u, V v);

    /**
     * Returns a composed {@code TeConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TeConsumer} that performs in sequence this
     *         operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is {@code null}
     */
    default TeConsumer<T, U, V> andThen(TeConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);

        return (t, u, v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}
