package org.boxutil.util.function.objecttype;

@FunctionalInterface
public interface ObjLongTeConsumer<T, U> extends TeConsumer<T, U, Long> {
    void accept(T t, U u, long v);

    default void accept(T t, U u, Long v) {
        this.accept(t, u, v == null ? (long) 0 : v);
    }
}
