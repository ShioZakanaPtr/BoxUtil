#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

smooth in vec2 fragUV;

layout (binding = 0) uniform sampler2D tex;
uniform float alphaFix;
uniform float level;

layout (location = 0) out vec4 fragColor;

void main()
{
    vec4 result = textureLod(tex, fragUV, level);
    if (alphaFix > -1.0) result.w = alphaFix;
    fragColor = result;
}
