#version 150

in vec2 location;
in vec2 facingVector;
in float timeStamp;

layout (std140) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform float time;

out VERT_GEOM_BLOCK {
    float fragUV;
    vec3 fragPos;
    vec4 fragEntityColor;
    vec4 fragMixEmissive;
} vgb_data;

void main() {

}