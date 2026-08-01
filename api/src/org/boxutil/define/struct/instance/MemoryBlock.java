package org.boxutil.define.struct.instance;

import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.GPUMemory;

/**
 * Maybe renaming at next Starsector update.
 */
public interface MemoryBlock extends GPUMemory<InstanceType> {
    @Deprecated
    InstanceType type();

    InstanceType meta();

    /**
     * The corresponding offset on SSBO memory as instance data.
     */
    int address_instance();

    /**
     * As <code>size() / sizeof(instance_type)</code>
     */
    int instance_count();

    boolean is_type_2D();

    boolean is_type_fixed();
}
