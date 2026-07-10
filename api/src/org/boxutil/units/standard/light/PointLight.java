package org.boxutil.units.standard.light;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.define.DirectEntityType;

import java.nio.FloatBuffer;

/**
 * For most usage scenarios.<p>
 * For instanced data: scaleX to controls attenuation radius scale.
 */
public class PointLight extends BaseIlluminantData {
    public void submitEntityData() {
        super.submitEntityData();
        this._statePackageBuffer.position(0);
        this._statePackageBuffer.limit(this._statePackageBuffer.capacity());
    }

    public Object entityType() {
        return DirectEntityType.POINT_LIGHT;
    }
}
