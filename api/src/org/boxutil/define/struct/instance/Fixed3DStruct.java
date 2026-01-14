package org.boxutil.define.struct.instance;

import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.InstanceType;
import org.boxutil.util.CommonUtil;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

/**
 * Just for formatted update, without getter.
 * <pre>
 * {@code
 * // 48 byte
 * struct Fixed3D { // binding 7
 *     vec4 rotate_LocationX;
 *     vec4 scale_LocationY;
 *     vec2 alpha_LocationZ;
 *     uvec2 colorBits;
 * };
 * }
 * </pre>
 */
public class Fixed3DStruct {
    protected final float[] state = new float[InstanceType.FIXED_3D.getComponent()];

    public Fixed3DStruct() {
        this.state[10] = this.state[11] = Float.intBitsToFloat(0xFFFFFFFF);
        this.state[8] = 21.0f;
    }

    public float[] getData() {
        return this.state;
    }

    public Fixed3DStruct setLocation(float x, float y, float z) {
        this.state[3] = x;
        this.state[7] = y;
        this.state[9] = z;
        return this;
    }

    public Fixed3DStruct setLocation(Vector3f location) {
        return this.setLocation(location.x, location.y, location.z);
    }

    public Fixed3DStruct setRotate(float z, float x, float y) {
        this.state[0] = z;
        this.state[1] = x;
        this.state[2] = y;
        return this;
    }

    public Fixed3DStruct setRotate(Vector3f rotate) {
        return this.setRotate(rotate.z, rotate.x, rotate.y);
    }

    public Fixed3DStruct setScale(float x, float y, float z) {
        this.state[4] = x;
        this.state[5] = y;
        this.state[6] = z;
        return this;
    }

    public Fixed3DStruct setScale(Vector3f scale) {
        return this.setScale(scale.x, scale.y, scale.z);
    }

    public Fixed3DStruct setColor(byte r, byte g, byte b, byte a) {
        this.state[10] = CommonUtil.packingBytesToFloat(r, g, b, a);
        return this;
    }

    public Fixed3DStruct setColor(int r, int g, int b, int a) {
        return this.setColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public Fixed3DStruct setColor(Color color) {
        return this.setColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public Fixed3DStruct setColor(float r, float g, float b, float a) {
        return this.setColor((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public Fixed3DStruct setColor(Vector4f color) {
        return this.setColor(color.x, color.y, color.z, color.w);
    }

    public Fixed3DStruct setEmissive(byte r, byte g, byte b, byte a) {
        this.state[11] = CommonUtil.packingBytesToFloat(r, g, b, a);
        return this;
    }

    public Fixed3DStruct setEmissive(int r, int g, int b, int a) {
        return this.setEmissive((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public Fixed3DStruct setEmissive(Color color) {
        return this.setEmissive((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public Fixed3DStruct setEmissive(float r, float g, float b, float a) {
        return this.setEmissive((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public Fixed3DStruct setEmissive(Vector4f color) {
        return this.setEmissive(color.x, color.y, color.z, color.w);
    }

    /**
     * Only for {@link InstanceRenderAPI#submitFixedInstanceData()}.
     *
     * @param state valid state: {@link BoxEnum#TIMER_IN}, {@link BoxEnum#TIMER_FULL}, {@link BoxEnum#TIMER_OUT}.
     */
    public Fixed3DStruct setFixedInstanceAlpha(float alpha, byte state) {
        this.state[8] = Math.max(Math.min(alpha, 1.0f), 0.0f);
        if (this.state[8] >= 0.0f) {
            if (state == BoxEnum.TIMER_FULL) this.state[8] += 1.0f;
            else if (state != BoxEnum.TIMER_IN) this.state[8] += 2.0f;
        }
        return this;
    }

    /**
     * Only for {@link InstanceRenderAPI#submitFixedInstanceData()}.
     */
    public Fixed3DStruct copyFixedInstanceAlphaState(Fixed3DStruct data) {
        this.state[8] = data.state[8];
        return this;
    }
}
