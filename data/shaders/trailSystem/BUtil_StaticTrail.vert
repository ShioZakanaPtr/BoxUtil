#version OVERWRITE_VERSION_TITLE

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define TIMER_STATE 5

#define TRAIL_VERSION_TITLE

#ifdef LEGACY_TRAIL_MODE
#extension BIT_ENCODE_EXT_TITLE : require

in vec2 a_position;
in vec2 a_facingVector;
in vec4 a_nodeColor;
in uint a_timeStampRaw;
in uint a_uv;
#else
layout (location = 0) in vec2 a_position;
layout (location = 1) in vec2 a_facingVector;
layout (location = 2) in vec4 a_nodeColor;
layout (location = 3) in uint a_timeStampRaw; // 0u is absolutely aux-point
layout (location = 4) in uint a_uv; // uint for avoid symbol folding
#endif

// 0: vec4(color)
// 1: vec4(emissiveColor)
// 2: vec4(alphaMix, colorMix, glowPower, anisotropic)
// 3: vec4(colorIn)
// 4: vec4(colorOut)
// 5: vec4(fadeIn, full, fadeOut, randomUVStart)
// 6: vec4(sizeIn, sizeOut, smoothEnds, texUVScroll)
// 7: vec4(velInRange)
// 8: vec4(velOutRange)
// 9: vec4(angluarInRange, angluarOutRange)
uniform vec4 u_statePackage[10];
uniform float u_time;

#ifdef LEGACY_TRAIL_MODE
out vec4 vgb_geomEntityColor;
out vec4 vgb_geomMixEmissive;
out uvec4 vgb_geomSymbolID_Life_UV_Width;
#else
out VERT_GEOM_BLOCK {
    vec4 geomEntityColor;
    vec4 geomMixEmissive;
    uvec4 geomSymbolID_Life_UV_Width;
} vgb_data;
#endif

float hash31(in float a, in float b, in float c) {
    vec3 p3 = fract(vec3(a, b, c) * 0.1031);
    p3 += dot(p3, p3.zyx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

void decodeTimeStamp(out float rndValue, out float timeStamp) {
    rndValue = float((a_timeStampRaw >> 29u) & 7u) * 0.125;
    uint rawBits = a_timeStampRaw & 0x1fffffffu;
    timeStamp = uintBitsToFloat(rawBits | (((rawBits & 0x10000000u) > 0u) ? 0x20000000u : 0x40000000u));
}

vec2 currPositionOffset(in float rndSeed, in float elapsedTime, in float life) {
    float rndPos = hash31(a_position.x, a_position.y, rndSeed), rndPosSub = hash31(a_position.y, a_uv, a_position.x);
    vec2 angluarRange = mix(u_statePackage[9].xy, u_statePackage[9].zw, life);
    float currSpin = radians(mix(angluarRange.x, angluarRange.y, rndPos) * elapsedTime), spinC = cos(currSpin), spinS = sin(currSpin);
    vec4 velRange = mix(u_statePackage[7], u_statePackage[8], life);
    vec2 normFacingVec = normalize(a_facingVector);
    vec2 currOffset = mix(velRange.xy, velRange.zw, vec2(rndPos, rndPosSub));
    return mat2(spinC, spinS, -spinS, spinC) * mat2(normFacingVec, -normFacingVec.y, normFacingVec.x) * currOffset;
}

void main() {
    if (a_timeStampRaw == 0u) {
        uvec4 out_emptyNodeData = uvec4(a_uv & 0x80000000u, floatBitsToUint(vec3(-1024.0)));
#ifdef LEGACY_TRAIL_MODE
        vgb_geomEntityColor = u_statePackage[COLOR];
        vgb_geomMixEmissive = u_statePackage[EMISSIVE_COLOR];
        vgb_geomSymbolID_Life_UV_Width = out_emptyNodeData;
#else
        vgb_data.geomEntityColor = u_statePackage[COLOR];
        vgb_data.geomMixEmissive = u_statePackage[EMISSIVE_COLOR];
        vgb_data.geomSymbolID_Life_UV_Width = out_emptyNodeData;
#endif
        gl_Position = vec4(-1024.0);
        return;
    }

    float rndID, timeStamp;
    decodeTimeStamp(rndID, timeStamp);
    float elapsedTime = u_time - timeStamp;
    float intoFadeOutLife = u_statePackage[TIMER_STATE].x + u_statePackage[TIMER_STATE].y;
    float totalLife = intoFadeOutLife + u_statePackage[TIMER_STATE].z;
    float life = clamp(elapsedTime / totalLife, 0.0, 1.0);

    float nodeUV = uintBitsToFloat(a_uv & 0x7fffffffu) - (u_statePackage[6].w * elapsedTime);
    if (u_statePackage[5].w > 0.0) nodeUV += rndID;
    vec4 nodeColor = mix(u_statePackage[3], u_statePackage[4], life) * a_nodeColor;
    vec2 currPosition = a_position + currPositionOffset(timeStamp * rndID, elapsedTime, life);

    if (elapsedTime < u_statePackage[TIMER_STATE].x) nodeColor.w *= elapsedTime / u_statePackage[TIMER_STATE].x;
    if (elapsedTime > intoFadeOutLife) nodeColor.w *= 1.0 - min((elapsedTime - intoFadeOutLife) / u_statePackage[TIMER_STATE].z, 1.0);

    vec4 out_entityColor = nodeColor * u_statePackage[COLOR];
    vec4 out_mixEmissive = nodeColor * mix(u_statePackage[EMISSIVE_COLOR], u_statePackage[EMISSIVE_COLOR] * u_statePackage[COLOR], vec4(vec3(u_statePackage[EMISSIVE_SA].y), u_statePackage[EMISSIVE_SA].x));
    uvec4 out_nodeData = uvec4(a_uv & 0x80000000u, floatBitsToUint(vec3(life, nodeUV, mix(u_statePackage[6].x, u_statePackage[6].y, life))));
#ifdef LEGACY_TRAIL_MODE
    vgb_geomEntityColor = out_entityColor;
    vgb_geomMixEmissive = out_mixEmissive;
    vgb_geomSymbolID_Life_UV_Width = out_nodeData;
#else
    vgb_data.geomEntityColor = out_entityColor;
    vgb_data.geomMixEmissive = out_mixEmissive;
    vgb_data.geomSymbolID_Life_UV_Width = out_nodeData;
#endif
    gl_Position = vec4(currPosition, 0.0, 1.0);
}