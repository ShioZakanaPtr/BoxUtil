package org.boxutil.backends.core.gameloop;

import com.fs.starfarer.api.combat.ViewportAPI;
import org.boxutil.backends.core.BUtil_ResourceStorage;
import org.boxutil.backends.core.statictrail.BUtil_StaticTrailMemoryPool;
import org.boxutil.backends.shader.BUtil_EntityImpl;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.base.StandardShaderpacksPass;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.everyframe.LayeredRenderingPlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.config.BoxThreadSync;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import org.boxutil.define.GLWrapper;
import org.boxutil.define.LayeredEntityType;
import org.boxutil.manager.ShaderCore;
import org.boxutil.util.ShaderUtil;

import java.util.EnumMap;
import java.util.List;
import java.util.Set;

public final class BUtil_SharedRenderingPass {
    public static void layerInit(boolean shaderEnable, final ViewportAPI viewport, final BaseShaderPacksContext context, boolean inCampaign) {
        if (BUtil_ResourceStorage.syncResource().shouldRenderingSync()) {
            BoxThreadSync.Logical.finishAdvance().arriveAndAwaitAdvance();
        }
        final var layeredRes = inCampaign ? BUtil_ResourceStorage.campaignLayered() : BUtil_ResourceStorage.combatLayered();
        layeredRes.delayAdd();
        BUtil_StaticTrailMemoryPool.processCutTrailOnEntity();
        BUtil_StaticTrailMemoryPool.processRemoveTrailOnEntity();

        BoxThreadSync.Rendering.beforeRendering().arriveAndAwaitAdvance();
        BUtil_ResourceStorage.sharedResource().refreshQueueBit();
        layeredRes.processGLCmdBeforeRendering();
        BUtil_ResourceStorage.sharedResource().runEntitySubmit(false);

        BUtil_GLImpl.refreshCurrFrameState(shaderEnable, viewport, context, inCampaign ? BoxEnum.TRUE : BoxEnum.FALSE);
        layeredRes.applyMemoryBarrier();
        BoxThreadSync.Rendering.beginRendering().arriveAndAwaitAdvance();
    }

    public static boolean highestLayer(final ViewportAPI viewport, final BaseShaderPacksContext context, boolean shaderEnable, boolean inCampaign) {
        final var layeredRes = inCampaign ? BUtil_ResourceStorage.campaignLayered() : BUtil_ResourceStorage.combatLayered();
        BoxThreadSync.Rendering.beginIllumination().arriveAndAwaitAdvance();
        layeredRes.processGLCmdBeginIllumination();
        ShaderCore.glBeginDraw();

        final boolean beautyOrBloom = BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassBloom(), canIllumination = beautyOrBloom && context.isCombatIlluminationSupported();
        BUtil_EntityImpl.Illumination.processIlluminationPass(beautyOrBloom, canIllumination, layeredRes.getDirectEntity(), viewport, inCampaign, context);

        BUtil_GLImpl.processShaderpacksResultPass(shaderEnable, viewport, inCampaign, context);

        final boolean in_notMultiPass = BoxConfigs.isMultiPassBeauty() || BoxConfigs.isMultiPassColor(),
                coreRendering = shaderEnable && in_notMultiPass;
        if (coreRendering) GLWrapper.Operation.glBlendFuncSeparatei(0, GLWrapper.Operation.GL_SRC_ALPHA, GLWrapper.Operation.GL_ONE_MINUS_SRC_ALPHA, GLWrapper.Operation.GL_ZERO, GLWrapper.Operation.GL_ONE);
        BUtil_EntityImpl.Mesh.processDistortionEntity(BoxConfigs.isDistortionEnable() && in_notMultiPass, layeredRes.getDirectEntity().get(DirectEntityType.DISTORTION));

        if (BoxConfigs.isMultiPassBeauty()) context.applyPostEffectPass(viewport, inCampaign);
        if (coreRendering) GLWrapper.Operation.glEnable(GLWrapper.Operation.GL_BLEND);
        return in_notMultiPass;
    }

    public static boolean checkSkipOrRemove(boolean shaderEnable, boolean highestLayer, boolean lowestLayer, final EnumMap<LayeredEntityType, List<RenderDataAPI>> meshMap, final Set<LayeredRenderingPlugin> renderingPlugins, int layerLoc, final Runnable onRemove) {
        final boolean stageContinue = BUtil_GLImpl.checkSkipMeshCurrentLayout(meshMap, renderingPlugins) && BUtil_StaticTrailMemoryPool.bypassDrawTrail(layerLoc);
        if (stageContinue) {
            if (highestLayer) ShaderCore.glEndDraw();
            if (highestLayer == lowestLayer) onRemove.run();
            if (highestLayer) BoxThreadSync.Rendering.afterRendering().arriveAndAwaitAdvance();
            return true;
        }
        return false;
    }

    public static void drawEachLayer(boolean shaderEnable, boolean highestLayer, int layerBits, final Object layer, int layerLoc, boolean notMultiPass, final ViewportAPI viewport, final EnumMap<LayeredEntityType, List<RenderDataAPI>> meshMap, final Set<LayeredRenderingPlugin> renderingPlugins) {
        if (!highestLayer) ShaderCore.glBeginDraw();
        final var renderingBuffer = ShaderCore.getRenderingBuffer();
        StandardShaderpacksPass.activateBloomPass();
        if (shaderEnable) {
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
            if (!highestLayer) {
                GLWrapper.Texture.glBindTexture(GLWrapper.Texture.GL_TEXTURE_2D, renderingBuffer.getColorResult());
                GLWrapper.Texture.glCopyTexSubImage2D(GLWrapper.Texture.GL_TEXTURE_2D, 0, 0, 0, 0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
                GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, renderingBuffer.getFBO(0));
            }
        }

        BUtil_EntityImpl.Mesh.processMeshCurrentLayout(shaderEnable, layerBits, layer, layerLoc, notMultiPass, viewport, meshMap, renderingPlugins);
        if (shaderEnable && !highestLayer) {
            GLWrapper.FBO.glBindFramebuffer(GLWrapper.FBO.GL_FRAMEBUFFER, 0);
            ShaderUtil.blitToScreen(renderingBuffer.getFBO(0));
        }
        ShaderCore.glEndDraw();
        if (highestLayer) BoxThreadSync.Rendering.afterRendering().arriveAndAwaitAdvance();
    }

    private BUtil_SharedRenderingPass() {}
}
