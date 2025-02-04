
inline void particleWakeUp (Particle* particle) {
    particle->isSleep = 0;
    particle->sleepTimer = 0;
}

inline float2 normaToTangent (float2 normal) {
    return (float2)(-normal.y, normal.x);
}

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

    float2 particlePosition = (float2)(particle.x, particle.y);
    float2 boundaryPosition = (float2)(boundary.sphereX, boundary.sphereY);
    float2 cursorPosition = (float2)(cursor.cursorX, cursor.cursorY);

    float2 particleSpeed = (float2)(particle.xSpeed, particle.ySpeed);

    //перевірка зіштовхування з межами прямокутної зони
    if (particlePosition.x - particle.radius - boundary.borderThickness < 0) {
        particlePosition.x = particle.radius + boundary.borderThickness;

        float2 normalVector = (float2)(1, 0);
        float normalSpeed = dot(particleSpeed, normalVector);
        float2 normalComponent = normalSpeed * normalVector;

        float2 tangentVector = (float2)(-normalVector.y, normalVector.x);
        float tangentSpeed = dot(particleSpeed, tangentVector);
        float2 tangentComponent = tangentVector * tangentSpeed * pow(DAMPING, 1.f / 10.f);

        if (particleSpeed.x < 0) {
            normalComponent *= -RESTITUTION;
        }

        particleSpeed = normalComponent + tangentComponent;

//        particleWakeUp(&particle);
    } else if (particlePosition.x + particle.radius + boundary.borderThickness > boundary.width) {
        particlePosition.x = boundary.width - particle.radius - boundary.borderThickness;

        float2 normalVector = (float2)(-1, 0);
        float normalSpeed = dot(particleSpeed, normalVector);
        float2 normalComponent = normalSpeed * normalVector;

        float2 tangentVector = (float2)(-normalVector.y, normalVector.x);
        float tangentSpeed = dot(particleSpeed, tangentVector);
        float2 tangentComponent = tangentVector * tangentSpeed * pow(DAMPING, 1.f / 10.f);

        if (particleSpeed.x > 0) {
            normalComponent *= -RESTITUTION;
        }

        particleSpeed = normalComponent + tangentComponent;

//        particleWakeUp(&particle);
    }

    if (particlePosition.y - particle.radius - boundary.borderThickness < 0) {
        particlePosition.y = particle.radius + boundary.borderThickness;

        float2 normalVector = (float2)(0, -1);
        float normalSpeed = dot(particleSpeed, normalVector);
        float2 normalComponent = normalSpeed * normalVector;

        float2 tangentVector = (float2)(-normalVector.y, normalVector.x);
        float tangentSpeed = dot(particleSpeed, tangentVector);
        float2 tangentComponent = tangentVector * tangentSpeed * pow(DAMPING, 1.f / 10.f);

        if (particleSpeed.y < 0) {
            normalComponent *= -RESTITUTION;
        }

        particleSpeed = normalComponent + tangentComponent;

//        particleWakeUp(&particle);
    } else if (particlePosition.y + particle.radius + boundary.borderThickness > boundary.height) {
        particlePosition.y = boundary.height - particle.radius - boundary.borderThickness;

        float2 normalVector = (float2)(0, 1);
        float normalSpeed = dot(particleSpeed, normalVector);
        float2 normalComponent = normalSpeed * normalVector;

        float2 tangentVector = (float2)(-normalVector.y, normalVector.x);
        float tangentSpeed = dot(particleSpeed, tangentVector);
        float2 tangentComponent = tangentVector * tangentSpeed * pow(DAMPING, 1.f / 10.f);

        if (particleSpeed.y > 0) {
            normalComponent *= -RESTITUTION;
        }

        particleSpeed = normalComponent + tangentComponent;

//        particleWakeUp(&particle);
    }

    //перевірка зіштовхування з межами сферичної зони
    if (boundary.sphereRadius > 0) { // Якщо частинка намагається вийти за межі сфери
        float2 positionDifference = particlePosition - boundaryPosition;
        float distanceBetweenCenters = length(positionDifference);
        float maxDistance = boundary.sphereRadius - particle.radius;

        if(distanceBetweenCenters > maxDistance) {
            float2 normalVector = normalize(positionDifference);
            float overlap = distanceBetweenCenters - maxDistance;

            // Коеркція позиції
            particlePosition -= normalVector * overlap;

            // Корекція швидкості
            float normalSpeed = dot(particleSpeed, normalVector);
            float2 normalComponent = normalSpeed * normalVector;

            float2 tangentVector = (float2)(-normalVector.y, normalVector.x);
            float tangentSpeed = dot(particleSpeed, tangentVector);
            float2 tangentComponent = tangentVector * tangentSpeed * pow(DAMPING, 1.f / 10.f);

            if (normalSpeed > 0) {
                normalComponent *= -RESTITUTION;
            }

            particleSpeed = normalComponent + tangentComponent;

//            particleWakeUp(&particle);
        }
    }

    //перевірка зіштовхування з межами курсору зони
    if (cursor.cursorRadius > 0) {
        float2 positionDifference = particlePosition - cursorPosition;
        float distanceBetweenCenters = length(positionDifference);
        float minDistance = cursor.cursorRadius + particle.radius;

        if (distanceBetweenCenters < minDistance) {  // Якщо частинка намагається увійти всередину курсора
            float2 normalVector = normalize(positionDifference);
            float overlap = minDistance - distanceBetweenCenters;

            // Коеркція позиції
            particlePosition += normalVector * overlap;

            // Корекція швидкості
            float normalSpeed = dot(particleSpeed, normalVector);
            float2 normalComponent = normalSpeed * normalVector;

            float2 tangentVector = (float2)(-normalVector.y, normalVector.x);
            float tangentSpeed = dot(particleSpeed, tangentVector);
            float2 tangentComponent = tangentVector * tangentSpeed;

            if (normalSpeed < 0) {
                normalComponent *= -1;
            }

            particleSpeed = normalComponent + tangentComponent;

//            particleWakeUp(&particle);
        }
    }

    particle.xSpeed = particleSpeed.x;
    particle.ySpeed = particleSpeed.y;
    particle.x = particlePosition.x;
    particle.y = particlePosition.y;

    particles[gid] = particle;
}
