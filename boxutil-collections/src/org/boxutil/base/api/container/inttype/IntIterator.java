package org.boxutil.base.api.container.inttype;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface IntIterator extends PrimitiveIterator.OfInt {
    int nextInt();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer next() {
        return this.nextInt();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEachRemaining(Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining((int l_value) -> action.accept(l_value));
    }
}
