
#include "Structs.cl"

__kernel void DrawWorkZone (
    __global    Object*     objects,
    const       Boundary    boundaries,
    const       Cursor      cursor,
    __global    uchar4*     outPut)
{
    int gid_x = get_global_id(0);
    int gid_y = get_global_id(1);


}
