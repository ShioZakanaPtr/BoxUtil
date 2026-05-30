#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

layout (location = 0) in vec2 vertex;

uniform vec2 uvStart;
uniform vec2 uvEnd;

smooth out vec2 fragUV;

void main()
{
	const vec2 uv[] = vec2[4](uvStart.xy, vec2(uvEnd.x, uvStart.y), vec2(uvStart.x, uvEnd.y), uvEnd.xy);
	fragUV = uv[gl_VertexID];
	gl_Position = vec4(vertex, 0.0, 1.0);
}
