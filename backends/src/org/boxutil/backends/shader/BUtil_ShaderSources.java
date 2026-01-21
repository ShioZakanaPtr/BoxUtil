package org.boxutil.backends.shader;

/**
 * <strong>DO NOT EDIT THEM.</strong>
 */
public final class BUtil_ShaderSources {
    public final static class Common {
        private Common() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2

subroutine void instanceStateCompute(out mat4 model, out vec4 mColor, out vec4 mEColor);
subroutine uniform instanceStateCompute instanceState;

layout (location = 0) in vec3 vertex;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 uv;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(alphaMix, colorMix, glowPower, anisotropic), vec4(lightColor), vec4(shadowColor), vec4(lightDirection, alpha)
uniform vec4 statePackage[6];
uniform vec3 baseSize;
uniform uvec3 additionEmissive_DataBit_InstanceOffset;

layout (binding = 8) uniform samplerBuffer vertexData_Tangent;

out VERTEX_BLOCK {
	mat3 fragTBN;
	vec2 fragUV;
	vec3 fragPos;
	vec4 fragEntityColor;
	vec4 fragMixEmissive;
} vb_data;

#include "BUtil_InstanceDataSSBO.h"

subroutine(instanceStateCompute) void noneData(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	model = modelMatrix;
	mColor = statePackage[COLOR] * statePackage[5].w;
	mEColor = statePackage[EMISSIVE_COLOR] * statePackage[5].w;
}

subroutine(instanceStateCompute) void haveData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	Dynamic2D data = dataDynamic2D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchDynamic2DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(statePackage[5].w, data.timer.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveFixedData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed2D data = dataFixed2D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchFixed2DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(statePackage[5].w, data.alpha_Facing_Location.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic3D data = dataDynamic3D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchDynamic3DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(statePackage[5].w, data.timer.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveFixedData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed3D data = dataFixed3D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchFixed3DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(statePackage[5].w, data.alpha_LocationZ.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

void main()
{
	mat4 currentMatrix;
	vec4 entityColor;
	vec4 entityEmissiveColor;
	instanceState(currentMatrix, entityColor, entityEmissiveColor);
	entityEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(statePackage[EMISSIVE_SA].y), statePackage[EMISSIVE_SA].x));
	int tangentIndex = int(float(gl_VertexID) * 0.3333333);
	mat3 modelRS = mat3(currentMatrix);
	vec3 tangent = texelFetch(vertexData_Tangent, tangentIndex).xyz;
	vec3 T = normalize(modelRS * tangent);
	vec3 N = normalize(modelRS * normal);
	T = normalize(T - dot(T, N) * N);

	vb_data.fragUV = uv;
	vb_data.fragEntityColor = entityColor;
	vb_data.fragMixEmissive = entityEmissiveColor;
	vb_data.fragTBN = mat3(T, cross(T, N), N);
	vec4 vertexPos = currentMatrix * vec4(vertex * baseSize, 1.0);
	vb_data.fragPos = vertexPos.xyz;
	vertexPos = gameViewport * vertexPos;
	vertexPos.z /= baseSize.z;
	if (max(entityColor.w, entityEmissiveColor.w) <= 0.0) vertexPos.xyz = vec3(-65536.0);
	gl_Position = vertexPos;
}
""";

        public final static String FRAG = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define EMISSIVE_SA 2
#define LIGHT_COLOR 3
#define SHADOW_COLOR 4
#define LIGHT_DIR 5
#define ALPHA_THRESHOLD 0.003

subroutine void surfaceStateDraw(inout vec3 diffuseParam, in vec3 currentNormal);
subroutine uniform surfaceStateDraw surfaceState;

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

// vec4(color), vec4(emissiveColor), vec4(alphaMix, colorMix, glowPower, anisotropic), vec4(lightColor), vec4(shadowColor), vec4(lightDirection, alpha)
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

//vec3 normalBlend(in vec3 face, in vec3 tex) {
//    vec3 t = face * 2.0 + vec3(-1.0, -1.0, 0.0);
//    vec3 u = tex * vec3(-2.0, -2.0, 2.0) + vec3(1.0, 1.0, -1.0);
//    return normalize(t * dot(t, u) / t.z - u);
//}

subroutine(surfaceStateDraw) void commonMode(inout vec3 diffuseParam, in vec3 currentNormal) {
    float brightnessRaw = max(dot(-statePackage[LIGHT_DIR].xyz, currentNormal), 0.0);
    vec3 shadowMix = mix(vec3(1.0), statePackage[SHADOW_COLOR].xyz, statePackage[SHADOW_COLOR].w);
    vec3 lightMix = mix(vec3(1.0), statePackage[LIGHT_COLOR].xyz, statePackage[LIGHT_COLOR].w);
    diffuseParam *= mix(shadowMix, lightMix, brightnessRaw);
}

subroutine(surfaceStateDraw) void colorMode(inout vec3 diffuseParam, in vec3 currentNormal) {
    diffuseParam *= statePackage[LIGHT_COLOR].xyz * statePackage[LIGHT_COLOR].w;
}

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

    surfaceState(diffuse.xyz, normalRaw.xyz);

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
""";
    }

    public final static class Sprite {
        private Sprite() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define TILE_STATE 3
#define UV_START_END 4
#define ENTITY_STATE 5

subroutine void uvMappingState(out vec2 uvStartP, out vec2 uvEndP);
subroutine uniform uvMappingState uvMapping;
subroutine void instanceStateCompute(out mat4 model, out vec4 mColor, out vec4 mEColor);
subroutine uniform instanceStateCompute instanceState;

layout (location = 0) in vec2 vertex;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic)
// [vec2(tile), startIndex, randomEach], [vec2(start), vec2(end)], hashCode, totalTilesMinusOne, vec2(baseSize)
uniform vec4 statePackage[6];
uniform float globalTimerAlpha;
uniform uvec3 additionEmissive_DataBit_InstanceOffset;

out VERTEX_BLOCK {
	mat3 fragTBN;
	vec2 fragUV;
	vec3 fragPos;
	vec4 fragEntityColor;
	vec4 fragMixEmissive;
} vb_data;

float hash12(vec2 p)
{
	vec3 p3 = fract(vec3(p.xyx) * 0.1031);
	p3 += dot(p3, p3.yzx + 33.33);
	return fract((p3.x + p3.y) * p3.z);
}

subroutine(uvMappingState) void commonUV(out vec2 uvStartP, out vec2 uvEndP) {
	uvStartP = statePackage[UV_START_END].xy;
	uvEndP = statePackage[UV_START_END].zw;
}

subroutine(uvMappingState) void tileUV(out vec2 uvStartP, out vec2 uvEndP) {
	float tileX = mod(statePackage[TILE_STATE].z, statePackage[TILE_STATE].x);
	float tileY = ceil((statePackage[TILE_STATE].z - tileX) / statePackage[TILE_STATE].x);
	vec2 tileSizeVec = 1.0 / statePackage[TILE_STATE].xy * (statePackage[UV_START_END].zw - statePackage[UV_START_END].xy);
	uvStartP = tileSizeVec * vec2(tileX, tileY) + statePackage[UV_START_END].xy;
	uvEndP = uvStartP + tileSizeVec;
}

subroutine(uvMappingState) void tileRUV(out vec2 uvStartP, out vec2 uvEndP) {
	vec2 seed = vec2(statePackage[ENTITY_STATE].x, 0.0);
	if (statePackage[TILE_STATE].w > 0.0) seed.y = float(gl_InstanceID << 1 + 7);
	float finalIndex = round(hash12(seed) * statePackage[ENTITY_STATE].y) + statePackage[TILE_STATE].z;
	if (finalIndex >= statePackage[ENTITY_STATE].y) finalIndex -= statePackage[ENTITY_STATE].y;
	float tileX = mod(finalIndex, statePackage[TILE_STATE].x);
	float tileY = round((finalIndex - tileX) / statePackage[TILE_STATE].x);
	vec2 tileSizeVec = 1.0 / statePackage[TILE_STATE].xy * (statePackage[UV_START_END].zw - statePackage[UV_START_END].xy);
	uvStartP = tileSizeVec * vec2(tileX, tileY) + statePackage[UV_START_END].xy;
	uvEndP = uvStartP + tileSizeVec;
}

#include "BUtil_InstanceDataSSBO.h"

subroutine(instanceStateCompute) void noneData(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	model = modelMatrix;
	mColor = statePackage[COLOR] * globalTimerAlpha;
	mEColor = statePackage[EMISSIVE_COLOR] * globalTimerAlpha;
}

subroutine(instanceStateCompute) void haveData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic2D data = dataDynamic2D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchDynamic2DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveFixedData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed2D data = dataFixed2D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchFixed2DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.alpha_Facing_Location.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic3D data = dataDynamic3D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchDynamic3DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveFixedData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed3D data = dataFixed3D[int(additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = modelMatrix * fetchFixed3DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.alpha_LocationZ.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

void main()
{
	mat4 currentMatrix;
	vec4 entityColor;
	vec4 entityEmissiveColor;
	instanceState(currentMatrix, entityColor, entityEmissiveColor);
	entityEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(statePackage[EMISSIVE_SA].y), statePackage[EMISSIVE_SA].x));
	vec2 startUV;
	vec2 endUV;
	uvMapping(startUV, endUV);
	vec2 uvs[] = vec2[4](startUV, vec2(endUV.x, startUV.y), vec2(startUV.x, endUV.y), endUV);
	vec3 T = normalize(currentMatrix[0].xyz);
	vec3 N = normalize(currentMatrix[2].xyz);

	vb_data.fragUV = uvs[gl_VertexID];
	vb_data.fragTBN = mat3(T, cross(T, N), N);
	vb_data.fragEntityColor = entityColor;
	vb_data.fragMixEmissive = entityEmissiveColor;
	vec4 vertexPos = currentMatrix * vec4(vertex * statePackage[ENTITY_STATE].zw, 0.0, 1.0);
	vb_data.fragPos = vertexPos.xyz;
	vertexPos = gameViewport * vertexPos;
	if (max(entityColor.w, entityEmissiveColor.w) <= 0.0) vertexPos.xyz = vec3(-65536.0);
	gl_Position = vertexPos;
}
""";

        public final static String FRAG = """
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
""";
    }

    public final static class Curve {
        private Curve() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define CURVE_STATE 3

subroutine void instanceStateCompute(out mat4 model, out vec4 mColor, out vec4 mEColor);
subroutine uniform instanceStateCompute instanceState;

layout (location = 0) in vec2 nodeLocation;
layout (location = 1) in vec4 nodeTangent;
layout (location = 2) in float nodeWidth;
layout (location = 3) in float nodeMixFactor;
layout (location = 4) in float nodeDistance;
layout (location = 5) in vec4 nodeColor;
layout (location = 6) in vec4 nodeEmissive;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];
uniform float globalTimerAlpha;
uniform int instanceOffset;

out VERT_TESC_BLOCK {
	flat mat4 tescMatrix;
	flat vec4 tescPoints;
	flat vec4 tescColor;
	flat vec4 tescEmissiveColor;
	flat float tescWidth;
	flat float tescMixFactor;
	flat float tescID;
	flat float tescDistance;
} vtb_data;

#include "BUtil_InstanceDataSSBO.h"

subroutine(instanceStateCompute) void noneData(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	model = modelMatrix;
	mColor = statePackage[COLOR] * globalTimerAlpha;
	mEColor = statePackage[EMISSIVE_COLOR] * globalTimerAlpha;
}

subroutine(instanceStateCompute) void haveData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic2D data = dataDynamic2D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchDynamic2DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveFixedData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed2D data = dataFixed2D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchFixed2DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.alpha_Facing_Location.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic3D data = dataDynamic3D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchDynamic3DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

subroutine(instanceStateCompute) void haveFixedData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed3D data = dataFixed3D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchFixed3DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(globalTimerAlpha, data.alpha_LocationZ.x), mColor, mEColor);
    mColor *= statePackage[COLOR];
    mEColor *= statePackage[EMISSIVE_COLOR];
}

void main()
{
	mat4 currentMatrix;
	vec4 entityColor;
	vec4 entityEmissiveColor;
	instanceState(currentMatrix, entityColor, entityEmissiveColor);
	entityColor *= nodeColor;
	entityEmissiveColor *= nodeEmissive;

	vtb_data.tescMatrix = currentMatrix;
	vtb_data.tescPoints = nodeLocation.xyxy + nodeTangent;
	vtb_data.tescColor = entityColor;
	vtb_data.tescEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(statePackage[EMISSIVE_SA].y), statePackage[EMISSIVE_SA].x));
	vtb_data.tescWidth = nodeWidth;
	vtb_data.tescMixFactor = nodeMixFactor;
	vtb_data.tescID = gl_VertexID == 0 ? 0.0 : ceil(float(gl_VertexID) / 2.0);
	vtb_data.tescDistance = nodeDistance;
	gl_Position = vec4(nodeLocation, 0.0, 1.0);
}
""";

        public final static String TESC = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define CURVE_STATE 3

layout(vertices = 2) out;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, timerAlpha), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];

in VERT_TESC_BLOCK {
    flat mat4 tescMatrix;
    flat vec4 tescPoints;
    flat vec4 tescColor;
    flat vec4 tescEmissiveColor;
    flat float tescWidth;
    flat float tescMixFactor;
    flat float tescID;
    flat float tescDistance;
} vtb_datas[];

out TESC_TESE_BLOCK {
    flat mat4 teseMatrix;
    flat vec4 tesePoints;
    flat vec4 teseColor;
    flat vec4 teseEmissiveColor;
    flat float teseWidth;
    flat float teseMixFactor;
    flat float teseID;
    flat float teseDistance;
} ttb_datas[];

void main()
{
    ttb_datas[gl_InvocationID].teseMatrix = vtb_datas[gl_InvocationID].tescMatrix;
    ttb_datas[gl_InvocationID].tesePoints = vtb_datas[gl_InvocationID].tescPoints;
    ttb_datas[gl_InvocationID].teseColor = vtb_datas[gl_InvocationID].tescColor;
    ttb_datas[gl_InvocationID].teseEmissiveColor = vtb_datas[gl_InvocationID].tescEmissiveColor;
    ttb_datas[gl_InvocationID].teseWidth = vtb_datas[gl_InvocationID].tescWidth;
    ttb_datas[gl_InvocationID].teseMixFactor = vtb_datas[gl_InvocationID].tescMixFactor;
    ttb_datas[gl_InvocationID].teseID = vtb_datas[gl_InvocationID].tescID;
    ttb_datas[gl_InvocationID].teseDistance = vtb_datas[gl_InvocationID].tescDistance;
    gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
    if (gl_InvocationID == 0) {
        gl_TessLevelOuter[0] = 1.0;
        gl_TessLevelOuter[1] = statePackage[CURVE_STATE].x;
    }
}
""";

        public final static String TESE = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define CURVE_STATE 3
#define CURVE_FILL 4

layout(isolines, equal_spacing, ccw) in;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, timerAlpha), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];
uniform float totalNodes;

in TESC_TESE_BLOCK {
    flat mat4 teseMatrix;
    flat vec4 tesePoints;
    flat vec4 teseColor;
    flat vec4 teseEmissiveColor;
    flat float teseWidth;
    flat float teseMixFactor;
    flat float teseID;
    flat float teseDistance;
} ttb_datas[];

out TESE_GEOM_BLOCK {
    flat mat4 geomMatrix;
    flat vec2 geomNormal;
    flat vec4 geomColor;
    flat vec4 geomEmissiveColor;
    flat float geomWidth;
    flat float geomUV;
} tgb_data;

void main()
{
    bool directCheck = statePackage[CURVE_STATE].x <= 1.0;
    float factor1 = gl_TessCoord.x;
    float factor1P2 = factor1 + factor1;
    float factor2 = factor1 * factor1;
    float factor2P2 = factor2 + factor2;
    float factor3 = factor2 * factor1;
    float mixFactor = pow(factor1, ttb_datas[0].teseMixFactor);
    float oneMinusF1 = 1.0 - factor1;
    float oneMinusF1M2 = oneMinusF1 * oneMinusF1;
    vec4 midPoints = vec4(ttb_datas[0].tesePoints.zw, ttb_datas[1].tesePoints.xy) * 3.0;
    vec2 tmp0 = oneMinusF1M2 * oneMinusF1 * gl_in[0].gl_Position.xy;
    vec2 tmp1 = (factor1 - factor2P2 + factor3) * midPoints.xy;
    vec2 tmp2 = (factor2 - factor3) * midPoints.zw;
    vec2 tmp3 = factor3 * gl_in[1].gl_Position.xy;

    vec2 tmpT0 = oneMinusF1M2 * gl_in[0].gl_Position.xy * - 3.0;
    vec2 tmpT1 = (oneMinusF1M2 - factor1P2 + factor2P2) * midPoints.xy;
    vec2 tmpT2 = (factor1P2 - factor2 - factor2P2) * midPoints.zw;
    vec2 tmpT3 = factor2 * gl_in[1].gl_Position.xy * 3.0;

    bool useGlobal = statePackage[CURVE_STATE].z > 0.0;
    vec2 ids = vec2(ttb_datas[0].teseID, ttb_datas[1].teseID);
    vec2 uv = useGlobal ? (statePackage[CURVE_STATE].z * ids) : (vec2(ttb_datas[0].teseDistance, ttb_datas[1].teseDistance) / statePackage[CURVE_STATE].y);
    tgb_data.geomMatrix = ttb_datas[0].teseMatrix;
    vec2 currentTangent = directCheck ? (gl_in[1].gl_Position.xy - gl_in[0].gl_Position.xy) : vec2(tmpT0 + tmpT1 + tmpT2 + tmpT3);
    tgb_data.geomNormal = normalize(vec2(-currentTangent.y, currentTangent.x));
    ids /= totalNodes;
    float fillUV = mix(ids.x, ids.y, factor1);
    vec2 fillMix = smoothstep(statePackage[CURVE_FILL].zw, vec2(1.0), vec2(1.0 - fillUV, fillUV)) * (1.0 - statePackage[CURVE_FILL].xy);
    fillMix = 1.0 - fillMix;
    float fillFactor = clamp(fillMix.x * fillMix.y, 0.0, 1.0);
    vec4 color = mix(ttb_datas[0].teseColor, ttb_datas[1].teseColor, mixFactor);
    color.w *= fillFactor;
    tgb_data.geomColor = color;
    vec4 emissive = mix(ttb_datas[0].teseEmissiveColor, ttb_datas[1].teseEmissiveColor, mixFactor);
    emissive.w *= fillFactor;
    tgb_data.geomEmissiveColor = emissive;
    tgb_data.geomWidth = mix(ttb_datas[0].teseWidth, ttb_datas[1].teseWidth, mixFactor);
    tgb_data.geomUV = mix(uv.x, uv.y, factor1) - statePackage[CURVE_STATE].w;
    gl_Position = directCheck ? gl_in[uint(factor1)].gl_Position : vec4(tmp0 + tmp1 + tmp2 + tmp3, 0.0, 1.0);
}
""";

