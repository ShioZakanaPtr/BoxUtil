package org.boxutil.backends.core.thread;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.boxutil.backends.core.instancedrendering.BUtil_InstanceDataMemoryPool;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.util.concurrent.SpinBarrier;
import org.boxutil.base.BaseIlluminantData;
import org.boxutil.base.BaseProjectileTrailTracker;
import org.boxutil.base.api.ControlDataAPI;
import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.manager.CombatRenderingManager;
import org.boxutil.manager.ShaderCore;
import org.boxutil.manager.StaticTrailManager;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.lwjgl.opengl.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Function;

final class BUtil_LogicalThread extends BUtil_BoxUtilBackgroundThread.ThreadTemplate {
    private final static class ComputeNum {
        int begin;
        int end;

        ComputeNum(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
    }

    private interface InvokePluginFun {
        void run(BackgroundEveryFramePlugin plugin, float amount, boolean isPaused);
    }

    @SuppressWarnings("unchecked")
    private final static Deque<ComputeNum>[] _SKIP_MEMORY = new Deque[]{new ConcurrentLinkedDeque<ComputeNum>(), new ConcurrentLinkedDeque<ComputeNum>()};
    private final static SpinBarrier _PROJ_SNAPSHOT_BARRIER = new SpinBarrier(2);
    private final static AtomicInteger _PROJ_SNAPSHOT_IDX = new AtomicInteger(0);
    private final static List<DamagingProjectileAPI> _PROJ_SNAPSHOT = new ArrayList<>(1024);
    private static volatile int _CURR_PROJ_SNAPSHOT_SIZE = 0;

    private final boolean _isAux;

    BUtil_LogicalThread(Thread hostThread, Drawable sharedDrawable, Object isAux) {
        super(hostThread, sharedDrawable, isAux);
        this._isAux = (boolean) isAux;
    }

    private void runThreadPlugin(final boolean isBegin) {
        final var deque = BUtil_ResourceStorage.sharedResource().getLogicalPlugins();
        final var dequeNext = BUtil_ResourceStorage.sharedResource().getNextLogicalPlugins();
        final float amount = BUtil_GLImpl.getLastFrameAmount();
        final boolean isPaused = BUtil_GLImpl.isPaused();
        final Function<Deque<BackgroundEveryFramePlugin>, BackgroundEveryFramePlugin> invokePoll = this._isAux ? Deque::pollLast : Deque::pollFirst;
        final InvokePluginFun invokePluginFun = isBegin ? BackgroundEveryFramePlugin::runBeginAdvance : BackgroundEveryFramePlugin::runAfterAdvance;
        final BiPredicate<Deque<BackgroundEveryFramePlugin>, BackgroundEveryFramePlugin> invokeOffer = this._isAux ? Deque::offerFirst : Deque::offerLast;

        BackgroundEveryFramePlugin plugin;
        while ((plugin = invokePoll.apply(deque)) != null) {
            invokePluginFun.run(plugin, amount, isPaused);
            if (!plugin.isAdvanceExpired()) invokeOffer.test(dequeNext, plugin);
        }
    }

