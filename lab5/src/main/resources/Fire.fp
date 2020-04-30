#define scaledTime            time * 40.0
#define normalStrength        30.0
#define distortionStrength    0.2

uniform vec2 resolution;
uniform float time;

vec2 getHash(vec2 p) {
    p = vec2(dot(p, vec2(127.1, 311.7)),
    dot(p, vec2(269.5, 183.3)));

    return -1.0 + 2.0*fract(sin(p) * 43758.5453123);
}

float getNoise(in vec2 p) {
    const float K1 = 0.366025404;// (sqrt(3)-1)/2;
    const float K2 = 0.211324865;// (3-sqrt(3))/6;

    vec2 i = floor(p + (p.x+p.y) * K1);

    vec2 a = p - i + (i.x+i.y) * K2;
    vec2 o = step(a.yx, a.xy);
    vec2 b = a - o + K2;
    vec2 c = a - 1.0 + 2.0*K2;

    vec3 h = max(0.5-vec3(dot(a, a), dot(b, b), dot(c, c)), 0.0);

    vec3 n = h*h*h*h*vec3(dot(a, getHash(i+0.0)), dot(b, getHash(i+o)), dot(c, getHash(i+1.0)));

    return dot(n, vec3(70.0));
}

float fbm (in vec2 p) {
    float f = 0.0;
    mat2 m = mat2(1.6, 1.2, -1.2, 1.6);
    f  = 0.5000*getNoise(p); p = m*p;
    f += 0.2500*getNoise(p); p = m*p;
    f += 0.1250*getNoise(p); p = m*p;
    f += 0.0625*getNoise(p); p = m*p;
    f = 0.5 + 0.5 * f;
    return f;
}

vec3 bumpMap(vec2 uv) {
    vec2 s = 1.0 / resolution.xy;
    float p =  fbm(uv);
    float h1 = fbm(uv + s * vec2(1.0, 0));
    float v1 = fbm(uv + s * vec2(0, 1.0));

    vec2 xy = (p - vec2(h1, v1)) * normalStrength;
    return vec3(xy + 0.5, 1.0);
}

void main() {
    vec2 uv = gl_FragCoord.xy/resolution.xy;
    vec3 normal = bumpMap(uv * vec2(1.0, 0.3) + vec2(-0.01, -0.3) * scaledTime);
    vec2 displacement = clamp((normal.xy - 0.5) * distortionStrength, -1.0, 1.0);
    uv += displacement;

    vec2 uvT = (uv * vec2(1.0, 0.5)) + vec2(-0.01, -0.5) * scaledTime;
    float n = pow(fbm(8.0 * uvT), 1.0);

    float gradient = pow(1.0 - uv.y, 2.0) * 3.0;
    float finalNoise = n * gradient;

    vec3 color = finalNoise * vec3(2.0*n, 2.0*n*n*n, n*n*n*n);
    gl_FragColor = vec4(color, 1.0);
}
