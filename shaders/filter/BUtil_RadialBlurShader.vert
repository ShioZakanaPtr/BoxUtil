#version 110

// vec4(center, radiusSamples, samplesInv)
uniform vec4 statePackage;

varying vec2 fragUV;

void main() {
	fragUV = max(gl_Vertex.xy, vec2(0.0)) - statePackage.xy;
	gl_Position = gl_Vertex;
}
