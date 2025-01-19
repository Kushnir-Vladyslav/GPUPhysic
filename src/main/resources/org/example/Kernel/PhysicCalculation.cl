
#define FREE_FALL   9.8
#define SIZE_OF_THREAD  64
#define NUM_OF_THREAD   32

__kernel void PhysicCalculation(
    __global    Object* objects,
    const       int     num_of_particles)
{
    int gid_x = get_global_id(0);
    int gid_x = get_global_id(0);

    __local Object masterObjects[NUM_OF_THREAD];
    __local Object slaveObjects[SIZE_OF_THREAD];

    int GID1D = get_global_id(0);
    int GID2D = get_global_id(1);
    int LID = get_local_id(0);

    masterObjects[LID] = Objects[GID1D];
    slaveObjects[LID] = Objects[GID2D];
    slaveObjects[LID + NUM_OF_THREAD] = objects[GID2D + NUM_OF_THREAD];

    for (int i = 0; i < SIZE_OF_THREAD; i++) {
        if (GID2D > GID1D &&
            (masterObjects[LID].radiys + slaveObjects[i].radiys) <=
                    length(masterObjects[LID].x, masterObjects[LID].y, slaveObjects[i].x, slaveObjects[i].y)) {
            //обробка зіткнення
        }
    }
}

