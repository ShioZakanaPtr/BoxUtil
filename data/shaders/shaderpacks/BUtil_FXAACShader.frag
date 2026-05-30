#version 420

precision highp float;

#define SCREENSTEPX OVERWRITE_SCREEN_X
#define SCREENSTEPY OVERWRITE_SCREEN_Y
#define FXAA_SHARPNESS 0.5
#define FXAA_ABSOLUTE_LUMA_THRESHOLD 0.05
#define FXAA_RELATIVE_LUMA_THRESHOLD 0.1
#define OFFSETSIZE 5
#define NW 0
#define NE 1
#define C 2
#define SW 3
#define SE 4
#define GLOW_TEX 2

subroutine float sampleMethod(in vec2 sampleUV);
subroutine uniform sampleMethod sampleMethodState;
subroutine vec4 displayMethod(in vec4 finalColor);
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

subroutine(displayMethod) vec4 commonDisplay(in vec4 finalColor) {
    return finalColor;
}

subroutine(displayMethod) vec4 edgeDisplay(in vec4 finalColor) {
    return vec4(0.0, 1.0, 0.0, 1.0);
}

const vec2 SCREEN_OFFSET[] = vec2[](vec2(-0.5, 0.5), vec2(0.5, 0.5), vec2( 0.0, 0.0), vec2(-0.5,-0.5), vec2(0.5,-0.5));

void main()
{
    vec2 screenStep = vec2(SCREENSTEPX, SCREENSTEPY);
    vec4 fragRaw = texture(screen, fragUV);

    float luma[OFFSETSIZE];
    for (int i = 0; i < OFFSETSIZE; i++) {
        vec2 eachUV = vec2(fragUV + screenStep * SCREEN_OFFSET[i]);
        luma[i] = sampleMethodState(eachUV);
    }

    float lumaMax = max(luma[NW], max(max(luma[NE], luma[C]), max(luma[SW], luma[SE])));
    float lumaMin = min(luma[NW], min(min(luma[NE], luma[C]), min(luma[SW], luma[SE])));
    float lumaContrast = lumaMax - lumaMin;
    if(lumaContrast < max(FXAA_ABSOLUTE_LUMA_THRESHOLD, lumaMax * FXAA_RELATIVE_LUMA_THRESHOLD)) {
        fragColor = fragRaw;
        return;
    }

    vec2 dir = vec2(0.0);
    dir.x = (luma[SW] + luma[SE]) - (luma[NW] + luma[NE]);
    dir.y = (luma[NE] + luma[SW]) - (luma[NE] + luma[SE]);
    dir = normalize(dir);
    vec4 P1 = texture(screen, fragUV + (dir * screenStep * 0.5));
    vec4 N1 = texture(screen, fragUV - (dir * screenStep * 0.5));

    float dirAbsMinTimesC = min(abs(dir.x), abs(dir.y)) * FXAA_SHARPNESS;
    vec2 minDir = clamp(dir / dirAbsMinTimesC, -1.0, 1.0) * 0.5;
    vec4 P2 = texture(screen, fragUV + (minDir * screenStep));
    vec4 N2 = texture(screen, fragUV - (minDir * screenStep));

    vec4 S1 = P1 + N1;
    vec4 S2 = (P2 + N2 + S1) / 4.0;
    float brightness = dot(S2.xyz, vec3(0.2126729, 0.7151522, 0.0721750));
    if (brightness < lumaMin || brightness > lumaMax) {
        S2.xyz = S1.xyz * 0.5;
    }
    fragColor = displayMethodState(S2);
}
