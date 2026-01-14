package org.boxutil.define.struct.instance;

import org.boxutil.define.InstanceType;
import org.boxutil.util.CommonUtil;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

/**
 * Just for formatted update, without getter.
 * <pre>
 * {@code
 * // 80 byte
 * struct Dynamic2D { // binding 4
 *     vec4 q22_q23_facing_TrunRate;
 *     vec4 location_Scale;
 *     vec4 velocity_ScaleRate;
 *     vec4 timer;
 *     uvec4 colorBits;
 * };
 * }
 * </pre>
 */
public class Dynamic2DStruct {
    protected final float[] state = new float[InstanceType.DYNAMIC_2D.getCompactComponent()];

    public Dynamic2DStruct() {
        this.state[10] = this.state[11] = this.state[12] = this.state[13] = -512.0f;
        this.state[14] = this.state[15] = this.state[16] = this.state[17] = Float.intBitsToFloat(0xFFFFFFFF);
    }

    public float[] getData() {
        final float[] result = new float[InstanceType.DYNAMIC_2D.getComponent()];
        System.arraycopy(this.state, 0, result, InstanceType.DYNAMIC_2D.getCompactOffset(), InstanceType.DYNAMIC_2D.getCompactComponent());
        return result;
    }

    public float[] getDataCompact() {
        return this.state;
    }

    public Dynamic2DStruct setLocation(float x, float y) {
        this.state[2] = x;
        this.state[3] = y;
        return this;
    }

    public Dynamic2DStruct setLocation(Vector2f location) {
        return this.setLocation(location.x, location.y);
    }

    public Dynamic2DStruct setFacing(float angle) {
        this.state[0] = angle;
        return this;
    }

    public Dynamic2DStruct setScale(float x, float y) {
        this.state[4] = x;
        this.state[5] = y;
        return this;
    }

    public Dynamic2DStruct setScale(Vector2f scale) {
        return this.setScale(scale.x, scale.y);
    }

    public Dynamic2DStruct setVelocity(float x, float y) {
        this.state[6] = x;
        this.state[7] = y;
        return this;
    }

    public Dynamic2DStruct setVelocity(Vector2f velocity) {
        return this.setVelocity(velocity.x, velocity.y);
    }

    public Dynamic2DStruct setTurnRate(float turnRate) {
        this.state[1] = turnRate;
        return this;
    }

    public Dynamic2DStruct setScaleRate(float x, float y) {
        this.state[8] = x;
        this.state[9] = y;
        return this;
    }

    public Dynamic2DStruct setScaleRate(Vector2f scaleRate) {
        return this.setScaleRate(scaleRate.x, scaleRate.y);
    }

    public Dynamic2DStruct setColorRed(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[14] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic2DStruct setColorRed(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorRed((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic2DStruct setColorRed(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorRed((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic2DStruct setColorRed(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorRed((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic2DStruct setColorGreen(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[15] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic2DStruct setColorGreen(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorGreen((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic2DStruct setColorGreen(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorGreen((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic2DStruct setColorGreen(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorGreen((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic2DStruct setColorBlue(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[16] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic2DStruct setColorBlue(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorBlue((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic2DStruct setColorBlue(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorBlue((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic2DStruct setColorBlue(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorBlue((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic2DStruct setColorAlpha(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[17] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic2DStruct setColorAlpha(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorAlpha((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic2DStruct setColorAlpha(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorAlpha((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic2DStruct setColorAlpha(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorAlpha((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic2DStruct setTimer(float fadeIn, float full, float fadeOut) {
        if (fadeIn <= 0.0f && full <= 0.0f && fadeOut <= 0.0f) {
            this.state[10] = this.state[11] = this.state[12] = this.state[13] = -512.0f;
            return this;
        }
        this.state[10] = 3.0f;
        if (fadeIn <= 0.0f) {
            this.state[11] = -512.0f;
            this.state[10] = 2.0f;
            if (full <= 0.0f) this.state[10] = 1.0f;
        } else this.state[11] = 1.0f / fadeIn;
        this.state[12] = full <= 0.0f ? -512.0f : 1.0f / full;
        this.state[13] = fadeOut <= 0.0f ? -512.0f : 1.0f / fadeOut;
        return this;
    }
}
