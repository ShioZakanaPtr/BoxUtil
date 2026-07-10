#version 110
// For reference
#define LINEAR vec3(0.2126729, 0.7151522, 0.0721750)

uniform vec2 u_state; // normalScale, keepAlpha
uniform vec2 u_stepUV;
uniform sampler2D u_srcTex;

varying vec2 vf_fragUV;

float getGray() {
    return texture2D(u_srcTex, vf_fragUV).x;
}

float getGray(vec2 offset) {
    return texture2D(u_srcTex, vf_fragUV + offset).x;
}

void main() {
    float gLT = getGray(vec2(-u_stepUV.x, u_stepUV.y));
    float gL = getGray(vec2(-u_stepUV.x, 0.0));
    float gLB = getGray(-u_stepUV);
    float gB = getGray(vec2(0.0, -u_stepUV.y));
    float gRB = getGray(vec2(u_stepUV.x, -u_stepUV.y));
    float gR = getGray(vec2(u_stepUV.x, 0.0));
    float gRT = getGray(u_stepUV);
    float gT = getGray(vec2(0.0, u_stepUV.y));

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

    gl_FragColor = vec4(normalize(vec3(vec2(dx, dy) * u_state.x, 0.05)) * 0.5 + 0.5, (u_state.y > 0.0) ? texture2D(u_srcTex, vf_fragUV).w : 1.0);
}