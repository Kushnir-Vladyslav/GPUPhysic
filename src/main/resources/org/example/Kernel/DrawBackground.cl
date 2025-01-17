
#define backgroundColor (uchar4)(240, 240, 240, 255)
#define borderColor (uchar4)(218, 165, 32, 255)

__kernel void DrawBackground (
    __global    int4*     outPut,
    const       Boundary    boundaries,
    const       Cursor      cursor)
{
    int gid_x = get_global_id(0);
    int gid_y = get_global_id(1);

    if (gid_x >= boundaries.width || gid_y >= boundaries.height) {
        return;
    }

    outPut[gid_y * (int) boundaries.width + gid_x] = 5;
    return;

    bool isBoundary =
        gid_x <= boundaries.borderThickness ||
        gid_x >= boundaries.width - boundaries.borderThickness ||
        gid_y <= boundaries.borderThickness ||
        gid_y >= boundaries.height - boundaries.borderThickness ||
        (boundaries.sphereRadius > 0 &&
        boundaries.sphereRadius > length(gid_x, gid_y, boundaries.sphereX, boundaries.sphereY));

    int pixel = gid_y * boundaries.width + gid_x;

    if (isBoundary) {
        outPut[pixel] = 4;
    } else {
        outPut[pixel] = 6;
    }
}
