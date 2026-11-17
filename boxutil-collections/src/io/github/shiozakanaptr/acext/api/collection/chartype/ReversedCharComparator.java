package io.github.shiozakanaptr.acext.api.collection.chartype;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

final class ReversedCharComparator implements CharComparator, Serializable {
    private static final long serialVersionUID = 1L;

    private final CharComparator comparator;

    public ReversedCharComparator(@NotNull CharComparator comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    public int compare(char o1, char o2) {
        return this.comparator.compare(o2, o1);
    }

    @NotNull
    public CharComparator reversed() {
        return this.comparator;
    }
}