        public final static String GEOM = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define CURVE_STATE 3

layout (lines) in;
layout (triangle_strip, max_vertices = 4) out;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];

in TESE_GEOM_BLOCK {
	flat mat4 geomMatrix;
	flat vec2 geomNormal;
	flat vec4 geomColor;
	flat vec4 geomEmissiveColor;
	flat float geomWidth;
	flat float geomUV;
} tgb_datas[];

out GEOM_FRAG_BLOCK {
	mat3 fragTBN;
	vec2 fragUV;
	vec3 fragPos;
	vec4 fragEntityColor;
	vec4 fragMixEmissive;
} gfb_data;

void main()
{
	float time = statePackage[CURVE_STATE].w;
	mat4 matrix = tgb_datas[0].geomMatrix;
	vec4 startOffset = vec4(tgb_datas[0].geomNormal * tgb_datas[0].geomWidth, 0.0, 0.0);
	vec4 endOffset = vec4(tgb_datas[1].geomNormal * tgb_datas[1].geomWidth, 0.0, 0.0);
	mat3 TBN;
	vec4 pos;

	if (max(tgb_datas[0].geomColor.w, tgb_datas[0].geomEmissiveColor.w) <= 0.0 && max(tgb_datas[1].geomColor.w, tgb_datas[1].geomEmissiveColor.w) <= 0.0) {
		matrix = mat4(0.0, 0.0, 0.0, -65536.0, 0.0, 0.0, 0.0, -65536.0, 0.0, 0.0, 0.0, -65536.0, 0.0, 0.0, 0.0, 1.0);
		startOffset = endOffset = vec4(0.0);
		TBN = mat3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);
	} else {
		TBN = mat3(normalize(matrix[0].xyz), 0.0, 0.0, 0.0, normalize(matrix[2].xyz));
		TBN[1] = cross(TBN[0], TBN[2]);
	}
	gfb_data.fragTBN = TBN;
	gfb_data.fragEntityColor = tgb_datas[0].geomColor;
	gfb_data.fragMixEmissive = tgb_datas[0].geomEmissiveColor;
	gfb_data.fragUV = vec2(tgb_datas[0].geomUV, 1.0);
	pos = matrix * (gl_in[0].gl_Position + startOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	gfb_data.fragUV = vec2(tgb_datas[0].geomUV, 0.0);
	pos = matrix * (gl_in[0].gl_Position - startOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	gfb_data.fragEntityColor = tgb_datas[1].geomColor;
	gfb_data.fragMixEmissive = tgb_datas[1].geomEmissiveColor;
	gfb_data.fragUV = vec2(tgb_datas[1].geomUV, 1.0);
	pos = matrix * (gl_in[1].gl_Position + endOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	gfb_data.fragUV = vec2(tgb_datas[1].geomUV, 0.0);
	pos = matrix * (gl_in[1].gl_Position - endOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	EndPrimitive();
}
""";

        public final static String FRAG = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define EMISSIVE_SA 2
#define ALPHA_THRESHOLD 0.003

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

in GEOM_FRAG_BLOCK {
    mat3 fragTBN;
    vec2 fragUV;
    vec3 fragPos;
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
} gfb_data;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];
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

vec3 encodePos(in vec3 posRaw) {
    return clamp((posRaw - vec3(gameScreenBorder.xy, -6400.0)) / vec3(gameScreenBorder.zw, 12800.0), vec3(0.0), vec3(1.0));
}

void main()
{
    vec4 diffuse = texture(diffuseMap, gfb_data.fragUV) * gfb_data.fragEntityColor;
    vec4 emissive = texture(emissiveMap, gfb_data.fragUV) * gfb_data.fragMixEmissive;
    if (diffuse.w + emissive.w <= ALPHA_THRESHOLD) discard;

    bool ignoreIllum = (additionEmissive_DataBit.y & 2u) == 2u;
    vec4 normalRaw = texture(normalMap, gfb_data.fragUV);
    normalRaw.xyz = fma(normalRaw.xyz, vec3(2.0), vec3(-1.0));
    if (normalRaw.w <= 0.0) normalRaw.xyz = vec3(0.0, 0.0, 1.0); else normalRaw.xyz = gfb_data.fragTBN * normalRaw.xyz;
    normalRaw.w = diffuse.w;
    vec3 complexRaw = texture(complexMap, gfb_data.fragUV).xyz;
    emissive.xyz += diffuse.xyz * complexRaw.x;

    diffuse.w = min(diffuse.w, 1.0);
    fragColor = additionEmissive_DataBit.x == 1u ? (diffuse + emissive * emissive.w) : mix(diffuse, emissive, emissive.w);
    emissive *= statePackage[EMISSIVE_SA].z;
    emissive.w = min(emissive.w, 1.0);
    float cullAlpha = max(diffuse.w, emissive.w);
    fragEmissive = emissive;
    fragWorldPos = ignoreIllum ? vec4(0.0) : vec4(encodePos(gfb_data.fragPos), step(ALPHA_THRESHOLD, cullAlpha));
    fragWorldNormal = ignoreIllum ? vec4(0.0, 0.0, 1.0, 0.0) : normalRaw;
    fragWorldTangent = ignoreIllum ? vec4(1.0, 0.0, 0.0, 0.0) : ((statePackage[EMISSIVE_SA].w == 0.0) ? vec4(1.0, 0.0, 0.0, diffuse.w) : vec4(gfb_data.fragTBN * texture(tangentMap, gfb_data.fragUV).xyz, diffuse.w));
    fragMaterial = ignoreIllum ? vec4(0.0, 0.0, 0.0, 0.0) : vec4(complexRaw.yz, statePackage[EMISSIVE_SA].w, diffuse.w);
    fragData = (cullAlpha > 0.0) ? uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), cullAlpha) * 1023.0), additionEmissive_DataBit.y, 0u) : uvec4(uvec3(0u), 1u);
}
""";
    }

    public final static class Segment {
        private Segment() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2

layout (location = 0) in vec2 nodeLocation;
layout (location = 1) in vec2 nodeTangent;
layout (location = 2) in float nodeWidth;
layout (location = 3) in float nodeMixFactor;
layout (location = 4) in float nodeDistance;
layout (location = 5) in vec4 nodeColor;
layout (location = 6) in vec4 nodeEmissive;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, reversed, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];
uniform float globalTimerAlpha;

out VERT_TESC_BLOCK {
	flat mat4 tescMatrix;
	flat vec2 tescPoint;
	flat vec4 tescColor;
	flat vec4 tescEmissiveColor;
	flat float tescWidth;
	flat float tescMixFactor;
	flat float tescDistance;
} vtb_data;

void main()
{
	vec4 entityColor = statePackage[COLOR] * nodeColor * globalTimerAlpha;
	vec4 entityEmissiveColor = statePackage[EMISSIVE_COLOR] * nodeEmissive * globalTimerAlpha;

	vtb_data.tescMatrix = modelMatrix;
	vtb_data.tescPoint = nodeLocation + nodeTangent;
	vtb_data.tescColor = entityColor;
	vtb_data.tescEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(statePackage[EMISSIVE_SA].y), statePackage[EMISSIVE_SA].x));
	vtb_data.tescWidth = nodeWidth;
	vtb_data.tescMixFactor = nodeMixFactor;
	vtb_data.tescDistance = nodeDistance;
	gl_Position = vec4(nodeLocation, 0.0, 1.0);
}
""";

        public final static String TESC = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define CURVE_STATE 3

layout(vertices = 2) out;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, reversed, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];

in VERT_TESC_BLOCK {
    flat mat4 tescMatrix;
    flat vec2 tescPoint;
    flat vec4 tescColor;
    flat vec4 tescEmissiveColor;
    flat float tescWidth;
    flat float tescMixFactor;
    flat float tescDistance;
} vtb_datas[];

out TESC_TESE_BLOCK {
    flat mat4 teseMatrix;
    flat vec2 tesePoint;
    flat vec4 teseColor;
    flat vec4 teseEmissiveColor;
    flat float teseWidth;
    flat float teseMixFactor;
    flat float teseDistance;
} ttb_datas[];

void main()
{
    ttb_datas[gl_InvocationID].teseMatrix = vtb_datas[gl_InvocationID].tescMatrix;
    ttb_datas[gl_InvocationID].tesePoint = vtb_datas[gl_InvocationID].tescPoint;
    ttb_datas[gl_InvocationID].teseColor = vtb_datas[gl_InvocationID].tescColor;
    ttb_datas[gl_InvocationID].teseEmissiveColor = vtb_datas[gl_InvocationID].tescEmissiveColor;
    ttb_datas[gl_InvocationID].teseWidth = vtb_datas[gl_InvocationID].tescWidth;
    ttb_datas[gl_InvocationID].teseMixFactor = vtb_datas[gl_InvocationID].tescMixFactor;
    ttb_datas[gl_InvocationID].teseDistance = vtb_datas[gl_InvocationID].tescDistance;
    gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
    if (gl_InvocationID == 0) {
        gl_TessLevelOuter[0] = 1.0;
        gl_TessLevelOuter[1] = statePackage[CURVE_STATE].x;
    }
}
""";

        public final static String TESE = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define CURVE_STATE 3
#define CURVE_FILL 4

layout(isolines, equal_spacing, ccw) in;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, reversed, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];

in TESC_TESE_BLOCK {
    flat mat4 teseMatrix;
    flat vec2 tesePoint;
    flat vec4 teseColor;
    flat vec4 teseEmissiveColor;
    flat float teseWidth;
    flat float teseMixFactor;
    flat float teseDistance;
} ttb_datas[];

out TESE_GEOM_BLOCK {
    flat mat4 geomMatrix;
    flat vec2 geomNormal;
    flat vec4 geomColor;
    flat vec4 geomEmissiveColor;
    flat float geomWidth;
    flat float geomUV;
} tgb_data;

void main()
{
    bool directCheck = statePackage[CURVE_STATE].x <= 1.0;
    float factor1 = gl_TessCoord.x;
    float factor1P2 = factor1 + factor1;
    float factor2 = factor1 * factor1;
    float factor2P2 = factor2 + factor2;
    float factor3 = factor2 * factor1;
    float mixFactor = pow(factor1, ttb_datas[0].teseMixFactor);
    float oneMinusF1 = 1.0 - factor1;
    float oneMinusF1M2 = oneMinusF1 * oneMinusF1;
    vec4 midPoints = vec4(ttb_datas[0].tesePoint, ttb_datas[1].tesePoint) * 3.0;
    vec2 tmp0 = oneMinusF1M2 * oneMinusF1 * gl_in[0].gl_Position.xy;
    vec2 tmp1 = (factor1 - factor2P2 + factor3) * midPoints.xy;
    vec2 tmp2 = (factor2 - factor3) * midPoints.zw;
    vec2 tmp3 = factor3 * gl_in[1].gl_Position.xy;

    vec2 tmpT0 = oneMinusF1M2 * gl_in[0].gl_Position.xy * -3.0;
    vec2 tmpT1 = (oneMinusF1M2 - factor1P2 + factor2P2) * midPoints.xy;
    vec2 tmpT2 = (factor1P2 - factor2 - factor2P2) * midPoints.zw;
    vec2 tmpT3 = factor2 * gl_in[1].gl_Position.xy * 3.0;

    vec2 uv = vec2(ttb_datas[0].teseDistance, ttb_datas[1].teseDistance) / statePackage[CURVE_STATE].y;
    tgb_data.geomMatrix = ttb_datas[0].teseMatrix;
    vec2 currentTangent = directCheck ? (gl_in[1].gl_Position.xy - gl_in[0].gl_Position.xy) : vec2(tmpT0 + tmpT1 + tmpT2 + tmpT3);
    tgb_data.geomNormal = normalize(vec2(-currentTangent.y, currentTangent.x));
    vec2 fillMix = smoothstep(statePackage[CURVE_FILL].zw, vec2(1.0), vec2(1.0 - factor1, factor1)) * (1.0 - statePackage[CURVE_FILL].xy);
    fillMix = 1.0 - fillMix;
    float fillFactor = clamp(fillMix.x * fillMix.y, 0.0, 1.0);
    vec4 color = mix(ttb_datas[0].teseColor, ttb_datas[1].teseColor, mixFactor);
    color.w *= fillFactor;
    tgb_data.geomColor = color;
    vec4 emissive = mix(ttb_datas[0].teseEmissiveColor, ttb_datas[1].teseEmissiveColor, mixFactor);
    emissive.w *= fillFactor;
    tgb_data.geomEmissiveColor = emissive;
    tgb_data.geomWidth = mix(ttb_datas[0].teseWidth, ttb_datas[1].teseWidth, mixFactor);
    tgb_data.geomUV = mix(uv.x, uv.y, factor1) - statePackage[CURVE_STATE].w;
    gl_Position = directCheck ? gl_in[uint(factor1)].gl_Position : vec4(tmp0 + tmp1 + tmp2 + tmp3, 0.0, 1.0);
}
""";
    }

    public final static class Trail {
        private Trail() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define COM_STATE 3
#define FILL_DATA 4
#define WIDTH_DATA 5
#define START_COLOR 6
#define END_COLOR 7
#define START_EMISSIVE 8
#define END_EMISSIVE 9

subroutine void lineMode(out vec2 posOut, out vec2 normalOut, out vec4 entityColorOut, out vec4 entityEmissiveColorOut, out float factorOut, out float widthOut, out float distanceOut);
subroutine uniform lineMode lineModeState;

layout (location = 0) in vec2 disabled;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

// vec2(point)
layout (binding = 10) uniform samplerBuffer nodeMap;
uniform mat4 modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(time, nodeCount, texturePixels, mixFactor), vec4(fillStart, fillEnd, startFactor, endFactor), vec4(startWidth, endWidth, jitterPower, flickerMix)
// 6: vec4(start), 7: vec4(end), 8: vec4(startEmissive), 9: vec4(endEmissive)
uniform vec4 statePackage[10];
// hashCode, hashCodeTime, globalTimerAlpha
uniform vec3 extraData;

out VERT_GEOM_BLOCK {
	flat mat4 geomMatrix;
	flat vec4 geomColor;
	flat vec4 geomEmissiveColor;
	flat vec4 geomNormalWidthDistance;
	flat float geomFactor;
} vgb_data;

float flickRandom(in float seed) {
	vec2 tanSeed = tan(vec2(seed) * vec2(0.42, 4.2) + vec2(12.7, 51.97));
	float result = fract(smoothstep(1.0, 0.0, sin(tanSeed.x) + abs(tanSeed.y)) * 2.0);
	return 1.0 - sqrt(result);
}

float getFlick() {
	float seed = abs(extraData.y);
	if (extraData.y < 0.0) seed += float(gl_InstanceID << 1);
	return (statePackage[WIDTH_DATA].w <= -1.0f) ? 1.0 : mix(1.0, flickRandom(seed), statePackage[WIDTH_DATA].w);
}

subroutine(lineMode) void lineStripMode(out vec2 posOut, out vec2 normalOut, out vec4 entityColorOut, out vec4 entityEmissiveColorOut, out float factorOut, out float widthOut, out float distanceOut) {
	int currIndex = gl_InstanceID + gl_VertexID;
	float factor = clamp(float(currIndex) / statePackage[COM_STATE].y, 0.0, 1.0);
	float factorPow = pow(factor, statePackage[COM_STATE].w);
	vec3 rawNodeData = texelFetch(nodeMap, currIndex).xyz;
	int leftIndex = max(currIndex - 1, 0);
	int rightIndex = min(currIndex + 1, int(statePackage[COM_STATE].y));
	vec2 leftNormal = rawNodeData.yx - texelFetch(nodeMap, leftIndex).yx;
	if (leftIndex != currIndex) {
		leftNormal.x = -leftNormal.x;
		leftNormal = normalize(leftNormal);
	} else leftNormal = vec2(0.0);
	vec2 rightNormal = texelFetch(nodeMap, rightIndex).yx - rawNodeData.yx;
	if (rightIndex != currIndex) {
		rightNormal.x = -rightNormal.x;
		rightNormal = normalize(rightNormal);
	} else rightNormal = vec2(0.0);

	posOut = rawNodeData.xy;
	normalOut = normalize(leftNormal + rightNormal);
	entityColorOut = mix(statePackage[START_COLOR], statePackage[END_COLOR], factorPow);
	entityEmissiveColorOut = mix(statePackage[START_EMISSIVE], statePackage[END_EMISSIVE], factorPow);
	factorOut = factor;
	widthOut = mix(statePackage[WIDTH_DATA].x, statePackage[WIDTH_DATA].y, factorPow);
	distanceOut = rawNodeData.z;
}

subroutine(lineMode) void linesMode(out vec2 posOut, out vec2 normalOut, out vec4 entityColorOut, out vec4 entityEmissiveColorOut, out float factorOut, out float widthOut, out float distanceOut) {
	bool pickStart = gl_VertexID == 0;
	int currIndex = (gl_InstanceID << 1) + gl_VertexID;
	vec3 rawNodeData = texelFetch(nodeMap, currIndex).xyz;
	if (pickStart) ++currIndex; else --currIndex;
	vec3 twinNodeData = texelFetch(nodeMap, currIndex).xyz;
	vec2 normal = rawNodeData.yx - twinNodeData.yx;
	normal.x = -normal.x;

	posOut = rawNodeData.xy;
	normalOut = normalize(pickStart ? -normal : normal);
	factorOut = float(gl_VertexID);
	if (pickStart) {
		entityColorOut = statePackage[START_COLOR];
		entityEmissiveColorOut = statePackage[START_EMISSIVE];
		widthOut = statePackage[WIDTH_DATA].x;
		distanceOut = 0.0;
	} else {
		entityColorOut = statePackage[END_COLOR];
		entityEmissiveColorOut = statePackage[END_EMISSIVE];
		widthOut = statePackage[WIDTH_DATA].y;
		distanceOut = rawNodeData.z - twinNodeData.z;
	}
}

void main()
{
	vec4 entityColorOut, entityEmissiveColorOut;
	vec2 posOut, normalOut;
	float factorOut, widthOut, distanceOut;
	lineModeState(posOut, normalOut, entityColorOut, entityEmissiveColorOut, factorOut, widthOut, distanceOut);

	float flicker = getFlick() * extraData.z;
	vec4 entityColor = entityColorOut * statePackage[COLOR] * flicker;
	vec4 entityEmissiveColor = entityEmissiveColorOut * statePackage[EMISSIVE_COLOR] * flicker;

	vgb_data.geomMatrix = modelMatrix;
	vgb_data.geomColor = entityColor;
	vgb_data.geomEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(statePackage[EMISSIVE_SA].y), statePackage[EMISSIVE_SA].x));
	vgb_data.geomNormalWidthDistance = vec4(normalOut, widthOut, distanceOut);
	vgb_data.geomFactor = factorOut;
	gl_Position = vec4(posOut, 0.0, 1.0);
}
""";

        public final static String GEOM = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COM_STATE 3

