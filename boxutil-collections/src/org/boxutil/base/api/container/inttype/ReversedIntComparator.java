package org.boxutil.base.api.container.inttype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

final class ReversedIntComparator implements IntComparator, Serializable {
    private static final long serialVersionUID = 1L;

    private final IntComparator comparator;

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
