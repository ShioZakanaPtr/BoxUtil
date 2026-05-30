#version 430

layout (lines) in;
layout (triangle_strip, max_vertices = 4) out;

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

out GEOM_FRAG_BLOCK {
    vec2 geomFacing;
    float fragUV;
    vec3 fragPos;
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
} gfb_data;

void main() {

}