layout (lines) in;
layout (triangle_strip, max_vertices = 4) out;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(time, nodeCount, texturePixels, mixFactor), vec4(fillStart, fillEnd, startFactor, endFactor), vec4(startWidth, endWidth, jitterPower, flickerMix)
// 6: vec4(start), 7: vec4(end), 8: vec4(startEmissive), 9: vec4(endEmissive)
uniform vec4 statePackage[10];

in VERT_GEOM_BLOCK {
	flat mat4 geomMatrix;
	flat vec4 geomColor;
	flat vec4 geomEmissiveColor;
	flat vec4 geomNormalWidthDistance;
	flat float geomFactor;
} vgb_datas[];

out GEOM_FRAG_BLOCK {
	mat3 fragTBN;
	vec4 fragUVSeedFactor;
	vec3 fragPos;
	vec4 fragEntityColor;
	vec4 fragMixEmissive;
} gfb_data;

void main()
{
	mat4 matrix = vgb_datas[0].geomMatrix;
	vec4 startOffset = vec4(vgb_datas[0].geomNormalWidthDistance.xy * vgb_datas[0].geomNormalWidthDistance.z, 0.0, 0.0);
	vec4 endOffset = vec4(vgb_datas[1].geomNormalWidthDistance.xy * vgb_datas[1].geomNormalWidthDistance.z, 0.0, 0.0);
	vec2 uv = (vec2(vgb_datas[0].geomNormalWidthDistance.w, vgb_datas[1].geomNormalWidthDistance.w) / statePackage[COM_STATE].z) - statePackage[COM_STATE].x;
	float seed;
	mat3 TBN;
	vec4 pos;

	if (max(vgb_datas[0].geomColor.w, vgb_datas[0].geomEmissiveColor.w) <= 0.0 && max(vgb_datas[1].geomColor.w, vgb_datas[1].geomEmissiveColor.w) <= 0.0) {
		matrix = mat4(0.0, 0.0, 0.0, -65536.0, 0.0, 0.0, 0.0, -65536.0, 0.0, 0.0, 0.0, -65536.0, 0.0, 0.0, 0.0, 1.0);
		startOffset = endOffset = vec4(0.0);
		TBN = mat3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);
	} else {
		TBN = mat3(normalize(matrix[0].xyz), 0.0, 0.0, 0.0, normalize(matrix[2].xyz));
		TBN[1] = cross(TBN[0], TBN[2]);
	}
	gfb_data.fragTBN = TBN;
	seed = fract((gl_in[0].gl_Position.x + gl_in[0].gl_Position.y) * 0.255) + 127.0;
	seed *= 640.0;
	gfb_data.fragEntityColor = vgb_datas[0].geomColor;
	gfb_data.fragMixEmissive = vgb_datas[0].geomEmissiveColor;
	gfb_data.fragUVSeedFactor = vec4(uv.x, 1.0, seed, vgb_datas[0].geomFactor);
	pos = matrix * (gl_in[0].gl_Position + startOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	gfb_data.fragUVSeedFactor = vec4(uv.x, 0.0, seed, vgb_datas[0].geomFactor);
	pos = matrix * (gl_in[0].gl_Position - startOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	seed = fract((gl_in[1].gl_Position.x + gl_in[1].gl_Position.y) * 0.255) + 127.0;
	seed *= 640.0;
	gfb_data.fragEntityColor = vgb_datas[1].geomColor;
	gfb_data.fragMixEmissive = vgb_datas[1].geomEmissiveColor;
	gfb_data.fragUVSeedFactor = vec4(uv.y, 1.0, seed, vgb_datas[1].geomFactor);
	pos = matrix * (gl_in[1].gl_Position + endOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	gfb_data.fragUVSeedFactor = vec4(uv.y, 0.0, seed, vgb_datas[1].geomFactor);
	pos = matrix * (gl_in[1].gl_Position - endOffset);
	gfb_data.fragPos = pos.xyz;
	gl_Position = gameViewport * pos;
	EmitVertex();
	EndPrimitive();
}
""";

        public final static String FRAG = """
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
""";
    }

    public final static class Flare {
        private Flare() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define FRINGE_COLOR 0
#define CORE_COLOR 1
#define STATE_A 2
#define STATE_B 3
#define STATE_EXT 4

subroutine void instanceStateCompute(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker);
subroutine uniform instanceStateCompute instanceState;

layout (location = 0) in vec2 vertex;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 modelMatrix;
// vec4(fringeColor), vec4(coreColor), vec4(size, aspect, flick/syncFlick), vec4(alpha, hashCode, glowPower, frameAmount), vec4(noisePower, flickMix, globalAlpha, discRatio)
uniform vec4 statePackage[5];
uniform int instanceOffset;

out VERTEX_BLOCK {
	vec2 fragUV;
	flat vec4 fragFringeColor;
	flat vec4 fragCoreColor;
	flat vec2 fragNoiseOffsetAlpha;
} vb_data;

#include "BUtil_InstanceDataSSBO.h"

float flickRandom(in float seed) {
	vec2 tanSeed = tan(vec2(seed) * vec2(0.42, 4.2) + vec2(12.7, 51.97));
	float result = fract(smoothstep(1.0, 0.0, sin(tanSeed.x) + abs(tanSeed.y)) * 2.0);
	return 1.0 - sqrt(result);
}

float getFlick() {
	uint flickState = uint(statePackage[STATE_A].w);
	float flickOffset = ((flickState & 1u) == 1u) ? statePackage[STATE_B].y : (float(gl_InstanceID << 2) - statePackage[STATE_B].y) * 0.01;
	return (flickState > 2u) ? mix(1.0, flickRandom(flickOffset + statePackage[STATE_B].w), statePackage[STATE_EXT].y) : 1.0;
}

subroutine(instanceStateCompute) void noneData(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
	model = modelMatrix;
    mCColor = statePackage[CORE_COLOR];
    mFColor = statePackage[FRINGE_COLOR];

	float flick = getFlick();
    flicker = flick;
	currAlpha = statePackage[STATE_B].x * flick;
}

subroutine(instanceStateCompute) void haveData2D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Dynamic2D data = dataDynamic2D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchDynamic2DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE_B].x, data.timer.x);
    decodeDynamicColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= statePackage[CORE_COLOR];
    mFColor *= statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

subroutine(instanceStateCompute) void haveFixedData2D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Fixed2D data = dataFixed2D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchFixed2DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE_B].x, data.alpha_Facing_Location.x);
    decodeFixedColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= statePackage[CORE_COLOR];
    mFColor *= statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

subroutine(instanceStateCompute) void haveData3D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Dynamic3D data = dataDynamic3D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchDynamic3DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE_B].x, data.timer.x);
    decodeDynamicColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= statePackage[CORE_COLOR];
    mFColor *= statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

subroutine(instanceStateCompute) void haveFixedData3D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Fixed3D data = dataFixed3D[instanceOffset + gl_InstanceID];
    model = modelMatrix * fetchFixed3DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE_B].x, data.alpha_LocationZ.x);
    decodeFixedColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= statePackage[CORE_COLOR];
    mFColor *= statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

void main()
{
	mat4 currentMatrix;
	vec4 entityFringeColor;
	vec4 entityCoreColor;
	float entityAlpha;
	float entityFlicker;
	instanceState(currentMatrix, entityCoreColor, entityFringeColor, entityAlpha, entityFlicker);

	vb_data.fragUV = vertex;
	vb_data.fragFringeColor = entityFringeColor;
	vb_data.fragCoreColor = entityCoreColor;
	vb_data.fragNoiseOffsetAlpha = vec2(mod(length(vec3(currentMatrix[3].xyz)), 100.0), entityAlpha * statePackage[STATE_EXT].z);
	vec4 vertexPos = gameViewport * currentMatrix * vec4(vertex * vec2(fma(entityFlicker, 0.42, 0.58) * statePackage[STATE_A].x, statePackage[STATE_A].y), 0.0, 1.0);
	if (max(entityFringeColor.w, entityCoreColor.w) <= 0.0) vertexPos.xyz = vec3(-65536.0);
	gl_Position = vertexPos;
}
""";

        public final static String FRAG = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define SHARP_EDGE_SMOOTH 0.42
#define DISC_THICKNESS_SCALE 2.0
#define SHARP_DISC_THICKNESS_SCALE 4.0
#define STATE_A 2
#define STATE_B 3
#define STATE_EXT 4

subroutine vec4 flareStateDraw(in vec2 uv);
subroutine uniform flareStateDraw flareState;

in VERTEX_BLOCK {
    vec2 fragUV;
    flat vec4 fragFringeColor;
    flat vec4 fragCoreColor;
    flat vec2 fragNoiseOffsetAlpha;
} vb_data;

// vec4(fringeColor), vec4(coreColor), vec4(size, aspect, flick/syncFlick), vec4(alpha, hashCode, glowPower, frameAmount), vec4(noisePower, flickMix, globalAlpha, discRatio)
uniform vec4 statePackage[5];
uniform uint dataBit;

layout (location = 0) out vec4 fragColor; // draw to RGB8
layout (location = 1) out vec4 fragEmissive; // draw to RGB8
layout (location = 6) out uvec4 fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.

float hash(in float p) {
    float f = fract(p * 0.011);
    f *= f + 7.5;
    return fract(f * (f + f));
}

float noise(in float x) {
    float f = fract(x);
    return mix(hash(x), hash(x + 1.0), f * f * (3.0 - 2.0 * f));
}

float fbm(in float x) {
    float v = 0.0;
    float a = 0.5;
    float f = x;
    for (int i = 0; i < 3; i++) {
        v += a * noise(f);
        f = f * 2.0 + 100.0;
        a *= 0.5;
    }
    return v;
}

float fi(in float a, in float b) {
    return 1.0 - (1.0 - a) * (1.0 - b);
}

subroutine(flareStateDraw) vec4 smoothMode(in vec2 uv) {
    float fringe = smoothstep(1.0, 0.0, length(uv)) * 2.0;
    float core = smoothstep(1.0, 0.0, length(uv * 2.0));
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    return mix(fringeColor, vb_data.fragCoreColor, core);
}

subroutine(flareStateDraw) vec4 sharpMode(in vec2 uv) {
    vec2 uvAbs = abs(uv);
    float fringe = smoothstep(1.0, 0.0, uvAbs.x) * 2.0;
    float core = smoothstep(1.0, 0.0, uvAbs.x * 2.0);
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    vec4 resultColor = mix(fringeColor, vb_data.fragCoreColor, core);
    resultColor.w *= smoothstep(0.5, SHARP_EDGE_SMOOTH, uvAbs.y);
    return resultColor;
}

subroutine(flareStateDraw) vec4 smoothDiscMode(in vec2 uv) {
    vec2 coreUV = vec2(uv.x * statePackage[STATE_A].z, uv.y);
    vec2 discUV = vec2(uv.x, uv.y * statePackage[STATE_EXT].w * DISC_THICKNESS_SCALE);
    float fringe = fi(smoothstep(1.0, 0.0, length(coreUV)), smoothstep(1.0, 0.0, length(discUV)));
    float core = fi(smoothstep(1.0, 0.0, length(coreUV * 2.0)), smoothstep(1.0, 0.0, length(discUV * 2.0)));
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    return mix(fringeColor, vb_data.fragCoreColor, core);
}

subroutine(flareStateDraw) vec4 sharpDiscMode(in vec2 uv) {
    vec2 uvAbs = abs(uv);
    vec2 coreUV = vec2(uv.x * statePackage[STATE_A].z, uv.y);
    vec2 discUV = vec2(uvAbs.x, uvAbs.y * statePackage[STATE_EXT].w * SHARP_DISC_THICKNESS_SCALE);
    vec2 discMask = vec2(smoothstep(1.0, 0.0, discUV.x), smoothstep(1.0, 0.0, discUV.x * 2.0)) * smoothstep(0.5, SHARP_EDGE_SMOOTH, discUV.y);
    float fringe = fi(smoothstep(1.0, 0.0, length(coreUV)), discMask.x);
    float core = fi(smoothstep(1.0, 0.0, length(coreUV * 2.0)), discMask.y);
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    return mix(fringeColor, vb_data.fragCoreColor, core);
}

void main()
{
    vec2 uv = vb_data.fragUV;
    uv.x *= smoothstep(0.0, 1.0, fbm(uv.y * 0.5 + statePackage[STATE_B].w * 10.0 + vb_data.fragNoiseOffsetAlpha.x)) * statePackage[STATE_EXT].x + 1.0;;
    vec4 finalColor = flareState(uv);
    finalColor.w = min(vb_data.fragNoiseOffsetAlpha.y * finalColor.w, 1.0);
    bool invalidFrag = finalColor.w <= 0.0;
    if (invalidFrag) discard;
    fragColor = finalColor;
    finalColor.xyz *= statePackage[STATE_B].z;
    fragEmissive = finalColor;
    fragData = (invalidFrag) ? uvec4(uvec3(0u), 1u) : uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), finalColor.w) * 1023.0), dataBit, 0u);
}
""";
    }

    public final static class TextField {
        private TextField() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (location = 0) in vec4 uv; // uvBL, uvTR
layout (location = 1) in vec2 location; // x, y
layout (location = 2) in vec2 fontDrawData; // fontHeight, baselineHeight
layout (location = 3) in uint style; // invert(1+8) + italic(1+7) + underline(1+6) + strikeout(1+5) + channel(3+2) + handelIndex(2+0)
layout (location = 4) in vec4 color;
layout (location = 5) in vec2 size;
layout (location = 6) in vec2 edge; // x = YOffset
layout (location = 7) in float fill;

// topStyleUV = 1.0f - (float) fontData.getYOffset() / fontLineHeight);
// bottomStyleUV = 1.0f - (float) (fontData.getSize()[1] + fontData.getYOffset()) / fontLineHeight
// strikeoutUV = 1.0f - (fontLineHeight - lineHeight) * 0.5 / fontLineHeight
layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};
uniform vec4 globalColor[2];
uniform mat4 modelMatrix;

out VERT_GEOM_BLOCK {
	mat4 geomMatrix;
	vec4 geomUV;
	vec4 geomStyleUV; // topStyleUV, bottomStyleUV, strikeoutUV, underlineUV
	vec4 geomSize;
	flat vec2 geomFillBase;

	flat vec4 geomColor;
	flat uvec4 geomStyleState; // fuck intel
	flat uvec3 geomState; // cahnnel, texIndex, reserved
} vgb_data;

void main()
{
	vec4 styleUV = vec4(edge.x, size.y + edge.x, (fontDrawData.x - fontDrawData.y) * 0.5, fontDrawData.y) / fontDrawData.x;
	styleUV.xyz = 1.0 - styleUV.xyz;
	styleUV.zw *= vec2(0.8, 0.5);
	vgb_data.geomMatrix = gameViewport * modelMatrix;
	vgb_data.geomUV = uv + 0.5;
	vgb_data.geomStyleUV = styleUV;
	vgb_data.geomSize = vec4(size, edge);
	vgb_data.geomFillBase = vec2(fill, fontDrawData.y);
	vgb_data.geomColor = color * globalColor[0];
	vgb_data.geomStyleState = uvec4(style >> 8, style >> 7, style >> 6, style >> 5) & 1u;
	vgb_data.geomState = uvec3(style & 28u, style & 3u, ((uv.x < -500.0) ? 1u : 0u));
	gl_Position = vec4(location, 0.0, 1.0);
}
""";

        public final static String GEOM = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (points) in;
layout (triangle_strip, max_vertices = 16) out;

uniform float italicFactor;

in VERT_GEOM_BLOCK {
	mat4 geomMatrix;
	vec4 geomUV;
	vec4 geomStyleUV; // topStyleUV, bottomStyleUV, strikeoutUV, underlineUV
	vec4 geomSize;
	flat vec2 geomFillBase;

	flat vec4 geomColor;
	flat uvec4 geomStyleState; // fuck intel
	flat uvec3 geomState; // cahnnel, texIndex, reserved
} vgb_datas[];

out GEOM_FRAG_BLOCK {
	vec3 fragUV;
    flat vec2 fragStyleUV; // strikeoutUV, underlineUV
	flat uint fragIsEdge;

	flat vec4 fragColor;
	flat uvec3 fragStyleState; // fuck intel
	flat uvec3 fragState; // cahnnel, texIndex, reserved
} gfb_data;

