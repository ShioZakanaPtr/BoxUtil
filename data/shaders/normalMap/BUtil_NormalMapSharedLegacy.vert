#version 110

varying vec2 vf_fragUV;

void main() {
    vf_fragUV = max(gl_Vertex.xy, vec2(0.0));
	gl_Position = gl_Vertex;
}
