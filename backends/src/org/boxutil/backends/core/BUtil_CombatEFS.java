package org.boxutil.backends.core;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.config.BoxConfigGUI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.RenderingUtil;
import org.boxutil.util.ShaderUtil;
import org.lwjgl.opengl.*;

import java.util.EnumSet;
import java.util.List;

public final class BUtil_CombatEFS extends BaseEveryFrameCombatPlugin {
    private CombatEngineAPI engine = null;

    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        final var context = BoxConfigs.getCurrShaderPacksContext();
        if (BUtil_GLImpl.Operations.checkCampaignCleanup()) {
            if (Global.getCurrentState() == GameState.TITLE) {
                context.cleanupCampaign(true);
                BUtil_ThreadResource.Rendering.Campaign.cleanupQueue();
                BUtil_ThreadResource.Rendering.Campaign.cleanupCustomData();
            }
        }
        if (BUtil_GLImpl.Operations.checkTitleCleanup()) {
            context.cleanupCombat();
            BUtil_ThreadResource.Rendering.Combat.cleanupQueue();
            BUtil_ThreadResource.Rendering.Combat.cleanupCustomData();
        }
        BUtil_ThreadResource.Rendering.Combat.initBasicLayers();
        context.initCombat(this.engine);
        BUtil_ThreadResource.Rendering.Combat.LOG.info("'BoxUtil' Combat rendering manager invited!");
    }

    public void advance(float amount, List<InputEventAPI> events) {
        BUtil_ThreadResource.checkShouldCloseGame();
        if (this.engine == null || !BoxConfigGUI.isGlobalInitialized()) return;
        final boolean isPaused = this.engine.isPaused();
        BUtil_GLImpl.Operations.advanceTimer(amount, isPaused);

        BUtil_ThreadResource._CURR_AMOUNT = amount;
        BUtil_ThreadResource._CURR_PAUSED = isPaused;
        if (BUtil_ThreadResource.__SHOULD_ADVANCE_SYNC_CURRENT_FRAME.compareAndSet(false, true)) {
            BoxThreadSync.Logical.beginAdvance().arriveAndAwaitAdvance();
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_BEGIN_ADVANCE);
            BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_AUX_BEGIN_ADVANCE);
        }

        final var context = BoxConfigs.getCurrShaderPacksContext();
        context.advanceInCombat(this.engine, amount);
        BUtil_GLDrawInstanceMemoryUsage.showGUI();
    }

    public void renderInUICoords(ViewportAPI viewport) {
        final var context = BoxConfigs.getCurrShaderPacksContext();
        if (context != null) context.applyCombatUIPass(viewport);
    }

    final static class Renderer extends BaseCombatLayeredRenderingPlugin {
        private final EnumSet<CombatEngineLayers> currLayers;
        private final int _layerBits;
        private final boolean _lowestLayer;
        private final boolean _highestLayer;
        private CombatEngineAPI engine;
        private boolean isExpired = false;

        Renderer(CombatEngineLayers activeLayer) {
            super();
            this.layer = activeLayer;
            this.currLayers = EnumSet.of(activeLayer);
            this._layerBits = (this.layer.ordinal() << 4) | 0b1;
            this._lowestLayer = activeLayer == RenderingUtil.getLowestCombatLayer();
            this._highestLayer = activeLayer == RenderingUtil.getHighestCombatLayer();
        }

        public void init(CombatEntityAPI entity) {
            this.entity = entity;
            this.engine = Global.getCombatEngine();
        }

        public void render(CombatEngineLayers layer, ViewportAPI viewport) {
            if (this._lowestLayer) BUtil_ThreadResource.checkShouldCloseGame();
            if (this.isExpired) return;
            if (this.engine == null || !BoxConfigGUI.isGlobalInitialized() || layer != this.layer) return;
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
                BUtil_ThreadResource.Rendering.Combat.delayAdd();

                BoxThreadSync.Rendering.beforeRendering().arriveAndAwaitAdvance();
                BUtil_ThreadResource.Logical.runEntitySubmit(false);

                BUtil_GLImpl.Operations.refreshCurrFrameState(viewport, context, BoxEnum.FALSE);
                BoxThreadSync.Rendering.beginRendering().arriveAndAwaitAdvance();
                BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_BEGIN_RENDERING);
            }

            boolean notMultiPass = shaderEnable;
            if (this._highestLayer) {
                BoxThreadSync.Rendering.beginIllumination().arriveAndAwaitAdvance();
                BUtil_ThreadResource.tryGLSync(BUtil_ThreadResource.__SYNC_BEGIN_ILLUMINATION);
                if (shaderEnable) ShaderCore.glBeginDraw();

                final boolean beautyOrBloom = BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassBloom(), combatIllumination = beautyOrBloom && context.isCombatIlluminationSupported();
                BUtil_GLImpl.IlluminationRender.processIlluminationPass(beautyOrBloom, combatIllumination, BUtil_ThreadResource._COMBAT_DIRECT_MAP, viewport, false, context);

                BUtil_GLImpl.IlluminationRender.processResultPass(viewport, false, context);

                notMultiPass &= BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassColor();
                if (notMultiPass) GL40.glBlendFuncSeparatei(0, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
                BUtil_GLImpl.MeshRender.processDistortionEntity(BoxConfigs.isDistortionEnable() && notMultiPass, BUtil_ThreadResource._COMBAT_DIRECT_MAP.get(DirectEntityType.DISTORTION), viewport);

                if (BoxConfigs.isMultiPassBeauty()) context.applyPostEffectPass(viewport, false);
                if (notMultiPass) GL11.glEnable(GL11.GL_BLEND);
            }

            final var meshArray = BUtil_ThreadResource._COMBAT_ENTITIES_R.get(layer);
            final var pluginSet = BUtil_ThreadResource._COMBAT_LAYERED_PLUGIN.get(layer);
            final boolean stageContinue = BUtil_GLImpl.Operations.checkSkipMeshCurrentLayout(meshArray, pluginSet);
            if (stageContinue) {
                if (shaderEnable && this._highestLayer) ShaderCore.glEndDraw();
                if (this._highestLayer == this._lowestLayer) {
                    this.isExpired = true;
                    BUtil_ThreadResource.Rendering.Combat.cleanupLayerQueue(this.layer);
                    this.engine.removeEntity(this.entity);
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

            BUtil_GLImpl.Operations.processMeshCurrentLayout(this._layerBits, this.layer, notMultiPass, viewport, meshArray, pluginSet);
            if (notMultiPass) {
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

        public boolean isExpired() {
            return this.isExpired;
        }

        public float getRenderRadius() {
            return Float.MAX_VALUE;
        }

        public EnumSet<CombatEngineLayers> getActiveLayers() {
            return this.currLayers;
        }
    }
}
