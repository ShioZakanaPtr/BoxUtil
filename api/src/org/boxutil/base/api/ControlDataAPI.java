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
    void controlInit(@NotNull RenderDataAPI renderEntity);

    /**
     * Executes before the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    void controlBeforeRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount);

    /**
     * Executes after the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    void controlAfterRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount);

    /**
     * <strong>Running on another thread.</strong><p>
     * Executes in the logical advance.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    void controlAdvance(@NotNull RenderDataAPI renderEntity, float amount);

    /**
     * Run once when call {@link RenderDataAPI#delete()}.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    void controlRemove(@NotNull RenderDataAPI renderEntity);

    /**
     * Affects {@link org.boxutil.base.BaseRenderData#getGlobalTimerAlpha()}.
     */
    boolean controlAlphaBasedTimer(@NotNull RenderDataAPI renderEntity);

    /**
     * Will call {@link ControlDataAPI#controlRemove(RenderDataAPI)} when timer of entity is out.
     */
    boolean controlRemoveBasedTimer(@NotNull RenderDataAPI renderEntity);

    /**
     * Check it in each rendering frame.
     */
    boolean controlCanRenderNow(@NotNull RenderDataAPI renderEntity);

    /**
     * Affects {@link ControlDataAPI#controlAdvance(RenderDataAPI, float)} and compute instance data.
     */
    boolean controlRunWhilePaused(@NotNull RenderDataAPI renderEntity);

    /**
     * Force remove entity if true.
     */
    boolean controlIsDone(@NotNull RenderDataAPI renderEntity);

    /**
     * Force remove entity after init and render one frame.
     */
    boolean controlIsOnceRender(@NotNull RenderDataAPI renderEntity);
}
