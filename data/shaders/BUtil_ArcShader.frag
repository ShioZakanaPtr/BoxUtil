#version 110

#define RIGHT vec2(1.0, 0.0)

// vec2(inner), ringHardness, innerHardness, vec4(color)
uniform vec4 u_statePackage[2];
uniform float u_arcValue;

varying vec2 vf_fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

void main() {
    float ring = length(vf_fragUV);
    float inner = length(vf_fragUV / u_statePackage[0].xy);
    inner = u_statePackage[0].w >= 1.0 ? step(inner, 1.0) : smoothStep(1.0, u_statePackage[0].w, inner);
    float result = (u_statePackage[0].z >= 1.0 ? step(ring, 1.0) : smoothStep(1.0, u_statePackage[0].z, ring)) - inner;
    if (dot(normalize(vf_fragUV), RIGHT) <= u_arcValue) result = 0.0;
    gl_FragColor = result * u_statePackage[1];
}
