package org.boxutil.manager;

import com.fs.starfarer.api.combat.*;
import org.boxutil.backends.core.BUtil_ThreadResource;
import org.boxutil.base.api.*;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.base.api.resource.TemporaryCleanupPlugin;
import org.boxutil.backends.shader.BUtil_GLImpl;
import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.define.BoxEnum;
import de.unkrig.commons.nullanalysis.Nullable;
import org.lwjgl.util.vector.Matrix4f;

import java.util.concurrent.ConcurrentMap;

public final class CombatRenderingManager {
    /**
     * <strong>NOTE: If entity at highest layer, without any post effect likes bloom or AA.</strong>
     *
     * @param target useless parameter now.
     * @return return {@link BoxEnum#STATE_SUCCESS} when entity valid, return {@link BoxEnum#STATE_FAILED} when entity adding failed.
     */
    @Deprecated
    public static byte addEntity(byte target, @NotNull RenderDataAPI entity) {
        return BUtil_ThreadResource.Rendering.Combat.addEntity(entity);
    }

    /**
     * <strong>NOTE: If entity at highest layer, without any post effect likes bloom or AA.</strong>
     *
     * @return return {@link BoxEnum#STATE_SUCCESS} when entity valid, return {@link BoxEnum#STATE_FAILED} when entity adding failed.
     */
    public static byte addEntity(@NotNull RenderDataAPI entity) {
        return BUtil_ThreadResource.Rendering.Combat.addEntity(entity);
    }

    /**
     * @param target useless parameter now.
     */
    @Deprecated
    public static boolean containsEntity(CombatEngineLayers layer, byte target, RenderDataAPI entity) {
        return BUtil_ThreadResource.Rendering.Combat.containsEntity(layer, entity);
    }

    /**
     * @param layer <code>null</code> for any direct draw entity.
     */
    public static boolean containsEntity(@Nullable CombatEngineLayers layer, RenderDataAPI entity) {
        return BUtil_ThreadResource.Rendering.Combat.containsEntity(layer, entity);
    }

    /**
     * @param plugin you should not add lots of plugin, and you must know where the layer you want to render.
     */
    public static void addRenderingPlugin(@NotNull LayeredRenderingPlugin plugin) {
        BUtil_ThreadResource.Rendering.Combat.addRenderingPlugin(plugin);
    }

    public static boolean containsRenderingPlugin(CombatEngineLayers layer, LayeredRenderingPlugin plugin) {
        return BUtil_ThreadResource.Rendering.Combat.containsRenderingPlugin(layer, plugin);
    }

    public static boolean containsRenderingPlugin(LayeredRenderingPlugin plugin) {
        return BUtil_ThreadResource.Rendering.Combat.containsRenderingPlugin(plugin);
    }

    public static boolean addBackgroundRenderingPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ThreadResource.offerBackgroundRenderingPlugin(plugin);
    }

    public static boolean containsBackgroundRenderingPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ThreadResource.containsBackgroundRenderingPlugin(plugin);
    }

    public static boolean addBackgroundLogicalPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ThreadResource.offerBackgroundLogicalPlugin(plugin);
    }

    public static boolean containsBackgroundLogicalPlugin(@NotNull BackgroundEveryFramePlugin plugin) {
        return BUtil_ThreadResource.containsBackgroundLogicalPlugin(plugin);
    }

    public static void addCleanupPlugin(@NotNull TemporaryCleanupPlugin plugin) {
        BUtil_ThreadResource.Rendering.Combat.addCleanupPlugin(plugin);
    }

    public static boolean containsCleanupPlugin(TemporaryCleanupPlugin plugin) {
        return BUtil_ThreadResource.Rendering.Combat.containsCleanupPlugin(plugin);
    }

    /**
     * Will auto cleanup after combat over.
     */
    public static ConcurrentMap<String, Object> getCustomData() {
        return BUtil_ThreadResource.Logical.getCombatCustomData();
    }

    public static Matrix4f getGameOrthoViewport() {
        return BUtil_GLImpl.getGameOrthoViewport(BoxEnum.FALSE);
    }

    public static Matrix4f getGamePerspectiveViewport() {
        return BUtil_GLImpl.getGamePerspectiveViewport(BoxEnum.FALSE);
    }

    public static float getTimeFraction() {
        return BUtil_GLImpl.Operations.getElapsedTimeFraction();
    }

    public static float getTimeFractionIncludePaused() {
        return BUtil_GLImpl.Operations.getElapsedTimeFractionIncludePaused();
    }

    private CombatRenderingManager() {}
}
