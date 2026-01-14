package org.boxutil.base;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import org.boxutil.base.api.shaderpacks.IlluminantInstanceDataLayoutAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.backends.shader.BUtil_GLImpl;
import org.boxutil.units.standard.light.*;
import de.unkrig.commons.nullanalysis.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.opengl.*;

import java.awt.*;
import java.util.List;

/**
 * Only and must have a no-parameter constructor.
 */
public abstract class BaseShaderPacksContext {
    /*
    =============================
    =====  Resource manage  =====
    =============================
    */

    /**
     * At <code>saves/common/shaderpacks</code>, file name is shader packs id.
     *
     * @param data just load the values, <code>null</code> for load falied.<p>Also call {@link BaseShaderPacksContext#configSave(JSONObject)} before if <code>null</code>.
     */
    public void configLoad(@Nullable JSONObject data) {}

    public void configSetDefault() {}

    /**
     * At <code>saves/common/shaderpacks</code>, file name is shader packs id.
     *
     * @param data just set the values.
     */
    public void configSave(JSONObject data) throws JSONException {}

    /**
     * Will call it after switching and applied config to this shader packs.
     *
     * @return false when initialization fails.
     */
    public boolean init() {
        return false;
    }

    /**
     * Will call it after switching and applied config to other shader packs, or {@link BaseShaderPacksContext#init()} return false.
     */
    public void destroy() {}



    /*
    ================================
    =====  Rendering pipeline  =====
    ================================
    */

    /**
     * @param preFiltering for implements, not required to delete it when failed.
     * @param shouldAllocate should re-allocate memory for pre-filtering texture when true.
     *
     * @return byte[] result = {state, maxLevel};<p>state returns {@link BoxEnum#STATE_SUCCESS} when success.<p> return {@link BoxEnum#STATE_FAILED} when implements code(compute shader etc.) not supported.
     */
    public byte[] applyTexturedAreaLightPreFiltering(int src, int preFiltering, boolean shouldAllocate) {
        return BUtil_GLImpl.StandardShaderPacks.applyTexturedAreaLightPreFiltering(src, preFiltering, shouldAllocate);
    }

    /**
     * Will call if the LocationAPI of player fleet has changed.<p>
     * Also call after in-campaign quick load.<p>
     * After {@link BaseShaderPacksContext#cleanupCampaign(boolean)}.
     */
    public void initCampaign(SectorAPI sector, boolean isCombatEnd, boolean init) {}

    public void initCombat(CombatEngineAPI engine) {}

    public void applyBeforeLowestLayerRender(ViewportAPI viewport, boolean isCampaign, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap, int auxFBO, int auxEmissive) {}

    public void applyBeforeIlluminationPass(ViewportAPI viewport, boolean isCampaign, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap, int auxFBO, int auxEmissive) {}

    /**
     * @return ignore shading if false.
     */
    public boolean applyBeforeInfiniteLightShading() {
        return false;
    }

    public void applyInfiniteLightShading(InfiniteLight entity, boolean validInstanceData, boolean notDefaultInstanceDataType) {}

    /**
     * @return ignore shading if false.
     */
    public boolean applyBeforePointLightShading() {
        return false;
    }

    public void applyPointLightShading(PointLight entity, boolean validInstanceData, boolean notDefaultInstanceDataType) {}

    /**
     * @return ignore shading if false.
     */
    public boolean applyBeforeSpotLightShading() {
        return false;
    }

    public void applySpotLightShading(SpotLight entity, boolean validInstanceData, boolean notDefaultInstanceDataType) {}

    /**
     * @return ignore shading if false.
     */
    public boolean applyBeforeLinearLightShading() {
        return false;
    }

    public void applyLinearLightShading(LinearLight entity, boolean validInstanceData, boolean notDefaultInstanceDataType) {}

    /**
     * @return ignore shading if false.
     */
    public boolean applyBeforeAreaLightShading() {
        return false;
    }

    public void applyAreaLightShading(AreaLight entity, boolean validInstanceData, boolean notDefaultInstanceDataType) {}

    public void applyAfterIlluminationPass(ViewportAPI viewport, boolean isCampaign, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap, int auxFBO, int auxEmissive) {}

    public void applyAAPass(ViewportAPI viewport, boolean isCampaign, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap) {
        BUtil_GLImpl.StandardShaderPacks.applyFXAA(true, false, false, colorMap, worldDataMap);
    }

    /**
     * @param isMultiPassBloom if ture, draw to black screen.
     */
    public void applyBloomPass(ViewportAPI viewport, boolean isCampaign, boolean isMultiPassBloom, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap, int auxFBO, int auxEmissive) {
        BUtil_GLImpl.StandardShaderPacks.applyBloom(isMultiPassBloom, emissiveMap, false, 0);
    }

    public void applyPostEffectPass(ViewportAPI viewport, boolean isCampaign) {}

