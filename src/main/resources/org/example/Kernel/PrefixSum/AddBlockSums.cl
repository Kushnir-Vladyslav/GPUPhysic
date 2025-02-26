
__kernel void AddBlockSums (
    __global    int*    gridDistribution,
    __global    int*    blockSums,
    const       int     numOfCells)
{
    int gid = get_global_id(0);
    int groupId = get_group_id(0);

    if (groupId > 0 && gid < numOfCells) {
        gridDistribution[gid] += blockSums[groupId -1];
    }
}
