#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define CURVE_STATE 3

subroutine void t_instanceStateCompute(out mat4 model, out vec4 mColor, out vec4 mEColor);
subroutine uniform t_instanceStateCompute f_instanceState;

layout (location = 0) in vec2 a_nodeLocation;
layout (location = 1) in vec4 a_nodeTangent;
layout (location = 2) in float a_nodeWidth;
layout (location = 3) in float a_nodeMixFactor;
layout (location = 4) in float a_nodeDistance;
layout (location = 5) in vec4 a_nodeColor;
layout (location = 6) in vec4 a_nodeEmissive;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform mat4 u_modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 u_statePackage[5];
uniform float u_globalTimerAlpha;
uniform int u_instanceOffset;

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

subroutine(t_instanceStateCompute) void p_noneData(out mat4 model, out vec4 mColor, out vec4 mEColor) {
	model = u_modelMatrix;
	mColor = u_statePackage[COLOR] * u_globalTimerAlpha;
	mEColor = u_statePackage[EMISSIVE_COLOR] * u_globalTimerAlpha;
}

subroutine(t_instanceStateCompute) void p_haveData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic2D data = b_dataDynamic2D[u_instanceOffset + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic2DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(u_globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveFixedData2D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed2D data = b_dataFixed2D[u_instanceOffset + gl_InstanceID];
    model = u_modelMatrix * fetchFixed2DMatrix(data);

    decodeFixedColor(data.colorBits, pickInstanceTimer(u_globalTimerAlpha, data.alpha_Facing_Location.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Dynamic3D data = b_dataDynamic3D[u_instanceOffset + gl_InstanceID];
    model = u_modelMatrix * fetchDynamic3DMatrix(data);

    decodeDynamicColor(data.colorBits, pickInstanceTimer(u_globalTimerAlpha, data.timer.x), mColor, mEColor);
    mColor *= u_statePackage[COLOR];
    mEColor *= u_statePackage[EMISSIVE_COLOR];
}

subroutine(t_instanceStateCompute) void p_haveFixedData3D(out mat4 model, out vec4 mColor, out vec4 mEColor) {
    Fixed3D data = b_dataFixed3D[u_instanceOffset + gl_InstanceID];
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
	entityColor *= a_nodeColor;
	entityEmissiveColor *= a_nodeEmissive;

	vtb_data.tescMatrix = currentMatrix;
	vtb_data.tescPoints = a_nodeLocation.xyxy + a_nodeTangent;
	vtb_data.tescColor = entityColor;
	vtb_data.tescEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(u_statePackage[EMISSIVE_SA].y), u_statePackage[EMISSIVE_SA].x));
	vtb_data.tescWidth = a_nodeWidth;
	vtb_data.tescMixFactor = a_nodeMixFactor;
	vtb_data.tescID = gl_VertexID == 0 ? 0.0 : ceil(float(gl_VertexID) / 2.0);
	vtb_data.tescDistance = a_nodeDistance;
	gl_Position = vec4(a_nodeLocation, 0.0, 1.0);
}
