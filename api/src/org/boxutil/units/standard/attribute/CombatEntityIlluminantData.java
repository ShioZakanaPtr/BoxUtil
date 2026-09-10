package org.boxutil.units.standard.attribute;

import org.boxutil.define.BoxDatabase;

import java.awt.*;

/**
 * For how to use custom illuminant data for any combat entity<strong>(only for ship and projectile)</strong>:
 * <pre>
 * {@code
 * CombatEntityIlluminantData flag = new CombatEntityIlluminantData();
 * // *here any setting for flag*
 * combatEntity.setCustomData(CombatEntityIlluminantData.KEY, flag);
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
}
