package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.QueueExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.ShortConsumer;
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
public interface ShortQueue extends QueueExt<Short>, ShortCollection {
    short peekRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Short peek() {
        return this.peekRaw();
    }

    default short elementRaw() {
        short result;
        if ((result = this.peekRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Short element() {
        return this.elementRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(short)
     */
    @Deprecated
    default boolean add(Short e) {
        return ShortCollection.super.add(e);
    }

    default boolean offer(short e) {
        return this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(short)
     */
    @Deprecated
    default boolean offer(Short e) {
        return this.offer(e != null ? e.shortValue() : (short) 0);
    }

    short pollRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Short poll() {
        return this.pollRaw();
    }

    default short removeRaw() {
        short result;
        if ((result = this.pollRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Short remove() {
        return this.removeRaw();
    }

    boolean tryPollExecuteOffer(@NotNull ShortConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOffer(ShortConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super Short> action) {
        return this.tryPollExecuteOffer((short l_t) -> action.accept(l_t));
    }

    default short getNullElementRaw() {
        return (short) 0;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getNullElementRaw()
     */
    @Deprecated
    default Short getNullElement() {
        return this.getNullElementRaw();
    }
}
