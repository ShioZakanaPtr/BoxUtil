package io.github.shiozakanaptr.acext.api.collection.doubletype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

final class ReversedDoubleComparator implements DoubleComparator, Serializable {
    private static final long serialVersionUID = 1L;

    private final DoubleComparator comparator;

    public ReversedDoubleComparator(@NotNull DoubleComparator comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    public int compare(double o1, double o2) {
        return this.comparator.compare(o2, o1);
    }

    @NotNull
    public DoubleComparator reversed() {
        return this.comparator;
    }
}
