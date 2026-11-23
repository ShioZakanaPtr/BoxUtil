package io.github.shiozakanaptr.acext.api.collection.floattype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Type specialization class.
 *
 * @see FloatComparator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class ReversedFloatComparator implements FloatComparator, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    private final FloatComparator comparator;

    public ReversedFloatComparator(@NotNull FloatComparator comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    public int compare(float o1, float o2) {
        return this.comparator.compare(o2, o1);
    }

    @NotNull
    public FloatComparator reversed() {
        return this.comparator;
    }
}
