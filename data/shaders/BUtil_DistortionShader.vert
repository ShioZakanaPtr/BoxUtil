#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define INOUT_FACTOR 2
#define STATE 4

subroutine void t_instanceStateCompute(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam);
subroutine uniform t_instanceStateCompute f_instanceState;

layout (location = 0) in vec2 a_vertex;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 u_modelMatrix;
// vec4(sizeIn, powerIn, powerFull), vec4(sizeFull, powerOut, hardnessRing), vec4(sizeOut, fadeInFactor, fadeOutFactor)
// vec4(sizeInRatio, sizeFullRatio), vec4(sizeOutRatio, hardnessInner, globalTimerRaw), vec4(arcStart, arcEnd, innerCenter)
uniform vec4 u_statePackage[6];
uniform int u_instanceDataOffset;

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
    if (timer == TIMER_IN) getPowValue = u_statePackage[INOUT_FACTOR].z;
    if (timer == TIMER_OUT) getPowValue = u_statePackage[INOUT_FACTOR].w;
    return (timer == TIMER_FULL) ? 1.0 : pow(decodeAlpha(alphaRaw), getPowValue);
}

subroutine(t_instanceStateCompute) void p_noneData(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
	model = u_modelMatrix;
	float check = u_statePackage[STATE].w;
	vec2 tmp = vec2(1.0);
	uint tmpU = TIMER_FULL;
	if (check > 2.0) {
		tmp = vec2(abs(3.0 - check), u_statePackage[INOUT_FACTOR].z);
		tmpU = TIMER_IN;
	}
	if (check < 1.0 && check > -500.0) {
		tmp = vec2(check, u_statePackage[INOUT_FACTOR].w);
		tmpU = TIMER_OUT;
	}
	timerState = tmpU;
	mixFactor = (tmpU == TIMER_FULL) ? 1.0 : pow(tmp.x, tmp.y);

    colorParam = vec4(1.0);
    emissiveParam = vec4(1.0);
}

subroutine(t_instanceStateCompute) void p_haveData2D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Dynamic2D data = b_dataDynamic2D[u_instanceDataOffset + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic2DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE].w, data.timer.x);
	uint timer = getTimerState(pickTimer);
    timerState = timer;
	mixFactor = getMixFactor(timer, pickTimer);

    decodeDynamicColor(data.colorBits, pickTimer, colorParam, emissiveParam);
}

subroutine(t_instanceStateCompute) void p_haveFixedData2D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Fixed2D data = b_dataFixed2D[u_instanceDataOffset + gl_InstanceID];
    model = u_modelMatrix * fetchFixed2DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE].w, data.alpha_Facing_Location.x);
    uint timer = getTimerState(pickTimer);
    timerState = timer;
    mixFactor = getMixFactor(timer, pickTimer);

    decodeFixedColor(data.colorBits, pickTimer, colorParam, emissiveParam);
}

subroutine(t_instanceStateCompute) void p_haveData3D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Dynamic3D data = b_dataDynamic3D[u_instanceDataOffset + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic3DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE].w, data.timer.x);
    uint timer = getTimerState(pickTimer);
    timerState = timer;
    mixFactor = getMixFactor(timer, pickTimer);

    decodeDynamicColor(data.colorBits, pickTimer, colorParam, emissiveParam);
}

subroutine(t_instanceStateCompute) void p_haveFixedData3D(out mat4 model, out uint timerState, out float mixFactor, out vec4 colorParam, out vec4 emissiveParam) {
    Fixed3D data = b_dataFixed3D[u_instanceDataOffset + gl_InstanceID];
    model = u_modelMatrix * fetchFixed3DMatrix(data);

    float pickTimer = pickInstanceTimer(u_statePackage[STATE].w, data.alpha_LocationZ.x);
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
	f_instanceState(currentMatrix, mixStateValue, mixFactorValue, colorParam, emissiveParam);

	vec4 size = vec4(u_statePackage[1].xy * emissiveParam.y, u_statePackage[3].zw * colorParam.y), sizeMix = size;
	float power = u_statePackage[0].w, powerMix = power;
	if (mixStateValue == TIMER_IN) {
		sizeMix = vec4(u_statePackage[0].xy * emissiveParam.x, u_statePackage[3].xy * colorParam.x);
		powerMix = u_statePackage[0].z;
	}
	if (mixStateValue == TIMER_OUT) {
		sizeMix = vec4(u_statePackage[2].xy * emissiveParam.z, u_statePackage[4].xy * colorParam.z);
		powerMix = u_statePackage[1].z;
	}
    if (mixStateValue != TIMER_FULL) {
        size = mix(sizeMix, size, mixFactorValue);
        power = mix(powerMix, power, mixFactorValue);
    }
    power *= colorParam.w;

	vec4 location = currentMatrix * vec4(a_vertex * size.xy, 0.0, 1.0);
	vec2 locationSize = abs(location.xy - currentMatrix[3].xy);
	vec4 currPos = gameViewport * location;
	if (power == 0.0) currPos.xyz = vec3(-65536.0);
	gl_Position = currPos;
	vb_data.fragUVMask = vec4(a_vertex, a_vertex / size.zw - u_statePackage[5].zw);
	vb_data.fragUVScreen = vec4(getUV(location.xy), locationSize.xy / gameScreenBorder.zw * a_vertex * power * 0.5);
	vb_data.fragGlobalHardness = emissiveParam.w;
}