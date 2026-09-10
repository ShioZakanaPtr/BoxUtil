package org.boxutil.util.container;

import java.util.HashSet;
import java.util.Set;

/**
 * If any element is <code>null</code>, it should be treated as <code>0</code> or <code>Integer(0)</code>.<p>
 * For store float value, just using {@link Float#floatToRawIntBits(float)} and {@link Float#intBitsToFloat(int)} directly.
 */
public interface IntSet extends Set<Integer> {
    boolean addNumber(int e);

    default boolean add(Integer e) {
        return this.addNumber(e == null ? 0 : e);
    }

    boolean containsNumber(int o);

    default boolean contains(Object o) {
        if (o == null) return this.containsNumber(0);
        if (!(o instanceof Integer i)) throw new IllegalArgumentException("Not a Integer");
        return this.containsNumber(i);
    }

    boolean removeNumber(int o);

    default boolean remove(Object o) {
        if (o == null) return this.removeNumber(0);
        if (!(o instanceof Integer i)) throw new IllegalArgumentException("Not a Integer");
        return this.removeNumber(i);
    }
}
