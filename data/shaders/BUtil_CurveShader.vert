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
