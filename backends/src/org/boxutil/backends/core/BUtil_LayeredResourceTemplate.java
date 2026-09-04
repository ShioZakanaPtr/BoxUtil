package org.boxutil.backends.core;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.apache.log4j.Logger;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.backends.util.BUtil_AtomicEnumSet32;
import org.boxutil.base.api.DirectDrawEntity;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.base.api.resource.TemporaryCleanupPlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.LayeredEntityType;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.boxutil.manager.CampaignRenderingManager;
import org.boxutil.manager.CombatRenderingManager;
import org.boxutil.util.concurrent.SpinLock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

public abstract class BUtil_LayeredResourceTemplate<E extends Enum<E>> {
    // entity resource
    protected final EnumMap<E, EnumMap<LayeredEntityType, List<RenderDataAPI>>> renderEntityMap;
    protected final EnumMap<E, Set<LayeredRenderingPlugin>> layeredPluginMap;
    protected final EnumMap<DirectEntityType, List<RenderDataAPI>> directEntityMap = new EnumMap<>(DirectEntityType.class);
    protected final Set<TemporaryCleanupPlugin> cleanupPluginSet = ConcurrentHashMap.newKeySet(4);
    // manager resource
    protected final BUtil_AtomicEnumSet32<E> delayAdderLayer;
    protected final Deque<Runnable> deferredAdderQueue = new ConcurrentLinkedDeque<>();
    protected final Deque<Runnable> deferredGLCmdBeforeRendering = new ConcurrentLinkedDeque<>();
    protected final Deque<Runnable> deferredGLCmdBeginIllumination = new ConcurrentLinkedDeque<>();

    protected final Deque<Runnable> deferredGLCmdBeginAdvance = new ConcurrentLinkedDeque<>();
    protected final AtomicInteger barrierBits = new AtomicInteger(0);

    // custom data
    protected final ConcurrentMap<String, Object> concurrentCustomData = new ConcurrentHashMap<>(16);

    protected final Lock lock = new SpinLock();
    protected final Logger log;

    public BUtil_LayeredResourceTemplate(final Class<E> target) {
        final E[] enums = target.getEnumConstants();
        final boolean isCombatLayers;
        if (enums == null || !((isCombatLayers = enums[0] instanceof CombatEngineLayers) || enums[0] instanceof CampaignEngineLayers)) {
            throw new Error("Not desired layer class");
        }

        this.renderEntityMap = new EnumMap<>(target);
        this.layeredPluginMap = new EnumMap<>(target);
        this.delayAdderLayer = new BUtil_AtomicEnumSet32<>(target);
        this.log = Global.getLogger(isCombatLayers ? CombatRenderingManager.class : CampaignRenderingManager.class);
    }

    public void offerMemoryBarrier(int barriers) {
        if (!GLWrapper.Operation.Sync.valid_Barrier()) return;
        int curr, newValue;
        while (true) {
            curr = this.barrierBits.get();
            newValue = curr | barriers;
            if (this.barrierBits.compareAndSet(curr, newValue)) return;
            else Thread.onSpinWait();
        }
    }

    public void applyMemoryBarrier() {
        if (!GLWrapper.Operation.Sync.valid_Barrier()) return;
        int resetBarriers = GLWrapper.Operation.Sync.GL_BUFFER_UPDATE_BARRIER_BIT | GLWrapper.Operation.Sync.GL_SHADER_STORAGE_BARRIER_BIT;
        if (GLWrapper.Buffer.valid_BufferStorage()) resetBarriers |= GLWrapper.Operation.Sync.GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT;
        final int barriers = this.barrierBits.getAndSet(resetBarriers);
        GLWrapper.Operation.Sync.glMemoryBarrier(barriers);
    }

    protected abstract boolean isCampaign();

    public void cleanupIlluminant() {
        if (BUtil_GLImpl.checkIlluminantCleanupForShaderPacks()) {
            this.directEntityMap.forEach((type, list) -> {
                if (!type.isIlluminant() || list == null) return;
                for (var entity : list) {
                    if (entity == null || entity.hasDelete()) continue;
                    entity.delete();
                }
                list.clear();
            });
        }
    }

