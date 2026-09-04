package org.boxutil.define.struct.memorypool;

import org.boxutil.define.GLWrapper;
import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL44;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class GPUPoolBehavior<T extends GPUMemoryPool.InternalMemory<D>, D> {
    public boolean immutableBufferAllow = true;
    public boolean useDynamicStorageBuffer = true;
    public boolean persistentMappingAllow = false;
    public final int glTarget;
    public int glBufferAccessBits = GLWrapper.Buffer.GL_MAP_WRITE_BIT;
    public int reservedSize = 0;
    public long defaultBufferSize = 65536;
    public long maxBufferSize = 0;
    public Function<? super GPUMemoryPool<T, D>, Boolean> glContextRequirements = null;
    public Consumer<? super GPUMemoryPool<T, D>> glRebindBuffer = null;
    public Consumer<? super GPUMemoryPool<T, D>> glPoolInit = null;
    public Consumer<? super GPUMemoryPool<T, D>> glPoolDestroy = null;
    public @NotNull BiFunction<Long, ? super GPUMemoryPool<T, D>, Long> bufferInitRule = (req, pool) -> req < this.defaultBufferSize ? this.defaultBufferSize : CalculateUtil.getPOTMax(req << 1);
    public @NotNull BiFunction<Long, ? super GPUMemoryPool<T, D>, Long> bufferExpendRule = (old, pool) -> old << 1;
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

    /**
     * Shallow copy constructor.<p>
     * <b>Do not forget to check if any functional interface have reference of src field.</b>
     */
    public GPUPoolBehavior(final GPUPoolBehavior<T, D> src) {
        this.useDynamicStorageBuffer = src.useDynamicStorageBuffer;
        this.immutableBufferAllow = src.immutableBufferAllow;
        this.persistentMappingAllow = src.persistentMappingAllow;
        this.glTarget = src.glTarget;
        this.glBufferAccessBits = src.glBufferAccessBits;
        this.reservedSize = src.reservedSize;
        this.defaultBufferSize = src.defaultBufferSize;
        this.maxBufferSize = src.maxBufferSize;
        this.glContextRequirements = src.glContextRequirements;
        this.glRebindBuffer = src.glRebindBuffer;
        this.glPoolInit = src.glPoolInit;
        this.glPoolDestroy = src.glPoolDestroy;
        this.bufferInitRule = src.bufferInitRule;
        this.bufferExpendRule = src.bufferExpendRule;
        this.newEmptyMemory = src.newEmptyMemory;
        this.newNotEmptyMemory = src.newNotEmptyMemory;
    }

    /**
     * <code>false</code> when not create an immutable buffer even if supported.
     * For default value was <code>true</code>.<p>
     */
    public GPUPoolBehavior<T, D> setImmutableBufferAllow(boolean immutableBufferAllow) {
        this.immutableBufferAllow = immutableBufferAllow;
        return this;
    }

    /**
     * Only effective when {@link GPUPoolBehavior#immutableBufferAllow} was <code>true</code>.<p>
     * The memory pool may be updated after creation through calls to {@link GL15#glBufferSubData} when set to <code>true</code>.<p>
     * For default value was <code>true</code>.
     */
    public GPUPoolBehavior<T, D> setUseDynamicStorageBuffer(boolean useDynamicStorageBuffer) {
        this.useDynamicStorageBuffer = useDynamicStorageBuffer;
        return this;
    }

    /**
     * Only effective when {@link GPUPoolBehavior#immutableBufferAllow} was <code>true</code>, and related OpenGL features was supported.<p>
     * It must be a bitwise combination of a subset of the following flags:<p>
     * {@link GL30#GL_MAP_READ_BIT}, {@link GL30#GL_MAP_WRITE_BIT}, {@link GL44#GL_MAP_PERSISTENT_BIT},
     * {@link GL44#GL_MAP_COHERENT_BIT}, {@link GL44#GL_CLIENT_STORAGE_BIT}, otherwise set to <code>0</code>.<p>
     * If access contains {@link GL44#GL_MAP_PERSISTENT_BIT}, must contain at least one of {@link GL30#GL_MAP_READ_BIT} or {@link GL30#GL_MAP_WRITE_BIT}.<p>
     * If access contains {@link GL44#GL_MAP_COHERENT_BIT}, it must also contain {@link GL44#GL_MAP_PERSISTENT_BIT},
     * and you should be use barrier and/or sync to ensure client/server can see the read/write commands at after.<p>
     * For default value was <code>GL_MAP_WRITE_BIT</code>.
     */
    public GPUPoolBehavior<T, D> setBufferAccessBits(int access) {
        this.glBufferAccessBits = access;
        return this;
    }

    /**
     * A nonnegative integer.<p>
     * The reserved size from the start of buffer in <code>byte</code>, this memory is not part of the pool's accessible memory.
     */
    public GPUPoolBehavior<T, D> setReservedSize(int reservedSize) {
        this.reservedSize = reservedSize;
        return this;
    }

    /**
     * A nonnegative integer.<p>
     * The initialized size of buffer in <code>byte</code>.
     */
    public GPUPoolBehavior<T, D> setDefaultBufferSize(long defaultBufferSize) {
        this.defaultBufferSize = defaultBufferSize;
        return this;
    }

    /**
     * A nonnegative integer.<p>
     * Limit value that the buffer object has an explicit size limit defined by the OpenGL specification in <code>byte</code>, otherwise set to <code>0</code>.<p>
     * When a new buffer object will create with unsupported size, an {@link OutOfMemoryError} will be thrown.
     */
    public GPUPoolBehavior<T, D> setMaxBufferSize(long maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
        return this;
    }

    /**
     * The creation conditions of the memory pool, will check it when pool initializations.<p>
     * Returns <code>false</code> when not supported on current device, otherwise <code>true</code>.<p>
     * <code>null</code> when <strong>OpenGL 1.5</strong> required only.
     */
    public GPUPoolBehavior<T, D> setContextRequirements(Function<? super GPUMemoryPool<T, D>, Boolean> glContextRequirements) {
        this.glContextRequirements = glContextRequirements;
        return this;
    }

    /**
     * After a new buffer has created, maybe you want to bind it to any location in shader; without {@link GL15#glBindBuffer(int, int)} before or after this calls,
     * so you should be bind and unbind your buffer in method.<p>
     * <b>NOTE:</b> For objects that are <b>private</b> to the OpenGL context – such as a VBO thread pool with an associated VAO –
     * the reBind operation should dispatch a signal (or closure) to the actual thread that requires it,
     * thereby deferring VAO configuration to be performed on that thread.<p>
     * <code>null</code> when without rebind behavior.
     */
    public GPUPoolBehavior<T, D> setRebindBuffer(Consumer<? super GPUMemoryPool<T, D>> glRebindBuffer) {
        this.glRebindBuffer = glRebindBuffer;
        return this;
    }

    /**
     * At end of the buffer object {@link GPUMemoryPool#init()}.<p>
     * <code>null</code> when without after-init behavior.
     */
    public GPUPoolBehavior<T, D> setPoolInit(Consumer<? super GPUMemoryPool<T, D>> glPoolInit) {
        this.glPoolInit = glPoolInit;
        return this;
    }

    /**
     * At end of the buffer object {@link GPUMemoryPool#destroy()}, for delete some vao/texture object.<p>
     * <code>null</code> when without after-destroy behavior.
     */
    public GPUPoolBehavior<T, D> setPoolDestroy(Consumer<? super GPUMemoryPool<T, D>> glPoolDestroy) {
        this.glPoolDestroy = glPoolDestroy;
        return this;
    }

    /**
     * The memory allocation behavior when malloc is first called after initialization.<p>
     * The first <code>Long</code> parameter is the requested size for that {@linkplain GPUMemoryPool#malloc(Object, long) malloc} call,
     * and this function should return a value no less than that parameter.
     */
    public GPUPoolBehavior<T, D> setBufferInitRule(@NotNull BiFunction<Long, ? super GPUMemoryPool<T, D>, Long> bufferInitRule) {
        this.bufferInitRule = bufferInitRule;
        return this;
    }

    /**
     * The behavior of re‑creating a larger‑capacity Buffer object when the current memory space is insufficient.<p>
     * The first <code>Long</code> parameter is the size of the existing buffer object before expansion, and this function should return a value no less than that parameter.
     */
    public GPUPoolBehavior<T, D> setBufferExpendRule(@NotNull BiFunction<Long, ? super GPUMemoryPool<T, D>, Long> bufferExpendRule) {
        this.bufferExpendRule = bufferExpendRule;
        return this;
    }
}
