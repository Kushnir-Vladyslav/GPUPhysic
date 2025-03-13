
#define DEFINE_PREFIX_SUM(TYPE) \
__kernel void PartialPrefixSum_##TYPE( \
    __global    TYPE*                       workBuffer, \
    __local     TYPE*                       localBuffer, \
    const       PrefixSumConstants_##TYPE   PSC) \
{ \
    int gid = get_global_id(0); \
    int lid = get_local_id(0); \
    int groupSize = get_local_size(0); \
\
    if (gid < PSC.sizeBase) { \
        localBuffer[lid] = workBuffer[gid + PSC.offsetBase]; \
    } else { \
        localBuffer[lid] = 0; \
    } \
\
    barrier(CLK_LOCAL_MEM_FENCE); \
\
    for (int offset = 1; offset < groupSize; offset *= 2) { \
        TYPE temp = (lid >= offset) ? localBuffer[lid - offset] : 0; \
        barrier(CLK_LOCAL_MEM_FENCE); \
        localBuffer[lid] += temp; \
        barrier(CLK_LOCAL_MEM_FENCE); \
    } \
\
    if (gid < PSC.sizeBase) { \
        workBuffer[gid + PSC.offsetBase] = localBuffer[lid]; \
    } \
\
    if (lid == groupSize - 1) { \
        workBuffer[get_group_id(0) + PSC.offsetBlocSum] = localBuffer[lid]; \
    } \
}

DEFINE_PREFIX_SUM(int)
DEFINE_PREFIX_SUM(float)
