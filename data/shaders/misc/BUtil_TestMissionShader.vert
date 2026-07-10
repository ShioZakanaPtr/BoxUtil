#version 110

varying vec2 vf_fragUV;

void main() {
	gl_Position = gl_Vertex;
	vf_fragUV = max(gl_Vertex.xy, 0.0);
}
