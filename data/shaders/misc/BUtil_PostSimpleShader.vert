#version 420

layout (location = 0) in vec2 a_vertex;

smooth out vec2 vf_fragUV;

void main() {
	const vec2 uv[] = vec2[4](vec2(0.0), vec2(1.0, 0.0), vec2(0.0, 1.0), vec2(1.0));
    vf_fragUV = uv[gl_VertexID];
	gl_Position = vec4(a_vertex, 0.0, 1.0);
}
