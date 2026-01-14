package org.boxutil.base.api.everyframe;

/**
 * <strong>Running on another thread, not the vanilla thread.</strong><p>
 * Always running unless it was expired.
 */
public interface BackgroundEveryFramePlugin {
    boolean isRenderingExpired();
    void runBeginRendering(float amount, boolean isPaused);
    void runBeginIllumination(float amount, boolean isPaused);
    void runAfterRendering(float amount, boolean isPaused);

    boolean isAdvanceExpired();
    void runBeginAdvance(float amount, boolean isPaused);
    void runAfterAdvance(float amount, boolean isPaused);
}
