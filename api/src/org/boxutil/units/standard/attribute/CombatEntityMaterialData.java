package org.boxutil.units.standard.attribute;

import org.boxutil.define.BoxDatabase;

/**
 * For how to use custom material data for any combat entity:
 * <pre>
 * {@code
 * CombatEntityOverrideData flag = new CombatEntityOverrideData();
 * // *here any setting for flag*
 * combatEntity.setCustomData(CombatEntityOverrideData.class.getName(), flag);
 * }
 * </pre>
 * ------------<p>
 * For pass shading, add to tags in <code>ship_data.csv</code> or <code>weapon_data.csv</code>:<p>
 * <code>ogl_PassShading</code><p>
 * <code>dweller</code> <em>(for vanilla compatible)</em><p>
 * See {@link BoxDatabase#PASS_SHADING_FLAG}<p>
 * If any entity have any "pass shading" tags, all map is invalid.<p>
 * ------------<p>
 * For block the normal map autogen, add to tags in <code>ship_data.csv</code> or <code>weapon_data.csv</code>:<p>
 * <code>ogl_NoAutoNormal</code><p>
 * <code>graphicslib_no_autogen</code> <em>(compatible)</em><p>
 * See {@link BoxDatabase#NORMAL_NO_AUTOGEN_FLAG}<p>
 * And then, they will use Z-vector for normal map(default normal).<p>
 */
public class CombatEntityMaterialData {
    public final static String KEY = CombatEntityMaterialData.class.getName();

    /**
     * The value less than or equals <code>0</code> for default texture
     */
    public int normalMap = 0;

    /**
     * The value less than or equals <code>0</code> for default texture: 0.0 emissive mask, 0.5 metalness and roughness.
     */
    public int complexMap = 0;

    /**
     * The value less than or equals <code>0</code> for default texture.
     */
    public int emissiveMap = 0;

    /**
     * The value <code>true</code> for pass shading in this frame, with ship weapon.<p>
     */
    public boolean passShading = false;

    public String toString() {
        return "CombatEntityMaterialData: Normal = '" + this.normalMap + "', Complex = '" + this.complexMap + "', Emissive = '" + this.emissiveMap + "', PassShading = '" + this.passShading + '\'';
    }

    public int hashCode() {
        int result = 31 + this.normalMap;
        result = 31 * result + this.complexMap;
        result = 31 * result + this.emissiveMap;
        return 31 * result + Boolean.hashCode(this.passShading);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CombatEntityMaterialData) return this.hashCode() == obj.hashCode();
        else return false;
    }
}
