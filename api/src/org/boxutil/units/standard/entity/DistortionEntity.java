package org.boxutil.units.standard.entity;

import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import org.boxutil.base.BaseInstanceRenderData;
import org.boxutil.base.api.DirectDrawEntity;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.DirectEntityType;
import org.boxutil.util.RenderingUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.boxutil.units.standard.attribute.Instance2Data;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Distortion entity is a local post-effect entity.
 */
public class DistortionEntity extends BaseInstanceRenderData implements DirectDrawEntity {
    // vec4(sizeIn, powerIn, powerFull), vec4(sizeFull, powerOut, oneMinusSoftness), vec4(sizeOut, fadeInFactor, fadeOutFactor)
    protected final float[] sizeState_A = new float[]{1.0f, 1.0f, 0.5f, 0.5f, 1.0f, 1.0f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f};
    // vec4(sizeInRatio, sizeFullRatio), vec3(sizeOutRatio, hardnessInner)
    protected final float[] sizeState_B = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f};
    protected final float[] state_C = new float[]{-1.0f, -1.0f, 0.0f, 0.0f}; // arcStart, arcEnd, innerOffsetX, innerOffsetY

    protected int _StatePackageStack() {
        return 6;
    }

    protected void _resetExc() {
        super._resetExc();
        this.state_C[0] = -1.0f;
        this.state_C[1] = -1.0f;
        this.state_C[2] = 0.0f;
        this.state_C[3] = 0.0f;
        this.sizeState_A[0] = BoxEnum.ONE;
        this.sizeState_A[1] = BoxEnum.ONE;
        this.sizeState_A[2] = 0.5f;
        this.sizeState_A[3] = 0.5f;
        this.sizeState_A[4] = BoxEnum.ONE;
        this.sizeState_A[5] = BoxEnum.ONE;
        this.sizeState_A[6] = 0.5f;
        this.sizeState_A[7] = 0.5f;
        this.sizeState_A[8] = BoxEnum.ONE;
        this.sizeState_A[9] = BoxEnum.ONE;
        this.sizeState_A[10] = BoxEnum.ONE;
        this.sizeState_A[11] = BoxEnum.ONE;
        this.sizeState_B[0] = BoxEnum.ZERO;
        this.sizeState_B[1] = BoxEnum.ZERO;
        this.sizeState_B[2] = BoxEnum.ZERO;
        this.sizeState_B[3] = BoxEnum.ZERO;
        this.sizeState_B[4] = BoxEnum.ZERO;
        this.sizeState_B[5] = BoxEnum.ZERO;
        this.sizeState_B[6] = 0.5f;
    }

    public float[] getSizeStateArray() {
        return this.sizeState_A;
    }

    public float[] getSizeInArray() {
        return new float[]{this.sizeState_A[0], sizeState_A[1]};
    }

    public Vector2f getSizeIn() {
        return new Vector2f(this.sizeState_A[0], sizeState_A[1]);
    }

    public void setSizeIn(float widthHalf, float heightHalf) {
        this.sizeState_A[0] = widthHalf;
        this.sizeState_A[1] = heightHalf;
    }

    public void setSizeIn(Vector2f sizeHalf) {
        this.setSizeIn(sizeHalf.x, sizeHalf.y);
    }

    public float getPowerIn() {
        return this.sizeState_A[2];
    }

    public void setPowerIn(float power) {
        this.sizeState_A[2] = power;
    }

    public float[] getSizeFullArray() {
        return new float[]{this.sizeState_A[4], sizeState_A[5]};
    }

    public Vector2f getSizeFull() {
        return new Vector2f(this.sizeState_A[4], sizeState_A[5]);
    }

    public void setSizeFull(float widthHalf, float heightHalf) {
        this.sizeState_A[4] = widthHalf;
        this.sizeState_A[5] = heightHalf;
    }

    public void setSizeFull(Vector2f sizeHalf) {
        this.setSizeFull(sizeHalf.x, sizeHalf.y);
    }

    public float getPowerFull() {
        return this.sizeState_A[3];
    }

    public void setPowerFull(float power) {
        this.sizeState_A[3] = power;
    }

    public float[] getSizeOutArray() {
        return new float[]{this.sizeState_A[8], sizeState_A[9]};
    }

    public Vector2f getSizeOut() {
        return new Vector2f(this.sizeState_A[8], sizeState_A[9]);
    }

    public void setSizeOut(float widthHalf, float heightHalf) {
        this.sizeState_A[8] = widthHalf;
        this.sizeState_A[9] = heightHalf;
    }

    public void setSizeOut(Vector2f sizeHalf) {
        this.setSizeOut(sizeHalf.x, sizeHalf.y);
    }

    public float getPowerOut() {
        return this.sizeState_A[6];
    }

    public void setPowerOut(float power) {
        this.sizeState_A[6] = power;
    }

    public float[] getInnerStateArray() {
        return this.sizeState_B;
    }

    public float[] getInnerInRatioArray() {
        return new float[]{this.sizeState_B[0], sizeState_B[1]};
    }

    public Vector2f getInnerInRatio() {
        return new Vector2f(this.sizeState_B[0], sizeState_B[1]);
    }

    public void setInnerIn(float widthRatio, float heightRatio) {
        this.sizeState_B[0] = widthRatio;
        this.sizeState_B[1] = heightRatio;
    }

    public void setInnerIn(Vector2f ratio) {
        this.setInnerIn(ratio.x, ratio.y);
    }

    public float[] getInnerFullRatioArray() {
        return new float[]{this.sizeState_B[2], sizeState_B[3]};
    }

    public Vector2f getInnerFullRatio() {
        return new Vector2f(this.sizeState_B[2], sizeState_B[3]);
    }

    public void setInnerFull(float widthRatio, float heightRatio) {
        this.sizeState_B[2] = widthRatio;
        this.sizeState_B[3] = heightRatio;
    }

    public void setInnerFull(Vector2f ratio) {
        this.setInnerFull(ratio.x, ratio.y);
    }

    public float[] getInnerOutRatioArray() {
        return new float[]{this.sizeState_B[4], sizeState_B[5]};
    }

    public Vector2f getInnerOutRatio() {
        return new Vector2f(this.sizeState_B[5], sizeState_B[4]);
    }

    public void setInnerOut(float widthRatio, float heightRatio) {
        this.sizeState_B[4] = widthRatio;
        this.sizeState_B[5] = heightRatio;
    }

    public void setInnerOut(Vector2f ratio) {
        this.setInnerOut(ratio.x, ratio.y);
    }

    public float[] getInnerOffsetFactorArray() {
        return new float[]{this.state_C[2], this.state_C[3]};
    }

    public Vector2f getInnerOffsetFactor() {
        return new Vector2f(this.state_C[2], this.state_C[3]);
    }

    public void setInnerOffsetFactor(float x, float y) {
        this.state_C[2] = x;
        this.state_C[3] = y;
    }

    public void setInnerOffsetFactor(Vector2f factor) {
        this.setInnerOffsetFactor(factor.x, factor.y);
    }

    public float getFadeInFactor() {
        return this.sizeState_A[10];
    }

    public void setFadeInFactor(float factor) {
        this.sizeState_A[10] = factor;
    }

    public float getFadeOutFactor() {
        return this.sizeState_A[11];
    }

    public void setFadeOutFactor(float factor) {
        this.sizeState_A[11] = factor;
    }

    public float getArcStart() {
        return (float) Math.toDegrees(Math.acos(this.state_C[0])) * 2.0f;
    }

    public void setArcStart(float angle) {
        this.state_C[0] = (float) Math.cos(Math.toRadians(angle * 0.5f));
    }

    public float getArcStartDirect() {
        return this.state_C[0];
    }

    /**
     * @param angleCosValue half angle value
     */
    public void setArcStartDirect(float angleCosValue) {
        this.state_C[0] = angleCosValue;
    }

    public float getArcEnd() {
        return (float) Math.toDegrees(Math.acos(this.state_C[1])) * 2.0f;
    }

    public void setArcEnd(float angle) {
        this.state_C[1] = (float) Math.cos(Math.toRadians(angle * 0.5f));
    }

    public float getArcEndDirect() {
        return this.state_C[1];
    }

    /**
     * @param angleCosValue half angle value
     */
    public void setArcEndDirect(float angleCosValue) {
        this.state_C[1] = angleCosValue;
    }

    /**
     * @return returns the start angle only.
     */
    @Deprecated
    public float getArc() {
        return this.getArcStart();
    }

    /**
     * Sets the start and end angle both.
     */
    public void setArc(float angle) {
        float value = (float) Math.cos(Math.toRadians(angle * 0.5f));
        this.setArcStartDirect(value);
        this.setArcEndDirect(value);
    }

    /**
     * @return returns the value of the start angle only.
     */
    @Deprecated
    public float getArcDirect() {
        return this.getArcStartDirect();
    }

    /**
     * Sets the start and end angle both.
     * @param angleCosValue half angle value
     */
    public void setArcDirect(float angleCosValue) {
        this.setArcStartDirect(angleCosValue);
        this.setArcEndDirect(angleCosValue);
    }

    public void setDefaultArcEnds() {
        this.state_C[0] = this.state_C[1] = -1.0f;
    }

    public float getRingHardness() {
        return this.sizeState_A[7];
    }

    public void setRingHardness(float hardness) {
        this.sizeState_A[7] = hardness;
    }

    public float getInnerHardness() {
        return this.sizeState_B[6];
    }

    public void setInnerHardness(float hardness) {
        this.sizeState_B[6] = hardness;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.sizeState_A, 0, 12);
        this._statePackageBuffer.put(12, this.sizeState_B, 0, 7);
        this._statePackageBuffer.put(19, this.haveValidInstanceData() ? this.getInstanceTimerOverride() : this.globalTimer[0]);
        this._statePackageBuffer.put(20, this.state_C, 0, 4);
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public Object entityType() {
        return DirectEntityType.DISTORTION;
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

    private static int clampFloat(final float x) {
        return Math.min(Math.max((int) (x * 255.0f), 0), 255);
    }

    /**
     * For put in to {@link Instance2Data#setColor(Color)} etc.
     */
    public static Color fetchInstanceColor(float innerInRatioScale, float innerFullRatioScale, float innerOutRatioScale, float globalPower) {
        return new Color(clampFloat(innerInRatioScale), clampFloat(innerFullRatioScale), clampFloat(innerOutRatioScale), clampFloat(globalPower));
    }

    /**
     * For put in to {@link Instance2Data#setEmissiveColor(Color)} etc.
     */
    public static Color fetchInstanceEmissiveColor(float sizeInScale, float sizeFullScale, float sizeOutScale, float globalHardness) {
        return new Color(clampFloat(sizeInScale), clampFloat(sizeFullScale), clampFloat(sizeOutScale), clampFloat(globalHardness));
    }
}
