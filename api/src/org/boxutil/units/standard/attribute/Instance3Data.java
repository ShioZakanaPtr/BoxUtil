package org.boxutil.units.standard.attribute;

import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.util.CommonUtil;
import de.unkrig.commons.nullanalysis.NotNull;
import org.boxutil.base.api.InstanceDataAPI;
import org.boxutil.define.BoxDatabase;
import org.boxutil.define.BoxEnum;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

/**
 * <strong>Priority use this if renders more than one entity.</strong>
 */
public class Instance3Data implements InstanceDataAPI {
    protected float alphaFixed = 2.0f;
    protected final float[] location = new float[3];
    protected final float[] rotate = new float[3];
    protected final float[] scale = new float[3];
    protected final float[] velocity = new float[3];
    protected final float[] rotateRate = new float[3];
    protected final float[] scaleRate = new float[3];
    protected final float[] timer = new float[]{-512.0f, -512.0f, -512.0f, -512.0f};
    // vec4(lowColor), vec4(highColor), vec4(lowEmissive), vec4(highEmissive)
    protected final byte[] colorState = new byte[]{BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR, BoxEnum.ONE_COLOR};

    public Instance3Data() {}

    public Instance3Data(Instance3Data data) {
        System.arraycopy(data.location, 0, this.location, 0, this.location.length);
        System.arraycopy(data.rotate, 0, this.rotate, 0, this.rotate.length);
        System.arraycopy(data.scale, 0, this.scale, 0, this.scale.length);
        System.arraycopy(data.velocity, 0, this.velocity, 0, this.velocity.length);
        System.arraycopy(data.rotateRate, 0, this.rotateRate, 0, this.rotateRate.length);
        System.arraycopy(data.scaleRate, 0, this.scaleRate, 0, this.scaleRate.length);
        System.arraycopy(data.timer, 0, this.timer, 0, this.timer.length);
        this.alphaFixed = data.alphaFixed;
        System.arraycopy(data.colorState, 0, this.colorState, 0, this.colorState.length);
    }

    public void reset() {
        this.alphaFixed = 2.0f;
        this.location[0] = 0.0f;
        this.location[1] = 0.0f;
        this.location[2] = 0.0f;
        this.rotate[0] = 0.0f;
        this.rotate[1] = 0.0f;
        this.rotate[2] = 0.0f;
        this.scale[0] = 0.0f;
        this.scale[1] = 0.0f;
        this.scale[2] = 0.0f;
        this.velocity[0] = 0.0f;
        this.velocity[1] = 0.0f;
        this.velocity[2] = 0.0f;
        this.rotateRate[0] = 0.0f;
        this.rotateRate[1] = 0.0f;
        this.rotateRate[2] = 0.0f;
        this.scaleRate[0] = 0.0f;
        this.scaleRate[1] = 0.0f;
        this.scaleRate[2] = 0.0f;
        this.timer[0] = -512.0f;
        this.timer[1] = -512.0f;
        this.timer[2] = -512.0f;
        this.timer[3] = -512.0f;
        this.colorState[0] = BoxEnum.ONE_COLOR;
        this.colorState[1] = BoxEnum.ONE_COLOR;
        this.colorState[2] = BoxEnum.ONE_COLOR;
        this.colorState[3] = BoxEnum.ONE_COLOR;
        this.colorState[4] = BoxEnum.ONE_COLOR;
        this.colorState[5] = BoxEnum.ONE_COLOR;
        this.colorState[6] = BoxEnum.ONE_COLOR;
        this.colorState[7] = BoxEnum.ONE_COLOR;
        this.colorState[8] = BoxEnum.ONE_COLOR;
        this.colorState[9] = BoxEnum.ONE_COLOR;
        this.colorState[10] = BoxEnum.ONE_COLOR;
        this.colorState[11] = BoxEnum.ONE_COLOR;
        this.colorState[12] = BoxEnum.ONE_COLOR;
        this.colorState[13] = BoxEnum.ONE_COLOR;
        this.colorState[14] = BoxEnum.ONE_COLOR;
        this.colorState[15] = BoxEnum.ONE_COLOR;
    }

