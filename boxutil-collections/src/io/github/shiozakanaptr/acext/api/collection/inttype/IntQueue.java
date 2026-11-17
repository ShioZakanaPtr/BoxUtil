package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.QueueExt;
import java.util.function.IntConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface IntQueue extends QueueExt<Integer>, IntCollection {
    int peekRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Integer peek() {
        return this.peekRaw();
    }

    default int elementRaw() {
        int result;
        if ((result = this.peekRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Integer element() {
        return this.elementRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(int)
     */
    @Deprecated
    default boolean add(Integer e) {
        return IntCollection.super.add(e);
    }

    default boolean offer(int e) {
        return this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(int)
     */
    @Deprecated
    default boolean offer(Integer e) {
        return this.offer(e != null ? e.intValue() : (int) 0);
    }

    int pollRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Integer poll() {
        return this.pollRaw();
    }

    default int removeRaw() {
        int result;
        if ((result = this.pollRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Integer remove() {
        return this.removeRaw();
    }

    boolean tryPollExecuteOffer(@NotNull IntConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOffer(IntConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super Integer> action) {
        return this.tryPollExecuteOffer((int l_t) -> action.accept(l_t));
    }

    default int getNullElementRaw() {
        return (int) 0;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getNullElementRaw()
     */
    @Deprecated
    default Integer getNullElement() {
        return this.getNullElementRaw();
    }
}
