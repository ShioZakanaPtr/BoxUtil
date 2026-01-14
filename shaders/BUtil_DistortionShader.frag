#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define HARDNESS_RING 1
#define HARDNESS_INNER 4

in VERTEX_BLOCK {
    vec4 fragUVMask;
    vec4 fragUVScreen;
    float fragGlobalHardness;
} vb_data;

// vec4(sizeIn, powerIn, powerFull), vec4(sizeFull, powerOut, hardnessRing), vec4(sizeOut, fadeInFactor, fadeOutFactor)
// vec4(sizeInRatio, sizeFullRatio), vec4(sizeOutRatio, hardnessInner, globalTimerRaw), vec4(arcStart, arcEnd, innerCenter)
uniform vec4 statePackage[6];
// screen
layout(binding = 0) uniform sampler2D texturePackage;

out vec4 fragColor;

float inverseLerp(in float edgeL, in float edgeR, in float value) {
    return (value - edgeL) / (edgeR - edgeL);
}

void main() {
    float ring = length(vb_data.fragUVMask.xy), inner = length(vb_data.fragUVMask.zw);
    float arc = (statePackage[5].x > -1.0) ? min(inverseLerp(statePackage[5].y, statePackage[5].x, vb_data.fragUVMask.x / ring), 1.0) : 1.0;
    float ringHardness = statePackage[HARDNESS_RING].w * vb_data.fragGlobalHardness, innerHardness = statePackage[HARDNESS_INNER].z * vb_data.fragGlobalHardness;

    ring = ringHardness >= 1.0 ? step(ring, 1.0) : smoothstep(1.0, ringHardness, ring);
    inner = innerHardness >= 1.0 ? step(1.0, inner) : smoothstep(innerHardness, 1.0, inner);
    float mask = ring * inner * arc;
    if (mask <= 0.0) discard;
    vec4 result = texture(texturePackage, vb_data.fragUVScreen.xy - (vb_data.fragUVScreen.zw * mask));
    result.w = 1.0;
    fragColor = result;
}