    public float[] _pickDynamic_ssbo() {
        return new float[]{
                this.rotateRate[0], this.rotateRate[1], this.rotateRate[2],
                this.location[0], this.location[1], this.location[2], this.velocity[0],
                this.rotate[0], this.rotate[1], this.rotate[2], this.velocity[1],
                this.scale[0], this.scale[1], this.scale[2], this.velocity[2],
                this.scaleRate[0], this.scaleRate[1], this.scaleRate[2], 0.0f,
                this.timer[0], this.timer[1], this.timer[2], this.timer[3],
                CommonUtil.packingBytesToFloat(this.colorState[0], this.colorState[4], this.colorState[8], this.colorState[12]),
                CommonUtil.packingBytesToFloat(this.colorState[1], this.colorState[5], this.colorState[9], this.colorState[13]),
                CommonUtil.packingBytesToFloat(this.colorState[2], this.colorState[6], this.colorState[10], this.colorState[14]),
                CommonUtil.packingBytesToFloat(this.colorState[3], this.colorState[7], this.colorState[11], this.colorState[15])
        };
    }

    public float[] _pickFixed_ssbo() {
        return new float[]{
                this.rotate[0], this.rotate[1], this.rotate[2], this.location[0],
                this.scale[0], this.scale[1], this.scale[2], this.location[1],
                this.alphaFixed, this.location[2],
                CommonUtil.packingBytesToFloat(this.colorState[4], this.colorState[5], this.colorState[6], this.colorState[7]),
                CommonUtil.packingBytesToFloat(this.colorState[12], this.colorState[13], this.colorState[14], this.colorState[15])
        };
    }

    @Deprecated
    public float[] pickFinal_vec4() {
        return null;
    }

    @Deprecated
    public float[][] pickFixedFinal_vec4() {
        return null;
    }

    @Deprecated
    public float[] pickTimer_vec4() {
        return null;
    }

    @Deprecated
    public float[][] pickState_vec4() {
        return null;
    }

    @Deprecated
    public byte[][] pickColor_vec4x4() {
        return null;
    }

    @Deprecated
    public byte[][] pickFixedColor_vec4x2() {
        return null;
    }

    @Deprecated
    public float[] getState() {
        return null;
    }

    @Deprecated
    public byte[] getColorState() {
        return this.colorState;
    }

    @Deprecated
    public float[] getBaseState() {
        return null;
    }

    public float[] getLocationArray() {
        return new float[]{this.location[0], this.location[1], this.location[2]};
    }

    public Vector3f getLocation() {
        return new Vector3f(this.location[0], this.location[1], this.location[2]);
    }

    public void setLocation(@NotNull Vector3f location) {
        this.setLocation(location.x, location.y, location.z);
    }

    public void setLocation(@NotNull Vector2f location) {
        this.setLocation(location.x, location.y);
    }

    public void setLocation(float x, float y) {
        this.location[0] = x;
        this.location[1] = y;
    }

    public void setLocation(float x, float y, float z) {
        this.setLocation(x, y);
        this.location[2] = z;
    }

    @Deprecated
    public float[] getLocationArrayDirect() {
        return new float[]{this.location[0], this.location[1], this.location[2]};
    }

    @Deprecated
    public Vector3f getLocationDirect() {
        return new Vector3f(this.location[0], this.location[1], this.location[2]);
    }

    @Deprecated
    public void setLocationDirect(@NotNull Vector3f location) {
        this.setLocationDirect(location.x, location.y, location.z);
    }

    @Deprecated
    public void setLocationDirect(@NotNull Vector2f location) {
        this.setLocationDirect(location.x, location.y);
    }

    @Deprecated
    public void setLocationDirect(float x, float y, float z) {
        this.setLocationDirect(x, y);
        this.location[2] = z;
    }

    @Deprecated
    public void setLocationDirect(float x, float y) {
        this.location[0] = x;
        this.location[1] = y;
    }

    public float[] getRotateArrayZXY() {
        return new float[]{this.rotate[0], this.rotate[1], this.rotate[2]};
    }

    public Vector3f getRotateZXY() {
        return new Vector3f(this.rotate[0], this.rotate[1], this.rotate[2]);
    }

    public void setRotateZXY(@NotNull Vector3f rotate) {
        this.setRotateZXY(rotate.x, rotate.y, rotate.z);
    }

    public void setRotateZXY(float yaw, float pitch, float roll) {
        this.setFacing(yaw);
        this.rotate[0] = pitch;
        this.rotate[1] = roll;
    }

