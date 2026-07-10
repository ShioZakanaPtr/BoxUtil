#version 110

// vec4(center, radiusSamples, samplesInv)
uniform vec4 u_statePackage;
uniform float u_alphaStrength;
uniform sampler2D u_tex;

varying vec2 vf_fragUV;

void main() {
    vec4 result = vec4(0.0);
    int limit = int(1.0 / u_statePackage.w);
    for (int i = 1; i <= limit; ++i) {
        result += texture2D(u_tex, vf_fragUV * float(i) * u_statePackage.z + u_statePackage.xy);
    }
    gl_FragColor = result * u_statePackage.w * u_alphaStrength;
}
