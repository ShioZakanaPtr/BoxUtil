#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define CURVE_STATE 3

layout(vertices = 2) out;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, timerAlpha), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];

in VERT_TESC_BLOCK {
    flat mat4 tescMatrix;
    flat vec4 tescPoints;
    flat vec4 tescColor;
    flat vec4 tescEmissiveColor;
    flat float tescWidth;
    flat float tescMixFactor;
    flat float tescID;
    flat float tescDistance;
} vtb_datas[];

out TESC_TESE_BLOCK {
    flat mat4 teseMatrix;
    flat vec4 tesePoints;
    flat vec4 teseColor;
    flat vec4 teseEmissiveColor;
    flat float teseWidth;
    flat float teseMixFactor;
    flat float teseID;
    flat float teseDistance;
} ttb_datas[];

void main()
{
    ttb_datas[gl_InvocationID].teseMatrix = vtb_datas[gl_InvocationID].tescMatrix;
    ttb_datas[gl_InvocationID].tesePoints = vtb_datas[gl_InvocationID].tescPoints;
    ttb_datas[gl_InvocationID].teseColor = vtb_datas[gl_InvocationID].tescColor;
    ttb_datas[gl_InvocationID].teseEmissiveColor = vtb_datas[gl_InvocationID].tescEmissiveColor;
    ttb_datas[gl_InvocationID].teseWidth = vtb_datas[gl_InvocationID].tescWidth;
    ttb_datas[gl_InvocationID].teseMixFactor = vtb_datas[gl_InvocationID].tescMixFactor;
    ttb_datas[gl_InvocationID].teseID = vtb_datas[gl_InvocationID].tescID;
    ttb_datas[gl_InvocationID].teseDistance = vtb_datas[gl_InvocationID].tescDistance;
    gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
    if (gl_InvocationID == 0) {
        gl_TessLevelOuter[0] = 1.0;
        gl_TessLevelOuter[1] = statePackage[CURVE_STATE].x;
    }
}
