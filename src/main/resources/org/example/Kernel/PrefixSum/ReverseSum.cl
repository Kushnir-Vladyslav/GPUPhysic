
#define DEFINE_PREFIX_SUM(TYPE) \
__kernel void ReverseSum_##TYPE( \
    __global    TYPE*                       workBuffer, \
    const       PrefixSumConstants_##TYPE   PSC) \
{ \
    int gid = get_global_id(0); \
    int groupId = get_group_id(0); \
\
    if (groupId > 0 && gid < PSC.sizeBase) { \
        workBuffer[gid + PSC.offsetBase] += workBuffer[groupId - 1 + PSC.offsetBlocSum]; \
    } \
}

DEFINE_PREFIX_SUM(int)
DEFINE_PREFIX_SUM(float)

