#version 110

// vec4(center, radiusSamples, samplesInv)
uniform vec4 u_statePackage;

varying vec2 vf_fragUV;

void main() {
    vf_fragUV = max(gl_Vertex.xy, vec2(0.0)) - u_statePackage.xy;
	gl_Position = gl_Vertex;
}
