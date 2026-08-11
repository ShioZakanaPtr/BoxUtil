package org.boxutil.backends.core.statictrail;

import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.struct.memorypool.GPUPoolBehavior;
import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * <pre>
 * {@code
 * // 20 byte
 * struct Node {
 *     vec2 a_vertex;
 *     vec2f16 a_facingVector;
 *     float a_timeStamp; // life time and 3-bits id
 *     float a_distance;
 * };
 * }
 * </pre>
 *
 * Draw with {@link GL32#GL_LINE_STRIP_ADJACENCY} that have <code>n+1</code> nodes memory allocated for <code>n</code> nodes, and with 1 reversed node in pool start.<p>
 * Typically, nodes are recorded every 1/30 second, so at most 30 nodes per second.
 */
public final class BUtil_StaticTrailMemoryPool extends GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> {
    public final static byte NODE_BYTE_SIZE = 20;
    private final static byte MAX_COMBAT_LAYERS = (byte) CombatEngineLayers.values().length;
    private final static byte MAX_LAYERS = (byte) (MAX_COMBAT_LAYERS + CampaignEngineLayers.values().length);
    private final static ConcurrentMap<StaticTrailData, BUtil_StaticTrailMemoryPool> TRAIL = new ConcurrentHashMap<>(64);
    private final static AtomicInteger POOL_BARRIER = new AtomicInteger(0);
    private final static AtomicInteger[] LAYER_TRAIL_COUNT = new AtomicInteger[MAX_LAYERS];
    private final static Deque<Consumer<Void>> DELAY_ADD_TRAIL = new ConcurrentLinkedDeque<>();
    private final static Set<StaticTrailData> SUBMIT_TRAIL_CHANGES = ConcurrentHashMap.newKeySet(64);
    private final static Deque<Object> CUT_TRAIL_PROCESS = new ConcurrentLinkedDeque<>();
    private final static ConcurrentMap<Object, Set<BUtil_StaticTrailMemory>> TRAIL_LINKED_ENTITY = new ConcurrentHashMap<>(64);
    private final static GPUPoolBehavior<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> BEHAVIOR;
    private static boolean _INVALID = true;

    static {
        BEHAVIOR = new GPUPoolBehavior<>(GL15.GL_ARRAY_BUFFER, BUtil_StaticTrailMemoryPool::makeEmptyMem, BUtil_StaticTrailMemoryPool::makeNotEmptyMem);
        BEHAVIOR.persistentMappingAllow = true;
        BEHAVIOR.reservedSize = NODE_BYTE_SIZE;
        BEHAVIOR.glContextRequirements = BUtil_StaticTrailMemoryPool::poolReq;
        BEHAVIOR.glRebindBuffer = BUtil_StaticTrailMemoryPool::poolRebind;
        BEHAVIOR.glPoolDestroy = BUtil_StaticTrailMemoryPool::poolDestroy;
    }

    private boolean rebindReq = false;
    private final int vao;
    private final int maxFullNodes;
    private final float maxDur;
    private final long allocSize;
    private final DrawPi[] drawPi = new DrawPi[MAX_LAYERS];
    private final FloatBuffer statePackageMem = BufferUtils.createFloatBuffer(40).position(0).limit(40);
    private final StaticTrailData trailData;
    private IntBuffer legacyUploadBuf = null;

    private static final class DrawPi {
        private final AtomicInteger writePtr = new AtomicInteger(0);
        private IntBuffer piFirst = null;
        private IntBuffer piCount = null;

        private boolean checkExpend(int reqSize) {
            return this.piFirst == null || this.piFirst.capacity() < reqSize;
        }

        private void putDrawPi(final BUtil_StaticTrailMemoryPool pool, int piFirst, int piCount) {
            final int reqSize = this.writePtr.incrementAndGet(); // ptr + 1 = size

            if (this.checkExpend(reqSize)) {
                pool.getClientLock().lock();
                if (this.checkExpend(reqSize)) {
                    final int size = CalculateUtil.getPOTMax(reqSize);

                    IntBuffer tmpBuf = BufferUtils.createIntBuffer(size).position(0);
                    if (this.piFirst != null) tmpBuf.put(this.piFirst);
                    tmpBuf.position(0).limit(size);
                    this.piFirst = tmpBuf;

                    tmpBuf = BufferUtils.createIntBuffer(size).position(0);
                    if (this.piCount != null) tmpBuf.put(this.piCount);
                    tmpBuf.position(0).limit(size);
                    this.piCount = tmpBuf;
                }
                pool.getClientLock().unlock();
            }

            final int writeP = reqSize - 1; // size - 1 = ptr
            this.piFirst.put(writeP, piFirst);
            this.piCount.put(writeP, piCount);
        }

        private void finishVertexPtr() {
            final int drawNum = this.writePtr.get(); // when have valid node: ptr++ => size
            if (drawNum < 1) return;
            this.piFirst.position(0).limit(drawNum);
            this.piCount.position(0).limit(drawNum);
            this.writePtr.set(0);
        }
    }

    public BUtil_StaticTrailMemoryPool(final GPUPoolBehavior<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> behavior, final StaticTrailData trailData, float maxDur) {
        super(behavior);
        this.vao = GL30.glGenVertexArrays();
        this.maxDur = maxDur;
        this.maxFullNodes = (int) Math.ceil(this.maxDur * BoxConfigs.getMaxTrailSystemNodePerSeconds()) + 2; // one for fill, another for loop draw
        this.allocSize = (long) this.maxFullNodes * NODE_BYTE_SIZE;
        this.trailData = trailData;
    }

    private static boolean poolReq(GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> ignore) {
        return BoxDatabase.getGLState().BOXUTIL_VALID && BoxConfigs.isBackgroundThreadGLValid() && BoxConfigs.isTrailSystemEnable();
    }

    private static void poolRebind(GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        final var poolCast = (BUtil_StaticTrailMemoryPool) pool;
        poolCast.rebindReq = true;

        final boolean notPersistentMapping = !pool.isPersistentMapping() || pool.getMappingBuffer() == null;
        if (notPersistentMapping && poolCast.legacyUploadBuf == null) poolCast.legacyUploadBuf = BufferUtils.createIntBuffer((int) (poolCast.allocSize >>> 2) - 5);
    }

    private void checkVAORebind() {
        if (this.rebindReq) {
            this.rebindReq = false;
            final var target = this.getPoolBehavior().glTarget;
            GL30.glBindVertexArray(this.vao);
            GL15.glBindBuffer(target, this.glID);

            GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, NODE_BYTE_SIZE, 0);
            GL20.glEnableVertexAttribArray(0);
            GL20.glVertexAttribPointer(1, 2, GL30.GL_HALF_FLOAT, false, NODE_BYTE_SIZE, 8);
            GL20.glEnableVertexAttribArray(1);
            GL30.glVertexAttribIPointer(2, 1, GL11.GL_UNSIGNED_INT, NODE_BYTE_SIZE, 12);
            GL20.glEnableVertexAttribArray(2);
            GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, NODE_BYTE_SIZE, 16);
            GL20.glEnableVertexAttribArray(3);

            GL30.glBindVertexArray(0);
            GL15.glBindBuffer(target, 0);
        }
    }

    private static void poolDestroy(GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        final var poolCast = (BUtil_StaticTrailMemoryPool) pool;
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(poolCast.vao);
    }

    private static BUtil_StaticTrailMemory makeEmptyMem(BUtil_StaticTrailTrackerObject meta, long address, long size, int index, GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        return new BUtil_StaticTrailMemory(meta, address, size, index, true);
    }

    private static BUtil_StaticTrailMemory makeNotEmptyMem(BUtil_StaticTrailTrackerObject meta, long address, long size, int index, GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        return new BUtil_StaticTrailMemory(meta, address, size, index, false);
    }

    // ====================================
    // =============== impl ===============
    // ====================================

    public static void initPool() {
        if (!poolReq(null)) return;
        final short nodes = BoxConfigs.getMaxTrailSystemNodePerSeconds();
        BEHAVIOR.defaultBufferSize = 1024 * NODE_BYTE_SIZE * nodes;
        _INVALID = false;
    }

    private static BUtil_StaticTrailMemoryPool setupTrail(final StaticTrailData target) {
        final float maxDur = Math.max(target.durFadeIn, 0.0f) + Math.max(target.durFull, 0.0f) + Math.max(target.durFadeOut, 0.0f);
        if (maxDur < StaticTrailData.MINIMUM_TOTAL_DURATION) return null;

        return TRAIL.computeIfAbsent(target, (targetData) -> {
            final var pool = new BUtil_StaticTrailMemoryPool(BEHAVIOR, targetData, maxDur);
            pool.init();
            return pool.isInvalid() ? null : pool;
        });
    }

    public static byte toLayerLoc(final CombatEngineLayers layer) {
        return (byte) layer.ordinal();
    }

    public static byte toLayerLoc(final CampaignEngineLayers layer) {
        return (byte) (layer.ordinal() + MAX_COMBAT_LAYERS);
    }

    public static boolean addTracker(@NotNull final StaticTrailData trailData, byte layerLoc, @NotNull final StaticTrailTracker tacker) {
        final var pool = setupTrail(trailData);
        if (pool == null || pool.isInvalid()) return false;

        DELAY_ADD_TRAIL.offer((ignore) -> { // at single thread only
            if (pool.isInvalid()) return;
            pool.malloc(new BUtil_StaticTrailTrackerObject(layerLoc, tacker, new BUtil_StaticTrailCallback()), pool.allocSize);
            if (pool.drawPi[layerLoc] == null) pool.drawPi[layerLoc] = new DrawPi();
            if (LAYER_TRAIL_COUNT[layerLoc] == null) LAYER_TRAIL_COUNT[layerLoc] = new AtomicInteger(0);
            submitTrailDataChanges(trailData);
            LAYER_TRAIL_COUNT[layerLoc].incrementAndGet();
        });
        return true;
    }

    public static void delayAddTracker(boolean auxThread) {
        if (auxThread) return;
        Consumer<Void> adder;
        while ((adder = DELAY_ADD_TRAIL.poll()) != null) {
            adder.accept(null);
        }
    }

    private static boolean bypassTrail(byte layerLoc, boolean inCampaign) {
        if (layerLoc < MAX_COMBAT_LAYERS) {
            return inCampaign; // do campaign only
        } else return !inCampaign; // do combat only
    }

    public static void computeTrailNode(float amount, float elapsedTime, boolean inCampaign, boolean auxThread) {
        if (BUtil_GLImpl.Operations.waitStaticTrailCompute()) return;

        for (var pool : TRAIL.values()) {
            if (pool == null || pool.isInvalid() || pool.getBufferReference() < 1) continue;

            final boolean notPersistentMapping = !pool.isPersistentMapping() || pool.getMappingBuffer() == null;
            final IntBuffer writeBuffer = notPersistentMapping ? pool.legacyUploadBuf : pool.getMappingBuffer().asIntBuffer();

            byte layerLoc;
            final int memSize = pool.mem.size(), memMidIndex = memSize / 2, memStart = auxThread ? memMidIndex : 0, memLimit = auxThread ? memSize : memMidIndex;
            int computeNode;
            BUtil_StaticTrailMemory trailMemory;
            final List<BUtil_StaticTrailMemory> toFreeList = new ArrayList<>(memSize);

            POOL_BARRIER.incrementAndGet();
            for (int i = memStart; i < memLimit; i++) {
                trailMemory = pool.mem.get(i);
                if (trailMemory.is_free()) continue;
                layerLoc = trailMemory.meta().layerLoc();
                if (bypassTrail(layerLoc, inCampaign)) continue;

                computeNode = trailMemory.computeData(pool, writeBuffer, notPersistentMapping, amount, elapsedTime);
                if (computeNode < 1) {
                    if (computeNode < 0) toFreeList.add(trailMemory);
                    continue;
                }

                pool.drawPi[layerLoc].putDrawPi(pool, trailMemory.getDrawAddress(), computeNode + 2); // curr-fill + pre-fill
            }
            POOL_BARRIER.decrementAndGet();

            while (POOL_BARRIER.get() > 0) {
                Thread.onSpinWait();
            }

            if (!auxThread) {
                final byte i_start, i_limit;
                if (inCampaign) {
                    i_start = MAX_COMBAT_LAYERS;
                    i_limit = MAX_LAYERS;
                } else {
                    i_start = 0;
                    i_limit = MAX_COMBAT_LAYERS;
                }
                for (byte i = i_start; i < i_limit; i++) {
                    if (pool.drawPi[i] != null) {
                        pool.drawPi[i].finishVertexPtr();
                    }
                }
            }

            for (var trailMem : toFreeList) {
                pool.free(trailMem);
                LAYER_TRAIL_COUNT[trailMem.meta().layerLoc()].decrementAndGet();
            }
        }
    }

    public static void submitTrailDataChanges(final StaticTrailData trailData) {
        SUBMIT_TRAIL_CHANGES.add(trailData);
    }

    private void glDrawTrail(final BaseShaderData program, int layerLoc, int layerBits) {
        if (this.drawPi[layerLoc] == null || this.drawPi[layerLoc].piFirst == null || this.drawPi[layerLoc].piFirst.limit() < 1) return;
        final var material = this.trailData.material;
        int dataBit = layerBits;

        if (SUBMIT_TRAIL_CHANGES.remove(this.trailData)) this.updateStateUniforms(this.trailData);
        if (material.isIgnoreIllumination()) dataBit |= 0b10;
        if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
        GL20.glUniform4(program.location[0], this.statePackageMem);
        GL30.glUniform1ui(program.location[2], material.isAdditionEmissive() ? 1 : 0);
        GL30.glUniform1ui(program.location[3], dataBit);

        this.trailData.material.putShaderTexture();

        BUtil_GLImpl.Operations.cullCheck(BoxEnum.MATERIAL_CULL_DISABLED);
        BUtil_GLImpl.Operations.matrixCheck(BoxEnum.ENTITY_VANILLA_PRIME_MATRIX, null);
        BUtil_GLImpl.Operations.blendCheck(this.trailData.additiveBlend ? BoxEnum.ENTITY_ADDITIVE_BLEND : BoxEnum.ENTITY_NORMAL_BLEND, 0, 0, 0, 0, 0);

        this.checkVAORebind();
        GL30.glBindVertexArray(this.vao);
        GL14.glMultiDrawArrays(GL32.GL_LINE_STRIP_ADJACENCY, this.drawPi[layerLoc].piFirst, this.drawPi[layerLoc].piCount);
    }

    public static boolean bypassDrawTrail(int layerLoc) {
        return LAYER_TRAIL_COUNT[layerLoc] == null || LAYER_TRAIL_COUNT[layerLoc].get() < 1;
    }

    public static void drawEachTrail(final BaseShaderData program, int layerLoc, int layerBits) {
        for (var pool : TRAIL.values()) {
            if (pool == null || pool.isInvalid() || pool.getBufferReference() < 1) continue;
            pool.glDrawTrail(program, layerLoc, layerBits);
        }
    }

    public static void offerCutTrail(final Object linkedEntity) {
        CUT_TRAIL_PROCESS.offer(linkedEntity);
    }

    public static void processCurTrail() {
        if (true) return;
        Object linkedEntity;
        Set<BUtil_StaticTrailMemory> trailSet;
        while ((linkedEntity = CUT_TRAIL_PROCESS.poll()) != null) {
            trailSet = TRAIL_LINKED_ENTITY.get(linkedEntity);
            if (trailSet == null || trailSet.isEmpty()) continue;

            for (BUtil_StaticTrailMemory trailMemory : trailSet) {
                if (trailMemory.is_free()) continue;
//                trailMemory.cutTrail();
            }
        }
    }

    public static void cleanupPool(boolean cleanupCombat) {
        Map.Entry<StaticTrailData, BUtil_StaticTrailMemoryPool> entry;
        BUtil_StaticTrailMemoryPool pool;
        for (final var iterator = TRAIL.entrySet().iterator(); iterator.hasNext();) {
            entry = iterator.next();
            pool = entry.getValue();
            if (pool == null || pool.isInvalid() || pool.getBufferReference() < 1) {
                iterator.remove();
                continue;
            }

            final List<BUtil_StaticTrailMemory> toFreeList = new ArrayList<>(pool.mem.size());
            for (var trailMemory : pool.mem) {
                if (trailMemory.is_free()) continue;
                if (bypassTrail(trailMemory.meta().layerLoc(), cleanupCombat)) toFreeList.add(trailMemory);
            }
            for (var trailMemory : toFreeList) {
                LAYER_TRAIL_COUNT[trailMemory.meta().layerLoc()].decrementAndGet();
                pool.free(trailMemory);
            }

            if (pool.getBufferReference() < 1) {
                pool.destroy();
                iterator.remove();
            }
        }
    }

    public static boolean removeTracker(@NotNull final StaticTrailData trailData) {
        final var pool = TRAIL.remove(trailData);
        if (pool == null) return false;
        for (var trailMemory : pool.mem) {
            if (trailMemory.is_free()) continue;
            LAYER_TRAIL_COUNT[trailMemory.meta().layerLoc()].decrementAndGet();
            pool.free(trailMemory);
        }
        pool.destroy();
        return true;
    }

    // ===================================
    // =============== com ===============
    // ===================================

    public static BUtil_StaticTrailMemoryPool getPool(final StaticTrailData target) {
        return TRAIL.get(target);
    }

    public static boolean isNotSupported() {
        return _INVALID;
    }

    public static int getTrailTypes() {
        return TRAIL.size();
    }

    public static long getTotalAllocatedMemory() {
        return TRAIL.values().stream().filter(Objects::nonNull).mapToLong(BUtil_StaticTrailMemoryPool::getBufferTotal).sum();
    }

    public static long getTotalSpace() {
        return TRAIL.values().stream().filter(Objects::nonNull).mapToLong(BUtil_StaticTrailMemoryPool::getBufferSpace).sum();
    }

    public int getMaxFullNodes() {
        return this.maxFullNodes;
    }

    public float getMaxDur() {
        return this.maxDur;
    }

    public StaticTrailData getTrailData() {
        return this.trailData;
    }

    public boolean realloc(BUtil_StaticTrailMemory memory, long newSize) {
        throw new UnsupportedOperationException("Pool method realloc() is not supported on static trail memory.");
    }

    public BUtil_StaticTrailMemory split(BUtil_StaticTrailMemory memory, long newSize, boolean fromStartOrEnd) {
        throw new UnsupportedOperationException("Pool method split() is not supported on static trail memory.");
    }

    private void tryVec4(int index, final Vector4f vec, float defX, float defY, float defZ, float defW) {
        final boolean putDef = vec == null;
        this.statePackageMem.put(index, putDef ? defX : vec.x);
        this.statePackageMem.put(index + 1, putDef ? defY : vec.y);
        this.statePackageMem.put(index + 2, putDef ? defZ : vec.z);
        this.statePackageMem.put(index + 3, putDef ? defW : vec.w);
    }

    private void tryVec2(int index, final Vector2f vec, float defX, float defY) {
        final boolean putDef = vec == null;
        this.statePackageMem.put(index, putDef ? defX : vec.x);
        this.statePackageMem.put(index + 1, putDef ? defY : vec.y);
    }

    private void updateStateUniforms(final StaticTrailData data) {
        this.statePackageMem.put(0, data.material.getState(), 0, 12);
        tryVec4(12, data.colorIn, 1.0f, 1.0f, 1.0f, 1.0f);
        tryVec4(16, data.colorOut, 1.0f, 1.0f, 1.0f, 1.0f);
        this.statePackageMem.put(20, data.durFadeIn);
        this.statePackageMem.put(21, data.durFull);
        this.statePackageMem.put(22, data.durFadeOut);
        this.statePackageMem.put(23, data.randomUVStartOffset ? 1.0f : 0.0f);
        this.statePackageMem.put(24, data.sizeIn);
        this.statePackageMem.put(25, data.sizeOut);
        this.statePackageMem.put(26, 1.0f / data.texturePixels);
        this.statePackageMem.put(27, data.textureSpeed);
        tryVec4(28, data.velocityInRange, 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec4(32, data.velocityOutRange, 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec2(36, data.angularInRange, 0.0f, 0.0f);
        tryVec2(38, data.angularOutRange, 0.0f, 0.0f);
    }
}
