#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define TILE_STATE 3
#define UV_START_END 4
#define ENTITY_STATE 5

subroutine void t_uvMappingState(out vec2 uvStartP, out vec2 uvEndP);
subroutine uniform t_uvMappingState f_uvMapping;
subroutine void t_instanceStateCompute(out mat4 model, out vec4 mColor, out vec4 mEColor);
subroutine uniform t_instanceStateCompute f_instanceState;

layout (location = 0) in vec2 a_vertex;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 u_modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic)
// [vec2(tile), startIndex, randomEach], [vec2(start), vec2(end)], hashCode, totalTilesMinusOne, vec2(baseSize)
uniform vec4 u_statePackage[6];
uniform float u_globalTimerAlpha;
uniform uvec3 u_additionEmissive_DataBit_InstanceOffset;

out VERTEX_BLOCK {
	mat3 fragTBN;
	vec2 fragUV;
	vec3 fragPos;
	vec4 fragEntityColor;
	vec4 fragMixEmissive;
} vfb_data;

float hash12(vec2 p) {
	vec3 p3 = fract(vec3(p.xyx) * 0.1031);
	p3 += dot(p3, p3.yzx + 33.33);
	return fract((p3.x + p3.y) * p3.z);
}

subroutine(t_uvMappingState) void p_commonUV(out vec2 uvStartP, out vec2 uvEndP) {
	uvStartP = u_statePackage[UV_START_END].xy;
	uvEndP = u_statePackage[UV_START_END].zw;
}

subroutine(t_uvMappingState) void p_tileUV(out vec2 uvStartP, out vec2 uvEndP) {
	float tileX = mod(u_statePackage[TILE_STATE].z, u_statePackage[TILE_STATE].x);
	float tileY = round((u_statePackage[TILE_STATE].z - tileX) / u_statePackage[TILE_STATE].x);
	vec2 tileSizeVec = 1.0 / u_statePackage[TILE_STATE].xy * (u_statePackage[UV_START_END].zw - u_statePackage[UV_START_END].xy);
	uvStartP = tileSizeVec * vec2(tileX, tileY) + u_statePackage[UV_START_END].xy;
	uvEndP = uvStartP + tileSizeVec;
}

subroutine(t_uvMappingState) void p_tileRUV(out vec2 uvStartP, out vec2 uvEndP) {
	vec2 seed = vec2(u_statePackage[ENTITY_STATE].x, 0.0);
	if (u_statePackage[TILE_STATE].w > 0.0) seed.y = float(gl_InstanceID << 1 + 7);
	float finalIndex = round(hash12(seed) * u_statePackage[ENTITY_STATE].y) + u_statePackage[TILE_STATE].z;
	if (finalIndex >= u_statePackage[ENTITY_STATE].y) finalIndex -= u_statePackage[ENTITY_STATE].y;
	float tileX = mod(finalIndex, u_statePackage[TILE_STATE].x);
	float tileY = round((finalIndex - tileX) / u_statePackage[TILE_STATE].x);
	vec2 tileSizeVec = 1.0 / u_statePackage[TILE_STATE].xy * (u_statePackage[UV_START_END].zw - u_statePackage[UV_START_END].xy);
	uvStartP = tileSizeVec * vec2(tileX, tileY) + u_statePackage[UV_START_END].xy;
	uvEndP = uvStartP + tileSizeVec;
}

#include "BUtil_InstanceDataSSBO.h"

subroutine(t_instanceStateCompute) void p_noneData(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	model = u_modelMatrix;
	mColor = u_statePackage[COLOR] * u_globalTimerAlpha;
	mEColor = u_statePackage[EMISSIVE_COLOR] * u_globalTimerAlpha;
}

subroutine(t_instanceStateCompute) void p_haveData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic2D data = b_dataDynamic2D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic2DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(u_globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveFixedData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed2D data = b_dataFixed2D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchFixed2DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(u_globalTimerAlpha, data.alpha_Facing_Location.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic3D data = b_dataDynamic3D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic3DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(u_globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveFixedData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed3D data = b_dataFixed3D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchFixed3DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(u_globalTimerAlpha, data.alpha_LocationZ.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

void main() {
	mat4 currentMatrix;
	vec4 entityColor;
	vec4 entityEmissiveColor;
	f_instanceState(currentMatrix, entityColor, entityEmissiveColor);
	entityEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(u_statePackage[EMISSIVE_SA].y), u_statePackage[EMISSIVE_SA].x));
	vec2 startUV;
	vec2 endUV;
	f_uvMapping(startUV, endUV);
	vec2 uvs[] = vec2[4](startUV, vec2(endUV.x, startUV.y), vec2(startUV.x, endUV.y), endUV);
	vec3 T = normalize(currentMatrix[0].xyz);
	vec3 N = normalize(currentMatrix[2].xyz);

	vfb_data.fragUV = uvs[gl_VertexID];
	vfb_data.fragTBN = mat3(T, cross(T, N), N);
	vfb_data.fragEntityColor = entityColor;
	vfb_data.fragMixEmissive = entityEmissiveColor;
	vec4 vertexPos = currentMatrix * vec4(a_vertex * u_statePackage[ENTITY_STATE].zw, 0.0, 1.0);
	vfb_data.fragPos = vertexPos.xyz;
	vertexPos = gameViewport * vertexPos;
	if (max(entityColor.w, entityEmissiveColor.w) <= 0.0) vertexPos.xyz = vec3(-65536.0);
	gl_Position = vertexPos;
}
