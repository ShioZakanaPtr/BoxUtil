package org.boxutil.util.function.objecttype;

@FunctionalInterface
public interface ObjLongTeFunction<T, U, R> extends TeFunction<T, U, Long, R> {
    R apply(T t, U u, long v);

    default R apply(T t, U u, Long v) {
        return this.apply(t, u, v == null ? (long) 0 : v);
    }
}
