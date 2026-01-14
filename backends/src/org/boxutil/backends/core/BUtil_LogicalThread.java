package org.boxutil.backends.core;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.api.ControlDataAPI;
import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.InstanceType;
import org.boxutil.define.struct.instance.MemoryBlock;
import org.boxutil.manager.ShaderCore;
import org.lwjgl.opengl.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

final class BUtil_LogicalThread extends BUtil_BoxUtilBackgroundThread._ThreadTemplate {
    private final static class ComputeNum {
        int begin;
        int end;

        ComputeNum(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
    }

    private final static Deque<ComputeNum>[] _SKIP_MEMORY = new Deque[]{new ConcurrentLinkedDeque<ComputeNum>(), new ConcurrentLinkedDeque<ComputeNum>()};

    private final Deque<BackgroundEveryFramePlugin> _tmpQueue_plugin = new ConcurrentLinkedDeque<>();
    private final Deque<RenderDataAPI> _tmpQueue_entity = new ConcurrentLinkedDeque<>();
    private final boolean _isAux;

    BUtil_LogicalThread(Thread hostThread, Drawable sharedDrawable, boolean isAux) {
        super(hostThread, sharedDrawable);
        this._isAux = isAux;
    }

    private void runThreadPlugin(final boolean isBegin) {
        this._tmpQueue_plugin.clear();

        final Deque<BackgroundEveryFramePlugin> queue = BUtil_ThreadResource.Logical.getThreadPluginQueue();
        final float amount = BUtil_ThreadResource._CURR_AMOUNT;
        final boolean isPaused = BUtil_ThreadResource._CURR_PAUSED;

        BackgroundEveryFramePlugin plugin;
        while ((plugin = this._isAux ? queue.pollLast() : queue.pollFirst()) != null) {
            if (isBegin) plugin.runBeginAdvance(amount, isPaused); else plugin.runAfterAdvance(amount, isPaused);
            if (!plugin.isAdvanceExpired()) {
                if (this._isAux) this._tmpQueue_plugin.offerFirst(plugin); else this._tmpQueue_plugin.offerLast(plugin);
            }
        }
    }

    private void runEntityAdvance() {
        this._tmpQueue_entity.clear();

        final Deque<RenderDataAPI> queue = BUtil_ThreadResource.Logical.getEntitiesLogicalQueue();
        final float amount = BUtil_ThreadResource._CURR_AMOUNT;
        final boolean isPaused = BUtil_ThreadResource._CURR_PAUSED;
        final boolean customShaderpacksDataLayout = BoxConfigs.getCurrShaderPacksContext().haveCustomInstanceDataLayout(), instanceDataSupported = !BUtil_InstanceDataMemoryPool.isNotSupported();

        RenderDataAPI entity;
        ControlDataAPI data;
        MemoryBlock memory;
        boolean toRemove, haveData, ignoreCompute, shaderpacksCustomData;
        int addBegin, addEnd, addBegin2, addEnd2;
        while ((entity = this._isAux ? queue.pollLast() : queue.pollFirst()) != null) {
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

            if (toRemove) entity.delete(); else {
                if (this._isAux) this._tmpQueue_entity.offerFirst(entity); else this._tmpQueue_entity.offerLast(entity);
            }
        }
    }

    private void runEntitySubmit() {
        BUtil_ThreadResource.Logical.runEntitySubmit(this._isAux);
    }

    private static void compactMemoryPoolTarget(final InstanceType target) {
        if (System.nanoTime() - BUtil_InstanceDataMemoryPool.getLastCompactTimeStampNano(target) > 20_000_000_000L) BUtil_InstanceDataMemoryPool._compact(target);
    }

    private void compactMemoryPool() {
        if (BUtil_InstanceDataMemoryPool.isNotSupported()) return;
        if (this._isAux) {
            compactMemoryPoolTarget(InstanceType.DYNAMIC_3D);
            compactMemoryPoolTarget(InstanceType.FIXED_3D);
        } else {
            compactMemoryPoolTarget(InstanceType.DYNAMIC_2D);
            compactMemoryPoolTarget(InstanceType.FIXED_2D);
        }
    }

    private void preComputeInstance() {
        if (BUtil_InstanceDataMemoryPool.isNotSupported()) return;
        final var instanceType = this._isAux ? InstanceType.DYNAMIC_3D : InstanceType.DYNAMIC_2D;
        final long edge = BUtil_InstanceDataMemoryPool.getBufferEdge(instanceType);
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
        final var program = this._isAux ? ShaderCore.getInstanceMatrix3DProgram() : ShaderCore.getInstanceMatrix2DProgram();
        final float dimAMD = BoxDatabase.isGLDeviceAMD() ? 64.0f : 32.0f;
        BUtil_InstanceDataMemoryPool.rebindSSBO(instanceType);
        program.active();
        GL20.glUniform1f(program.location[0], BUtil_ThreadResource._CURR_AMOUNT);
        for (var target : processQueue) {
            final int computeBegin = target.begin, computeEnd = target.end, itemDim = (int) Math.ceil(Math.sqrt((computeEnd - computeBegin) / dimAMD));
            GL20.glUniform2i(program.location[1], computeBegin, computeEnd);
            GL43.glDispatchCompute(1, itemDim, itemDim);
        }
        program.close();
        GL42.glMemoryBarrier(GL43.GL_SHADER_STORAGE_BARRIER_BIT);
    }

    private void sendBeginAdvanceSync() {
        BUtil_ThreadResource.sendGLSync(this._isAux ? BUtil_ThreadResource.__SYNC_AUX_BEGIN_ADVANCE : BUtil_ThreadResource.__SYNC_BEGIN_ADVANCE);
    }

    private void sendFinishAdvanceSync() {
        BUtil_ThreadResource.sendGLSync(this._isAux ? BUtil_ThreadResource.__SYNC_AUX_FINISH_ADVANCE : BUtil_ThreadResource.__SYNC_FINISH_ADVANCE);
    }

    protected void runBody() {
        BoxThreadSync.Logical.beginAdvance().arriveAndAwaitAdvance();
        BUtil_ThreadResource.tryGLSync(this._isAux ? BUtil_ThreadResource.__SYNC_AUX_AFTER_RENDERING_HOST : BUtil_ThreadResource.__SYNC_AFTER_RENDERING_HOST);
        if (!this._tmpQueue_plugin.isEmpty()) BUtil_ThreadResource.Logical.addAllThreadPlugin(this._tmpQueue_plugin);
        this.runThreadPlugin(true);
        this.runEntityAdvance();
        this.runEntitySubmit();
        GL42.glMemoryBarrier(GL42.GL_BUFFER_UPDATE_BARRIER_BIT);

        BoxThreadSync.Logical.beginPoolCompact().arriveAndAwaitAdvance();
        if (!this._tmpQueue_plugin.isEmpty()) BUtil_ThreadResource.Logical.addAllThreadPlugin(this._tmpQueue_plugin);
        if (!this._tmpQueue_entity.isEmpty()) BUtil_ThreadResource.Logical.addAllEntitiesLogical(this._tmpQueue_entity);
        this.compactMemoryPool();

        BoxThreadSync.Logical.beginInstanceCompute().arriveAndAwaitAdvance();
        this.preComputeInstance();
        this.sendFinishAdvanceSync();

        BoxThreadSync.Logical.finishAdvance().arriveAndAwaitAdvance();
        this.runThreadPlugin(false);
        this.sendBeginAdvanceSync();
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
