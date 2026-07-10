#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define EMISSIVE_SA 2
#define LIGHT_COLOR 3
#define SHADOW_COLOR 4
#define LIGHT_DIR 5
#define ALPHA_THRESHOLD 0.003

subroutine void t_surfaceStateDraw(inout vec3 diffuseParam, in vec3 currentNormal);
subroutine uniform t_surfaceStateDraw f_surfaceState;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

// vec4(color), vec4(emissiveColor), vec4(alphaMix, colorMix, glowPower, anisotropic), vec4(lightColor), vec4(shadowColor), vec4(lightDirection, alpha)
uniform vec4 u_statePackage[6];
uniform uvec3 u_additionEmissive_DataBit_InstanceOffset;

layout (binding = 0) uniform sampler2D u_diffuseMap;
layout (binding = 1) uniform sampler2D u_normalMap;
layout (binding = 2) uniform sampler2D u_complexMap;
layout (binding = 3) uniform sampler2D u_emissiveMap;
layout (binding = 4) uniform sampler2D u_tangentMap;

in VERTEX_BLOCK {
    mat3 fragTBN;
    vec2 fragUV;
    vec3 fragPos;
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
} vfb_data;

layout (location = 0) out vec4 o_fragColor; // draw to RGB8
layout (location = 1) out vec4 o_fragEmissive; // draw to RGB8
layout (location = 2) out vec4 o_fragWorldPos; // draw to RGB16
layout (location = 3) out vec4 o_fragWorldNormal; // draw to RGB16_SNORM
layout (location = 4) out vec4 o_fragWorldTangent; // draw to RGB16_SNORM
layout (location = 5) out vec4 o_fragMaterial; // roughness, metalness, anisotropic; draw to RGB8
layout (location = 6) out uvec4 o_fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.

//vec3 normalBlend(in vec3 face, in vec3 tex) {
//    vec3 t = face * 2.0 + vec3(-1.0, -1.0, 0.0);
//    vec3 u = tex * vec3(-2.0, -2.0, 2.0) + vec3(1.0, 1.0, -1.0);
//    return normalize(t * dot(t, u) / t.z - u);
//}

subroutine(t_surfaceStateDraw) void p_commonMode(inout vec3 diffuseParam, in vec3 currentNormal) {
    float brightnessRaw = max(dot(-u_statePackage[LIGHT_DIR].xyz, currentNormal), 0.0);
    vec3 shadowMix = mix(vec3(1.0), u_statePackage[SHADOW_COLOR].xyz, u_statePackage[SHADOW_COLOR].w);
    vec3 lightMix = mix(vec3(1.0), u_statePackage[LIGHT_COLOR].xyz, u_statePackage[LIGHT_COLOR].w);
    diffuseParam *= mix(shadowMix, lightMix, brightnessRaw);
}

subroutine(t_surfaceStateDraw) void p_colorMode(inout vec3 diffuseParam, in vec3 currentNormal) {
    diffuseParam *= u_statePackage[LIGHT_COLOR].xyz * u_statePackage[LIGHT_COLOR].w;
}

vec3 encodePos(in vec3 posRaw) {
    return clamp((posRaw - vec3(gameScreenBorder.xy, -6400.0)) / vec3(gameScreenBorder.zw, 12800.0), vec3(0.0), vec3(1.0));
}

void main() {
    vec4 diffuse = texture(u_diffuseMap, vfb_data.fragUV) * vfb_data.fragEntityColor;
    vec4 emissive = texture(u_emissiveMap, vfb_data.fragUV) * vfb_data.fragMixEmissive;
    if (diffuse.w + emissive.w <= ALPHA_THRESHOLD) discard;
    diffuse.w = min(diffuse.w, 1.0);

    bool ignoreIllum = (u_additionEmissive_DataBit_InstanceOffset.y & 2u) == 2u;
    vec4 normalRaw = texture(u_normalMap, vfb_data.fragUV);
    normalRaw.xyz = fma(normalRaw.xyz, vec3(2.0), vec3(-1.0));
    if (normalRaw.w <= 0.0) normalRaw.xyz = vec3(0.0, 0.0, 1.0); else normalRaw.xyz = vfb_data.fragTBN * normalRaw.xyz;
    normalRaw.w = diffuse.w;

    vec4 resultTangent = vec4(1.0, 0.0, 0.0, 0.0);
    if (!ignoreIllum) {
        resultTangent.w = diffuse.w;
        if (u_statePackage[EMISSIVE_SA].w != 0.0) {
            resultTangent.xyz = vfb_data.fragTBN * fma(texture(u_tangentMap, vfb_data.fragUV).xyz, vec3(2.0), vec3(-1.0));
        }
    }

    vec3 complexRaw = texture(u_complexMap, vfb_data.fragUV).xyz;
    emissive.xyz += diffuse.xyz * complexRaw.x;
    f_surfaceState(diffuse.xyz, normalRaw.xyz);

    o_fragColor = u_additionEmissive_DataBit_InstanceOffset.x > 0u ? (diffuse + emissive * emissive.w) : mix(diffuse, emissive, emissive.w);
    emissive *= u_statePackage[EMISSIVE_SA].z;
    emissive.w = min(emissive.w, 1.0);
    float cullAlpha = max(diffuse.w, emissive.w);
    o_fragEmissive = emissive;
    o_fragWorldPos = ignoreIllum ? vec4(0.0) : vec4(encodePos(vfb_data.fragPos), step(ALPHA_THRESHOLD, cullAlpha));
    o_fragWorldNormal = ignoreIllum ? vec4(0.0, 0.0, 1.0, 0.0) : normalRaw;
    o_fragWorldTangent = resultTangent;
    o_fragMaterial = ignoreIllum ? vec4(0.0, 0.0, 0.0, 0.0) : vec4(complexRaw.yz, u_statePackage[EMISSIVE_SA].w, diffuse.w);
    o_fragData = (cullAlpha > 0.0) ? uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), cullAlpha) * 1023.0), u_additionEmissive_DataBit_InstanceOffset.y, 1u) : uvec4(0u);
}