    protected abstract void cleanupAllVanillaPlugin();

    public void cleanupAllQueue() {
        this.lock.lock();
        for (var type : this.renderEntityMap.values()) {
            for (var list : type.values()) {
                if (list == null) continue;
                for (var entity : list) {
                    if (entity == null || entity.hasDelete()) continue;
                    entity.delete();
                }
                list.clear();
            }
        }

        for (var list : this.directEntityMap.values()) {
            if (list == null) continue;
            for (var entity : list) {
                if (entity == null || entity.hasDelete()) continue;
                entity.delete();
            }
            list.clear();
        }

        for (var set : this.layeredPluginMap.values()) {
            if (set == null) continue;
            for (var plugin : set) if (plugin != null) plugin.cleanup();
            set.clear();
        }

        final Consumer<TemporaryCleanupPlugin> invokeCleanupOnce = this.isCampaign() ? TemporaryCleanupPlugin::cleanupCampaignOnce : TemporaryCleanupPlugin::cleanupCombatOnce;
        for (var plugin : this.cleanupPluginSet) if (plugin != null) invokeCleanupOnce.accept(plugin);
        this.cleanupPluginSet.clear();

        this.cleanupAllVanillaPlugin();

        BUtil_GLImpl.resetTimer();
        this.lock.unlock();
        this.log.info("'BoxUtil' rendering plugin queue cleanup.");
    }

    protected abstract void checkRenderingPluginAndAdd(final E layer);

    public abstract void initBasicLayers();

    public void delayAdd() {
        this.lock.lock();
        this.delayAdderLayer.forEach(this::checkRenderingPluginAndAdd);
        this.delayAdderLayer.clear();
        this.lock.unlock();
        Runnable adder;
        while ((adder = this.deferredAdderQueue.poll()) != null) {
            adder.run();
        }
    }

    protected abstract void cleanupVanillaPlugin(final E layer);

    public void cleanupLayerQueue(final E layer) {
        this.lock.lock();
        final var toRemove = this.renderEntityMap.get(layer);
        if (toRemove != null) {
            for (List<RenderDataAPI> list : toRemove.values()) if (list != null) list.clear();
            toRemove.clear();
        }
        this.renderEntityMap.remove(layer);

        final var toRemoveSet = this.layeredPluginMap.get(layer);
        if (toRemoveSet != null) toRemoveSet.clear();
        this.layeredPluginMap.remove(layer);

        this.cleanupVanillaPlugin(layer);
        this.lock.unlock();
        this.log.info("'BoxUtil' rendering plugin cleanup: '" + layer.name() + '\'');
    }

    protected abstract @Nullable E layerCast(final Object layerRaw);

    public byte addEntity(@NotNull final RenderDataAPI entity) {
        final var layer = this.layerCast(entity.getLayer());
        List<RenderDataAPI> targetList;
        if (entity instanceof DirectDrawEntity) {
            final var type = (DirectEntityType) entity.entityType();
            if (type != null) {
                this.lock.lock();
                targetList = this.directEntityMap.computeIfAbsent(type, k -> new ArrayList<>(8));
                this.lock.unlock();
            } else targetList = null;
        } else {
            if (layer == null) return BoxEnum.STATE_FAILED;
            final var type = (LayeredEntityType) entity.entityType();
            if (type != null) {
                this.delayAdderLayer.add(layer);
                this.lock.lock();
                final var currMap = this.renderEntityMap.computeIfAbsent(layer, k -> new EnumMap<>(LayeredEntityType.class));
                targetList = currMap.computeIfAbsent(type, k -> new ArrayList<>(8));
                this.lock.unlock();
            } else targetList = null;
        }

        if (targetList != null) {
            this.deferredAdderQueue.offer(() -> {
                targetList.add(entity);
                BUtil_ResourceStorage.sharedResource().getSharedLogicalEntities().offer(entity);
            });
            return BoxEnum.STATE_SUCCESS;
        } else return BoxEnum.STATE_FAILED;
    }

