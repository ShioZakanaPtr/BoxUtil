package org.boxutil.base.api.container.intt;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

record ReversedIntComparator(IntComparator comparator) implements IntComparator, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public ReversedIntComparator(IntComparator comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    public int compare(int o1, int o2) {
        return this.comparator.compare(o2, o1);
    }

    @NotNull
    public IntComparator reversed() {
        return this.comparator;
    }
}