void main()
{
	mat4 matrix = vgb_datas[0].geomMatrix;
	vec4 size = vgb_datas[0].geomSize;
	vec4 uv = vgb_datas[0].geomUV;
	vec4 styleUV = vgb_datas[0].geomStyleUV;
	vec2 fillBase = vgb_datas[0].geomFillBase;
	vec2 basePoint = gl_in[0].gl_Position.xy;
    float baselineAlignedEdge = italicFactor * fillBase.y;

	bool isItalic = vgb_datas[0].geomStyleState.y == 1u;
	vec4 upPoint = vec4(basePoint, 0.0, 1.0);
	if (isItalic) upPoint.x += italicFactor * (size.y + size.w) - baselineAlignedEdge;
	vec4 upPointM = matrix * upPoint;
	vec4 bottomPoint = vec4(basePoint.x, basePoint.y - size.y, 0.0, 1.0);
	if (isItalic) bottomPoint.x += italicFactor * size.w - baselineAlignedEdge;
	vec4 bottomPointM = matrix * bottomPoint;

	vec2 upEdgeUpPoint = vec2(basePoint.x, basePoint.y + size.z);
	vec2 bottomEdgeBottomPoint = vec2(basePoint.x, basePoint.y - size.y - size.w);
	if (isItalic) {
        upEdgeUpPoint.x += italicFactor * (size.y + size.z + size.w) - baselineAlignedEdge;
        bottomEdgeBottomPoint.x -= baselineAlignedEdge;
    }
	vec4 topEdgeL = vec4(upEdgeUpPoint, 0.0, 1.0);
	vec4 topEdgeLM = matrix * topEdgeL;
	vec4 topEdgeR = matrix * vec4(upEdgeUpPoint.x + size.x, upEdgeUpPoint.y, 0.0, 1.0);
	vec4 bottomEdgeL = vec4(bottomEdgeBottomPoint, 0.0, 1.0);
	vec4 bottomEdgeLM = matrix * bottomEdgeL;
	vec4 bottomEdgeR = matrix * vec4(bottomEdgeBottomPoint.x + size.x, bottomEdgeBottomPoint.y, 0.0, 1.0);

	bool setZeroB = vgb_datas[0].geomColor.w > 0.0;
	bool topValid = (size.z > 0.0) && setZeroB;
	bool bottomValid = (size.w > 0.0) && setZeroB;
	bool leftFill = (fillBase.x > 0.0) && setZeroB;
	vec4 fillVertex[] = vec4[](upPoint, upPointM, bottomPoint, bottomPointM);
	vec2 fillUv = vec2(styleUV.x, styleUV.y);
    if (leftFill) {
        if (topValid) {
            fillVertex[0] = topEdgeL;
            fillVertex[1] = topEdgeLM;
            fillUv.x = 1.0;
        }
        if (bottomValid) {
            fillVertex[2] = bottomEdgeL;
            fillVertex[3] = bottomEdgeLM;
            fillUv.y = 0.0;
        }
        fillVertex[0].x -= fillBase.x;
        fillVertex[2].x -= fillBase.x;
    }

	vec4 upPointRight = matrix * vec4(upPoint.x + size.x, upPoint.y, 0.0, 1.0);
	vec4 bottomPointRight = matrix * vec4(bottomPoint.x + size.x, bottomPoint.y, 0.0, 1.0);
	if (!setZeroB) upPointM = upPointRight = bottomPointM = bottomPointRight = vec4(vec3(-65536.0), 1.0);

	gfb_data.fragColor = vgb_datas[0].geomColor;
	gfb_data.fragStyleState = uvec3(vgb_datas[0].geomStyleState.x, vgb_datas[0].geomStyleState.z, vgb_datas[0].geomStyleState.w);
	gfb_data.fragState = vgb_datas[0].geomState;

	gfb_data.fragIsEdge = 0u;
    gfb_data.fragStyleUV = styleUV.zw;
	gfb_data.fragUV = vec3(uv.xy, styleUV.y);
	gl_Position = bottomPointM;
	EmitVertex();
	gfb_data.fragUV = vec3(uv.zy, styleUV.y);
	gl_Position = bottomPointRight;
	EmitVertex();
    gfb_data.fragUV = vec3(uv.xw, styleUV.x);
    gl_Position = upPointM;
    EmitVertex();
    gfb_data.fragUV = vec3(uv.zw, styleUV.x);
    gl_Position = upPointRight;
    EmitVertex();
	EndPrimitive();

	gfb_data.fragIsEdge = 1u;
	if (topValid) { // topEdge
		gfb_data.fragUV = vec3(styleUV.x);
		gl_Position = upPointM;
		EmitVertex();
		gl_Position = upPointRight;
		EmitVertex();
        gfb_data.fragUV = vec3(1.0);
        gl_Position = topEdgeLM;
        EmitVertex();
        gl_Position = topEdgeR;
        EmitVertex();
		EndPrimitive();
	}

	if (bottomValid) { // bottomEdge
		gfb_data.fragUV = vec3(0.0);
		gl_Position = bottomEdgeLM;
		EmitVertex();
		gl_Position = bottomEdgeR;
		EmitVertex();
		gfb_data.fragUV = vec3(styleUV.y);
		gl_Position = bottomPointM;
		EmitVertex();
		gl_Position = bottomPointRight;
		EmitVertex();
		EndPrimitive();
	}

	if (leftFill) {
        gfb_data.fragUV = vec3(fillUv.y);
        gl_Position = matrix * fillVertex[2];
        EmitVertex();
        gl_Position = fillVertex[3];
        EmitVertex();
		gfb_data.fragUV = vec3(fillUv.x);
		gl_Position = matrix * fillVertex[0];
		EmitVertex();
		gl_Position = fillVertex[1];
		EmitVertex();
		EndPrimitive();
	}
}
""";

        public final static String FRAG = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

uniform sampler2D fontMap[4];
uniform vec4 globalColor[2];
uniform uint dataBit;
uniform int blendBloom;

in GEOM_FRAG_BLOCK {
    vec3 fragUV;
    flat vec2 fragStyleUV; // strikeoutUV, underlineUV
    flat uint fragIsEdge;

    flat vec4 fragColor;
    flat uvec3 fragStyleState; // fuck intel
    flat uvec3 fragState; // cahnnel, texIndex, reserved
} gfb_data;

layout (location = 0) out vec4 fragColor; // draw to RGB8
layout (location = 1) out vec4 fragEmissive; // draw to RGB8
layout (location = 6) out uvec4 fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.

float getUnderline(in float uv, in float offset) {
    return smoothstep(0.04, 0.03, abs(uv - offset));
}

float getStrikeout(in float uv, in float offset) {
    return smoothstep(0.055, 0.04, abs(uv - offset));
}

void main()
{
    vec4 result = texture(fontMap[gfb_data.fragState.y], gfb_data.fragUV.xy);
    if (gfb_data.fragState.x == 4u) result = vec4(result.x);
    if (gfb_data.fragState.x == 8u) result = vec4(result.y);
    if (gfb_data.fragState.x == 12u) result = vec4(result.z);
    if (gfb_data.fragState.x == 16u) result = vec4(result.w);
    if (bool(gfb_data.fragIsEdge)) result = vec4(1.0, 1.0, 1.0, 0.0);
    if (bool(gfb_data.fragStyleState.y)) result = max(result, vec4(getUnderline(gfb_data.fragUV.z, gfb_data.fragStyleUV.y)));
    if (bool(gfb_data.fragStyleState.z)) result = max(result, vec4(getStrikeout(gfb_data.fragUV.z, gfb_data.fragStyleUV.x)));
    if (bool(gfb_data.fragStyleState.x)) result = vec4(1.0, 1.0, 1.0, 1.0 - result.w);
    result *= gfb_data.fragColor;
    if (bool(gfb_data.fragState.z)) result = vec4(gfb_data.fragColor.xyz, 0.0);
    result.w = min(result.w, 1.0);
    float cullAlpha = result.w;
    bool invalidFrag = result.w <= 0.0;
    if (invalidFrag) discard;
    fragColor = result;
    if (bool(blendBloom)) result.xyz *= globalColor[1].xyz; else result.xyz = globalColor[1].xyz;
    result.w = min(result.w * globalColor[1].w, 1.0);
    fragEmissive = result;
    fragData = (invalidFrag) ? uvec4(uvec3(0u), 1u) : uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), cullAlpha) * 1023.0), dataBit, 0u);
}
""";
    }

    public final static class Distortion {
        private Distortion() {}

        public final static String VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define INOUT_FACTOR 2
#define STATE 4

subroutine void instanceStateCompute(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam);
subroutine uniform instanceStateCompute instanceState;

layout (location = 0) in vec2 vertex;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 modelMatrix;
// vec4(sizeIn, powerIn, powerFull), vec4(sizeFull, powerOut, hardnessRing), vec4(sizeOut, fadeInFactor, fadeOutFactor)
// vec4(sizeInRatio, sizeFullRatio), vec4(sizeOutRatio, hardnessInner, globalTimerRaw), vec4(arcStart, arcEnd, innerCenter)
uniform vec4 statePackage[6];
uniform float screenScale;
uniform int instanceDataOffset;

out VERTEX_BLOCK {
	vec4 fragUVMask;
	vec4 fragUVScreen;
    float fragGlobalHardness;
} vb_data;

#include "BUtil_InstanceDataSSBO.h"

vec2 getUV(in vec2 location) {
	return (location - gameScreenBorder.xy) / gameScreenBorder.zw;
}

#define TIMER_IN 0u
#define TIMER_FULL 1u
#define TIMER_OUT 2u

uint getTimerState(in float alphaRaw) {
    if (alphaRaw > 2.0) return TIMER_IN;
    else if (alphaRaw > 1.0) return TIMER_FULL;
    else return TIMER_OUT;
}

float getMixFactor(in uint timer, in float alphaRaw) {
    float getPowValue = 1.0;
    if (timer == TIMER_IN) getPowValue = statePackage[INOUT_FACTOR].z;
    if (timer == TIMER_OUT) getPowValue = statePackage[INOUT_FACTOR].w;
    return (timer == TIMER_FULL) ? 1.0 : pow(decodeAlpha(alphaRaw), getPowValue);
}

subroutine(instanceStateCompute) void noneData(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
	model = modelMatrix;
	float check = statePackage[STATE].w;
	vec2 tmp = vec2(1.0);
	uint tmpU = TIMER_FULL;
	if (check > 2.0) {
		tmp = vec2(abs(3.0 - check), statePackage[INOUT_FACTOR].z);
		tmpU = TIMER_IN;
	}
	if (check < 1.0 && check > -500.0) {
		tmp = vec2(check, statePackage[INOUT_FACTOR].w);
		tmpU = TIMER_OUT;
	}
	timerState = tmpU;
	mixFactor = (tmpU == TIMER_FULL) ? 1.0 : pow(tmp.x, tmp.y);

    colorParam = vec4(1.0);
    emissiveParam = vec4(1.0);
}

subroutine(instanceStateCompute) void haveData2D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Dynamic2D data = dataDynamic2D[instanceDataOffset + gl_InstanceID];
    model = modelMatrix * fetchDynamic2DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE].w, data.timer.x);
	uint timer = getTimerState(pickTimer);
    timerState = timer;
	mixFactor = getMixFactor(timer, pickTimer);

    decodeDynamicColor(data.colorBits, pickTimer, colorParam, emissiveParam);
}

subroutine(instanceStateCompute) void haveFixedData2D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Fixed2D data = dataFixed2D[instanceDataOffset + gl_InstanceID];
    model = modelMatrix * fetchFixed2DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE].w, data.alpha_Facing_Location.x);
    uint timer = getTimerState(pickTimer);
    timerState = timer;
    mixFactor = getMixFactor(timer, pickTimer);

    decodeFixedColor(data.colorBits, pickTimer, colorParam, emissiveParam);
}

subroutine(instanceStateCompute) void haveData3D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Dynamic3D data = dataDynamic3D[instanceDataOffset + gl_InstanceID];
    model = modelMatrix * fetchDynamic3DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE].w, data.timer.x);
    uint timer = getTimerState(pickTimer);
    timerState = timer;
    mixFactor = getMixFactor(timer, pickTimer);

    decodeDynamicColor(data.colorBits, pickTimer, colorParam, emissiveParam);
}

subroutine(instanceStateCompute) void haveFixedData3D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Fixed3D data = dataFixed3D[instanceDataOffset + gl_InstanceID];
    model = modelMatrix * fetchFixed3DMatrix(data);

    float pickTimer = pickInstanceTimer(statePackage[STATE].w, data.alpha_LocationZ.x);
    uint timer = getTimerState(pickTimer);
    timerState = timer;
    mixFactor = getMixFactor(timer, pickTimer);

    decodeFixedColor(data.colorBits, pickTimer, colorParam, emissiveParam);
}

