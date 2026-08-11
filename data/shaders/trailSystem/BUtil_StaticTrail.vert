#version OVERWRITE_VERSION

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define TIMER_STATE 5

#define TRAIL_VERSION_TITLE

#ifdef LEGACY_TRAIL_MODE
#extension ARB_EXTENSION_TITLE : enable

in vec2 a_position;
in vec2 a_facingVector;
in uint a_timeStampRaw;
in float a_distance;
#else
layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_facingVector;
layout (location = 2) in uint a_timeStampRaw;
layout (location = 3) in float a_distance; // -1.0f for aux-point
#endif

// 0: vec4(color)
// 1: vec4(emissiveColor)
// 2: vec4(alphaMix, colorMix, glowPower, anisotropic)
// 3: vec4(colorIn)
// 4: vec4(colorOut)
// 5: vec4(fadeIn, full, fadeOut, randomUVStart)
// 6: vec4(sizeIn, sizeOut, texPixelsDiv, texSpeed)
// 7: vec4(velInRange)
// 8: vec4(velOutRange)
// 9: vec4(angluarInRange, angluarOutRange)
uniform vec4 u_statePackage[10];
uniform float u_time;

out VERT_GEOM_BLOCK {
    vec4 geomEntityColor;
    vec4 geomMixEmissive;
    float geomDistance;
    // vec4 geomPos_UV_Width; // as gl_Position
} vgb_data;

float hash31(in vec2 a, in float b) {
    vec3 p3 = fract(vec3(a, b) * 0.1031);
    p3 += dot(p3, p3.zyx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

void decodeTimeStamp(out float rndValue, out float timeStamp) {
    rndValue = float((a_timeStampRaw >> 29u) & 7u) * 0.125;
    uint rawBits = a_timeStampRaw & 536870911u;
    timeStamp = uintBitsToFloat(rawBits | (((rawBits & 268435456u) > 0u) ? 536870912u : 1073741824u));
}

vec2 currPositionOffset(in float rndSeed, in float elapsedTime, in float life) {
    float rndPos = hash31(a_position, rndSeed);
    vec2 angluarRange = mix(u_statePackage[9].xy, u_statePackage[9].zw, life);
    float currSpin = radians(mix(angluarRange.x, angluarRange.y, rndPos) * elapsedTime), spinC = cos(currSpin), spinS = sin(currSpin);
    vec4 velRange = mix(u_statePackage[7], u_statePackage[8], life);
    return mat2(spinC, -spinS, spinS, spinC) * mat2(a_facingVector.x, -a_facingVector.y, a_facingVector.yx) * mix(velRange.xy, velRange.zw, rndPos);
}

void main() {
    if (a_distance < 0.0f) {
        vgb_data.geomEntityColor = u_statePackage[COLOR];
        vgb_data.geomMixEmissive = u_statePackage[EMISSIVE_COLOR];
        gl_Position = vec4(-1.0);
        return;
    }

    float rndID, timeStamp;
    decodeTimeStamp(rndID, timeStamp);
    float elapsedTime = u_time - timeStamp;
    float intoFadeOutLife = u_statePackage[TIMER_STATE].x + u_statePackage[TIMER_STATE].y;
    float totalLife = intoFadeOutLife + u_statePackage[TIMER_STATE].z;
    float life = clamp(elapsedTime / totalLife, 0.0, 1.0);

    float nodeUV = abs(fract(a_distance - u_statePackage[6].w * elapsedTime) * u_statePackage[6].z);
    if (u_statePackage[5].w > 0.0) nodeUV += rndID;
    vec4 nodeColor = mix(u_statePackage[3], u_statePackage[4], life);
    vec2 currPosition = a_position + currPositionOffset(timeStamp * rndID, elapsedTime, life);

    if (elapsedTime < u_statePackage[TIMER_STATE].x) nodeColor.w *= elapsedTime / u_statePackage[TIMER_STATE].x;
    if (elapsedTime > intoFadeOutLife) nodeColor.w = 1.0 - min((elapsedTime - intoFadeOutLife) / u_statePackage[TIMER_STATE].z, 1.0);

    vgb_data.geomEntityColor = nodeColor * u_statePackage[COLOR];
    vgb_data.geomMixEmissive = nodeColor * mix(u_statePackage[EMISSIVE_COLOR], u_statePackage[EMISSIVE_COLOR] * u_statePackage[COLOR], vec4(vec3(u_statePackage[EMISSIVE_SA].y), u_statePackage[EMISSIVE_SA].x));
    vgb_data.geomDistance = a_distance;
    gl_Position = vec4(currPosition, nodeUV, mix(u_statePackage[6].x, u_statePackage[6].y, life));
}