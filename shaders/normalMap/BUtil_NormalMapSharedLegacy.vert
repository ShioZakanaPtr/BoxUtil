#version 110

varying vec2 fragUV;

void main() {
    fragUV = max(gl_Vertex.xy, vec2(0.0));
	gl_Position = gl_Vertex;
}
