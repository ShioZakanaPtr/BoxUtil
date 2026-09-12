package org.boxutil.backends.core.instancedrendering;

import com.fs.starfarer.api.Global;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.memorypool.GPUPoolBehavior;
import org.boxutil.manager.InstanceDataMemoryPool;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.GPUMemoryPool;
import org.lwjgl.opengl.*;

@SuppressWarnings("UnusedReturnValue")
public final class BUtil_InstanceDataMemoryPool extends GPUMemoryPool<BUtil_InstanceMemory, InstanceType> {
    private final static BUtil_InstanceDataMemoryPool[] POOL = new BUtil_InstanceDataMemoryPool[InstanceType.values().length];
    private static boolean INVALID = true;

    private final byte binding;
    private final InstanceType target;

    public BUtil_InstanceDataMemoryPool(final GPUPoolBehavior<BUtil_InstanceMemory, InstanceType> behavior, final InstanceType target, byte binding) {
        super(behavior);
        this.binding = (byte) (4 + binding);
        this.target = target;
    }

    private static boolean poolReq(GPUMemoryPool<BUtil_InstanceMemory, InstanceType> ignore) {
        return BoxDatabase.getGLState().BOXUTIL_VALID && BoxConfigs.isBackgroundThreadGLValid() &&
                BoxDatabase.getGLState().MAX_VERTEX_SHADER_STORAGE_BLOCKS > 7L && ShaderCore.isCoreProgramValid() &&
                GLWrapper.Operation.Sync.valid_Barrier();
    }

    private static void poolRebind(GPUMemoryPool<BUtil_InstanceMemory, InstanceType> pool) {
        final var poolCast = (BUtil_InstanceDataMemoryPool) pool;
        final var target = pool.getPoolBehavior().glTarget;
        GLWrapper.Buffer.SSBO.glBindBuffer(target, poolCast.glID);
        GLWrapper.Buffer.SSBO.glBindBufferBase(target, poolCast.binding, poolCast.glID);
        GLWrapper.Buffer.SSBO.glBindBuffer(target, 0);
    }

    private static BUtil_InstanceMemory makeEmptyMem(InstanceType meta, long address, long size, int index, GPUMemoryPool<BUtil_InstanceMemory, InstanceType> pool) {
        return new BUtil_InstanceMemory(meta, address, size, index, true);
    }

    private static BUtil_InstanceMemory makeNotEmptyMem(InstanceType meta, long address, long size, int index, GPUMemoryPool<BUtil_InstanceMemory, InstanceType> pool) {
        return new BUtil_InstanceMemory(meta, address, size, index, false);
    }

    public static void initPool() {
        final var poolSize = (byte) POOL.length;

        boolean check = false;
        final InstanceType[] target = InstanceType.values();
        for (byte i = 0; i < poolSize; i++) {
            final var behavior = new GPUPoolBehavior<>(GLWrapper.Buffer.SSBO.GL_SHADER_STORAGE_BUFFER, BUtil_InstanceDataMemoryPool::makeEmptyMem, BUtil_InstanceDataMemoryPool::makeNotEmptyMem)
                    .setBufferAccessBits(GLWrapper.Buffer.GL_MAP_READ_BIT | GLWrapper.Buffer.GL_MAP_WRITE_BIT | GLWrapper.Buffer.GL_MAP_PERSISTENT_BIT)
                    .setContextRequirements(BUtil_InstanceDataMemoryPool::poolReq)
                    .setRebindBuffer(BUtil_InstanceDataMemoryPool::poolRebind)
                    .setDefaultBufferSize(target[i].getSize() * 65536L);

            POOL[i] = new BUtil_InstanceDataMemoryPool(behavior, target[i], i);
            POOL[i].init();
            check |= POOL[i].isInvalid();
        }
        INVALID = check;
    }

    public static boolean isPoolInvalid() {
        return INVALID;
    }

    public static BUtil_InstanceDataMemoryPool getPool(InstanceType target) {
        return POOL[target.ordinal()];
    }

    public long compact() {
        final long timeNano = super.compact();
        if (timeNano > 0) {
            final double time = timeNano * 0.001d;
            final boolean toMS = time > 1000.0d;
            final String timeStr = String.format("%.3f", toMS ? time * 0.001d : time) + (toMS ? " ms'" : " us'");
            Global.getLogger(InstanceDataMemoryPool.class).info("'BoxUtil' [" + this.target.name() + "] instance memory pool compact elapsed: '" + timeStr);
        }
        return timeNano;
    }

    public void rebindBase() {
        this.getClientLock().lock();
        this.poolGPULock().lock();
        if (this.glID < 1 || this.memTotal < 1) {
            this.poolGPULock().unlock();
            this.getClientLock().unlock();
            return;
        }

        this.behavior.glRebindBuffer.accept(this);
        this.poolGPULock().unlock();
        this.getClientLock().unlock();
    }

    /**
     * Anchor at bottom-left, default size <code>1.0d * 1.0d</code>
     *
     * @param values {space, total}
     */
    public void glDrawMemoryUsage(final long[] values) {
        this.getClientLock().lock();
        if (this.glID < 1) {
            GL11.glColor4ub(BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ZERO, BoxEnum.ONE_COLOR);
            GL11.glRectf(0.0f, 0.0f, 1.0f, 1.0f);
            values[0] = values[1] = 0L;
        } else {
            final byte[] rgb = new byte[3];
            final double div = 1.0d / this.memTotal;
            int colorSeed;

            for (BUtil_InstanceMemory block : this.mem) {
                if (block == null) continue;
                if (block.is_free()) {
                    rgb[0] = rgb[1] = rgb[2] = BoxEnum.ZERO;
                } else {
                    colorSeed = Long.hashCode((block.address() + 1L) * block.size());
                    colorSeed ^= colorSeed >> 16;
                    colorSeed *= 0x85ebca6b;
                    colorSeed ^= colorSeed >> 13;
                    colorSeed *= 0xc2b2ae35;
                    colorSeed ^= colorSeed >> 16;
                    rgb[0] = (byte) (colorSeed >>> 16 & 0xff);
                    rgb[1] = (byte) (colorSeed >>> 8 & 0xff);
                    rgb[2] = (byte) (colorSeed & 0xff);
                }
                GL11.glColor4ub(rgb[0], rgb[1], rgb[2], BoxEnum.ONE_COLOR);
                GL11.glRectd(block.address() * div, 0.0d, (block.address() + block.size()) * div, 1.0d);
            }
            values[0] = this.memSpace;
            values[1] = this.memTotal;
        }
        this.getClientLock().unlock();
    }
}