    public void setFacing(float facing) {
        this.rotate[2] = facing;
    }

    public float[] getScaleArray() {
        return new float[]{this.scale[0], this.scale[1], this.scale[2]};
    }

    public Vector3f getScale() {
        return new Vector3f(this.scale[0], this.scale[1], this.scale[2]);
    }

    public void setScale(@NotNull Vector3f rotate) {
        this.setScale(rotate.x, rotate.y, rotate.z);
    }

    public void setScale(float x, float y, float z) {
        this.setScaleVanilla(x, y);
        this.scale[2] = z;
    }

    public void setScaleVanilla(float x, float y) {
        this.scale[0] = x;
        this.scale[1] = y;
    }

    public void setScaleAll(float factor) {
        this.setScale(factor, factor, factor);
    }

    @Deprecated
    public float[] getDynamicState() {
        return null;
    }

    public float[] getVelocityArray() {
        return this.velocity;
    }

    public Vector3f getVelocity() {
        return new Vector3f(this.velocity[0], this.velocity[1], this.velocity[2]);
    }

    public void setVelocity(@NotNull Vector3f location) {
        this.setVelocity(location.x, location.y, location.z);
    }

    public void setVelocity(@NotNull Vector2f location) {
        this.setVelocity(location.x, location.y);
    }

    public void setVelocity(float x, float y, float z) {
        this.setVelocity(x, y);
        this.velocity[2] = z;
    }

    public void setVelocity(float x, float y) {
        this.velocity[0] = x;
        this.velocity[1] = y;
    }

    @Deprecated
    public void setVelocityDirect(@NotNull Vector3f location) {
        this.setVelocityDirect(location.x, location.y, location.z);
    }

    @Deprecated
    public void setVelocityDirect(@NotNull Vector2f location) {
        this.setVelocityDirect(location.x, location.y);
    }

    @Deprecated
    public void setVelocityDirect(float x, float y, float z) {
        this.setVelocityDirect(x, y);
        this.velocity[2] = z;
    }

    @Deprecated
    public void setVelocityDirect(float x, float y) {
        this.velocity[0] = x;
        this.velocity[1] = y;
    }

    public float[] getRotateRateArrayZXY() {
        return new float[]{this.rotateRate[0], this.rotateRate[1], this.rotateRate[2]};
    }

    public Vector3f getRotateRateZXY() {
        return new Vector3f(this.rotateRate[0], this.rotateRate[1], this.rotateRate[2]);
    }

    public void setRotateRateZXY(@NotNull Vector3f rotate) {
        this.setRotateRateZXY(rotate.x, rotate.y, rotate.z);
    }

    public void setRotateRateZXY(float yaw, float pitch, float roll) {
        this.setTurnRate(yaw);
        this.rotateRate[0] = pitch;
        this.rotateRate[1] = roll;
    }

    public void setTurnRate(float facing) {
        this.rotateRate[2] = facing;
    }

    public float[] getScaleRateArray() {
        return new float[]{this.scaleRate[0], this.scaleRate[1], this.scaleRate[2]};
    }

    public Vector3f getScaleRate() {
        return new Vector3f(this.scaleRate[0], this.scaleRate[1], this.scaleRate[2]);
    }

    public void setScaleRate(@NotNull Vector3f rotate) {
        this.setScaleRate(rotate.x, rotate.y, rotate.z);
    }

    public void setScaleRate(float x, float y, float z) {
        this.setScaleRateVanilla(x, y);
        this.scaleRate[2] = z;
    }

    public void setScaleRateVanilla(float x, float y) {
        this.scaleRate[0] = x;
        this.scaleRate[1] = y;
    }

    public void setScaleRateAll(float factor) {
        this.setScaleRate(factor, factor, factor);
    }

    /**
     * @return high color.
     */
    public byte[] getColorArray() {
        return new byte[]{this.colorState[4], this.colorState[5], this.colorState[6], this.colorState[7]};
    }

    public byte[] getLowColorArray() {
        return new byte[]{this.colorState[0], this.colorState[1], this.colorState[2], this.colorState[3]};
    }

    public byte[] getHighColorArray() {
        return this.getColorArray();
    }

    /**
     * @return high color.
     */
    public Color getColor() {
        return new Color(
                this.colorState[4] & 0xFF,
                this.colorState[5] & 0xFF,
                this.colorState[6] & 0xFF,
                this.colorState[7] & 0xFF);
    }

