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
public abstract class BaseControlData implements ControlDataAPI {
    /**
     * Run once as long as linked to a render entity.
     */
    public void controlInit(@NotNull RenderDataAPI renderEntity) {}

    /**
     * Executes before the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    public void controlBeforeRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount) {}

    /**
     * Executes after the rendering call and {@link ControlDataAPI#controlCanRenderNow(RenderDataAPI)}, and before the logical advance of this frame.<p>
     * Should not be call instance data refresh or change them on gpu here.
     */
    public void controlAfterRenderingAdvance(@NotNull RenderDataAPI renderEntity, float lastFrameAmount) {}

    /**
     * <strong>Running on another thread.</strong><p>
     * Executes in the logical advance.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    public void controlAdvance(@NotNull RenderDataAPI renderEntity, float amount) {}

    /**
     * Run once when call {@link RenderDataAPI#delete()}.<p>
     * Invalid when {@link ControlDataAPI#controlIsOnceRender(RenderDataAPI)} is ture.
     */
    public void controlRemove(@NotNull RenderDataAPI renderEntity) {}

    public boolean controlAlphaBasedTimer(@NotNull RenderDataAPI renderEntity) {
        return true;
    }

    /**
     * Will call {@link ControlDataAPI#controlRemove(RenderDataAPI)} when timer of entity is out.
     */
    public boolean controlRemoveBasedTimer(@NotNull RenderDataAPI renderEntity) {
        return true;
    }

    /**
     * Check it in each rendering frame.
     */
    public boolean controlCanRenderNow(@NotNull RenderDataAPI renderEntity) {
        return true;
    }

    /**
     * Affects {@link ControlDataAPI#controlAdvance(RenderDataAPI, float)} and compute instance data.
     */
    public boolean controlRunWhilePaused(@NotNull RenderDataAPI renderEntity) {
        return false;
    }

    /**
     * Force remove entity if true.
     */
    public boolean controlIsDone(@NotNull RenderDataAPI renderEntity) {
        return false;
    }

    /**
     * Force remove entity after init and render one frame.
     */
    public boolean controlIsOnceRender(@NotNull RenderDataAPI renderEntity) {
        return false;
    }
}
