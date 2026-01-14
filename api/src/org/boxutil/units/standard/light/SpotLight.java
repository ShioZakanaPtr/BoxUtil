package org.boxutil.units.standard.light;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.define.DirectEntityType;

import java.nio.FloatBuffer;

/**
 * Default direction is <code>vec3(1.0, 0.0, 0.0)</code>, use model matrix to rotate it<p>
 * For instanced data: scaleX to controls attenuation radius scale, scaleY to controls cone of illuminant.
 */
public class SpotLight extends BaseIlluminantData {
    protected final float[] state = new float[]{1.0f, 0.0f}; // innerCone, outerCone

    public void reset() {
        super.reset();
        this.state[0] = 1.0f;
        this.state[1] = 0.0f;
    }

    public float getInnerCone() {
        return (float) Math.toDegrees(Math.acos(this.state[0])) * 2.0f;
    }

    public void setInnerCone(float angle) {
        this.state[0] = (float) Math.cos(Math.toRadians(angle * 0.5f));
    }

    public float getInnerConeDirect() {
        return this.state[0];
    }

    /**
     * @param angleCosValue half angle value
     */
    public void setInnerConeDirect(float angleCosValue) {
        this.state[0] = angleCosValue;
    }

    public float getOuterCone() {
        return (float) Math.toDegrees(Math.acos(this.state[1])) * 2.0f;
    }

    public void setOuterCone(float angle) {
        this.state[1] = (float) Math.cos(Math.toRadians(angle * 0.5f));
    }

    public float getOuterConeDirect() {
        return this.state[1];
    }

    /**
     * @param angleCosValue half angle value
     */
    public void setOuterConeDirect(float angleCosValue) {
        this.state[1] = angleCosValue;
    }

    /**
     * Sets the inner and outer cone both.
     */
    public void setCone(float angle) {
        float value = (float) Math.cos(Math.toRadians(angle * 0.5f));
        this.setInnerConeDirect(value);
        this.setOuterConeDirect(value);
    }

    /**
     * @param angleCosValue half angle value
     */
    public void setConeDirect(float angleCosValue) {
        this.setInnerConeDirect(angleCosValue);
        this.setOuterConeDirect(angleCosValue);
    }

    public FloatBuffer pickDataPackage_vec4() {
        FloatBuffer buffer = super.pickDataPackage_vec4();
        buffer.put(6, Math.max(this.state[0] - this.state[1], 0.0f));
        buffer.put(7, this.state[1]);
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public Object entityType() {
        return DirectEntityType.SPOT_LIGHT;
    }
}
