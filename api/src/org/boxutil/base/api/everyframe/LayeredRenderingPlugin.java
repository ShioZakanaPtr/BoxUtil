package org.boxutil.base.api.everyframe;

import com.fs.starfarer.api.campaign.CampaignEngineLayers;
import com.fs.starfarer.api.combat.CombatEngineLayers;
import com.fs.starfarer.api.combat.ViewportAPI;

import java.util.EnumSet;

/**
 * Use it when needs draw something to main g-buffer.<p>
 * For layout:
 * <pre>
 * {@code glsl:
 * layout (location = 0) out vec4 fragColor; // draw to RGB8
 * layout (location = 1) out vec4 fragEmissive; // draw to RGB8
 * layout (location = 2) out vec4 fragWorldPos; // draw to RGB16; not draw when pass illumination.
 * layout (location = 3) out vec4 fragWorldNormal; // draw to RGB16_SNORM; not draw when pass illumination.
 * layout (location = 4) out vec4 fragWorldTangent; // draw to RGB16_SNORM; not draw when pass illumination.
 * layout (location = 5) out vec4 fragMaterial; // roughness, metalness, anisotropic; draw to RGB8; not draw when pass illumination.
 * layout (location = 6) out uvec4 fragData; // oneMinusDepth, alpha, flag; draw to RGB10_A2UI, alpha channel write ignored.
 * }
 * </pre>
 * <p>
 * For flag in fragData layout:
 * <pre>
 * {@code
 * 0
 * b
 * ---------------------------------------------------
 * l9 -> none
 * l8 = 496u -> layers bit 0-31                         (*1)
 * l7 = 496u -> layers bit 0-31                         (*1)
 * l6 = 496u -> layers bit 0-31                         (*1)
 * l5 = 496u -> layers bit 0-31                         (*1)
 * l4 = 496u -> layers bit 0-31                         (*1)
 * l3 =   8u -> 1 for vanilla draw                      (*2)
 * l2 =   4u -> 1 for negative anisotropic strength
 * l1 =   2u -> 1 for ignore illumination
 * l0 =   1u -> 0 for invalid illumination
 *
 * (*1) CombatEngineLayers or CampaignEngineLayers, lowest(0) -> highest(1+)
 * (*2) Ship, weapon, missile, asteroid etc.
 * }
 * </pre>
 * For fetch flag in Java:
 * <pre>
 * {@code
 * int flag = (layer.ordinal() << 4) | 0b1
 * if (ignoreIllumination) flag |= 0b10;
 * if (anisotropic < 0.0f) flag |= 0b100;
 * }
 * <p>
 * If the alpha value of fragment is less than or equal to <code>0</code> (or other appropriate threshold), recommend to discard it.
 * ----------<p>
 * For world position encode and decode:
 * <pre>
 * {@code glsl:
 * // UBO data
 * layout (std140, binding = 0) uniform BUtilGlobalData // 0 for default binding
 * {
 *     mat4 gameViewport;
 *     vec4 gameScreenBorder; // vec4(screenLB, screenSize)
 * };
 *
 * vec3 encodePos(in vec3 posRaw) {
 *     return clamp((posRaw - vec3(gameScreenBorder.xy, -6400.0)) / vec3(gameScreenBorder.zw, 12800.0), vec3(0.0), vec3(1.0));
 * }
 *
 * vec3 decodePos(in vec3 posRaw) {
 *     return fma(posRaw, vec3(gameScreenBorder.zw, 12800.0), vec3(gameScreenBorder.xy, -6400.0));
 * }
 * }
 * </pre><p>
 * @see org.boxutil.manager.CombatRenderingManager#addRenderingPlugin(LayeredRenderingPlugin)
 * @see org.boxutil.manager.CampaignRenderingManager#addRenderingPlugin(LayeredRenderingPlugin)
 */
public interface LayeredRenderingPlugin {
    boolean isExpired();

    /**
     * @param layer can be {@link CombatEngineLayers} or {@link CampaignEngineLayers}.
     * @param layerBit (layer.ordinal() << 4) | 0b1.
     */
    void render(Object layer, int layerBit, boolean framebufferValid, ViewportAPI viewport, int fbo, int colorMap, int emissiveMap, int worldPosMap, int worldNormalMap, int worldTangentMap, int worldMaterialMap, int worldDataMap);

    void cleanup();

    /**
     * @return const set, donot changing it when rendering.
     */
    EnumSet<CombatEngineLayers> getCombatActiveLayers();

    /**
     * @return const set, donot changing it when rendering.
     */
    EnumSet<CampaignEngineLayers> getCampaignActiveLayers();
}
