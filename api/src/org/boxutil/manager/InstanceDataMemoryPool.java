package org.boxutil.manager;

import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.backends.core.BUtil_InstanceDataMemoryPool;
import de.unkrig.commons.nullanalysis.NotNull;

/**
 * Used for {@link InstanceRenderAPI}.<p>
 *
 * <strong>Any client thread should have valid shared OpenGL context of the main thread<p>
 * Memory priority, not for speed.<p>
 * DON'T catch <code>Throwable</code> and then ignored them, some fatal memory error will occur so must fix them in modding.</strong>
 */
@SuppressWarnings("UnusedReturnValue")
public final class InstanceDataMemoryPool {
    public static boolean isNotSupported() {
        return BUtil_InstanceDataMemoryPool.isNotSupported();
    }

    /**
     * @return the buffer object name of SSBO, <code>0</code> if never have call malloc with target.<p>
     *     without mutex.
     */
    public static int getBufferID(InstanceType target) {
        return BUtil_InstanceDataMemoryPool.getBufferID(target);
    }

    /**
     * @param count instance data count.
     *
     * @return <code>null</code> when allocation failed.
     */
    public static MemoryBlock malloc(@NotNull InstanceType target, int count) {
        return BUtil_InstanceDataMemoryPool.malloc(target, count);
    }

    /**
     * Bad for performance, so recommend to allocation the large enough memory block before all.
     *
     * @param memory different from C/C++, will not change the object pointer, only remapping and then copy data if needed.
     * @param newCount instance data count.
     *
     * @return <code>null</code> when re-allocation failed.
     */
    public static boolean realloc(@NotNull MemoryBlock memory, int newCount) {
        return BUtil_InstanceDataMemoryPool.realloc(memory, newCount);
    }

    public static MemoryBlock split(@NotNull MemoryBlock memory, int newCount, boolean fromStartOrEnd) {
        return BUtil_InstanceDataMemoryPool.split(memory, newCount, fromStartOrEnd);
    }

    /**
     * Even if memory was free, the operation is safe.
     *
     * @return <code>false</code> when still have reference after call.
     */
    public static boolean free(@NotNull MemoryBlock memory) {
        return BUtil_InstanceDataMemoryPool.free(memory);
    }

    public static MemoryBlock share(@NotNull MemoryBlock memory) {
        return BUtil_InstanceDataMemoryPool.share(memory);
    }

    private InstanceDataMemoryPool() {}
}
