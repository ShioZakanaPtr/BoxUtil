#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define EMISSIVE_SA 2
#define FILL_DATA 4
#define WIDTH_DATA 5
#define ALPHA_THRESHOLD 0.003

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

in GEOM_FRAG_BLOCK {
    mat3 fragTBN;
    vec4 fragUVSeedFactor;
    vec3 fragPos;
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
} gfb_data;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, timerAlpha), vec4(time, nodeCount, texturePixels, mixFactor), vec4(fillStart, fillEnd, startFactor, endFactor), vec4(startWidth, endWidth, jitterPower, flickerMix)
// 6: vec4(start), 7: vec4(end), 8: vec4(startEmissive), 9: vec4(endEmissive)
uniform vec4 statePackage[10];
// hashCode, hashCodeTime, globalTimerAlpha
uniform vec3 extraData;
uniform uvec2 additionEmissive_DataBit;

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

float hash12(vec2 p)
{
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float getJitter(in float uv, in float seed) {
    float uvO = hash12(vec2(round(seed) + 255.0, extraData.x)) * 2.0 - 1.0;
    float posFactor = step(0.0, uvO);

    float result = uv - posFactor;
    uvO = abs(uvO) + 1.0;
    result = mix(result, result * uvO, statePackage[WIDTH_DATA].z) + posFactor;
    result = clamp(result, 0.0, 1.0);
    return result;
}

vec3 encodePos(in vec3 posRaw) {
    return clamp((posRaw - vec3(gameScreenBorder.xy, -6400.0)) / vec3(gameScreenBorder.zw, 12800.0), vec3(0.0), vec3(1.0));
}

void main()
{
    vec2 uv = gfb_data.fragUVSeedFactor.xy;
    uv.y = getJitter(uv.y, gfb_data.fragUVSeedFactor.z);
    vec2 fillMix = smoothstep(statePackage[FILL_DATA].zw, vec2(1.0), vec2(1.0 - gfb_data.fragUVSeedFactor.w, gfb_data.fragUVSeedFactor.w)) * (1.0 - statePackage[FILL_DATA].xy);
    fillMix = 1.0 - fillMix;
    float fillFactor = clamp(fillMix.x * fillMix.y, 0.0, 1.0);

    vec4 diffuse = texture(diffuseMap, uv) * gfb_data.fragEntityColor;
    vec4 emissive = texture(emissiveMap, uv) * gfb_data.fragMixEmissive;
    diffuse.w *= fillFactor;
    emissive.w *= fillFactor;
    if (diffuse.w + emissive.w <= ALPHA_THRESHOLD) discard;

    bool ignoreIllum = (additionEmissive_DataBit.y & 2u) == 2u;
    vec4 normalRaw = texture(normalMap, uv);
    normalRaw.xyz = fma(normalRaw.xyz, vec3(2.0), vec3(-1.0));
    if (normalRaw.w <= 0.0) normalRaw.xyz = vec3(0.0, 0.0, 1.0); else normalRaw.xyz = gfb_data.fragTBN * normalRaw.xyz;
    normalRaw.w = diffuse.w;
    vec3 complexRaw = texture(complexMap, uv).xyz;
    emissive.xyz += diffuse.xyz * complexRaw.x;

    diffuse.w = min(diffuse.w, 1.0);
    fragColor = additionEmissive_DataBit.x == 1u ? (diffuse + emissive * emissive.w) : mix(diffuse, emissive, emissive.w);
    emissive *= statePackage[EMISSIVE_SA].z;
    emissive.w = min(emissive.w, 1.0);
    float cullAlpha = max(diffuse.w, emissive.w);
    fragEmissive = emissive;
    fragWorldPos = ignoreIllum ? vec4(0.0) : vec4(encodePos(gfb_data.fragPos), step(ALPHA_THRESHOLD, cullAlpha));
    fragWorldNormal = ignoreIllum ? vec4(0.0, 0.0, 1.0, 0.0) : normalRaw;
    fragWorldTangent = ignoreIllum ? vec4(1.0, 0.0, 0.0, 0.0) : ((statePackage[EMISSIVE_SA].w == 0.0) ? vec4(1.0, 0.0, 0.0, diffuse.w) : vec4(gfb_data.fragTBN * texture(tangentMap, uv).xyz, diffuse.w));
    fragMaterial = ignoreIllum ? vec4(0.0, 0.0, 0.0, 0.0) : vec4(complexRaw.yz, statePackage[EMISSIVE_SA].w, diffuse.w);
    fragData = (cullAlpha > 0.0) ? uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), cullAlpha) * 1023.0), additionEmissive_DataBit.y, 0u) : uvec4(uvec3(0u), 1u);
}
