#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2

layout (location = 0) in vec2 nodeLocation;
layout (location = 1) in vec2 nodeTangent;
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
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(interpolationFloat + 1, texturePixels, reversed, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];
uniform float globalTimerAlpha;

out VERT_TESC_BLOCK {
	flat mat4 tescMatrix;
	flat vec2 tescPoint;
	flat vec4 tescColor;
	flat vec4 tescEmissiveColor;
	flat float tescWidth;
	flat float tescMixFactor;
	flat float tescDistance;
} vtb_data;

void main()
{
	vec4 entityColor = statePackage[COLOR] * nodeColor * globalTimerAlpha;
	vec4 entityEmissiveColor = statePackage[EMISSIVE_COLOR] * nodeEmissive * globalTimerAlpha;

	vtb_data.tescMatrix = modelMatrix;
	vtb_data.tescPoint = nodeLocation + nodeTangent;
	vtb_data.tescColor = entityColor;
	vtb_data.tescEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(statePackage[EMISSIVE_SA].y), statePackage[EMISSIVE_SA].x));
	vtb_data.tescWidth = nodeWidth;
	vtb_data.tescMixFactor = nodeMixFactor;
	vtb_data.tescDistance = nodeDistance;
	gl_Position = vec4(nodeLocation, 0.0, 1.0);
}
