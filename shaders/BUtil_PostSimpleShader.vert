#version 420

precision highp float;

layout (location = 0) in vec2 vertex;

smooth out vec2 fragUV;

void main()
{
	const vec2 uv[] = vec2[4](vec2(0.0), vec2(1.0, 0.0), vec2(0.0, 1.0), vec2(1.0));
	fragUV = uv[gl_VertexID];
	gl_Position = vec4(vertex, 0.0, 1.0);
}
