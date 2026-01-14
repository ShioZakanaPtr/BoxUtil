__kernel void createInstanceMatrix3D(
    __read_only image1d_t final0R, __write_only image1d_t final0W,
    __read_only image1d_t final1R, __write_only image1d_t final1W,
    __read_only image1d_t final2R, __write_only image1d_t final2W,
    __global float4 *timerT,
    __read_only image1d_t state0R, __write_only image1d_t state0W,
    __global float4 *state1_3,
    sampler_t samplerIn, float2 amount_size
) {
    int index = get_global_id(0) * get_global_size(1) + get_global_id(1);
    if (index >= (int) (amount_size.y)) return;
	float4 timer = timerT[index];
	if (timer.x < -1000.0f) return;
    float4 final0 = read_imagef(final0R, samplerIn, index);
    float4 final1 = read_imagef(final1R, samplerIn, index);
    float4 final2 = read_imagef(final2R, samplerIn, index);
    float4 state0 = read_imagef(state0R, samplerIn, index);
    int state1Index = index * 3;
    int state2Index = state1Index + 1;
    int state3Index = state2Index + 1;
    float4 state1 = state1_3[state1Index];
    float4 state2 = state1_3[state2Index];
    float4 state3 = state1_3[state3Index];

    float3 loc = final0.wyz;
	float3 rotate = (float3) (state0.x, state0.y, state0.z);
	float3 scale = (float3) (state1.x, state1.y, state1.z);

    // dLoc, dRotate, dScale
	float8 dynamic = (float8) (
		state1.w, state2.x, state2.y,
		state2.z, state2.w, state3.x,
		state3.y, state3.z
	);
    float dScaleZ = state3.w;
	dynamic *= amount_size.x;
    dScaleZ *= amount_size.x;

    float3 pryRotate = radians(rotate * 0.5f);
	float3 prySin = native_sin(pryRotate);
	float3 pryCos = native_cos(pryRotate);

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

    float alpha = 10.0f;
	bool finishSet = timer.x < -500.0f;
	float2 timerTmp = finishSet ? (float2) (-512.0f) : (float2) (timer.w, -10.0f);
	if (timer.x > 2.0f) {
		alpha = abs(timer.x - 3.0f);
		timerTmp = (float2) (timer.y, 2.0f);
	} else if (timer.x > 1.0f) {
		timerTmp = (float2) (timer.z, 1.0f);
		alpha = 21.0f;
	} else if (timer.x > 0.0f) {
		alpha = timer.x + 10.0f;
	}
	timer.x = timerTmp.x > -500.0f ? (timer.x - timerTmp.x * instanceState.x) : timerTmp.y;
	if (timer.x <= 0.0f && timer.x > -500.0f) {
		timer.x = -512.0f;
	} else if (finishSet) timer.x = -1024.0f;
    timerT[index] = timer;

    loc += dynamic.s012;
	rotate = fmod(rotate + dynamic.s345 + 360.0f, 360.0f);
	scale += (float3) (dynamic.s67, dScaleZ);
    write_imagef(final0W, index, (float4) (
		scale.x - (q11 + q22) * scale.x,
		loc.z,
		loc.y,
		loc.x
	));
    write_imagef(final1W, index, (float4) (
		(q01 + q23) * scale.x,
		scale.y - (q22 + q00) * scale.y,
		(q12 - q03) * scale.z,
		(q01 - q23) * scale.y
	));
    write_imagef(final2W, index, (float4) (
		(q02 - q13) * scale.x,
		(q12 + q03) * scale.y,
		scale.z - (q11 + q00) * scale.z,
		(q02 + q13) * scale.z
	));

    write_imagef(state0W, index, (float4) (rotate, alpha));
    state1_3[state1Index] = (float4) (scale.s012, state1.w);
}