void main() {
	mat4 currentMatrix;
    // color = innerInRatioScale_innerFullRatioScale_innerOutRatioScale_globalPower
    // emissive = sizeInScale_sizeFullScale_sizeOutScale_globalHardness
    vec4 colorParam, emissiveParam;
	uint mixStateValue;
	float mixFactorValue;
	instanceState(currentMatrix, mixStateValue, mixFactorValue, colorParam, emissiveParam);

	vec4 size = vec4(statePackage[1].xy * emissiveParam.y, statePackage[3].zw * colorParam.y), sizeMix = size;
	float power = statePackage[0].w, powerMix = power;
	if (mixStateValue == TIMER_IN) {
		sizeMix = vec4(statePackage[0].xy * emissiveParam.x, statePackage[3].xy * colorParam.x);
		powerMix = statePackage[0].z;
	}
	if (mixStateValue == TIMER_OUT) {
		sizeMix = vec4(statePackage[2].xy * emissiveParam.z, statePackage[4].xy * colorParam.z);
		powerMix = statePackage[1].z;
	}
    if (mixStateValue != TIMER_FULL) {
        size = mix(sizeMix, size, mixFactorValue);
        power = mix(powerMix, power, mixFactorValue);
    }
    power *= colorParam.w;

	vec4 location = currentMatrix * vec4(vertex * size.xy, 0.0, 1.0);
	vec2 locationSize = abs(location.xy - currentMatrix[3].xy);
	vec4 currPos = gameViewport * location;
	if (power == 0.0) currPos.xyz = vec3(-65536.0);
	gl_Position = currPos;
	vb_data.fragUVMask = vec4(vertex, vertex / size.zw - statePackage[5].zw);
	vb_data.fragUVScreen = vec4(getUV(location.xy) * screenScale, locationSize.xy / gameScreenBorder.zw * vertex * 0.5 * screenScale * power);
	vb_data.fragGlobalHardness = emissiveParam.w;
}
""";

        public final static String FRAG = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define HARDNESS_RING 1
#define HARDNESS_INNER 4

in VERTEX_BLOCK {
    vec4 fragUVMask;
    vec4 fragUVScreen;
    float fragGlobalHardness;
} vb_data;

// vec4(sizeIn, powerIn, powerFull), vec4(sizeFull, powerOut, hardnessRing), vec4(sizeOut, fadeInFactor, fadeOutFactor)
// vec4(sizeInRatio, sizeFullRatio), vec4(sizeOutRatio, hardnessInner, globalTimerRaw), vec4(arcStart, arcEnd, innerCenter)
uniform vec4 statePackage[6];
// screen
layout(binding = 0) uniform sampler2D texturePackage;

out vec4 fragColor;

float inverseLerp(in float edgeL, in float edgeR, in float value) {
    return (value - edgeL) / (edgeR - edgeL);
}

void main() {
    float ring = length(vb_data.fragUVMask.xy), inner = length(vb_data.fragUVMask.zw);
    float arc = (statePackage[5].x > -1.0) ? min(inverseLerp(statePackage[5].y, statePackage[5].x, vb_data.fragUVMask.x / ring), 1.0) : 1.0;
    float ringHardness = statePackage[HARDNESS_RING].w * vb_data.fragGlobalHardness, innerHardness = statePackage[HARDNESS_INNER].z * vb_data.fragGlobalHardness;
    
    ring = ringHardness >= 1.0 ? step(ring, 1.0) : smoothstep(1.0, ringHardness, ring);
    inner = innerHardness >= 1.0 ? step(1.0, inner) : smoothstep(innerHardness, 1.0, inner);
    float mask = ring * inner * arc;
    if (mask <= 0.0) discard;
    vec4 result = texture(texturePackage, vb_data.fragUVScreen.xy - (vb_data.fragUVScreen.zw * mask));
    result.w = 1.0;
    fragColor = result;
}

""";
    }

    public final static class InstanceMatrix {
        private InstanceMatrix() {}

        public final static String INSTANCE_2D = """
#version 430

precision OVERWRITE_PRECISION float;

#define WORKGROUP_SIZE WORKGROUP_SIZE_VALUE

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 4) in;

// 80 byte
struct Dynamic2D { // binding 4
    vec4 q22_q23_facing_TurnRate;
    vec4 location_Scale;
    vec4 velocity_ScaleRate;
    vec4 timer;
    uvec4 colorBits;
};

layout(std430, binding = 4) restrict buffer BUtilInstanceData_Dynamic2D
{
    Dynamic2D dataDynamic2D[];
};

uniform float amount;
uniform ivec2 instanceRange; // offset, edge

float computeTimerX(in vec4 timer) {
    bool finishSet = timer.x < -2000.0;
    vec2 timerTmp = finishSet ? vec2(-2048.0) : vec2(timer.w, 0.0);
    if (timer.x > 2.0) {
        timerTmp = vec2(timer.y, 2.0);
    } else if (timer.x > 1.0) {
        timerTmp = vec2(timer.z, 1.0);
    }
    float result = timerTmp.x > -500.0 ? fma(timerTmp.x, -amount, timer.x) : timerTmp.y;
    if (timer.x <= 0.0 && timer.x > -2000.0) {
        result = -2048.0;
    } else if (finishSet) result = -5120.0;
    return result;
}

void main() {
	uint globalIndex = (gl_WorkGroupID.z * gl_NumWorkGroups.x * gl_NumWorkGroups.y + gl_WorkGroupID.y * gl_NumWorkGroups.x + gl_WorkGroupID.x) * WORKGROUP_SIZE;
	int indexNow = int(gl_LocalInvocationIndex + globalIndex) + instanceRange.x;
	if (indexNow >= instanceRange.y) return;

    Dynamic2D instanceData = dataDynamic2D[indexNow];
    vec4 timer = instanceData.timer;
    if (timer.x < -5000.0) return;

    dataDynamic2D[indexNow].timer.x = computeTimerX(timer);

    float pryFacing = radians(instanceData.q22_q23_facing_TurnRate.z * 0.5);
    float pryCos = cos(pryFacing);
    float prySin = sin(pryFacing);

    float dqz = prySin + prySin;

    dataDynamic2D[indexNow].q22_q23_facing_TurnRate.xyz = vec3(dqz * prySin, dqz * pryCos, mod(fma(instanceData.q22_q23_facing_TurnRate.w, amount, instanceData.q22_q23_facing_TurnRate.z + 360.0), 360.0));
    dataDynamic2D[indexNow].location_Scale = fma(instanceData.velocity_ScaleRate, vec4(amount), instanceData.location_Scale);
}
""";

        public final static String INSTANCE_3D = """
#version 430

precision OVERWRITE_PRECISION float;

#define WORKGROUP_SIZE WORKGROUP_SIZE_VALUE

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 4) in;

// 144 byte
struct Dynamic3D { // binding 6
    vec4 m00_m01_m02_m21;
    vec4 m10_m11_m12_m22;
    vec4 m20_rotateRate;

    vec4 location_velocityX;
    vec4 rotate_velocityY;
    vec4 scale_velocityZ;
    vec4 scaleRate_rd;

    vec4 timer;
    uvec4 colorBits;
};

layout(std430, binding = 6) restrict buffer BUtilInstanceData_Dynamic3D
{
    Dynamic3D dataDynamic3D[];
};

uniform float amount;
uniform ivec2 instanceRange; // offset, edge

float computeTimerX(in vec4 timer) {
    bool finishSet = timer.x < -2000.0;
    vec2 timerTmp = finishSet ? vec2(-2048.0) : vec2(timer.w, 0.0);
    if (timer.x > 2.0) {
        timerTmp = vec2(timer.y, 2.0);
    } else if (timer.x > 1.0) {
        timerTmp = vec2(timer.z, 1.0);
    }
    float result = timerTmp.x > -500.0 ? fma(timerTmp.x, -amount, timer.x) : timerTmp.y;
    if (timer.x <= 0.0 && timer.x > -2000.0) {
        result = -2048.0;
    } else if (finishSet) result = -5120.0;
    return result;
}

void main() {
	uint globalIndex = (gl_WorkGroupID.z * gl_NumWorkGroups.x * gl_NumWorkGroups.y + gl_WorkGroupID.y * gl_NumWorkGroups.x + gl_WorkGroupID.x) * WORKGROUP_SIZE;
	int indexNow = int(gl_LocalInvocationIndex + globalIndex) + instanceRange.x;
	if (indexNow >= instanceRange.y) return;

    Dynamic3D instanceData = dataDynamic3D[indexNow];
    vec4 timer = instanceData.timer;
    if (timer.x < -5000.0) return;

    dataDynamic3D[indexNow].timer.x = computeTimerX(timer);

    // dLoc, dRotate, dScale
    mat3 dynamic = mat3(
    instanceData.location_velocityX.w, instanceData.rotate_velocityY.w, instanceData.scale_velocityZ.w,
    instanceData.m20_rotateRate.yzw,
    instanceData.scaleRate_rd.xyz
    );
    dynamic *= amount;

    vec3 rotate = instanceData.rotate_velocityY.xyz;
    vec3 scale = instanceData.scale_velocityZ.xyz;

    vec3 pryRotate = radians(rotate * 0.5);
    vec3 pryCos = cos(pryRotate);
    vec3 prySin = sin(pryRotate);

    float wq = pryCos.x * pryCos.y * pryCos.z - prySin.x * prySin.y * prySin.z;
    float xq = prySin.x * pryCos.y * pryCos.z - pryCos.x * prySin.y * prySin.z;
    float yq = pryCos.x * prySin.y * pryCos.z + prySin.x * pryCos.y * prySin.z;
    float zq = pryCos.x * pryCos.y * prySin.z + prySin.x * prySin.y * pryCos.z;

    float dqx = xq + xq;
    float dqy = yq + yq;
    float dqz = zq + zq;
    float q00 = dqx * xq;
    float q11 = dqy * yq;
    float q22 = dqz * zq;
    float q01 = dqx * yq;
    float q02 = dqx * zq;
    float q03 = dqx * wq;
    float q12 = dqy * zq;
    float q13 = dqy * wq;
    float q23 = dqz * wq;

    dataDynamic3D[indexNow].m00_m01_m02_m21 = vec4(scale.x - (q11 + q22) * scale.x, (q01 + q23) * scale.x, (q02 - q13) * scale.x, (q12 - q03) * scale.z);
    dataDynamic3D[indexNow].m10_m11_m12_m22 = vec4((q01 - q23) * scale.y, scale.y - (q22 + q00) * scale.y, (q12 + q03) * scale.y, scale.z - (q11 + q00) * scale.z);
    dataDynamic3D[indexNow].m20_rotateRate.x = (q02 + q13) * scale.z;

    dataDynamic3D[indexNow].location_velocityX.xyz += dynamic[0];
    dataDynamic3D[indexNow].rotate_velocityY.xyz = mod(rotate + dynamic[1] + 360.0, 360.0);
    dataDynamic3D[indexNow].scale_velocityZ.xyz += dynamic[2];
}
""";

        public final static String STRUCT = """
// 80 byte
struct Dynamic2D { // binding 4
    vec4 q22_q23_facing_TurnRate;
    vec4 location_Scale;
    vec4 velocity_ScaleRate;
    vec4 timer;
    uvec4 colorBits;
};

// 32 byte
struct Fixed2D { // binding 5
    vec4 alpha_Facing_Location;
    vec2 scale;
    uvec2 colorBits;
};

// 144 byte
struct Dynamic3D { // binding 6
    vec4 m00_m01_m02_m21;
    vec4 m10_m11_m12_m22;
    vec4 m20_rotateRate;

    vec4 location_velocityX;
    vec4 rotate_velocityY;
    vec4 scale_velocityZ;
    vec4 scaleRate_rd;

    vec4 timer;
    uvec4 colorBits;
};

// 48 byte
struct Fixed3D { // binding 7
    vec4 rotate_LocationX;
    vec4 scale_LocationY;
    vec2 alpha_LocationZ;
    uvec2 colorBits;
};

layout(std430, binding = 4) restrict readonly buffer BUtilInstanceData_Dynamic2D
{
    Dynamic2D dataDynamic2D[];
};

layout(std430, binding = 5) restrict readonly buffer BUtilInstanceData_Fixed2D
{
    Fixed2D dataFixed2D[];
};

layout(std430, binding = 6) restrict readonly buffer BUtilInstanceData_Dynamic3D
{
    Dynamic3D dataDynamic3D[];
};

layout(std430, binding = 7) restrict readonly buffer BUtilInstanceData_Fixed3D
{
    Fixed3D dataFixed3D[];
};

float pickInstanceTimer(in float _in_override, in float _in_instance) {
    return (_in_override >= 0.0) ? _in_override : _in_instance;
}

float decodeAlpha(in float _in_alpha) {
    if (_in_alpha > 2.0) return abs(3.0 - _in_alpha);
    else if (_in_alpha > 1.0) return 1.0;
    else return max(_in_alpha, 0.0);
}

void decodeFixedColor(in uvec2 _in_bits, in float _in_raw_alpha, out vec4 _out_color, out vec4 _out_emissive) {
    mat2x4 colorMat = mat2x4(vec4(uvec4(_in_bits >> 24u, _in_bits >> 16u) & 0xFFu), vec4(uvec4(_in_bits >> 8u, _in_bits) & 0xFFu)) * 0.0039215;
    
    float alpha = decodeAlpha(_in_raw_alpha);
    _out_color = vec4(colorMat[0].x, colorMat[0].z, colorMat[1].x, colorMat[1].z * alpha);
    _out_emissive = vec4(colorMat[0].y, colorMat[0].w, colorMat[1].y, colorMat[1].w * alpha);
}

void decodeDynamicColor(in uvec4 _in_bits, in float _in_raw_alpha, out vec4 _out_color, out vec4 _out_emissive) {
    vec4 lowColor = vec4((_in_bits >> 24u) & 0xFFu);
    vec4 highColor = vec4((_in_bits >> 16u) & 0xFFu);
    vec4 lowEmissive = vec4((_in_bits >> 8u) & 0xFFu);
    vec4 highEmissive = vec4(_in_bits & 0xFFu);
    mat4 colorMat = mat4(lowColor, highColor, lowEmissive, highEmissive) * 0.0039215;
    
    float alpha = decodeAlpha(_in_raw_alpha);
    _out_color = mix(colorMat[0], colorMat[1], alpha);
    _out_emissive = mix(colorMat[2], colorMat[3], alpha);
    _out_color.w *= alpha;
    _out_emissive.w *= alpha;
}

mat4 fetchDynamic2DMatrix(in Dynamic2D data) {
    return mat4(
    data.location_Scale.z - data.q22_q23_facing_TurnRate.x * data.location_Scale.z, data.q22_q23_facing_TurnRate.y * data.location_Scale.z, 0.0, 0.0,
        -data.q22_q23_facing_TurnRate.y * data.location_Scale.w, data.location_Scale.w - data.q22_q23_facing_TurnRate.x * data.location_Scale.w, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        data.location_Scale.xy, 0.0, 1.0
    );
}

mat4 fetchDynamic3DMatrix(in Dynamic3D data) {
    return mat4(
        data.m00_m01_m02_m21.xyz, 0.0,
        data.m10_m11_m12_m22.xyz, 0.0,
        data.m20_rotateRate.x, data.m00_m01_m02_m21.w, data.m10_m11_m12_m22.w, 0.0,
        data.location_velocityX.xyz, 1.0
    );
}

mat4 fetchFixed2DMatrix(in Fixed2D data) {
    float pryFacing = radians(data.alpha_Facing_Location.y * 0.5);
    float pryCos = cos(pryFacing);
    float prySin = sin(pryFacing);
    float dqz = prySin + prySin;
    float q22 = dqz * prySin;
    float q23 = dqz * pryCos;
    return mat4(
    data.scale.x - q22 * data.scale.x, q23 * data.scale.x, 0.0, 0.0,
    -q23 * data.scale.y, data.scale.y - q22 * data.scale.y, 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    data.alpha_Facing_Location.zw, 0.0, 1.0
    );
}

mat4 fetchFixed3DMatrix(in Fixed3D data) {
    vec3 pryRotate = radians(data.rotate_LocationX.xyz * 0.5);
    vec3 pryCos = cos(pryRotate);
    vec3 prySin = sin(pryRotate);
    float wq = pryCos.x * pryCos.y * pryCos.z - prySin.x * prySin.y * prySin.z;
    float xq = prySin.x * pryCos.y * pryCos.z - pryCos.x * prySin.y * prySin.z;
    float yq = pryCos.x * prySin.y * pryCos.z + prySin.x * pryCos.y * prySin.z;
    float zq = pryCos.x * pryCos.y * prySin.z + prySin.x * prySin.y * pryCos.z;
    float dqx = xq + xq;
    float dqy = yq + yq;
    float dqz = zq + zq;
    float q00 = dqx * xq;
    float q11 = dqy * yq;
    float q22 = dqz * zq;
    float q01 = dqx * yq;
    float q02 = dqx * zq;
    float q03 = dqx * wq;
    float q12 = dqy * zq;
    float q13 = dqy * wq;
    float q23 = dqz * wq;
    return mat4(
        data.scale_LocationY.x - (q11 + q22) * data.scale_LocationY.x, (q01 + q23) * data.scale_LocationY.x, (q02 - q13) * data.scale_LocationY.x, 0.0,
        (q01 - q23) * data.scale_LocationY.y, data.scale_LocationY.y - (q22 + q00) * data.scale_LocationY.y, (q12 + q03) * data.scale_LocationY.y, 0.0,
        (q02 + q13) * data.scale_LocationY.z, (q12 - q03) * data.scale_LocationY.z, data.scale_LocationY.z - (q11 + q00) * data.scale_LocationY.z, 0.0,
        data.rotate_LocationX.w, data.scale_LocationY.w, data.alpha_LocationZ.y, 1.0
    );
}
""";
    }

    public final static class Bloom {
        private Bloom() {}

        public final static String COMP = """
#version 430

precision highp float;
precision highp int;

#define RADIUS_SCALE OVERWRITE_RADIUS_SCALE

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine vec4 sampleMode(in ivec2 coord);
subroutine uniform sampleMode sampleModeState;

layout (binding = 0) uniform sampler2D texIn;
layout (binding = 1) uniform sampler2D texHL;
layout (binding = 0, rgb10_a2) writeonly uniform image2D texOut;
layout (binding = 1, rgba8) writeonly uniform image2D texOutFirst;

uniform ivec2 size;
uniform vec4 targetUVStepDiv;
uniform int initPass;

const vec2 downCoord[] = vec2[4](
vec2(-1.0, -1.0) * RADIUS_SCALE, vec2(1.0, -1.0) * RADIUS_SCALE, vec2(-1.0, 1.0) * RADIUS_SCALE, vec2(1.0, 1.0) * RADIUS_SCALE
);

const vec2 upCoordA[] = vec2[4](
vec2(0.0, -2.0) * RADIUS_SCALE, vec2(-2.0, 0.0) * RADIUS_SCALE, vec2(2.0, 0.0) * RADIUS_SCALE, vec2(0.0, 2.0) * RADIUS_SCALE
);

const vec2 upCoordB[] = vec2[4](
vec2(-1.0, -1.0) * RADIUS_SCALE, vec2(1.0, -1.0) * RADIUS_SCALE, vec2(-1.0, 1.0) * RADIUS_SCALE, vec2(1.0, 1.0) * RADIUS_SCALE
);

subroutine(sampleMode) vec4 sampleInit(in ivec2 coord) {
	return max(texelFetch(texIn, coord, 0), texelFetch(texHL, coord, 0));
}

subroutine(sampleMode) vec4 downSampleFirst(in ivec2 coord) {
	vec2 uv = vec2(coord) * targetUVStepDiv.zw;
	vec3 result = texture(texIn, uv).xyz, resultA, weight;
	weight = result * 0.4;
	weight = fma(texture(texIn, fma(targetUVStepDiv.xy, vec2(-1.0, -1.0), uv)).xyz, vec3(0.15), weight);
	weight = fma(texture(texIn, fma(targetUVStepDiv.xy, vec2(1.0, -1.0), uv)).xyz, vec3(0.15), weight);
	weight = fma(texture(texIn, fma(targetUVStepDiv.xy, vec2(-1.0, 1.0), uv)).xyz, vec3(0.15), weight);
	weight = fma(texture(texIn, fma(targetUVStepDiv.xy, vec2(1.0, 1.0), uv)).xyz, vec3(0.15), weight);

	resultA = texture(texIn, fma(targetUVStepDiv.xy, downCoord[0], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, downCoord[1], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, downCoord[2], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, downCoord[3], uv)).xyz;

	resultA *= 0.125;
	result = fma(result, vec3(0.5), resultA);
	return vec4(result / (0.6666667 + dot(weight, vec3(0.2126729, 0.7151522, 0.0721750))), 1.0);
}

subroutine(sampleMode) vec4 downSample(in ivec2 coord) {
	vec2 uv = vec2(coord) * targetUVStepDiv.zw;
	vec3 result = texture(texIn, uv).xyz, resultA;

	resultA = texture(texIn, fma(targetUVStepDiv.xy, downCoord[0], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, downCoord[1], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, downCoord[2], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, downCoord[3], uv)).xyz;

	resultA *= 0.125;
	return vec4(fma(result, vec3(0.5), resultA), 1.0);
}

subroutine(sampleMode) vec4 upSample(in ivec2 coord) {
	vec2 uv = vec2(coord) * targetUVStepDiv.zw;
	vec3 result = texelFetch(texHL, coord, 0).xyz, resultA, resultB;

	resultA = texture(texIn, fma(targetUVStepDiv.xy, upCoordA[0], uv)).xyz;
	resultB = texture(texIn, fma(targetUVStepDiv.xy, upCoordB[0], uv)).xyz;
	resultB += texture(texIn, fma(targetUVStepDiv.xy, upCoordB[1], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, upCoordA[1], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, upCoordA[2], uv)).xyz;
	resultB += texture(texIn, fma(targetUVStepDiv.xy, upCoordB[2], uv)).xyz;
	resultB += texture(texIn, fma(targetUVStepDiv.xy, upCoordB[3], uv)).xyz;
	resultA += texture(texIn, fma(targetUVStepDiv.xy, upCoordA[3], uv)).xyz;

	resultB *= 0.1666667;
	result += fma(resultA, vec3(0.0833333), resultB);
	return vec4(result, 1.0);
}

void main() {
	ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
	if (any(greaterThanEqual(coord, size))) return;
	vec4 result = sampleModeState(coord);
	if (bool(initPass)) imageStore(texOutFirst, coord, result); else imageStore(texOut, coord, result);
}
""";
    }

    public final static class FXAA {
        private FXAA() {}

        public final static String CONSOLE = """
#version 420

precision highp float;

#define SCREENSTEPX OVERWRITE_SCREEN_X
#define SCREENSTEPY OVERWRITE_SCREEN_Y
#define FXAA_SHARPNESS 0.5
#define FXAA_ABSOLUTE_LUMA_THRESHOLD 0.05
#define FXAA_RELATIVE_LUMA_THRESHOLD 0.1
#define OFFSETSIZE 5
#define NW 0
#define NE 1
#define C 2
#define SW 3
#define SE 4
#define GLOW_TEX 2

subroutine float sampleMethod(in vec2 sampleUV);
subroutine uniform sampleMethod sampleMethodState;
subroutine vec4 displayMethod(in vec4 finalColor);
subroutine uniform displayMethod displayMethodState;

smooth in vec2 fragUV;

layout(binding = 0) uniform sampler2D screen;
layout(binding = 1) uniform sampler2D fragData;

out vec4 fragColor;

subroutine(sampleMethod) float fromRaw(in vec2 sampleUV) {
    return dot(texture(screen, sampleUV).xyz, vec3(0.2126729, 0.7151522, 0.0721750));
}

subroutine(sampleMethod) float fromDepth(in vec2 sampleUV) {
    return texture(fragData, sampleUV).y;
}

subroutine(displayMethod) vec4 commonDisplay(in vec4 finalColor) {
    return finalColor;
}

subroutine(displayMethod) vec4 edgeDisplay(in vec4 finalColor) {
    return vec4(0.0, 1.0, 0.0, 1.0);
}

const vec2 SCREEN_OFFSET[] = vec2[](vec2(-0.5, 0.5), vec2(0.5, 0.5), vec2( 0.0, 0.0), vec2(-0.5,-0.5), vec2(0.5,-0.5));

void main()
{
    vec2 screenStep = vec2(SCREENSTEPX, SCREENSTEPY);
    vec4 fragRaw = texture(screen, fragUV);

    float luma[OFFSETSIZE];
    for (int i = 0; i < OFFSETSIZE; i++) {
        vec2 eachUV = vec2(fragUV + screenStep * SCREEN_OFFSET[i]);
        luma[i] = sampleMethodState(eachUV);
    }

    float lumaMax = max(luma[NW], max(max(luma[NE], luma[C]), max(luma[SW], luma[SE])));
    float lumaMin = min(luma[NW], min(min(luma[NE], luma[C]), min(luma[SW], luma[SE])));
    float lumaContrast = lumaMax - lumaMin;
    if(lumaContrast < max(FXAA_ABSOLUTE_LUMA_THRESHOLD, lumaMax * FXAA_RELATIVE_LUMA_THRESHOLD)) {
        fragColor = fragRaw;
        return;
    }

    vec2 dir = vec2(0.0);
    dir.x = (luma[SW] + luma[SE]) - (luma[NW] + luma[NE]);
    dir.y = (luma[NE] + luma[SW]) - (luma[NE] + luma[SE]);
    dir = normalize(dir);
    vec4 P1 = texture(screen, fragUV + (dir * screenStep * 0.5));
    vec4 N1 = texture(screen, fragUV - (dir * screenStep * 0.5));

    float dirAbsMinTimesC = min(abs(dir.x), abs(dir.y)) * FXAA_SHARPNESS;
    vec2 minDir = clamp(dir / dirAbsMinTimesC, -1.0, 1.0) * 0.5;
    vec4 P2 = texture(screen, fragUV + (minDir * screenStep));
    vec4 N2 = texture(screen, fragUV - (minDir * screenStep));

    vec4 S1 = P1 + N1;
    vec4 S2 = (P2 + N2 + S1) / 4.0;
    float brightness = dot(S2.xyz, vec3(0.2126729, 0.7151522, 0.0721750));
    if (brightness < lumaMin || brightness > lumaMax) {
        S2.xyz = S1.xyz * 0.5;
    }
    fragColor = displayMethodState(S2);
}
""";

        public final static String QUALITY = """
#version 420

precision highp float;

#define SCREENSTEPX OVERWRITE_SCREEN_X
#define SCREENSTEPY OVERWRITE_SCREEN_Y
#define FXAA_SHARPNESS 0.5
#define FXAA_ABSOLUTE_LUMA_THRESHOLD 0.1
#define FXAA_RELATIVE_LUMA_THRESHOLD 0.15
#define OFFSETSIZE 9
#define EDGESIZE 5
#define NW 0
#define N 1
#define NE 2
#define W 3
#define C 4
#define E 5
#define SW 6
#define S 7
#define SE 8

subroutine float sampleMethod(in vec2 sampleUV);
subroutine uniform sampleMethod sampleMethodState;
subroutine vec4 displayMethod(in vec2 finalUV);
subroutine uniform displayMethod displayMethodState;

smooth in vec2 fragUV;

layout(binding = 0) uniform sampler2D screen;
layout(binding = 1) uniform sampler2D fragData;

out vec4 fragColor;

subroutine(sampleMethod) float fromRaw(in vec2 sampleUV) {
    return dot(texture(screen, sampleUV).xyz, vec3(0.2126729, 0.7151522, 0.0721750));
}

subroutine(sampleMethod) float fromDepth(in vec2 sampleUV) {
    return texture(fragData, sampleUV).y;
}

subroutine(displayMethod) vec4 commonDisplay(in vec2 finalUV) {
    return texture(screen, finalUV);
}

subroutine(displayMethod) vec4 edgeDisplay(in vec2 finalUV) {
    return vec4(0.0, 1.0, 0.0, 1.0);
}

const vec2 SCREEN_OFFSET[] = vec2[](vec2(-1.0, 1.0), vec2(0.0, 1.0), vec2(1.0, 1.0), vec2(-1.0, 0.0), vec2(0.0, 0.0), vec2(1.0, 0.0), vec2(-1.0,-1.0), vec2(0.0,-1.0), vec2(1.0,-1.0));
const float EDGE_STEP[] = float[](1.0, 1.5, 2.0, 2.0, 8.0);

void main()
{
    vec2 screenStepVec = vec2(SCREENSTEPX, SCREENSTEPY);
    vec4 fragRaw = texture(screen, fragUV);

    float luma[OFFSETSIZE];
    for (int i = 0; i < OFFSETSIZE; i++) {
        vec2 eachUV = vec2(fragUV + screenStepVec * SCREEN_OFFSET[i]);
        luma[i] = sampleMethodState(eachUV);
    }

    float lumaMax = max(luma[N], max(max(luma[W], luma[C]), max(luma[E], luma[S])));
    float lumaMin = min(luma[N], min(min(luma[W], luma[C]), min(luma[E], luma[S])));
    float lumaContrast = lumaMax - lumaMin;
    if (lumaContrast < max(FXAA_ABSOLUTE_LUMA_THRESHOLD, lumaMax * FXAA_RELATIVE_LUMA_THRESHOLD)) {
        fragColor = fragRaw;
        return;
    }

    float lumaHorzC = luma[N] + luma[S];
    float lumaVertC = luma[W] + luma[E];
    float lumaHorzTR = luma[NE] + luma[SE];
    float lumaVertTR = luma[NW] + luma[NE];
    float lumaHorzBL = luma[NW] + luma[SW];
    float lumaVertBL = luma[SW] + luma[SE];
    float edgeHorz = abs((-2.0 * luma[W])+ lumaHorzBL) + (abs((-2.0 * luma[C]) + lumaHorzC) * 2.0) + abs((-2.0 * luma[E]) + lumaHorzTR);
    float edgeVert = abs((-2.0 * luma[S]) + lumaVertBL) + (abs((-2.0 * luma[C]) + lumaVertC) * 2.0) + abs((-2.0 * luma[N]) + lumaVertTR);
    bool isHorz = edgeHorz >= edgeVert;

    float screenStep = isHorz ? screenStepVec.y : screenStepVec.x;
    float luma1 = isHorz ? luma[S] : luma[W];
    float luma2 = isHorz ? luma[N] : luma[E];
    float gradient1 = luma1 - luma[C];
    float gradient2 = luma2 - luma[C];
    float gradientScaled = 0.25 * max(abs(gradient1), abs(gradient2));

    bool is1Steepest = abs(gradient1) >= abs(gradient2);
    float lumaLocalAverage = is1Steepest ? (0.5 * (luma1 + luma[C])) : (0.5 * (luma2 + luma[C]));
    if (is1Steepest) {
        screenStep = -screenStep;
    }

    vec2 startN = isHorz ? vec2(fragUV.x, fragUV.y + screenStep * 0.5) : vec2(fragUV.x + screenStep * 0.5, fragUV.y);
    vec2 uvOffsetT = isHorz ? vec2(screenStepVec.x, 0.0) : vec2(0.0, screenStepVec.y);

    vec2 uvL = startN - uvOffsetT;
    vec2 uvR = startN + uvOffsetT;
    float lumaEndL = sampleMethodState(uvL) - lumaLocalAverage;
    float lumaEndR = sampleMethodState(uvR) - lumaLocalAverage;

    bool reachedL = abs(lumaEndL) >= gradientScaled;
    bool reachedR = abs(lumaEndR) >= gradientScaled;
    bool reachedLR = reachedL && reachedR;

    if (!reachedL){
        uvL -= uvOffsetT * EDGE_STEP[0];
    }
    if (!reachedR){
        uvR += uvOffsetT * EDGE_STEP[0];
    }

    if (!reachedLR) {
        for (int i = 1; i < EDGESIZE; i++) {
            if(!reachedL) lumaEndL = sampleMethodState(uvL) - lumaLocalAverage;
            if(!reachedR) lumaEndR = sampleMethodState(uvR) - lumaLocalAverage;
            reachedL = abs(lumaEndL) >= gradientScaled;
            reachedR = abs(lumaEndR) >= gradientScaled;
            reachedLR = reachedL && reachedR;

            if(!reachedL) uvL -= uvOffsetT * EDGE_STEP[i];
            if(!reachedR) uvR += uvOffsetT * EDGE_STEP[i];
            if(reachedLR) break;
        }
    }

    float nearestUVL = isHorz ? (fragUV.x - uvL.x) : (fragUV.y - uvL.y);
    float nearestUVR = isHorz ? (uvR.x - fragUV.x) : (uvR.y - fragUV.y);

    bool isNearestL = nearestUVL <= nearestUVR;
    float nearestUV = min(nearestUVL, nearestUVR);
    float edgeLength = nearestUVL + nearestUVR;

    bool isLumaCenterSmaller = luma[C] < lumaLocalAverage;
    bool correctVariationL = (lumaEndL < 0.0) != isLumaCenterSmaller;
    bool correctVariationR = (lumaEndR < 0.0) != isLumaCenterSmaller;
    bool correctVariation = isNearestL ? correctVariationL : correctVariationR;
    float finalOffset = correctVariation ? (-nearestUV / edgeLength + 0.5) : 0.0;

    vec2 finalUV = isHorz ? vec2(fragUV.x, finalOffset * screenStep + fragUV.y) : vec2(finalOffset * screenStep + fragUV.x, fragUV.y);
    fragColor = displayMethodState(finalUV);
}
""";
    }

    public final static class SDF {
        public final static String INIT = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine float sampleMethod(in ivec2 sampleCoord);
subroutine uniform sampleMethod sampleMethodState;

layout(binding = 0) uniform sampler2D checkMap;
layout(binding = 0, rgba16i) uniform writeonly iimage2D resultMap;

uniform ivec4 sizeState; // ivec2(checkMapSize), ivec2(resultMapSize)
uniform ivec2 border;
uniform float threshold;

subroutine(sampleMethod) float fromRed(in ivec2 sampleCoord) {
    return texelFetch(checkMap, sampleCoord, 0).x;
}

subroutine(sampleMethod) float fromGreen(in ivec2 sampleCoord) {
    return texelFetch(checkMap, sampleCoord, 0).y;
}

subroutine(sampleMethod) float fromBlue(in ivec2 sampleCoord) {
    return texelFetch(checkMap, sampleCoord, 0).z;
}

subroutine(sampleMethod) float fromAlpha(in ivec2 sampleCoord) {
    return texelFetch(checkMap, sampleCoord, 0).w;
}

subroutine(sampleMethod) float fromRGB(in ivec2 sampleCoord) {
    return dot(texelFetch(checkMap, sampleCoord, 0).xyz, vec3(LINEAR_VALUES));
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, sizeState.zw))) return;
    ivec2 coordStore = ivec2(coord + 1);
    ivec2 checkCoord = coord - border;
    ivec4 result;
    if (all(bvec4(greaterThanEqual(coord, border), lessThan(checkCoord, sizeState.xy)))) {
        result = (sampleMethodState(checkCoord) >= threshold) ? ivec4(coordStore, 0, 0) : ivec4(0, 0, coordStore);
    } else result = ivec4(0, 0, coordStore);
    imageStore(resultMap, coord, result);
}
""";

        public final static String PROCESS = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

#define MAX_DISTANCE 268435456.0

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

layout(binding = 0) uniform isampler2D coordMapIn;
layout(binding = 0, rgba16i) uniform iimage2D coordMapOut;

uniform ivec2 size;
uniform int step;

ivec4 advanceJFA(in ivec2 coord, ivec2 maxIndex) {
    ivec4 result = ivec4(0);

    ivec2 currCoord = ivec2(0);
    ivec4 targetCoord;
    vec4 diffCoord = vec4(0.0);
    vec2 currDistSQ = vec2(0.0), distSQ = vec2(MAX_DISTANCE);
    for (int y = -1; y <= 1; ++y) {
        currCoord.y = y * step + coord.y;
        for (int x = -1; x <= 1; ++x) {
            currCoord.x = x * step + coord.x;
            if (any(bvec4(lessThan(currCoord, ivec2(0)), greaterThanEqual(currCoord, maxIndex)))) continue;
            targetCoord = texelFetch(coordMapIn, currCoord, 0) - 1;

            if (targetCoord.x > -1) {
                diffCoord.xy = vec2(coord - targetCoord.xy);
                currDistSQ.x = dot(diffCoord.xy, diffCoord.xy);
                if (currDistSQ.x < distSQ.x) {
                    distSQ.x = currDistSQ.x;
                    result.xy = targetCoord.xy + 1;
                }
            }
            if (targetCoord.z > -1) {
                diffCoord.zw = vec2(coord - targetCoord.zw);
                currDistSQ.y = dot(diffCoord.zw, diffCoord.zw);
                if (currDistSQ.y < distSQ.y) {
                    distSQ.y = currDistSQ.y;
                    result.zw = targetCoord.zw + 1;
                }
            }
        }
    }
    return result;
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, size))) return;
    ivec2 maxIndex = size - 1;
    imageStore(coordMapOut, coord, advanceJFA(coord, maxIndex));
}
""";

        public final static String RESULT = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine void formatPickerStore(in ivec2 coord, in vec4 result);
