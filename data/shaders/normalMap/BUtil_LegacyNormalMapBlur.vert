#version 110

uniform vec4 u_uvRegion;

varying vec4 vf_fragUV_samplingUV;

void main() {
    vec2 rawUV = max(gl_Vertex.xy, vec2(0.0));
    vf_fragUV_samplingUV = vec4(rawUV, rawUV * u_uvRegion.zw + u_uvRegion.xy);
	gl_Position = gl_Vertex;
}
