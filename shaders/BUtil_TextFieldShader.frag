#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

uniform sampler2D fontMap[4];
uniform vec4 globalColor[2];
uniform uint dataBit;
uniform int blendBloom;

in GEOM_FRAG_BLOCK {
    vec3 fragUV;
    flat vec2 fragStyleUV; // strikeoutUV, underlineUV
    flat uint fragIsEdge;

    flat vec4 fragColor;
    flat uvec3 fragStyleState; // fuck intel
    flat uvec3 fragState; // cahnnel, texIndex, reserved
} gfb_data;

layout (location = 0) out vec4 fragColor; // draw to RGB8
layout (location = 1) out vec4 fragEmissive; // draw to RGB8
layout (location = 6) out uvec4 fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.

float getUnderline(in float uv, in float offset) {
    return smoothstep(0.04, 0.03, abs(uv - offset));
}

float getStrikeout(in float uv, in float offset) {
    return smoothstep(0.055, 0.04, abs(uv - offset));
}

void main()
{
    vec4 result = texture(fontMap[gfb_data.fragState.y], gfb_data.fragUV.xy);
    if (gfb_data.fragState.x == 4u) result = vec4(result.x);
    if (gfb_data.fragState.x == 8u) result = vec4(result.y);
    if (gfb_data.fragState.x == 12u) result = vec4(result.z);
    if (gfb_data.fragState.x == 16u) result = vec4(result.w);
    if (bool(gfb_data.fragIsEdge)) result = vec4(1.0, 1.0, 1.0, 0.0);
    if (bool(gfb_data.fragStyleState.y)) result = max(result, vec4(getUnderline(gfb_data.fragUV.z, gfb_data.fragStyleUV.y)));
    if (bool(gfb_data.fragStyleState.z)) result = max(result, vec4(getStrikeout(gfb_data.fragUV.z, gfb_data.fragStyleUV.x)));
    if (bool(gfb_data.fragStyleState.x)) result = vec4(1.0, 1.0, 1.0, 1.0 - result.w);
    result *= gfb_data.fragColor;
    if (bool(gfb_data.fragState.z)) result = vec4(gfb_data.fragColor.xyz, 0.0);
    result.w = min(result.w, 1.0);
    float cullAlpha = result.w;
    bool invalidFrag = result.w <= 0.0;
    if (invalidFrag) discard;
    fragColor = result;
    if (bool(blendBloom)) result.xyz *= globalColor[1].xyz; else result.xyz = globalColor[1].xyz;
    result.w = min(result.w * globalColor[1].w, 1.0);
    fragEmissive = result;
    fragData = (invalidFrag) ? uvec4(uvec3(0u), 1u) : uvec4(uvec2(vec2(1.0 - clamp(gl_FragCoord.z, 0.0, 1.0), cullAlpha) * 1023.0), dataBit, 0u);
}
