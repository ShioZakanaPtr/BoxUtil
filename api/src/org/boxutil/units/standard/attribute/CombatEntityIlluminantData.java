package org.boxutil.units.standard.attribute;

import org.boxutil.define.BoxDatabase;

import java.awt.*;

/**
 * For how to use custom illuminant data for any combat entity<strong>(only for ship and projectile)</strong>:
 * <pre>
 * {@code
 * CombatEntityIlluminantData flag = new CombatEntityIlluminantData();
 * // *here any setting for flag*
 * combatEntity.setCustomData(CombatEntityIlluminantData.class.getName(), flag);
 * }
 * </pre>
 * ------------<p>
 * For block the illuminant autogen or none illuminant, add to tags in <code>ship_data.csv</code>:<p>
 * <code>ogl_NoneIlluminant</code><p>
 * <code>dweller</code> <em>(for vanilla compatible)</em><p>
 * See {@link BoxDatabase#NONE_ILLUMINANT_FLAG}<p>
 * If any ship have the tags, will not autogen illuminant for each ship engine.<p>
 * If any weapon have the tags, will not autogen illuminant for *beam* or projectile that spawn by the weapon.<p>
 * <strong>If any weapon have the tags, and it is *beam* weapon: you might have to making an illuminant entity, and add to rendering manager by manual</strong>.
 */
public class CombatEntityIlluminantData {
    public final static String KEY = CombatEntityIlluminantData.class.getName();

    /**
     * For all.
     */
    public boolean noneIlluminant = false;

    // For ship
    /**
     * The value <code>null</code> for no illuminant spawn this frame.<p>
     * The alpha for strength scale.
     */
    public Color SHIP_engineColor = null;

    /**
     * The value <code>true</code> for mix this color to original color, not overwrite it.
     */
    public boolean SHIP_MUL_MODE = false;

    /**
     * The value less than or equals <code>0.0f</code> for no illuminant spawn this frame.
     */
    public float SHIP_engineRadius = 0.0f;


    // For projectile spawn
    /**
     * The value <code>null</code> for no illuminant spawn when the projectile spawn.<p>
     * The alpha for strength scale.
     */
    public Color PROJ_spawnColor = null;

    /**
     * The value less than or equals <code>0.0f</code> for no illuminant spawn when the projectile spawn.
     */
    public float PROJ_spawnRadius = 0.0f;

    /**
     * The value less than or equals <code>0.0f</code> for no illuminant spawn when the projectile spawn.
     */
    public float PROJ_spawnDuration = 0.0f;


    // For projectile body
    /**
     * The value <code>null</code> for no illuminant spawn this frame.<p>
     * The alpha for strength scale.
     */
    public Color PROJ_bodyColor = null;

    /**
     * The value less than or equals <code>0.0f</code> for no illuminant spawn this frame.
     */
    public float PROJ_bodyRadius = 0.0f;


    // For projectile did damage
    /**
     * The value <code>null</code> for no illuminant spawn when the projectile did damage.<p>
     * The alpha for strength scale.
     */
    public Color PROJ_hitColor = null;

    /**
     * The value less than or equals <code>0.0f</code> for no illuminant spawn when the projectile did damage.
     */
    public float PROJ_hitRadius = 0.0f;

    /**
     * The value less than or equals <code>0.0f</code> for no illuminant spawn when the projectile did damage.
     */
    public float PROJ_hitDuration = 0.0f;

    public int hashCode() {
        int result = 31 + Boolean.hashCode(this.noneIlluminant);
        result = 31 * result + (this.SHIP_engineColor == null ? 0 : this.SHIP_engineColor.hashCode());
        result = 31 * result + Boolean.hashCode(this.SHIP_MUL_MODE);
        result = 31 * result + Float.hashCode(this.SHIP_engineRadius);

        result = 31 * result + (this.PROJ_spawnColor == null ? 0 : this.PROJ_spawnColor.hashCode());
        result = 31 * result + Float.hashCode(this.PROJ_spawnRadius);
        result = 31 * result + Float.hashCode(this.PROJ_spawnDuration);

        result = 31 * result + (this.PROJ_bodyColor == null ? 0 : this.PROJ_bodyColor.hashCode());
        result = 31 * result + Float.hashCode(this.PROJ_bodyRadius);

        result = 31 * result + (this.PROJ_hitColor == null ? 0 : this.PROJ_hitColor.hashCode());
        result = 31 * result + Float.hashCode(this.PROJ_hitRadius);
        return 31 * result + Float.hashCode(this.PROJ_hitDuration);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CombatEntityIlluminantData) return this.hashCode() == obj.hashCode();
        else return false;
    }
}
