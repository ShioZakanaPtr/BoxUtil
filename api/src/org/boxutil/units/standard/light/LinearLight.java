package org.boxutil.units.standard.light;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.define.DirectEntityType;

import java.nio.FloatBuffer;

/**
 * For beam or ray, the default ends is <code>start=vec3(-lengthHalf, 0.0, 0.0)</code> and <code>end=vec3(lengthHalf, 0.0, 0.0)</code>.<p>
 * Should fastest in all the illuminant (in LTCs BRDF lighting).<p>
 * For instanced data: scaleX to controls length scale (also will be the attenuation radius scale), scaleY to controls cylinder radius scale.
 */
public class LinearLight extends BaseIlluminantData {
    protected final float[] state = new float[]{64.0f, 1.0f}; // length, radius
    protected boolean endCaps = false;

    public void reset() {
        super.reset();
        this.state[0] = 64.0f;
        this.state[1] = 1.0f;
        this.endCaps = false;
    }

    public float getLength() {
        return this.state[0] + this.state[0];
    }

    public void setLength(float length) {
        this.state[0] = length * 0.5f;
    }

    public float getCylinderRadius() {
        return this.state[1];
    }

    /**
     * @param radius not recommended with large radius, because it just linear light.
     */
    public void setCylinderRadius(float radius) {
        this.state[1] = radius;
    }

    public boolean isWithEndCaps() {
        return this.endCaps;
    }

    public void setWithEndCaps(boolean withEndCaps) {
        this.endCaps = withEndCaps;
    }

    public FloatBuffer pickDataPackage_vec4() {
        FloatBuffer buffer = super.pickDataPackage_vec4();
        buffer.put(6, this.state[0]);
        buffer.put(7, this.state[1]);
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public Object entityType() {
        return DirectEntityType.LINEAR_LIGHT;
    }
}
