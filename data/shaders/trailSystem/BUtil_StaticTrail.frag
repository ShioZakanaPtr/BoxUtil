#version OVERWRITE_VERSION

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define TIMER_STATE 5
#define ALPHA_THRESHOLD 0.003

#define TRAIL_VERSION_TITLE

// 0: vec4(color)
// 1: vec4(emissiveColor)
// 2: vec4(alphaMix, colorMix, glowPower, anisotropic)
// 3: vec4(colorIn)
// 4: vec4(colorOut)
// 5: vec4(fadeIn, full, fadeOut, randomUVStart)
// 6: vec4(sizeIn, sizeOut, texPixelsDiv, texSpeed)
// 7: vec4(velInRange)
// 8: vec4(velOutRange)
// 9: vec4(angluarInRange, angluarOutRange)
uniform vec4 u_statePackage[10];
uniform float u_time;
uniform uvec2 u_additionEmissive_DataBit;

#ifdef LEGACY_TRAIL_MODE
uniform sampler2D u_diffuseMap;
uniform sampler2D u_complexMap;
uniform sampler2D u_emissiveMap;
#else
layout (binding = 0) uniform sampler2D u_diffuseMap;
layout (binding = 1) uniform sampler2D u_normalMap;
layout (binding = 2) uniform sampler2D u_complexMap;
layout (binding = 3) uniform sampler2D u_emissiveMap;
layout (binding = 4) uniform sampler2D u_tangentMap;
#endif


in GEOM_FRAG_BLOCK {
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
#ifdef LEGACY_TRAIL_MODE
    vec2 fragUV;
#else
    vec4 fragUV_TBN;
    vec2 fragPosN;
#endif
} gfb_data;


#ifdef LEGACY_TRAIL_MODE
out vec4 o_fragColor;
#else
layout (location = 0) out vec4 o_fragColor; // draw to RGB8
layout (location = 1) out vec4 o_fragEmissive; // draw to RGB8
layout (location = 2) out vec4 o_fragWorldPos; // draw to RGB16
layout (location = 3) out vec4 o_fragWorldNormal; // draw to RGB16_SNORM
layout (location = 4) out vec4 o_fragWorldTangent; // draw to RGB16_SNORM
layout (location = 5) out vec4 o_fragMaterial; // roughness, metalness, anisotropic; draw to RGB8
layout (location = 6) out uvec4 o_fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.
#endif

void main() {
#ifdef LEGACY_TRAIL_MODE
    vec4 diffuse = texture(u_diffuseMap, gfb_data.fragUV) * gfb_data.fragEntityColor;
    vec4 emissive = texture(u_emissiveMap, gfb_data.fragUV) * gfb_data.fragMixEmissive;
#else
    vec4 diffuse = texture(u_diffuseMap, gfb_data.fragUV_TBN.xy) * gfb_data.fragEntityColor;
    vec4 emissive = texture(u_emissiveMap, gfb_data.fragUV_TBN.xy) * gfb_data.fragMixEmissive;
#endif
    if (diffuse.w + emissive.w <= ALPHA_THRESHOLD) discard;

#ifdef LEGACY_TRAIL_MODE
    vec3 complexRaw = texture(u_complexMap, gfb_data.fragUV).xyz;
#else
    vec3 complexRaw = texture(u_complexMap, gfb_data.fragUV_TBN.xy).xyz;
#endif
    emissive.xyz += diffuse.xyz * complexRaw.x;

    diffuse.w = min(diffuse.w, 1.0);
    o_fragColor = u_additionEmissive_DataBit.x > 0u ? (diffuse + emissive * emissive.w) : mix(diffuse, emissive, emissive.w);

#ifndef LEGACY_TRAIL_MODE
    mat2 TBN = mat2(gfb_data.fragUV_TBN.zw, -gfb_data.fragUV_TBN.w, gfb_data.fragUV_TBN.z);

    bool ignoreIllum = (u_additionEmissive_DataBit.y & 2u) == 2u;
    vec4 normalRaw = texture(u_normalMap, gfb_data.fragUV_TBN.xy);
    normalRaw.xyz = fma(normalRaw.xyz, vec3(2.0), vec3(-1.0));
    if (normalRaw.w <= 0.0) normalRaw.xyz = vec3(0.0, 0.0, 1.0); else {
        normalRaw.xyz = normalRaw.xyz;
        normalRaw.xy = TBN * normalRaw.xy;
    }
    normalRaw.w = diffuse.w;
    vec4 resultTangent = vec4(1.0, 0.0, 0.0, 0.0);
    if (!ignoreIllum) {
        resultTangent.w = diffuse.w;
        if (u_statePackage[EMISSIVE_SA].w != 0.0) {
            resultTangent.xyz = fma(texture(u_tangentMap, gfb_data.fragUV_TBN.xy).xyz, vec3(2.0), vec3(-1.0));
            resultTangent.xy = TBN * resultTangent.xy;
        }
    }

    emissive *= u_statePackage[EMISSIVE_SA].z;
    emissive.w = min(emissive.w, 1.0);
    float cullAlpha = max(diffuse.w, emissive.w);
    o_fragEmissive = emissive;
    o_fragWorldPos = ignoreIllum ? vec4(0.0) : vec4(gfb_data.fragPosN, 0.5, step(ALPHA_THRESHOLD, cullAlpha));
    o_fragWorldNormal = ignoreIllum ? vec4(0.0, 0.0, 1.0, 0.0) : normalRaw;
    o_fragWorldTangent = resultTangent;
    o_fragMaterial = ignoreIllum ? vec4(0.0, 0.0, 0.0, 0.0) : vec4(complexRaw.yz, u_statePackage[EMISSIVE_SA].w, diffuse.w);
    o_fragData = (cullAlpha > 0.0) ? uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), cullAlpha) * 1023.0), u_additionEmissive_DataBit.y, 1u) : uvec4(0u);
#endif
}