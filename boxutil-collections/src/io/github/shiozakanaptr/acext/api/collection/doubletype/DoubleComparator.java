package io.github.shiozakanaptr.acext.api.collection.doubletype;

import java.util.Comparator;

public interface DoubleComparator extends Comparator<Double> {
    int compare(double o1, double o2);

    default int compare(Double o1, Double o2) {
        return this.compare(o1 != null ? o1.doubleValue() : (double) 0, o2 != null ? o2.doubleValue() : (double) 0);
    }
}
