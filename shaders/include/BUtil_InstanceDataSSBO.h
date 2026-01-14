// 80 byte
struct Dynamic2D { // binding 4
    vec4 q22_q23_facing_TurnRate;
    vec4 location_Scale;
    vec4 velocity_ScaleRate;
    vec4 timer;
    uvec4 colorBits;
};

// 32 byte
struct Fixed2D { // binding 5
    vec4 alpha_Facing_Location;
    vec2 scale;
    uvec2 colorBits;
};

// 144 byte
struct Dynamic3D { // binding 6
    vec4 m00_m01_m02_m21;
    vec4 m10_m11_m12_m22;
    vec4 m20_rotateRate;

    vec4 location_velocityX;
    vec4 rotate_velocityY;
    vec4 scale_velocityZ;
    vec4 scaleRate_rd;

    vec4 timer;
    uvec4 colorBits;
};

// 48 byte
struct Fixed3D { // binding 7
    vec4 rotate_LocationX;
    vec4 scale_LocationY;
    vec2 alpha_LocationZ;
    uvec2 colorBits;
};

layout(std430, binding = 4) restrict readonly buffer BUtilInstanceData_Dynamic2D
{
    Dynamic2D dataDynamic2D[];
};

layout(std430, binding = 5) restrict readonly buffer BUtilInstanceData_Fixed2D
{
    Fixed2D dataFixed2D[];
};

layout(std430, binding = 6) restrict readonly buffer BUtilInstanceData_Dynamic3D
{
    Dynamic3D dataDynamic3D[];
};

layout(std430, binding = 7) restrict readonly buffer BUtilInstanceData_Fixed3D
{
    Fixed3D dataFixed3D[];
};

float pickInstanceTimer(in float _in_override, in float _in_instance) {
    return (_in_override >= 0.0) ? _in_override : _in_instance;
}

float decodeAlpha(in float _in_alpha) {
    if (_in_alpha > 2.0) return abs(3.0 - _in_alpha);
    else if (_in_alpha > 1.0) return 1.0;
    else return max(_in_alpha, 0.0);
}

void decodeFixedColor(in uvec2 _in_bits, in float _in_raw_alpha, out vec4 _out_color, out vec4 _out_emissive) {
    mat2x4 colorMat = mat2x4(vec4(uvec4(_in_bits >> 24u, _in_bits >> 16u) & 0xFFu), vec4(uvec4(_in_bits >> 8u, _in_bits) & 0xFFu)) * 0.0039215;

    float alpha = decodeAlpha(_in_raw_alpha);
    _out_color = vec4(colorMat[0].x, colorMat[0].z, colorMat[1].x, colorMat[1].z * alpha);
    _out_emissive = vec4(colorMat[0].y, colorMat[0].w, colorMat[1].y, colorMat[1].w * alpha);
}

void decodeDynamicColor(in uvec4 _in_bits, in float _in_raw_alpha, out vec4 _out_color, out vec4 _out_emissive) {
    vec4 lowColor = vec4((_in_bits >> 24u) & 0xFFu);
    vec4 highColor = vec4((_in_bits >> 16u) & 0xFFu);
    vec4 lowEmissive = vec4((_in_bits >> 8u) & 0xFFu);
    vec4 highEmissive = vec4(_in_bits & 0xFFu);
    mat4 colorMat = mat4(lowColor, highColor, lowEmissive, highEmissive) * 0.0039215;

    float alpha = decodeAlpha(_in_raw_alpha);
    _out_color = mix(colorMat[0], colorMat[1], alpha);
    _out_emissive = mix(colorMat[2], colorMat[3], alpha);
    _out_color.w *= alpha;
    _out_emissive.w *= alpha;
}

mat4 fetchDynamic2DMatrix(in Dynamic2D data) {
    return mat4(
    data.location_Scale.z - data.q22_q23_facing_TurnRate.x * data.location_Scale.z, data.q22_q23_facing_TurnRate.y * data.location_Scale.z, 0.0, 0.0,
        -data.q22_q23_facing_TurnRate.y * data.location_Scale.w, data.location_Scale.w - data.q22_q23_facing_TurnRate.x * data.location_Scale.w, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        data.location_Scale.xy, 0.0, 1.0
    );
}

mat4 fetchDynamic3DMatrix(in Dynamic3D data) {
    return mat4(
        data.m00_m01_m02_m21.xyz, 0.0,
        data.m10_m11_m12_m22.xyz, 0.0,
        data.m20_rotateRate.x, data.m00_m01_m02_m21.w, data.m10_m11_m12_m22.w, 0.0,
        data.location_velocityX.xyz, 1.0
    );
}

mat4 fetchFixed2DMatrix(in Fixed2D data) {
    float pryFacing = radians(data.alpha_Facing_Location.y * 0.5);
    float pryCos = cos(pryFacing);
    float prySin = sin(pryFacing);
    float dqz = prySin + prySin;
    float q22 = dqz * prySin;
    float q23 = dqz * pryCos;
    return mat4(
    data.scale.x - q22 * data.scale.x, q23 * data.scale.x, 0.0, 0.0,
    -q23 * data.scale.y, data.scale.y - q22 * data.scale.y, 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    data.alpha_Facing_Location.zw, 0.0, 1.0
    );
}

mat4 fetchFixed3DMatrix(in Fixed3D data) {
    vec3 pryRotate = radians(data.rotate_LocationX.xyz * 0.5);
    vec3 pryCos = cos(pryRotate);
    vec3 prySin = sin(pryRotate);
    float wq = pryCos.x * pryCos.y * pryCos.z - prySin.x * prySin.y * prySin.z;
    float xq = prySin.x * pryCos.y * pryCos.z - pryCos.x * prySin.y * prySin.z;
    float yq = pryCos.x * prySin.y * pryCos.z + prySin.x * pryCos.y * prySin.z;
    float zq = pryCos.x * pryCos.y * prySin.z + prySin.x * prySin.y * pryCos.z;
    float dqx = xq + xq;
    float dqy = yq + yq;
    float dqz = zq + zq;
    float q00 = dqx * xq;
    float q11 = dqy * yq;
    float q22 = dqz * zq;
    float q01 = dqx * yq;
    float q02 = dqx * zq;
    float q03 = dqx * wq;
    float q12 = dqy * zq;
    float q13 = dqy * wq;
    float q23 = dqz * wq;
    return mat4(
        data.scale_LocationY.x - (q11 + q22) * data.scale_LocationY.x, (q01 + q23) * data.scale_LocationY.x, (q02 - q13) * data.scale_LocationY.x, 0.0,
        (q01 - q23) * data.scale_LocationY.y, data.scale_LocationY.y - (q22 + q00) * data.scale_LocationY.y, (q12 + q03) * data.scale_LocationY.y, 0.0,
        (q02 + q13) * data.scale_LocationY.z, (q12 - q03) * data.scale_LocationY.z, data.scale_LocationY.z - (q11 + q00) * data.scale_LocationY.z, 0.0,
        data.rotate_LocationX.w, data.scale_LocationY.w, data.alpha_LocationZ.y, 1.0
    );
}