package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.QueueExt;
import java.util.function.DoubleConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface DoubleQueue extends QueueExt<Double>, DoubleCollection {
    double peekRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Double peek() {
        return this.peekRaw();
    }

    default double elementRaw() {
        double result;
        if ((result = this.peekRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Double element() {
        return this.elementRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(double)
     */
    @Deprecated
    default boolean add(Double e) {
        return DoubleCollection.super.add(e);
    }

    default boolean offer(double e) {
        return this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(double)
     */
    @Deprecated
    default boolean offer(Double e) {
        return this.offer(e != null ? e.doubleValue() : (double) 0);
    }

    double pollRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Double poll() {
        return this.pollRaw();
    }

    default double removeRaw() {
        double result;
        if ((result = this.pollRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Double remove() {
        return this.removeRaw();
    }

    boolean tryPollExecuteOffer(@NotNull DoubleConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOffer(DoubleConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super Double> action) {
        return this.tryPollExecuteOffer((double l_t) -> action.accept(l_t));
    }

    default double getNullElementRaw() {
        return (double) 0;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getNullElementRaw()
     */
    @Deprecated
    default Double getNullElement() {
        return this.getNullElementRaw();
    }
}
