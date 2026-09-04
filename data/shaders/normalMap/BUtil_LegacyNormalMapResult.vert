#version 110

uniform vec2 u_uvRegion;

varying vec2 vf_fragUV;

void main() {
    vec2 rawUV = max(gl_Vertex.xy, vec2(0.0));
    vf_fragUV = rawUV * u_uvRegion;
	gl_Position = gl_Vertex;
}
