package org.boxutil.base;

import org.boxutil.base.api.everyframe.BackgroundEveryFramePlugin;

/**
 * <strong>Running on another thread, not the vanilla thread.</strong><p>
 * Always running unless it was expired.
 */
public class BaseBackgroundEveryFramePlugin implements BackgroundEveryFramePlugin {
    public boolean isRenderingExpired() {
        return false;
    }
    public void runBeginRendering(float amount, boolean isPaused) {}
    public void runBeginIllumination(float amount, boolean isPaused) {}
    public void runAfterRendering(float amount, boolean isPaused) {}

    public boolean isAdvanceExpired() {
        return false;
    }
    public void runBeginAdvance(float amount, boolean isPaused) {}
    public void runAfterAdvance(float amount, boolean isPaused) {}
}
