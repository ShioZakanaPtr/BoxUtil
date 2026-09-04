#version 430

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2

subroutine void t_instanceStateCompute(out mat4 model, out vec4 mColor, out vec4 mEColor);
subroutine uniform t_instanceStateCompute f_instanceState;

layout (location = 0) in vec3 a_vertex;
layout (location = 1) in vec3 a_normal;
layout (location = 2) in vec2 a_uv;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 u_modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(alphaMix, colorMix, glowPower, anisotropic), vec4(lightColor), vec4(shadowColor), vec4(lightDirection, alpha)
uniform vec4 u_statePackage[6];
uniform vec3 u_baseSize;
uniform uvec3 u_additionEmissive_DataBit_InstanceOffset;

layout (binding = 8) uniform samplerBuffer u_vertexData_TangentanceOffset;

out VERTEX_BLOCK {
	mat3 fragTBN;
	vec2 fragUV;
	vec3 fragPos;
	vec4 fragEntityColor;
	vec4 fragMixEmissive;
} vfb_data;

#include "BUtil_InstanceDataSSBO.h"

subroutine(t_instanceStateCompute) void p_noneData(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	model = u_modelMatrix;
	mColor = u_statePackage[COLOR] * u_statePackage[5].w;
	mEColor = u_statePackage[EMISSIVE_COLOR] * u_statePackage[5].w;
}

subroutine(t_instanceStateCompute) void p_haveData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	Dynamic2D data = b_dataDynamic2D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic2DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(u_statePackage[5].w, data.timer.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveFixedData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed2D data = b_dataFixed2D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchFixed2DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(u_statePackage[5].w, data.alpha_Facing_Location.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic3D data = b_dataDynamic3D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic3DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(u_statePackage[5].w, data.timer.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveFixedData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed3D data = b_dataFixed3D[int(u_additionEmissive_DataBit_InstanceOffset.z) + gl_InstanceID];
    model = u_modelMatrix * fetchFixed3DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(u_statePackage[5].w, data.alpha_LocationZ.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

void main() {
	mat4 currentMatrix;
	vec4 entityColor;
	vec4 entityEmissiveColor;
	f_instanceState(currentMatrix, entityColor, entityEmissiveColor);
	entityEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(u_statePackage[EMISSIVE_SA].y), u_statePackage[EMISSIVE_SA].x));
	int tangentIndex = int(float(gl_VertexID) * 0.3333333);
	mat3 modelRS = mat3(currentMatrix);
	vec3 tangent = texelFetch(u_vertexData_TangentanceOffset, tangentIndex).xyz;
	vec3 T = normalize(modelRS * tangent);
	vec3 N = normalize(modelRS * a_normal);
	T = normalize(T - dot(T, N) * N);

	vfb_data.fragUV = a_uv;
	vfb_data.fragEntityColor = entityColor;
	vfb_data.fragMixEmissive = entityEmissiveColor;
	vfb_data.fragTBN = mat3(T, cross(T, N), N);
	vec4 vertexPos = currentMatrix * vec4(a_vertex * u_baseSize, 1.0);
	vfb_data.fragPos = vertexPos.xyz;
	vertexPos = gameViewport * vertexPos;
	vertexPos.z /= u_baseSize.z;
	if (max(entityColor.w, entityEmissiveColor.w) <= 0.0) vertexPos.xyz = vec3(-65536.0);
	gl_Position = vertexPos;
}
