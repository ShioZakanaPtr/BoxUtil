#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2

layout (location = 0) in vec2 a_nodeLocation;
layout (location = 1) in vec2 a_nodeTangent;
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
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, reversed, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 u_statePackage[5];
uniform float u_globalTimerAlpha;

out VERT_TESC_BLOCK {
	flat mat4 tescMatrix;
	flat vec2 tescPoint;
	flat vec4 tescColor;
	flat vec4 tescEmissiveColor;
	flat float tescWidth;
	flat float tescMixFactor;
	flat float tescDistance;
} vtb_data;

void main() {
	vec4 entityColor = u_statePackage[COLOR] * a_nodeColor * u_globalTimerAlpha;
	vec4 entityEmissiveColor = u_statePackage[EMISSIVE_COLOR] * a_nodeEmissive * u_globalTimerAlpha;

	vtb_data.tescMatrix = u_modelMatrix;
	vtb_data.tescPoint = a_nodeLocation + a_nodeTangent;
	vtb_data.tescColor = entityColor;
	vtb_data.tescEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(u_statePackage[EMISSIVE_SA].y), u_statePackage[EMISSIVE_SA].x));
	vtb_data.tescWidth = a_nodeWidth;
	vtb_data.tescMixFactor = a_nodeMixFactor;
	vtb_data.tescDistance = a_nodeDistance;
	gl_Position = vec4(a_nodeLocation, 0.0, 1.0);
}
