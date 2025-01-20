
//Ядро відповідає за перевірку зіштовхування частинок з границями області...
__kernel void BoundaryCollision (
    __global    Particle*   particles,
    const       Boundary    boundary,
    const       Cursor      cursor,
    const       int         num_of_particles)
{
    int gid = get_global_id(0);
    if (gid >= num_of_particles) return;

    Particle particle = particles[gid];

    //Перевірка сну/пробудження
    //...........................................

    //перевірка зіштовхування з межами прямокутної зони
    if (particle.x - particle.radius - boundary.borderThickness < 0) {
        particle.x = particle.radius + boundary.borderThickness;
        particle.xSpeed = -particle.xSpeed * RESTITUTION;
    } else if (particle.x + particle.radius + boundary.borderThickness > boundary.width) {
        particle.x = boundary.width - particle.radius - boundary.borderThickness;
        particle.xSpeed = -particle.xSpeed * RESTITUTION;
    }

    if (particle.y - particle.radius - boundary.borderThickness < 0) {
        particle.y = particle.radius + boundary.borderThickness;
        particle.ySpeed = -particle.ySpeed * RESTITUTION;
    } else if (particle.y + particle.radius + boundary.borderThickness > boundary.height) {
        particle.y = boundary.height - particle.radius - boundary.borderThickness;
        particle.ySpeed = -particle.ySpeed * RESTITUTION;
    }

    //перевірка зіштовхування з межами сферичної зони
    if (boundary.sphereRadius > 0) { // Якщо частинка намагається вийти за межі сфери
        float dist = length(boundary.sphereX, boundary.sphereY, particle.x, particle.y);
        float maxDist = boundary.sphereRadius - particle.radius;

        if(dist > maxDist) {
            float2 vectorBetweenCenters = vecToCenter(boundary.sphereX, boundary.sphereY, particle.x, particle.y);
            float2 normalVector = normal(vectorBetweenCenters);

            // Коеркція позиції
            float overlap = dist - maxDist;
            particle.x -= normalVector.x * overlap;
            particle.y -= normalVector.y * overlap;

            // Корекція швидкості
            float scalarProduct = scalar((float2)(particle.xSpeed, particle.ySpeed), normalVector);
            if (scalarProduct > 0) {  // Якщо частинка рухається назовні
                float2 newObjectSpeed = newSpeed((float2)(particle.xSpeed, particle.ySpeed), normalVector, scalarProduct);
                particle.xSpeed = newObjectSpeed.x * RESTITUTION;
                particle.ySpeed = newObjectSpeed.y * RESTITUTION;
            }
        }
    }

    //перевірка зіштовхування з межами курсору зони
    if (cursor.cursorRadius > 0) {
        float dist = length(cursor.cursorX, cursor.cursorY, particle.x, particle.y);
        float minDist = cursor.cursorRadius + particle.radius;

        if (dist < minDist) {  // Якщо частинка намагається увійти всередину курсора
            float2 vectorBetweenCenters = vecToCenter(cursor.cursorX, cursor.cursorY, particle.x, particle.y);
            float2 normalVector = normal(vectorBetweenCenters);

            // Коеркція позиції
            float overlap = minDist - dist;
            particle.x += normalVector.x * overlap;
            particle.y += normalVector.y * overlap;

            // Корекція швидкості
            float scalarProduct = scalar((float2)(particle.xSpeed, particle.ySpeed), normalVector);
            if (scalarProduct < 0) {  // Якщо частинка рухається всередину
                float2 newObjectSpeed = newSpeed((float2)(particle.xSpeed, particle.ySpeed), normalVector, scalarProduct);
                particle.xSpeed = newObjectSpeed.x * RESTITUTION;
                particle.ySpeed = newObjectSpeed.y * RESTITUTION;
            }
        }
    }

    particles[gid] = particle;
}
