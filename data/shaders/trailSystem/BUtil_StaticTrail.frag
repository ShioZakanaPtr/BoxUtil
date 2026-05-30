#version 430

layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
    mat4 gameViewport;
    vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};

layout (binding = 0) uniform sampler2D diffuseMap;
layout (binding = 1) uniform sampler2D normalMap;
layout (binding = 2) uniform sampler2D complexMap;
layout (binding = 3) uniform sampler2D emissiveMap;
layout (binding = 4) uniform sampler2D tangentMap;

layout (location = 0) out vec4 fragColor; // draw to RGB8
layout (location = 1) out vec4 fragEmissive; // draw to RGB8
layout (location = 2) out vec4 fragWorldPos; // draw to RGB16
layout (location = 3) out vec4 fragWorldNormal; // draw to RGB16_SNORM
layout (location = 4) out vec4 fragWorldTangent; // draw to RGB16_SNORM
layout (location = 5) out vec4 fragMaterial; // roughness, metalness, anisotropic; draw to RGB8
layout (location = 6) out uvec4 fragData; // depth, alpha, flag; draw to RGB10_A2UI, alpha write ignored.

void main() {

}