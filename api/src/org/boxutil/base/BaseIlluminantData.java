package org.boxutil.base;

import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.boxutil.base.api.DirectDrawEntity;
import org.boxutil.base.api.IlluminantAPI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.InstanceType;
import org.boxutil.util.CommonUtil;
import org.boxutil.util.RenderingUtil;
import de.unkrig.commons.nullanalysis.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * The default light direction is <code>vec3(1.0, 0.0, 0.0)</code>, use model matrix to rotate it.
 */
public class BaseIlluminantData extends BaseInstanceRenderData implements IlluminantAPI, DirectDrawEntity {
    protected byte stateBaseBit = 0b10; // 0bXX: attenuationMode
    protected final float[] stateBase = new float[]{BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, 128.0f}; // vec4(color), radius

    protected int _StatePackageStack() {
        return 2;
    }

    protected void _resetExc() {
        super._resetExc();
        this.stateBase[0] = BoxEnum.ONE;
        this.stateBase[1] = BoxEnum.ONE;
        this.stateBase[2] = BoxEnum.ONE;
        this.stateBase[3] = BoxEnum.ONE;
        this.stateBase[4] = 128.0f;
        this.stateBaseBit = 0b10;
    }

    public void resetInstanceData() {
        if (BoxConfigs.getCurrShaderPacksContext().haveCustomInstanceDataLayout()) {
            this._sync_lock.lock();
            BoxConfigs.getCurrShaderPacksContext().getCustomInstanceDataLayout().resetInstanceData(this);
            this._sync_lock.unlock();
        } else super.resetInstanceData();
    }

    public void submitInstance() {
        if (BoxConfigs.getCurrShaderPacksContext().haveCustomInstanceDataLayout()) {
            this._sync_lock.lock();
            BoxConfigs.getCurrShaderPacksContext().getCustomInstanceDataLayout().submitInstance(this);
            this._sync_lock.unlock();
        } else super.submitInstance();
    }

    public void mallocInstance(InstanceType target, int dataNum) {
        if (BoxConfigs.getCurrShaderPacksContext().haveCustomInstanceDataLayout()) {
            this._sync_lock.lock();
            BoxConfigs.getCurrShaderPacksContext().getCustomInstanceDataLayout().mallocInstance(this, target, dataNum);
            this._sync_lock.unlock();
        } else super.mallocInstance(target, dataNum);
    }

    public byte getStateBit() {
        return this.stateBaseBit;
    }

    public float[] getColorArray() {
        return new float[]{this.stateBase[0], this.stateBase[1], this.stateBase[2], this.stateBase[3]};
    }

    public Color getColorC() {
        return CommonUtil.toCommonColor(this.getColor());
    }

    public Vector4f getColor() {
        return new Vector4f(this.stateBase[0], this.stateBase[1], this.stateBase[2], this.stateBase[3]);
    }

    public void setColor(@NotNull Vector4f color) {
        this.stateBase[0] = color.x;
        this.stateBase[1] = color.y;
        this.stateBase[2] = color.z;
        this.stateBase[3] = color.w;
    }

    /**
     * @param a strength of light.
     */
    public void setColor(float r, float g, float b, float a) {
        this.stateBase[0] = r;
        this.stateBase[1] = g;
        this.stateBase[2] = b;
        this.stateBase[3] = a;
    }

    public void setColor(Color color) {
        this.stateBase[0] = color.getRed() / 255.0f;
        this.stateBase[1] = color.getGreen() / 255.0f;
        this.stateBase[2] = color.getBlue() / 255.0f;
        this.stateBase[3] = color.getAlpha();
    }

    public float getStrength() {
        return this.stateBase[3];
    }

    public void setStrength(float strength) {
        this.stateBase[3] = strength;
    }

    public float getAttenuationRadius() {
        return this.stateBase[4];
    }

    /**
     * The shading range of the light.<p>
     * Physically incorrect, but good for performance.
     */
    public void setAttenuationRadius(float radius) {
        this.stateBase[4] = radius;
    }

    public void setDefaultAttenuation() {
        this.stateBaseBit &= (byte) 0b11111100;
        this.stateBaseBit |= 0b10;
    }

    public boolean isNoneAttenuation() {
        return (this.stateBaseBit & 0b11) == 0b00;
    }

    public void setNoneAttenuation() {
        this.stateBaseBit &= 0b100;
    }

    public boolean isLinearAttenuation() {
        return (this.stateBaseBit & 0b11) == 0b01;
    }

    public void setLinearAttenuation() {
        this.stateBaseBit &= (byte) 0b11111100;
        this.stateBaseBit |= 0b01;
    }

    public boolean isSquareAttenuation() {
        return (this.stateBaseBit & 0b11) == 0b10;
    }

    public void setSquareAttenuation() {
        this.setDefaultAttenuation();
    }

    public boolean isSqrtAttenuation() {
        return (this.stateBaseBit & 0b11) == 0b11;
    }

    public void setSqrtAttenuation() {
        this.stateBaseBit |= 0b11;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.stateBase, 0, 5);
        this._statePackageBuffer.put(6, this.haveValidInstanceData() ? this.getInstanceTimerOverride() : this.getGlobalTimerAlpha());
        return this._statePackageBuffer;
    }

    @Deprecated
    public int getBlendColorSRC() {
        return GL11.GL_SRC_ALPHA;
    }

    @Deprecated
    public int getBlendColorDST() {
        return GL11.GL_ONE_MINUS_SRC_ALPHA;
    }

    @Deprecated
    public int getBlendAlphaSRC() {
        return GL11.GL_SRC_ALPHA;
    }

    @Deprecated
    public int getBlendAlphaDST() {
        return GL11.GL_ONE_MINUS_SRC_ALPHA;
    }

    @Deprecated
    public int getBlendEquation() {
        return GL14.GL_FUNC_ADD;
    }

    @Deprecated
    public byte getBlendState() {
        return 0;
    }

    @Deprecated
    public void setBlendFunc(int srcFactor, int dstFactor) {}

    @Deprecated
    public void setBlendFuncSeparate(int srcColorFactor, int dstColorFactor, int srcAlphaFactor, int dstAlphaFactor) {}

    @Deprecated
    public void setBlendEquation(int mode) {}

    @Deprecated
    public void setAdditiveBlend() {}

    @Deprecated
    public void setNormalBlend() {}

    @Deprecated
    public void setNegativeBlend() {}

    @Deprecated
    public void setDisableBlend() {}

    @Deprecated
    public Object getLayer() {
        return null;
    }

    @Deprecated
    public CombatEngineLayers getCombatLayer() {
        return RenderingUtil.getHighestCombatLayer();
    }

    @Deprecated
    public CampaignEngineLayers getCampaignLayer() {
        return RenderingUtil.getHighestCampaignLayer();
    }

    @Deprecated
    public void setLayer(Object layer) {}

    @Deprecated
    public void setDefaultCombatLayer() {}

    @Deprecated
    public void setDefaultCampaignLayer() {}
}
