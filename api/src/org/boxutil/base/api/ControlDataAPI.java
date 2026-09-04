package org.boxutil.base.api;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.config.BoxConfigs;

/**
 * Always available, even if {@link BoxConfigs#isShaderEnable()} at false.
 * @see RenderDataAPI#setControlData(ControlDataAPI)
 */
public interface ControlDataAPI {
    /**
     * Run once as long as linked to a render entity.
     */
    default void controlInit(@NotNull RenderDataAPI renderEntity) {}

    /**
     * Executes before the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    default void controlBeforeRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount) {}

    /**
     * Executes after the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    default void controlAfterRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount) {}

    /**
     * <b>Running on another thread.</b><p>
     * Executes in the logical advance.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    default void controlAdvance(@NotNull RenderDataAPI renderEntity, float amount) {}

    /**
     * Run once when call {@link RenderDataAPI#delete()}.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    default void controlRemove(@NotNull RenderDataAPI renderEntity) {}

    /**
     * Affects {@link org.boxutil.base.BaseRenderData#getGlobalTimerAlpha()}.
     */
    default boolean controlAlphaBasedTimer(@NotNull RenderDataAPI renderEntity) {
        return true;
    }

    /**
     * Will call {@link ControlDataAPI#controlRemove(RenderDataAPI)} when timer of entity is out.
     */
    default boolean controlRemoveBasedTimer(@NotNull RenderDataAPI renderEntity) {
        return true;
    }

    /**
     * Check it in each rendering frame.
     */
    default boolean controlCanRenderNow(@NotNull RenderDataAPI renderEntity) {
        return true;
    }

    /**
     * Affects {@link ControlDataAPI#controlAdvance(RenderDataAPI, float)} and compute instance data.
     */
    default boolean controlRunWhilePaused(@NotNull RenderDataAPI renderEntity) {
        return false;
    }

    /**
     * Force remove entity if true.
     */
    default boolean controlIsDone(@NotNull RenderDataAPI renderEntity) {
        return false;
    }

    /**
     * Force remove entity after init and render one frame.
     */
    default boolean controlIsOnceRender(@NotNull RenderDataAPI renderEntity) {
        return false;
    }
}
