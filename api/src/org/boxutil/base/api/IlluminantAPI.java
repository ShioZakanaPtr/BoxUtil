package org.boxutil.base.api;

import de.unkrig.commons.nullanalysis.NotNull;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

public interface IlluminantAPI {
    float[] getColorArray();

    Color getColorC();

    Vector4f getColor();

    void setColor(@NotNull Vector4f color);

    /**
     * @param a strength of light, can over 100 or more.
     */
    void setColor(float r, float g, float b, float a);

    void setColor(Color color);

    float getStrength();

    void setStrength(float strength);

    float getAttenuationRadius();

    /**
     * The shading range of the light.<p>
     * Physically incorrect, but good for performance.
     */
    void setAttenuationRadius(float radius);

    void setDefaultAttenuation();

    boolean isNoneAttenuation();

    void setNoneAttenuation();

    boolean isLinearAttenuation();

    void setLinearAttenuation();

    boolean isSquareAttenuation();

    void setSquareAttenuation();

    boolean isSqrtAttenuation();

    void setSqrtAttenuation();
}
