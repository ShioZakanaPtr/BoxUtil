package org.boxutil.base;

import org.boxutil.base.api.resource.TemporaryCleanupPlugin;

/**
 * <strong>Always running on vanilla thread.</strong>
 * @see org.boxutil.manager.CombatRenderingManager#addCleanupPlugin(TemporaryCleanupPlugin)
 * @see org.boxutil.manager.CampaignRenderingManager#addCleanupPlugin(TemporaryCleanupPlugin)
 */
@Deprecated
public class BaseTemporaryCleanupPlugin implements TemporaryCleanupPlugin {
    /**
     * When turn to title from combat, or vice versa.<p>
     * And back into campaign from combat.
     */
    public void cleanupCombatOnce() {
        TemporaryCleanupPlugin.super.cleanupCombatOnce();
    }

    /**
     * When player fleet goto new map, or back to title (whether active in combat).<p>
     * And before game save stage.
     */
    public void cleanupCampaignOnce() {
        TemporaryCleanupPlugin.super.cleanupCampaignOnce();
    }
}
