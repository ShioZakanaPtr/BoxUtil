package org.boxutil.units.standard.light;

import org.boxutil.base.BaseIlluminantData;
import org.boxutil.define.DirectEntityType;

import java.nio.FloatBuffer;

/**
 * For most usage scenarios.<p>
 * For instanced data: scaleX to controls attenuation radius scale.
 */
public class PointLight extends BaseIlluminantData {
    public FloatBuffer pickDataPackage_vec4() {
        FloatBuffer buffer = super.pickDataPackage_vec4();
        buffer.position(0);
        buffer.limit(buffer.capacity());
        return buffer;
    }

    public Object entityType() {
        return DirectEntityType.POINT_LIGHT;
    }
}
