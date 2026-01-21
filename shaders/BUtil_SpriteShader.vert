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
