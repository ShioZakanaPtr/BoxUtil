#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (location = 0) in vec2 a_vertex;

uniform vec2 u_uvStart;
uniform vec2 u_uvEnd;

smooth out vec2 vf_fragUV;

void main() {
	const vec2 uv[] = vec2[4](u_uvStart.xy, vec2(u_uvEnd.x, u_uvStart.y), vec2(u_uvStart.x, u_uvEnd.y), u_uvEnd.xy);
	vf_fragUV = uv[gl_VertexID];
	gl_Position = vec4(a_vertex, 0.0, 1.0);
}
