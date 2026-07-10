#version 110

// vec2(length), number, invert, vec4(color)
uniform vec4 u_statePackage[2];

varying vec2 vf_fragUV;

void main() {
	vec4 pos = ftransform();
	if (u_statePackage[1].w <= 0.0) pos.xyz = vec3(-65536.0);
	gl_Position = pos;
	vf_fragUV = vec2(gl_Vertex.x > 0.0 ? 1.0 : 0.0, gl_Vertex.y > 0.0 ? 1.0 : 0.0);
}
