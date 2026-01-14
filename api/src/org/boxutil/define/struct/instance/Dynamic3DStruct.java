package org.boxutil.define.struct.instance;

import org.boxutil.define.InstanceType;
import org.boxutil.util.CommonUtil;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

/**
 * Just for formatted update, without getter.
 * <pre>
 * {@code
 * // 144 byte
 * struct Dynamic3D { // binding 6
 *     vec4 m00_m01_m02_m21;
 *     vec4 m10_m11_m12_m22;
 *     vec4 m20_rotateRate;
 *
 *     vec4 location_velocityX;
 *     vec4 rotate_velocityY;
 *     vec4 scale_velocityZ;
 *     vec4 scaleRate_rd;
 *
 *     vec4 timer;
 *     uvec4 colorBits;
 * };
 * }
 * </pre>
 */
public class Dynamic3DStruct {
    protected final float[] state = new float[InstanceType.DYNAMIC_3D.getCompactComponent()];

    public Dynamic3DStruct() {
        this.state[19] = this.state[20] = this.state[21] = this.state[22] = -512.0f;
        this.state[23] = this.state[24] = this.state[25] = this.state[26] = Float.intBitsToFloat(0xFFFFFFFF);
        this.state[18] = 21.0f;
    }

    public float[] getData() {
        final float[] result = new float[InstanceType.DYNAMIC_3D.getComponent()];
        System.arraycopy(this.state, 0, result, InstanceType.DYNAMIC_3D.getCompactOffset(), InstanceType.DYNAMIC_3D.getCompactComponent());
        return result;
    }

    public float[] getDataCompact() {
        return this.state;
    }

    public Dynamic3DStruct setLocation(float x, float y, float z) {
        this.state[3] = x;
        this.state[4] = y;
        this.state[5] = z;
        return this;
    }

    public Dynamic3DStruct setLocation(Vector3f location) {
        return this.setLocation(location.x, location.y, location.z);
    }

    public Dynamic3DStruct setRotate(float z, float x, float y) {
        this.state[7] = z;
        this.state[8] = x;
        this.state[9] = y;
        return this;
    }

    public Dynamic3DStruct setRotate(Vector3f rotate) {
        return this.setRotate(rotate.z, rotate.x, rotate.y);
    }

    public Dynamic3DStruct setScale(float x, float y, float z) {
        this.state[11] = x;
        this.state[12] = y;
        this.state[13] = z;
        return this;
    }

    public Dynamic3DStruct setScale(Vector3f scale) {
        return this.setScale(scale.x, scale.y, scale.z);
    }

    public Dynamic3DStruct setVelocity(float x, float y, float z) {
        this.state[6] = x;
        this.state[10] = y;
        this.state[14] = z;
        return this;
    }

    public Dynamic3DStruct setVelocity(Vector3f velocity) {
        return this.setVelocity(velocity.x, velocity.y, velocity.z);
    }

    public Dynamic3DStruct setRotateRate(float z, float x, float y) {
        this.state[0] = z;
        this.state[1] = x;
        this.state[2] = y;
        return this;
    }

    public Dynamic3DStruct setRotateRate(Vector3f rotateRate) {
        return this.setRotate(rotateRate.z, rotateRate.x, rotateRate.y);
    }

    public Dynamic3DStruct setScaleRate(float x, float y, float z) {
        this.state[15] = x;
        this.state[16] = y;
        this.state[17] = z;
        return this;
    }

    public Dynamic3DStruct setScaleRate(Vector3f scaleRate) {
        return this.setScaleRate(scaleRate.x, scaleRate.y, scaleRate.z);
    }

    public Dynamic3DStruct setColorRed(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[23] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic3DStruct setColorRed(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorRed((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic3DStruct setColorRed(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorRed((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic3DStruct setColorRed(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorRed((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic3DStruct setColorGreen(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[24] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic3DStruct setColorGreen(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorGreen((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic3DStruct setColorGreen(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorGreen((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic3DStruct setColorGreen(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorGreen((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic3DStruct setColorBlue(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[25] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic3DStruct setColorBlue(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorBlue((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic3DStruct setColorBlue(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorBlue((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic3DStruct setColorBlue(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorBlue((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic3DStruct setColorAlpha(byte lowColor, byte highColor, byte lowEmissive, byte highEmissive) {
        this.state[26] = CommonUtil.packingBytesToFloat(lowColor, highColor, lowEmissive, highEmissive);
        return this;
    }

    public Dynamic3DStruct setColorAlpha(int lowColor, int highColor, int lowEmissive, int highEmissive) {
        return this.setColorAlpha((byte) lowColor, (byte) highColor, (byte) lowEmissive, (byte) highEmissive);
    }

    public Dynamic3DStruct setColorAlpha(Color lowColor, Color highColor, Color lowEmissive, Color highEmissive) {
        return this.setColorAlpha((byte) lowColor.getRed(), (byte) highColor.getRed(), (byte) lowEmissive.getRed(), (byte) highEmissive.getRed());
    }

    public Dynamic3DStruct setColorAlpha(float lowColor, float highColor, float lowEmissive, float highEmissive) {
        return this.setColorAlpha((int) (lowColor * 255.0f), (int) (highColor * 255.0f), (int) (lowEmissive * 255.0f), (int) (highEmissive * 255.0f));
    }

    public Dynamic3DStruct setTimer(float fadeIn, float full, float fadeOut) {
        if (fadeIn <= 0.0f && full <= 0.0f && fadeOut <= 0.0f) {
            this.state[19] = this.state[20] = this.state[21] = this.state[22] = -512.0f;
            return this;
        }
        this.state[19] = 3.0f;
        if (fadeIn <= 0.0f) {
            this.state[20] = -512.0f;
            this.state[19] = 2.0f;
            if (full <= 0.0f) this.state[19] = 1.0f;
        } else this.state[20] = 1.0f / fadeIn;
        this.state[21] = full <= 0.0f ? -512.0f : 1.0f / full;
        this.state[22] = fadeOut <= 0.0f ? -512.0f : 1.0f / fadeOut;
        return this;
    }
}
