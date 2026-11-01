package org.boxutil.base.api.container.bytetype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

final class ReversedByteComparator implements ByteComparator, Serializable {
    private static final long serialVersionUID = 1L;

    private final ByteComparator comparator;

    public ReversedByteComparator(ByteComparator comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    public int compare(byte o1, byte o2) {
        return this.comparator.compare(o2, o1);
    }

    @NotNull
    public ByteComparator reversed() {
        return this.comparator;
    }
}
