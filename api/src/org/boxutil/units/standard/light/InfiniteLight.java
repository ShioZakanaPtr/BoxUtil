package org.boxutil.units.standard.light;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.define.DirectEntityType;

import java.nio.FloatBuffer;

/**
 * Default direction is <code>vec3(0.0, 0.0, -1.0)</code><p>
 * Radius used for depth of attenuation.<p>
 * For instanced data: scaleX to controls attenuation depth scale.
 */
public class InfiniteLight extends BaseIlluminantData {
    public InfiniteLight() {
        this.resetAttenuationRadius();
    }

    protected void _resetExc() {
        super._resetExc();
        this.resetAttenuationRadius();
    }

    public void setNoneAttenuation() {
        this.stateBase[4] = 0.0f;
    }

    public void resetAttenuationRadius() {
        this.stateBase[4] = 2048.0f;
    }

    public void submitEntityData() {
        super.submitEntityData();
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
    }

    public Object entityType() {
        return DirectEntityType.INFINITE_LIGHT;
    }
}
