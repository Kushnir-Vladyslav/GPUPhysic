
__kernel void AddBlockSums (
    __global    int*                workBuffer,
    const       PrefixSumConstants  PSC)
{
    int gid = get_global_id(0);
    int groupId = get_group_id(0);

    if (groupId > 0 && gid < PSC.sizeBase) {
        workBuffer[gid + PSC.offsetBase] += workBuffer[groupId - 1 + PSC.offsetBlocSum];
    }
}
