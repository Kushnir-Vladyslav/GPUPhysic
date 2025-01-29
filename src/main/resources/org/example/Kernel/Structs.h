
typedef struct  {
    float   x;
    float   y;
    float   radius; //Прирівнюю до маси
    float   xSpeed;
    float   ySpeed;
    int     isSleep; //прапорець що символізує при зупинення часинки
    float   sleepTimer; //час який частинка маєшвидкість менше критичної
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
    int2 size;
    int2 num;
}   GridStruct;

typedef struct { //структура тестового ядра
    int y;
} __attribute__((packed)) MyStruct;

