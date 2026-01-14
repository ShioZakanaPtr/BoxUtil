#version 110

#define SIZE vec2(3.0, 5.0)
#define SPACING 4.0
#define SIZE_FACTOR SPACING / SIZE.x
#define POINT 2.0
#define NEG 448.0

// vec2(length), number, invert, vec4(color)
uniform vec4 statePackage[2];
uniform float charLength;

varying vec2 fragUV;

float getNumber(int digit) {
    float result = 31599.0;
    if (digit == 1) result = 25751.0;
    if (digit == 2) result = 29671.0;
    if (digit == 3) result = 29647.0;
    if (digit == 4) result = 23497.0;
    if (digit == 5) result = 31183.0;
    if (digit == 6) result = 31215.0;
    if (digit == 7) result = 29257.0;
    if (digit == 8) result = 31727.0;
    if (digit == 9) result = 31689.0;
    return result;
}

float getSprite(float sprite, vec2 uv) {
    uv = floor(uv);
    float bit = (SIZE.x - uv.x - 1.0) + uv.y * SIZE.x;
    bool bounds = all(greaterThanEqual(uv, vec2(0)));
    bounds = bounds && all(lessThan(uv, SIZE));
    return bounds ? floor(mod(sprite / pow(2.0, bit), 2.0)) : 0.0;
}

void main() {
    vec2 uv = fragUV * SIZE;
    uv.x *= charLength * SIZE_FACTOR;
    vec2 offset = vec2(0.0);
    float number, clip, result, step;
    bool negative = statePackage[0].z < 0.0;
    number = abs(statePackage[0].z);
    clip = result = step = 0.0;
    int digit = 0;
    for(int i = int(statePackage[0].x); i >= -int(statePackage[0].y); i--) {
        clip = float(number > pow(10.0, float(i)) || i == 0);
        digit = int(mod(number / pow(10.0, float(i)), 10.0));
        step = SPACING * clip;
        if (negative && i == int(statePackage[0].x)) {
            result += getSprite(NEG, uv - offset);
            offset.x += SPACING;
        }
        if(statePackage[0].x != 0.0 && i == -1) {
            result += getSprite(POINT, uv - offset) * clip;
            offset.x += step;
        }
        result += getSprite(getNumber(digit), uv - offset) * clip;
        offset.x += step;
    }
    vec4 col = vec4(result);
    if (statePackage[0].w == 1.0) col = 1.0 - col;
    gl_FragColor = col * statePackage[1];
}
