package org.boxutil.backends.shader;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.boxutil.manager.ShaderCore;
import de.unkrig.commons.nullanalysis.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.opengl.ContextCapabilities;

import java.awt.*;
import java.util.List;

public class BUtil_NotSelectedShaderPacks extends BaseShaderPacksContext {
    protected final static byte _CHECKBOX_HEIGHT = 32;
    protected final static byte _BUTTON_BLOOM = 0;
    protected final static byte _BUTTON_AA = 1;
    protected final static byte _BUTTON_AAC = 2;
    protected final static byte _BUTTON_AAQ = 3;
    protected final static byte _BUTTON_DEPTHAA = 4;

    protected boolean _BLOOM_ENABLED = true;
    protected boolean _AA_ENABLED = true;
    protected boolean _AA_CONSOLE = false;
    protected boolean _DEPTH_AA = false;

    protected final ButtonAPI[] _button = new ButtonAPI[5];

    public void configLoad(@Nullable JSONObject data) {
        if (data != null) {
            this._BLOOM_ENABLED = ShaderCore.isBloomValid() && data.optBoolean("BUtil_EnableBloom", true);
            this._AA_ENABLED = ShaderCore.isFXAAValid() && data.optBoolean("BUtil_EnableAA", true);
            this._AA_CONSOLE = data.optBoolean("BUtil_AAConsole", false);
            this._DEPTH_AA = ShaderCore.isFXAAValid() && data.optBoolean("BUtil_DepthAA", false);
        }

        if (this._button[0] != null) this._button[0].setChecked(this._BLOOM_ENABLED);
        if (this._button[1] != null) this._button[1].setChecked(this._AA_ENABLED);
        if (this._button[2] != null) this._button[2].setChecked(this._AA_CONSOLE);
        if (this._button[3] != null) this._button[3].setChecked(!this._AA_CONSOLE);
        if (this._button[4] != null) this._button[4].setChecked(this._DEPTH_AA);
    }

    public void configSetDefault() {
        this._BLOOM_ENABLED = true;
        this._AA_ENABLED = true;
        this._AA_CONSOLE = false;
        this._DEPTH_AA = false;

        this._button[0].setChecked(true);
        this._button[1].setChecked(true);
        this._button[2].setChecked(false);
        this._button[3].setChecked(true);
        this._button[4].setChecked(false);
    }

    public void configSave(JSONObject data) throws JSONException {
        data.put("BUtil_EnableBloom", this._BLOOM_ENABLED);
        data.put("BUtil_EnableAA", this._AA_ENABLED);
        data.put("BUtil_AAConsole", this._AA_CONSOLE);
        data.put("BUtil_DepthAA", this._DEPTH_AA);
    }

    public boolean init() {
        return true;
    }

    public byte[] applyTexturedAreaLightPreFiltering(int src, int preFiltering, boolean shouldAllocate) {
        return new byte[]{BoxEnum.STATE_FAILED, 0};
    }

    public boolean applyBeforeInfiniteLightShading() {
        return false;
    }

    public boolean applyBeforePointLightShading() {
        return false;
    }

    public boolean applyBeforeSpotLightShading() {
        return false;
    }

    public boolean applyBeforeLinearLightShading() {
        return false;
    }

    public boolean applyBeforeAreaLightShading() {
        return false;
    }

    public void applyAAPass(ViewportAPI viewport, boolean isCampaign, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap) {
        BUtil_GLImpl.StandardShaderPacks.applyFXAA(this._AA_ENABLED, this._AA_CONSOLE, this._DEPTH_AA, colorMap, worldDataMap);
    }

    public void applyBloomPass(ViewportAPI viewport, boolean isCampaign, boolean isMultiPassBloom, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap, int auxFBO, int auxEmissive) {
        if (this._BLOOM_ENABLED) super.applyBloomPass(viewport, isCampaign, isMultiPassBloom, fbo, colorMap, emissiveMap, worldPosMap, worldNormalMap, worldTangentMap, worldMaterialMap, worldDataMap, auxFBO, auxEmissive);
    }

    public String getID() {
        return BoxDatabase.DEFAULT_SHADERPACKS_ID;
    }

    public String getDisplayName() {
        return BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Name");
    }

    public String getDisplayVersion() {
        return BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Version") + Global.getSettings().getModManager().getModSpec(BoxDatabase.MOD_ID).getVersionInfo().getString();
    }

    public int getIconTextureID() {
        return super.getIconTextureID();
    }

    public Color getIconTextureColor() {
        return super.getIconTextureColor();
    }

    public boolean isCombatIlluminationSupported() {
        return false;
    }

    public boolean isCampaignIlluminationSupported() {
        return false;
    }

    public boolean isBloomSupported() {
        return true;
    }

    public boolean isAASupported() {
        return true;
    }

    public boolean isBaseAnisotropySupported() {
        return false;
    }

    public boolean isTexturedAreaLightSupported() {
        return false;
    }

    public boolean isCompleteAnisotropySupported() {
        return false;
    }

    public boolean isUsable(ContextCapabilities glContext) {
        return true;
    }

