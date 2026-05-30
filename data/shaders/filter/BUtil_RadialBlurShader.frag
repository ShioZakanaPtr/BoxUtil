#version 110

// vec4(center, radiusSamples, samplesInv)
uniform vec4 statePackage;
uniform float alphaStrength;
uniform sampler2D tex;

varying vec2 fragUV;

void main() {
    vec4 result = vec4(0.0);
    int limit = int(1.0 / statePackage.w);
    for (int i = 1; i <= limit; ++i) {
        result += texture2D(tex, fragUV * float(i) * statePackage.z + statePackage.xy);
    }
    gl_FragColor = result * statePackage.w * alphaStrength;
}