subroutine uniform formatPickerStore formatPickerStoreState;

layout(binding = 0) uniform isampler2D coordMap;
layout(binding = 0, r8) uniform writeonly image2D resultMap;
layout(binding = 1, r16) uniform writeonly image2D resultMapR16;

uniform ivec2 size;
uniform vec2 preMultiply;

subroutine(formatPickerStore) void bit8Store(in ivec2 coord, in vec4 result) {
    imageStore(resultMap, coord, result);
}

subroutine(formatPickerStore) void bit16Store(in ivec2 coord, in vec4 result) {
    imageStore(resultMapR16, coord, result);
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, size))) return;
    vec4 result = vec4(max(texelFetch(coordMap, coord, 0) - 1, 0) - coord.xyxy);
    float sdf = clamp(length(result.xy) * preMultiply.y, 0.0, 1.0) - clamp(length(result.zw) * preMultiply.x, 0.0, 1.0);
    result = vec4(clamp(fma(sdf, 0.5, 0.5), 0.0, 1.0), vec3(0.0));
    formatPickerStoreState(coord, result);
}
""";

        private SDF() {}
    }

    public final static class GaussianBlur {
        private GaussianBlur() {}

        public final static String RGBA = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine void formatPickerStore(in ivec2 coord, in vec4 result);
subroutine uniform formatPickerStore formatPickerStoreState;
subroutine vec4 workMode(in ivec2 coord);
subroutine uniform workMode workModeState;

layout(binding = 0) uniform sampler2D texIn;
layout(binding = 0, rgba8) uniform writeonly image2D texOut;
layout(binding = 1, rgba16) uniform writeonly image2D texOut16;

uniform ivec3 sizeStep;
uniform int vertical;
uniform float perStep;

float getGaussian(float x) {
    float p = x * perStep;
    return perStep * 0.3989423 * exp(-0.5 * p * p);
}

subroutine(formatPickerStore) void bit8Store(in ivec2 coord, in vec4 result) {
    imageStore(texOut, coord, result);
}

subroutine(formatPickerStore) void bit16Store(in ivec2 coord, in vec4 result) {
    imageStore(texOut16, coord, result);
}

subroutine(workMode) vec4 filterMode(in ivec2 coord) {
    vec4 result = vec4(0.0);
    ivec2 maxIndex = sizeStep.xy - 1;
    ivec2 offset;
    float gaussian, fix = 0.0;
    bool isVertical = bool(vertical);
    for (int i = -sizeStep.z; i <= sizeStep.z; ++i) {
        gaussian = getGaussian(float(i));
        fix += gaussian;
        offset = isVertical ? ivec2(0, i) : ivec2(i, 0);
        result += texelFetch(texIn, clamp(offset + coord, ivec2(0), maxIndex), 0) * gaussian;
    }
    result /= fix;
    return result;
}

subroutine(workMode) vec4 copyMode(in ivec2 coord) {
    return texelFetch(texIn, coord, 0);
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, sizeStep.xy))) return;
    vec4 result = workModeState(coord);
    formatPickerStoreState(coord, result);
}
""";

        public final static String RED = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine void formatPickerStore(in ivec2 coord, in float result);
subroutine uniform formatPickerStore formatPickerStoreState;
subroutine float workMode(in ivec2 coord);
subroutine uniform workMode workModeState;

layout(binding = 0) uniform sampler2D texIn;
layout(binding = 0, r8) uniform writeonly image2D texOut;
layout(binding = 1, r16) uniform writeonly image2D texOut16;

uniform ivec3 sizeStep;
uniform int vertical;
uniform float perStep;

float getGaussian(float x) {
    float p = x * perStep;
    return perStep * 0.3989423 * exp(-0.5 * p * p);
}

subroutine(formatPickerStore) void bit8Store(in ivec2 coord, in float result) {
    imageStore(texOut, coord, vec4(result, vec3(0.0)));
}

subroutine(formatPickerStore) void bit16Store(in ivec2 coord, in float result) {
    imageStore(texOut16, coord, vec4(result, vec3(0.0)));
}

subroutine(workMode) float filterMode(in ivec2 coord) {
    float result = 0.0;
    ivec2 maxIndex = sizeStep.xy - 1;
    ivec2 offset;
    float gaussian, fix = 0.0;
    bool isVertical = bool(vertical);
    for (int i = -sizeStep.z; i <= sizeStep.z; ++i) {
        gaussian = getGaussian(float(i));
        fix += gaussian;
        offset = isVertical ? ivec2(0, i) : ivec2(i, 0);
        result += texelFetch(texIn, clamp(offset + coord, ivec2(0), maxIndex), 0).x * gaussian;
    }
    result /= fix;
    return result;
}

