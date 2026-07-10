#version 110

// innerHardness, ringHardness, innerFactor, arc, vec4(color)
uniform vec4 u_statePackage[2];
uniform sampler2D u_diffuseMap;

varying vec2 vf_fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

float inverseLerp(float left, float right, float v) {
    return (v - left) * 1.0 / (right - left);
}

void main() {
    vec2 uv = vec2(inverseLerp(u_statePackage[0].z, 0.0, 1.0 - length(vf_fragUV)), atan(-vf_fragUV.y, vf_fragUV.x) * -0.5 / 3.14159265 + 0.5);
    float arc = abs(u_statePackage[0].w);
    uv.x /= arc;
    uv.x -= ((1.0 / arc) - 1.0) * 0.5;
    float mask = uv.y * 2.0 - 1.0;
    float maskV = (mask < 0.0) ? u_statePackage[0].x : u_statePackage[0].y;
    mask = (maskV >= 1.0) ? step(abs(mask), 1.0) : smoothstep(1.0, maskV, abs(mask));
    if ((u_statePackage[0].w > 0.0) && (uv.x < 0.0 || uv.x > 1.0)) mask = 0.0;
    gl_FragColor = texture2D(u_diffuseMap, uv) * u_statePackage[1] * mask;
}
