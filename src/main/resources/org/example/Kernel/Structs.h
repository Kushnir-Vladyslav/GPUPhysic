
typedef struct  {
    float   x;
    float   y;
    float   radius; //Прирівнюю до маси
    float   xSpeed;
    float   ySpeed;
    float   xAcceleration;
    float   yAcceleration;
} Particle;

typedef struct {
    float width;
    float height;
    float borderThickness;
    float sphereX;
    float sphereY;
    float sphereRadius;
} Boundary;

typedef struct {
    float cursorRadius;
    float cursorX;
    float cursorY;
} Cursor;

typedef struct {
    int y;
} __attribute__((packed)) MyStruct;

