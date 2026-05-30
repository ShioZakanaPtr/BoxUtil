#version 110

// vec2(length), number, invert, vec4(color)
uniform vec4 statePackage[2];
uniform float charLength;

varying vec2 fragUV;

void main() {
	vec4 pos = ftransform();
	if (statePackage[1].w <= 0.0) pos.xyz = vec3(-65536.0);
	gl_Position = pos;
	fragUV = vec2(gl_Vertex.x > 0.0 ? 1.0 : 0.0, gl_Vertex.y > 0.0 ? 1.0 : 0.0);
}
