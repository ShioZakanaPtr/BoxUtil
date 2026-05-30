#version 110

uniform float time;

varying vec2 fragUV;

void main() {
	gl_Position = gl_Vertex;
	fragUV = max(gl_Vertex.xy, 0.0);
}
