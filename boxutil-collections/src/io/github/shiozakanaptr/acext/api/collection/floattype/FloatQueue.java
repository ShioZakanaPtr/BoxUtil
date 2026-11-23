package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.QueueExt;
import io.github.shiozakanaptr.acext.util.function.floattype.FloatConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Type specialization interface.
 *
 * @see QueueExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatQueue extends QueueExt<Float>, FloatCollection {
    float peekRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Float peek() {
        return this.peekRaw();
    }

    default float elementRaw() {
        float result;
        if ((result = this.peekRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Float element() {
        return this.elementRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(float)
     */
    @Deprecated
    default boolean add(Float e) {
        return FloatCollection.super.add(e);
    }

    default boolean offer(float e) {
        return this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(float)
     */
    @Deprecated
    default boolean offer(Float e) {
        return this.offer(e != null ? e.floatValue() : (float) 0);
    }

    float pollRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Float poll() {
        return this.pollRaw();
    }

    default float removeRaw() {
        float result;
        if ((result = this.pollRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Float remove() {
        return this.removeRaw();
    }

    boolean tryPollExecuteOffer(@NotNull FloatConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOffer(FloatConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super Float> action) {
        return this.tryPollExecuteOffer((float l_t) -> action.accept(l_t));
    }

    default float getNullElementRaw() {
        return (float) 0;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getNullElementRaw()
     */
    @Deprecated
    default Float getNullElement() {
        return this.getNullElementRaw();
    }
}
