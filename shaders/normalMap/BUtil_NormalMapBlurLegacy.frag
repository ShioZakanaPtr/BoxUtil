#version 110
// For reference
#define LINEAR vec3(0.2126729, 0.7151522, 0.0721750)

uniform vec3 state[3]; // vec3(rampMix.xy, step), vec3(brightnessAdj, contrastAdj, perStep), vec3(srcStrength, srcPowFactor, srcSmoothstepMix)
uniform vec4 stepUV_srcUVDiv;
uniform bool vertical;
uniform sampler2D srcTex;

varying vec2 fragUV;

float smoothStep(float edgeL, float edgeR, float value) {
    float result = clamp((value - edgeL) / (edgeR - edgeL), 0.0, 1.0);
    return result * result * (3.0 - 2.0 * result);
}

float extraFix(float x) {
    float result = pow(x, state[2].y);
    result -= 0.5;
    result *= state[1].y;
    result += 0.5;
    result *= state[1].x;
    result = mix(result, smoothstep(0.0, 1.0, result), state[2].z);
    return result;
}

float getGray(vec3 col) {
    return extraFix(dot(col, LINEAR)) * state[2].x;
}

float getGaussian(float x) {
    float p = x * state[1].z;
    return state[1].z * 0.3989423 * exp(-0.5 * p * p);
}

float fi(float a, float b) {
    return 1.0 - (1.0 - a) * (1.0 - b);
}

float getRamp(vec2 uv) {
    float x = 1.0 - abs(uv.x * 2.0 - 1.0);
    float y = smoothstep(1.0, 0.0, abs(uv.y * 2.0 - 1.0));
    return fi(x * state[0].x, y * state[0].y);
}

void main() {
    vec4 result = vec4(0.0);
    float alpha = 0.0;
    int stepNum = int(state[0].z);

    if (stepNum != 0) {
        vec3 resultTmp = vec3(0.0);
        vec2 offset;
        float gaussian, fi, fix = 0.0;
        for (int i = -stepNum; i <= stepNum; ++i) {
            fi = float(i);
            gaussian = getGaussian(fi);
            fix += gaussian;
            offset = vertical ? vec2(0.0, fi * stepUV_srcUVDiv.y) : vec2(fi * stepUV_srcUVDiv.x, 0.0);
            result = texture2D(srcTex, fragUV + offset);
            resultTmp += result.xyz * gaussian;
            if (i == 0) alpha = result.w;
        }
        result.xyz = resultTmp / fix;
        result.w = alpha;
    } else {
        result = texture2D(srcTex, fragUV);
    }

    if (vertical) {
        result.x = getGray(result.xyz) * result.w;
        if (state[0].x > 0.0 || state[0].y > 0.0) result.x = fi(result.x, getRamp(fragUV * stepUV_srcUVDiv.zw));
    }

    gl_FragColor = result;
}