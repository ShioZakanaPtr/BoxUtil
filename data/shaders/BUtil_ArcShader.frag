#version 110

#define RIGHT vec2(1.0, 0.0)

// vec2(inner), ringHardness, innerHardness, vec4(color)
uniform vec4 statePackage[2];
uniform float arcValue;

varying vec2 fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

void main() {
    float ring = length(fragUV);
    float inner = length(fragUV / statePackage[0].xy);
    inner = statePackage[0].w >= 1.0 ? step(inner, 1.0) : smoothStep(1.0, statePackage[0].w, inner);
    float result = (statePackage[0].z >= 1.0 ? step(ring, 1.0) : smoothStep(1.0, statePackage[0].z, ring)) - inner;
    if (dot(normalize(fragUV), RIGHT) <= arcValue) result = 0.0;
    gl_FragColor = result * statePackage[1];
}
