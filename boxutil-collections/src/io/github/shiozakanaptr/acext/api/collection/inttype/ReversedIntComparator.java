package io.github.shiozakanaptr.acext.api.collection.inttype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Type specialization class.
 *
 * @see IntComparator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class ReversedIntComparator implements IntComparator, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    private final IntComparator comparator;

    public ReversedIntComparator(@NotNull IntComparator comparator) {
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
