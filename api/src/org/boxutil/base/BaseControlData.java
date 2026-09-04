package org.boxutil.base;

import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.api.ControlDataAPI;
import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.config.BoxConfigs;

/**
 * Always available, even if {@link BoxConfigs#isShaderEnable()} at false.
 * @see RenderDataAPI#setControlData(ControlDataAPI)
 */
@Deprecated
public abstract class BaseControlData implements ControlDataAPI {
    /**
     * Run once as long as linked to a render entity.
     */
    public void controlInit(@NotNull RenderDataAPI renderEntity) {
        ControlDataAPI.super.controlInit(renderEntity);
    }

    /**
     * Executes before the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    public void controlBeforeRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount) {
        ControlDataAPI.super.controlBeforeRenderingAdvance(renderEntity, lastFrameAmount);
    }

    /**
     * Executes after the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    public void controlAfterRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount) {
        ControlDataAPI.super.controlAfterRenderingAdvance(renderEntity, lastFrameAmount);
    }

    /**
     * <b>Running on another thread.</b><p>
     * Executes in the logical advance.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    public void controlAdvance(@NotNull RenderDataAPI renderEntity, float amount) {
        ControlDataAPI.super.controlAdvance(renderEntity, amount);
    }

    /**
     * Run once when call {@link RenderDataAPI#delete()}.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    public void controlRemove(@NotNull RenderDataAPI renderEntity) {
        ControlDataAPI.super.controlRemove(renderEntity);
    }

    public boolean controlAlphaBasedTimer(@NotNull RenderDataAPI renderEntity) {
        return ControlDataAPI.super.controlAlphaBasedTimer(renderEntity);
    }

    /**
     * Will call {@link ControlDataAPI#controlRemove(RenderDataAPI)} when timer of entity is out.
     */
    public boolean controlRemoveBasedTimer(@NotNull RenderDataAPI renderEntity) {
        return ControlDataAPI.super.controlRemoveBasedTimer(renderEntity);
    }

    /**
     * Check it in each rendering frame.
     */
    public boolean controlCanRenderNow(@NotNull RenderDataAPI renderEntity) {
        return ControlDataAPI.super.controlCanRenderNow(renderEntity);
    }

    /**
     * Affects {@link ControlDataAPI#controlAdvance(RenderDataAPI, float)} and compute instance data.
     */
    public boolean controlRunWhilePaused(@NotNull RenderDataAPI renderEntity) {
        return ControlDataAPI.super.controlRunWhilePaused(renderEntity);
    }

    /**
     * Force remove entity if true.
     */
    public boolean controlIsDone(@NotNull RenderDataAPI renderEntity) {
        return ControlDataAPI.super.controlIsDone(renderEntity);
    }

    /**
     * Force remove entity after init and render one frame.
     */
    public boolean controlIsOnceRender(@NotNull RenderDataAPI renderEntity) {
        return ControlDataAPI.super.controlIsOnceRender(renderEntity);
    }
}
