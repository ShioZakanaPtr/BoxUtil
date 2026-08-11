#version OVERWRITE_VERSION

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define TIMER_STATE 5

#define TRAIL_VERSION_TITLE

layout (lines_adjacency) in;
layout (triangle_strip, max_vertices = 4) out;

#ifdef LEGACY_TRAIL_MODE
layout (std140) uniform BUtilGlobalData {
    mat4 b_gameViewport;
    vec4 b_gameScreenBorder; // vec4(screenLB, screenSize)
};
#else
layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData {
    mat4 b_gameViewport;
    vec4 b_gameScreenBorder; // vec4(screenLB, screenSize)
};
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

in VERT_GEOM_BLOCK {
    vec4 geomEntityColor;
    vec4 geomMixEmissive;
    float geomDistance;
} vgb_datas[];

out GEOM_FRAG_BLOCK {
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
#ifdef LEGACY_TRAIL_MODE
    vec2 fragUV;
#else
    vec4 fragUV_TBN;
    vec2 fragPosN;
#endif
} gfb_data;

#ifndef LEGACY_TRAIL_MODE
    vec2 toGBufferPos(in vec2 glRawPos) {
        return (glRawPos + 1.0) * 0.5;
    }
#endif

void main() {
    if (vgb_datas[1].geomDistance >= vgb_datas[2].geomDistance) return; // cutoff check

    bool isLeftAuxPoint = (gl_in[0].gl_Position.z < 0.0) || (vgb_datas[0].geomDistance >= vgb_datas[1].geomDistance);
    bool isRightAuxPoint = (gl_in[3].gl_Position.z < 0.0) || (vgb_datas[2].geomDistance >= vgb_datas[3].geomDistance);
    vec2 leftPos = gl_in[0].gl_Position.xy, startPos = gl_in[1].gl_Position.xy, endPos = gl_in[2].gl_Position.xy, rightPos = gl_in[3].gl_Position.xy;

    vec2 trailDir, midNormal, startNormal, endNormal;
    trailDir = normalize(endPos.xy - startPos.xy);

    midNormal = trailDir.yx;
    midNormal.x = -midNormal.x;
    if (!isLeftAuxPoint) {
        startNormal = startPos.yx - leftPos.yx;
        startNormal.x = -startNormal.x;
        startNormal = normalize(normalize(startNormal) + midNormal);
    }
    if (isRightAuxPoint) endNormal = startNormal; else {
        endNormal = rightPos.yx - endPos.yx;
        endNormal.x = -endNormal.x;
        endNormal = normalize(normalize(endNormal) + midNormal);
    }
    if (isLeftAuxPoint) startNormal = endNormal;
    startNormal *= gl_in[1].gl_Position.w;
    endNormal *= gl_in[2].gl_Position.w;

    gfb_data.fragEntityColor = vgb_datas[1].geomEntityColor;
    gfb_data.fragMixEmissive = vgb_datas[1].geomMixEmissive;
#ifdef LEGACY_TRAIL_MODE
    gfb_data.fragUV = vec2(gl_in[1].gl_Position.z, 1.0);
    gl_Position = b_gameViewport * vec4(startPos + startNormal, 0.0, 1.0);
    EmitVertex();

    gfb_data.fragUV.y = 0.0;
    gl_Position = b_gameViewport * vec4(startPos - startNormal, 0.0, 1.0);
    EmitVertex();
#else
    vec4 resultGLPos;
    gfb_data.fragUV_TBN = vec4(gl_in[1].gl_Position.z, 1.0, trailDir);
    resultGLPos = b_gameViewport * vec4(startPos + startNormal, 0.0, 1.0);
    gfb_data.fragPosN = toGBufferPos(resultGLPos.xy);
    gl_Position = resultGLPos;
    EmitVertex();

    gfb_data.fragUV_TBN.y = 0.0;
    resultGLPos = b_gameViewport * vec4(startPos - startNormal, 0.0, 1.0);
    gfb_data.fragPosN = toGBufferPos(resultGLPos.xy);
    gl_Position = resultGLPos;
    EmitVertex();
#endif

    gfb_data.fragEntityColor = vgb_datas[2].geomEntityColor;
    gfb_data.fragMixEmissive = vgb_datas[2].geomMixEmissive;
#ifdef LEGACY_TRAIL_MODE
    gfb_data.fragUV.x = gl_in[2].gl_Position.z;
    gfb_data.fragUV.y = 1.0;
    gl_Position = b_gameViewport * vec4(endPos + endNormal, 0.0, 1.0);
    EmitVertex();

    gfb_data.fragUV.y = 0.0;
    gl_Position = b_gameViewport * vec4(endPos - endNormal, 0.0, 1.0);
    EmitVertex();
    EndPrimitive();
#else
    gfb_data.fragUV_TBN.x = gl_in[2].gl_Position.z;
    gfb_data.fragUV_TBN.y = 1.0;
    resultGLPos = b_gameViewport * vec4(endPos + endNormal, 0.0, 1.0);
    gfb_data.fragPosN = toGBufferPos(resultGLPos.xy);
    gl_Position = resultGLPos;
    EmitVertex();

    gfb_data.fragUV_TBN.y = 0.0;
    resultGLPos = b_gameViewport * vec4(endPos - endNormal, 0.0, 1.0);
    gfb_data.fragPosN = toGBufferPos(resultGLPos.xy);
    gl_Position = resultGLPos;
    EmitVertex();
    EndPrimitive();
#endif
}