
inline float length (float x1, float y1, float x2, float y2) {
    return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
}

inline float2 vecToCenter (float x1, float y1, float x2, float y2) {
    return (float2)(x2 - x1, y2 - y1);
}

inline float2 normal (float2 point) {
    float len = sqrt(point.x * point.x + point.y * point.y);
    return (len > 0) ? (point / len) : (float2)(0., 0.);
}

inline float scalar (float2 particleSpeed, float2 nor) {
    return particleSpeed.x * nor.x + particleSpeed.y * nor.y;
}

inline float2 newSpeed (float2 particleSpeed, float2 nor, float scal) {
    return particleSpeed - 2 * scal * nor;
}
