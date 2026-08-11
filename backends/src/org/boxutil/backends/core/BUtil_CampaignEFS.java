package org.boxutil.backends.core;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import org.boxutil.backends.core.dev.BUtil_GLDrawInfo;
import org.boxutil.backends.core.instancedrendering.BUtil_GLDrawInstanceMemoryUsage;
import org.boxutil.backends.core.statictrail.BUtil_GLDrawStaticTrailMemoryUsage;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;

public final class BUtil_CampaignEFS implements EveryFrameScript {
    private transient LocationAPI lastLocation = null;

    private void refreshManagerEntity(final LocationAPI playerLoc) {
        this.lastLocation = playerLoc;
        BUtil_ThreadResource.Rendering.Campaign.cleanupQueue();
        BUtil_ThreadResource.Rendering.Campaign.initBasicLayers();
    }

    public void advance(float amount) {
        BUtil_ThreadResource.checkShouldCloseGame();
        if (Global.getCurrentState() == GameState.TITLE || Global.getSector() == null) return;
        final var context = BoxConfigs.getCurrShaderPacksContext();
        final var sector = Global.getSector();
        final var player = sector.getPlayerFleet();

        BUtil_GLImpl.Operations.setCampaignFlag();
        if (player == null || player.getContainingLocation() == null) {
            BUtil_GLDrawInfo.showInfo();
            return;
        }
        if (BUtil_GLImpl.Operations.checkCampaignCleanup()) {
            context.cleanupCombat();
            BUtil_ThreadResource.Rendering.Combat.cleanupQueue();
            BUtil_ThreadResource.Rendering.Combat.cleanupCustomData();
            BUtil_StaticTrailMemoryPool.cleanupPool(true);
        }
        final var playerLoc = player.getContainingLocation();

        if (this.lastLocation == null) {
            this.refreshManagerEntity(playerLoc);
            context.initCampaign(sector, false, true);
            BUtil_ThreadResource.Rendering.Campaign.LOG.info("'BoxUtil' Campaign rendering manager invited!");
        } else if (this.lastLocation != playerLoc) {
            context.cleanupCampaign(false);
            BUtil_StaticTrailMemoryPool.cleanupPool(false);
            this.refreshManagerEntity(playerLoc);
            context.initCampaign(sector, false, false);
        }

        final boolean isPaused = Global.getSector().isPaused();
        BUtil_GLImpl.Operations.advanceTimer(amount, isPaused);

        if (BUtil_ThreadResource.__SHOULD_ADVANCE_SYNC_CURRENT_FRAME.compareAndSet(false, true)) {
            BoxThreadSync.Logical.beginAdvance().arriveAndAwaitAdvance();
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_BEGIN_ADVANCE);
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_AUX_BEGIN_ADVANCE);
        }

        context.advanceInCampaign(sector, amount);
        BUtil_GLDrawInfo.showInfo();
    }

    public boolean isDone() {
        return false;
    }

    public boolean runWhilePaused() {
        return true;
    }
}
