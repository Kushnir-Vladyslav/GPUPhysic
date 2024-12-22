
#define FREE_FALL   9.8;

typedef struct  {
    float   x;
    float   y;
    floar   radiys; //Врирівнюю до маси
    float   xSpeed;
    float   ySpeed;
    float   xAcseleration;
    float   yAcseleration;
} Object;

__kernel void physicCalculation(
    __global    Object* objects,
    __local     float*  masterObjects,
    __local     float*  slaveObjects,
    const       int     sizeOfObjects)
{
}


