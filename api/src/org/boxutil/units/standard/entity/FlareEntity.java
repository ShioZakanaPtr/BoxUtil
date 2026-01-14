package org.boxutil.units.standard.entity;

import org.boxutil.define.LayeredEntityType;
import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.BaseInstanceRenderData;
import org.boxutil.define.BoxEnum;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * Flare entity will not to be applying AA if depth based AA is enabled, and always pass illumination.<p>
 * For instance data: <strong>| color => core color | emissive color => fringe color |</strong>
 */
public class FlareEntity extends BaseInstanceRenderData {
    protected final static byte _SMOOTH = 0;
    protected final static byte _SHARP = 1;
    protected final static byte _SMOOTH_DISC = 2;
    protected final static byte _SHARP_DISC = 3;
    // vec4(fringeColor), vec4(coreColor), vec2(size), aspect
    protected final float[] lensState = new float[]{BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE, BoxEnum.ONE};
    // style, flick, syncFlick
    protected final byte[] lensState_B = new byte[]{_SMOOTH, BoxEnum.TRUE, BoxEnum.FALSE};
    // flickerCode, glowPower, flickerAnimationRate, noisePower, flickMix, globalAlpha, discRatioInv
    protected float[] extraState = new float[]{this.hashCode() * 0.00066667f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f};
    protected boolean flickWhenPaused = false;

    protected int _StatePackageStack() {
        return 5;
    }

    protected void _resetExc() {
        super._resetExc();
        this.lensState[8] = BoxEnum.ONE;
        this.lensState[9] = BoxEnum.ONE;
        this.lensState[10] = BoxEnum.ONE;
        this.extraState[0] = this.hashCode() * 0.00066667f;
        this.extraState[1] = 0.0f;
        this.extraState[2] = 1.0f;
        this.extraState[3] = 0.0f;
        this.extraState[4] = 1.0f;
        this.extraState[5] = 1.0f;
        this.extraState[6] = 1.0f;
        this.lensState_B[0] = _SMOOTH;
        this.lensState_B[1] = BoxEnum.TRUE;
        this.lensState_B[2] = BoxEnum.FALSE;
        this.lensState[0] = BoxEnum.ONE;
        this.lensState[1] = BoxEnum.ONE;
        this.lensState[2] = BoxEnum.ONE;
        this.lensState[3] = BoxEnum.ONE;
        this.lensState[4] = BoxEnum.ONE;
        this.lensState[5] = BoxEnum.ONE;
        this.lensState[6] = BoxEnum.ONE;
        this.lensState[7] = BoxEnum.ONE;
        this.flickWhenPaused = false;
    }

    public float[] getSizeArray() {
        return new float[]{this.lensState[8] * 2.0f, this.lensState[9] * 2.0f};
    }

    public Vector2f getSize() {
        return new Vector2f(this.lensState[8] * 2.0f, this.lensState[9] * 2.0f);
    }

    public float getWidth() {
        return this.lensState[8] * 2.0f;
    }

    public void setWidth(float width) {
        this.lensState[8] = width * 0.5f;
    }

    public void setWidthDirect(float widthHalf) {
        this.lensState[8] = widthHalf;
    }

    public float getHeight() {
        return this.lensState[9] * 2.0f;
    }

    public void setHeight(float height) {
        this.lensState[9] = height * 0.5f;
    }

    public void setHeightDirect(float heightHalf) {
        this.lensState[9] = heightHalf;
    }

    public void setSize(float width, float height) {
        this.setWidth(width);
        this.setHeight(height);
    }

    public void setSizeDirect(float widthHalf, float heightHalf) {
        this.setWidthDirect(widthHalf);
        this.setHeightDirect(heightHalf);
    }

    public void setSize(Vector2f size) {
        this.setSize(size.x, size.y);
    }

    public byte getStyleBit() {
        return this.lensState_B[0];
    }

    public boolean isSmooth() {
        return this.lensState_B[0] == _SMOOTH;
    }

    public void setSmooth() {
        this.lensState_B[0] = _SMOOTH;
    }

    public boolean isSharp() {
        return this.lensState_B[0] == _SHARP;
    }

    public void setSharp() {
        this.lensState_B[0] = _SHARP;
    }

    public boolean isSmoothDisc() {
        return this.lensState_B[0] == _SMOOTH_DISC;
    }

    public void setSmoothDisc() {
        this.lensState_B[0] = _SMOOTH_DISC;
    }

    public boolean isSharpDisc() {
        return this.lensState_B[0] == _SHARP_DISC;
    }

    public void setSharpDisc() {
        this.lensState_B[0] = _SHARP_DISC;
    }

    public float getNoisePower() {
        return this.extraState[3];
    }

    /**
     * Jitter at width.
     */
    public void setNoisePower(float power) {
        this.extraState[3] = power;
    }

    public boolean isFlick() {
        return this.lensState_B[1] == BoxEnum.TRUE;
    }

    public void setFlick(boolean flick) {
        this.lensState_B[1] = flick ? BoxEnum.TRUE : BoxEnum.FALSE;
    }

    public boolean isSyncFlick() {
        return this.lensState_B[2] == BoxEnum.TRUE;
    }

