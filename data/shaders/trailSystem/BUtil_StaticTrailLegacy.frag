#version 150

layout (std140) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

uniform sampler2D diffuseMap;
uniform sampler2D complexMap;
uniform sampler2D emissiveMap;

void main() {

}