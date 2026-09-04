package org.boxutil.base;

import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;

/**
 * <b>Running on another thread, not the vanilla thread.</b><p>
 * Always running unless it was expired.<p>
 * All the plugin is unordered execution on random background thread，the order in which they are added is not guaranteed to be preserved during execution.<p>
 * If a new plugin is added during the current frame, it is considered to start running only from the next frame onward.
 */
@Deprecated
public class BaseBackgroundEveryFramePlugin implements BackgroundEveryFramePlugin {
    public boolean isRenderingExpired() {
        return BackgroundEveryFramePlugin.super.isRenderingExpired();
    }
    public void runBeginRendering(float amount, boolean isPaused) {
        BackgroundEveryFramePlugin.super.runBeginRendering(amount, isPaused);
    }
    public void runBeginIllumination(float amount, boolean isPaused) {
        BackgroundEveryFramePlugin.super.runBeginIllumination(amount, isPaused);
    }
    public void runAfterRendering(float amount, boolean isPaused) {
        BackgroundEveryFramePlugin.super.runAfterRendering(amount, isPaused);
    }

    public boolean isAdvanceExpired() {
        return BackgroundEveryFramePlugin.super.isAdvanceExpired();
    }
    public void runBeginAdvance(float amount, boolean isPaused) {
        BackgroundEveryFramePlugin.super.runBeginAdvance(amount, isPaused);
    }
    public void runAfterAdvance(float amount, boolean isPaused) {
        BackgroundEveryFramePlugin.super.runAfterAdvance(amount, isPaused);
    }
}
