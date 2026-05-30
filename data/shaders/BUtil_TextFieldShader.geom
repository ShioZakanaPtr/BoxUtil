#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (points) in;
layout (triangle_strip, max_vertices = 16) out;

uniform float italicFactor;

in VERT_GEOM_BLOCK {
	mat4 geomMatrix;
	vec4 geomUV;
	vec4 geomStyleUV; // topStyleUV, bottomStyleUV, strikeoutUV, underlineUV
	vec4 geomSize;
	flat vec2 geomFillBase;

	flat vec4 geomColor;
	flat uvec4 geomStyleState; // fuck intel
	flat uvec3 geomState; // cahnnel, texIndex, reserved
} vgb_datas[];

out GEOM_FRAG_BLOCK {
	vec3 fragUV;
    flat vec2 fragStyleUV; // strikeoutUV, underlineUV
	flat uint fragIsEdge;

	flat vec4 fragColor;
	flat uvec3 fragStyleState; // fuck intel
	flat uvec3 fragState; // cahnnel, texIndex, reserved
} gfb_data;

void main()
{
	mat4 matrix = vgb_datas[0].geomMatrix;
	vec4 size = vgb_datas[0].geomSize;
	vec4 uv = vgb_datas[0].geomUV;
	vec4 styleUV = vgb_datas[0].geomStyleUV;
	vec2 fillBase = vgb_datas[0].geomFillBase;
	vec2 basePoint = gl_in[0].gl_Position.xy;
    float baselineAlignedEdge = italicFactor * fillBase.y;

	bool isItalic = vgb_datas[0].geomStyleState.y == 1u;
	vec4 upPoint = vec4(basePoint, 0.0, 1.0);
	if (isItalic) upPoint.x += italicFactor * (size.y + size.w) - baselineAlignedEdge;
	vec4 upPointM = matrix * upPoint;
	vec4 bottomPoint = vec4(basePoint.x, basePoint.y - size.y, 0.0, 1.0);
	if (isItalic) bottomPoint.x += italicFactor * size.w - baselineAlignedEdge;
	vec4 bottomPointM = matrix * bottomPoint;

	vec2 upEdgeUpPoint = vec2(basePoint.x, basePoint.y + size.z);
	vec2 bottomEdgeBottomPoint = vec2(basePoint.x, basePoint.y - size.y - size.w);
	if (isItalic) {
        upEdgeUpPoint.x += italicFactor * (size.y + size.z + size.w) - baselineAlignedEdge;
        bottomEdgeBottomPoint.x -= baselineAlignedEdge;
    }
	vec4 topEdgeL = vec4(upEdgeUpPoint, 0.0, 1.0);
	vec4 topEdgeLM = matrix * topEdgeL;
	vec4 topEdgeR = matrix * vec4(upEdgeUpPoint.x + size.x, upEdgeUpPoint.y, 0.0, 1.0);
	vec4 bottomEdgeL = vec4(bottomEdgeBottomPoint, 0.0, 1.0);
	vec4 bottomEdgeLM = matrix * bottomEdgeL;
	vec4 bottomEdgeR = matrix * vec4(bottomEdgeBottomPoint.x + size.x, bottomEdgeBottomPoint.y, 0.0, 1.0);

	bool setZeroB = vgb_datas[0].geomColor.w > 0.0;
	bool topValid = (size.z > 0.0) && setZeroB;
	bool bottomValid = (size.w > 0.0) && setZeroB;
	bool leftFill = (fillBase.x > 0.0) && setZeroB;
	vec4 fillVertex[] = vec4[](upPoint, upPointM, bottomPoint, bottomPointM);
	vec2 fillUv = vec2(styleUV.x, styleUV.y);
    if (leftFill) {
        if (topValid) {
            fillVertex[0] = topEdgeL;
            fillVertex[1] = topEdgeLM;
            fillUv.x = 1.0;
        }
        if (bottomValid) {
            fillVertex[2] = bottomEdgeL;
            fillVertex[3] = bottomEdgeLM;
            fillUv.y = 0.0;
        }
        fillVertex[0].x -= fillBase.x;
        fillVertex[2].x -= fillBase.x;
    }

	vec4 upPointRight = matrix * vec4(upPoint.x + size.x, upPoint.y, 0.0, 1.0);
	vec4 bottomPointRight = matrix * vec4(bottomPoint.x + size.x, bottomPoint.y, 0.0, 1.0);
	if (!setZeroB) upPointM = upPointRight = bottomPointM = bottomPointRight = vec4(vec3(-65536.0), 1.0);

	gfb_data.fragColor = vgb_datas[0].geomColor;
	gfb_data.fragStyleState = uvec3(vgb_datas[0].geomStyleState.x, vgb_datas[0].geomStyleState.z, vgb_datas[0].geomStyleState.w);
	gfb_data.fragState = vgb_datas[0].geomState;

	gfb_data.fragIsEdge = 0u;
    gfb_data.fragStyleUV = styleUV.zw;
	gfb_data.fragUV = vec3(uv.xy, styleUV.y);
	gl_Position = bottomPointM;
	EmitVertex();
	gfb_data.fragUV = vec3(uv.zy, styleUV.y);
	gl_Position = bottomPointRight;
	EmitVertex();
    gfb_data.fragUV = vec3(uv.xw, styleUV.x);
    gl_Position = upPointM;
    EmitVertex();
    gfb_data.fragUV = vec3(uv.zw, styleUV.x);
    gl_Position = upPointRight;
    EmitVertex();
	EndPrimitive();

	gfb_data.fragIsEdge = 1u;
	if (topValid) { // topEdge
		gfb_data.fragUV = vec3(styleUV.x);
		gl_Position = upPointM;
		EmitVertex();
		gl_Position = upPointRight;
		EmitVertex();
        gfb_data.fragUV = vec3(1.0);
        gl_Position = topEdgeLM;
        EmitVertex();
        gl_Position = topEdgeR;
        EmitVertex();
		EndPrimitive();
	}

	if (bottomValid) { // bottomEdge
		gfb_data.fragUV = vec3(0.0);
		gl_Position = bottomEdgeLM;
		EmitVertex();
		gl_Position = bottomEdgeR;
		EmitVertex();
		gfb_data.fragUV = vec3(styleUV.y);
		gl_Position = bottomPointM;
		EmitVertex();
		gl_Position = bottomPointRight;
		EmitVertex();
		EndPrimitive();
	}

	if (leftFill) {
        gfb_data.fragUV = vec3(fillUv.y);
        gl_Position = matrix * fillVertex[2];
        EmitVertex();
        gl_Position = fillVertex[3];
        EmitVertex();
		gfb_data.fragUV = vec3(fillUv.x);
		gl_Position = matrix * fillVertex[0];
		EmitVertex();
		gl_Position = fillVertex[1];
		EmitVertex();
		EndPrimitive();
	}
}
