package org.boxutil.base.api.resource;

/**
 * <strong>Running on another thread, not the vanilla thread.</strong>
 * @see org.boxutil.manager.CombatRenderingManager#addCleanupPlugin(TemporaryCleanupPlugin)
 * @see org.boxutil.manager.CampaignRenderingManager#addCleanupPlugin(TemporaryCleanupPlugin)
 */
public interface TemporaryCleanupPlugin {
    /**
     * When turn to title from combat, or vice versa.<p>
     * And back into campaign from combat.
     */
    void cleanupCombatOnce();

    /**
     * When player fleet goto new map, or back to title (whether active in combat).<p>
     * And before game save stage.
     */
    void cleanupCampaignOnce();
}
