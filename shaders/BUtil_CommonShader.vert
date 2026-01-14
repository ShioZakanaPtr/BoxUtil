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
