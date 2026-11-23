package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.util.function.floattype.FloatConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see java.util.Iterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatIterator extends PrimitiveIterator<Float, FloatConsumer> {
    float nextFloat();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #nextFloat()
     */
    @Deprecated
    default Float next() {
        return this.nextFloat();
    }

    default void forEachRemaining(@NotNull FloatConsumer action) {
        while (this.hasNext()) {
            action.accept(this.nextFloat());
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEachRemaining(FloatConsumer)
     */
    @Deprecated
    default void forEachRemaining(@NotNull Consumer<? super Float> action) {
        this.forEachRemaining((float l_t) -> action.accept(l_t));
    }
}
