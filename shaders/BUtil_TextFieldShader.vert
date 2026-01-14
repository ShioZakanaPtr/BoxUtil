#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (location = 0) in vec4 uv; // uvBL, uvTR
layout (location = 1) in vec2 location; // x, y
layout (location = 2) in vec2 fontDrawData; // fontHeight, baselineHeight
layout (location = 3) in uint style; // invert(1+8) + italic(1+7) + underline(1+6) + strikeout(1+5) + channel(3+2) + handelIndex(2+0)
layout (location = 4) in vec4 color;
layout (location = 5) in vec2 size;
layout (location = 6) in vec2 edge; // x = YOffset
layout (location = 7) in float fill;

// topStyleUV = 1.0f - (float) fontData.getYOffset() / fontLineHeight);
// bottomStyleUV = 1.0f - (float) (fontData.getSize()[1] + fontData.getYOffset()) / fontLineHeight
// strikeoutUV = 1.0f - (fontLineHeight - lineHeight) * 0.5 / fontLineHeight
layout (std140, binding = OVERWRITE_MATRIX_UBO) uniform BUtilGlobalData
{
	mat4 gameViewport;
	vec4 gameScreenBorder; // vec4(screenLB, screenSize)
};
uniform vec4 globalColor[2];
uniform mat4 modelMatrix;

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

void main()
{
	vec4 styleUV = vec4(edge.x, size.y + edge.x, (fontDrawData.x - fontDrawData.y) * 0.5, fontDrawData.y) / fontDrawData.x;
	styleUV.xyz = 1.0 - styleUV.xyz;
	styleUV.zw *= vec2(0.8, 0.5);
	vgb_data.geomMatrix = gameViewport * modelMatrix;
	vgb_data.geomUV = uv + 0.5;
	vgb_data.geomStyleUV = styleUV;
	vgb_data.geomSize = vec4(size, edge);
	vgb_data.geomFillBase = vec2(fill, fontDrawData.y);
	vgb_data.geomColor = color * globalColor[0];
	vgb_data.geomStyleState = uvec4(style >> 8, style >> 7, style >> 6, style >> 5) & 1u;
	vgb_data.geomState = uvec3(style & 28u, style & 3u, ((uv.x < -500.0) ? 1u : 0u));
	gl_Position = vec4(location, 0.0, 1.0);
}
