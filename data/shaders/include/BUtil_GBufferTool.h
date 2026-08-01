#ifndef BUtil_GBufferTool_H
#define BUtil_GBufferTool_H

#if __VERSION__ < 420
layout (std140) uniform BUtilGlobalData {
    mat4 b_gameViewport;
    vec4 b_gameScreenBorder; // vec4(screenLB, screenSize)
};
#else
layout (std140, binding = 0) uniform BUtilGlobalData {
    mat4 b_gameViewport;
    vec4 b_gameScreenBorder; // vec4(screenLB, screenSize)
};
#endif

vec3 encodePos(in vec3 posRaw) {
    return clamp((posRaw - vec3(b_gameScreenBorder.xy, -6400.0)) / vec3(b_gameScreenBorder.zw, 12800.0), vec3(0.0), vec3(1.0));
}

vec3 decodePos(in vec3 posRaw) {
#if __VERSION__ < 400
    return posRaw * vec3(b_gameScreenBorder.zw, 12800.0) + vec3(b_gameScreenBorder.xy, -6400.0);
#else
    return fma(posRaw, vec3(b_gameScreenBorder.zw, 12800.0), vec3(b_gameScreenBorder.xy, -6400.0));
#endif
}

#endif // BUtil_GBufferTool_H