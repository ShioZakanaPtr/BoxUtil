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