    public void createDetailsPanel(TooltipMakerAPI tooltip, PositionAPI planePos, PositionAPI elementPos) {
        final Color buttonBg = Global.getSettings().getColor("buttonBg"), buttonBgDark = Global.getSettings().getColor("buttonBgDark"), buttonText = Misc.getButtonTextColor();
        final float padWidth = 10.0f, cbWidth = planePos.getWidth(), cbWidthHalf = (cbWidth - padWidth - 10.0f) * 0.5f;
        tooltip.addPara(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Desc"), 0.0f);

        tooltip.addSectionHeading(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Setting_BloomTitle"), Alignment.MID, 12.0f);
        if (!ShaderCore.isBloomValid()) tooltip.addSectionHeading(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_FeatureWarn"), Misc.getNegativeHighlightColor(), new Color(0, true), Alignment.MID, 5.0f);
        tooltip.addPara(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Setting_BloomDesc"), 5.0f);
        this._button[0] = tooltip.addCheckbox(cbWidth, _CHECKBOX_HEIGHT, BoxConfigs.getString("BUtil_ConfigPanel_ValueValid"), _BUTTON_BLOOM, ButtonAPI.UICheckboxSize.LARGE, 5.0f);
        this._button[0].setEnabled(ShaderCore.isBloomValid());

        tooltip.addSectionHeading(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Setting_AATitle"), Alignment.MID, 12.0f);
        if (!ShaderCore.isFXAAValid()) tooltip.addSectionHeading(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_FeatureWarn"), Misc.getNegativeHighlightColor(), new Color(0, true), Alignment.MID, 5.0f);
        tooltip.addPara(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Setting_AADesc"), 5.0f);
        this._button[1] = tooltip.addCheckbox(cbWidth, _CHECKBOX_HEIGHT, BoxConfigs.getString("BUtil_ConfigPanel_ValueValid"), _BUTTON_AA, ButtonAPI.UICheckboxSize.LARGE, 5.0f);
        this._button[1].setEnabled(ShaderCore.isFXAAValid());
        tooltip.addTitle(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Setting_AAType")).getPosition().belowLeft(this._button[1], 12.0f);
        this._button[2] = tooltip.addAreaCheckbox(Global.getSettings().getString("ui", "BUtil_NotSelectedShaderPacks_Setting_AAC"), _BUTTON_AAC, buttonBg, buttonBgDark, buttonText, cbWidthHalf, _CHECKBOX_HEIGHT, 5.0f);
        this._button[2].setEnabled(ShaderCore.isFXAACValid());
        this._button[3] = tooltip.addAreaCheckbox(Global.getSettings().getString("ui", "BUtil_NotSelectedShaderPacks_Setting_AAQ"), _BUTTON_AAQ, buttonBg, buttonBgDark, buttonText, cbWidthHalf, _CHECKBOX_HEIGHT, 0.0f);
        this._button[3].getPosition().rightOfMid(this._button[2], padWidth);
        this._button[3].setEnabled(ShaderCore.isFXAAQValid());

        tooltip.addTitle(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Setting_DepthAA")).getPosition().belowLeft(this._button[2], 12.0f);
        tooltip.addPara(BoxConfigs.getString("BUtil_NotSelectedShaderPacks_Setting_DepthAADesc"), 5.0f);
        this._button[4] = tooltip.addCheckbox(cbWidth, _CHECKBOX_HEIGHT, BoxConfigs.getString("BUtil_ConfigPanel_ValueValid"), _BUTTON_DEPTHAA, ButtonAPI.UICheckboxSize.LARGE, 5.0f);
        this._button[4].setEnabled(ShaderCore.isFXAAValid());

        this._button[0].setChecked(this._BLOOM_ENABLED);
        this._button[1].setChecked(this._AA_ENABLED);
        this._button[2].setChecked(this._AA_CONSOLE);
        this._button[3].setChecked(!this._AA_CONSOLE);
        this._button[4].setChecked(this._DEPTH_AA);
    }

    public void detailsPanelRenderBelow(float alpha, PositionAPI planePos, PositionAPI elementPos) {
        super.detailsPanelRenderBelow(alpha, planePos, elementPos);
    }

    public boolean detailsPanelProcessInput(List<InputEventAPI> events, PositionAPI planePos, PositionAPI elementPos) {
        return super.detailsPanelProcessInput(events, planePos, elementPos);
    }

    public boolean detailsPanelButtonPressed(Object buttonId, PositionAPI planePos, PositionAPI elementPos) {
        byte id = (byte) buttonId;
        boolean result = false;
        switch (id) {
            case _BUTTON_BLOOM: {
                if (this._button[0] != null) {
                    this._BLOOM_ENABLED = this._button[0].isChecked();
                    result = true;
                }
                break;
            }
            case _BUTTON_AA: {
                if (this._button[1] != null) {
                    this._AA_ENABLED = this._button[1].isChecked();
                    result = true;
                }
                break;
            }
            case _BUTTON_AAC: {
                if (this._button[2] != null) {
                    if (this._button[2].isChecked()) {
                        if (this._button[3] != null) this._button[3].setChecked(false);
                        this._AA_CONSOLE = true;
                        result = true;
                    } else {
                        this._button[2].setChecked(true);
                    }
                }
                break;
            }
            case _BUTTON_AAQ: {
                if (this._button[3] != null) {
                    if (this._button[3].isChecked()) {
                        if (this._button[2] != null) this._button[2].setChecked(false);
                        this._AA_CONSOLE = false;
                        result = true;
                    } else {
                        this._button[3].setChecked(true);
                    }
                }
                break;
            }
            case _BUTTON_DEPTHAA: {
                if (this._button[4] != null) {
                    this._DEPTH_AA = this._button[4].isChecked();
                    result = true;
                }
                break;
            }
        }
        return result;
    }
}
