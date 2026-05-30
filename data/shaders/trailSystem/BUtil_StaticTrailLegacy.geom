#version 150

layout (lines) in;
layout (triangle_strip, max_vertices = 4) out;

out GEOM_FRAG_BLOCK {
    vec2 geomFacing;
    float fragUV;
    vec3 fragPos;
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
} gfb_data;

void main() {

}