
#define backgroundColor (uchar4)(255, 184, 134, 11)
#define borderColor (uchar4)(255, 105, 105, 105)

__kernel void DrawBackground (
    __global    uchar4*     outPut,
    const       Boundary    boundaries,
    const       Cursor      cursor)
{
    int gid_x = get_global_id(0);
    int gid_y = get_global_id(1);

    if (gid_x >= boundaries.width || gid_y >= boundaries.height) {
        return;
    }

        bool isBoundary =
            gid_x <= boundaries.borderThickness ||
            gid_x >= boundaries.width - boundaries.borderThickness ||
            gid_y <= boundaries.borderThickness ||
            gid_y >= boundaries.height - boundaries.borderThickness ||
            (boundaries.sphereRadius > 0 &&
            boundaries.sphereRadius < length(gid_x, gid_y, boundaries.sphereX, boundaries.sphereY));

    int pixel = gid_y * boundaries.width + gid_x;

    if (isBoundary) {
        outPut[pixel] = borderColor;
    } else {
        outPut[pixel] = backgroundColor;
    }
}
