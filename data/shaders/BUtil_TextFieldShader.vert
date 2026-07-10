#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (location = 0) in vec4 a_uv; // uvBL, uvTR
layout (location = 1) in vec2 a_position; // x, y
layout (location = 2) in vec2 a_fontDrawData; // fontHeight, baselineHeight
layout (location = 3) in uint a_style; // invert(1+8) + italic(1+7) + underline(1+6) + strikeout(1+5) + channel(3+2) + handelIndex(2+0)
layout (location = 4) in vec4 a_color;
layout (location = 5) in vec2 a_size;
layout (location = 6) in vec2 a_edge; // x = YOffset
layout (location = 7) in float a_fill;

// topStyleUV = 1.0f - (float) fontData.getYOffset() / fontLineHeight);
// bottomStyleUV = 1.0f - (float) (fontData.getSize()[1] + fontData.getYOffset()) / fontLineHeight
// strikeoutUV = 1.0f - (fontLineHeight - lineHeight) * 0.5 / fontLineHeight
layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};
uniform vec4 u_globalColor[2];
uniform mat4 u_modelMatrix;
uniform vec2 u_blendBloom_globalTimerAlpha;

out VERT_GEOM_BLOCK {
	mat4 geomMatrix;
	vec4 geomUV;
	vec4 geomStyleUV; // topStyleUV, bottomStyleUV, strikeoutUV, underlineUV
	vec4 geomSize;
	flat vec2 geomFillBase;

	flat vec4 geomColor;
	flat uvec4 geomStyleState; // fuck intel
	flat uvec3 geomState; // cahnnel, texIndex, reserved
} vgb_data;

void main() {
	vec4 styleUV = vec4(a_edge.x, a_size.y + a_edge.x, (a_fontDrawData.x - a_fontDrawData.y) * 0.5, a_fontDrawData.y) / a_fontDrawData.x;
	styleUV.xyz = 1.0 - styleUV.xyz;
	styleUV.zw *= vec2(0.8, 0.5);
    vec4 charColor = a_color * u_globalColor[0];
    charColor.w *= u_blendBloom_globalTimerAlpha.y;
	vgb_data.geomMatrix = gameViewport * u_modelMatrix;
	vgb_data.geomUV = a_uv + 0.5;
	vgb_data.geomStyleUV = styleUV;
	vgb_data.geomSize = vec4(a_size, a_edge);
	vgb_data.geomFillBase = vec2(a_fill, a_fontDrawData.y);
	vgb_data.geomColor = charColor;
	vgb_data.geomStyleState = uvec4(a_style >> 8, a_style >> 7, a_style >> 6, a_style >> 5) & 1u;
	vgb_data.geomState = uvec3(a_style & 28u, a_style & 3u, ((a_uv.x < -500.0) ? 1u : 0u));
	gl_Position = vec4(a_position, 0.0, 1.0);
}
