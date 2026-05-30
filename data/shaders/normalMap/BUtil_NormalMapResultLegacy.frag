#version 110
// For reference
#define LINEAR vec3(0.2126729, 0.7151522, 0.0721750)

uniform vec2 state; // normalScale, keepAlpha
uniform vec2 stepUV;
uniform sampler2D srcTex;

varying vec2 fragUV;

float getGray() {
    return texture2D(srcTex, fragUV).x;
}

float getGray(vec2 offset) {
    return texture2D(srcTex, fragUV + offset).x;
}

void main() {
    float gLT = getGray(vec2(-stepUV.x, stepUV.y));
    float gL = getGray(vec2(-stepUV.x, 0.0));
    float gLB = getGray(-stepUV);
    float gB = getGray(vec2(0.0, -stepUV.y));
    float gRB = getGray(vec2(stepUV.x, -stepUV.y));
    float gR = getGray(vec2(stepUV.x, 0.0));
    float gRT = getGray(stepUV);
    float gT = getGray(vec2(0.0, stepUV.y));

    float gxL = gL * 0.625; // sobel 0.5
    gxL += (gLT + gLB) * 0.1875; // sobel 0.25
    float gxR = gR * 0.625;
    gxR += (gRT + gRB) * 0.1875;
    float gyU = gT * 0.625;
    gyU += (gLT + gRT) * 0.1875;
    float gyD = gB * 0.625;
    gyD += (gLB + gRB) * 0.1875;
    float dx = gxL - gxR;
    float dy = gyD - gyU;

    gl_FragColor = vec4(normalize(vec3(vec2(dx, dy) * state.x, 0.05)) * 0.5 + 0.5, (state.y > 0.0) ? texture2D(srcTex, fragUV).w : 1.0);
}