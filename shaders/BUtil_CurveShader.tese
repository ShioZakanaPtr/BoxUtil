#version OVERWRITE_VERSION

precision OVERWRITE_PRECISION float;

#define CURVE_STATE 3
#define CURVE_FILL 4

layout(isolines, equal_spacing, ccw) in;

// vec4(color), vec4(emissiveColor), vec4(emissiveState, timerAlpha), vec4(interpolationFloat + 1, texturePixels, globalUV, time), vec4(fillStart, fillEnd, startFactor, endFactor)
uniform vec4 statePackage[5];
uniform float totalNodes;

in TESC_TESE_BLOCK {
    flat mat4 teseMatrix;
    flat vec4 tesePoints;
    flat vec4 teseColor;
    flat vec4 teseEmissiveColor;
    flat float teseWidth;
    flat float teseMixFactor;
    flat float teseID;
    flat float teseDistance;
} ttb_datas[];

out TESE_GEOM_BLOCK {
    flat mat4 geomMatrix;
    flat vec2 geomNormal;
    flat vec4 geomColor;
    flat vec4 geomEmissiveColor;
    flat float geomWidth;
    flat float geomUV;
} tgb_data;

void main()
{
    bool directCheck = statePackage[CURVE_STATE].x <= 1.0;
    float factor1 = gl_TessCoord.x;
    float factor1P2 = factor1 + factor1;
    float factor2 = factor1 * factor1;
    float factor2P2 = factor2 + factor2;
    float factor3 = factor2 * factor1;
    float mixFactor = pow(factor1, ttb_datas[0].teseMixFactor);
    float oneMinusF1 = 1.0 - factor1;
    float oneMinusF1M2 = oneMinusF1 * oneMinusF1;
    vec4 midPoints = vec4(ttb_datas[0].tesePoints.zw, ttb_datas[1].tesePoints.xy) * 3.0;
    vec2 tmp0 = oneMinusF1M2 * oneMinusF1 * gl_in[0].gl_Position.xy;
    vec2 tmp1 = (factor1 - factor2P2 + factor3) * midPoints.xy;
    vec2 tmp2 = (factor2 - factor3) * midPoints.zw;
    vec2 tmp3 = factor3 * gl_in[1].gl_Position.xy;

    vec2 tmpT0 = oneMinusF1M2 * gl_in[0].gl_Position.xy * - 3.0;
    vec2 tmpT1 = (oneMinusF1M2 - factor1P2 + factor2P2) * midPoints.xy;
    vec2 tmpT2 = (factor1P2 - factor2 - factor2P2) * midPoints.zw;
    vec2 tmpT3 = factor2 * gl_in[1].gl_Position.xy * 3.0;

    bool useGlobal = statePackage[CURVE_STATE].z > 0.0;
    vec2 ids = vec2(ttb_datas[0].teseID, ttb_datas[1].teseID);
    vec2 uv = useGlobal ? (statePackage[CURVE_STATE].z * ids) : (vec2(ttb_datas[0].teseDistance, ttb_datas[1].teseDistance) / statePackage[CURVE_STATE].y);
    tgb_data.geomMatrix = ttb_datas[0].teseMatrix;
    vec2 currentTangent = directCheck ? (gl_in[1].gl_Position.xy - gl_in[0].gl_Position.xy) : vec2(tmpT0 + tmpT1 + tmpT2 + tmpT3);
    tgb_data.geomNormal = normalize(vec2(-currentTangent.y, currentTangent.x));
    ids /= totalNodes;
    float fillUV = mix(ids.x, ids.y, factor1);
    vec2 fillMix = smoothstep(statePackage[CURVE_FILL].zw, vec2(1.0), vec2(1.0 - fillUV, fillUV)) * (1.0 - statePackage[CURVE_FILL].xy);
    fillMix = 1.0 - fillMix;
    float fillFactor = clamp(fillMix.x * fillMix.y, 0.0, 1.0);
    vec4 color = mix(ttb_datas[0].teseColor, ttb_datas[1].teseColor, mixFactor);
    color.w *= fillFactor;
    tgb_data.geomColor = color;
    vec4 emissive = mix(ttb_datas[0].teseEmissiveColor, ttb_datas[1].teseEmissiveColor, mixFactor);
    emissive.w *= fillFactor;
    tgb_data.geomEmissiveColor = emissive;
    tgb_data.geomWidth = mix(ttb_datas[0].teseWidth, ttb_datas[1].teseWidth, mixFactor);
    tgb_data.geomUV = mix(uv.x, uv.y, factor1) - statePackage[CURVE_STATE].w;
    gl_Position = directCheck ? gl_in[uint(factor1)].gl_Position : vec4(tmp0 + tmp1 + tmp2 + tmp3, 0.0, 1.0);
}