    public boolean containsEntity(@Nullable final E layer, final RenderDataAPI entity) {
        List<RenderDataAPI> targetList;
        if (entity instanceof DirectDrawEntity) targetList = this.directEntityMap.get((DirectEntityType) entity.entityType());
        else targetList = this.renderEntityMap.get(layer).get((LayeredEntityType) entity.entityType());
        return targetList != null && targetList.contains(entity);
    }

    public boolean removeEntity(final RenderDataAPI entity) {
        entity.delete();
        return entity.hasDelete();
    }

    public void addRenderingPlugin(@NotNull final LayeredRenderingPlugin plugin, final EnumSet<E> targetLayers) {
        if (targetLayers == null) return;
        for (var layer : targetLayers) {
            if (layer == null) continue;
            this.delayAdderLayer.add(layer);
            this.lock.lock();
            this.layeredPluginMap.computeIfAbsent(layer, k -> ConcurrentHashMap.newKeySet(8));
            this.lock.unlock();
            this.deferredAdderQueue.offer(() -> this.layeredPluginMap.get(layer).add(plugin));
        }
    }

    public boolean containsRenderingPlugin(final E layer, final LayeredRenderingPlugin plugin) {
        if (!this.layeredPluginMap.containsKey(layer)) return false;
        return plugin != null && this.layeredPluginMap.get(layer).contains(plugin);
    }

    public boolean containsRenderingPlugin(final LayeredRenderingPlugin plugin) {
        if (plugin == null) return false;
        for (var set : this.layeredPluginMap.values()) if (set.contains(plugin)) return true;
        return false;
    }

    public void addCleanupPlugin(@NotNull final TemporaryCleanupPlugin plugin) {
        this.deferredAdderQueue.offer(() -> this.cleanupPluginSet.add(plugin));
    }

    public boolean containsCleanupPlugin(final TemporaryCleanupPlugin plugin) {
        return plugin != null && this.cleanupPluginSet.contains(plugin);
    }

    public boolean addStaticTrailTracker(@NotNull final StaticTrailData trailData, @Nullable final Object linkedEntity, final E layer, byte layerLoc, @NotNull final StaticTrailTracker tacker) {
        if (!BoxConfigs.isTrailSystemEnable()) return false;
        final boolean success = BUtil_StaticTrailMemoryPool.addTracker(trailData, linkedEntity, layerLoc, tacker);
        if (success) this.delayAdderLayer.add(layer);
        return success;
    }

    public void offerGLCmdBeforeRendering(final Runnable cmd) {
        this.deferredGLCmdBeforeRendering.offer(cmd);
    }

    public void processGLCmdBeforeRendering() {
        Runnable cmd;
        while ((cmd = this.deferredGLCmdBeforeRendering.poll()) != null) {
            cmd.run();
        }
    }

    public void offerGLCmdBeginIllumination(final Runnable cmd) {
        this.deferredGLCmdBeginIllumination.offer(cmd);
    }

    public void processGLCmdBeginIllumination() {
        Runnable cmd;
        while ((cmd = this.deferredGLCmdBeginIllumination.poll()) != null) {
            cmd.run();
        }
    }

    public void offerGLCmdBeginAdvance(final Runnable cmd) {
        this.deferredGLCmdBeginAdvance.offer(cmd);
    }

    public void processGLCmdBeginAdvance() {
        Runnable cmd;
        while ((cmd = this.deferredGLCmdBeginAdvance.poll()) != null) {
            cmd.run();
        }
    }

    public ConcurrentMap<String, Object> getCustomData() {
        return this.concurrentCustomData;
    }

    public void cleanupCustomData() {
        this.concurrentCustomData.clear();
    }

    public EnumMap<LayeredEntityType, List<RenderDataAPI>> getEntity(final E layer) {
        return this.renderEntityMap.get(layer);
    }

    public Set<LayeredRenderingPlugin> getLayerPlugin(final E layer) {
        return this.layeredPluginMap.get(layer);
    }

    public EnumMap<DirectEntityType, List<RenderDataAPI>> getDirectEntity() {
        return this.directEntityMap;
    }

    public Logger getLog() {
        return this.log;
    }
}
