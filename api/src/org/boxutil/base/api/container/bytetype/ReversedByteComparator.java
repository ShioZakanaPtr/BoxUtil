package org.boxutil.base.api.container.bytetype;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

record ReversedByteComparator(ByteComparator comparator) implements ByteComparator, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
