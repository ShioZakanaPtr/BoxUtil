package org.boxutil.base;

import org.boxutil.base.api.resource.TemporaryCleanupPlugin;

/**
 * <strong>Running on another thread, not the vanilla thread.</strong>
 * @see org.boxutil.manager.CombatRenderingManager#addCleanupPlugin(TemporaryCleanupPlugin)
 * @see org.boxutil.manager.CampaignRenderingManager#addCleanupPlugin(TemporaryCleanupPlugin)
 */
public class BaseTemporaryCleanupPlugin implements TemporaryCleanupPlugin {
    /**
     * When turn to title from combat, or vice versa.<p>
     * And back into campaign from combat.
     */
    public void cleanupCombatOnce() {}

    /**
     * When player fleet goto new map, or back to title (whether active in combat).<p>
     * And before game save stage.
     */
    public void cleanupCampaignOnce() {}
}
