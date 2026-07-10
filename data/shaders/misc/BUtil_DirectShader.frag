#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

smooth in vec2 vf_fragUV;

layout (binding = 0) uniform sampler2D u_tex;
uniform float u_alphaFix;
uniform float u_level;

layout (location = 0) out vec4 o_fragColor;

void main() {
    vec4 result = textureLod(u_tex, vf_fragUV, u_level);
    if (u_alphaFix > -1.0) result.w = u_alphaFix;
    o_fragColor = result;
}
