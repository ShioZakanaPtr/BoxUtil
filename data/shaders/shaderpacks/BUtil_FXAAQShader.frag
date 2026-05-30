#version 420

precision highp float;

#define SCREENSTEPX OVERWRITE_SCREEN_X
#define SCREENSTEPY OVERWRITE_SCREEN_Y
#define FXAA_SHARPNESS 0.5
#define FXAA_ABSOLUTE_LUMA_THRESHOLD 0.1
#define FXAA_RELATIVE_LUMA_THRESHOLD 0.15
#define OFFSETSIZE 9
#define EDGESIZE 5
#define NW 0
#define N 1
#define NE 2
#define W 3
#define C 4
#define E 5
#define SW 6
#define S 7
#define SE 8

subroutine float sampleMethod(in vec2 sampleUV);
subroutine uniform sampleMethod sampleMethodState;
subroutine vec4 displayMethod(in vec2 finalUV);
subroutine uniform displayMethod displayMethodState;

smooth in vec2 fragUV;

layout(binding = 0) uniform sampler2D screen;
layout(binding = 1) uniform sampler2D fragData;

out vec4 fragColor;

subroutine(sampleMethod) float fromRaw(in vec2 sampleUV) {
    return dot(texture(screen, sampleUV).xyz, vec3(0.2126729, 0.7151522, 0.0721750));
}

subroutine(sampleMethod) float fromDepth(in vec2 sampleUV) {
    return texture(fragData, sampleUV).y;
}

subroutine(displayMethod) vec4 commonDisplay(in vec2 finalUV) {
    return texture(screen, finalUV);
}

subroutine(displayMethod) vec4 edgeDisplay(in vec2 finalUV) {
    return vec4(0.0, 1.0, 0.0, 1.0);
}

const vec2 SCREEN_OFFSET[] = vec2[](vec2(-1.0, 1.0), vec2(0.0, 1.0), vec2(1.0, 1.0), vec2(-1.0, 0.0), vec2(0.0, 0.0), vec2(1.0, 0.0), vec2(-1.0,-1.0), vec2(0.0,-1.0), vec2(1.0,-1.0));
const float EDGE_STEP[] = float[](1.0, 1.5, 2.0, 2.0, 8.0);

void main()
{
    vec2 screenStepVec = vec2(SCREENSTEPX, SCREENSTEPY);
    vec4 fragRaw = texture(screen, fragUV);

    float luma[OFFSETSIZE];
    for (int i = 0; i < OFFSETSIZE; i++) {
        vec2 eachUV = vec2(fragUV + screenStepVec * SCREEN_OFFSET[i]);
        luma[i] = sampleMethodState(eachUV);
    }

    float lumaMax = max(luma[N], max(max(luma[W], luma[C]), max(luma[E], luma[S])));
    float lumaMin = min(luma[N], min(min(luma[W], luma[C]), min(luma[E], luma[S])));
    float lumaContrast = lumaMax - lumaMin;
    if (lumaContrast < max(FXAA_ABSOLUTE_LUMA_THRESHOLD, lumaMax * FXAA_RELATIVE_LUMA_THRESHOLD)) {
        fragColor = fragRaw;
        return;
    }

    float lumaHorzC = luma[N] + luma[S];
    float lumaVertC = luma[W] + luma[E];
    float lumaHorzTR = luma[NE] + luma[SE];
    float lumaVertTR = luma[NW] + luma[NE];
    float lumaHorzBL = luma[NW] + luma[SW];
    float lumaVertBL = luma[SW] + luma[SE];
    float edgeHorz = abs((-2.0 * luma[W])+ lumaHorzBL) + (abs((-2.0 * luma[C]) + lumaHorzC) * 2.0) + abs((-2.0 * luma[E]) + lumaHorzTR);
    float edgeVert = abs((-2.0 * luma[S]) + lumaVertBL) + (abs((-2.0 * luma[C]) + lumaVertC) * 2.0) + abs((-2.0 * luma[N]) + lumaVertTR);
    bool isHorz = edgeHorz >= edgeVert;

    float screenStep = isHorz ? screenStepVec.y : screenStepVec.x;
    float luma1 = isHorz ? luma[S] : luma[W];
    float luma2 = isHorz ? luma[N] : luma[E];
    float gradient1 = luma1 - luma[C];
    float gradient2 = luma2 - luma[C];
    float gradientScaled = 0.25 * max(abs(gradient1), abs(gradient2));

    bool is1Steepest = abs(gradient1) >= abs(gradient2);
    float lumaLocalAverage = is1Steepest ? (0.5 * (luma1 + luma[C])) : (0.5 * (luma2 + luma[C]));
    if (is1Steepest) {
        screenStep = -screenStep;
    }

    vec2 startN = isHorz ? vec2(fragUV.x, fragUV.y + screenStep * 0.5) : vec2(fragUV.x + screenStep * 0.5, fragUV.y);
    vec2 uvOffsetT = isHorz ? vec2(screenStepVec.x, 0.0) : vec2(0.0, screenStepVec.y);

    vec2 uvL = startN - uvOffsetT;
    vec2 uvR = startN + uvOffsetT;
    float lumaEndL = sampleMethodState(uvL) - lumaLocalAverage;
    float lumaEndR = sampleMethodState(uvR) - lumaLocalAverage;

    bool reachedL = abs(lumaEndL) >= gradientScaled;
    bool reachedR = abs(lumaEndR) >= gradientScaled;
    bool reachedLR = reachedL && reachedR;

    if (!reachedL){
        uvL -= uvOffsetT * EDGE_STEP[0];
    }
    if (!reachedR){
        uvR += uvOffsetT * EDGE_STEP[0];
    }

    if (!reachedLR) {
        for (int i = 1; i < EDGESIZE; i++) {
            if(!reachedL) lumaEndL = sampleMethodState(uvL) - lumaLocalAverage;
            if(!reachedR) lumaEndR = sampleMethodState(uvR) - lumaLocalAverage;
            reachedL = abs(lumaEndL) >= gradientScaled;
            reachedR = abs(lumaEndR) >= gradientScaled;
            reachedLR = reachedL && reachedR;

            if(!reachedL) uvL -= uvOffsetT * EDGE_STEP[i];
            if(!reachedR) uvR += uvOffsetT * EDGE_STEP[i];
            if(reachedLR) break;
        }
    }

    float nearestUVL = isHorz ? (fragUV.x - uvL.x) : (fragUV.y - uvL.y);
    float nearestUVR = isHorz ? (uvR.x - fragUV.x) : (uvR.y - fragUV.y);

    bool isNearestL = nearestUVL <= nearestUVR;
    float nearestUV = min(nearestUVL, nearestUVR);
    float edgeLength = nearestUVL + nearestUVR;

    bool isLumaCenterSmaller = luma[C] < lumaLocalAverage;
    bool correctVariationL = (lumaEndL < 0.0) != isLumaCenterSmaller;
    bool correctVariationR = (lumaEndR < 0.0) != isLumaCenterSmaller;
    bool correctVariation = isNearestL ? correctVariationL : correctVariationR;
    float finalOffset = correctVariation ? (-nearestUV / edgeLength + 0.5) : 0.0;

    vec2 finalUV = isHorz ? vec2(fragUV.x, finalOffset * screenStep + fragUV.y) : vec2(finalOffset * screenStep + fragUV.x, fragUV.y);
    fragColor = displayMethodState(finalUV);
}
