package org.boxutil.backends.core;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.impl.campaign.BaseCustomEntityPlugin;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.config.BoxConfigGUI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.RenderingUtil;
import org.boxutil.util.ShaderUtil;
import org.boxutil.util.concurrent.SpinLock;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;
import org.lwjgl.util.vector.Vector2f;

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

    public void render(CampaignEngineLayers layer, ViewportAPI viewport) {
        if (this._lowestLayer) BUtil_ThreadResource.checkShouldCloseGame();
        if (this.isExpired) return;
        if (Global.getSector() == null || !BoxConfigGUI.isGlobalInitialized() || layer != this.layer) return;
        final var playerFleet = Global.getSector().getPlayerFleet();
        if (playerFleet == null || this.entity == null || playerFleet.getContainingLocation() != this.entity.getContainingLocation()) return;
        final boolean shaderEnable = BoxConfigs.isShaderEnable();
        final var renderingBuffer = ShaderCore.getRenderingBuffer();
        final var context = BoxConfigs.getCurrShaderPacksContext();
        if (this._lowestLayer) {
            if (BUtil_ThreadResource.__SHOULD_ADVANCE_SYNC_CURRENT_FRAME.compareAndSet(true, false)) {
                BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_FINISH_ADVANCE_HOST);
                BoxThreadSync.Logical.finishAdvance().arriveAndAwaitAdvance();
                BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_FINISH_ADVANCE);
                BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_AUX_FINISH_ADVANCE);
            }
            BUtil_ThreadResource.Rendering.Campaign.delayAdd();

            BoxThreadSync.Rendering.beforeRendering().arriveAndAwaitAdvance();
            BUtil_ThreadResource.Logical.runEntitySubmit(false);

            BUtil_GLImpl.Operations.refreshCurrFrameState(viewport, context, BoxEnum.TRUE);
            BoxThreadSync.Rendering.beginRendering().arriveAndAwaitAdvance();
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_BEGIN_RENDERING);
        }

        boolean notMultiPass = shaderEnable;
        if (this._highestLayer) {
            BoxThreadSync.Rendering.beginIllumination().arriveAndAwaitAdvance();
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_BEGIN_ILLUMINATION);
            if (shaderEnable) ShaderCore.glBeginDraw();

            final boolean beautyOrBloom = BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassBloom(), campaignIllumination = beautyOrBloom && context.isCampaignIlluminationSupported();
            BUtil_GLImpl.IlluminationRender.processIlluminationPass(beautyOrBloom, campaignIllumination, BUtil_ThreadResource._CAMPAIGN_DIRECT_MAP, viewport, true, context);

            BUtil_GLImpl.IlluminationRender.processResultPass(viewport, true, context);

            notMultiPass &= BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassColor();
            GL40.glBlendFuncSeparatei(0, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
            BUtil_GLImpl.MeshRender.processDistortionEntity(BoxConfigs.isDistortionEnable() && notMultiPass, BUtil_ThreadResource._CAMPAIGN_DIRECT_MAP.get(DirectEntityType.DISTORTION), viewport);

            if (BoxConfigs.isMultiPassBeauty()) context.applyPostEffectPass(viewport, true);
            if (notMultiPass) GL11.glEnable(GL11.GL_BLEND);
        }

        final var meshArray = BUtil_ThreadResource._CAMPAIGN_ENTITIES_R.get(layer);
        final var pluginSet = BUtil_ThreadResource._CAMPAIGN_LAYERED_PLUGIN.get(layer);
        final boolean stageContinue = BUtil_GLImpl.Operations.checkSkipMeshCurrentLayout(meshArray, pluginSet);
        if (stageContinue) {
            if (shaderEnable && this._highestLayer) ShaderCore.glEndDraw();
            if (this._highestLayer == this._lowestLayer) {
                BUtil_ThreadResource.Rendering.Campaign.cleanupLayerQueue(this.layer);
            }
            if (this._highestLayer) {
                BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_AFTER_RENDERING_HOST);
                BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_AUX_AFTER_RENDERING_HOST);
                BoxThreadSync.Rendering.afterRendering().arriveAndAwaitAdvance();
                BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_AFTER_RENDERING);
            }
            return;
        }

        if (shaderEnable) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
            if (!this._highestLayer) {
                ShaderCore.glBeginDraw();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderingBuffer.getColorResult());
                GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, renderingBuffer.getFBO(0));
            }
        }

        BUtil_GLImpl.Operations.processMeshCurrentLayout(this._layerBits, layer, notMultiPass, viewport, meshArray, pluginSet);
        if (shaderEnable) {
            if (!this._highestLayer) {
                GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
                ShaderUtil.blitToScreen(renderingBuffer.getFBO(0));
            }
            ShaderCore.glEndDraw();
        }
        if (this._highestLayer) {
            BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_AFTER_RENDERING_HOST);
            BUtil_ThreadResource.sendGLSync(BUtil_ThreadResource.__SYNC_AUX_AFTER_RENDERING_HOST);
            BoxThreadSync.Rendering.afterRendering().arriveAndAwaitAdvance();
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_AFTER_RENDERING);
        }
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
