package org.boxutil.manager;

import com.fs.starfarer.api.combat.*;
import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.base.api.*;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.base.api.resource.StaticTrailTracker;
import org.boxutil.define.struct.statictrail.StaticTrailData;
import org.boxutil.base.api.resource.TemporaryCleanupPlugin;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.define.BoxEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Matrix4f;

import java.util.concurrent.ConcurrentMap;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class CombatRenderingManager {
    /**
     * <b>NOTE: If entity at highest layer, without any post effect likes bloom or AA.</b>
     *
     * @param target useless parameter now.
     * @return return {@link BoxEnum#STATE_SUCCESS} when entity valid, return {@link BoxEnum#STATE_FAILED} when entity adding failed.
     */
    @Deprecated
    public static byte addEntity(byte target, @NotNull RenderDataAPI entity) {
        return BUtil_ResourceStorage.combatLayered().addEntity(entity);
    }

    /**
     * <b>NOTE: If entity at highest layer, without any post effect likes bloom or AA.</b>
     *
     * @return return {@link BoxEnum#STATE_SUCCESS} when entity valid, return {@link BoxEnum#STATE_FAILED} when entity adding failed.
     */
    public static byte addEntity(@NotNull RenderDataAPI entity) {
        return BUtil_ResourceStorage.combatLayered().addEntity(entity);
    }

    /**
     * @param target useless parameter now.
     */
    @Deprecated
    public static boolean containsEntity(CombatEngineLayers layer, byte target, RenderDataAPI entity) {
        return BUtil_ResourceStorage.combatLayered().containsEntity(layer, entity);
    }

    /**
     * @param layer <code>null</code> for any direct draw entity.
     */
    public static boolean containsEntity(@Nullable CombatEngineLayers layer, RenderDataAPI entity) {
        return BUtil_ResourceStorage.combatLayered().containsEntity(layer, entity);
    }

    /**
     * @return returns <code>true</code> if this entity has finished deleted.
     */
    public static boolean removeEntity(RenderDataAPI entity) {
        return BUtil_ResourceStorage.combatLayered().removeEntity(entity);
    }

    /**
     * @param trailData will submit once automatically if this trail data is not exist in system before add.
     * @param linkedEntity for declare this trail subordinate to the entity.
     *
     * @return returns <code>true</code> when trail data adding success, else <code>false</code> if it not.
     */
    public static boolean addStaticTrail(@NotNull final StaticTrailData trailData, @Nullable final CombatEntityAPI linkedEntity, @NotNull final CombatEngineLayers layer, @NotNull final StaticTrailTracker tacker) {
        return BUtil_ResourceStorage.combatLayered().addStaticTrailTracker(trailData, linkedEntity, layer, BUtil_StaticTrailMemoryPool.toLayerLoc(layer), tacker);
    }

    /**
     * Call if something changes(include material) for trail data in this frame if needed, only once executed in each frame and have active trail tracker.
     */
    public static void submitTrailDataChanges(@NotNull final StaticTrailData trailData) {
        BUtil_StaticTrailMemoryPool.submitTrailDataChanges(trailData);
    }

    /**
     * Notifies all the trails that the linked entity have to cut off the current trail segment before the next draw, so that it will be treated as a new trail segment.<p>
     * For performance and thread‑safety reasons,
     * this method should be called after the start of the <b>advance</b> stage of the current frame <b>AND</b> before the start of the <b>lowest layer rendering</b> stage of the next frame,
     * because it was deferred execution before rendering; otherwise, unexpected visual effects may occur.<p>
     * <b>NOTE:</b> Since a trail can be attached to any object independently (or no attached),
     * even though this entity may visually have any number of trails,
     * it is not guaranteed that this entity is the actual subject to which those trails were linked at creation time.
     */
    public static void tryCutTrailOnEntity(@NotNull final CombatEntityAPI linkedEntity) {
        BUtil_StaticTrailMemoryPool.offerCutTrailOnEntity(linkedEntity);
    }

    /**
     * Notifies all trails associated with this entity to enter the destruction process.<p>
     * For performance and thread‑safety reasons,
     * this method should be called after the start of the <b>advance</b> stage of the current frame <b>AND</b> before the start of the <b>lowest layer rendering</b> stage of the next frame,
     * because it was deferred execution before rendering; otherwise, unexpected visual effects may occur.<p>
     * <b>NOTE:</b> Since a trail can be attached to any object independently (or no attached),
     * even though this entity may visually have any number of trails,
     * it is not guaranteed that this entity is the actual subject to which those trails were linked at creation time.
     *
     * @param isImmediate  notifies with {@link StaticTrailTracker.Result#destroy()} when <code>false</code>, otherwise {@link StaticTrailTracker.Result#destroyImmediate()}
     */
    public static void tryRemoveTrailOnEntity(@NotNull final CombatEntityAPI linkedEntity, boolean isImmediate) {
        BUtil_StaticTrailMemoryPool.offerRemoveTrailOnEntity(linkedEntity, isImmediate);
    }

    /**
     * Directly remove the all the trail about this trail data, ignore layer or lifetime.
     *
     * @return returns <code>true</code> when trail data was existed before call this method, else <code>false</code> if it not.
     */
    public static boolean removeStaticTrail(@NotNull final StaticTrailData trailData) {
        return BUtil_StaticTrailMemoryPool.removeTracker(trailData, false);
    }

    /**
     * @param plugin you should not add lots of plugin, and you must know where the layer you want to render.
     */
    public static void addRenderingPlugin(@NotNull LayeredRenderingPlugin plugin) {
        BUtil_ResourceStorage.combatLayered().addRenderingPlugin(plugin, plugin.getCombatActiveLayers());
    }

    public static boolean containsRenderingPlugin(CombatEngineLayers layer, LayeredRenderingPlugin plugin) {
        return BUtil_ResourceStorage.combatLayered().containsRenderingPlugin(layer, plugin);
    }

    public static boolean containsRenderingPlugin(LayeredRenderingPlugin plugin) {
        return BUtil_ResourceStorage.combatLayered().containsRenderingPlugin(plugin);
    }

    public static boolean addBackgroundRenderingPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ResourceStorage.sharedResource().offerBackgroundRenderingPlugin(plugin);
    }

    public static boolean containsBackgroundRenderingPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ResourceStorage.sharedResource().containsBackgroundRenderingPlugin(plugin);
    }

    public static boolean addBackgroundLogicalPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ResourceStorage.sharedResource().offerBackgroundLogicalPlugin(plugin);
    }

    public static boolean containsBackgroundLogicalPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ResourceStorage.sharedResource().containsBackgroundLogicalPlugin(plugin);
    }

    public static void addCleanupPlugin(@NotNull TemporaryCleanupPlugin plugin) {
        BUtil_ResourceStorage.combatLayered().addCleanupPlugin(plugin);
    }

    public static boolean containsCleanupPlugin(TemporaryCleanupPlugin plugin) {
        return BUtil_ResourceStorage.combatLayered().containsCleanupPlugin(plugin);
    }

    /**
     * Will auto cleanup after combat over.
     */
    public static ConcurrentMap<String, Object> getCustomData() {
        return BUtil_ResourceStorage.combatLayered().getCustomData();
    }

    public static Matrix4f getGameOrthoViewport() {
        return BUtil_GLImpl.getGameOrthoViewport(BoxEnum.FALSE);
    }

    public static Matrix4f getGamePerspectiveViewport() {
        return BUtil_GLImpl.getGamePerspectiveViewport(BoxEnum.FALSE);
    }

    public static float getTimeFraction() {
        return BUtil_GLImpl.getElapsedTimeFraction();
    }

    public static float getTimeFractionIncludePaused() {
        return BUtil_GLImpl.getElapsedTimeFractionIncludePaused();
    }

    private CombatRenderingManager() {}
}
