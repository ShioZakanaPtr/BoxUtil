package org.boxutil.base.api.container.shorttype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

final class ReversedShortComparator implements ShortComparator, Serializable {
    private static final long serialVersionUID = 1L;

    private final ShortComparator comparator;

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
