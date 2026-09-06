package org.boxutil.backends.core.gameloop;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.impl.campaign.BaseCustomEntityPlugin;
import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.config.BoxConfigGUI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.RenderingUtil;

public final class BUtil_CampaignRenderingPlugin extends BaseCustomEntityPlugin {
    private transient boolean _lowestLayer = true;
    private transient boolean _highestLayer = false;
    private transient boolean isExpired = false;
    private transient int _layerBits = 0b1;
    private transient CampaignEngineLayers layer = CampaignEngineLayers.TERRAIN_1;

    public void init(SectorEntityToken entity, Object pluginParams) {
        this.entity = entity;
    }

    public void initLayer(CampaignEngineLayers layer) {
        this._lowestLayer = layer == RenderingUtil.getLowestCampaignLayer();
        this._highestLayer = layer == RenderingUtil.getHighestCampaignLayer();
        this._layerBits = (layer.ordinal() << 4) | 0b1;
        this.layer = layer;
    }

    public void advance(float amount) {
        if (Global.getSector() == null || Global.getSector().getPlayerFleet() == null) return;
        final var player = Global.getSector().getPlayerFleet();
        if (player == null) return;
        final var playerLoc = player.getLocation();
        if (this.entity != null) this.entity.setFixedLocation(playerLoc.x + ShaderCore.getScreenScaleWidth(), playerLoc.y + ShaderCore.getScreenScaleHeight());
    }

    private void onRemoveLayer() {
        BUtil_ResourceStorage.campaignLayered().cleanupLayerQueue(this.layer);
    }

    public void render(CampaignEngineLayers layer, ViewportAPI viewport) {
        if (this._lowestLayer) BUtil_ResourceStorage.sharedResource().checkShouldCloseGame();
        if (this.isExpired) return;
        if (Global.getSector() == null || !BoxConfigGUI.isGlobalInitialized() || layer != this.layer) return;
        final var playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null || this.entity == null || playerFleet.getContainingLocation() != this.entity.getContainingLocation()) return;
        final boolean shaderEnable = BoxConfigs.isShaderEnable();
        final var context = BoxConfigs.getCurrShaderPacksContext();
        if (this._lowestLayer) {
            BUtil_SharedRenderingPass.layerInit(shaderEnable, viewport, context, true);
        }

        boolean notMultiPass = true;
        if (this._highestLayer) {
            notMultiPass = BUtil_SharedRenderingPass.highestLayer(viewport, context, shaderEnable, true);
        }

        final int staticTrailLayerLoc = BUtil_StaticTrailMemoryPool.toLayerLoc(layer);
        final var meshMap =  BUtil_ResourceStorage.campaignLayered().getEntity(layer);
        final var pluginSet = BUtil_ResourceStorage.campaignLayered().getLayerPlugin(layer);
        if (BUtil_SharedRenderingPass.checkSkipOrRemove(shaderEnable, this._highestLayer, this._lowestLayer, meshMap, pluginSet, staticTrailLayerLoc, this::onRemoveLayer)) return;

        BUtil_SharedRenderingPass.drawEachLayer(shaderEnable, this._highestLayer, _layerBits, layer, staticTrailLayerLoc, notMultiPass, viewport, meshMap, pluginSet);
    }

    public boolean isRenderWhenViewportAlphaMultIsZero() {
        return false;
    }

    public float getRenderRange() {
        return Float.MAX_VALUE;
    }

    public void destroy() {
        if (this.isExpired) return;
        this.isExpired = true;
        if (this.entity != null) {
            if (this.entity.getContainingLocation() != null) this.entity.getContainingLocation().removeEntity(this.entity);
            this.entity.setExpired(true);
        }
    }
}
