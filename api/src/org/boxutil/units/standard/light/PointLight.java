package org.boxutil.units.standard.light;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.define.DirectEntityType;

/**
 * For most usage scenarios.<p>
 * For instanced data: scaleX to controls attenuation radius scale.
 */
public class PointLight extends BaseIlluminantData {
    public void submitEntityData() {
        super.submitEntityData();
        this.statePackageBuffer.position(0);
        this.statePackageBuffer.limit(this.statePackageBuffer.capacity());
    }

    public Object entityType() {
        return DirectEntityType.POINT_LIGHT;
    }
}
