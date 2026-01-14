#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define COM_STATE 3
#define FILL_DATA 4
#define WIDTH_DATA 5
#define START_COLOR 6
#define END_COLOR 7
#define START_EMISSIVE 8
#define END_EMISSIVE 9

subroutine void lineMode(out vec2 posOut, out vec2 normalOut, out vec4 entityColorOut, out vec4 entityEmissiveColorOut, out float factorOut, out float widthOut, out float distanceOut);
subroutine uniform lineMode lineModeState;

layout (location = 0) in vec2 disabled;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

// vec2(point)
layout (binding = 10) uniform samplerBuffer nodeMap;
uniform mat4 modelMatrix;
// vec4(color), vec4(emissiveColor), vec4(emissiveState, anisotropic), vec4(time, nodeCount, texturePixels, mixFactor), vec4(fillStart, fillEnd, startFactor, endFactor), vec4(startWidth, endWidth, jitterPower, flickerMix)
// 6: vec4(start), 7: vec4(end), 8: vec4(startEmissive), 9: vec4(endEmissive)
uniform vec4 statePackage[10];
// hashCode, hashCodeTime, globalTimerAlpha
uniform vec3 extraData;

out VERT_GEOM_BLOCK {
	flat mat4 geomMatrix;
	flat vec4 geomColor;
	flat vec4 geomEmissiveColor;
	flat vec4 geomNormalWidthDistance;
	flat float geomFactor;
} vgb_data;

float flickRandom(in float seed) {
	vec2 tanSeed = tan(vec2(seed) * vec2(0.42, 4.2) + vec2(12.7, 51.97));
	float result = fract(smoothstep(1.0, 0.0, sin(tanSeed.x) + abs(tanSeed.y)) * 2.0);
	return 1.0 - sqrt(result);
}

float getFlick() {
	float seed = abs(extraData.y);
	if (extraData.y < 0.0) seed += float(gl_InstanceID << 1);
	return (statePackage[WIDTH_DATA].w <= -1.0f) ? 1.0 : mix(1.0, flickRandom(seed), statePackage[WIDTH_DATA].w);
}

subroutine(lineMode) void lineStripMode(out vec2 posOut, out vec2 normalOut, out vec4 entityColorOut, out vec4 entityEmissiveColorOut, out float factorOut, out float widthOut, out float distanceOut) {
	int currIndex = gl_InstanceID + gl_VertexID;
	float factor = clamp(float(currIndex) / statePackage[COM_STATE].y, 0.0, 1.0);
	float factorPow = pow(factor, statePackage[COM_STATE].w);
	vec3 rawNodeData = texelFetch(nodeMap, currIndex).xyz;
	int leftIndex = max(currIndex - 1, 0);
	int rightIndex = min(currIndex + 1, int(statePackage[COM_STATE].y));
	vec2 leftNormal = rawNodeData.yx - texelFetch(nodeMap, leftIndex).yx;
	if (leftIndex != currIndex) {
		leftNormal.x = -leftNormal.x;
		leftNormal = normalize(leftNormal);
	} else leftNormal = vec2(0.0);
	vec2 rightNormal = texelFetch(nodeMap, rightIndex).yx - rawNodeData.yx;
	if (rightIndex != currIndex) {
		rightNormal.x = -rightNormal.x;
		rightNormal = normalize(rightNormal);
	} else rightNormal = vec2(0.0);

	posOut = rawNodeData.xy;
	normalOut = normalize(leftNormal + rightNormal);
	entityColorOut = mix(statePackage[START_COLOR], statePackage[END_COLOR], factorPow);
	entityEmissiveColorOut = mix(statePackage[START_EMISSIVE], statePackage[END_EMISSIVE], factorPow);
	factorOut = factor;
	widthOut = mix(statePackage[WIDTH_DATA].x, statePackage[WIDTH_DATA].y, factorPow);
	distanceOut = rawNodeData.z;
}

subroutine(lineMode) void linesMode(out vec2 posOut, out vec2 normalOut, out vec4 entityColorOut, out vec4 entityEmissiveColorOut, out float factorOut, out float widthOut, out float distanceOut) {
	bool pickStart = gl_VertexID == 0;
	int currIndex = (gl_InstanceID << 1) + gl_VertexID;
	vec3 rawNodeData = texelFetch(nodeMap, currIndex).xyz;
	if (pickStart) ++currIndex; else --currIndex;
	vec3 twinNodeData = texelFetch(nodeMap, currIndex).xyz;
	vec2 normal = rawNodeData.yx - twinNodeData.yx;
	normal.x = -normal.x;

	posOut = rawNodeData.xy;
	normalOut = normalize(pickStart ? -normal : normal);
	factorOut = float(gl_VertexID);
	if (pickStart) {
		entityColorOut = statePackage[START_COLOR];
		entityEmissiveColorOut = statePackage[START_EMISSIVE];
		widthOut = statePackage[WIDTH_DATA].x;
		distanceOut = 0.0;
	} else {
		entityColorOut = statePackage[END_COLOR];
		entityEmissiveColorOut = statePackage[END_EMISSIVE];
		widthOut = statePackage[WIDTH_DATA].y;
		distanceOut = rawNodeData.z - twinNodeData.z;
	}
}

void main()
{
	vec4 entityColorOut, entityEmissiveColorOut;
	vec2 posOut, normalOut;
	float factorOut, widthOut, distanceOut;
	lineModeState(posOut, normalOut, entityColorOut, entityEmissiveColorOut, factorOut, widthOut, distanceOut);

	float flicker = getFlick() * extraData.z;
	vec4 entityColor = entityColorOut * statePackage[COLOR] * flicker;
	vec4 entityEmissiveColor = entityEmissiveColorOut * statePackage[EMISSIVE_COLOR] * flicker;

	vgb_data.geomMatrix = modelMatrix;
	vgb_data.geomColor = entityColor;
	vgb_data.geomEmissiveColor = mix(entityEmissiveColor, entityEmissiveColor * entityColor, vec4(vec3(statePackage[EMISSIVE_SA].y), statePackage[EMISSIVE_SA].x));
	vgb_data.geomNormalWidthDistance = vec4(normalOut, widthOut, distanceOut);
	vgb_data.geomFactor = factorOut;
	gl_Position = vec4(posOut, 0.0, 1.0);
}
