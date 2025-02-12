
__kernel void CalculationPrefixAmount(
    __global    int*    gridDistribution,
    __global    int*    blockSums,
    __local     int*    localBuffer,
    const       int     numOfCells)
{
    int gid = get_global_id(0);
    int lid = get_local_id(0);
    int groupSize = get_local_size(0);

    if (gid < numOfCells) {
        localBuffer[lid] = gridDistribution[gid];
    } else {
        localBuffer[lid] = 0;
    }

    barrier(CLK_LOCAL_MEM_FENCE);

    for (int offset = 1; offset < groupSize; offset *= 2) {
        int temp = (lid >= offset) ? localBuffer[lid - offset] : 0;
        barrier(CLK_LOCAL_MEM_FENCE);
        localBuffer[lid] += temp;
        barrier(CLK_LOCAL_MEM_FENCE);
    }

    if (gid < numOfCells) {
        gridDistribution[gid] = localBuffer[lid];
    }

    if (lid == groupSize - 1) {
        blockSums[get_group_id(0)] = localBuffer[lid];
    }
}
