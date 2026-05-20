package org.boxutil.manager;

import com.fs.starfarer.api.combat.*;
import org.boxutil.backends.core.BUtil_ThreadResource;
import org.boxutil.base.api.*;
import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.units.standard.attribute.StaticTrailData;
import org.boxutil.base.api.resource.TemporaryCleanupPlugin;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.define.BoxEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

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
     * @return returns <code>true</code> if this entity has finished deleted.
     */
    public static boolean removeEntity(RenderDataAPI entity) {
        return BUtil_ThreadResource.Rendering.Combat.removeEntity(entity);
    }

    /**
     * @param locationTacker the input is current frame time <code>amount</code> in second;<p>the returns were current frame state that <code>{location.x, location.y, facingVector.x, facingVector.y}</code>;<p>will remove this trail tracker when returns <code>null</code>.
     *
     * @return returns <code>true</code> when trail data was existed, else <code>false</code> if not exist.
     */
    public static boolean addStaticTrailGenerator(@NotNull final StaticTrailData trailData, @NotNull final CombatEngineLayers layer, @NotNull final Function<Float, Vector4f> locationTacker) {
        // todo
        return false;
    }

    /**
     * @return returns <code>true</code> when trail data was existed before call this method, else <code>false</code> if not exist.
     */
    public static boolean removeStaticTrailGenerator(@NotNull final StaticTrailData trailData) {
        return false;
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
