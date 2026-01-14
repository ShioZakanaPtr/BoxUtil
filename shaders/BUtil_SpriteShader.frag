#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define EMISSIVE_SA 2
#define ALPHA_THRESHOLD 0.003

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

in VERTEX_BLOCK {
    mat3 fragTBN;
    vec2 fragUV;
    vec3 fragPos;
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
} vb_data;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic)
// [vec2(tile), startIndex, randomEach], [vec2(start), vec2(end)], hashCode, totalTilesMinusOne, vec2(baseSize)
uniform vec4 statePackage[6];
uniform uvec3 additionEmissive_DataBit_InstanceOffset;

layout (binding = 0) uniform sampler2D diffuseMap;
layout (binding = 1) uniform sampler2D normalMap;
layout (binding = 2) uniform sampler2D complexMap;
layout (binding = 3) uniform sampler2D emissiveMap;
layout (binding = 4) uniform sampler2D tangentMap;

layout (location = 0) out vec4 fragColor; // draw to RGB8
layout (location = 1) out vec4 fragEmissive; // draw to RGB8
layout (location = 2) out vec4 fragWorldPos; // draw to RGB16
layout (location = 3) out vec4 fragWorldNormal; // draw to RGB16_SNORM
layout (location = 4) out vec4 fragWorldTangent; // draw to RGB16_SNORM
layout (location = 5) out vec4 fragMaterial; // roughness, metalness, anisotropic; draw to RGB8
layout (location = 6) out uvec4 fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.

vec3 encodePos(in vec3 posRaw) {
    return clamp((posRaw - vec3(gameScreenBorder.xy, -6400.0)) / vec3(gameScreenBorder.zw, 12800.0), vec3(0.0), vec3(1.0));
}

void main()
{
    vec4 diffuse = texture(diffuseMap, vb_data.fragUV) * vb_data.fragEntityColor;
    vec4 emissive = texture(emissiveMap, vb_data.fragUV) * vb_data.fragMixEmissive;
    if (diffuse.w + emissive.w <= ALPHA_THRESHOLD) discard;

    bool ignoreIllum = (additionEmissive_DataBit_InstanceOffset.y & 2u) == 2u;
    vec4 normalRaw = texture(normalMap, vb_data.fragUV);
    normalRaw.xyz = fma(normalRaw.xyz, vec3(2.0), vec3(-1.0));
    if (normalRaw.w <= 0.0) normalRaw.xyz = vec3(0.0, 0.0, 1.0); else normalRaw.xyz = vb_data.fragTBN * normalRaw.xyz;
    normalRaw.w = diffuse.w;
    vec3 complexRaw = texture(complexMap, vb_data.fragUV).xyz;
    emissive.xyz += diffuse.xyz * complexRaw.x;

    diffuse.w = min(diffuse.w, 1.0);
    fragColor = additionEmissive_DataBit_InstanceOffset.x == 1u ? (diffuse + emissive * emissive.w) : mix(diffuse, emissive, emissive.w);
    emissive *= statePackage[EMISSIVE_SA].z;
    emissive.w = min(emissive.w, 1.0);
    float cullAlpha = max(diffuse.w, emissive.w);
    fragEmissive = emissive;
    fragWorldPos = ignoreIllum ? vec4(0.0) : vec4(encodePos(vb_data.fragPos), step(ALPHA_THRESHOLD, cullAlpha));
    fragWorldNormal = ignoreIllum ? vec4(0.0, 0.0, 1.0, 0.0) : normalRaw;
    fragWorldTangent = ignoreIllum ? vec4(1.0, 0.0, 0.0, 0.0) : ((statePackage[EMISSIVE_SA].w == 0.0) ? vec4(1.0, 0.0, 0.0, diffuse.w) : vec4(vb_data.fragTBN * texture(tangentMap, vb_data.fragUV).xyz, diffuse.w));
    fragMaterial = ignoreIllum ? vec4(0.0, 0.0, 0.0, 0.0) : vec4(complexRaw.yz, statePackage[EMISSIVE_SA].w, diffuse.w);
    fragData = (cullAlpha > 0.0) ? uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), cullAlpha) * 1023.0), additionEmissive_DataBit_InstanceOffset.y, 0u) : uvec4(uvec3(0u), 1u);
}
