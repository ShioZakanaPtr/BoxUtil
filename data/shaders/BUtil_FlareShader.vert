#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define FRINGE_COLOR 0
#define CORE_COLOR 1
#define STATE_A 2
#define STATE_B 3
#define STATE_EXT 4

subroutine void t_instanceStateCompute(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker);
subroutine uniform t_instanceStateCompute f_instanceState;

layout (location = 0) in vec2 a_vertex;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 u_modelMatrix;
// vec4(fringeColor), vec4(coreColor), vec4(size, aspect, flick/syncFlick), vec4(alpha, hashCode, glowPower, frameAmount), vec4(noisePower, flickMix, globalAlpha, discRatio)
uniform vec4 u_statePackage[5];
uniform int u_instanceOffset;

out VERTEX_BLOCK {
	vec2 fragUV;
	flat vec4 fragFringeColor;
	flat vec4 fragCoreColor;
	flat vec2 fragNoiseOffsetAlpha;
} vfb_data;

#include "BUtil_InstanceDataSSBO.h"

float flickRandom(in float seed) {
	vec2 tanSeed = tan(vec2(seed) * vec2(0.42, 4.2) + vec2(12.7, 51.97));
	float result = fract(smoothstep(1.0, 0.0, sin(tanSeed.x) + abs(tanSeed.y)) * 2.0);
	return 1.0 - sqrt(result);
}

float getFlick() {
	uint flickState = uint(u_statePackage[STATE_A].w);
	float flickOffset = ((flickState & 1u) == 1u) ? u_statePackage[STATE_B].y : (float(gl_InstanceID << 2) - u_statePackage[STATE_B].y) * 0.01;
	return (flickState > 2u) ? mix(1.0, flickRandom(flickOffset + u_statePackage[STATE_B].w), u_statePackage[STATE_EXT].y) : 1.0;
}

subroutine(t_instanceStateCompute) void p_noneData(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
	model = u_modelMatrix;
    mCColor = u_statePackage[CORE_COLOR];
    mFColor = u_statePackage[FRINGE_COLOR];

	float flick = getFlick();
    flicker = flick;
	currAlpha = u_statePackage[STATE_B].x * flick;
}

subroutine(t_instanceStateCompute) void p_haveData2D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Dynamic2D data = b_dataDynamic2D[u_instanceOffset + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic2DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE_B].x, data.timer.x);
    decodeDynamicColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= u_statePackage[CORE_COLOR];
    mFColor *= u_statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

subroutine(t_instanceStateCompute) void p_haveFixedData2D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Fixed2D data = b_dataFixed2D[u_instanceOffset + gl_InstanceID];
    model = u_modelMatrix * fetchFixed2DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE_B].x, data.alpha_Facing_Location.x);
    decodeFixedColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= u_statePackage[CORE_COLOR];
    mFColor *= u_statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

subroutine(t_instanceStateCompute) void p_haveData3D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Dynamic3D data = b_dataDynamic3D[u_instanceOffset + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic3DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE_B].x, data.timer.x);
    decodeDynamicColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= u_statePackage[CORE_COLOR];
    mFColor *= u_statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

subroutine(t_instanceStateCompute) void p_haveFixedData3D(out mat4 model, out vec4 mCColor, out vec4 mFColor, out float currAlpha, out float flicker) {
    Fixed3D data = b_dataFixed3D[u_instanceOffset + gl_InstanceID];
    model = u_modelMatrix * fetchFixed3DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE_B].x, data.alpha_LocationZ.x);
    decodeFixedColor(data.colorBits, pickTimer, mCColor, mFColor);
    mCColor *= u_statePackage[CORE_COLOR];
    mFColor *= u_statePackage[FRINGE_COLOR];

    float flick = getFlick();
    flicker = flick;
    currAlpha = decodeAlpha(pickTimer) * flick;
}

void main() {
	mat4 currentMatrix;
	vec4 entityFringeColor;
	vec4 entityCoreColor;
	float entityAlpha;
	float entityFlicker;
	f_instanceState(currentMatrix, entityCoreColor, entityFringeColor, entityAlpha, entityFlicker);

	vfb_data.fragUV = a_vertex;
	vfb_data.fragFringeColor = entityFringeColor;
	vfb_data.fragCoreColor = entityCoreColor;
	vfb_data.fragNoiseOffsetAlpha = vec2(mod(length(vec3(currentMatrix[3].xyz)), 100.0), entityAlpha * u_statePackage[STATE_EXT].z);
	vec4 vertexPos = gameViewport * currentMatrix * vec4(a_vertex * vec2(fma(entityFlicker, 0.42, 0.58) * u_statePackage[STATE_A].x, u_statePackage[STATE_A].y), 0.0, 1.0);
	if (max(entityFringeColor.w, entityCoreColor.w) <= 0.0) vertexPos.xyz = vec3(-65536.0);
	gl_Position = vertexPos;
}
