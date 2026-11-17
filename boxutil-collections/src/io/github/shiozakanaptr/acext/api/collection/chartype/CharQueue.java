package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.QueueExt;
import io.github.shiozakanaptr.acext.util.function.chartype.CharConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public interface CharQueue extends QueueExt<Character>, CharCollection {
    char peekRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #peekRaw()
     */
    @Deprecated
    default Character peek() {
        return this.peekRaw();
    }

    default char elementRaw() {
        char result;
        if ((result = this.peekRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #elementRaw()
     */
    @Deprecated
    default Character element() {
        return this.elementRaw();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #add(char)
     */
    @Deprecated
    default boolean add(Character e) {
        return CharCollection.super.add(e);
    }

    default boolean offer(char e) {
        return this.add(e);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #offer(char)
     */
    @Deprecated
    default boolean offer(Character e) {
        return this.offer(e != null ? e.charValue() : (char) 0);
    }

    char pollRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #pollRaw()
     */
    @Deprecated
    default Character poll() {
        return this.pollRaw();
    }

    default char removeRaw() {
        char result;
        if ((result = this.pollRaw()) == this.getNullElementRaw()) throw new NoSuchElementException();
        return result;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw()
     */
    @Deprecated
    default Character remove() {
        return this.removeRaw();
    }

    boolean tryPollExecuteOffer(@NotNull CharConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #tryPollExecuteOffer(CharConsumer)
     */
    @Deprecated
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super Character> action) {
        return this.tryPollExecuteOffer((char l_t) -> action.accept(l_t));
    }

    default char getNullElementRaw() {
        return (char) 0;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getNullElementRaw()
     */
    @Deprecated
    default Character getNullElement() {
        return this.getNullElementRaw();
    }
}
