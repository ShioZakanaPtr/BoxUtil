#version OVERWRITE_VERSION_TITLE

#define COLOR 0
#define EMISSIVE_COLOR 1
#define EMISSIVE_SA 2
#define TIMER_STATE 5

#define TRAIL_VERSION_TITLE

#ifdef LEGACY_TRAIL_MODE
#extension BIT_ENCODE_EXT_TITLE : require
#extension UBO_EXT_TITLE : require
#extension GEOM_SHADER_EXT_TITLE : require

layout (std140) uniform BUtilGlobalData {
    mat4 b_gameViewport;
    vec4 b_gameScreenBorder; // vec4(screenLB, screenSize)
};
#else
layout (lines_adjacency) in;
layout (triangle_strip, max_vertices = 4) out;

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
// 6: vec4(sizeIn, sizeOut, smoothEnds, texUVScroll)
// 7: vec4(velInRange)
// 8: vec4(velOutRange)
// 9: vec4(angluarInRange, angluarOutRange)
uniform vec4 u_statePackage[10];
uniform float u_time;

#ifdef LEGACY_TRAIL_MODE
varying in vec4 vgb_geomEntityColor[];
varying in vec4 vgb_geomMixEmissive[];
varying in uvec4 vgb_geomSymbolID_Life_UV_Width[];

varying out vec4 gfb_fragEntityColor;
varying out vec4 gfb_fragMixEmissive;
varying out vec2 gfb_fragUV;
varying out float gfb_fragEndsAlpha;
#else
in VERT_GEOM_BLOCK {
    vec4 geomEntityColor;
    vec4 geomMixEmissive;
    uvec4 geomSymbolID_Life_UV_Width;
} vgb_datas[];

out GEOM_FRAG_BLOCK {
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
    vec4 fragUV_TBN;
    vec2 fragPosN;
    float fragEndsAlpha;
} gfb_data;

vec2 toGBufferPos(in vec2 glRawPos) {
    return (glRawPos + 1.0) * 0.5;
}
#endif

void main() {
#ifdef LEGACY_TRAIL_MODE
    vec2 leftPos = gl_PositionIn[0].xy,
        startPos = gl_PositionIn[1].xy,
        endPos = gl_PositionIn[2].xy,
        rightPos = gl_PositionIn[3].xy;
    uvec4 in_vgb_nodeData_0 = vgb_geomSymbolID_Life_UV_Width[0],
        in_vgb_nodeData_1 = vgb_geomSymbolID_Life_UV_Width[1],
        in_vgb_nodeData_2 = vgb_geomSymbolID_Life_UV_Width[2],
        in_vgb_nodeData_3 = vgb_geomSymbolID_Life_UV_Width[3];
    vec4 in_vgb_entityColor_1 = vgb_geomEntityColor[1],
        in_vgb_mixEmissive_1 = vgb_geomMixEmissive[1],
        in_vgb_entityColor_2 = vgb_geomEntityColor[2],
        in_vgb_mixEmissive_2 = vgb_geomMixEmissive[2];
#else
    vec2 leftPos = gl_in[0].gl_Position.xy,
        startPos = gl_in[1].gl_Position.xy,
        endPos = gl_in[2].gl_Position.xy,
        rightPos = gl_in[3].gl_Position.xy;
    uvec4 in_vgb_nodeData_0 = vgb_datas[0].geomSymbolID_Life_UV_Width,
        in_vgb_nodeData_1 = vgb_datas[1].geomSymbolID_Life_UV_Width,
        in_vgb_nodeData_2 = vgb_datas[2].geomSymbolID_Life_UV_Width,
        in_vgb_nodeData_3 = vgb_datas[3].geomSymbolID_Life_UV_Width;
    vec4 in_vgb_entityColor_1 = vgb_datas[1].geomEntityColor,
        in_vgb_mixEmissive_1 = vgb_datas[1].geomMixEmissive,
        in_vgb_entityColor_2 = vgb_datas[2].geomEntityColor,
        in_vgb_mixEmissive_2 = vgb_datas[2].geomMixEmissive;
#endif

    // cut different trail || cut loop in same trail
    vec3 startNode_Life_UV_Width = uintBitsToFloat(in_vgb_nodeData_1.yzw),
        endNode_Life_UV_Width = uintBitsToFloat(in_vgb_nodeData_2.yzw);
    if (in_vgb_nodeData_1.x != in_vgb_nodeData_2.x || startNode_Life_UV_Width.x <= endNode_Life_UV_Width.x) return;

    bool isLeftAuxPoint = (uintBitsToFloat(in_vgb_nodeData_0.z) < -1000.0) || (in_vgb_nodeData_0.x != in_vgb_nodeData_1.x),
        isRightAuxPoint = (uintBitsToFloat(in_vgb_nodeData_3.z) < -1000.0) || (in_vgb_nodeData_2.x != in_vgb_nodeData_3.x);

    vec2 trailDir, midNormal, startNormal = vec2(0.0), endNormal = vec2(0.0);
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
    startNormal *= startNode_Life_UV_Width.z;
    endNormal *= endNode_Life_UV_Width.z;

#ifdef LEGACY_TRAIL_MODE
    gfb_fragEndsAlpha = isLeftAuxPoint ? 0.0 : 1.0;
    gfb_fragEntityColor = in_vgb_entityColor_1;
    gfb_fragMixEmissive = in_vgb_mixEmissive_1;
    gfb_fragUV = vec2(startNode_Life_UV_Width.y, 1.0);
    gl_Position = b_gameViewport * vec4(startPos + startNormal, 0.0, 1.0);
    EmitVertex();

    gfb_fragUV.y = 0.0;
    gl_Position = b_gameViewport * vec4(startPos - startNormal, 0.0, 1.0);
    EmitVertex();
#else
    gfb_data.fragEndsAlpha = isLeftAuxPoint ? 0.0 : 1.0;
    gfb_data.fragEntityColor = in_vgb_entityColor_1;
    gfb_data.fragMixEmissive = in_vgb_mixEmissive_1;
    vec4 resultGLPos;
    gfb_data.fragUV_TBN = vec4(startNode_Life_UV_Width.y, 1.0, trailDir);
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


#ifdef LEGACY_TRAIL_MODE
    gfb_fragEndsAlpha = isRightAuxPoint ? 0.0 : 1.0;
    gfb_fragEntityColor = in_vgb_entityColor_2;
    gfb_fragMixEmissive = in_vgb_mixEmissive_2;
    gfb_fragUV.x = endNode_Life_UV_Width.y;
    gfb_fragUV.y = 1.0;
    gl_Position = b_gameViewport * vec4(endPos + endNormal, 0.0, 1.0);
    EmitVertex();

    gfb_fragUV.y = 0.0;
    gl_Position = b_gameViewport * vec4(endPos - endNormal, 0.0, 1.0);
    EmitVertex();
    EndPrimitive();
#else
    gfb_data.fragEndsAlpha = isRightAuxPoint ? 0.0 : 1.0;
    gfb_data.fragEntityColor = in_vgb_entityColor_2;
    gfb_data.fragMixEmissive = in_vgb_mixEmissive_2;
    gfb_data.fragUV_TBN.x = endNode_Life_UV_Width.y;
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