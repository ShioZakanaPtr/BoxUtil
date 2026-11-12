package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts three arguments and produces a result.
 * This is the three-arity specialization of
 * {@link java.util.function.Function}.
 *
 * <p>This is a <a href="{@docRoot}/java.base/java/lang/FunctionalInterface.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object)}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @param <R> the type of the result of the function
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @since 1.0.0
 */
@FunctionalInterface
public interface TeFunction<T, U, V, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     * @return the function result
     */
    R apply(T t, U u, V v);

    /**
     * Returns a composed function that first applies this function to its
     * input, and then applies the {@code after} function to the result. If
     * evaluation of either function throws an exception, it is relayed to the
     * caller of the composed function.
     *
     * @param <A> the type of output of the {@code after} function, and of the
     *        composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     *         applies the {@code after} function
     * @throws NullPointerException if {@code after} is {@code null}
     */
    default <A> TeFunction<T, U, V, A> andThen(Function<? super R, ? extends A> after) {
        Objects.requireNonNull(after);

        return (t, u, v) -> after.apply(this.apply(t, u, v));
    }
}
