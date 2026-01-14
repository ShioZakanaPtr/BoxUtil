package org.boxutil.backends.core;

import com.fs.starfarer.Version;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import org.apache.log4j.Logger;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.base.api.DirectDrawEntity;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.base.api.resource.TemporaryCleanupPlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.*;
import org.boxutil.manager.CampaignRenderingManager;
import org.boxutil.manager.CombatRenderingManager;
import org.boxutil.util.RenderingUtil;
import org.boxutil.util.concurrent.SpinLock;
import de.unkrig.commons.nullanalysis.NotNull;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class BUtil_ThreadResource {
    final static AtomicBoolean __SHOULD_ADVANCE_SYNC_CURRENT_FRAME = new AtomicBoolean(false);
    static float _CURR_AMOUNT = 0.0f;
    static boolean _CURR_PAUSED = false;


    // combat resource
    final static EnumMap<CombatEngineLayers, EnumMap<LayeredEntityType, List<RenderDataAPI>>> _COMBAT_ENTITIES_R = new EnumMap<>(CombatEngineLayers.class);
    final static EnumMap<CombatEngineLayers, Set<LayeredRenderingPlugin>> _COMBAT_LAYERED_PLUGIN = new EnumMap<>(CombatEngineLayers.class);
    final static EnumMap<DirectEntityType, List<RenderDataAPI>> _COMBAT_DIRECT_MAP = new EnumMap<>(DirectEntityType.class);
    final static Set<TemporaryCleanupPlugin> _COMBAT_CLEANUP_PLUGIN = new HashSet<>(4);
    // manager resource
    final static EnumSet<CombatEngineLayers> _COMBAT_ACTIVE_LAYER = EnumSet.noneOf(CombatEngineLayers.class);
    final static EnumSet<CombatEngineLayers> _COMBAT_DELAY_LAYER = EnumSet.noneOf(CombatEngineLayers.class);
    final static Deque<Consumer<Void>> _COMBAT_DELAY_ADD = new ConcurrentLinkedDeque<>();

    // combat custom data
    private final static ConcurrentMap<String, Object> _COMBAT_GLOBAL_MAP = new ConcurrentHashMap<>(16);


    // campaign resource
    final static EnumMap<CampaignEngineLayers, EnumMap<LayeredEntityType, List<RenderDataAPI>>> _CAMPAIGN_ENTITIES_R = new EnumMap<>(CampaignEngineLayers.class);
    final static EnumMap<CampaignEngineLayers, Set<LayeredRenderingPlugin>> _CAMPAIGN_LAYERED_PLUGIN = new EnumMap<>(CampaignEngineLayers.class);
    final static EnumMap<DirectEntityType, List<RenderDataAPI>> _CAMPAIGN_DIRECT_MAP = new EnumMap<>(DirectEntityType.class);
    final static Set<TemporaryCleanupPlugin> _CAMPAIGN_CLEANUP_PLUGIN = new HashSet<>(4);
    // manager resource
    final static EnumMap<CampaignEngineLayers, BUtil_CampaignRenderingPlugin> _CAMPAIGN_MANAGERS = new EnumMap<>(CampaignEngineLayers.class);
    final static EnumSet<CampaignEngineLayers> _CAMPAIGN_DELAY_LAYER = EnumSet.noneOf(CampaignEngineLayers.class);
    final static Deque<Consumer<Void>> _CAMPAIGN_DELAY_ADD = new ConcurrentLinkedDeque<>();

    // campaign custom data
    private final static ConcurrentMap<String, Object> _CAMPAIGN_GLOBAL_MAP = new ConcurrentHashMap<>(16);


    // shared logical advance
    private final static Deque<RenderDataAPI> _ENTITIES_L = new ConcurrentLinkedDeque<>();


    // thread process
    private final static Deque<Consumer<Void>> _SUBMIT_DEQUE = new ConcurrentLinkedDeque<>();
    private final static Deque<BackgroundEveryFramePlugin> _RENDERING_PLUGIN_DEQUE = new ConcurrentLinkedDeque<>();
    private final static Deque<BackgroundEveryFramePlugin> _LOGICAL_PLUGIN_DEQUE = new ConcurrentLinkedDeque<>();

    final static AtomicReference<GLSync> __SYNC_BEGIN_ADVANCE = new AtomicReference<>(null);
    final static AtomicReference<GLSync> __SYNC_FINISH_ADVANCE = new AtomicReference<>(null);
    final static AtomicReference<GLSync> __SYNC_AUX_BEGIN_ADVANCE = new AtomicReference<>(null);
    final static AtomicReference<GLSync> __SYNC_AUX_FINISH_ADVANCE = new AtomicReference<>(null);

    final static AtomicReference<GLSync> __SYNC_BEGIN_RENDERING = new AtomicReference<>(null);
    final static AtomicReference<GLSync> __SYNC_BEGIN_ILLUMINATION = new AtomicReference<>(null);
    final static AtomicReference<GLSync> __SYNC_AFTER_RENDERING = new AtomicReference<>(null);

    final static AtomicReference<GLSync> __SYNC_AFTER_RENDERING_HOST = new AtomicReference<>(null);
    final static AtomicReference<GLSync> __SYNC_AUX_AFTER_RENDERING_HOST = new AtomicReference<>(null);
    final static AtomicReference<GLSync> __SYNC_FINISH_ADVANCE_HOST = new AtomicReference<>(null);

    private static final Deque<Throwable> _THREAD_CATCH_EXCEPTION_COLLECTION = new ConcurrentLinkedDeque<>();
    private static volatile boolean _THREAD_CATCH_EXCEPTION = false;

    public final static class Logical {
        final static SpinLock __OP_LOCK = new SpinLock();

        static void runEntitySubmit(final boolean atLast) {
            final Deque<Consumer<Void>> queue = _SUBMIT_DEQUE;
            Consumer<Void> command;
            while ((command = atLast ? queue.pollLast() : queue.pollFirst()) != null) {
                command.accept(null);
            }
            if (BoxDatabase.getGLState().GL_GL43) GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
            if (BoxDatabase.getGLState().GL_GL42) GL42.glMemoryBarrier(GL42.GL_BUFFER_UPDATE_BARRIER_BIT);
        }

        static Deque<BackgroundEveryFramePlugin> getThreadPluginQueue() {
            return _LOGICAL_PLUGIN_DEQUE;
        }

        static void addAllThreadPlugin(Collection<? extends BackgroundEveryFramePlugin> c) {
            __OP_LOCK.lock();
            _LOGICAL_PLUGIN_DEQUE.addAll(c);
            __OP_LOCK.unlock();
        }

        static Deque<RenderDataAPI> getEntitiesLogicalQueue() {
            return _ENTITIES_L;
        }

        static void addAllEntitiesLogical(Collection<? extends RenderDataAPI> c) {
            __OP_LOCK.lock();
            _ENTITIES_L.addAll(c);
            __OP_LOCK.unlock();
        }

        public static void offerSubmitInstance(Consumer<Void> command) {
            _SUBMIT_DEQUE.offer(command);
        }

        public static ConcurrentMap<String, Object> getCombatCustomData() {
            return _COMBAT_GLOBAL_MAP;
        }

        public static ConcurrentMap<String, Object> getCampaignCustomData() {
            return _CAMPAIGN_GLOBAL_MAP;
        }

        private Logical() {}
    }

    public final static class Rendering {
        final static SpinLock __OP_LOCK = new SpinLock();

        static Deque<BackgroundEveryFramePlugin> getThreadPluginQueue() {
            return _RENDERING_PLUGIN_DEQUE;
        }

        private final static class Route {
            private static void _cleanupIlluminant(EnumMap<DirectEntityType, List<RenderDataAPI>> var0) {
                if (BUtil_GLImpl.Operations.checkIlluminantCleanupForShaderPacks()) {
                    var0.forEach((type, list) -> {
                        if (!type.isIlluminant() || list == null) return;
                        for (var entity : list) {
                            if (entity == null || entity.hasDelete()) continue;
                            entity.delete();
                        }
                        list.clear();
                    });
                }
            }

            private static void delayAdd(Deque<Consumer<Void>> var0) {
                Consumer<Void> adder;
                while ((adder = var0.poll()) != null) {
                    adder.accept(null);
                }
            }

            private Route() {}
        }

        public final static class Combat {
            final static Logger LOG = Global.getLogger(CombatRenderingManager.class);

            public static void _cleanupIlluminant() {
                Route._cleanupIlluminant(BUtil_ThreadResource._COMBAT_DIRECT_MAP);
            }

            public static void cleanupCustomData() {
                _COMBAT_GLOBAL_MAP.clear();
            }

            static void cleanupQueue() {
                __OP_LOCK.lock();
                for (var type : BUtil_ThreadResource._COMBAT_ENTITIES_R.values()) {
                    for (var list : type.values()) {
                        if (list == null) continue;
                        for (var entity : list) {
                            if (entity == null || entity.hasDelete()) continue;
                            entity.delete();
                        }
                        list.clear();
                    }
                }

                for (var list : BUtil_ThreadResource._COMBAT_DIRECT_MAP.values()) {
                    if (list == null) continue;
                    for (var entity : list) {
                        if (entity == null || entity.hasDelete()) continue;
                        entity.delete();
                    }
                    list.clear();
                }

                for (var set : BUtil_ThreadResource._COMBAT_LAYERED_PLUGIN.values()) {
                    if (set == null) continue;
                    for (var plugin : set) if (plugin != null) plugin.cleanup();
                    set.clear();
                }

                for (var plugin : BUtil_ThreadResource._COMBAT_CLEANUP_PLUGIN) if (plugin != null) plugin.cleanupCombatOnce();
                BUtil_ThreadResource._COMBAT_CLEANUP_PLUGIN.clear();
                BUtil_ThreadResource._COMBAT_ACTIVE_LAYER.clear();

                BUtil_GLImpl.Operations.resetTimer();
                __OP_LOCK.unlock();
                LOG.info("'BoxUtil' Combat rendering plugin queue cleanup.");
            }

            private static void checkRenderingPluginAndAdd(CombatEngineLayers layer) {
                if (!_COMBAT_ACTIVE_LAYER.contains(layer)) {
                    _COMBAT_ACTIVE_LAYER.add(layer);
                    Global.getCombatEngine().addLayeredRenderingPlugin(new BUtil_CombatEFS.Renderer(layer));
                    LOG.info("'BoxUtil' Combat rendering plugin join layer: '" + layer.name() + '\'');
                }
            }

            static void initBasicLayers() {
                __OP_LOCK.lock();
                checkRenderingPluginAndAdd(RenderingUtil.getLowestCombatLayer());
                checkRenderingPluginAndAdd(RenderingUtil.getHighestCombatLayer());
                __OP_LOCK.unlock();
            }

            static void delayAdd() {
                __OP_LOCK.lock();
                for (CombatEngineLayers layer : _COMBAT_DELAY_LAYER) checkRenderingPluginAndAdd(layer);
                _COMBAT_DELAY_LAYER.clear();
                __OP_LOCK.unlock();
                Route.delayAdd(BUtil_ThreadResource._COMBAT_DELAY_ADD);
            }

            static void cleanupLayerQueue(CombatEngineLayers layer) {
                __OP_LOCK.lock();
                final var toRemove = BUtil_ThreadResource._COMBAT_ENTITIES_R.get(layer);
                if (toRemove != null) {
                    for (List<RenderDataAPI> list : toRemove.values()) if (list != null) list.clear();
                    toRemove.clear();
                }
                BUtil_ThreadResource._COMBAT_ENTITIES_R.remove(layer);

                final var toRemoveSet = BUtil_ThreadResource._COMBAT_LAYERED_PLUGIN.get(layer);
                if (toRemoveSet != null) toRemoveSet.clear();
                BUtil_ThreadResource._COMBAT_LAYERED_PLUGIN.remove(layer);

                BUtil_ThreadResource._COMBAT_ACTIVE_LAYER.remove(layer);
                __OP_LOCK.unlock();
                LOG.info("'BoxUtil' Combat rendering plugin cleanup: '" + layer.name() + '\'');
            }

            public static byte addEntity(@NotNull RenderDataAPI entity) {
                final var layer = (CombatEngineLayers) entity.getLayer();
                List<RenderDataAPI> targetList;
                if (entity instanceof DirectDrawEntity) {
                    final var type = (DirectEntityType) entity.entityType();
                    if (type != null) {
                        __OP_LOCK.lock();
                        targetList = BUtil_ThreadResource._COMBAT_DIRECT_MAP.computeIfAbsent(type, k -> new ArrayList<>(8));
                        __OP_LOCK.unlock();
                    } else targetList = null;
                } else {
                    if (layer == null) return BoxEnum.STATE_FAILED;
                    final var type = (LayeredEntityType) entity.entityType();
                    if (type != null) {
                        __OP_LOCK.lock();
                        _COMBAT_DELAY_LAYER.add(layer);
                        final var currMap = BUtil_ThreadResource._COMBAT_ENTITIES_R.computeIfAbsent(layer, k -> new EnumMap<>(LayeredEntityType.class));
                        targetList = currMap.computeIfAbsent(type, k -> new ArrayList<>(8));
                        __OP_LOCK.unlock();
                    } else targetList = null;
                }

                if (targetList != null) {
                    BUtil_ThreadResource._COMBAT_DELAY_ADD.offer(unused -> {
                        targetList.add(entity);
                        _ENTITIES_L.offer(entity);
                    });
                    return BoxEnum.STATE_SUCCESS;
                } else return BoxEnum.STATE_FAILED;
            }

            public static boolean containsEntity(@Nullable CombatEngineLayers layer, RenderDataAPI entity) {
                List<RenderDataAPI> targetList;
                if (entity instanceof DirectDrawEntity) targetList = BUtil_ThreadResource._COMBAT_DIRECT_MAP.get((DirectEntityType) entity.entityType());
                else targetList = BUtil_ThreadResource._COMBAT_ENTITIES_R.get(layer).get((LayeredEntityType) entity.entityType());
                return targetList != null && targetList.contains(entity);
            }

            public static void addRenderingPlugin(@NotNull LayeredRenderingPlugin plugin) {
                for (var layer : plugin.getCombatActiveLayers()) {
                    __OP_LOCK.lock();
                    _COMBAT_DELAY_LAYER.add(layer);
                    _COMBAT_LAYERED_PLUGIN.computeIfAbsent(layer, k -> new HashSet<>(8));
                    __OP_LOCK.unlock();
                    BUtil_ThreadResource._COMBAT_DELAY_ADD.offer(unused -> _COMBAT_LAYERED_PLUGIN.get(layer).add(plugin));
                }
            }

            public static boolean containsRenderingPlugin(CombatEngineLayers layer, LayeredRenderingPlugin plugin) {
                if (!_COMBAT_LAYERED_PLUGIN.containsKey(layer)) return false;
                return plugin != null && _COMBAT_LAYERED_PLUGIN.get(layer).contains(plugin);
            }

            public static boolean containsRenderingPlugin(LayeredRenderingPlugin plugin) {
                if (plugin == null) return false;
                for (var set : _COMBAT_LAYERED_PLUGIN.values()) if (set.contains(plugin)) return true;
                return false;
            }

            public static void addCleanupPlugin(@NotNull TemporaryCleanupPlugin plugin) {
                BUtil_ThreadResource._COMBAT_DELAY_ADD.offer(unused -> _COMBAT_CLEANUP_PLUGIN.add(plugin));
            }

            public static boolean containsCleanupPlugin(TemporaryCleanupPlugin plugin) {
                return plugin != null && _COMBAT_CLEANUP_PLUGIN.contains(plugin);
            }

            private Combat() {}
        }

        public final static class Campaign {
            final static Logger LOG = Global.getLogger(CampaignRenderingManager.class);

            public static void _cleanupIlluminant() {
                Route._cleanupIlluminant(BUtil_ThreadResource._CAMPAIGN_DIRECT_MAP);
            }

            public static void cleanupCustomData() {
                _CAMPAIGN_GLOBAL_MAP.clear();
            }

            public static void cleanupQueue() {
                __OP_LOCK.lock();
                for (var type : BUtil_ThreadResource._CAMPAIGN_ENTITIES_R.values()) {
                    for (var list : type.values()) {
                        if (list == null) continue;
                        for (var entity : list) {
                            if (entity == null || entity.hasDelete()) continue;
                            entity.delete();
                        }
                        list.clear();
                    }
                }

                for (var list : BUtil_ThreadResource._CAMPAIGN_DIRECT_MAP.values()) {
                    if (list == null) continue;
                    for (var entity : list) {
                        if (entity == null || entity.hasDelete()) continue;
                        entity.delete();
                    }
                    list.clear();
                }

                for (var set : BUtil_ThreadResource._CAMPAIGN_LAYERED_PLUGIN.values()) {
                    if (set == null) continue;
                    for (var plugin : set) if (plugin != null) plugin.cleanup();
                    set.clear();
                }

                for (var plugin : BUtil_ThreadResource._CAMPAIGN_CLEANUP_PLUGIN) if (plugin != null) plugin.cleanupCampaignOnce();
                BUtil_ThreadResource._CAMPAIGN_CLEANUP_PLUGIN.clear();

                for (var plugin : BUtil_ThreadResource._CAMPAIGN_MANAGERS.values()) if (plugin != null) plugin.destroy();
                BUtil_ThreadResource._CAMPAIGN_MANAGERS.clear();

                BUtil_GLImpl.Operations.resetTimer();
                __OP_LOCK.unlock();
                LOG.info("'BoxUtil' Campaign rendering plugin queue cleanup.");
            }

            private static void checkRenderingPluginAndAdd(CampaignEngineLayers layer) {
                final var location = Global.getSector() != null && Global.getSector().getPlayerFleet() != null ? Global.getSector().getPlayerFleet().getContainingLocation() : null;
                if (location == null) {
                    LOG.error("'BoxUtil' Campaign rendering plugin failed to join layer: '" + layer.name() + "' cause player fleet cannot be found.");
                    return;
                }
                if (!BUtil_ThreadResource._CAMPAIGN_MANAGERS.containsKey(layer)) {
                    final var id = location.getId() + '_' + BoxDatabase.CAMPAIGN_MANAGE_ID + '_' + layer.name();
                    final var entity = location.addCustomEntity(id, id, BoxDatabase.CAMPAIGN_MANAGE_ID, Factions.NEUTRAL);
                    entity.addTag(BoxDatabase.CAMPAIGN_MANAGE_TAG);
                    entity.setDiscoverable(false);
                    entity.setActiveLayers(layer);
                    if (entity.getCustomPlugin() instanceof BUtil_CampaignRenderingPlugin plugin) {
                        plugin.initLayer(layer);
                        BUtil_ThreadResource._CAMPAIGN_MANAGERS.put(layer, plugin);
                        LOG.info("'BoxUtil' Campaign rendering plugin join layer: '" + layer.name() + '\'');
                    } else {
                        location.removeEntity(entity);
                        entity.setExpired(true);
                        LOG.error("'BoxUtil' Campaign rendering plugin failed to join layer: '" + layer.name() + "' cause plugin has been damaged.");
                    }
                }
            }

            static void initBasicLayers() {
                __OP_LOCK.lock();
                checkRenderingPluginAndAdd(RenderingUtil.getLowestCampaignLayer());
                checkRenderingPluginAndAdd(RenderingUtil.getHighestCampaignLayer());
                __OP_LOCK.unlock();
            }

            static void delayAdd() {
                __OP_LOCK.lock();
                for (CampaignEngineLayers layer : _CAMPAIGN_DELAY_LAYER) checkRenderingPluginAndAdd(layer);
                _CAMPAIGN_DELAY_LAYER.clear();
                __OP_LOCK.unlock();
                Route.delayAdd(BUtil_ThreadResource._CAMPAIGN_DELAY_ADD);
            }

            static void cleanupLayerQueue(CampaignEngineLayers layer) {
                __OP_LOCK.lock();
                final var toRemoveMap = BUtil_ThreadResource._CAMPAIGN_ENTITIES_R.get(layer);
                if (toRemoveMap != null) {
                    for (List<RenderDataAPI> list : toRemoveMap.values()) if (list != null) list.clear();
                    toRemoveMap.clear();
                }
                BUtil_ThreadResource._CAMPAIGN_ENTITIES_R.remove(layer);

                final var toRemoveSet = BUtil_ThreadResource._CAMPAIGN_LAYERED_PLUGIN.get(layer);
                if (toRemoveSet != null) toRemoveSet.clear();
                BUtil_ThreadResource._CAMPAIGN_LAYERED_PLUGIN.remove(layer);

                final var removedPlugin = BUtil_ThreadResource._CAMPAIGN_MANAGERS.remove(layer);
                if (removedPlugin != null) removedPlugin.destroy();
                __OP_LOCK.unlock();
                LOG.info("'BoxUtil' Campaign rendering plugin cleanup: '" + layer.name() + '\'');
            }

            public static byte addEntity(@NotNull RenderDataAPI entity) {
                final var layer = (CampaignEngineLayers) entity.getLayer();
                List<RenderDataAPI> targetList;
                if (entity instanceof DirectDrawEntity) {
                    final var type = (DirectEntityType) entity.entityType();
                    if (type != null) {
                        __OP_LOCK.lock();
                        targetList = BUtil_ThreadResource._CAMPAIGN_DIRECT_MAP.computeIfAbsent(type, k -> new ArrayList<>(8));
                        __OP_LOCK.unlock();
                    } else targetList = null;
                } else {
                    if (layer == null) return BoxEnum.STATE_FAILED;
                    final var type = (LayeredEntityType) entity.entityType();
                    if (type != null) {
                        __OP_LOCK.lock();
                        _CAMPAIGN_DELAY_LAYER.add(layer);
                        final var currMap = BUtil_ThreadResource._CAMPAIGN_ENTITIES_R.computeIfAbsent(layer, k -> new EnumMap<>(LayeredEntityType.class));
                        targetList = currMap.computeIfAbsent(type, k -> new ArrayList<>(8));
                        __OP_LOCK.unlock();
                    } else targetList = null;
                }
                if (targetList != null) {
                    BUtil_ThreadResource._CAMPAIGN_DELAY_ADD.offer(unused -> {
                        targetList.add(entity);
                        _ENTITIES_L.offer(entity);;
                    });
                    return BoxEnum.STATE_SUCCESS;
                } else return BoxEnum.STATE_FAILED;
            }

            public static boolean containsEntity(@Nullable CampaignEngineLayers layer, RenderDataAPI entity) {
                List<RenderDataAPI> targetList;
                if (entity instanceof DirectDrawEntity) targetList = BUtil_ThreadResource._CAMPAIGN_DIRECT_MAP.get((DirectEntityType) entity.entityType());
                else targetList = BUtil_ThreadResource._CAMPAIGN_ENTITIES_R.get(layer).get((LayeredEntityType) entity.entityType());
                return targetList != null && targetList.contains(entity);
            }

            public static void addRenderingPlugin(@NotNull LayeredRenderingPlugin plugin) {
                for (var layers : plugin.getCampaignActiveLayers()) {
                    __OP_LOCK.lock();
                    _CAMPAIGN_DELAY_LAYER.add(layers);
                    _CAMPAIGN_LAYERED_PLUGIN.computeIfAbsent(layers, k -> new HashSet<>(8));
                    __OP_LOCK.unlock();
                    BUtil_ThreadResource._CAMPAIGN_DELAY_ADD.offer(unused -> _CAMPAIGN_LAYERED_PLUGIN.get(layers).add(plugin));
                }
            }

            public static boolean containsRenderingPlugin(CampaignEngineLayers layer, LayeredRenderingPlugin plugin) {
                if (!_CAMPAIGN_LAYERED_PLUGIN.containsKey(layer)) return false;
                return plugin != null && _CAMPAIGN_LAYERED_PLUGIN.get(layer).contains(plugin);
            }

            public static boolean containsRenderingPlugin(LayeredRenderingPlugin plugin) {
                if (plugin == null) return false;
                for (var set : _CAMPAIGN_LAYERED_PLUGIN.values()) if (set.contains(plugin)) return true;
                return false;
            }

            public static void addCleanupPlugin(@NotNull TemporaryCleanupPlugin plugin) {
                BUtil_ThreadResource._CAMPAIGN_DELAY_ADD.offer(unused -> _CAMPAIGN_CLEANUP_PLUGIN.add(plugin));
            }

            public static boolean containsCleanupPlugin(TemporaryCleanupPlugin plugin) {
                return plugin != null && _CAMPAIGN_CLEANUP_PLUGIN.contains(plugin);
            }

            private Campaign() {}
        }

        private Rendering() {}
    }

    static void pushThreadException(final Throwable exception) {
        _THREAD_CATCH_EXCEPTION_COLLECTION.offerLast(exception);
        BUtil_ThreadResource._THREAD_CATCH_EXCEPTION = true;
    }

    static void checkShouldCloseGame() {
        if (BUtil_ThreadResource._THREAD_CATCH_EXCEPTION) {
            StringBuilder eStr = new StringBuilder();
            Throwable e;
            while ((e = _THREAD_CATCH_EXCEPTION_COLLECTION.pollFirst()) != null) {
                eStr.append('\t').append(e.getClass().getName()).append(": ").append(e.getMessage()).append('\n');
            }
            Sys.alert(Version.versionString, BoxConfigs.getString("BUtil_ThreadAlertMessage").replace("%s", eStr));
            System.exit(1);
        }
    }

    static void sendGLSync(AtomicReference<GLSync> target) {
        if (!BoxDatabase.getGLState().GL_CORE_SYNC) return;
        final GLSync sync = GL32.glFenceSync(GL32.GL_SYNC_GPU_COMMANDS_COMPLETE, 0);
        if (target.compareAndSet(null, sync)) GL11.glFlush();
        else GL32.glDeleteSync(sync);
    }

    static void tryGLSync(AtomicReference<GLSync> target) {
        if (!BoxDatabase.getGLState().GL_CORE_SYNC) return;
        GLSync glSync = target.getAndSet(null);
        if (glSync != null) {
            GL32.glWaitSync(glSync, 0, GL32.GL_TIMEOUT_IGNORED);
            GL32.glDeleteSync(glSync);
        }
    }

    public static boolean offerBackgroundRenderingPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return _RENDERING_PLUGIN_DEQUE.offer(plugin);
    }

    public static boolean containsBackgroundRenderingPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return _RENDERING_PLUGIN_DEQUE.contains(plugin);
    }

    public static boolean offerBackgroundLogicalPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return _LOGICAL_PLUGIN_DEQUE.offer(plugin);
    }

    public static boolean containsBackgroundLogicalPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return _LOGICAL_PLUGIN_DEQUE.contains(plugin);
    }

    private BUtil_ThreadResource() {}
}
