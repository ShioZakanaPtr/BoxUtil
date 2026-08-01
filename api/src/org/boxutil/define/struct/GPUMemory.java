package org.boxutil.define.struct;

import org.jetbrains.annotations.NotNull;

public interface GPUMemory<T> extends Comparable<GPUMemory<T>> {
    int reference();

    /**
     * The corresponding offset on GPU object memory, without reserved space of memory pool.
     */
    long address();

    /**
     * The corresponding length on GPU object memory.
     */
    long size();

    default T meta() {
        return null;
    }

    /**
     * The memory as <code>nullptr</code> when true.
     */
    default boolean is_free() {
        return this.reference() < 1;
    }

    default int compareTo(@NotNull GPUMemory<T> o) {
        return is_free() ? 1 : Long.compare(this.size(), o.size());
    }
}
