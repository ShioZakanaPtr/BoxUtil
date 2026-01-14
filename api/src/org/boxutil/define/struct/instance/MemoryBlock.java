package org.boxutil.define.struct.instance;

import org.boxutil.define.InstanceType;

public interface MemoryBlock {
    InstanceType type();

    int reference();

    /**
     * The corresponding offset on SSBO memory.
     */
    long address();

    /**
     * The corresponding offset on SSBO memory as instance data.
     */
    int address_instance();

    /**
     * The corresponding length on SSBO memory.
     */
    long size();

    /**
     * As <code>size() / sizeof(instance_type)</code>
     */
    int instance_count();

    /**
     * The memory as <code>nullptr</code> when true.
     */
    boolean is_free();

    boolean is_type_2D();

    boolean is_type_fixed();
}
