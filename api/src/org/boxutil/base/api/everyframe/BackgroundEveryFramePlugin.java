package org.boxutil.base.api.everyframe;

/**
 * <b>Running on another thread, not the vanilla thread.</b><p>
 * Always running unless it was expired, the plugin run once in each frame, and then will check the expired sate.<p>
 * All the plugin is unordered execution on random background thread，the order in which they are added is not guaranteed to be preserved during execution.<p>
 * If a new plugin is added during the current frame, it is considered to start running only from the next frame onward.
 */
public interface BackgroundEveryFramePlugin {
    default boolean isRenderingExpired() {
        return false;
    }
    default void runBeginRendering(float amount, boolean isPaused) {}
    default void runBeginIllumination(float amount, boolean isPaused) {}
    default void runAfterRendering(float amount, boolean isPaused) {}

    default boolean isAdvanceExpired() {
        return false;
    }
    default void runBeginAdvance(float amount, boolean isPaused) {}
    default void runAfterAdvance(float amount, boolean isPaused) {}
}
