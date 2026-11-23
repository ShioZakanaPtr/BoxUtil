package io.github.shiozakanaptr.acext.api.collection.longtype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Type specialization class.
 *
 * @see LongComparator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class ReversedLongComparator implements LongComparator, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    private final LongComparator comparator;

    public ReversedLongComparator(@NotNull LongComparator comparator) {
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