    public void applyCombatUIPass(ViewportAPI viewport) {}

    public void advanceInCombat(CombatEngineAPI engine, float amount) {}

    /**
     * Call in campaign, just for OpenGL resource cleanup.
     */
    public void cleanupCombat() {}

    public void advanceInCampaign(SectorAPI sector, float amount) {}

    /**
     * Will call if the LocationAPI of player fleet has changed, or join battle.<p>
     * Also call after in-campaign quick load.<p>
     * Before {@link BaseShaderPacksContext#initCampaign(SectorAPI, boolean, boolean)}.
     */
    public void cleanupCampaign(boolean isSwitchToTitle) {}



    /*
    ============================
    =====  User interface  =====
    ============================
    */

    public String getID() {
        return "__ShaderPacksID";
    }

    public String getDisplayName() {
        return "__ShaderPacksName";
    }

    /**
     * @return returns <code>null</code> for hidden.
     */
    public String getDisplayVersion() {
        return null;
    }

    /**
     * Recommended size: 256*128
     *
     * @return returns less than <code>0</code> for none icon, equals for default icon.
     */
    public int getIconTextureID() {
        return BoxDatabase.BUtil_DefaultShaderPacksIcon.getTextureId();
    }

    /**
     * @return <code>null</code> for white, the raw texture.
     */
    public Color getIconTextureColor() {
        return Global.getSettings().getColor("standardUIIconColor");
    }

    /**
     * Technical hint.<p>
     * Combat shading (or with shadowing), without VFX.
     */
    public boolean isCombatIlluminationSupported() {
        return false;
    }

    /**
     * Technical hint.<p>
     * Campaign shading (or with shadowing), without VFX.
     */
    public boolean isCampaignIlluminationSupported() {
        return false;
    }

    /**
     * Technical hint.
     */
    public boolean isBloomSupported() {
        return true;
    }

    /**
     * Technical hint.
     */
    public boolean isAASupported() {
        return true;
    }

    /**
     * Technical hint.<p>
     * <strong>The anisotropy shading is only supports for infinite-light / point-light / spot-light, without area-light.</strong>
     */
    public boolean isBaseAnisotropySupported() {
        return false;
    }

    /**
     * Technical hint.
     */
    public boolean isTexturedAreaLightSupported() {
        return true;
    }

    /**
     * Technical hint.<p>
     * <strong>For real-time line/area lighting(based LTC, the Linearly-Transformed-Cosines), its high performance cost, difficult and complicated to implement it.<p>
     * A compromised and easy way is only implements the anisotropic lighting for except area light(InfiniteLight, PointLight, SpotLight).</strong>
     */
    public boolean isCompleteAnisotropySupported() {
        return false;
    }

    /**
     * For {@link BaseIlluminantData} that have custom instance data layout on this shader packs.
     *
     * @return ture if the shader packs use specific instance data layout, like custom SSBOs, custom UBOs, custom TBOs, custom VBOs, storage to any texture object or any other.
     */
    public boolean haveCustomInstanceDataLayout() {
        return false;
    }

    /**
     * @return NOT <code>null</code> when {@link BaseShaderPacksContext#haveCustomInstanceDataLayout()} returns true, and should implement all the methods.
     */
    public IlluminantInstanceDataLayoutAPI getCustomInstanceDataLayout() {
        return null;
    }

    /**
     * To tell players if this shader pack is usable in current hardware.
     */
    public boolean isUsable(ContextCapabilities glContext) {
        return BoxConfigs.isBaseGL42Supported();
    }

    /**
     * With description and config option.
     */
    public void createDetailsPanel(TooltipMakerAPI tooltip, PositionAPI planePos, PositionAPI elementPos) {}

    /**
     * @return returns ture for any valid setting has changed, by OR operation.
     */
    public boolean detailsPanelAdvance(float amount, PositionAPI planePos, PositionAPI elementPos) {
        return false;
    }

    public void detailsPanelRender(float alpha, PositionAPI planePos, PositionAPI elementPos) {}

    public void detailsPanelRenderBelow(float alpha, PositionAPI planePos, PositionAPI elementPos) {}

    /**
     * @return returns ture for any valid setting has changed.
     */
    public boolean detailsPanelProcessInput(List<InputEventAPI> events, PositionAPI planePos, PositionAPI elementPos) {
        return false;
    }

    /**
     * @return returns ture for any valid setting has changed.
     */
    public boolean detailsPanelButtonPressed(Object buttonId, PositionAPI planePos, PositionAPI elementPos) {
        return false;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof BaseShaderPacksContext) return this.hashCode() == obj.hashCode();
        else return false;
    }

    public final String toString() {
        return "ShaderPacksID=" + this.getID() + ",Name=" + this.getDisplayName() + ",Version=" + this.getDisplayVersion();
    }

    public final int hashCode() {
        return this.getID().hashCode();
    }
}
