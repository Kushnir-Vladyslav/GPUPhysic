
float atomic_add_float(__global float* addr, float val) {
    union {
        unsigned int u32;
        float f32;
    } next, expected, current;
    current.f32 = *addr;
    do {
        expected.f32 = current.f32;
        next.f32 = expected.f32 + val;
        current.u32 = atomic_cmpxchg((__global volatile unsigned int*)addr,
                                    expected.u32, next.u32);
    } while (current.u32 != expected.u32);
    return current.f32;
}

inline void particleWakeUp (__global Particle* particle) {
    particle->isSleep = 0;
    particle->sleepTimer = 0;
}

__kernel void PhysicCalculation(
    __global    Particle*   particles,
    const       int         num_of_particles)
{
    int gid_x = get_global_id(0);
    int gid_y = get_global_id(1);

    if (gid_x >= num_of_particles ||
        gid_y >= num_of_particles ||
        gid_y <= gid_x) {
        return;
    }

    Particle mainParticle = particles[gid_x];
    Particle secondParticle = particles[gid_y];

    float2 mainPos = (float2) (mainParticle.x, mainParticle.y);
    float2 secondPos = (float2) (secondParticle.x, secondParticle.y);

    float2 positionDifference = mainPos - secondPos;
    float distanceBetweenCenters = length(positionDifference);

    float minDist = mainParticle.radius + secondParticle.radius;

    if (distanceBetweenCenters <= minDist) {
        float2 normalVector = normalize(positionDifference);
        float overlap = minDist - distanceBetweenCenters;

        // Корекція позицій
        float correctionFactor = overlap * 0.5f;
        atomic_add_float(&particles[gid_y].x, -normalVector.x * correctionFactor);
        atomic_add_float(&particles[gid_y].y, -normalVector.y * correctionFactor);

        atomic_add_float(&particles[gid_x].x, normalVector.x * correctionFactor);
        atomic_add_float(&particles[gid_x].y, normalVector.y * correctionFactor);

        // Корекція швидкостей
        float2 relativeVelocity = (float2)(
            mainParticle.xSpeed - secondParticle.xSpeed,
            mainParticle.ySpeed - secondParticle.ySpeed
        );
        float velAlongNormal = dot(relativeVelocity, normalVector);

        if (velAlongNormal < 0) {
            float mainMass = mainParticle.radius;
            float secondMass = secondParticle.radius;
            float totalMass = mainParticle.radius + secondParticle.radius;

            float impulseScalar = -(1.0f + RESTITUTION) * velAlongNormal / totalMass;
            float2 impulse =  normalVector * impulseScalar;

            atomic_add_float(&particles[gid_x].xSpeed, impulse.x * DAMPING * secondMass );
            atomic_add_float(&particles[gid_x].ySpeed, impulse.y * DAMPING * secondMass );

            atomic_add_float(&particles[gid_y].xSpeed, -impulse.x * DAMPING * mainMass );
            atomic_add_float(&particles[gid_y].ySpeed, -impulse.y * DAMPING * mainMass );
        }

    }
}

