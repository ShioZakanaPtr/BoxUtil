package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.QueueExt;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public interface LongQueue extends QueueExt<Long>, LongCollection {
    long peekRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long peek() {
        return this.peekRaw();
    }

    default long elementRaw() {
        long result;
        if ((result = this.peekRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long element() {
        return this.elementRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean add(Long e) {
        return LongCollection.super.add(e);
    }

    default boolean offer(long e) {
        return this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean offer(Long e) {
        return this.offer(e != null ? e.longValue() : (long) 0);
    }

    long pollRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long poll() {
        return this.pollRaw();
    }

    default long removeRaw() {
        long result;
        if ((result = this.pollRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remove() {
        return this.removeRaw();
    }

    boolean tryPollExecuteOffer(@NotNull LongConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super Long> action) {
        return this.tryPollExecuteOffer((long l_t) -> action.accept(l_t));
    }

    default long getNullElementRaw() {
        return (long) 0;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getNullElement() {
        return this.getNullElementRaw();
    }
}
