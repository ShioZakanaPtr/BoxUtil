package org.boxutil.backends.core.gameloop;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.boxutil.backends.core.dev.BUtil_GLDrawInfo;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.config.BoxConfigGUI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.util.RenderingUtil;

import java.util.EnumSet;
import java.util.List;

public final class BUtil_CombatEFS extends BaseEveryFrameCombatPlugin {
    private CombatEngineAPI engine = null;

    public void init(CombatEngineAPI engine) {
        this.engine = engine;
        final var context = BoxConfigs.getCurrShaderPacksContext();
        if (BUtil_GLImpl.checkCampaignCleanup()) {
            if (Global.getCurrentState() == GameState.TITLE) {
                context.cleanupCampaign(true);
                BUtil_ResourceStorage.campaignLayered().cleanupAllQueue();
                BUtil_ResourceStorage.campaignLayered().cleanupCustomData();
                BUtil_StaticTrailMemoryPool.cleanupPool(false);
            }
        }
        if (BUtil_GLImpl.checkTitleCleanup()) {
            context.cleanupCombat();
            BUtil_ResourceStorage.combatLayered().cleanupAllQueue();
            BUtil_ResourceStorage.combatLayered().cleanupCustomData();
            BUtil_StaticTrailMemoryPool.cleanupPool(true);
        }
        BUtil_ResourceStorage.combatLayered().initBasicLayers();
        context.initCombat(this.engine);
        BUtil_ResourceStorage.combatLayered().getLog().info("'BoxUtil' Combat rendering manager invited!");
    }

    public void advance(float amount, List<InputEventAPI> events) {
        BUtil_ResourceStorage.sharedResource().checkShouldCloseGame();
        if (this.engine == null || !BoxConfigGUI.isGlobalInitialized()) return;
        final boolean isPaused = this.engine.isPaused();
        BUtil_GLImpl.advanceTimer(amount, isPaused);

        if (BUtil_ResourceStorage.syncResource().shouldLogicalSync()) {
            BoxThreadSync.Logical.beginAdvance().arriveAndAwaitAdvance();
        }
        BUtil_ResourceStorage.combatLayered().processGLCmdBeginAdvance();

        final var context = BoxConfigs.getCurrShaderPacksContext();
        context.advanceInCombat(this.engine, amount);
        BUtil_GLDrawInfo.showInfo();
    }

    public void renderInUICoords(ViewportAPI viewport) {
        final var context = BoxConfigs.getCurrShaderPacksContext();
        if (context != null) context.applyCombatUIPass(viewport);
    }

    public final static class Renderer extends BaseCombatLayeredRenderingPlugin {
        private final EnumSet<CombatEngineLayers> currLayers;
        private final int _layerBits;
        private final boolean _lowestLayer;
        private final boolean _highestLayer;
        private CombatEngineAPI engine;
        private boolean isExpired = false;

        public Renderer(CombatEngineLayers activeLayer) {
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

        private void onRemoveLayer() {
            BUtil_ResourceStorage.combatLayered().cleanupLayerQueue(this.layer);
            this.isExpired = true;
            this.engine.removeEntity(this.entity);
        }

        public void render(CombatEngineLayers layer, ViewportAPI viewport) {
            if (this._lowestLayer) BUtil_ResourceStorage.sharedResource().checkShouldCloseGame();
            if (this.isExpired) return;
            if (this.engine == null || !BoxConfigGUI.isGlobalInitialized() || layer != this.layer) return;
            final boolean shaderEnable = BoxConfigs.isShaderEnable();
            final var context = BoxConfigs.getCurrShaderPacksContext();
            if (this._lowestLayer) {
                BUtil_SharedRenderingPass.layerInit(shaderEnable, viewport, context, false);
            }

            boolean notMultiPass = true;
            if (this._highestLayer) {
                notMultiPass = BUtil_SharedRenderingPass.highestLayer(viewport, context, shaderEnable, false);
            }

            final int staticTrailLayerLoc = BUtil_StaticTrailMemoryPool.toLayerLoc(layer);
            final var meshMap = BUtil_ResourceStorage.combatLayered().getEntity(layer);
            final var pluginSet = BUtil_ResourceStorage.combatLayered().getLayerPlugin(layer);
            if (BUtil_SharedRenderingPass.checkSkipOrRemove(shaderEnable, this._highestLayer, this._lowestLayer, meshMap, pluginSet, staticTrailLayerLoc, this::onRemoveLayer)) return;

            BUtil_SharedRenderingPass.drawEachLayer(shaderEnable, this._highestLayer, _layerBits, layer, staticTrailLayerLoc, notMultiPass, viewport, meshMap, pluginSet);
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
