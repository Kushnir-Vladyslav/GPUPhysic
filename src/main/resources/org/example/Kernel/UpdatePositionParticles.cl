
inline float clampSpeed(float speed) {
    float absSpeed = fabs(speed);
    if (absSpeed > MAX_SPEED) {
        return (speed > 0) ? MAX_SPEED : -MAX_SPEED;
    }
    return speed;
}

inline void particleWakeUp (Particle* particle) {
    particle->isSleep = 0;
    particle->sleepTimer = 0;
}

__kernel void UpdatePositionParticles (
    __global    Particle*   particles,
    const       int         num_of_particles,
    const       float         time)
{
    int gid = get_global_id(0);
    if (gid >= num_of_particles) return;

    Particle particle = particles[gid];

    particle.ySpeed += GRAVITY * time;

    // Застосування демпфінгу
    particle.xSpeed *= 1 - (1 - DAMPING) * time;
    particle.ySpeed *= 1 - (1 - DAMPING) * time;

    // Обмеженя швидкісті
    particle.xSpeed = clampSpeed(particle.xSpeed);
    particle.ySpeed = clampSpeed(particle.ySpeed);

    if (particle.isSleep) {
        if (particle.xSpeed >= WAKE_VELOCITY_THRESHOLD ||
                particle.ySpeed >= WAKE_VELOCITY_THRESHOLD) {
            particleWakeUp (&particle);
        }
    } else {
        if (fabs(particle.xSpeed) <= MIN_SPEED &&
                   fabs(particle.ySpeed) <= MIN_SPEED) {
            particle.sleepTimer += time;
            if (particle.sleepTimer >= SLEEP_TIME_THRESHOLD) {
                particle.isSleep = 1;
                particle.xSpeed = 0;
                particle.ySpeed = 0;
            }
        } else {
            particle.sleepTimer = 0;
        }
    }

    if (!particle.isSleep) {


        particle.x += particle.xSpeed * time;
        particle.y += particle.ySpeed * time;
    }

    particles[gid] = particle;
}