    private void runEntityAdvance() {
        final var deque = BUtil_ResourceStorage.sharedResource().getSharedLogicalEntities();
        final var dequeNext = BUtil_ResourceStorage.sharedResource().getNextSharedLogicalEntities();
        final float amount = BUtil_GLImpl.getLastFrameAmount();
        final boolean isPaused = BUtil_GLImpl.isPaused();
        final boolean customShaderpacksDataLayout = BoxConfigs.getCurrShaderPacksContext().haveCustomInstanceDataLayout(), instanceDataSupported = !BUtil_InstanceDataMemoryPool.isPoolInvalid();
        final Function<Deque<RenderDataAPI>, RenderDataAPI> invokePoll = this._isAux ? Deque::pollLast : Deque::pollFirst;
        final BiPredicate<Deque<RenderDataAPI>, RenderDataAPI> invokeOffer = this._isAux ? Deque::offerFirst : Deque::offerLast;

        RenderDataAPI entity;
        ControlDataAPI data;
        MemoryBlock memory;
        boolean toRemove, haveData, ignoreCompute, shaderpacksCustomData;
        int addBegin, addEnd, addBegin2, addEnd2;
        while ((entity = invokePoll.apply(deque)) != null) {
            data = entity.getControlData();
            haveData = data != null;
            if (entity.hasDelete()) continue;

            toRemove = entity.getGlobalTimerState() == BoxEnum.TIMER_INVALID;
            ignoreCompute = false;
            if (haveData) {
                ignoreCompute = isPaused && !data.controlRunWhilePaused(entity); // not running
                if (!ignoreCompute) {
                    data.controlAdvance(entity, amount);
                    if (!data.controlRemoveBasedTimer(entity)) toRemove = false;
                    toRemove |= data.controlIsDone(entity);
                } else toRemove = false;
            }
            entity.advanceGlobalTimer(amount, isPaused);
            ignoreCompute |= toRemove || entity.isTimerPaused() || (isPaused && !entity.isTimingWhenPaused());

            shaderpacksCustomData = entity instanceof BaseIlluminantData && customShaderpacksDataLayout;
            if (shaderpacksCustomData) {
                BoxConfigs.getCurrShaderPacksContext().getCustomInstanceDataLayout().systemAdvance((BaseIlluminantData) entity, amount, isPaused, toRemove, ignoreCompute);
            }

            if (BoxConfigs.isShaderEnable() && instanceDataSupported && entity instanceof InstanceRenderAPI instance) {
                memory = instance.getInstanceDataMemory();
                if (instance.haveValidInstanceData() && !memory.is_type_fixed()) {
                    final int memAddress = memory.address_instance(), memCount = memory.instance_count(),
                            instanceOffset = instance.getRenderingOffset(), instanceCount = instance.getRenderingCount();
                    final var queuePicker = memory.is_type_2D() ? BoxEnum.ZERO : BoxEnum.ONE;
                    addBegin = addEnd = addBegin2 = addEnd2 = -1;

                    if (ignoreCompute || shaderpacksCustomData || !instance.isNeedRefreshInstanceData()) {
                        addBegin = memAddress;
                        addEnd = memAddress + memCount;
                    } else if (memCount > instanceCount) {
                        if (instanceOffset > 0) {
                            addBegin = memAddress;
                            addEnd = memAddress + instanceOffset;
                        }

                        addBegin2 = memAddress + instanceOffset + instanceCount;
                        addEnd2 = memAddress + memCount;
                    }

                    if (addBegin > -1) _SKIP_MEMORY[queuePicker].offer(new ComputeNum(addBegin, addEnd));
                    if (addBegin2 > -1 && addBegin2 < addEnd2) _SKIP_MEMORY[queuePicker].offer(new ComputeNum(addBegin2, addEnd2));
                    if (!instance.isAlwaysRefreshInstanceData()) instance.callRefreshInstanceData(false);
                }
            }

            if (toRemove) entity.delete(); else invokeOffer.test(dequeNext, entity);
        }
    }

    private void runEntitySubmit() {
        BUtil_ResourceStorage.sharedResource().runEntitySubmit(this._isAux);
        if (GLWrapper.Operation.Sync.valid_Barrier() && !GLWrapper.Buffer.valid_BufferStorage()) GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_BUFFER_UPDATE_BARRIER_BIT);
    }

    private static void compactMemoryPoolTarget(final InstanceType target) {
        final var pool = BUtil_InstanceDataMemoryPool.getPool(target);
        final long _poolCompactDelay = 20_000_000_000L;
        if (System.nanoTime() - pool.getLastCompactTimestampNano() > _poolCompactDelay) pool.compact();
    }

