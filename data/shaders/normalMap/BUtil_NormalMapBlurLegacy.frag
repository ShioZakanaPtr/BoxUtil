#version 110
// For reference
#define LINEAR vec3(0.2126729, 0.7151522, 0.0721750)

uniform vec3 u_state[3]; // vec3(rampMix.xy, step), vec3(brightnessAdj, contrastAdj, perStep), vec3(srcStrength, srcPowFactor, srcSmoothstepMix)
uniform vec4 u_stepUV_srcUVDiv;
uniform bool u_vertical;
uniform sampler2D u_srcTex;

varying vec2 vf_fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

float extraFix(float x) {
    float result = pow(x, u_state[2].y);
    result -= 0.5;
    result *= u_state[1].y;
    result += 0.5;
    result *= u_state[1].x;
    result = mix(result, smoothstep(0.0, 1.0, result), u_state[2].z);
    return result;
}

float getGray(vec3 col) {
    return extraFix(dot(col, LINEAR)) * u_state[2].x;
}

float getGaussian(float x) {
    float p = x * u_state[1].z;
    return u_state[1].z * 0.3989423 * exp(-0.5 * p * p);
}

float fi(float a, float b) {
    return 1.0 - (1.0 - a) * (1.0 - b);
}

float getRamp(vec2 uv) {
    float x = 1.0 - abs(uv.x * 2.0 - 1.0);
    float y = smoothstep(1.0, 0.0, abs(uv.y * 2.0 - 1.0));
    return fi(x * u_state[0].x, y * u_state[0].y);
}

void main() {
    vec4 result = vec4(0.0);
    float alpha = 0.0;
    int stepNum = int(u_state[0].z);

    if (stepNum != 0) {
        vec3 resultTmp = vec3(0.0);
        vec2 offset;
        float gaussian, fi, fix = 0.0;
        for (int i = -stepNum; i <= stepNum; ++i) {
            fi = float(i);
            gaussian = getGaussian(fi);
            fix += gaussian;
            offset = u_vertical ? vec2(0.0, fi * u_stepUV_srcUVDiv.y) : vec2(fi * u_stepUV_srcUVDiv.x, 0.0);
            result = texture2D(u_srcTex, vf_fragUV + offset);
            resultTmp += result.xyz * gaussian;
            if (i == 0) alpha = result.w;
        }
        result.xyz = resultTmp / fix;
        result.w = alpha;
    } else {
        result = texture2D(u_srcTex, vf_fragUV);
    }

    if (u_vertical) {
        result.x = getGray(result.xyz) * result.w;
        if (u_state[0].x > 0.0 || u_state[0].y > 0.0) result.x = fi(result.x, getRamp(vf_fragUV * u_stepUV_srcUVDiv.zw));
    }

    gl_FragColor = result;
}