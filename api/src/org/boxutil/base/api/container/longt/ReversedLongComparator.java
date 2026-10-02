package org.boxutil.base.api.container.longt;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

record ReversedLongComparator(LongComparator comparator) implements LongComparator, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