    private void compactMemoryPool() { // maybe static trail pool compact was needless
        if (BUtil_InstanceDataMemoryPool.isPoolInvalid()) return;
        if (this._isAux) {
            compactMemoryPoolTarget(InstanceType.FIXED_2D);
            compactMemoryPoolTarget(InstanceType.FIXED_3D);
        } else {
            compactMemoryPoolTarget(InstanceType.DYNAMIC_2D);
            compactMemoryPoolTarget(InstanceType.DYNAMIC_3D);
        }
        GLWrapper.Operation.Sync.glMemoryBarrier(GLWrapper.Operation.Sync.GL_BUFFER_UPDATE_BARRIER_BIT);
    }

    private void preComputeInstance() {
        if (BUtil_InstanceDataMemoryPool.isPoolInvalid()) return;
        final var instanceType = this._isAux ? InstanceType.DYNAMIC_3D : InstanceType.DYNAMIC_2D;
        final long edge = BUtil_InstanceDataMemoryPool.getPool(instanceType).getBufferRightEdge();
        final int dataSize = instanceType.getSize();
        if (edge < dataSize) return;

        final byte picker = this._isAux ? BoxEnum.ONE : BoxEnum.ZERO;
        final Deque<ComputeNum> skipBlocks = _SKIP_MEMORY[picker],
                processQueue = new ArrayDeque<>(skipBlocks.size() + 1);
        processQueue.offer(new ComputeNum(0, Math.toIntExact(edge / dataSize)));

        Iterator<ComputeNum> iterator;
        ComputeNum dst;
        for (var src : skipBlocks) {
            iterator = processQueue.iterator();
            while (iterator.hasNext()) {
                dst = iterator.next();

                if (src.begin >= dst.end || src.end <= dst.begin) continue;
                if (src.begin <= dst.begin && src.end >= dst.end) {
                    iterator.remove();
                    continue;
                }

                final boolean inBegin = src.begin >= dst.begin, inEnd = src.end <= dst.end;
                if (inBegin && inEnd) {
                    if (src.end != dst.end) processQueue.offerLast(new ComputeNum(src.end, dst.end));
                } else if (inEnd) dst.begin = src.end;
                if (inBegin) dst.end = src.begin;
                if (dst.begin >= dst.end) iterator.remove();
            }
            if (processQueue.isEmpty()) break;
        }
        skipBlocks.clear();

        if (processQueue.isEmpty()) return;
        int barriers = GLWrapper.Operation.Sync.GL_BUFFER_UPDATE_BARRIER_BIT;
        if (GLWrapper.Buffer.valid_BufferStorage()) barriers |= GLWrapper.Operation.Sync.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;
        GLWrapper.Operation.Sync.glMemoryBarrier(barriers);
        final var program = this._isAux ? ShaderCore.getInstanceMatrix3DProgram() : ShaderCore.getInstanceMatrix2DProgram();
        final float dimAMD = BoxDatabase.isGLDeviceAMD() ? 64.0f : 32.0f;
        BUtil_InstanceDataMemoryPool.getPool(instanceType).rebindBase();
        program.active();
        GLWrapper.Shader.glUniform1f(program.location[0], BUtil_GLImpl.getLastFrameAmount());
        for (var target : processQueue) {
            final int computeBegin = target.begin, computeEnd = target.end, itemDim = (int) Math.ceil(Math.sqrt((computeEnd - computeBegin) / dimAMD));
            GLWrapper.Shader.glUniform2i(program.location[1], computeBegin, computeEnd);
            GLWrapper.Shader.Comp.glDispatchCompute(1, itemDim, itemDim);
        }
        program.close();
    }

