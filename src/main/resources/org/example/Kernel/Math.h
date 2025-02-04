
//визначення різниць між центрами
inline float2 subTwoPair (float x1, float y1, float x2, float y2) {
    return (float2) (
        x2 - x1, y2 -y1
    );
}

//відстань між двома точками
inline float length (float x1, float y1, float x2, float y2) {
    return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
}

//побудова вектору до центру першої з точок між двома точками
inline float2 vecToCenter (float x1, float y1, float x2, float y2) {
    return (float2)(x2 - x1, y2 - y1);
}

//нормалізація вектору
inline float2 normal (float2 point) {
    float len = sqrt(point.x * point.x + point.y * point.y);
    return (len > 0) ? (point / len) : (float2)(0.f, 0.f);
}

//скалярне множення двох векторів
inline float scalar (float2 particleSpeed, float2 nor) {
    return particleSpeed.x * nor.x + particleSpeed.y * nor.y;
}

//розрахунок нової швидкості частинки після зіткнення зі сферою
inline float2 newSpeed (float2 particleSpeed, float2 nor, float scal) {
    return particleSpeed - 2.f * scal * nor;
}


