
typedef struct  {
    float   x;
    float   y;
    float   radius; //Прирівнюю до маси
    float   xSpeed;
    float   ySpeed;
    float   xAcceleration;
    float   yAcceleration;
} Object;

typedef struct {
    float maxX;
    float maxY;
    float sphereRadius;
    float sphereX;
    float sphereY;
} Boundary;

typedef struct {
    float cursorRadius;
    float cursorX;
    float cursorY;
} Cursor;