subroutine(workMode) float copyMode(in ivec2 coord) {
    return texelFetch(texIn, coord, 0).x;
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, sizeStep.xy))) return;
    float result = workModeState(coord);
    formatPickerStoreState(coord, result);
}
""";
    }

    public final static class BilateralFilter {
        private BilateralFilter() {}

        public final static String RGBA = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine void formatPickerStore(in ivec2 coord, in vec4 result);
subroutine uniform formatPickerStore formatPickerStoreState;

layout(binding = 0) uniform sampler2D texSrc;
layout(binding = 1) uniform sampler2D texIn;
layout(binding = 0, rgba8) uniform writeonly image2D texOut;
layout(binding = 1, rgba16) uniform writeonly image2D texOut16;

uniform ivec3 sizeStep;
uniform int vertical;
uniform vec2 gSigmaSRInv;

vec3 getWeight(in vec3 diff, in float i) {
    return exp(gSigmaSRInv.x * i * i + gSigmaSRInv.y * diff * diff);
}

subroutine(formatPickerStore) void bit8Store(in ivec2 coord, in vec4 result) {
    imageStore(texOut, coord, result);
}

subroutine(formatPickerStore) void bit16Store(in ivec2 coord, in vec4 result) {
    imageStore(texOut16, coord, result);
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, sizeStep.xy))) return;
    vec4 center = texelFetch(texSrc, coord, 0);
    bool isVertical = bool(vertical);
    ivec2 checkSize = ivec2(0, isVertical ? sizeStep.y : sizeStep.x);
    ivec2 coordCurr;
    vec3 curr, diff, sum = vec3(0.0), weightCurr, weight = vec3(0.0);
    for (int i = -sizeStep.z; i <= sizeStep.z; ++i) {
        coordCurr = coord + (isVertical ? ivec2(0, i) : ivec2(i, 0));
        checkSize.x = isVertical ? coordCurr.y : coordCurr.x;
        if (checkSize.x < 0 || checkSize.x >= checkSize.y) continue;
        curr = texelFetch(texIn, coordCurr, 0).xyz;
        diff = center.xyz - curr;
        weightCurr = getWeight(diff, float(i));
        sum += curr * weightCurr;
        weight += weightCurr;
    }
    formatPickerStoreState(coord, vec4(sum / weight, center.w));
}
""";

        public final static String RED = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine void formatPickerStore(in ivec2 coord, in float result);
subroutine uniform formatPickerStore formatPickerStoreState;

layout(binding = 0) uniform sampler2D texSrc;
layout(binding = 1) uniform sampler2D texIn;
layout(binding = 0, r8) uniform writeonly image2D texOut;
layout(binding = 1, r16) uniform writeonly image2D texOut16;

uniform ivec3 sizeStep;
uniform int vertical;
uniform vec2 gSigmaSRInv;

float getWeight(in float diff, in float i) {
    return exp(gSigmaSRInv.x * i * i + gSigmaSRInv.y * diff * diff);
}

subroutine(formatPickerStore) void bit8Store(in ivec2 coord, in float result) {
    imageStore(texOut, coord, vec4(result, vec3(0.0)));
}

subroutine(formatPickerStore) void bit16Store(in ivec2 coord, in float result) {
    imageStore(texOut16, coord, vec4(result, vec3(0.0)));
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, sizeStep.xy))) return;
    float center = texelFetch(texSrc, coord, 0).x;
    bool isVertical = bool(vertical);
    ivec2 checkSize = ivec2(0, isVertical ? sizeStep.y : sizeStep.x);
    ivec2 coordCurr;
    float curr, diff, sum = 0.0, weightCurr, weight = 0.0;
    for (int i = -sizeStep.z; i <= sizeStep.z; ++i) {
        coordCurr = coord + (isVertical ? ivec2(0, i) : ivec2(i, 0));
        checkSize.x = isVertical ? coordCurr.y : coordCurr.x;
        if (checkSize.x < 0 || checkSize.x >= checkSize.y) continue;
        curr = texelFetch(texIn, coordCurr, 0).x;
        diff = center - curr;
        weightCurr = getWeight(diff, float(i));
        sum += curr * weightCurr;
        weight += weightCurr;
    }
    formatPickerStoreState(coord, sum / weight);
}
""";
    }

    public final static class FourierTransform {
        private FourierTransform() {}

        public final static String DFT = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

#define PI2 6.2831853

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

struct ComplexArray {
    vec2 red;
    vec2 green;
    vec2 blue;
    vec2 alpha;
};

subroutine void formatPickerStore(in ivec2 coord, in ComplexArray result, in bool isGenRGBA);
subroutine uniform formatPickerStore formatPickerStoreState;

layout(binding = 0) uniform sampler2D texIn;
layout(binding = 0, rgba8) uniform writeonly image2D texOut;
layout(binding = 1, rgba16f) uniform writeonly image2D texOut16;
layout(binding = 2, rgba32f) uniform writeonly image2D texOut32;

uniform ivec2 size;
uniform int state; // 0b1 = inverse, 0b10 = vertical, 0b100 = genFromRGBA, 0b1000 = genRGBA;
uniform vec2 sizeDiv;

vec2 getEuler(in float theta) {
    return vec2(cos(theta), sin(theta));
}

vec2 cmul(in vec2 left, in vec2 right)
{
    return vec2(left.x * right.x - left.y * right.y, left.x * right.y + left.y * right.x);
}

subroutine(formatPickerStore) void bit8Store(in ivec2 coord, in ComplexArray result, in bool isGenRGBA) {
    vec4 resultStore = isGenRGBA ? vec4(length(result.red), length(result.green), length(result.blue), length(result.alpha)) : vec4(result.red, result.green);
    imageStore(texOut, coord, resultStore);
    if (!isGenRGBA) imageStore(texOut, ivec2(coord.x + size.x, coord.y), vec4(result.blue, result.alpha));
}

subroutine(formatPickerStore) void bit16Store(in ivec2 coord, in ComplexArray result, in bool isGenRGBA) {
    vec4 resultStore = isGenRGBA ? vec4(length(result.red), length(result.green), length(result.blue), length(result.alpha)) : vec4(result.red, result.green);
    imageStore(texOut16, coord, resultStore);
    if (!isGenRGBA) imageStore(texOut16, ivec2(coord.x + size.x, coord.y), vec4(result.blue, result.alpha));
}

subroutine(formatPickerStore) void bit32Store(in ivec2 coord, in ComplexArray result, in bool isGenRGBA) {
    vec4 resultStore = isGenRGBA ? vec4(length(result.red), length(result.green), length(result.blue), length(result.alpha)) : vec4(result.red, result.green);
    imageStore(texOut32, coord, resultStore);
    if (!isGenRGBA) imageStore(texOut32, ivec2(coord.x + size.x, coord.y), vec4(result.blue, result.alpha));
}

ComplexArray transformHFromRGBA(in ivec2 coord, in float centerCoord) {
    float theta, coordF = PI2 * centerCoord * sizeDiv.x;
    vec2 euler;
    vec4 raw;
    ComplexArray result = ComplexArray(vec2(0.0), vec2(0.0), vec2(0.0), vec2(0.0));
    for (int n = 0; n < size.x; ++n) {
        theta = float(n) * coordF;
        euler = getEuler(theta);
        raw = texelFetch(texIn, ivec2(n, coord.y), 0);
        result.red += cmul(vec2(raw.x, 0.0), euler);
        result.green += cmul(vec2(raw.y, 0.0), euler);
        result.blue += cmul(vec2(raw.z, 0.0), euler);
        result.alpha += cmul(vec2(raw.w, 0.0), euler);
    }
    return result;
}

ComplexArray transformH(in ivec2 coord, in float pi, in float centerCoord) {
    float theta, coordF = pi * centerCoord * sizeDiv.x;
    int coordSub = size.x;
    vec2 euler;
    vec4 comp;
    ComplexArray result = ComplexArray(vec2(0.0), vec2(0.0), vec2(0.0), vec2(0.0));
    for (int n = 0; n < size.x; ++n) {
        theta = float(n) * coordF;
        euler = getEuler(theta);
        comp = texelFetch(texIn, ivec2(n, coord.y), 0);
        result.red += cmul(comp.xy, euler);
        result.green += cmul(comp.zw, euler);
        comp = texelFetch(texIn, ivec2(coordSub, coord.y), 0);
        result.blue += cmul(comp.xy, euler);
        result.alpha += cmul(comp.zw, euler);
        ++coordSub;
    }
    return result;
}

ComplexArray transformV(in ivec2 coord, in float pi, in float centerCoord) {
    float theta, coordF = pi * centerCoord * sizeDiv.y;
    int coordSub = coord.x + size.x;
    vec2 euler;
    vec4 comp;
    ComplexArray result = ComplexArray(vec2(0.0), vec2(0.0), vec2(0.0), vec2(0.0));
    for (int n = 0; n < size.y; ++n) {
        theta = float(n) * coordF;
        euler = getEuler(theta);
        comp = texelFetch(texIn, ivec2(coord.x, n), 0);
        result.red += cmul(comp.xy, euler);
        result.green += cmul(comp.zw, euler);
        comp = texelFetch(texIn, ivec2(coordSub, n), 0);
        result.blue += cmul(comp.xy, euler);
        result.alpha += cmul(comp.zw, euler);
    }
    return result;
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, size))) return;
    bool isInverse = (state & 1) == 1;
    bool isVertical = (state & 2) == 2;
    bool isGenFromRGBA = (state & 4) == 4;
    bool isGenRGBA = (state & 8) == 8;
    vec2 centerCoord = isVertical ? vec2(coord.y, size.y) : vec2(coord.x, size.x);
    if (!isInverse) centerCoord.x -= centerCoord.y * 0.5;

    float pi = isInverse ? -PI2 : PI2;
    ComplexArray result = ComplexArray(vec2(0.0), vec2(0.0), vec2(0.0), vec2(0.0));
    if (isGenFromRGBA) {
        result = transformHFromRGBA(coord, centerCoord.x);
    } else {
        result = isVertical ? transformV(coord, pi, centerCoord.x) : transformH(coord, pi, centerCoord.x);
    }
    if (isInverse) {
        vec2 div = vec2(isVertical ? sizeDiv.y : sizeDiv.x);
        result.red *= div;
        result.green *= div;
        result.blue *= div;
        result.alpha *= div;
    }
    formatPickerStoreState(coord, result, isGenRGBA);
}
""";

        public final static String DFT_RED = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

#define PI2 6.2831853

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine void formatPickerStore(in ivec2 coord, in vec2 result, in bool isGenRed);
subroutine uniform formatPickerStore formatPickerStoreState;

layout(binding = 0) uniform sampler2D texIn;
layout(binding = 0, r8) uniform writeonly image2D texOut;
layout(binding = 1, r16f) uniform writeonly image2D texOut16;
layout(binding = 2, r32f) uniform writeonly image2D texOut32;

uniform ivec2 size;
uniform int state; // 0b1 = inverse, 0b10 = vertical, 0b100 = genFromRed, 0b1000 = genRed;
uniform vec2 sizeDiv;

vec2 getEuler(in float theta) {
    return vec2(cos(theta), sin(theta));
}

vec2 cmul(in vec2 left, in vec2 right)
{
    return vec2(left.x * right.x - left.y * right.y, left.x * right.y + left.y * right.x);
}

subroutine(formatPickerStore) void bit8Store(in ivec2 coord, in vec2 result, in bool isGenRed) {
    vec4 resultStore = isGenRed ? vec4(length(result), vec3(0.0)) : vec4(result.x, vec3(0.0));
    imageStore(texOut, coord, resultStore);
    if (!isGenRed) imageStore(texOut, ivec2(coord.x + size.x, coord.y), vec4(result.y, vec3(0.0)));
}

subroutine(formatPickerStore) void bit16Store(in ivec2 coord, in vec2 result, in bool isGenRed) {
    vec4 resultStore = isGenRed ? vec4(length(result), vec3(0.0)) : vec4(result.x, vec3(0.0));
    imageStore(texOut16, coord, resultStore);
    if (!isGenRed) imageStore(texOut16, ivec2(coord.x + size.x, coord.y), vec4(result.y, vec3(0.0)));
}

subroutine(formatPickerStore) void bit32Store(in ivec2 coord, in vec2 result, in bool isGenRed) {
    vec4 resultStore = isGenRed ? vec4(length(result), vec3(0.0)) : vec4(result.x, vec3(0.0));
    imageStore(texOut32, coord, resultStore);
    if (!isGenRed) imageStore(texOut32, ivec2(coord.x + size.x, coord.y), vec4(result.y, vec3(0.0)));
}

vec2 transformHFromRed(in ivec2 coord, in float centerCoord) {
    float theta, coordF = PI2 * centerCoord * sizeDiv.x;
    vec2 euler;
    vec2 result = vec2(0.0);
    for (int n = 0; n < size.x; ++n) {
        theta = float(n) * coordF;
        euler = getEuler(theta);
        result += cmul(vec2(texelFetch(texIn, ivec2(n, coord.y), 0).x, 0.0), euler);
    }
    return result;
}

vec2 transformH(in ivec2 coord, in float pi, in float centerCoord) {
    float theta, coordF = pi * centerCoord * sizeDiv.x;
    int coordSub = size.x;
    vec2 euler;
    vec2 comp = vec2(0.0);
    vec2 result = vec2(0.0);
    for (int n = 0; n < size.x; ++n) {
        theta = float(n) * coordF;
        euler = getEuler(theta);
        comp.x = texelFetch(texIn, ivec2(n, coord.y), 0).x;
        comp.y = texelFetch(texIn, ivec2(coordSub, coord.y), 0).x;
        result += cmul(comp, euler);
        ++coordSub;
    }
    return result;
}

