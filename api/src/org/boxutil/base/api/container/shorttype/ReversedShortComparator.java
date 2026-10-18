package org.boxutil.base.api.container.shorttype;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

record ReversedShortComparator(ShortComparator comparator) implements ShortComparator, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public ReversedShortComparator(ShortComparator comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    public int compare(short o1, short o2) {
        return this.comparator.compare(o2, o1);
    }

    @NotNull
    public ShortComparator reversed() {
        return this.comparator;
    }
}
