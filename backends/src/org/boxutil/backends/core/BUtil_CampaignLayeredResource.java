package org.boxutil.backends.core;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import org.boxutil.backends.core.gameloop.BUtil_CampaignRenderingPlugin;
import org.boxutil.define.BoxDatabase;
import org.boxutil.util.RenderingUtil;

import java.util.EnumMap;

public class BUtil_CampaignLayeredResource extends BUtil_LayeredResourceTemplate<CampaignEngineLayers> {
    private final EnumMap<CampaignEngineLayers, BUtil_CampaignRenderingPlugin> campaignEntityMap = new EnumMap<>(CampaignEngineLayers.class);

    public BUtil_CampaignLayeredResource() {
        super(CampaignEngineLayers.class);
    }

    protected boolean isCampaign() {
        return true;
    }

    protected void cleanupAllVanillaPlugin() {
        for (var plugin : this.campaignEntityMap.values()) if (plugin != null) plugin.destroy();
        this.campaignEntityMap.clear();
    }

    protected void checkRenderingPluginAndAdd(CampaignEngineLayers layer) {
        final var location = (Global.getSector() != null && Global.getSector().getPlayerFleet() != null) ? Global.getSector().getPlayerFleet().getContainingLocation() : null;
        if (location == null) {
            this.log.error("'BoxUtil' rendering plugin failed to join layer: '" + layer.name() + "' cause player fleet cannot be found.");
            return;
        }
        if (!this.campaignEntityMap.containsKey(layer)) {
            final var id = location.getId() + '_' + BoxDatabase.CAMPAIGN_MANAGE_ID + '_' + layer.name();
            final var entity = location.addCustomEntity(id, id, BoxDatabase.CAMPAIGN_MANAGE_ID, Factions.NEUTRAL);
            entity.addTag(BoxDatabase.CAMPAIGN_MANAGE_TAG);
            entity.setDiscoverable(false);
            entity.setActiveLayers(layer);
            if (entity.getCustomPlugin() instanceof BUtil_CampaignRenderingPlugin plugin) {
                plugin.initLayer(layer);
                this.campaignEntityMap.put(layer, plugin);
                this.log.info("'BoxUtil' rendering plugin join layer: '" + layer.name() + '\'');
            } else {
                location.removeEntity(entity);
                entity.setExpired(true);
                this.log.error("'BoxUtil' rendering plugin failed to join layer: '" + layer.name() + "' cause plugin has been damaged.");
            }
        }
    }

    public void initBasicLayers() {
        this.lock.lock();
        checkRenderingPluginAndAdd(RenderingUtil.getLowestCampaignLayer());
        checkRenderingPluginAndAdd(RenderingUtil.getHighestCampaignLayer());
        this.lock.unlock();
    }

    protected void cleanupVanillaPlugin(CampaignEngineLayers layer) {
        final var removedPlugin = this.campaignEntityMap.remove(layer);
        if (removedPlugin != null) removedPlugin.destroy();
    }

    protected CampaignEngineLayers layerCast(Object layerRaw) {
        if (layerRaw instanceof CampaignEngineLayers cast) return cast;
        else return null;
    }
}
