#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define SHARP_EDGE_SMOOTH 0.42
#define DISC_THICKNESS_SCALE 2.0
#define SHARP_DISC_THICKNESS_SCALE 4.0
#define STATE_A 2
#define STATE_B 3
#define STATE_EXT 4

subroutine vec4 flareStateDraw(in vec2 uv);
subroutine uniform flareStateDraw flareState;

in VERTEX_BLOCK {
    vec2 fragUV;
    flat vec4 fragFringeColor;
    flat vec4 fragCoreColor;
    flat vec2 fragNoiseOffsetAlpha;
} vb_data;

// vec4(fringeColor), vec4(coreColor), vec4(size, aspect, flick/syncFlick), vec4(alpha, hashCode, glowPower, frameAmount), vec4(noisePower, flickMix, globalAlpha, discRatio)
uniform vec4 statePackage[5];
uniform uint dataBit;

layout (location = 0) out vec4 fragColor; // draw to RGB8
layout (location = 1) out vec4 fragEmissive; // draw to RGB8
layout (location = 6) out uvec4 fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.

float hash(in float p) {
    float f = fract(p * 0.011);
    f *= f + 7.5;
    return fract(f * (f + f));
}

float noise(in float x) {
    float f = fract(x);
    return mix(hash(x), hash(x + 1.0), f * f * (3.0 - 2.0 * f));
}

float fbm(in float x) {
    float v = 0.0;
    float a = 0.5;
    float f = x;
    for (int i = 0; i < 3; i++) {
        v += a * noise(f);
        f = f * 2.0 + 100.0;
        a *= 0.5;
    }
    return v;
}

float fi(in float a, in float b) {
    return 1.0 - (1.0 - a) * (1.0 - b);
}

subroutine(flareStateDraw) vec4 smoothMode(in vec2 uv) {
    float fringe = smoothstep(1.0, 0.0, length(uv)) * 2.0;
    float core = smoothstep(1.0, 0.0, length(uv * 2.0));
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    return mix(fringeColor, vb_data.fragCoreColor, core);
}

subroutine(flareStateDraw) vec4 sharpMode(in vec2 uv) {
    vec2 uvAbs = abs(uv);
    float fringe = smoothstep(1.0, 0.0, uvAbs.x) * 2.0;
    float core = smoothstep(1.0, 0.0, uvAbs.x * 2.0);
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    vec4 resultColor = mix(fringeColor, vb_data.fragCoreColor, core);
    resultColor.w *= smoothstep(0.5, SHARP_EDGE_SMOOTH, uvAbs.y);
    return resultColor;
}

subroutine(flareStateDraw) vec4 smoothDiscMode(in vec2 uv) {
    vec2 coreUV = vec2(uv.x * statePackage[STATE_A].z, uv.y);
    vec2 discUV = vec2(uv.x, uv.y * statePackage[STATE_EXT].w * DISC_THICKNESS_SCALE);
    float fringe = fi(smoothstep(1.0, 0.0, length(coreUV)), smoothstep(1.0, 0.0, length(discUV)));
    float core = fi(smoothstep(1.0, 0.0, length(coreUV * 2.0)), smoothstep(1.0, 0.0, length(discUV * 2.0)));
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    return mix(fringeColor, vb_data.fragCoreColor, core);
}

subroutine(flareStateDraw) vec4 sharpDiscMode(in vec2 uv) {
    vec2 uvAbs = abs(uv);
    vec2 coreUV = vec2(uv.x * statePackage[STATE_A].z, uv.y);
    vec2 discUV = vec2(uvAbs.x, uvAbs.y * statePackage[STATE_EXT].w * SHARP_DISC_THICKNESS_SCALE);
    vec2 discMask = vec2(smoothstep(1.0, 0.0, discUV.x), smoothstep(1.0, 0.0, discUV.x * 2.0)) * smoothstep(0.5, SHARP_EDGE_SMOOTH, discUV.y);
    float fringe = fi(smoothstep(1.0, 0.0, length(coreUV)), discMask.x);
    float core = fi(smoothstep(1.0, 0.0, length(coreUV * 2.0)), discMask.y);
    vec4 fringeColor = vb_data.fragFringeColor;
    fringeColor.w *= fringe;
    return mix(fringeColor, vb_data.fragCoreColor, core);
}

void main()
{
    vec2 uv = vb_data.fragUV;
    uv.x *= smoothstep(0.0, 1.0, fbm(uv.y * 0.5 + statePackage[STATE_B].w * 10.0 + vb_data.fragNoiseOffsetAlpha.x)) * statePackage[STATE_EXT].x + 1.0;;
    vec4 finalColor = flareState(uv);
    finalColor.w = min(vb_data.fragNoiseOffsetAlpha.y * finalColor.w, 1.0);
    bool invalidFrag = finalColor.w <= 0.0;
    if (invalidFrag) discard;
    fragColor = finalColor;
    finalColor.xyz *= statePackage[STATE_B].z;
    fragEmissive = finalColor;
    fragData = (invalidFrag) ? uvec4(uvec3(0u), 1u) : uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), finalColor.w) * 1023.0), dataBit, 0u);
}
