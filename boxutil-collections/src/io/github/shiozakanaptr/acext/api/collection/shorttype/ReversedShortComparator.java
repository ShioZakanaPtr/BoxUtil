package io.github.shiozakanaptr.acext.api.collection.shorttype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Type specialization class.
 *
 * @see ShortComparator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
final class ReversedShortComparator implements ShortComparator, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;

    private final ShortComparator comparator;

    public ReversedShortComparator(@NotNull ShortComparator comparator) {
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