    public Color getLowColor() {
        return new Color(
                this.colorState[0] & 0xFF,
                this.colorState[1] & 0xFF,
                this.colorState[2] & 0xFF,
                this.colorState[3] & 0xFF);
    }

    public Color getHighColor() {
        return this.getColor();
    }

    /**
     * @return high color.
     */
    public Vector4f getColor4f() {
        return new Vector4f(
                (this.colorState[4] & 0xFF) / 255.0f,
                (this.colorState[5] & 0xFF) / 255.0f,
                (this.colorState[6] & 0xFF) / 255.0f,
                (this.colorState[7] & 0xFF) / 255.0f);
    }

    public Vector4f getLowColor4f() {
        return new Vector4f(
                (this.colorState[0] & 0xFF) / 255.0f,
                (this.colorState[1] & 0xFF) / 255.0f,
                (this.colorState[2] & 0xFF) / 255.0f,
                (this.colorState[3] & 0xFF) / 255.0f);
    }

    public Vector4f getHighColor4f() {
        return this.getColor4f();
    }

    /**
     * @return high color.
     */
    public byte getColorAlpha() {
        return this.colorState[7];
    }

    public byte getLowColorAlpha() {
        return this.colorState[3];
    }

    public byte getHighColorAlpha() {
        return this.getColorAlpha();
    }

    /**
     * @return high color.
     */
    public int getColorAlphaI() {
        return this.getColorAlpha() & 0xFF;
    }

    public int getLowColorAlphaI() {
        return this.getLowColorAlpha() & 0xFF;
    }

    public int getHighColorAlphaI() {
        return this.getColorAlphaI();
    }

    /**
     * @return high color.
     */
    public float getColorAlphaF() {
        return this.getColorAlphaI() / 255.0f;
    }

    public float getLowColorAlphaF() {
        return this.getLowColorAlphaI() / 255.0f;
    }

    public float getHighColorAlphaF() {
        return this.getColorAlphaF();
    }

    public void setLowColor(byte r, byte g, byte b, byte a) {
        this.colorState[0] = r;
        this.colorState[1] = g;
        this.colorState[2] = b;
        this.colorState[3] = a;
    }

    public void setHighColor(byte r, byte g, byte b, byte a) {
        this.colorState[4] = r;
        this.colorState[5] = g;
        this.colorState[6] = b;
        this.colorState[7] = a;
    }

    /**
     * Set both.
     */
    public void setColor(byte r, byte g, byte b, byte a) {
        this.setLowColor(r, g, b, a);
        this.setHighColor(r, g, b, a);
    }

