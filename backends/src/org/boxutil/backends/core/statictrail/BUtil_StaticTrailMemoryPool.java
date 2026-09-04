package org.boxutil.backends.core.statictrail;

import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.backends.util.BUtil_SpinBarrier;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.struct.memorypool.GPUPoolBehavior;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.GPUMemoryPool;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.boxutil.util.CalculateUtil;
import org.boxutil.util.concurrent.SpinLock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * <pre>
 * {@code
 * // 24 byte
 * struct Node {
 *     vec2 a_vertex;
 *     vec2f16 a_facingVector;
 *     vec4 a_nodeColor;
 *     uint a_timeStampRaw; // life time and 3-bits id
 *     uint a_uv; // uint for avoid symbol folding
 * };
 * }
 * </pre>
 *
 * Draw with {@link GLWrapper.Drawcall#GL_LINE_STRIP_ADJACENCY} that each trail have <code>n+3</code> nodes memory allocated for <code>n</code> nodes.<p>
 * Typically, nodes are recorded every 1/30 second, so at most 30 nodes per second.
 */
public final class BUtil_StaticTrailMemoryPool extends GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> {
    public final static byte NODE_BYTE_SIZE = 24;
    private final static byte MAX_COMBAT_LAYERS = (byte) CombatEngineLayers.values().length;
    private final static byte MAX_LAYERS = (byte) (MAX_COMBAT_LAYERS + CampaignEngineLayers.values().length);
    private final static OpResource RES = new OpResource();

    private final static class OpResource {
        private boolean valid = false;
        private boolean additiveBlend = true;
        private final ConcurrentMap<StaticTrailData, BUtil_StaticTrailMemoryPool> trailPoolMap = new ConcurrentHashMap<>(64);
        private final Deque<Runnable> deferredAddTrail = new ConcurrentLinkedDeque<>();
        private final Set<StaticTrailData> submitTrailChanges = ConcurrentHashMap.newKeySet(64);

        private final ConcurrentMap<Object, Set<BUtil_StaticTrailMemory>> entityLinkedTrailMap = new ConcurrentHashMap<>(64);
        private final ConcurrentMap<BUtil_StaticTrailMemory, Object> trailLinkedEntityMap = new ConcurrentHashMap<>(256);
        private final Set<Object> cutTrailProcessMark = ConcurrentHashMap.newKeySet(64);
        private final Deque<Object> cutTrailProcess = new ConcurrentLinkedDeque<>();
        private final Set<Object> removeTrailProcessMark = ConcurrentHashMap.newKeySet(64);
        private final Deque<RemoveEntityTrailPair> removeTrailProcess = new ConcurrentLinkedDeque<>();
        private final Deque<Runnable> deferredDeletePool = new ConcurrentLinkedDeque<>();

        private final BUtil_SpinBarrier computeBarrier = new BUtil_SpinBarrier(2);
        private final AtomicInteger[] layerTrailCount = new AtomicInteger[MAX_LAYERS];
        private final Lock initLayerLock = new SpinLock();

        private boolean u_lastLocation2 = false;
        private int u_lastLocation3 = 0;

        OpResource() {}
    }

    private record RemoveEntityTrailPair(Object entity, boolean isImmediate) {}

    private boolean rebindReq = false;
    private int vao = 0;
    private final int maxFullNodes;
    private final float maxDur;
    private final long allocSize;
    private final FloatBuffer statePackageMem = BufferUtils.createFloatBuffer(40).clear();
    private final StaticTrailData trailData;
    private final AtomicInteger trailComputeIdx = new AtomicInteger(0);
    private final AtomicInteger trailVertexBufIdx = new AtomicInteger(0);
    private final SpinLock drawPiLock = new SpinLock();
    private final DrawPi[] drawPi = new DrawPi[MAX_LAYERS];

    private static final class DrawPi {
        private final AtomicInteger bufSize = new AtomicInteger(2);
        private final AtomicInteger writePtr = new AtomicInteger(0);
        private volatile IntBuffer piFirst = BufferUtils.createIntBuffer(2).clear();
        private volatile IntBuffer piCount = BufferUtils.createIntBuffer(2).clear();
        private IntBuffer piFirstViewer = this.piFirst.duplicate();
        private IntBuffer piCountViewer = this.piCount.duplicate();

        private static void limitBuf(final IntBuffer buf, int cap) {
            buf.position(0).limit(cap);
        }

        private void writeData(int pos, int piFirst, int piCount) {
            this.piFirst.put(pos, piFirst);
            this.piCount.put(pos, piCount);
        }

        private int putDrawPi(final BUtil_StaticTrailMemoryPool pool, int piFirst, int piCount) {
            final int writeP = this.writePtr.getAndIncrement();

            if (this.bufSize.get() > writeP) {
                this.writeData(writeP, piFirst, piCount);
                return writeP;
            }

            while (true) { // just for more threads at future
                if (pool.drawPiLock.tryLock()) {
                    if (this.bufSize.get() > writeP) {
                        this.writeData(writeP, piFirst, piCount);
                        pool.drawPiLock.unlock();
                        return writeP;
                    }

                    final int newSize = this.bufSize.get() << 2;

                    IntBuffer tmpBuf = BufferUtils.createIntBuffer(newSize).position(0);
                    tmpBuf.put(this.piFirst.duplicate());
                    this.piFirst = tmpBuf.clear();
                    this.piFirstViewer = tmpBuf.duplicate();

                    tmpBuf = BufferUtils.createIntBuffer(newSize).position(0);
                    tmpBuf.put(this.piCount.duplicate());
                    this.piCount = tmpBuf.clear();
                    this.piCountViewer = tmpBuf.duplicate();

                    this.writeData(writeP, piFirst, piCount);

                    this.bufSize.set(newSize);
                    pool.drawPiLock.unlock();
                } else {
                    while (pool.drawPiLock.isLocked()) Thread.onSpinWait();

                    if (this.bufSize.get() > writeP) {
                        this.writeData(writeP, piFirst, piCount);
                        return writeP;
                    }
                }
            }
        }

        private void finishVertexPtr() {
            final int drawNum = this.writePtr.get(); // when have valid node: ptr++ => size
            if (drawNum < 1) return;
            limitBuf(this.piFirstViewer, drawNum);
            limitBuf(this.piCountViewer, drawNum);
            this.writePtr.set(0);
        }

        private void makeTrailHidden(int bufPos) {
            this.piCount.put(bufPos, 0);
        }
    }

    private static boolean poolReq(GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> ignore) {
        return BoxConfigs.isBackgroundThreadGLValid() && ShaderCore.isStaticTrailShaderValid() && ShaderCore.isGlobalDataUBOValid();
    }

    private static void poolRebind(GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        if (pool instanceof BUtil_StaticTrailMemoryPool poolCast) poolCast.rebindReq = true;
    }

    private static void poolDestroy(GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        if (pool instanceof BUtil_StaticTrailMemoryPool poolCast && poolCast.vao > 0) {
            GLWrapper.VAO.glBindVertexArray(0);
            GLWrapper.VAO.glDeleteVertexArrays(poolCast.vao);
        }
    }

    private static BUtil_StaticTrailMemory makeEmptyMem(BUtil_StaticTrailTrackerObject meta, long address, long size, int index, GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        return new BUtil_StaticTrailMemory(meta, address, size, index, true);
    }

    private static BUtil_StaticTrailMemory makeNotEmptyMem(BUtil_StaticTrailTrackerObject meta, long address, long size, int index, GPUMemoryPool<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> pool) {
        return new BUtil_StaticTrailMemory(meta, address, size, index, false);
    }

    public BUtil_StaticTrailMemoryPool(final GPUPoolBehavior<BUtil_StaticTrailMemory, BUtil_StaticTrailTrackerObject> behavior, final StaticTrailData trailData, float maxDur) {
        super(behavior);
        this.maxDur = maxDur;
        this.maxFullNodes = (int) Math.ceil(this.maxDur * BoxConfigs.getMaxTrailSystemNodePerSeconds()) + 3; // two for ends fill, another for loop draw
        this.allocSize = (long) this.maxFullNodes * NODE_BYTE_SIZE;
        this.trailData = trailData;
        this.behavior.setDefaultBufferSize(this.allocSize * trailData.initCapacity);
    }

    private void applyVertexAttrib() {
        if (GLWrapper.VAO.valid()) {
            if (this.vao < 1) this.vao = GLWrapper.VAO.glGenVertexArrays();
            GLWrapper.VAO.glBindVertexArray(this.vao);
            if (this.rebindReq) this.rebindReq = false; else return;
        }

        GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, this.glID);
        GLWrapper.VAO.glVertexAttribPointer(0, 2, GLWrapper.DataType.GL_FLOAT, false, NODE_BYTE_SIZE, 0);
        GLWrapper.VAO.glVertexAttribPointer(1, 2, GLWrapper.DataType.GL_HALF_FLOAT, false, NODE_BYTE_SIZE, 8);
        GLWrapper.VAO.glVertexAttribPointer(2, 4, GLWrapper.DataType.GL_UNSIGNED_BYTE, true, NODE_BYTE_SIZE, 12);
        GLWrapper.VAO.glVertexAttribIPointer(3, 1, GLWrapper.DataType.GL_UNSIGNED_INT, NODE_BYTE_SIZE, 16);
        GLWrapper.VAO.glVertexAttribIPointer(4, 1, GLWrapper.DataType.GL_UNSIGNED_INT, NODE_BYTE_SIZE, 20);

        if (GLWrapper.VAO.valid()) {
            GLWrapper.VAO.glEnableVertexAttribArray(0);
            GLWrapper.VAO.glEnableVertexAttribArray(1);
            GLWrapper.VAO.glEnableVertexAttribArray(2);
            GLWrapper.VAO.glEnableVertexAttribArray(3);
            GLWrapper.VAO.glEnableVertexAttribArray(4);
            GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
        }
    }

    private static void legacyBlendCheck(boolean additiveBlend) {
        if (additiveBlend != RES.additiveBlend) {
            RES.additiveBlend = additiveBlend;
            GLWrapper.Operation.glBlendFunc(GLWrapper.Operation.GL_SRC_ALPHA, additiveBlend ? GLWrapper.Operation.GL_ONE : GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    private void glDrawTrail(final BaseShaderData program, int layerLoc, int layerBits) {
        if (this.drawPi[layerLoc] == null) return;
        final IntBuffer piFirst = this.drawPi[layerLoc].piFirstViewer, piCount = this.drawPi[layerLoc].piCountViewer;
        if (piFirst.limit() < 1 || piCount.limit() < 1) return;

        final var material = this.trailData.material;
        final boolean isAdditionEmissive = material.isAdditionEmissive();
        int dataBit = layerBits;

        if (checkUpdateTrailUniforms(this.trailData)) this.updateStateUniforms();
        if (material.isIgnoreIllumination()) dataBit |= 0b10;
        if (material.getAnisotropic() < 0.0f) dataBit |= 0b100;
        GLWrapper.Shader.glUniform4(program.location[0], this.statePackageMem);
        if (RES.u_lastLocation2 != isAdditionEmissive) {
            RES.u_lastLocation2 = isAdditionEmissive;
            GLWrapper.Shader.glUniform1ui(program.location[2], isAdditionEmissive ? 1 : 0);
        }
        if (RES.u_lastLocation3 != dataBit) {
            GLWrapper.Shader.glUniform1ui(program.location[3], (RES.u_lastLocation3 = dataBit));
        }

        this.trailData.material.putShaderTexture();

        if (BoxConfigs.isShaderEnable()) BUtil_GLImpl.blendCheck(this.trailData.additiveBlend ? BoxEnum.ENTITY_ADDITIVE_BLEND : BoxEnum.ENTITY_NORMAL_BLEND, 0, 0, 0, 0, 0);
        else legacyBlendCheck(this.trailData.additiveBlend);

        this.applyVertexAttrib();
        GLWrapper.Drawcall.glMultiDrawArrays(GLWrapper.Drawcall.GL_LINE_STRIP_ADJACENCY, piFirst, piCount);
    }

    // ====================================
    // =============== impl ===============
    // ====================================

    public static byte toLayerLoc(final CombatEngineLayers layer) {
        return (byte) layer.ordinal();
    }

    public static byte toLayerLoc(final CampaignEngineLayers layer) {
        return (byte) (layer.ordinal() + MAX_COMBAT_LAYERS);
    }

    private static BUtil_StaticTrailMemoryPool setupTrail(final StaticTrailData target) {
        final float maxDur = Math.max(target.durFadeIn, 0.0f) + Math.max(target.durFull, 0.0f) + Math.max(target.durFadeOut, 0.0f);
        if (maxDur < StaticTrailData.MINIMUM_TOTAL_DURATION) return null;

        return RES.trailPoolMap.computeIfAbsent(target, (targetData) -> {
            final var l_behavior = new GPUPoolBehavior<>(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, BUtil_StaticTrailMemoryPool::makeEmptyMem, BUtil_StaticTrailMemoryPool::makeNotEmptyMem);
            l_behavior.setBufferAccessBits(GLWrapper.Buffer.GL_MAP_WRITE_BIT | GLWrapper.Buffer.GL_MAP_PERSISTENT_BIT)
                    .setContextRequirements(BUtil_StaticTrailMemoryPool::poolReq)
                    .setRebindBuffer(BUtil_StaticTrailMemoryPool::poolRebind)
                    .setPoolDestroy(BUtil_StaticTrailMemoryPool::poolDestroy)
                    .setBufferInitRule((req, pool) -> req < l_behavior.defaultBufferSize ? l_behavior.defaultBufferSize : CalculateUtil.getPOTMax(req));
            final var l_pool = new BUtil_StaticTrailMemoryPool(l_behavior, targetData, maxDur);
            l_pool.init();
            return l_pool.isInvalid() ? null : l_pool;
        });
    }

    public static boolean addTracker(@NotNull final StaticTrailData trailData, @Nullable final Object linkedEntity, byte layerLoc, @NotNull final StaticTrailTracker tacker) {
        final var pool = setupTrail(trailData);
        if (pool == null || pool.isInvalid()) return false;

        RES.deferredAddTrail.offer(() -> { // at single thread only
            if (pool.isInvalid()) return;
            final var l_trailMem = pool.malloc(
                    new BUtil_StaticTrailTrackerObject(layerLoc, tacker,
                            new BUtil_StaticTrailCallback(), pool,
                            pool.isNotPersistentMapping() ? BufferUtils.createIntBuffer((int) (pool.allocSize >>> 2)).clear() : null),
                    pool.allocSize
            );

            if (linkedEntity != null) {
                RES.entityLinkedTrailMap.computeIfAbsent(linkedEntity, (k) -> ConcurrentHashMap.newKeySet(64))
                        .add(l_trailMem);
                RES.trailLinkedEntityMap.put(l_trailMem, linkedEntity);
            }
            if (pool.drawPi[layerLoc] == null) {
                pool.drawPiLock.lock();
                if (pool.drawPi[layerLoc] == null) {
                    pool.drawPi[layerLoc] = new DrawPi();
                }
                pool.drawPiLock.unlock();
            }
            if (RES.layerTrailCount[layerLoc] == null) {
                RES.initLayerLock.lock();
                if (RES.layerTrailCount[layerLoc] == null) {
                    RES.layerTrailCount[layerLoc] = new AtomicInteger(1);
                }
                RES.initLayerLock.unlock();
            } else RES.layerTrailCount[layerLoc].incrementAndGet();
            submitTrailDataChanges(trailData);
        });
        return true;
    }

    public static void submitTrailDataChanges(final StaticTrailData trailData) {
        if (BoxConfigs.isTrailSystemEnable()) RES.submitTrailChanges.add(trailData);
    }

    public static void deferredAddTracker(boolean auxThread) {
        final var deque = RES.deferredAddTrail;
        final Function<Deque<Runnable>, Runnable> invokePoll = auxThread ? Deque::pollLast : Deque::pollFirst;
        Runnable adder;
        while ((adder = invokePoll.apply(deque)) != null) {
            adder.run();
        }
    }

    public static void deferredDeletePool(boolean auxThread) {
        final var deque = RES.deferredDeletePool;
        final Function<Deque<Runnable>, Runnable> invokePoll = auxThread ? Deque::pollLast : Deque::pollFirst;
        Runnable adder;
        while ((adder = invokePoll.apply(deque)) != null) {
            adder.run();
        }
    }

    private static void tryRemoveTrailLinkedEntity(final BUtil_StaticTrailMemory trailMem) {
        final var trailLinkedEntity = RES.trailLinkedEntityMap.remove(trailMem);
        if (trailLinkedEntity != null) {
            final var trailSet = RES.entityLinkedTrailMap.get(trailLinkedEntity);
            if (trailSet != null && !trailSet.isEmpty()) trailSet.remove(trailMem);
            if (trailSet == null || trailSet.isEmpty()) RES.entityLinkedTrailMap.remove(trailLinkedEntity);
        }
    }

    private static boolean bypassTrail(byte layerLoc, boolean inCampaign) {
        if (layerLoc < MAX_COMBAT_LAYERS) {
            return inCampaign; // do campaign only
        } else return !inCampaign; // do combat only
    }

    public static void computeTrailNode(boolean auxThread) {
        final float amount = BUtil_GLImpl.getStaticTrailFrameAmount(), elapsedTime = BUtil_GLImpl.getElapsedTimeWithoutPaused();
        final boolean inCampaign = BUtil_GLImpl.isInCampaignSector();

        for (var pool : RES.trailPoolMap.values()) {
            if (pool == null || pool.isInvalid() || pool.getBufferReference() < 1) continue;

            final var trailMemList = pool.mem; // maybe use double-deque switching for lots of thread at future
            final int totalTrail = trailMemList.size();
            if (totalTrail < 1) continue;

            if (auxThread) {
                pool.trailComputeIdx.set(0);
                pool.trailVertexBufIdx.set(inCampaign ? MAX_COMBAT_LAYERS : 0);
            }
            RES.computeBarrier.barrier();

            byte layerLoc;
            int computeNode;
            BUtil_StaticTrailMemory trailMemory;

            final boolean notPersistentMapping = pool.isNotPersistentMapping();
            final IntBuffer writeBuffer = notPersistentMapping ? null : pool.getMappingBuffer().asIntBuffer(); // notnull
            final List<BUtil_StaticTrailMemory> toFreeList = new ArrayList<>(totalTrail);

            int idx;
            if (notPersistentMapping) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, pool.getBufferID());
            while ((idx = pool.trailComputeIdx.getAndIncrement()) < totalTrail) {
                trailMemory = trailMemList.get(idx);
                if (trailMemory.is_free()) continue;
                layerLoc = trailMemory.meta().layerLoc();
                if (bypassTrail(layerLoc, inCampaign)) continue;

                computeNode = trailMemory.computeData(pool, writeBuffer, notPersistentMapping, amount, elapsedTime);
                if (computeNode < 1) {
                    if (computeNode < 0) toFreeList.add(trailMemory);
                    continue;
                }

                trailMemory.setCurrPiFirstBufPos(pool.drawPi[layerLoc].putDrawPi(pool, trailMemory.getPiFirstAddress(), computeNode + 2)); // 2 => ends fill node
            }
            if (notPersistentMapping) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
            RES.computeBarrier.barrier();

            DrawPi drawPiVar;
            final byte totalLayers = inCampaign ? MAX_LAYERS : MAX_COMBAT_LAYERS;
            while ((idx = pool.trailVertexBufIdx.getAndIncrement()) < totalLayers) {
                if ((drawPiVar = pool.drawPi[idx]) != null) drawPiVar.finishVertexPtr();
            }

            for (var trailMem : toFreeList) {
                tryRemoveTrailLinkedEntity(trailMem);

                if (trailMem.is_free()) continue;
                RES.layerTrailCount[trailMem.meta().layerLoc()].decrementAndGet();
                pool.free(trailMem);
            }
        }
    }

    public static boolean bypassDrawTrail(int layerLoc) {
        return RES.layerTrailCount[layerLoc] == null || RES.layerTrailCount[layerLoc].get() < 1;
    }

    private static boolean checkUpdateTrailUniforms(final StaticTrailData trailData) {
        return RES.submitTrailChanges.remove(trailData);
    }

    public static void drawEachTrail(final BaseShaderData program, int layerLoc, int layerBits) {
        final boolean active = !RES.trailPoolMap.isEmpty();
        if (!BoxConfigs.isShaderEnable()) {
            RES.additiveBlend = true;
            GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_BLEND);
            GLWrapper.Operation.glBlendFunc(GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE);
        }
        if (!GLWrapper.VAO.valid() && active) {
            GLWrapper.VAO.glEnableVertexAttribArray(0);
            GLWrapper.VAO.glEnableVertexAttribArray(1);
            GLWrapper.VAO.glEnableVertexAttribArray(2);
            GLWrapper.VAO.glEnableVertexAttribArray(3);
            GLWrapper.VAO.glEnableVertexAttribArray(4);
        }
        for (var pool : RES.trailPoolMap.values()) {
            if (pool == null || pool.isInvalid() || pool.getBufferReference() < 1) continue;
            pool.glDrawTrail(program, layerLoc, layerBits);
        }
        if (active) {
            if (GLWrapper.VAO.valid()) {
                GLWrapper.VAO.glBindVertexArray(0);
            } else {
                GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
                GLWrapper.VAO.glDisableVertexAttribArray(0);
                GLWrapper.VAO.glDisableVertexAttribArray(1);
                GLWrapper.VAO.glDisableVertexAttribArray(2);
                GLWrapper.VAO.glDisableVertexAttribArray(3);
                GLWrapper.VAO.glDisableVertexAttribArray(4);
            }
        }
    }

    public static void offerCutTrailOnEntity(final Object linkedEntity) {
        if (BoxConfigs.isTrailSystemEnable() && RES.cutTrailProcessMark.add(linkedEntity)) RES.cutTrailProcess.offer(linkedEntity);
    }

    public static void offerRemoveTrailOnEntity(final Object linkedEntity, boolean isImmediate) {
        if (BoxConfigs.isTrailSystemEnable() && RES.removeTrailProcessMark.add(linkedEntity)) RES.removeTrailProcess.offer(new RemoveEntityTrailPair(linkedEntity, isImmediate));
    }

    public static void processCutTrailOnEntity() { // single thread only
        if (RES.cutTrailProcess.isEmpty()) return;

        Object linkedEntity;
        Set<BUtil_StaticTrailMemory> trailSet;
        final Map<BUtil_StaticTrailMemoryPool, List<BUtil_StaticTrailMemory>> toProcess = new HashMap<>(16);
        while ((linkedEntity = RES.cutTrailProcess.poll()) != null) {
            RES.cutTrailProcessMark.remove(linkedEntity);

            trailSet = RES.entityLinkedTrailMap.get(linkedEntity);
            if (trailSet == null || trailSet.isEmpty()) continue;

            for (BUtil_StaticTrailMemory trailMemory : trailSet) {
                if (trailMemory.is_free()) continue;

                toProcess.computeIfAbsent(trailMemory.meta().pool(), (k) -> new ArrayList<>(128))
                        .add(trailMemory);
            }
        }

        if (toProcess.isEmpty()) return;

        BUtil_StaticTrailMemoryPool pool = null;
        List<BUtil_StaticTrailMemory> trailList;
        boolean shouldUnbind = false;
        for (var entry : toProcess.entrySet()) {
            pool = entry.getKey();
            trailList = entry.getValue();

            final boolean notPersistentMapping = pool.isNotPersistentMapping();
            final IntBuffer writeBuffer = notPersistentMapping ? null : pool.getMappingBuffer().asIntBuffer(); // notnull
            shouldUnbind |= notPersistentMapping;

            if (notPersistentMapping) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, pool.getBufferID());
            for (BUtil_StaticTrailMemory trailMemory : trailList) {
                if (trailMemory.is_free()) continue;
                trailMemory.cutTrail(pool, writeBuffer, notPersistentMapping);
            }
        }
        if (shouldUnbind) GLWrapper.Buffer.glBindBuffer(GLWrapper.Buffer.VBO.GL_ARRAY_BUFFER, 0);
    }

    public static void processRemoveTrailOnEntity() {
        if (RES.removeTrailProcess.isEmpty()) return;

        RemoveEntityTrailPair linkedEntity;
        Set<BUtil_StaticTrailMemory> trailSet;
        while ((linkedEntity = RES.removeTrailProcess.poll()) != null) {
            RES.removeTrailProcessMark.remove(linkedEntity.entity());

            trailSet = RES.entityLinkedTrailMap.get(linkedEntity.entity());
            if (trailSet == null || trailSet.isEmpty()) continue;

            for (BUtil_StaticTrailMemory trailMemory : trailSet) {
                if (trailMemory.is_free()) continue;

                final var meta = trailMemory.meta();
                meta.pool().drawPi[meta.layerLoc()].makeTrailHidden(trailMemory.getCurrPiFirstBufPos());
                if (linkedEntity.isImmediate) meta.callback().destroyImmediate(); else meta.callback().destroy();
            }
        }
    }

    public static void cleanupPool(boolean cleanupCombat) {
        Map.Entry<StaticTrailData, BUtil_StaticTrailMemoryPool> entry;
        BUtil_StaticTrailMemoryPool pool;
        for (final var iterator = RES.trailPoolMap.entrySet().iterator(); iterator.hasNext();) {
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
                tryRemoveTrailLinkedEntity(trailMemory);

                if (trailMemory.is_free()) continue;
                RES.layerTrailCount[trailMemory.meta().layerLoc()].decrementAndGet();
                pool.free(trailMemory);
            }

            if (pool.getBufferReference() < 1) {
                pool.destroy(); // on main thread, safe delete vao
                iterator.remove();
            }
        }
    }

    public static boolean removeTracker(@NotNull final StaticTrailData trailData, boolean inCampaign) {
        if (!BoxConfigs.isTrailSystemEnable()) return false;
        final var pool = RES.trailPoolMap.remove(trailData);
        if (pool == null) return false;
        RES.deferredDeletePool.offer(() -> {
            for (var trailMemory : pool.mem) {
                tryRemoveTrailLinkedEntity(trailMemory);

                if (trailMemory.is_free()) continue;
                RES.layerTrailCount[trailMemory.meta().layerLoc()].decrementAndGet();
                pool.free(trailMemory);
            }
            BoxThreadSync.Logical.offerBeginAdvanceDelayGLCmd(pool::destroy, inCampaign); // on background threads, not safe for delete vao
        });
        return true;
    }

    // ===================================
    // =============== com ===============
    // ===================================

    public static void initPool() {
        if (!poolReq(null)) return;
        RES.valid = true;
    }

    public static BUtil_StaticTrailMemoryPool getPool(final StaticTrailData target) {
        return RES.trailPoolMap.get(target);
    }

    public static boolean isSupported() {
        return RES.valid;
    }

    public static int getTrailTypes() {
        return RES.trailPoolMap.size();
    }

    private static boolean l_checkPoolValid(final BUtil_StaticTrailMemoryPool pool) {
        return pool != null && !pool.isInvalid();
    }

    public static long getTotalAllocatedMemory() {
        return RES.trailPoolMap.values().stream().filter(BUtil_StaticTrailMemoryPool::l_checkPoolValid).mapToLong(BUtil_StaticTrailMemoryPool::getBufferTotal).sum();
    }

    public static long getTotalSpace() {
        return RES.trailPoolMap.values().stream().filter(BUtil_StaticTrailMemoryPool::l_checkPoolValid).mapToLong(BUtil_StaticTrailMemoryPool::getBufferSpace).sum();
    }

    private boolean isNotPersistentMapping() {
        return !this.isPersistentMapping() || this.getMappingBuffer() == null;
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

    private void updateStateUniforms() {
        final var in_trailData = this.trailData;
        this.statePackageMem.put(0, in_trailData.material.getState(), 0, 12);
        tryVec4(12, in_trailData.colorIn, 1.0f, 1.0f, 1.0f, 1.0f);
        tryVec4(16, in_trailData.colorOut, 1.0f, 1.0f, 1.0f, 1.0f);
        this.statePackageMem.put(20, in_trailData.durFadeIn);
        this.statePackageMem.put(21, in_trailData.durFull);
        this.statePackageMem.put(22, in_trailData.durFadeOut);
        this.statePackageMem.put(23, in_trailData.randomUVStartOffset ? 1.0f : 0.0f);
        this.statePackageMem.put(24, in_trailData.sizeIn * 0.5f); // half-width * normal both sides
        this.statePackageMem.put(25, in_trailData.sizeOut * 0.5f);
        this.statePackageMem.put(26, in_trailData.smoothEnds);
        this.statePackageMem.put(27, in_trailData.textureSpeed / in_trailData.texturePixels);
        tryVec4(28, in_trailData.velocityInRange, 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec4(32, in_trailData.velocityOutRange, 0.0f, 0.0f, 0.0f, 0.0f);
        tryVec2(36, in_trailData.angularInRange, 0.0f, 0.0f);
        tryVec2(38, in_trailData.angularOutRange, 0.0f, 0.0f);
    }
}
