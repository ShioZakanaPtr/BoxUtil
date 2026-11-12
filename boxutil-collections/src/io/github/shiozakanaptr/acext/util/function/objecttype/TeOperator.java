package io.github.shiozakanaptr.acext.util.function.objecttype;

/**
 * Represents an operation on three operands that produces a result of the
 * same type as its operands. This is a specialization of
 * {@link TeFunction} for the case where the arguments and the result are all
 * the same type.
 *
 * <p>This is a <a href="{@docRoot}/java.base/java/lang/FunctionalInterface.html">functional interface</a>
 * whose functional method is {@link #apply(Object, Object, Object)}.
 *
 * @param <T> the type of the first operand and of the result
 * @param <U> the type of the second operand
 * @param <R> the type of the third operand and of the result
 * @see TeFunction
 * @see java.util.function.UnaryOperator
 * @see java.util.function.BinaryOperator
 * @since 1.0.0
 */
@FunctionalInterface
public interface TeOperator<T, U, R> extends TeFunction<T, U, R, R> {
}
