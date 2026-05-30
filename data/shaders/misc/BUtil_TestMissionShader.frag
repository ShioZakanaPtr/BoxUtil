#version 110

uniform float time;

varying vec2 fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

vec2 smoothStep(float edgeL, float edgeR, vec2 value) {
    vec2 result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

void main() {
    vec2 uv = fragUV;
    float line = uv.y - fract(time / 3.0) * 9.0;
    line = smoothStep(0.15, -0.2, abs(line + 0.3)) * 0.2;
    uv.x += uv.y * mix(-0.1, 0.1, uv.x) * 24.0;
    uv *= 16.0;
    uv.x *= 0.25;
    uv.y += time * 0.5;
    uv = abs(fract(uv) - 0.5);
    uv = smoothStep(0.3, 0.75, uv);
    float result = length(uv);
    vec3 mixCol = 0.3 + 0.15 * sin(time + fragUV.y * vec3(4.5, 2.0, 0.3));
    vec3 resultCol = vec3(0.06 + mixCol.x, 0.3 + mixCol.y, mixCol.z) * 0.3 * result + vec3(0.8, 1.0, 0.1) * 2.0 * sqrt(result * line);
    gl_FragColor = vec4(resultCol, 0.8);
}
