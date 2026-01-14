package org.boxutil.define.struct.instance;

import org.boxutil.base.api.InstanceRenderAPI;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.InstanceType;
import org.boxutil.util.CommonUtil;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

/**
 * Just for formatted update, without getter.
 * <pre>
 * {@code
 * // 32 byte
 * struct Fixed2D { // binding 5
 *     vec4 alpha_Facing_Location;
 *     vec2 scale;
 *     uvec2 colorBits;
 * };
 * }
 * </pre>
 */
public class Fixed2DStruct {
    protected final float[] state = new float[InstanceType.FIXED_2D.getComponent()];

    public Fixed2DStruct() {
        this.state[6] = this.state[7] = Float.intBitsToFloat(0xFFFFFFFF);
        this.state[0] = 2.0f;
    }

    public float[] getData() {
        return this.state;
    }

    public Fixed2DStruct setLocation(float x, float y) {
        this.state[2] = x;
        this.state[3] = y;
        return this;
    }

    public Fixed2DStruct setLocation(Vector2f location) {
        return this.setLocation(location.x, location.y);
    }

    public Fixed2DStruct setFacing(float angle) {
        this.state[1] = angle;
        return this;
    }

    public Fixed2DStruct setScale(float x, float y) {
        this.state[4] = x;
        this.state[5] = y;
        return this;
    }

    public Fixed2DStruct setScale(Vector2f scale) {
        return this.setScale(scale.x, scale.y);
    }

    public Fixed2DStruct setColor(byte r, byte g, byte b, byte a) {
        this.state[6] = CommonUtil.packingBytesToFloat(r, g, b, a);
        return this;
    }

    public Fixed2DStruct setColor(int r, int g, int b, int a) {
        return this.setColor((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public Fixed2DStruct setColor(Color color) {
        return this.setColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public Fixed2DStruct setColor(float r, float g, float b, float a) {
        return this.setColor((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public Fixed2DStruct setColor(Vector4f color) {
        return this.setColor(color.x, color.y, color.z, color.w);
    }

    public Fixed2DStruct setEmissive(byte r, byte g, byte b, byte a) {
        this.state[7] = CommonUtil.packingBytesToFloat(r, g, b, a);
        return this;
    }

    public Fixed2DStruct setEmissive(int r, int g, int b, int a) {
        return this.setEmissive((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public Fixed2DStruct setEmissive(Color color) {
        return this.setEmissive((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha());
    }

    public Fixed2DStruct setEmissive(float r, float g, float b, float a) {
        return this.setEmissive((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public Fixed2DStruct setEmissive(Vector4f color) {
        return this.setEmissive(color.x, color.y, color.z, color.w);
    }

    /**
     * Only for {@link InstanceRenderAPI#submitFixedInstanceData()}.
     *
     * @param state valid state: {@link BoxEnum#TIMER_IN}, {@link BoxEnum#TIMER_FULL}, {@link BoxEnum#TIMER_OUT}.
     */
    public Fixed2DStruct setFixedInstanceAlpha(float alpha, byte state) {
        this.state[0] = Math.max(Math.min(alpha, 1.0f), 0.0f);
        if (this.state[0] >= 0.0f) {
            if (state == BoxEnum.TIMER_FULL) this.state[0] += 1.0f;
            else if (state != BoxEnum.TIMER_IN) this.state[0] += 2.0f;
        }
        return this;
    }

    /**
     * Only for {@link InstanceRenderAPI#submitFixedInstanceData()}.
     */
    public Fixed2DStruct copyFixedInstanceAlphaState(Fixed2DStruct data) {
        this.state[0] = data.state[0];
        return this;
    }
}