vec2 transformV(in ivec2 coord, in float pi, in float centerCoord) {
    float theta, coordF = pi * centerCoord * sizeDiv.y;
    int coordSub = coord.x + size.x;
    vec2 euler;
    vec2 comp = vec2(0.0);
    vec2 result = vec2(0.0);
    for (int n = 0; n < size.y; ++n) {
        theta = float(n) * coordF;
        euler = getEuler(theta);
        comp.x = texelFetch(texIn, ivec2(coord.x, n), 0).x;
        comp.y = texelFetch(texIn, ivec2(coordSub, n), 0).x;
        result += cmul(comp, euler);
    }
    return result;
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, size))) return;
    bool isInverse = (state & 1) == 1;
    bool isVertical = (state & 2) == 2;
    bool isGenFromRed = (state & 4) == 4;
    bool isGenRed = (state & 8) == 8;
    vec2 centerCoord = isVertical ? vec2(coord.y, size.y) : vec2(coord.x, size.x);
    if (!isInverse) centerCoord.x -= centerCoord.y * 0.5;

    float pi = isInverse ? -PI2 : PI2;
    vec2 result = vec2(0.0);
    if (isGenFromRed) {
        result = transformHFromRed(coord, centerCoord.x);
    } else {
        result = isVertical ? transformV(coord, pi, centerCoord.x) : transformH(coord, pi, centerCoord.x);
    }
    if (isInverse) {
        vec2 div = vec2(isVertical ? sizeDiv.y : sizeDiv.x);
        result *= div;
    }
    formatPickerStoreState(coord, result, isGenRed);
}
""";
    }

    public final static class NormalMapGen {
        private NormalMapGen() {}

        public final static String INIT = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine vec2 texInput(in float luminance, in float alpha, in ivec2 coord);
subroutine uniform texInput texInputState;

layout(binding = 0) uniform sampler2D texIn;
layout(binding = 1) uniform sampler2D volumeMap;
layout(binding = 2) uniform sampler2D detailsMap;
layout(binding = 0, rgba16) uniform writeonly image2D texOut;

uniform ivec2 size;
uniform vec4 state[2]; // vec4(srcStrength, srcPowFactor, violumeApply, DetailsApply), vec4(srcBrightness, srcContrast, srcSmoothstepMix, volumeSmoothMix)
uniform vec2 rampMix;

float extraFix(in float x) {
    float result = pow(x, state[0].y);
    result -= 0.5;
    result *= state[1].y;
    result += 0.5;
    result *= state[1].x;
    result = mix(result, smoothstep(0.0, 1.0, result), state[1].z);
    return result;
}

float fi(in float a, in float b) {
    return 1.0 - (1.0 - a) * (1.0 - b);
}

float getRamp(in vec2 uv) {
    float x = 1.0 - abs(uv.x * 2.0 - 1.0);
    float y = smoothstep(1.0, 0.0, abs(uv.y * 2.0 - 1.0));
    return fi(x * rampMix.x, y * rampMix.y);
}

subroutine(texInput) vec2 texOnly(in float luminance, in float alpha, in ivec2 coord) {
    return vec2(luminance, alpha);
}

subroutine(texInput) vec2 withVolume(in float luminance, in float alpha, in ivec2 coord) {
    float volume = 1.0 - min(texelFetch(volumeMap, coord, 0).x * 2.0, 1.0);
    volume = (state[1].w > 0.0) ? smoothstep(0.0, 1.0, volume) : volume;
    return vec2(fi(luminance, volume * state[0].z), alpha);
}

subroutine(texInput) vec2 withDetails(in float luminance, in float alpha, in ivec2 coord) {
    vec4 detailsRaw = texelFetch(detailsMap, coord, 0);
    float details = dot(detailsRaw.xyz * detailsRaw.w, vec3(LINEAR_VALUES));
    details = pow(details, state[0].y);
    return vec2(mix(luminance, extraFix(details), state[0].w), alpha);
}

subroutine(texInput) vec2 withBoth(in float luminance, in float alpha, in ivec2 coord) {
    float volume = 1.0 - min(texelFetch(volumeMap, coord, 0).x * 2.0, 1.0);
    volume = (state[1].w > 0.0) ? smoothstep(0.0, 1.0, volume) : volume;
    vec4 detailsRaw = texelFetch(detailsMap, coord, 0);
    float details = dot(detailsRaw.xyz * detailsRaw.w, vec3(LINEAR_VALUES));
    return vec2(fi(mix(luminance, extraFix(details), state[0].w), volume * state[0].z), alpha);
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, size))) return;
    vec4 raw = texelFetch(texIn, coord, 0);
    float luminance = dot(raw.xyz * raw.w, vec3(LINEAR_VALUES));
    luminance = extraFix(luminance) * state[0].x;
    vec2 result = texInputState(luminance, raw.w, coord);
    if (rampMix.x > 0.0 || rampMix.y > 0.0) result.x = fi(result.x, getRamp(vec2(coord) / vec2(size - 1)));
    imageStore(texOut, coord, vec4(vec3(result.x), result.y));
}
""";

        public final static String RESULT = """
#version 430

precision OVERWRITE_PRECISION float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

layout(binding = 0, rgba8) uniform writeonly image2D tex;
layout(binding = 0) uniform sampler2D texHeight;
layout(binding = 1) uniform sampler2D alphaSrc;

uniform ivec3 sizeState;
uniform float normalStrength;

float getPixel(in ivec2 offset, in ivec2 coord, in ivec2 maxIndex) {
    return texelFetch(texHeight, clamp(coord + offset, ivec2(0), maxIndex), 0).x;
}

vec3 getNormal(in ivec2 coord) {
    ivec2 maxIndex = sizeState.xy - 1;
    float gLT = getPixel(ivec2(-1, 1), coord, maxIndex);
    float gT = getPixel(ivec2(0, 1), coord, maxIndex);
    float gRT = getPixel(ivec2(1, 1), coord, maxIndex);
    float gL = getPixel(ivec2(-1, 0), coord, maxIndex);
    float gR = getPixel(ivec2(1, 0), coord, maxIndex);
    float gLB = getPixel(ivec2(-1, -1), coord, maxIndex);
    float gB = getPixel(ivec2(0, -1), coord, maxIndex);
    float gRB = getPixel(ivec2(1, -1), coord, maxIndex);

    float gxL = gL * 0.625;
    gxL += (gLT + gLB) * 0.1875;
    float gxR = gR * 0.625;
    gxR += (gRT + gRB) * 0.1875;
    float gyU = gT * 0.625;
    gyU += (gLT + gRT) * 0.1875;
    float gyD = gB * 0.625;
    gyD += (gLB + gRB) * 0.1875;
    bool flipX = (sizeState.z & 2) == 2;
    bool flipY = (sizeState.z & 1) == 1;
    float dx = flipX ? (gxR - gxL) : (gxL - gxR);
    float dy = flipY ? (gyU - gyD) : (gyD - gyU);
    return normalize(vec3(vec2(dx, dy) * normalStrength, 0.05));
}

void main()
{
    ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
    if (any(greaterThanEqual(coord, sizeState.xy))) return;
    bool keepAlpha = (sizeState.z & 4) == 4;
    float alpha = texelFetch(alphaSrc, coord, 0).w;
    if (keepAlpha && alpha <= 0.0) {
        imageStore(tex, coord, vec4(0.5, 0.5, 1.0, 0.0));
        return;
    }
    vec3 normal = getNormal(coord);
    normal = clamp(normal * 0.5 + 0.5, 0.0, 1.0);
    imageStore(tex, coord, vec4(normal, keepAlpha ? alpha : 1.0));
}
""";

        public final static String LEGACY_VERT = """
#version 110

varying vec2 fragUV;

void main() {
    fragUV = max(gl_Vertex.xy, vec2(0.0));
	gl_Position = gl_Vertex;
}
""";

        public final static String LEGACY_BLUR = """
#version 110
// For reference
#define LINEAR vec3(0.2126729, 0.7151522, 0.0721750)

uniform vec3 state[3]; // vec3(rampMix.xy, step), vec3(brightnessAdj, contrastAdj, perStep), vec3(srcStrength, srcPowFactor, srcSmoothstepMix)
uniform vec4 stepUV_srcUVDiv;
uniform bool vertical;
uniform sampler2D srcTex;

varying vec2 fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

float extraFix(float x) {
    float result = pow(x, state[2].y);
    result -= 0.5;
    result *= state[1].y;
    result += 0.5;
    result *= state[1].x;
    result = mix(result, smoothstep(0.0, 1.0, result), state[2].z);
    return result;
}

float getGray(vec3 col) {
    return extraFix(dot(col, LINEAR)) * state[2].x;
}

float getGaussian(float x) {
    float p = x * state[1].z;
    return state[1].z * 0.3989423 * exp(-0.5 * p * p);
}

float fi(float a, float b) {
    return 1.0 - (1.0 - a) * (1.0 - b);
}

float getRamp(vec2 uv) {
    float x = 1.0 - abs(uv.x * 2.0 - 1.0);
    float y = smoothstep(1.0, 0.0, abs(uv.y * 2.0 - 1.0));
    return fi(x * state[0].x, y * state[0].y);
}

void main() {
    vec4 result = vec4(0.0);
    float alpha = 0.0;
    int stepNum = int(state[0].z);

    if (stepNum != 0) {
        vec3 resultTmp = vec3(0.0);
        vec2 offset;
        float gaussian, fi, fix = 0.0;
        for (int i = -stepNum; i <= stepNum; ++i) {
            fi = float(i);
            gaussian = getGaussian(fi);
            fix += gaussian;
            offset = vertical ? vec2(0.0, fi * stepUV_srcUVDiv.y) : vec2(fi * stepUV_srcUVDiv.x, 0.0);
            result = texture2D(srcTex, fragUV + offset);
            resultTmp += result.xyz * gaussian;
            if (i == 0) alpha = result.w;
        }
        result.xyz = resultTmp / fix;
        result.w = alpha;
    } else {
        result = texture2D(srcTex, fragUV);
    }

    if (vertical) {
        result.x = getGray(result.xyz) * result.w;
        if (state[0].x > 0.0 || state[0].y > 0.0) result.x = fi(result.x, getRamp(fragUV * stepUV_srcUVDiv.zw));
    }

    gl_FragColor = result;
}
""";

        public final static String LEGACY_RESULT = """
#version 110
// For reference
#define LINEAR vec3(0.2126729, 0.7151522, 0.0721750)

uniform vec2 state; // normalScale, keepAlpha
uniform vec2 stepUV;
uniform sampler2D srcTex;

varying vec2 fragUV;

float getGray() {
    return texture2D(srcTex, fragUV).x;
}

float getGray(vec2 offset) {
    return texture2D(srcTex, fragUV + offset).x;
}

void main() {
    float gLT = getGray(vec2(-stepUV.x, stepUV.y));
    float gL = getGray(vec2(-stepUV.x, 0.0));
    float gLB = getGray(-stepUV);
    float gB = getGray(vec2(0.0, -stepUV.y));
    float gRB = getGray(vec2(stepUV.x, -stepUV.y));
    float gR = getGray(vec2(stepUV.x, 0.0));
    float gRT = getGray(stepUV);
    float gT = getGray(vec2(0.0, stepUV.y));

    float gxL = gL * 0.625; // sobel 0.5
    gxL += (gLT + gLB) * 0.1875; // sobel 0.25
    float gxR = gR * 0.625;
    gxR += (gRT + gRB) * 0.1875;
    float gyU = gT * 0.625;
    gyU += (gLT + gRT) * 0.1875;
    float gyD = gB * 0.625;
    gyD += (gLB + gRB) * 0.1875;
    float dx = gxL - gxR;
    float dy = gyD - gyU;

    gl_FragColor = vec4(normalize(vec3(vec2(dx, dy) * state.x, 0.05)) * 0.5 + 0.5, (state.y > 0.0) ? texture2D(srcTex, fragUV).w : 1.0);
}
""";
    }

    public final static class RadialBlur {
        private RadialBlur() {}

        public final static String VERT = """
#version 110

// vec4(center, radiusSamples, samplesInv)
uniform vec4 statePackage;

varying vec2 fragUV;

void main() {
	fragUV = max(gl_Vertex.xy, vec2(0.0)) - statePackage.xy;
	gl_Position = gl_Vertex;
}
""";

        public final static String FRAG = """
#version 110

// vec4(center, radiusSamples, samplesInv)
uniform vec4 statePackage;
uniform float alphaStrength;
uniform sampler2D tex;

varying vec2 fragUV;

void main() {
    vec4 result = vec4(0.0);
    int limit = int(1.0 / statePackage.w);
    for (int i = 1; i <= limit; ++i) {
        result += texture2D(tex, fragUV * float(i) * statePackage.z + statePackage.xy);
    }
    gl_FragColor = result * statePackage.w * alphaStrength;
}
""";
    }

    public final static class Share {
        private Share() {}

        public final static String POST_VERT = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (location = 0) in vec2 vertex;

uniform vec2 uvStart;
uniform vec2 uvEnd;

smooth out vec2 fragUV;

void main()
{
	const vec2 uv[] = vec2[4](uvStart.xy, vec2(uvEnd.x, uvStart.y), vec2(uvStart.x, uvEnd.y), uvEnd.xy);
	fragUV = uv[gl_VertexID];
	gl_Position = vec4(vertex, 0.0, 1.0);
}
""";

        public final static String POST_VERT_SIMPLE = """
#version 420

precision highp float;

layout (location = 0) in vec2 vertex;

smooth out vec2 fragUV;

void main()
{
	const vec2 uv[] = vec2[4](vec2(0.0), vec2(1.0, 0.0), vec2(0.0, 1.0), vec2(1.0));
	fragUV = uv[gl_VertexID];
	gl_Position = vec4(vertex, 0.0, 1.0);
}
""";

        public final static String DIRECT_FRAG = """
#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

smooth in vec2 fragUV;

layout (binding = 0) uniform sampler2D tex;
uniform float alphaFix;
uniform float level;

layout (location = 0) out vec4 fragColor;

void main()
{
    vec4 result = textureLod(tex, fragUV, level);
    if (alphaFix > -1.0) result.w = alphaFix;
    fragColor = result;
}
""";
    }

    /**
     * For vanilla and fixed pipeline.
     */
    public final static class Number {
        private Number() {}

        public final static String VERT = """
#version 110

// vec2(length), number, invert, vec4(color)
uniform vec4 statePackage[2];
uniform float charLength;

varying vec2 fragUV;

void main() {
	vec4 pos = ftransform();
	if (statePackage[1].w <= 0.0) pos.xyz = vec3(-65536.0);
	gl_Position = pos;
	fragUV = vec2(gl_Vertex.x > 0.0 ? 1.0 : 0.0, gl_Vertex.y > 0.0 ? 1.0 : 0.0);
}
""";

        public final static String FRAG = """
#version 110

#define SIZE vec2(3.0, 5.0)
#define SPACING 4.0
#define SIZE_FACTOR SPACING / SIZE.x
#define POINT 2.0
#define NEG 448.0

// vec2(length), number, invert, vec4(color)
uniform vec4 statePackage[2];
uniform float charLength;

varying vec2 fragUV;

float getNumber(int digit) {
    float result = 31599.0;
    if (digit == 1) result = 25751.0;
    if (digit == 2) result = 29671.0;
    if (digit == 3) result = 29647.0;
    if (digit == 4) result = 23497.0;
    if (digit == 5) result = 31183.0;
    if (digit == 6) result = 31215.0;
    if (digit == 7) result = 29257.0;
    if (digit == 8) result = 31727.0;
    if (digit == 9) result = 31689.0;
    return result;
}

float getSprite(float sprite, vec2 uv) {
    uv = floor(uv);
    float bit = (SIZE.x - uv.x - 1.0) + uv.y * SIZE.x;
    bool bounds = all(greaterThanEqual(uv, vec2(0)));
    bounds = bounds && all(lessThan(uv, SIZE));
    return bounds ? floor(mod(sprite / pow(2.0, bit), 2.0)) : 0.0;
}

void main() {
    vec2 uv = fragUV * SIZE;
    uv.x *= charLength * SIZE_FACTOR;
    vec2 offset = vec2(0.0);
    float number, clip, result, step;
    bool negative = statePackage[0].z < 0.0;
    number = abs(statePackage[0].z);
    clip = result = step = 0.0;
    int digit = 0;
    for(int i = int(statePackage[0].x); i >= -int(statePackage[0].y); i--) {
        clip = float(number > pow(10.0, float(i)) || i == 0);
        digit = int(mod(number / pow(10.0, float(i)), 10.0));
        step = SPACING * clip;
        if (negative && i == int(statePackage[0].x)) {
            result += getSprite(NEG, uv - offset);
            offset.x += SPACING;
        }
        if(statePackage[0].x != 0.0 && i == -1) {
            result += getSprite(POINT, uv - offset) * clip;
            offset.x += step;
        }
        result += getSprite(getNumber(digit), uv - offset) * clip;
        offset.x += step;
    }
    vec4 col = vec4(result);
    if (statePackage[0].w == 1.0) col = 1.0 - col;
    gl_FragColor = col * statePackage[1];
}
""";
    }

    public final static class Arc {
        private Arc() {}

        public final static String VERT = """
#version 110

// vec2(inner), ringHardness, innerHardness, vec4(color)
uniform vec4 statePackage[2];
uniform float arcValue;

varying vec2 fragUV;

void main() {
	vec4 pos = ftransform();
	if (statePackage[1].w <= 0.0) pos.xyz = vec3(-65536.0);
	gl_Position = pos;
	fragUV = vec2(gl_Vertex.x > 0.0 ? 1.0 : -1.0, gl_Vertex.y > 0.0 ? 1.0 : -1.0);
}
""";

        public final static String FRAG = """
#version 110

#define RIGHT vec2(1.0, 0.0)

// vec2(inner), ringHardness, innerHardness, vec4(color)
uniform vec4 statePackage[2];
uniform float arcValue;

varying vec2 fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

void main() {
    float ring = length(fragUV);
    float inner = length(fragUV / statePackage[0].xy);
    inner = statePackage[0].w >= 1.0 ? step(inner, 1.0) : smoothStep(1.0, statePackage[0].w, inner);
    float result = (statePackage[0].z >= 1.0 ? step(ring, 1.0) : smoothStep(1.0, statePackage[0].z, ring)) - inner;
    if (dot(normalize(fragUV), RIGHT) <= arcValue) result = 0.0;
    gl_FragColor = result * statePackage[1];
}
""";
    }

    public final static class TexArc {
        private TexArc() {}

        public final static String FRAG = """
#version 110

// innerHardness, ringHardness, innerFactor, arc, vec4(color)
uniform vec4 statePackage[2];
uniform sampler2D diffuseMap;

varying vec2 fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

float inverseLerp(float left, float right, float v) {
    return (v - left) * 1.0 / (right - left);
}

void main() {
    vec2 uv = vec2(inverseLerp(statePackage[0].z, 0.0, 1.0 - length(fragUV)), atan(-fragUV.y, fragUV.x) * -0.5 / 3.14159265 + 0.5);
    float arc = abs(statePackage[0].w);
    uv.x /= arc;
    uv.x -= ((1.0 / arc) - 1.0) * 0.5;
    float mask = uv.y * 2.0 - 1.0;
    float maskV = (mask < 0.0) ? statePackage[0].x : statePackage[0].y;
    mask = (maskV >= 1.0) ? step(abs(mask), 1.0) : smoothstep(1.0, maskV, abs(mask));
    if ((statePackage[0].w > 0.0) && (uv.x < 0.0 || uv.x > 1.0)) mask = 0.0;
    gl_FragColor = texture2D(diffuseMap, uv) * statePackage[1] * mask;
}
""";
    }

    public final static class Mission {
        private Mission() {}

        public final static String VERT = """
#version 110

uniform float time;

varying vec2 fragUV;

void main() {
	gl_Position = gl_Vertex;
	fragUV = max(gl_Vertex.xy, 0.0);
}
""";

        public final static String FRAG = """
#version 110

uniform float time;

varying vec2 fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

vec2 smoothStep(float edgeL, float edgeR, vec2 value) {
    vec2 result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

void main() {
    vec2 uv = fragUV;
    float line = uv.y - fract(time / 3.0) * 9.0;
    line = smoothStep(0.15, -0.2, abs(line + 0.3)) * 0.2;
    uv.x += uv.y * mix(-0.1, 0.1, uv.x) * 24.0;
    uv *= 16.0;
    uv.x *= 0.25;
    uv.y += time * 0.5;
    uv = abs(fract(uv) - 0.5);
    uv = smoothStep(0.3, 0.75, uv);
    float result = length(uv);
    vec3 mixCol = 0.3 + 0.15 * sin(time + fragUV.y * vec3(4.5, 2.0, 0.3));
    vec3 resultCol = vec3(0.06 + mixCol.x, 0.3 + mixCol.y, mixCol.z) * 0.3 * result + vec3(0.8, 1.0, 0.1) * 2.0 * sqrt(result * line);
    gl_FragColor = vec4(resultCol, 0.8);
}
""";
    }

    public final static class Illumination {
        private Illumination() {}

        public final static String AREA_TEX = """
#version 430

precision highp float;
precision highp int;

layout(local_size_x = RESET_VALUE, local_size_y = 8, local_size_z = 1) in;

subroutine vec4 filteringMode(in ivec2 coord);
subroutine uniform filteringMode filteringModeState;

layout (binding = 0) uniform sampler2D texIn;
layout (binding = 0, rgba8) writeonly uniform image2D texOut;

uniform ivec3 sizeStep;
uniform int vertical;
uniform vec4 uvStepDiv_Lod_ADiv;

float getGaussian(float x) {
	float p = x * uvStepDiv_Lod_ADiv.w;
	return uvStepDiv_Lod_ADiv.w * 0.3989423 * exp(-0.5 * p * p);
}

subroutine(filteringMode) vec4 normalMode(in ivec2 coord) {
	vec2 uv = vec2(coord) / sizeStep.xy;
	vec4 result = vec4(0.0);
	float fi, gaussian, fix = 0.0;
	bool isVertical = bool(vertical);
	for (int i = -sizeStep.z; i <= sizeStep.z; ++i) {
		fi = float(i);
		gaussian = getGaussian(fi);
		fix += gaussian;
		result += textureLod(texIn, fma(uvStepDiv_Lod_ADiv.xy, isVertical ? vec2(0, fi) : vec2(fi, 0), uv), uvStepDiv_Lod_ADiv.z) * gaussian;
	}
	return result / fix;
}

subroutine(filteringMode) vec4 copyMode(in ivec2 coord) {
	return texelFetch(texIn, coord, 0);
}

void main() {
	ivec2 coord = ivec2(gl_GlobalInvocationID.xy);
	if (any(greaterThanEqual(coord, sizeStep.xy))) return;
	imageStore(texOut, coord, filteringModeState(coord));
}
""";
    }

    private BUtil_ShaderSources() {}
}
