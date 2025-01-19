
__kernel void BoundaryCollision (
    __global    Object*     objects,
    const       Boundary    boundary,
    const       Cursor      cursor,
    const       int         num_of_particles)
{
    Object object = objects[get_global_id(0)];
    Boundary boundary = boundaries[0];

    if (object.x < 0 || object.x > boundary.maxX) {
        object.xSpeed *= -1;
    }
    if (object.y < 0 || object.y > boundary.maxY) {
        object.ySpeed *= -1;
    }
    if (boundary.sphereRadius > 0 &&
        length(boundary.sphereX, boundary.sphereY, object.x, object.y) >= (boundary.sphereRadius - object.radius )) {
        float2 vectorBetweenCenters = vecToCenter(object.x, boundary.sphereX, object.y, boundary.sphereY);
        float2 normalVector = normal(vectorBetweenCenters);
        float scalarProduct = scalar((float2)(object.xSpeed, object.ySpeed), normalVector);
        float2 newObjectSpeed = newSpeed((float2)(object.xSpeed, object.ySpeed), normalVector, scalarProduct);
        object.xSpeed = newObjectSpeed.x;
        object.ySpeed = newObjectSpeed.y;
    }
    if (cursor.cursorRadius > 0 &&
        length(cursor.cursorX, cursor.cursorY, object.x, object.y) >= (cursor.cursorRadius - object.radius )) {
        float2 vectorBetweenCenters = vecToCenter(object.x, cursor.cursorX, object.y, cursor.cursorY);
        float2 normalVector = normal(vectorBetweenCenters);
        float scalarProduct = scalar((float2)(object.xSpeed, object.ySpeed), normalVector);
        float2 newObjectSpeed = newSpeed((float2)(object.xSpeed, object.ySpeed), normalVector, scalarProduct);
        object.xSpeed = newObjectSpeed.x;
        object.ySpeed = newObjectSpeed.y;
    }

    objects[get_global_id(0)] = object;
}