    /**
     * Set both.
     */
    public void setColor(int r, int g, int b, int a) {
        this.setColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public void setLowColor(int r, int g, int b, int a) {
        this.setLowColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public void setHighColor(int r, int g, int b, int a) {
        this.setHighColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    /**
     * Set both.
     */
    public void setColor(@NotNull Color color) {
        this.setColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public void setLowColor(@NotNull Color color) {
        this.setLowColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public void setHighColor(@NotNull Color color) {
        this.setHighColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    /**
     * Set both, the value 0.0 to 1.0
     */
    public void setColor(float r, float g, float b, float a) {
        this.setColor((byte) (r * 255.0f), (byte) (g * 255.0f), (byte) (b * 255.0f), (byte) (a * 255.0f));
    }

    public void setLowColor(float r, float g, float b, float a) {
        this.setLowColor((byte) (r * 255.0f), (byte) (g * 255.0f), (byte) (b * 255.0f), (byte) (a * 255.0f));
    }

    public void setHighColor(float r, float g, float b, float a) {
        this.setHighColor((byte) (r * 255.0f), (byte) (g * 255.0f), (byte) (b * 255.0f), (byte) (a * 255.0f));
    }

    public void setLowAlpha(byte a) {
        this.colorState[3] = a;
    }

    public void setHighAlpha(byte a) {
        this.colorState[7] = a;
    }

    /**
     * Set both, the value 0.0 to 1.0
     */
    public void setAlpha(byte a) {
        this.setLowAlpha(a);
        this.setHighAlpha(a);
    }

    public void setAlpha(int a) {
        this.setAlpha((byte) a);
    }

    public void setLowAlpha(int a) {
        this.setLowAlpha((byte) a);
    }

    public void setHighAlpha(int a) {
        this.setHighAlpha((byte) a);
    }

    /**
     * Set both, the value 0.0 to 1.0
     */
    public void setAlpha(float a) {
        this.setAlpha((byte) (a * 255.0f));
    }

    public void setLowAlpha(float a) {
        this.setLowAlpha((byte) (a * 255.0f));
    }

    public void setHighAlpha(float a) {
        this.setHighAlpha((byte) (a * 255.0f));
    }

    /**
     * @return high emissive color.
     */
    public byte[] getEmissiveColorArray() {
        return new byte[]{this.colorState[12], this.colorState[13], this.colorState[14], this.colorState[15]};
    }

    public byte[] getLowEmissiveColorArray() {
        return new byte[]{this.colorState[8], this.colorState[9], this.colorState[10], this.colorState[11]};
    }

    public byte[] getHighEmissiveColorArray() {
        return this.getEmissiveColorArray();
    }

    /**
     * @return high emissive color.
     */
    public Color getEmissiveColor() {
        return new Color(
                this.colorState[12] & 0xFF,
                this.colorState[13] & 0xFF,
                this.colorState[14] & 0xFF,
                this.colorState[15] & 0xFF);
    }

    public Color getLowEmissiveColor() {
        return new Color(
                this.colorState[8] & 0xFF,
                this.colorState[9] & 0xFF,
                this.colorState[10] & 0xFF,
                this.colorState[11] & 0xFF);
    }

    public Color getHighEmissiveColor() {
        return this.getEmissiveColor();
    }

    /**
     * @return high emissive color.
     */
    public Vector4f getEmissiveColor4f() {
        return new Vector4f(
                (this.colorState[12] & 0xFF) / 255.0f,
                (this.colorState[13] & 0xFF) / 255.0f,
                (this.colorState[14] & 0xFF) / 255.0f,
                (this.colorState[15] & 0xFF) / 255.0f);
    }

    public Vector4f getLowEmissiveColor4f() {
        return new Vector4f(
                (this.colorState[8] & 0xFF) / 255.0f,
                (this.colorState[9] & 0xFF) / 255.0f,
                (this.colorState[10] & 0xFF) / 255.0f,
                (this.colorState[11] & 0xFF) / 255.0f);
    }

    public Vector4f getHighEmissiveColor4f() {
        return this.getEmissiveColor4f();
    }

    /**
     * @return high emissive color.
     */
    public byte getEmissiveColorAlpha() {
        return this.colorState[15];
    }

    public byte getLowEmissiveColorAlpha() {
        return this.colorState[11];
    }

    public byte getHighEmissiveColorAlpha() {
        return this.getEmissiveColorAlpha();
    }

    /**
     * @return high emissive color.
     */
    public int getEmissiveColorAlphaI() {
        return this.getEmissiveColorAlpha() & 0xFF;
    }

    public int getLowEmissiveColorAlphaI() {
        return this.getLowEmissiveColorAlpha() & 0xFF;
    }

    public int getHighEmissiveColorAlphaI() {
        return this.getEmissiveColorAlphaI();
    }

    /**
     * @return high emissive color.
     */
    public float getEmissiveColorAlphaF() {
        return this.getEmissiveColorAlphaI() / 255.0f;
    }

    public float getLowEmissiveColorAlphaF() {
        return this.getLowEmissiveColorAlphaI() / 255.0f;
    }

    public float getHighEmissiveColorAlphaF() {
        return this.getEmissiveColorAlphaF();
    }

    public void setLowEmissiveColor(byte r, byte g, byte b, byte a) {
        this.colorState[8] = r;
        this.colorState[9] = g;
        this.colorState[10] = b;
        this.colorState[11] = a;
    }

    public void setHighEmissiveColor(byte r, byte g, byte b, byte a) {
        this.colorState[12] = r;
        this.colorState[13] = g;
        this.colorState[14] = b;
        this.colorState[15] = a;
    }

    /**
     * Set both.
     */
    public void setEmissiveColor(byte r, byte g, byte b, byte a) {
        this.setLowEmissiveColor(r, g, b, a);
        this.setHighEmissiveColor(r, g, b, a);
    }

    public void setLowEmissiveColor(int r, int g, int b, int a) {
        this.setLowEmissiveColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public void setHighEmissiveColor(int r, int g, int b, int a) {
        this.setHighEmissiveColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    /**
     * Set both.
     */
    public void setEmissiveColor(int r, int g, int b, int a) {
        this.setEmissiveColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    /**
     * Set both.
     */
    public void setEmissiveColor(@NotNull Color color) {
        this.setEmissiveColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public void setLowEmissiveColor(@NotNull Color color) {
        this.setLowEmissiveColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public void setHighEmissiveColor(@NotNull Color color) {
        this.setHighEmissiveColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public void setLowEmissiveColor(float r, float g, float b, float a) {
        this.setLowEmissiveColor((byte) (r * 255.0f), (byte) (g * 255.0f), (byte) (b * 255.0f), (byte) (a * 255.0f));
    }

    public void setHighEmissiveColor(float r, float g, float b, float a) {
        this.setHighEmissiveColor((byte) (r * 255.0f), (byte) (g * 255.0f), (byte) (b * 255.0f), (byte) (a * 255.0f));
    }

    /**
     * Set both, the value 0.0 to 1.0
     */
    public void setEmissiveColor(float r, float g, float b, float a) {
        this.setEmissiveColor((byte) (r * 255.0f), (byte) (g * 255.0f), (byte) (b * 255.0f), (byte) (a * 255.0f));
    }

    public void setLowEmissiveAlpha(byte a) {
        this.colorState[11] = a;
    }

    public void setHighEmissiveAlpha(byte a) {
        this.colorState[15] = a;
    }

    public void setEmissiveAlpha(byte a) {
        this.setLowEmissiveAlpha(a);
        this.setHighEmissiveAlpha(a);
    }

    public void setLowEmissiveAlpha(int a) {
        this.setLowEmissiveAlpha((byte) a);
    }

    public void setHighEmissiveAlpha(int a) {
        this.setHighEmissiveAlpha((byte) a);
    }

    public void setEmissiveAlpha(int a) {
        this.setEmissiveAlpha((byte) a);
    }

    public void setLowEmissiveAlpha(float a) {
        this.setLowEmissiveAlpha((byte) (a * 255.0f));
    }

    public void setHighEmissiveAlpha(float a) {
        this.setHighEmissiveAlpha((byte) (a * 255.0f));
    }

    /**
     * Set both, the value 0.0 to 1.0
     */
    public void setEmissiveAlpha(float a) {
        this.setEmissiveAlpha((byte) (a * 255.0f));
    }

    public float[] getTimer() {
        return this.timer;
    }

    /**
     * Only for dynamic data, invalid call for fixed data.
     */
    public void setTimer(float fadeIn, float full, float fadeOut) {
        if (fadeIn <= 0.0f && full <= 0.0f && fadeOut <= 0.0f) {
            this.timer[0] = this.timer[1] = this.timer[2] = this.timer[3] = -512.0f;
            return;
        }
        this.timer[0] = 3.0f;
        if (fadeIn <= 0.0f) {
            this.timer[1] = -512.0f;
            this.timer[0] = 2.0f;
            if (full <= 0.0f) this.timer[0] = 1.0f;
        } else this.timer[1] = 1.0f / fadeIn;
        this.timer[2] = full <= 0.0f ? -512.0f : 1.0f / full;
        this.timer[3] = fadeOut <= 0.0f ? -512.0f : 1.0f / fadeOut;
    }

    /**
     * Only for fixed data, invalid call for dynamic data.
     *
     * @param state valid state: {@link BoxEnum#TIMER_IN}, {@link BoxEnum#TIMER_FULL}, {@link BoxEnum#TIMER_OUT}.
     */
    public void setFixedInstanceAlpha(float alpha, byte state) {
        this.alphaFixed = Math.max(Math.min(alpha, 1.0f), 0.0f);
        if (this.alphaFixed >= 0.0f) {
            if (state == BoxEnum.TIMER_FULL) this.alphaFixed += 1.0f;
            else if (state != BoxEnum.TIMER_IN) this.alphaFixed += 2.0f;
        }
    }

    public float getFixedInstanceAlpha() {
        return this.alphaFixed;
    }

    /**
     * Only for fixed data, invalid call for dynamic data.
     */
    public void copyFixedInstanceAlphaState(InstanceDataAPI instanceData) {
        this.alphaFixed = instanceData.getFixedInstanceAlpha();
    }
}