    private void markAndGenProjectiles() {
        if (this._isAux) { // just get all and quickly check, do not modify, ignore changes from another threads
            _PROJ_SNAPSHOT.clear();
            final var engine = Global.getCombatEngine();
            if (engine == null) {
                _CURR_PROJ_SNAPSHOT_SIZE = 0;
                _PROJ_SNAPSHOT_BARRIER.barrier();
                return;
            }
            final var projectiles = engine.getProjectiles();
            if (projectiles == null || projectiles.isEmpty()) {
                _CURR_PROJ_SNAPSHOT_SIZE = 0;
                _PROJ_SNAPSHOT_BARRIER.barrier();
                return;
            }
            _PROJ_SNAPSHOT.addAll(projectiles);
            _CURR_PROJ_SNAPSHOT_SIZE = _PROJ_SNAPSHOT.size();
            _PROJ_SNAPSHOT_IDX.set(0);
        }
        _PROJ_SNAPSHOT_BARRIER.barrier();

        final int totalProj = _CURR_PROJ_SNAPSHOT_SIZE;
        if (totalProj < 1) return;

        int i;
        DamagingProjectileAPI proj;
        while ((i = _PROJ_SNAPSHOT_IDX.getAndIncrement()) < totalProj) {
            proj = _PROJ_SNAPSHOT.get(i);
            if (proj == null || proj.wasRemoved() || proj.isExpired()) continue;

            final String projID = proj.getProjectileSpecId();
            if (projID == null || projID.isBlank()) continue;

            final Set<String> trailConfig = StaticTrailManager.getTrailDataConfig(projID);
            if (trailConfig == null || trailConfig.isEmpty()) continue;
            if (BUtil_ResourceStorage.combatLayered().checkAndMarkAutogenProjectile(proj)) {
                for (String trailID : trailConfig) {
                    if (trailID == null || trailID.isBlank()) continue;

                    final StaticTrailData trail = StaticTrailManager.getTrailData(trailID);
                    if (trail == null) continue;
                    CombatRenderingManager.addStaticTrail(
                            trail, proj,
                            trail.renderBelowExplosions ? CombatEngineLayers.ABOVE_SHIPS_LAYER : CombatEngineLayers.BELOW_INDICATORS_LAYER,
                            new BaseProjectileTrailTracker(proj, trail)
                    );
                }
            }
        }
    }

    protected void runBody() {
        BoxThreadSync.Logical.beginAdvance().arriveAndAwaitAdvance();
        this.runThreadPlugin(true);
        this.runEntityAdvance();
        this.runEntitySubmit();
        if (BoxConfigs.isTrailSystemEnable()) BUtil_StaticTrailMemoryPool.deferredDeletePool(this._isAux);

        BoxThreadSync.Logical.beginPoolCompact().arriveAndAwaitAdvance();
        this.compactMemoryPool();

        BoxThreadSync.Logical.beginInstanceCompute().arriveAndAwaitAdvance();
        if (BoxConfigs.isShaderEnable()) this.preComputeInstance();
        if (BoxConfigs.isTrailSystemEnable()) {
            this.markAndGenProjectiles();
            if (!BUtil_GLImpl.isPaused() && BUtil_GLImpl.doStaticTrailCompute()) BUtil_StaticTrailMemoryPool.computeTrailNode(this._isAux);
        }
        if (!this._FAILED) {
            if (GLWrapper.Operation.Sync.valid_Barrier()) {
                int barriers = GLWrapper.Operation.Sync.GL_BUFFER_UPDATE_BARRIER_BIT;
                if (GLWrapper.Buffer.valid_BufferStorage()) barriers |= GLWrapper.Operation.Sync.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;
                GLWrapper.Operation.Sync.glMemoryBarrier(barriers);
            }
            GLWrapper.Operation.Sync.glFlush();
        }

        BoxThreadSync.Logical.finishAdvance().arriveAndAwaitAdvance();
        this.runThreadPlugin(false);
        if (BoxConfigs.isTrailSystemEnable()) BUtil_StaticTrailMemoryPool.deferredAddTracker(this._isAux);
    }

    protected void logicalInit() {
        BoxThreadSync.Logical.beginAdvance().register();
        BoxThreadSync.Logical.beginPoolCompact().register();
        BoxThreadSync.Logical.beginInstanceCompute().register();
        BoxThreadSync.Logical.finishAdvance().register();
    }

    protected void logicalDestroy() {
        BoxThreadSync.Logical.beginAdvance().arriveAndDeregister();
        BoxThreadSync.Logical.beginPoolCompact().arriveAndDeregister();
        BoxThreadSync.Logical.beginInstanceCompute().arriveAndDeregister();
        BoxThreadSync.Logical.finishAdvance().arriveAndDeregister();
    }
}