    /**
     * For use instance data to rendering.
     */
    public void setSyncFlick(boolean syncFlick) {
        this.lensState_B[2] = syncFlick ? BoxEnum.TRUE : BoxEnum.FALSE;
    }

    public boolean isFlickWhenPaused() {
        return this.flickWhenPaused;
    }

    public void setFlickWhenPaused(boolean flick) {
        this.flickWhenPaused = flick;
    }

    public float getFlickMixValue() {
        return this.extraState[4];
    }

    public void setFlickMixValue(float mix) {
        this.extraState[4] = mix;
    }

    public float getFlickerAnimationRateMulti() {
        return this.extraState[2];
    }

    public void setFlickerAnimationRateMulti(float rate) {
        this.extraState[2] = rate;
    }

    public float getAspect() {
        return this.lensState[10];
    }

    /**
     * For sharp-disc and smooth-disc mode.
     *
     * @param value (width / height) for normally
     */
    public void setAspect(float value) {
        this.lensState[10] = value;
    }

    /**
     * Not recommended when rendering by instance data.
     */
    public void autoAspect() {
        this.lensState[10] = this.lensState[8] / this.lensState[9] * 0.25f;
    }

    public float[] getFringeColorArray() {
        return new float[]{this.lensState[0], this.lensState[1], this.lensState[2], this.lensState[3]};
    }

    public Vector4f getFringeColor() {
        return new Vector4f(this.lensState[0], this.lensState[1], this.lensState[2], this.lensState[3]);
    }

    public float getFringeAlpha() {
        return this.lensState[3];
    }

    public void setFringeColor(@NotNull Vector4f color) {
        this.lensState[0] = color.x;
        this.lensState[1] = color.y;
        this.lensState[2] = color.z;
        this.lensState[3] = color.w;
    }

    public void setFringeColor(float r, float g, float b, float a) {
        this.lensState[0] = r;
        this.lensState[1] = g;
        this.lensState[2] = b;
        this.lensState[3] = a;
    }

    public void setFringeColor(Color color) {
        this.lensState[0] = color.getRed() / 255.0f;
        this.lensState[1] = color.getGreen() / 255.0f;
        this.lensState[2] = color.getBlue() / 255.0f;
        this.lensState[3] = color.getAlpha() / 255.0f;
    }

    public void setFringeAlpha(float a) {
        this.lensState[3] = a;
    }

    public void setFringeAlphaI(int a) {
        this.lensState[3] = a / 255.0f;
    }

    public float[] getCoreColorArray() {
        return new float[]{this.lensState[4], this.lensState[5], this.lensState[6], this.lensState[7]};
    }

    public Vector4f getCoreColor() {
        return new Vector4f(this.lensState[4], this.lensState[5], this.lensState[6], this.lensState[7]);
    }

    public float getCoreAlpha() {
        return this.lensState[7];
    }

    public void setCoreColor(@NotNull Vector4f color) {
        this.lensState[4] = color.x;
        this.lensState[5] = color.y;
        this.lensState[6] = color.z;
        this.lensState[7] = color.w;
    }

    public void setCoreColor(float r, float g, float b, float a) {
        this.lensState[0] = r;
        this.lensState[1] = g;
        this.lensState[2] = b;
        this.lensState[3] = a;
    }

    public void setCoreColor(Color color) {
        this.lensState[4] = color.getRed() / 255.0f;
        this.lensState[5] = color.getGreen() / 255.0f;
        this.lensState[6] = color.getBlue() / 255.0f;
        this.lensState[7] = color.getAlpha() / 255.0f;
    }

    public void setCoreAlpha(float a) {
        this.lensState[7] = a;
    }

    public void setCoreAlphaI(int a) {
        this.lensState[7] = a / 255.0f;
    }

    /**
     * Default value is zero.
     */
    public float getGlowPower() {
        return this.extraState[1];
    }

    /**
     * @param glowPower Decided by final of emissive level; 0 to 1.0f
     */
    public void setGlowPower(float glowPower) {
        this.extraState[1] = glowPower;
    }

    public float getDiscRatio() {
        return 1.0f / this.extraState[6];
    }

    /**
     * For xxDisc mode.
     */
    public void setDiscRatio(float ratio) {
        if (ratio == 0.0f) this.extraState[6] = 0.0f;
        else this.extraState[6] = 1.0f / ratio;
    }

    public float getGlobalAlpha() {
        return this.extraState[5];
    }

    public void setGlobalAlpha(float alpha) {
        this.extraState[5] = alpha;
    }

    public float getCurrentFlickerSyncValue() {
        return this.extraState[0];
    }

    /**
     * @param code java instance {@link #hashCode()} * 0.00066667f for default.
     */
    public void setFlickerSyncCode(int code) {
        this.extraState[0] = code;
    }

    public FloatBuffer pickDataPackage_vec4() {
        this._statePackageBuffer.put(0, this.lensState, 0, 11);
        this._statePackageBuffer.put(11, this.lensState_B[1] * 10 + this.lensState_B[2]);
        this._statePackageBuffer.put(12, this.haveValidInstanceData() ? this.getInstanceTimerOverride() : this.getGlobalTimerAlpha());
        this._statePackageBuffer.put(13, this.extraState, 0, 7);
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
        return this._statePackageBuffer;
    }

    public Object entityType() {
        return LayeredEntityType.FLARE;
    }
}
