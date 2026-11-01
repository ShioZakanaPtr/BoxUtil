package org.boxutil.base.api.container.longtype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

final class ReversedLongComparator implements LongComparator, Serializable {
    private static final long serialVersionUID = 1L;

    private final LongComparator comparator;

    public ReversedLongComparator(LongComparator comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    public int compare(long o1, long o2) {
        return this.comparator.compare(o2, o1);
    }

    @NotNull
    public LongComparator reversed() {
        return this.comparator;
    }
}
