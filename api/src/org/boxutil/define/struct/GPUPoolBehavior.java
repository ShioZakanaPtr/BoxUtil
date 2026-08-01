package org.boxutil.define.struct;

import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class GPUPoolBehavior<T extends GPUMemoryPool.InternalMemory<D>, D> {
    /**
     * <code>false</code> when not create an immutable buffer even if supported.
     */
    public boolean immutableBufferAllow = true;
    /**
     * <code>false</code> when not create as a client persistent mapping buffer even if supported, or invalid if {@link GPUPoolBehavior#immutableBufferAllow} is <code>false</code>.
     */
    public boolean persistentMappingAllow = false;
    public final int glTarget;
    /**
     * A nonnegative integer.<p>
     * The reserved size from the start of buffer in <code>byte</code>, this memory is not part of the pool's accessible memory.
     */
    public int reservedSize = 0;
    /**
     * A nonnegative integer.<p>
     * The initialized size of buffer in <code>byte</code>.
     */
    public long defaultBufferSize = 65536;
    /**
     * A nonnegative integer.<p>
     * Limit value that the buffer object has an explicit size limit defined by the OpenGL specification in <code>byte</code>, otherwise set to <code>0</code>.<p>
     * When a new buffer object will create with unsupported size, an {@link OutOfMemoryError} will be thrown.
     */
    public long maxBufferSize = 0;
    /**
     * The creation conditions of the memory pool, will check it when pool initializations.<p>
     * Returns <code>false</code> when not supported on current device, otherwise <code>true</code>.<p>
     * <code>null</code> when <strong>OpenGL 1.5</strong> required only.
     */
    public Function<? super GPUMemoryPool<T, D>, Boolean> glContextRequirements = null;
    /**
     * After a new buffer has created, maybe you want to bind it to any location in shader, without {@link GL15#glBindBuffer(int, int)} calls.<p>
     * <code>null</code> when without rebind behavior.
     */
    public Consumer<? super GPUMemoryPool<T, D>> glRebindBuffer = null;
    /**
     * The memory allocation behavior when malloc is first called after initialization.<p>
     * The first <code>Long</code> parameter is the requested size for that {@linkplain GPUMemoryPool#malloc(Object, long) malloc} call, and this function should return a value no less than that parameter.
     */
    @NotNull
    public BiFunction<Long, ? super GPUMemoryPool<T, D>, Long> bufferInitRule = (req, pool) -> req < this.defaultBufferSize ? this.defaultBufferSize : CalculateUtil.getPOTMax(req << 1);
    /**
     * The behavior of re‑creating a larger‑capacity Buffer object when the current memory space is insufficient.<p>
     * The first <code>Long</code> parameter is the size of the existing buffer object before expansion, and this function should return a value no less than that parameter.
     */
    @NotNull
    public BiFunction<Long, ? super GPUMemoryPool<T, D>, Long> bufferExpendRule = (old, pool) -> old << 1;

    public final GPUMemoryPool.MemoryBuilder<T, D> newEmptyMemory;
    public final GPUMemoryPool.MemoryBuilder<T, D> newNotEmptyMemory;

    /**
     * @param glTarget          the type of buffer, for example: {@link GL15#GL_ARRAY_BUFFER}, {@link GL31#GL_TEXTURE_BUFFER}, {@link GL31#GL_UNIFORM_BUFFER} etc.
     * @param newEmptyMemory    for example: <pre>{@code (meta, address, size, index, pool) -> new InternalMemory<>(meta, address, size, index, true)}</pre>
     * @param newNotEmptyMemory for example: <pre>{@code (meta, address, size, index, pool) -> new InternalMemory<>(meta, address, size, index, false)}</pre>
     */
    public GPUPoolBehavior(int glTarget, @NotNull final GPUMemoryPool.MemoryBuilder<T, D> newEmptyMemory, @NotNull final GPUMemoryPool.MemoryBuilder<T, D> newNotEmptyMemory) {
        this.glTarget = glTarget;
        this.newEmptyMemory = newEmptyMemory;
        this.newNotEmptyMemory = newNotEmptyMemory;
    }
}
