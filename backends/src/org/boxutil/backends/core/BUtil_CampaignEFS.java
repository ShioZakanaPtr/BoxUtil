package org.boxutil.backends.core;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.RenderingUtil;
import org.boxutil.util.ShaderUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.EnumMap;

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
            BUtil_GLDrawInstanceMemoryUsage.showGUI();
            return;
        }
        if (BUtil_GLImpl.Operations.checkCampaignCleanup()) {
            context.cleanupCombat();
            BUtil_ThreadResource.Rendering.Combat.cleanupQueue();
            BUtil_ThreadResource.Rendering.Combat.cleanupCustomData();
        }
        final var playerLoc = player.getContainingLocation();

        if (this.lastLocation == null) {
            this.refreshManagerEntity(playerLoc);
            context.initCampaign(sector, false, true);
            BUtil_ThreadResource.Rendering.Campaign.LOG.info("'BoxUtil' Campaign rendering manager invited!");
        } else if (this.lastLocation != playerLoc) {
            context.cleanupCampaign(false);
            this.refreshManagerEntity(playerLoc);
            context.initCampaign(sector, false, false);
        }

        final boolean isPaused = Global.getSector().isPaused();
        BUtil_GLImpl.Operations.advanceTimer(amount, isPaused);

        BUtil_ThreadResource._CURR_AMOUNT = amount;
        BUtil_ThreadResource._CURR_PAUSED = isPaused;
        if (BUtil_ThreadResource.__SHOULD_ADVANCE_SYNC_CURRENT_FRAME.compareAndSet(false, true)) {
            BoxThreadSync.Logical.beginAdvance().arriveAndAwaitAdvance();
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_BEGIN_ADVANCE);
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_AUX_BEGIN_ADVANCE);
        }

        context.advanceInCampaign(sector, amount);
        BUtil_GLDrawInstanceMemoryUsage.showGUI();
    }

    public boolean isDone() {
        return false;
    }

    public boolean runWhilePaused() {
        return true;
    }
}
