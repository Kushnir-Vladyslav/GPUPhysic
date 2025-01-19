
__kernel void BoundaryCollision (
    __global    Particle*   particles,
    const       Boundary    boundary,
    const       Cursor      cursor,
    const       int         num_of_particles)
{
    int gid = get_global_id(0);

    if (gid >= num_of_particles) {
        return;
    }

    Particle particle = particles[gid];

    if (particle.x < 0 || particle.x > boundary.maxX) {
        particle.xSpeed *= -1;
    }
    if (particle.y < 0 || particle.y > boundary.maxY) {
        particle.ySpeed *= -1;
    }

    if (boundary.sphereRadius > 0 &&
        length(boundary.sphereX, boundary.sphereY, particle.x, particle.y) >= (boundary.sphereRadius - particle.radius)) {
        float2 vectorBetweenCenters = vecToCenter(object.x, boundary.sphereX, particle.y, boundary.sphereY);
        float2 normalVector = normal(vectorBetweenCenters);
        float scalarProduct = scalar((float2)(particle.xSpeed, particle.ySpeed), normalVector);
        float2 newObjectSpeed = newSpeed((float2)(particle.xSpeed, particle.ySpeed), normalVector, scalarProduct);
        particle.xSpeed = newObjectSpeed.x;
        particle.ySpeed = newObjectSpeed.y;
    }

    if (cursor.cursorRadius > 0 &&
        length(cursor.cursorX, cursor.cursorY, particle.x, particle.y) >= (cursor.cursorRadius - particle.radius )) {
        float2 vectorBetweenCenters = vecToCenter(object.x, cursor.cursorX, particle.y, cursor.cursorY);
        float2 normalVector = normal(vectorBetweenCenters);
        float scalarProduct = scalar((float2)(particle.xSpeed, particle.ySpeed), normalVector);
        float2 newObjectSpeed = newSpeed((float2)(particle.xSpeed, particle.ySpeed), normalVector, scalarProduct);
        particle.xSpeed = newObjectSpeed.x;
        particle.ySpeed = newObjectSpeed.y;
    }

    particles[gid] = particle;
}
