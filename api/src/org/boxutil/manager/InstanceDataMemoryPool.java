package org.boxutil.manager;

import org.boxutil.backends.core.instancedrendering.BUtil_InstanceMemory;
import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.backends.core.instancedrendering.BUtil_InstanceDataMemoryPool;
import de.unkrig.commons.nullanalysis.NotNull;

/**
 * Used for {@link InstanceRenderAPI}.
 */
@SuppressWarnings("UnusedReturnValue")
public final class InstanceDataMemoryPool {
    public static boolean isNotSupported() {
        return BUtil_InstanceDataMemoryPool.isPoolInvalid();
    }

    /**
     * @return the buffer object name of SSBO, <code>0</code> if never have call {@linkplain InstanceDataMemoryPool#malloc(InstanceType, int)  malloc} with target, or pool was unsupported.<p>
     *     Without mutex.
     */
    public static int getBufferID(@NotNull final InstanceType target) {
        return BUtil_InstanceDataMemoryPool.getPool(target).getBufferID();
    }

    /**
     * @param count instance data count.
     *
     * @return <code>null</code> when allocation failed.
     */
    public static MemoryBlock malloc(@NotNull final InstanceType target, int count) {
        return BUtil_InstanceDataMemoryPool.getPool(target).malloc(target, (long) target.getSize() * count);
    }

    /**
     * Bad for performance, so recommend to allocation the large enough memory block before all.
     *
     * @param memory different from C/C++, will not change the object pointer, only remapping and then copy data if needed.
     * @param newCount instance data count.
     *
     * @return <code>false</code> when re-allocation failed.
     */
    public static boolean realloc(@NotNull final MemoryBlock memory, int newCount) {
        if (!(memory instanceof BUtil_InstanceMemory memoryReal)) throw new IllegalArgumentException("Unmatched memory target.");
        final var target = memoryReal.meta();
        return BUtil_InstanceDataMemoryPool.getPool(target).realloc(memoryReal, (long) target.getSize() * newCount);
    }

    public static MemoryBlock split(@NotNull final MemoryBlock memory, int newCount, boolean fromStartOrEnd) {
        if (!(memory instanceof BUtil_InstanceMemory memoryReal)) throw new IllegalArgumentException("Unmatched memory target.");
        final var target = memoryReal.meta();
        return BUtil_InstanceDataMemoryPool.getPool(target).split(memoryReal, (long) target.getSize() * newCount, fromStartOrEnd);
    }

    /**
     * Even if memory was free, the operation is safe.
     *
     * @return <code>false</code> when still have reference after call.
     */
    public static boolean free(@NotNull final MemoryBlock memory) {
        if (!(memory instanceof BUtil_InstanceMemory memoryReal)) throw new IllegalArgumentException("Unmatched memory target.");
        return BUtil_InstanceDataMemoryPool.getPool(memoryReal.meta()).free(memoryReal);
    }

    public static MemoryBlock share(@NotNull final MemoryBlock memory) {
        if (!(memory instanceof BUtil_InstanceMemory memoryReal)) throw new IllegalArgumentException("Unmatched memory target.");
        return BUtil_InstanceDataMemoryPool.getPool(memoryReal.meta()).share(memoryReal);
    }

    private InstanceDataMemoryPool() {}
}
